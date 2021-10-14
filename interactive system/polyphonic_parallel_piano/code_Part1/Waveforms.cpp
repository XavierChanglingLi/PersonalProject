#include "Waveforms.h"
#include <limits.h>

volatile char* curWave= waveformsTable;
float TOP = 31372.549;

//initializes the phase step using set_phaseStep, volume and phase accumulation
OscillatorState::OscillatorState(float frequency){
  volume = 0;
  accumulated_phase = 0;
  set_phaseStep(frequency, volume);
  }

/*sets the phase step to be the frequency multiplied by the max value
phase step can be, divided by the timer's frequency*/
void OscillatorState::set_phaseStep(float frequency, int vol){
  phase_step = frequency*UINT_MAX/TOP;
  volume = vol;
  }

/*adds the phase step to the phase accumulator and returns the current value
of the oscillator multiplied by the oscillator's volume. To find the current
value of the oscillator, we can use "high byte" of the phase accumulator as the
current sample index, which is the equivalent of dividing the value by 256*/
int OscillatorState::update(){
  accumulated_phase +=phase_step;
  return (curWave[accumulated_phase>>8])*volume;
  }
  
