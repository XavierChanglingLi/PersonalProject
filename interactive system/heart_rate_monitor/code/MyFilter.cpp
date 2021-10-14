//
//  MyFilter.cpp
//  
//
//  Created by Changling Li on 9/8/20.
//

#include <stdio.h>
#include <math.h>
#include "MyFilter.h"



MovingAverage::MovingAverage(int len, float win[]){
  index = 0;
  arrayLength = len;
  total = 0;
  win[arrayLength] = {};
  full = 0;
}

  //update the window with the input value and return the average
float MovingAverage::update( float inVal, float win[] ){
  float average;
  if(full!=(arrayLength-1) && index<arrayLength){
    full++;
    win[index]=inVal;
    total += inVal;
    average = total/index;
    }
  else{
    full = arrayLength-1;
    if(index>arrayLength-1){
      index=0;
      }
      total -= win[index];
      win[index]=inVal;
      total += win[index];
      average = total/arrayLength;
    }
  index++;
  return average;
}


// the filter implements a recursive filter (low pass / highpass)
// note that this must be updated in a loop, using the most recent acquired values and the time acquired
FilterOnePole::FilterOnePole( FILTER_TYPE ft=LOWPASS, float cutOffFrequency=1.0, float initialValue=0 ){
  TauUS = 1/(2*PI*cutOffFrequency)*1000000;
  FT = ft;
  LastUS = micros();
  Ylast = 0;
  Y=0;
  X=0;
  }

  //update the filter with the current input value
  //return the filtered value
float FilterOnePole::update( float inVal ){
  float a,a1,b;
  long currentUS = micros();
  ElapsedUS = (currentUS-LastUS);
  LastUS = currentUS;
  TauSamps = TauUS/ElapsedUS;
  float ampfactor = exp(-1/TauSamps);
  if(FT==LOWPASS){
    a = 1-ampfactor;
    a1=0;
    b = ampfactor;
    }
  else{
    a = (1+ampfactor)/2;
    a1 = -(1+ampfactor)/2;
    b = ampfactor;
    }
  Y = a*inVal+a1*X+b*Ylast;
  Ylast = Y;
  X=inVal;
  return Y;
}

  //called in update, return different output value dependent on the filter_type
float FilterOnePole::output();
void FilterOnePole::print();
void FilterOnePole::test();
