#ifndef MyFilter_h
#define MyFilter_h

#include <Arduino.h>

enum FILTER_TYPE {
  HIGHPASS,
  LOWPASS
};

struct MovingAverage {
  float total;
  int arrayLength;
  int index;
  int full;

  //constructor
  //define the window array inside your .ino file
  MovingAverage(int len, float win[]);

  //update the window with the input value and return the average
  float update( float inVal, float win[] );
};


// the filter implements a recursive filter (low pass / highpass)
// note that this must be updated in a loop, using the most recent acquired values and the time acquired
struct FilterOnePole {
  FILTER_TYPE FT;
  float TauUS;       // decay constant of the filter, in US (micro seconds)
  float TauSamps;    // tau, measured in samples (this changes, depending on how long between input()s

  // filter values - these are public, but should not be set externally
  float Y;       // most recent output value (gets computed on update)
  float Ylast;   // prevous output value

  float X;      // most recent input value

  // elapsed times are kept in long, and will wrap every
  // 35 mins, 47 seconds ... however, the wrap does not matter,
  // because the delta will still be correct (always positive and small)
  float ElapsedUS;   // time since last update in US (micro seconds)
  long LastUS;       // last time measured in US (micro seconds)

  //constructor
  FilterOnePole( FILTER_TYPE ft=LOWPASS, float cutOffFrequency=1.0, float initialValue=0 );

  //update the filter with the current input value
  //return the filtered value
  float update( float inVal );

  //called in update, return different output value dependent on the filter_type
  float output();
  
  void print();

  void test();
};



#endif
