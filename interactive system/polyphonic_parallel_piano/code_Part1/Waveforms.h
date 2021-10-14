#ifndef Waveforms_h
#define Waveforms_h

#include <Arduino.h>
const int maxSamplesNum = 256;
//a constant sine wave table with 256 chars allocated to a 256 byte boundary
const signed char waveformsTable[maxSamplesNum] __attribute__((aligned(256)))={
0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45, 48, 51, 54, 57, 59, 62, 65, 67, 70,
73, 75, 78, 80, 82, 85, 87, 89, 91, 94, 96, 98, 100, 102, 103, 105, 107, 108, 110, 112, 113, 114, 116, 117, 118,
119, 120, 121, 122, 123, 123, 124, 125, 125, 126, 126, 126, 126, 126, 126, 127, 126, 126, 126, 126, 125, 125, 124,
123, 123, 122, 121, 120, 119, 118, 117, 116, 114, 113, 112, 110, 108, 107, 105, 103, 102, 100, 98, 96, 94, 91, 89,
87, 85, 82, 80, 78, 75, 73, 70, 67, 65, 62, 59, 57, 54, 51, 48, 45, 42, 39, 36, 33, 30, 27, 24, 21, 18, 15, 12, 9, 
6, 3, 0, -3, -6, -9, -12, -15, -18, -21, -24, -27, -30, -33, -36, -39, -42, -45, -48, -51, -54, -57, -59, -62, -65, -67,
-70, -73, -75, -78, -80, -82, -85, -87, -89, -91, -94, -96, -98, -100, -102, -103, -105, -107, -108, -110, -112, -113, -114,
-116, -117, -118, -119, -120, -121, -122, -123, -123, -124, -125, -125, -126, -126, -126, -126, -126, -126, -126, -126, -126,
-126, -126, -125, -125, -124, -123, -123, -122, -121, -120, -119, -118, -117, -116, -114, -113, -112, -110, -108, -107, -105,
-103, -102, -100, -98, -96, -94, -91, -89, -87, -85, -82, -80, -78, -75, -73, -70, -67, -65, -62, -59, -57, -54, -51, -48, -45,
-42, -39, -36, -33, -30, -27, -24, -21, -18, -15, -12, -9, -6, 0};

//enum with the note for each frequency
enum note{NOTE_A5=880, NOTE_B5=988, NOTE_C6=1047, NOTE_D6=1175, NOTE_E6=1319, NOTE_F6=1397, NOTE_G6=1568};
//oscillator state struct
struct OscillatorState {
  /*
  volume: the volume multiplied for the oscillator
  phase_step: keep track of the phase step, frequency of the oscillator
  accumulated_phase: keep track of the accumulated phase, which step through oscillator
  */
  char volume;
  unsigned int phase_step;
  unsigned int accumulated_phase;

  //construtor header
  OscillatorState(float frequency);

  //coverts from frequency to phase step
  void set_phaseStep(float frequency, int vol);

  //gets the current value of the oscillator multiplied by the oscillator's volume
  int update();
  };
#endif /* Waveforms_h */
