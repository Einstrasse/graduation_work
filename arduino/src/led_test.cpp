void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600); //시리얼 콘솔의 baud rate를 9600으로 설정
  Serial.println("Arduino Set up!");
  pinMode(13, OUTPUT); //13번 핀을 출력용으로 설정
  pinMode(2, INPUT); //2번 핀을 입력용으로 설정
}
bool led_order = false;
bool led_state = false;
void loop() {
  // put your main code here, to run repeatedly:
  if (Serial.available()) {
    char ch = Serial.read();
    Serial.println(ch);
    if (ch == '1') {
      led_order = true;
    } else if (ch == '0') {
      led_order = false;
    }
  }
  if (led_order != led_state) {
    if (led_order) {
      digitalWrite(13, HIGH);    
    } else {
      digitalWrite(13, LOW);
    }
    led_state = led_order;
  }
  delay(1000);
}