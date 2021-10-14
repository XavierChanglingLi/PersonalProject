#include <Arduino.h>
#include <SPI.h>
#include "Adafruit_BLE.h"
#include "Adafruit_BluefruitLE_SPI.h"
#include "Adafruit_BluefruitLE_UART.h"
#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_LSM303DLH_Mag.h>
#include <Wire.h>

#include "BluefruitConfig.h"

#include "Adafruit_APDS9960.h"

#if SOFTWARE_SERIAL_AVAILABLE
  #include <SoftwareSerial.h>
#endif
    #define FACTORYRESET_ENABLE         0
    #define MINIMUM_FIRMWARE_VERSION    "0.6.6"
/*=========================================================================*/


Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_CS, BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);
Adafruit_APDS9960 apds;

//variable for microphone
int peakToPeak;
volatile int minimum=1023;
volatile int maximum = 0;
volatile int count = 0;
int on=0;

//variable for gesture sensor
bool rightSwipe = false;
bool leftSwipe = false;
int swipeResetCounter = 0;
int swipeResetThreshold = 80;
int wave = 0;

// A small helper
void error(const __FlashStringHelper*err) {
  Serial.println(err);
  while (1);
}


void setup(void)
{

  while (!Serial);  // required for Flora & Micro
  delay(500);

  /*set up microphone*/
  Serial.begin(115200);
  const byte interruptPin = 3;
  attachInterrupt(digitalPinToInterrupt(interruptPin),stop,RISING);
  TCCR1A |= (1<<WGM12);
  TCCR1B = (TCCR1B & 0b11111000) | (1<<CS10) | (1<<CS11);
  OCR1A = 249;//hard code it 
  TIMSK1 |= (1<<OCIE1A);
  
  Serial.println(F("Adafruit Bluefruit HID Keyboard Example"));
  Serial.println(F("---------------------------------------"));

  /* Initialise the module */
  Serial.print(F("Initialising the Bluefruit LE module: "));

  if ( !ble.begin(VERBOSE_MODE) )
  {
    error(F("Couldn't find Bluefruit, make sure it's in CoMmanD mode & check wiring?"));
  }
  Serial.println( F("OK!") );

  if ( FACTORYRESET_ENABLE )
  {
    /* Perform a factory reset to make sure everything is in a known state */
    Serial.println(F("Performing a factory reset: "));
    if ( ! ble.factoryReset() ){
      error(F("Couldn't factory reset"));
    }
  }

  /* Disable command echo from Bluefruit */
  ble.echo(false);

  Serial.println("Requesting Bluefruit info:");
  /* Print Bluefruit information */
  ble.info();

  /* Change the device name to make it easier to find */
  Serial.println(F("Setting device name to '705178_microcontroller': "));
  if (! ble.sendCommandCheckOK(F( "AT+GAPDEVNAME=705178_microcontroller" )) ) {
    error(F("Could not set device name?"));
  }

  /* Enable HID Service */
  Serial.println(F("Enable HID Service (including Keyboard): "));
  if ( ble.isVersionAtLeast(MINIMUM_FIRMWARE_VERSION) )
  {
    if ( !ble.sendCommandCheckOK(F( "AT+BleHIDEn=On" ))) {
      error(F("Could not enable Keyboard"));
    }
  }else
  {
    if (! ble.sendCommandCheckOK(F( "AT+BleKeyboardEn=On"  ))) {
      error(F("Could not enable Keyboard"));
    }
  }

  /* Add or remove service requires a reset */
  Serial.println(F("Performing a SW reset (service changes require a reset): "));
  if (! ble.reset() ) {
    error(F("Couldn't reset??"));
  }

  Serial.println();
  Serial.println(F("Go to your phone's Bluetooth settings to pair your device"));
  Serial.println(F("then open an application that accepts keyboard input"));

  Serial.println();
  Serial.println(F("Enter the character(s) to send:"));
  Serial.println(F("- \\r for Enter"));
  Serial.println(F("- \\n for newline"));
  Serial.println(F("- \\t for tab"));
  Serial.println(F("- \\b for backspace"));

  Serial.println();

  /*set up the gesture sensor*/
  if(!apds.begin()){
    Serial.println("failed to initialize device! Please check your wiring.");
  }
  else Serial.println("Device initialized!");

  //gesture mode will be entered once proximity mode senses something close
  apds.enableProximity(true);
  apds.enableGesture(true);
  
}


void loop(void)
{

  Serial.print(F("keyboard > "));

   //gesture detection
  uint8_t gesture = apds.readGesture();
  if(leftSwipe||rightSwipe){
    swipeResetCounter++;
    if(swipeResetCounter==swipeResetThreshold){
      resetSwipes();
    }
  }
  /*power up*/
  if(on==1){
    if(gesture==APDS9960_LEFT){
      leftSwipe = true;
      wave=1;
      if(rightSwipe==true){
        wave=1;
        resetSwipes();
      }
    }
    if(gesture==APDS9960_RIGHT){
      rightSwipe=true;
      wave=1;
      Serial.println(wave);
      if(leftSwipe==true){
        wave=1;
        resetSwipes();
      }
    }
    ble.print("AT+BleKeyboard=");
    ble.print(wave);   
    //volume
    ble.print(",");
    ble.print(peakToPeak);
    ble.println("\\n");
  }

  if( ble.waitForOK() )
  {
    Serial.println( F("OK!") );
  }else
  {
    Serial.println( F("FAILED!") );
  }
  wave=0;
  delay(100);
}

void stop(){
  if(on==0){
    on=1;}
  else{
    on=0;}
}

ISR(TIMER1_COMPA_vect){
/*read from a0, check the reading range whether it is bigger than max(1023) or min(0). 
 * initially set max =0, min = 1023, keep a count of 128, calculate peak to peak, reset */
  int var = analogRead(A0);
  if(var>=maximum&&var<=1023){
    maximum=var;}
  if(var<=minimum&&var>=0){
    minimum=var;}
  count+=1;
  if(count==127){
    peakToPeak=maximum-minimum;
    count = 0;
    maximum = 0;
    minimum= 1023;}
  }

void resetSwipes(){
  leftSwipe = false;
  rightSwipe = false;
  }
