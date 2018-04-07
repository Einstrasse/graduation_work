# 아두이노 작업

## 1. 아두이노에 서보모터 연결
[참고 유튜브 강좌](https://www.youtube.com/watch?v=h8GRTrGia8c)    
[참고 회로도](https://youtu.be/h8GRTrGia8c?t=2m14s)    


서보모터 스펙
- 갈색 	: Ground
- 빨간색  : VCC(5Volt)
- 주황색	: digital pin

서보모터의 가동범위는 0~180도이다.

## 2. 라즈베리파이와 아두이노의 시리얼 통신
다양한 방법이 있다. UART(Serial)통신, SoftwareSerial을 이용한 통신 I2C를 이용한 통신, SPI를 이용한 통신 등이 있다.    
[참고](http://www.hardcopyworld.com/ngine/aduino/index.php/archives/360)    
이러한 방법 중에서, 지정된 핀이 아닌 핀으로 통신하기 위해 SoftwareSerial을 이용하도록 한다.    
[아두이노 SoftwareSerial Reference](https://www.arduino.cc/en/Reference/SoftwareSerial)    

그리고 라즈베리파이(3.3V)와 아두이노(5V)의 전압 차이를 호환시켜주기 위해 로직 레벨 컨버터를 이용한다.    
[로직레벨 컨버터 그림 레퍼런스](http://hinco.tistory.com/4)    

[라즈베리파이2 Pin Map 레퍼런스](https://www.raspberrypi.org/documentation/usage/gpio-plus-and-raspi2/README.md)    


라즈베리파이 핀 맵에 대한 자세한 정보는, 라즈베리파이 쉘에서 다음 명령어를 통해 확인할 수 있다.
```
gpio readall
```
BCM 번호가 위의 Pin Map 레퍼런스에 나타나는 핀 번호이고, Physical 은 해당 판때기를 반시계방향으로 90도 눕히면 실제 물리적으로 위치된 레이아웃과 일치되게 된다. Tx, Rx가 Physical으로는 8번 10번이지만 BCM 번호는 14번 15번이다.

아두이노 Uno에서 Interrupt를 사용할 수 있는 핀은 2,3번이라고 한다. 해당 핀을 이용해서 SoftwareSerial을 만드는것이 좋을 듯 하다.

sw_serial_test.cpp 소스코드를 아두이노에서 동작시키고, 2번핀(아두이노의 RX)는 로직레벨 컨버터의 TX에 연결한다. 3번핀(아두이노의 TX)는 로직레벨 컨버터의 RX에 연결한다.
반대로 라즈베리파이의 경우 RX를 로직레벨 컨버터의 RX에 연결하고, TX를 로직레벨 컨버터의 TX에 연결한다. RX와 TX를 서로 교차되게 연결해야지 통신이 정상적으로 된다. 그리고 BaudRate는 9600으로 통일한다.


완성된 아두이노 코드는 src/main.cpp에 있다. 해당 코드를 아두이노 보드에 올리면 된다. GPIO(2,3번 핀)에서 받은 데이터를 토대로 서보모터의 각도를 제어하는 코드이다.

## 3. 서보모터를 통한 전등 스위치 제어
각도에 따른 전등의 On/Off에 대한 실험 결과이다.

각도 85~99 : 전등 On    
각도 100~103 : 애매한 상태    
각도 104 ~ 110 : 전등 Off    

위 범위 밖의 각도는 서보모터 파손의 위험이 있어서 하지 않는 것을 권장한다.
기본 값으로는 다음과 같이 정의한다.    

##### 전등 On = 각도 88    
##### 전등 Off = 각도 105