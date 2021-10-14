

String sig;
int motor;
int vol;

int motorPin = 3;

int bluePin = 5;
int greenPin = 6;
int redPin = 9;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

}

void loop() {
  // put your main code here, to run repeatedly:
  sig = Serial.readString();
  motor = sig[0] - '0';
  vol = sig.substring(2).toInt();
  if(vol > 1023){
    vol = 1023;
  }
   
  //Serial.println(motor);

  if (motor==1) {
    analogWrite(motorPin, 1023);
    delay(1000);
    analogWrite(motorPin, 0);
  }

  Serial.println(vol);
  if(vol > 0){
    if(vol <= 256){
        analogWrite(redPin, 0);
        analogWrite(greenPin, vol);
        analogWrite(bluePin, 256-vol);
      } else if (vol <= 512){
        analogWrite(redPin, vol-256);
        analogWrite(greenPin, map(vol, 257, 512, 255, 128) );
        analogWrite(bluePin, 0);
        
      } else if (vol <= 768){
        analogWrite(redPin, 255);
        analogWrite(greenPin, map(vol, 512, 768, 128, 0) );
        analogWrite(bluePin, 0);
      } else {
        analogWrite(redPin, 639 - vol/2);
        analogWrite(greenPin, 0);
        analogWrite(bluePin, vol - 768);
      }
  }
  else{
    analogWrite(redPin, 0);
    analogWrite(greenPin, 0);
    analogWrite(bluePin, 0);
    }
}
