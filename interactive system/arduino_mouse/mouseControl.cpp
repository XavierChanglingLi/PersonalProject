#include "mouseControl.h"

/*method to filter and smooth the data*/
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

/*linear interpolation, take a value from one range and map it to a new value inside another range*/
float interpolation(float x, float x0, float x1, float y0, float y1){
  float y;  
  y = (y1-y0)/(x1-x0)*(x-x0)+y0;
  return y;
}
