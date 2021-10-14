#ifndef mouseControl_h
#define mouseControl_h


/*method to filter and smooth the data*/
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

/*linear interpolation, take a value from one range and map it to a new value inside another range*/
float interpolation(float x, float x0, float x1, float y0, float y1);
#endif
