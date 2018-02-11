# graduation_work

# 0. 목차

[1. 주제](#m1)    
[2. 유사 제품 사례](#m2)    
[　2.1 스마트 알람 Bonjour](#m2.1)    
[　2.2 스마트 알람 Kello](#m2.2)    
[　2.3 Brookstone 스마트폰 앱 연동 알람시계](#m2.3)    
[　2.4 Philips의 Wake Up Light](#m2.4)    
[　2.5 카펫 알람](#m2.5)    
[3. 진행](#m3)    
[　3.1 요구사항 설계](#m3.1)    
[　3.2 소프트웨어/하드웨어 스택 및 플랫폼 조사 및 확정](#m3.2)    
[　3.3 스터디](#m3.3)    
[　　3.3.1 라즈베리파이 스터디](#m3.3.1)    
[　　3.3.2 AVR / 아두이노 스터디](#m3.3.2)    
[　　3.3.3 IoTivity 스터디](#m3.3.3)    
[　　　3.3.3.5 OCF표준과 IoTivity  개요](#m3.3.3.5)    
[　　　3.3.3.6 OCF 시소스 타입과 IoTivity 시뮬레이터](#m3.3.3.6)    
[　　　3.3.3.p3 IoTivity 빌드](#m3.3.3.p3)
[　　　3.3.3.p5 IoTivity 심플 서버와 심플 클라이언트](#m3.3.3.p5)    
[　3.4 작업](#m3.4)    
[　　3.4.1 라즈베리파이 작업](#m3.4.1)    
[　　3.4.2 아두이노 작업](#m3.4.2)    
[　　3.4.3 IoTivity 작업](#m3.4.3)    


<a name="m1" />    

# 1. 주제
주제: ```IoTivity를 활용한 스마트 알람 시스템 구축```

가정에서 유용하게 사용할 수 있는 스마트 알람 시스템을 구축하는 것이 졸업작품의 주제이다. 사용할 주요 장비들로는 라즈베리파이, 아두이노, IoTivity 등이 있다.

<a name="m2" />

# 2. 유사 제품 사례

<a name="m2.1" />

## 2.1 스마트 알람 Bonjour
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

<a name="m2.2" />

## 2.2 스마트 알람 Kello
[Kello 공식 URL](https://www.getkello.com/)    
Kello Labs에서 5만 불을 목표로 크라우드 펀딩을 시작한 알람시계이다. 일반적인 알람시계와는 다르게 수면 습관을 훈련시켜주는 기능을 가지고 있다. 수면 전문가의 수면 행태 분석 연구 결과를 녹여낸 프로그램이 설치되어 있다. 다음과 같은 기능들을 제공한다.
1. Snooze Less: 일 주일 동안 알람 버튼을 눌러서 알람을 끄는 회수를 제한함
2. Bedtime Alert: 잠자리에 들 시간이 되면 알려줌
3. Wake Up Earlier: 조금씩이지만 꾸준한 변화를 통하여 일찍 일어날 수 있도록 함
4. Power Naps: 낮잠을 효율적으로 잘 수 있도록 음악을 틀어줌
5. Fall Asleep Fast: 빨리 잡들도록 숨쉬는 패턴에 맞추어 불빛을 내보내 줌
6. Fight Jet Lag: 비행기를 통한 여행 일정을 알려주면, 신체 시계에 맞추어 최적화된 잠자리 시간을 알려줌

<a name="m2.3" />

## 2.3 Brookstone 스마트폰 앱 연동 알람시계
[구매 URL](http://www.brookstone.com/pd/timesmart-app-controlled-bluetooth-alarm-clock-radio/897153p.html)    
Brookstone에서 판매하는 스마트폰 연동 알람시계이다. 특별한 기능을 많이 넣기 보다는, 알람시계 본연의 기능을 넣되 필수 기능에 충실한 모습을 보여준다. UX에 좀 더 신경을 쓴 듯한 제품이다. 주요 특징은 다음과 같다.
1. 케이블로 전원을 연결하되, 수은 전지를 통해 전원 케이블 연결 해제시에도 시간 정보를 저장한다. 따라서 케이블이 재연결되더라도 설정을 다시 할 필요가 없다.
2. 스마트 폰을 통하여 심플한 UI로 모든 기능과 설정을 간편히 할 수 있다. 물론 스마트 폰이 없더라도 기능 사용에 제약은 없다.
3. 알람 소리가 0db부터 100db 까지 점진적으로 커지는 크레센도식 알람을 지원한다.

<a name="m2.4" />

## 2.4 Philips의 Wake Up Light
![wake_up_light](img/wake_up_light.jpeg)    
[아마존 구매 URL](https://www.amazon.com/gp/product/B0093162RM/)    
기존에 소리로만 깨우는 알람과는 다르게, 아침 햇살을 모방하는 방식으로 깨워주는 알람시계이다. LED만으로 깨어나지 않는다면 소리도 같이 발생하는 기능도 갖추고 있다.

<a name="m2.5" />

## 2.5 카펫 알람
[아마존 구매 URL](https://www.amazon.com/Ruggie-Alarm-Clock-Original-Operated/dp/B01JU0Y4WA)    
강력한 소리를 통해 수면자를 깨우고, 알람을 끄기 위해서는 최소 3초 동안 카펫 위에 서야 꺼진다. 이 값은 최대 30초까지 설정할 수 있다. 알람을 끄기 위해서는 강제적으로 침대에서 나올 수 밖에 없다.

<a name="m3" />

# 3. 진행

<a name="m3.1" />

## 3.1 요구사항 설계
스마트 알람은 혼자사는 개인을 위한 제품으로 대표적으로 다음과 같은 기능들을 제공한다.

1. 빛을 이용한 알람과 소리를 이용한 알람을 동시에 사용
2. 개인 스케쥴러와 연동하여 일어날 시간을 자동으로 계산하여 알람 등록
3. 센서를 이용하여 개인이 수면에 드는 시간을 수집
4. 웹을 통한 UI 제공    

그리고 +alpha로 머신러닝을 활용한 기능도 추가할 예정이다.

추가적인 기능적 요구사항이 있을 시 이 문서에 추가한다.

<a name="m3.2" />

## 3.2 소프트웨어/하드웨어 스택 및 플랫폼 조사 및 확정
지금 생각하는 SW/HW 스택 및 플랫폼은 다음과 같다.
1. 라즈베리파이 - 라즈비안 OS
2. 아두이노
3. IoTivity

각각의 스택과 플랫폼에 대한 조사를 해보자 한다.

<a name="m3.3" />

## 3.3 스터디

<a name="m3.3.1" />

### 3.3.1 라즈베리파이 스터디

[위키백과 링크](https://ko.wikipedia.org/wiki/%EB%9D%BC%EC%A6%88%EB%B2%A0%EB%A6%AC_%ED%8C%8C%EC%9D%B4)    
라즈베리파이는 영국 잉글랜드의 라즈베리 파이 재단이 만든 싱글 보드 컴퓨터이다. 교육용의 목적성을 띠고 제작되었으며, 저성능이지만 가격이 매우 싼 편이다. 그리고 높은 범용성을 가지고 있다. ARM CPU를 사용하며, 운영체제는 대부분 리눅스 계열을 사용한다. 그 중 데비안 계열 리눅스를 라즈베리파이용으로 만든, 라즈베리 파이의 개발사에서 기본적으로 제공하는 OS인 라즈비안이 있다. 그 외에도 다양한 리눅스와 저사양 디바이스용 Windows, 안드로이드 OS 등을 설치 가능하다.    
라즈베리파이 3 모델 B의 스펙은 다음과 같다.
- Quad Cord 1.2GHz Broadcom BCM2837 64bit CPU
- 1GB RAM
- BCM43438 무선랜 과 BLE(Bluetooth Low Energy) 보드 내장
- 확장된 GPIO를 위한 40개 핀
- USB 2.0 포트 4개
- 4 Pole stereo output and composite video port
- 풀 사이즈 HDMI
- CSI 카메라 포트
- DSI 디스플레이 포트(터치스크린 디스플레이 연결용)
- 저장소를 위한 Micro SD 포트
- 2.5A까지 지원하는 Micro USB 전원 입력부

확인해보면, 보드 자체에서 Wi-Fi와 블루투스를 지원하므로 따로 Wi-Fi 동글 같은 것을 끼울 필요가 없다. 그리고 저장소로 Micro SD카드를 지원하므로, 라즈베리파이에 OS를 올리기 위해서는 이미지 업로드에 사용할 컴퓨터에 Micro SD 슬롯을 지원하거나 젠더를 가지고 있어야 한다.

<a name="m3.3.2" />

### 3.3.2 AVR / 아두이노 스터디
AVR 마이크로 컨트롤러 프로젝트인 Wiring에서 파생한 프로젝트이다. 마이크로컨트롤러 유닛(MCU: Micro Controller Unit)은 간단하게 이야기 해서 작은 컴퓨터라고 볼 수 있다. 특수목적을 수행하는 소형화된 CPU로서, 주로 C언어를 이용하여 프로그래밍을 한다. CPU와 다른점은, 하나의 칩에 CPU, Flash Memory, SRAM, I/O의 기능이 내장되어 있다. 따라서 MCU 독립적으로 동작이 가능하다.    
AVR은 하드웨어 엔지니어가 될 때 처음 공부하면 좋을만한 마이크로컨트롤러로, 1996년 아트멜 사에서 개발한 8비트 RISC MCU이다. 단일 칩 플래시 메모리를 사용한 최초의 MCU중 하나이다. AVR 프로그래밍을 통해 하드웨어를 개발할 시 하드웨어적으로 로우 레벨 부분을 신경써야 하므로, 해당 부분의 지식을 습득하기 좋다. AVR 프로그래밍을 할 시 Atmel Studio라는 IDE를 활용하면 편리하다. 개발도구가 무료이며, 하드웨어 가격도 저렴하다. 가격 대비 만족스러운 성능도 보여준다.    

아두이노는 2005년 이탈리아의 IDII(Interaction Design Institutelvera)에서 하드웨어에 익숙치 않은 학생들이 자신의 작품들을 손쉽게 만들수 있게 하는 목적으로 만들어졌으며, AVR을 기반으로 제작되었다. 아두이이노는 임베디드 시스템 중의 하나로 장치 제어를 손쉽게 할 수 있는 환경을 제공한다. 아두이노 개발을 위한 아두이노 IDE도 무료로 제공되며 사용이 매우 쉽다. 펌웨어 업로드를 USB를 통하여 쉽게 할 수 있다. API는 C++로 제공된다.

<a name="m3.3.3" />

### 3.3.3 IoTivity 스터디
IoTivity는 IoT 장비간의 연결과 관련된 다양한 기능을 제공하는 오픈소스 소프트웨어 프레임워크이다.    

IoTivity가 어떤 서비스를 제공해주는지 알아보기 위해 학습을 진행한다.

관련된 교육 자료들은 다음 링크에서 얻을 수 있다.    
[SKKU 2017 Lecture Materials](https://wiki.tizen.org/Tizen_Lecture_Materials/Sungkyunkwan_University_2017)    

<a name="m3.3.3.5" />

#### [05-OCF표준과 Iotivity 개요](https://wiki.tizen.org/images/8/80/05-OCF_Standard_and_IoTivity_Overview.pdf)    

IoT의 서비스 레벨 상호호환성을 지원하기 위한 OCF 표준이라는 것이 있다. IoTivity는 이 OCF 표준을 구현한 오픈소스 소프트웨어 프레임워크 중 하나이다.    

** > OCF 표준 **    
OCF표준은 원격 접근이나, 리소스 타입, 보안 등의 스펙들을 정해놓았다.    
OCF는 Open Connectivity Foundation의 약자로, 기존에 있던 OIC(Open Interconnect Consortium)에 Microsoft Qualcomm, Electrolux가 새롭게 합류하면서 확장된  IoT 표준단체이다. 운영, 특허 정책 등 모든 내용이 OIC와 동일하지만, 신규 회원사의 참여로 인해서 이름만 바뀐 형태이다.    

OIC Role은 클라이언트와 서버로 구성되어 있다. CRUDN 작업들은 CoAP/HTTP 프로토콜을 통해서 이루어진다.
[CoAP](https://en.wikipedia.org/wiki/Constrained_Application_Protocol)는 Constrained Application Protocol의 약자로, 요즘 떠오르는 IoT용 프로토콜이다. CoAP프로토콜은 UDP를 기반으로 이루어지며, 낮은 신뢰성은 반복적 메시징에 의해 의존한다. 또한 멀티캐스트를 지원하며, UDP 프로오콜 상 DTLS를 이용하여 보안을 강화환다. 리소스와 서비스는 URI를 이용하여 구분한다.    

OIC 아키텍쳐는 클라이언트-서버 구조이다. OIC 클라이언트는 트랜젝션을 실행시키고, 서비스를 얻기 위해 OIC 서버에 접근한다. OIC 서버는 리소스를 제공하며, 응답을 주면서 서비스르 제공한다. RESTful 아키텍쳐에 기반한다.    

RESTful 모델은 URI로 리소스를 표현하며 형태는 다음과 같다.
```
oic://<regular_name>/<path>?<query>
```
쿼리 파라매터는 ';' 문자를 통해 구분하며, 파라매터의 순서는 중요하다.    
RESTful 모델에서 사용하는 동작은 CRUDN이며, 이는 각각 Create, Retrieve, Update, Delete, Notify의 머릿글자를 딴 것이다. 일반적인 자료구조의 Primitive Operation이 아닌 Notify는 상태 변화에 대하여 알림(notificiation)을 요청하는 동작이다.    

이러한 RESTful 리소스 모델은, 네트워크 프로토콜에 관계없이 원격에 있는 OIC 디바이스에 접근할 수 있도록 추상화 계층을 제공한다.

JSON 스키마로 프로퍼티들을 나타내며, RAML(RESTful API Modeling Language)로 요청, 응답의 형태를 나타낸다.

**IoTivity**    
크로스 플랫폼 프레임워크이며, 아두이노와 같은 저사양의 장비들을 지원한다. 그리고 다양한 언어에 API를 제공한다.    
IoTivity가 서비스 레벨에서 제공하는 것들은 다음과 같다.
1. 장비 관리    
Thing Manager로 사물들을 관리한다.    
Scene Manager로 여러개의 장비를 제어하는 시나리오를 만든다.    
2. 저전력 관리    
리소스 호스팅을 한다. 원격 클라이언트가 할 요청, 데이터 관리를 대신 해준다고 생각하면 된다.    
리소스 디렉토리 기능을 제공한다. 소비 전력을 줄이기 위해서 Sleep 해야하고 Multicast에 응답할 수 없는 장비들을 찾아내는 쿼리를 보낸다.    
3. 리소스 캡슐화    
리소스 브로커 역할을 한다. 원격에 있는 리소스가 있는지 확인하고, 알려진 리소스에 대해 지속적으로 연결이 가능한지 확인하는 역할이다.    
리소스 캐시역할을 한다. 원격 리소스의 가장 최신 정보를 가지고 있는다.    
4. 리소스 컨테이너    
프로토콜 브릿지 역할을 한다. OCF 리소스가 아닌 것들을 통합한다.

<a name="m3.3.3.6" />

#### [06-OCF 리소스 타입과 Iotivity 시뮬레이터](https://wiki.tizen.org/images/0/0b/06-OCF_Resource_Type_and_IoTivity_Simulator.pdf)    

** RAML **    
RAML(RESTful API Modeling Language)는 API 라이프사이클 설계와 공유를 쉽고 간편하게 해주는 모델링 언어이다. 공식 사이트 링크는 http://raml.org 이다.    
RAML은 기계가 인식할 수 있는 API 설계도이며, 사람에게도 친숙하다. Nodejs, Java, .NET, 파이썬의 몇몇 언어들을 지원한다. RAML으로 CRUDN 연산 시 전송하는 패킷 내용을 정의할 수 있다. 시뮬레이션에 사용할 수도 있고, 응답의 타당성을 검증할 수 있다.    

** CRUDN 연산 응답 코드 **    
리소스 타입의 정의외 허용된 CRUDN 연산에 따라서 리소스는 생성되거나 변경될 수 있다. CRUDN 연산에 따라서 다른 의미를 갖는 다른 응답코드가 올 수 있다.    

| 응답 코드 | 의미 |
|:--------|:--------|
| 200 | 변경이 잘 이루어짐 |
| 201 | CREATE 연산에 의해 리소스가 성공적으로 생성되었고, 응답 내용은 생성된 리소스의 URL이다.|
| 204 | 모든 것이 잘 동작했고, 응답 내용은 없다. |
| 403 | RETRIEVE 연산의 경우, 서버는 해당 요청에 대하여 지원하지 않는다. 혹은 서버가 어떤 문제로 인하여 CREATE나 UPDATE 연산을 수행할 수 없다.|    

**OIC에서 정의하는 리소스 타입들**    
62개의 리소스 타입이 정의되어 있다.    
[OCF Resource Type Spec v1.3.0](https://openconnectivity.org/specs/OCF_Resource_Type_Specification_v1.3.0.pdf)    
[OIC Resource Type Spec v.1.1.0](https://openconnectivity.org/specs/OIC_Resource_Type_Specification_v1.1.0.pdf)    

**IoTivity Simulator**    
[IoTivity 위키의 시뮬레이터 설명](https://wiki.iotivity.org/iotivity_simulator)    
[IoTivity 시뮬레이터 유저 가이드](https://wiki.iotivity.org/iotivity_tool_guide)    
실제 장비들을 통해서 서비스 구성을 하지 않더라도, IoTivity Simulator를 통해서 설계한 RESTful API와 상황들을 테스트해볼 수 있다. 시뮬레이터는 크게 두가지 기능을 제공한다.
- 서비스 제공자
- 클라이언트 컨트롤러    
시뮬레이터는 Eclipse 플러그인 형태를 띤다. 그리고 제공하는 피쳐가 리눅스 플랫폼에서 자바 API를 통한 SDK 지원이므로, 시뮬레이터 관련 작업은 리눅스에서 진행하도록 한다. 리눅스는 Vmware에 설치된 Ubuntu 16.04를 사용한다.

<a name="m3.3.3.p3" />

#### [실습-03 IoTivity 빌드](https://wiki.tizen.org/images/e/ef/Practice-03-Building_IoTivity.pdf)    


IoTivity는 멀티 플랫폼을 지원한다. x86, x86_64, arm-v7a, arm, arm64 CPU 아키텍쳐를 지원하고, 운영체제는 Linux, 안드로이드, 타이젠, 아두이노를 지원한다.    
IoTivity의 기본 빌드 툴은 Scon으로 멀티 플랫폼의 크로스 빌드를 지원한다. 명령어는 다음과 같은 형식으로 사용한다.

	$ scons TARGET_ARCH=arm TARGET_OS=android


<a name="m3.3.3.p5" />

#### [실습-05 IoTivity 심플 서버와 심플클라이언트](https://wiki.tizen.org/images/7/71/Practice-05-Simple_Server_and_Simple_Client.pdf)

IoTivity에서 센서나 구동기같은 엔티티는 OCF 리소스 모델에 따라 리소스로써 다루어진다. 각각의 리소스는 장치 타입, CRUDN 연산, URI 정보를 갖는다. 그리고 유저는 리소스를 URI와 RESTful 인터페이스로 접근할 수 있다.    
IoTivity 네트워크 토폴로지에서 OCF 서버는 CRUDN 요청을 처리한다. 그리고 OCF 클라이언트는 리소스를 찾고 CRUDN 요청을 보낸다.    
여기서 만들어볼 심플 서버, 클라이언트 구성은 다음과 같다. 서버는 온도, 습도 센서이다. 클라이언트는 안드로이드에 설치된 어플리케이션이다. 해당 소스의 레포지토리는 다음과 같다.    
https://github.com/gudbooy/IoTivity_Sample_Things.git

<a name="m3.4" />

## 3.4 작업

<a name="m3.4.1" />

### 3.4.1 라즈베리파이 작업
구매한 라즈베리파이에 운영체제를 업로드 해야한다. 업로드할 운영체제 이미지는 라즈베리파이에 적합하게 커스터마이징된 데비안 계열 리눅스인 라즈비안이다. 라즈비안은 다음 링크에서 다운로드 받을 수 있다.     
[라즈비안 다운로드 사이트 링크](https://www.raspberrypi.org/downloads/raspbian/)    
[라즈비안 다운로드 링크](http://director.downloads.raspberrypi.org/raspbian/images/raspbian-2017-12-01/2017-11-29-raspbian-stretch.zip)    
Lite버전과 일반 버전이 있는데 일반 버전을 다운로드 하였다. 다운받은 이미지 데이터를 SD카드에 써야 하는데, 이 방법에 대한 라즈베리 파이 공식 홈페이지에서 제공하는 가이드 링크는 다음과 같다.    
[라즈비안 이미징 방법 링크](https://www.raspberrypi.org/documentation/installation/installing-images/README.md)    
윈도우 계열 OS에서 이미징 작업을 할 때에는 Win32 Disk Imager를 사용하면 편리하다. 해당 소프트웨어는 다음 링크에서 다운받을 수 있다.    
[Win32 Disk Imager SourceForge 링크](https://www.raspberrypi.org/documentation/installation/installing-images/README.md)    

위 절차를 통해서 라즈베리파이의 SD카드에 라즈비안 OS 이미징 작업을 완료하였다. 입력장치로 LED가 포함된 기계식 키보드를 사용할 시, 부족한 전원량으로 인하여 가끔씩 라즈베리파이가 꺼지는 현상이 있었다. 이러한 현상을 방지하기 위해서 LED가 없는 키보드를 사용하거나, Telnet이나 SSH와 같은 원격 쉘을 이용한 작업을 할 것이 권장된다.    

라즈베리파이의 기본 유저 이름은 "pi"이고 초기에는 아무런 인증 없이 사용자 로그인이 되었다. 그리고 sudo su 명령어를 통해서 인증 없이 루트 권한을 취득할 수 있다. 이 상태에서 초기 패스워드를 지정해 주었다.

<a name="m3.4.2" />    

### 3.4.2 아두이노 작업

<a name="m3.4.3" />    

### 3.4.3 IoTivity 작업

** IoTivity 시뮬레이터 구동 환경 구축 **    

IoTivity 시뮬레이터는 리눅스에서만 지원이 된다. 현재 작업용 PC의 OS는 Windows 10이므로, 리눅스 작업은 가상머신에서 실시하도록 한다. 가상머신의 Guest OS는 Ubuntu 16.04 LTS 배포판을 이용하기로 한다.    

IoTivity 시뮬레이터는 이클립스 플러그인 형태를 띠므로 일단 이클립스 먼저 설치하기로 한다.
이클립스는 이클립스 제단에서 주도적으로 진행하는 오픈소스 IDE 프로젝트이다. 자바로 작성되었으며, 오픈소스지만 강력한 기능을 제공한다. 또한 Windows, Linux, Mac의 플랫폼에서 동작한다.    

시뮬레이터 설치와 관련된 링크는 다음과 같다.    
[IoTivity 시뮬레이터 유저 가이드](https://wiki.iotivity.org/iotivity_tool_guide)      

위 링크의 문서에 따르면 리눅스 플랫폼에서 4.4버전 이상의 이클립스가 필요하다.    
이클립스를 설치하기에 앞서서 JDK가 설치되어 있지 않다면, JDK를 설치한다. OpenJDK 7버전 혹은 Oracle JDK를 설치하면 된다.    

apt 패키지 매니저를 통해서 이클립스를 설치할 경우, 4.4 버전 이상의 최신 버전이 설치가 되지 않으므로 이클립스 공식 사이트를 방문하여 다운로드를 받는다. 만약 패키지 매니저를 통해 설치를 했다면 다음 명령어를 통해 삭제한다.
```
	sudo apt-get --purge remove eclipse
```

이클립스 다운로드 링크는 다음과 같다.    
[이클립스 다운로드 링크](http://www.eclipse.org/downloads)    

리눅스 64bit용 설치 파일을 다운로드하면 tar.gz 파일이 나타나게 된다.    
다음 명령어로 압축을 풀어준다.
```
	tar xvf [tar.gz file name]
```
압축을 풀면 폴더 안에 `eclipse-inst`라는 ELF파일이 있다. 실행시켜서 이클립스를 설치할 수 있다.
Java IDE로 기본 설정대로 설치한다.

그리고 Eclipse를 실행하여 IoTivity Simulator 플러그인을 설치한다.
메뉴의 `Help > Install New Software`를 선택한 뒤, `Work with` 항목에 다음 URL 중 하나를 입력한다.    
* https://downloads.iotivity.org/tools/simulator/latest
* https://mirrors.kernel.org/iotivity/tools/simulator/latest
* ftp://mirrors.kernel.org/iotivity/tools/simulator/latest    
매뉴 중 `Group itesm by category`를 체크 해제하고, `Simulator`를 선택 후 `Next`를 누른다.
이후 나타나는 Simulator를 설치한다.    
License에 동의한다고 체크한다.    
Unsigned contents warning이 뜰 경우 OK를 눌러 계속 진행한다.    
이후 Restart에 대한 내용이 나오면 Yes를 누른다.

시뮬레이터 플러그인이 설치된 후에, 시뮬레이터를 사용하려면 `Open Perspective`를 눌러서 `Client Controller`나 `Service Provider`를 눌러서 진행하면 된다. 시뮬레이터는 두개의 관점으로 이루어져 있는데 IoTivity의 클라이언트 관점과 IoTivity 서버의 관점으로 이루어져 있다.

IoTivity를 활용하여 프로젝트가 진행 될 것이므로, IoTivity를 빌드 및 실행해보아야 한다.    
IoTivity 빌드는 우분투 16.04 환경에서 진행한다.    
[IoTivity 우분투 빌드 가이드](https://wiki.iotivity.org/build_iotivity_with_ubuntu_build_machine)    
[IoTivity 빌드 가이드](https://wiki.iotivity.org/build_for_your_system)    

**빌드 환경 구축 커맨드**

    $ sudo apt-get -y install \
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
	
**의존 라이브러리 설치(tinycbor, crypto lib)**    

	$ git clone https://github.com/01org/tinycbor.git extlibs/tinycbor/tinycbor
	$ git clone https://github.com/ARMmbed/mbedtls.git extlibs/mbedtls/mbedtls -b mbedtls-2.4.2
	
**빌드**    
소스코드 받아오기

    $ git clone https://github.com/iotivity/iotivity
	
릴리즈 버전 바이너리 빌드 커맨드
	
    $ scons
	
디버깅 바이너리 빌드 커맨드

    $ scons RELEASE=false
	
Verbose 모드

    $ scons VERBOSE=true
	
클리어

    $ scons -c
	
소스코드는 [github](https://github.com/iotivity/iotivity)에 공개된 소스코드를 git clone하여 받아서 사용하였다.    
scons라는 빌드 툴을 이용하여 간단한 명령어로 빌드를 할 수 있다. i3-CPU 랩탑에서 수 분 정도 소요된다.

**샘플 실행**

    $ export LD_LIRBRARY_PATH=<iotivity>/out/linux/x86_64/release

    $ ./IotivitySample
	
샘플 코드를 실행하기 위해서는 공유 라이브러리(.so) 파일의 경로를 지정해주어야 한다. 인라인에서 "LD_LIBRARY_PATH"라는 환경변수의 설정을 통하여 라이브러리 위치를 지정해줄 수 있다.