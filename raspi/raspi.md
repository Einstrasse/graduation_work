# 라즈베리 파이 작업

## 1. 아두이노와 GPIO 시리얼 통신

일단 라즈베리파이의 GPIO핀을 확인하는 방법과 사용법에 대해 알아야 한다.
라즈베리파이의 GPIO핀을 확인하는 방법은 터미널에서 다음명령어를 통해 가능하다.

```
gpio readall
```

physical 핀 번호를 반시계방향으로 90도 돌리면 실제 라즈베리파이를 정면으로 두었을때의 핀 레이아웃과 일치하게 된다. 

[라즈베리파이2 Pin Map 레퍼런스](https://www.raspberrypi.org/documentation/usage/gpio-plus-and-raspi2/README.md)      
위 링크의 Pin Map 레퍼런스에서 나타나는 핀번호는 BCM 핀 번호이다. 해당 핀번호를 참조하여 도선을 연결하면 된다.

GPIO 핀을 처리할때는 유용한 wiring Pi라는 유용한 라이브러리가 있다. 라이브러리의 레퍼런스는 다음과 같다.    
[Wiring Pi Ref](http://wiringpi.com/reference/serial-library/)    
[사용 예제 블로그](http://ghdrl95.tistory.com/141)    

해당 라이브러리를 이용해서 raspi에서 GPIO핀을 컨트롤 하는 C 프로그램을 작성할 수 있다. 컴파일 시 다음 링크 옵션을 넣어주어야 한다.
```
-lwiringPi
```

라즈베리파이에서 /dev/ttyAMA0 를 이용해서 시리얼 통신을 할 것인데, 이 인터페이스로는 로그인도 가능하다. 이를 비활성화 해 주어야 한다. 관련 내용은 다음 블로그에서 확인하였다.    
[/dev/ttyAMA0 로그인 비활성화](http://pinocc.tistory.com/185)    


```
sudo systemctl stop serial-getty@ttyAMA0.service
sudo systemctl disable serial-getty@ttyAMA0.service
```
위 2 커맨드로 ttyAMA0 인터페이스를 통한 로그인을 비활성화 시킬 수 있다.

UART 커뮤니케이션을 위해서 wiringPi 라이브러리를 이용했는데, minicom을 이용해보자.
minicom 설치를 해보자.
```
apt install -y minicom
```
```
sudo minicom -b 9600 -o -D /dev/ttyAMA0
```

다음과 같은 명령어로 시리얼에 데이터를 보낼 수 있다.
```
echo "Serveral Data" > /dev/ttyAMA0
```

src디렉토리에 있는 turn.c를 빌드한 파일을 이용해서 서보모터를 제어하는 시그널을 아두이노에 보낼 수 있다.
다음과 같은 사용방식으로 시그널을 라즈베리파이에서 GPIO로 보낼 수 있다.
```
/usr/bin/turn 180
```