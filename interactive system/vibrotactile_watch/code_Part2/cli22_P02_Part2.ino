#include "Waveforms.h"
#include <Arduino.h>
const int interruptPin = 3;
int state = 0;
long oneHzSample = 1000000/maxSamplesNum;
//current time in seconds
long startTime = 32760;
long time_elapsed;
int timeResult[4];
int hours;
int minutes;
int seconds;
int var;
int mapped;


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(5, OUTPUT);
  pinMode(interruptPin, INPUT_PULLUP);
  attachInterrupt(digitalPinToInterrupt(interruptPin),buttonPressed,RISING);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(state==1){
    state=displayTime(state);}

    
  time_elapsed = millis()/1000;

  long currentTime;
  currentTime= startTime+time_elapsed;
  //convert the time into hours:minutes:seconds
  hours = currentTime/3600;
  minutes = (currentTime - (currentTime/3600)*3600)/60;
  seconds = currentTime - (currentTime/3600)*3600 - ((currentTime - (currentTime/3600)*3600)/60)*60;
  convert(currentTime);
//  Serial.print("Hour: "); Serial.print(timeResult[0]);Serial.print(timeResult[1]); Serial.print(" ");
//  Serial.print("Minute: "); Serial.print(timeResult[2]);Serial.print(timeResult[3]);Serial.print(" ");
//  Serial.print("Second: "); Serial.print(seconds); Serial.print(" ");
//  Serial.println();
  delayMicroseconds(map(analogRead(A0),0,1023,0,oneHzSample));
}

void buttonPressed(){
  state = 1;
}


void convert(int currentTime){
  int tens;
  int rest;
  if(hours>=10){
    tens = hours/10;
    rest = hours - (hours/10)*10;
    timeResult[0]=tens;
    timeResult[1]=rest;
    }
  else{
    timeResult[0]=0;
    timeResult[1]=hours;
    }
  if(minutes>=10){
    tens = minutes/10;
    rest = minutes - (minutes/10)*10;
    timeResult[2]=tens;
    timeResult[3]=rest;
    }
  else{
    timeResult[2]=0;
    timeResult[3]=minutes;
    }
}

//n: the number of vibration circle
//waveForm: the wave type to be used
//wait: dealy time after each circle of vibration
void tick(int n, int waveForm, int wait){
  for(int m=0; m<n; m++){
    for(int i=0; i<maxSamplesNum; i++){
      Serial.println(waveformsTable[maxSamplesNum*waveForm+i]);
      analogWrite(5, waveformsTable[maxSamplesNum*waveForm+i]);}
    delayMicroseconds(wait);
  }
  analogWrite(5,LOW);
}
  
int displayTime(int st){
  if(st==1){
    int i;
    //loop through the time result list to let the motor vibrate 
    for(i=0; i<4; i++){
      if(i<2){
        if(timeResult[i]==0){
          tick(1, 0, oneHzSample);
          }
        else{
          tick(timeResult[i], 2, map(analogRead(A0),0,1023,0,oneHzSample));
          }
        } 
      else{
        if(timeResult[i]==0){
            tick(1, 0, oneHzSample);
          }
        else{
          tick(timeResult[i], 1, map(analogRead(A0),0,1023,0,oneHzSample));
          }
        }
      delay(500);
      }
    }
  return 0;
}
