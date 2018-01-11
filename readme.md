# graduation_work

### 1. 주제
주제: ```IoTivity를 활용한 스마트 알람 시스템 구축```

가정에서 유용하게 사용할 수 있는 스마트 알람 시스템을 구축하는 것이 졸업작품의 주제이다. 사용할 주요 장비들로는 라즈베리파이, 아두이노, IoTivity 등이 있다.

### 2. 유사 제품 사례
#### 2.1 스마트 알람 Bonjour
[킥 스타터 출처](https://www.kickstarter.com/projects/1450781303/bonjour-smart-alarm-clock-with-artificial-intellig)    
크라우드 펀딩 사이트인 킥 스타터에 제안된 스마트 알람시계이다. 개인비서 역할을 수행한다. 학습기능도 갖추고 있다. 제공하는 기능들의 예시는 다음과 같다.
1. 음성을 통한 알람 기능
2. 교통상황 정보 제공
3. 날씨 정보 제공
4. 다른 스마트 홈 IoT 기기와의 연동
5. 음악, 동영상 재생 기능
6. 음성 인터페이스 제공
7. 전용 스마트폰 앱을 통한 설정
8. 사용자 생활 패턴에 따라 학습

기본적인 알람 기능 뿐만 아니라 날씨, 교통 정보 등을 종합하여 여유로운 시간 관리가 가능하도록 제안해준다.
또한 사용자의 생활 패턴을 분석하여 환경설정을 하여 맞춤형 정보를 제공한다.    
스마트 홈 허브 역할도 수행한다. 음성 인터페이스를 제공한다.
#### 2.2 스마트 알람 Kello
[Kello 공식 URL](https://www.getkello.com/)    
Kello Labs에서 5만 불을 목표로 크라우드 펀딩을 시작한 알람시계이다. 일반적인 알람시계와는 다르게 수면 습관을 훈련시켜주는 기능을 가지고 있다. 수면 전문가의 수면 행태 분석 연구 결과를 녹여낸 프로그램이 설치되어 있다. 다음과 같은 기능들을 제공한다.
1. Snooze Less: 일 주일 동안 알람 버튼을 눌러서 알람을 끄는 회수를 제한함
2. Bedtime Alert: 잠자리에 들 시간이 되면 알려줌
3. Wake Up Earlier: 조금씩이지만 꾸준한 변화를 통하여 일찍 일어날 수 있도록 함
4. Power Naps: 낮잠을 효율적으로 잘 수 있도록 음악을 틀어줌
5. Fall Asleep Fast: 빨리 잡들도록 숨쉬는 패턴에 맞추어 불빛을 내보내 줌
6. Fight Jet Lag: 비행기를 통한 여행 일정을 알려주면, 신체 시계에 맞추어 최적화된 잠자리 시간을 알려줌

#### 2.3 Brookstone 스마트폰 앱 연동 알람시계
[구매 URL](http://www.brookstone.com/pd/timesmart-app-controlled-bluetooth-alarm-clock-radio/897153p.html)    
Brookstone에서 판매하는 스마트폰 연동 알람시계이다. 특별한 기능을 많이 넣기 보다는, 알람시계 본연의 기능을 넣되 필수 기능에 충실한 모습을 보여준다. UX에 좀 더 신경을 쓴 듯한 제품이다. 주요 특징은 다음과 같다.
1. 케이블로 전원을 연결하되, 수은 전지를 통해 전원 케이블 연결 해제시에도 시간 정보를 저장한다. 따라서 케이블이 재연결되더라도 설정을 다시 할 필요가 없다.
2. 스마트 폰을 통하여 심플한 UI로 모든 기능과 설정을 간편히 할 수 있다. 물론 스마트 폰이 없더라도 기능 사용에 제약은 없다.
3. 알람 소리가 0db부터 100db 까지 점진적으로 커지는 크레센도식 알람을 지원한다.

#### 2.4 Philips의 Wake Up Light
![wake_up_light](images/wake_up_light.jpeg)    
[아마존 구매 URL](https://www.amazon.com/gp/product/B0093162RM/)    
기존에 소리로만 깨우는 알람과는 다르게, 아침 햇살을 모방하는 방식으로 깨워주는 알람시계이다. LED만으로 깨어나지 않는다면 소리도 같이 발생하는 기능도 갖추고 있다.

#### 2.5 카펫 알람
[아마존 구매 URL](https://www.amazon.com/Ruggie-Alarm-Clock-Original-Operated/dp/B01JU0Y4WA)    
강력한 소리를 통해 수면자를 깨우고, 알람을 끄기 위해서는 최소 3초 동안 카펫 위에 서야 꺼진다. 이 값은 최대 30초까지 설정할 수 있다. 알람을 끄기 위해서는 강제적으로 침대에서 나올 수 밖에 없다.

### 3. 진행
#### 1. IoTivity
IoTivity는 IoT 장비간의 연결과 관련된 다양한 기능을 제공하는 오픈소스 소프트웨어 프레임워크이다.

IoTivity를 활용하여 프로젝트가 진행 될 것이므로, IoTivity를 빌드 및 실행해보아야 한다.    
IoTivity 빌드는 우분투 16.04 환경에서 진행한다.    
[IoTivity 우분투 빌드 가이드](https://wiki.iotivity.org/build_iotivity_with_ubuntu_build_machine)    
[IoTivity 빌드 가이드](https://wiki.iotivity.org/build_for_your_system)    

**빌드 환경 구축 커맨드**

    $ sudo apt-get install \
    build-essential git scons libtool autoconf \
	valgrind doxygen wget zip
	
git은 리눅스 토발즈가 만든 유명한 분산 버전관리 툴이다.    
wget은 http 클라이언트의 구현체이다.    
zip은 압축 유틸리티이다.    
scons는 make의 뒤를 잇는 오픈소스 빌드 툴이다.    
doxygen은 주석을 통해 자동적으로 문서를 만들어주는 도구이다.    
valgrind는 메모리 디버깅 툴이다.    
libtool은 컴파일된 포터블 라이브러리를 만드는 데 사용된다.

**외부 라이브러리 설치 커맨드**

    $ sudo apt-get install \
	libboost-dev libboost-program-options-dev libboost-thread-dev \
	uuid-dev libexpat1-dev libglib2.0-dev libsqlite3-dev libcurl4-gnutls-dev
	
**빌드**

    $ scons
	
소스코드는 [github](https://github.com/iotivity/iotivity)에 공개된 소스코드를 git clone하여 받아서 사용하였다.    
scons라는 빌드 툴을 이용하여 간단한 명령어로 빌드를 할 수 있다. i3-CPU 랩탑에서 수 분 정도 소요된다.

**샘플 실행**

    $ LD_LIBRARY_PATH=/your/library/path ./IotivitySample
	
샘플 코드를 실행하기 위해서는 공유 라이브러리(.so) 파일의 경로를 지정해주어야 한다. 인라인에서 "LD_LIBRARY_PATH"라는 환경변수의 설정을 통하여 라이브러리 위치를 지정해줄 수 있다.