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
## 2. 서비스 등록
lightserver를 포함한 IoTivity 서버들을 적절히 제어하기 위해서 서비스를 등록한다. 라즈베리파이의 /usr/sbin/service 파일은 POSIX 쉘 스크립트이며, 해당 파일은 service.sh라는 이름으로 저장되어 있다.

`/etc/init.d/lightserver` 경로에 디렉토리에 `lightserver` 파일을 붙여넣어준다. 그리고 다음 `/etc/rc.local` 파일의 마지막 줄에 다음 라인을 추가한다.

```
/usr/sbin/service lightserver start
```

`/home/pi/Projects/lightserver` 경로에 lightserver 바이너리를 위치시키고, security_resource_manager와 관련된 PersistentStorage 파일도 같이 위치시킨다.

그리고 `/home/pi/Projects/run_light_server.sh` 에 lightserver를 실행시키는 스크립트를 작성한다.
이제 lightserver는 부팅 시 자동으로 실행되게 된다.

alarmserver의 경우 다음 `/home/pi/Projects/run_alarm_server.sh`의 쉘 스크립트를 실행함으로써 서버를 실행시킨다. 해당 스크립트는 위 service_script 디렉토리에 첨부되어 있다.

부팅시 동작하는 본 쉘 스크립트인 `/etc/rc.local` 스크립트에는 위 `run_*_server.sh` 스크립트들을 실행하도록 한다.

## 3. Sqlite 연동
sqlite는 경량화된 DBMS이다. IoT나 임베디드 장비 등을 위한 경량화된 로컬 스토리지 DBMS 역할을 수행한다. 원격에서 접속이 불가능하며, 파일 기반으로 데이터베이스를 저장한다. 알람 기능을 위해서 알람관련 데이터를 저장하기 위해서 sqlite를 사용한다. 다음 명령어를 통해 설치할 수 있다.

```
sudo apt install -y sqlite
```

개발의 편의를 위해서 SQLite GUI Client App인 SQLite Studio를 설치한다.
해당 앱은 다음 사이트에서 다운로드 가능하다
[SQLite Studio Homepage](https://sqlitestudio.pl/index.rvt)    

SQLite Studio를 열면 다음과 같은 에러 메시지가 나타날 수 있다.
```
[10:48:23] Cannot load plugin DbSqliteCipher. Error details: Cannot load library /home/bobgil/Downloads/SQLiteStudio/plugins/libDbSqliteCipher.so: (libcrypto.so.1: cannot open shared object file: No such file or directory)
```
DbSqliteCipher라는 플러그인을 불러오지 못하는 모습이다. 일단은 크게 상관없어 보인다.

## 4. 타임존 설정
알람이 정상적으로 동작하도록 하기 위해서 UTC를 KST로 바꾸어 주어야 한다.
타임존은 `/etc/localtime` 파일에 따라 설정된다.
기존에는  `/usr/share/zoneinfo/Etc/UTC`의 심볼릭 링크로 설정되어 있기 때문에, `/usr/share/zoneinfo/Asia/Seoul`로 바꾸어주면 된다.

```
rm /etc/localtime
ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
```

## 5. NTP 설정
라즈비안을 시간 동기화 서버로 사용할 것이 아니라면 NTP는 불필요하며, timedatectl에서 NTP를 enable시킬 수 있다.

```
timedatectl status
```
여기서 NTP Synchronized, NTP enabled와 같은 설정으로 확인할 수 있다. 지금 현재상태는 Enabled된 상태이므로 따로 체크할 일은 없다.

## 6. mjpg-streamer 설치
파이카메라를 이용한 CCTV기능은 다양하게 구현이 가능한데, 이번 경우에는 간편한 웹 스트리밍이 가능한 mjpg-streamer라는 오픈소스를 활용하기로 하였다.    
[mjpg-stream 깃허브 미러페이지](https://github.com/jacksonliam/mjpg-streamer)    

설치하는 과정을 따라서 차근차근 설치해보자.

```
cd /home/pi/Desktop/mjpg-streamer/mjpg-streamer-experimental
export LD_LIBRARY_PATH=`pwd`
./mjpg_streamer -i "./input_uvc.so -n -f 15 -r 640x480 -l off" -o "./output_http.so -n -p 8083 -w ./www"
```

파이카메라를 설치한 뒤 위 커맨드를 실행하면 다음 URL에서 라이브 CCTV를 확인할 수 있다.
```
http://192.168.35.135:8083/stream.html
http://192.168.35.135:8083/?action=stream
```