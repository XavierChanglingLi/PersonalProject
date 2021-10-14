#include "MyFilter.h"
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

int arraylength = 15;
float win[15];
float hearbeat = 0;
long startTime = micros();
int beatCount =0;
int heartRate;
float lastaverage=0;
MovingAverage movingAverage = MovingAverage(arraylength,win);
FilterOnePole highpass = FilterOnePole(HIGHPASS,2.5);
FilterOnePole lowpass= FilterOnePole(LOWPASS, 1);
void loop() {
  // put your main code here, to run repeatedly:
  int input;
  float average;
  float after_low;
  float after_high;
  long currentTime = micros();
  input = analogRead(A0)*100;
  after_low = lowpass.update(input);
  after_high = highpass.update(after_low);
  average = movingAverage.update(after_high,win);

  if((average*lastaverage)<0){
    beatCount++;
    }
  if(lastaverage==0 && average!=0){
    beatCount++;
    }
  if(lastaverage!=0 & &average==0){
    beatCount++;
    }
//  if(currentTime-startTime==120000000){
//    heartRate = beatCount/2;
//    startTime = currentTime;
//    Serial.print("The heart rate is");Serial.print(heartRate);Serial.print("/min");Serial.print(" ");
//    }
  lastaverage = average;
  long timedifference = (currentTime-startTime)/1000000;  //time difference in seconds
  heartRate = beatCount/timedifference*60; //heart rate per min
  if(timedifference>10){
    beatCount=0;
    startTime=currentTime;
    }

//  Serial.print("heartRate: "); Serial.print(heartRate); Serial.print("  ");
  Serial.print("average: "); Serial.print(average); Serial.print("  ");
  Serial.println("uT");

//  Serial.print("The heart rate is");Serial.print(heartRate);Serial.print("/min");Serial.print(" ");
//  Serial.println(average);
//  Serial.println(heartRate);
}
