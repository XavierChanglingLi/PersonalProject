#include "Waveforms.h"

OscillatorState oscillatorstate = OscillatorState(0);

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
//  Serial.println(value);
  Serial.println(value);
  oscillatorstate.set_phaseStep(checkTone(value), 125);
}


/*check the output and decide which tone to return*/
float checkTone(int out){
  if(out>=600 && out<=610){
    return NOTE_A5;}
  if(out>=505 && out<=515){
    return NOTE_B5;}
  if(out>=310 && out<=320){
    return NOTE_C6;}
  if(out>=175 && out<=185){
    return NOTE_D6;}
  if(out>=125 && out<=135){
    return NOTE_E6;}
  if(out>=90 && out<=100){
    return NOTE_F6;}
  if(out>=45 && out<=55){
    return NOTE_G6;}
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
  OCR1A = getByteLevel(oscillatorstate.update());
}
