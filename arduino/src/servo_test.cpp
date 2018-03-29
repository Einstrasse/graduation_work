#include <Servo.h>
// #include <SoftwareSerial.h>

// SoftwareSerial
Servo servo;

void setup() {
  turn(180);
  Serial.begin(9600); //시리얼 콘솔의 baud rate를 9600으로 설정
  
}

void loop() {
  if (Serial.available()) {
    int angle = Serial.parseInt();
    if (angle >= 0 && angle <= 180) {
      turn(angle);  
    }
  }
}

void turn(int angle) {
  servo.attach(9);
  servo.write(angle);
  delay(1000);
  servo.detach();
}