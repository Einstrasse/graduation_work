#include <SoftwareSerial.h>
#include <Servo.h>
SoftwareSerial mySerial(2, 3); // RX, TX
Servo servo;

void setup() {
  // Open serial communications and wait for port to open:
	// turn(90);
  // Serial.begin(9600);
  // while (!Serial) {
  //   ; // wait for serial port to connect. Needed for native USB port only
  // }


  // Serial.println("Goodnight moon!");

  // // set the data rate for the SoftwareSerial port
  mySerial.begin(9600);
  // mySerial.println("Hello, world?");
}

void loop() { // run over and over
	if (mySerial.available()) {
	int angle = mySerial.parseInt();
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