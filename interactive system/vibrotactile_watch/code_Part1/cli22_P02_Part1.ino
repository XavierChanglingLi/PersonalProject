#include "Waveforms.h"
volatile byte state = LOW;
long oneHzSample = 1000000/maxSamplesNum;
int index = 0;
int waveForm =0;
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  const byte interruptPin = 3;
  attachInterrupt(digitalPinToInterrupt(interruptPin),stop,RISING);
}

void loop() {
  // put your main code here, to run repeatedly:
  int var;
  int mapped;
  var = analogRead(A0);
  mapped = map(var,0,1023,0,oneHzSample);
  if(waveForm==0){
    analogWrite(5,waveformsTable[index]);
    Serial.println(waveformsTable[index]);
    }
  if(waveForm==1){
    analogWrite(5,waveformsTable[index+maxSamplesNum]);
    Serial.println(waveformsTable[index+maxSamplesNum]);
    }
  if(waveForm==2){
    analogWrite(5,waveformsTable[index+maxSamplesNum*2]);
    Serial.println(waveformsTable[index+maxSamplesNum*2]);
    }
  if(waveForm==3){
    analogWrite(5,waveformsTable[index+maxSamplesNum*3]);
    Serial.println(waveformsTable[index+maxSamplesNum*3]);
    }
  index+=1;
  index = index%maxSamplesNum;
  delayMicroseconds(mapped);
}


void stop(){
  index=0;
  state = !state;
  waveForm += 1;
  waveForm = waveForm%4;
  }
