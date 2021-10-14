#include "Waveforms.h"

OscillatorState oscillatorstate1 = OscillatorState(0);
OscillatorState oscillatorstate2 = OscillatorState(0);


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  //PIN 9 is the output
  DDRB = (1<<DDB1);
  TCCR1B = (TCCR1B & 0b11111000) | (1<<CS10);
  TCCR1A |= (1<<COM1A1);
  TCCR1A |= (1<<WGM10);
  TIMSK1 |= (1<<TOIE1);
}

void loop() {
/*put your main code here, to run repeatedly:
update oscillator's volume and phase step dependent on the button
pressed instead of tone()*/
  int value = analogRead(A0);
  Serial.println(value);
  int t1,t2 = checkTone(value);
  oscillatorstate1.set_phaseStep(t1,63);
  oscillatorstate2.set_phaseStep(t2,63);
}

/*receive a value from wavetable and return the modified value as an 
inline byte*/
float checkTone(int out){
  if(out>=600 && out<=610){
    return NOTE_A5, NOTE_A5;}
  if(out>=505 && out<=515){
    return NOTE_B5, NOTE_B5;}
  if(out>=314 && out<=324){
    return NOTE_C6, NOTE_C6;}
  if(out>=175 && out<=185){
    return NOTE_D6, NOTE_D6;}
  if(out>=125 && out<=135){
    return NOTE_E6, NOTE_E6;}
  if(out>=90 && out<=100){
    return NOTE_F6, NOTE_F6;}
  if(out>=45 && out<=55){
    return NOTE_G6, NOTE_G6;}
    
  if(out>=720 && out<=730){
    Serial.println("NOTE_A5, NOTE_B5");
    return NOTE_A5, NOTE_B5;}
  if(out>=665 && out<=675){
    Serial.println("NOTE_A5, NOTE_C6");
    return NOTE_A5, NOTE_C6;}
  if(out>=635 && out<=645){
    Serial.println("NOTE_A5, NOTE_D6");
    return NOTE_A5, NOTE_D6;}
  if(out>=625 && out<=634){
    Serial.println("NOTE_A5, NOTE_E6");
    return NOTE_A5, NOTE_E6;}
  if(out>=615 && out<=624){
    Serial.println("NOTE_A5, NOTE_F6");
    return NOTE_A5, NOTE_F6;}
  if(out>=605 && out<=614){
    Serial.println("NOTE_A5, NOTE_G6");
    return NOTE_A5, NOTE_G6;}
    
  if(out>=590 && out<=599){
    Serial.println("NOTE_B5, NOTE_C6");
    return NOTE_B5, NOTE_C6;}
  if(out>=555 && out<=565){
    Serial.println("NOTE_B5, NOTE_D6");
    return NOTE_B5, NOTE_D6;}
  if(out>=540 && out<=550){
    Serial.println("NOTE_B5, NOTE_E6");
    return NOTE_B5, NOTE_E6;}
  if(out>=530 && out<=539){
    Serial.println("NOTE_B5, NOTE_F6");
    return NOTE_B5, NOTE_F6;} 
  if(out>=516 && out<=525){
    Serial.println("NOTE_B5, NOTE_G6");
    return NOTE_B5, NOTE_G6;}  
     
  if(out>=400 && out<=410){
    Serial.println("NOTE_C6, NOTE_D6");
    return NOTE_C6, NOTE_D6;}
  if(out>=375 && out<=385){
    Serial.println("NOTE_C6, NOTE_E6");
    return NOTE_C6, NOTE_E6;}
  if(out>=360 && out<=370){
    Serial.println("NOTE_C6, NOTE_F6");
    return NOTE_C6, NOTE_F6;}
  if(out>=335 && out<=345){
    Serial.println("NOTE_C6, NOTE_G6");
    return NOTE_C6, NOTE_G6;} 
    
  if(out>=265 && out<=275){
    Serial.println("NOTE_D6, NOTE_E6");
    return NOTE_D6, NOTE_E6;}
  if(out>=240 && out<=250){
    Serial.println("NOTE_D6, NOTE_F6");
    return NOTE_D6, NOTE_F6;}
  if(out>=210 && out<=220){
    Serial.println("NOTE_D6, NOTE_G6");
    return NOTE_D6, NOTE_G6;}
    
  if(out>=200 && out<=209){
    Serial.println("NOTE_E6, NOTE_F6");
    return NOTE_E6, NOTE_F6;} 
  if(out>=160 && out<=170){
    Serial.println("NOTE_E6, NOTE_G6");
    return NOTE_E6, NOTE_G6;}
    
  if(out>=136 && out<=145){
    Serial.println("NOTE_F6, NOTE_G6");
    return NOTE_F6, NOTE_G6;}
 
    
  return 0;
 }

 /*receive a value from wavetable and return the modified value as an 
inline byte*/
inline byte getByteLevel(int waveValue){
  int returnValue = (waveValue>>7) + 128;
  return byte(returnValue);
}

/*interrupt when overflow*/
ISR(TIMER1_OVF_vect){
//  Serial.println(oscillatorstate.update());
  OCR1A = getByteLevel(oscillatorstate1.update() + oscillatorstate2.update());
}
