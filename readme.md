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
[　　　3.3.3.7 IoTivity 연결 추상계층](#m3.3.3.7)    
[　　　3.3.3.8 IoTivity P2P 연결](#m3.3.3.8)    
[　　　3.3.3.p3 IoTivity 빌드](#m3.3.3.p3)    
[　　　3.3.3.p5 IoTivity 심플 서버와 심플 클라이언트](#m3.3.3.p5)    
[　　3.3.4 관련 툴 스터디](#m3.3.4)    
[　　　3.3.4.1 SCon](#m3.3.4.1)    
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

**OCF 표준**    
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

<a name="m3.3.3.7" />

#### [07-Iotivity 연결 추상계층](https://wiki.tizen.org/images/9/9c/07-IoTivity_Connectivity_Abstraction.pdf)    

IoTivity 네트워크 연결 아키텍쳐는 크게 4단계로 나뉘어진다.    
1. 메시지 스위칭    
게이트웨이 리소스 발견    
라우팅 테이블 관리
메시지 포워딩    
2. CA 제어 컴포넌트    
네트워크 선택과 인터페이스 제어    
CoAP 메시지 직렬화와 파싱    
블록단위 메시징의 흐름 제어    
3. 전송 어댑터 컴포넌트    
UDP, TCP, BLE, BT와 NFC를 통한 데이터 전송    
DTLS를 사용한 안전한 데이터 교환
4. 플랫폼 어댑터 컴포넌트    
Ubuntu, Android, Tizen, Arduiono와 같은 플랫폼별 해당되는 Wi-Fi, BLE, 이더넷, BT    


- Routing Through Heterogeneous Connectivity    

만약 중간 게이트웨이가 다양한 네트워크 연결을 지원한다면(IP와 Bluetooth), 게이트웨이는 서로 다른 전송계층에 있는 요청과 응답을 포워드 해줘서 연결가능하게 해줄 수 있다.

- Blockwise Transfer    

OIC 클라이언트와 OIC 서버는 작은 블록단위의 메시지로 쪼개서 보냄으로써, 큰 크기의 메시지를 보내고 받을 수 있다.

- Start from Basic API Example    

클라이언트 사이드에서는 서비스나 리소스를 찾기위해 다음 API를 사용할 수 있다.    
[OCPlatform::findResource Docs 링크](https://api-docs.iotivity.org/latest/namespace_o_c_1_1_o_c_platform.html#a96b3c0ceb833135936fa6204ea569a8a)    
```
OCPlatform::findResource(const std::string& host, const std::string& resourceName, OCConectivityType connectivityType, FindCallback resourceHandler);
```

이 때 모든 알려진 OIC 디바이스를 찾기 위해선 다음과 같은 코드를 사용할 수 있다.

```
requestURI << OC_RSRVD_WELL_KNOWN_URI;

OCPlatform::findResource("", requetURI.str(), CT_DEFAULT, &foundResource);
```

OC_RSRVD_WELL_KNOWN_URI는 "/oic/res"를 뜻하며, 모든 discoverable한 OIC 장치를 뜻한다.    
host 인자에 null값을 넣으면 리소스 탐색 쿼리를 멀티캐스트 하게 된다.

- Call Path(호출 과정)    

API를 호출하면 OCPlatform -> OCPlatform_impl -> InProcClientWrapper -> OCStack -> CaAPI 순서로 호출된다.    
다른 RESTful API의 호출 과정과 동일하다(POST, GET, PUT, OBSERVE)

- 소스 코드 계층구조(Source Code Hierarchy)    

`iotivity/resource/csdk/connectivity/src`에 연결 관련 소스코드가 있다.    
라우팅 메니저 소스코드는 `/iotivity/resource/csdk/routing` 경로에 있다.

- 데이터 전송 시 호출 과정(Call Path: Sending Data)    


1. 전송 요청은 블록 단위(BWT:Block-wise tranfer)로 처리된다. 일반적으로 UDP나 블루투스를 통해 전송된다.

2. BWT는 단위 데이터를 기본 크기인 1KB의 블록 데이터로 만들어서 전송 큐 쓰레드로 보낸다.
3. 전송 큐 쓰레드는 적절한 Transport 핸들러에 데이터를 보낸다.
4. UDP의 경우 UDP 전송 큐 스레드는 단말로 데이터를 보낸다.

전송 큐는 2가지 종류가 있으며, 각각이 공통 전송 큐과 어댑터 전송 큐이다.

- 데이터 수신 시 호출 과정(Call Path: Receiving Data)    


1. UDP의 경우, UDP 수신 쓰레드는 단말로부터 데이터를 받아온다.
2. 적절한 Transport가 UDP나 블루투스인 경우, BWT는 블록 데이터의 다음 전송 데이터 준비를 한다.
3. 전송큐 쓰레드로 다시 보내진다.
4. 만약 받은 데이터가 BWT의 마지막 블록 데이터인 경우, 수신 큐 쓰레드로 데이터가 전송되고, 최종적으로는 상위 레이어로 보내지게 된다.


- CA(Connectivity Abstraction) APIs

연결 추상계층의 API들은 다음과 같이 존재한다.
```
CAResult_t 	CAInitialize();
void 		CATerminate();
CAResult_t	CAStartListeningServer();
CAResult_t	CAStopListeningServer();
CAResult_t	CAStartDiscoveryServer();
void		CARegisterHandler(CARequestCallback ReqHandler, CARequestCallback RespHandler, CAErrorCallback ErrorHandler);
CAResult_t	CACreateEndpoint(CATransportFlags_t flags, CATransportAdapter_t adapter, const char *addr, uint16_t port, CAEndpoint_t **object);
void		CADestroyEndpoint(CAEndpoint_t *object);
CAResult_t	CAGenerateToken(CAToken_t &token, uint8_t tokenLength);
void		CADestroyToken(CAToken_t token);
CAResult_t	CASendRequest(const CAEndpoint_t *object, const CARequestInfo_t *requestInfo);
CAResult_t	CASendResponse(const CAEndpoint_t *object, const CAResponseInfo_t *responseInfo);
CAResult_t	CASelectNetwork(CATransportAdapter_t interestedNetwork);
CAResult_t	CAUnSelectNetwork(CATransportAdapter_t nonInterestedNetwork);
CAResult_t	CAGetNetworkInformation(CAEndpoint_t **info, uint32_t *size);
CAResult_t	CAHandleRequestResponse()
CAResult_t	CASetRAInfo(const CARAInfo_t *caraInfo)
```

`CAInitialize()` - 연결 추상화 모듈을 초기화한다. 어댑터와 공통 쓰레드 풀, 그외 모듈들을 초기화한다.    
`CASelectNetwork()` - 사용할 네트워크를 고른다.    
`CAStartDiscoveryServer()` - 멀티케스트 요청을 Listen하기 위한 서버를 실행한다. 어뎁터 설정에 따라 다른 종류의 서버가 구동된다.    

`CACreateEndpoint()` - 단말을 지정한다. CADestroyEndpoint()를 이용해서 단말을 해제할 수 있다.    
`CAGenerateToken()` - 요청과 응답을 매칭시키기 위한 토큰을 생성한다.    
`CASendRequest()` - 리소스에 제어 요청을 보낸다.    
`CAGetNetworkInformation()` - 네트워크 정보를 받아온다.    


<a name="m3.3.3.8" />

#### [08-Iotivity P2P 연결](https://wiki.tizen.org/images/5/50/08-IoTivity_P2P_Connection.pdf)  

IoTivity는 다양한 언어들과 플랫폼을 위한 API들을 지원한다.



<a name="m3.3.3.p3" />

#### [실습-03 IoTivity 빌드](https://wiki.tizen.org/images/e/ef/Practice-03-Building_IoTivity.pdf)    


IoTivity는 멀티 플랫폼을 지원한다. x86, x86_64, arm-v7a, arm, arm64 CPU 아키텍쳐를 지원하고, 운영체제는 Linux, 안드로이드, 타이젠, 아두이노를 지원한다.    
IoTivity의 기본 빌드 툴은 Scon으로 멀티 플랫폼의 크로스 빌드를 지원한다. 명령어는 다음과 같은 형식으로 사용한다.

	$ scons TARGET_ARCH=arm TARGET_OS=android

IoTivity gerrit이라는 곳에서 소스코드를 받을 수 있다. 소스코드를 받기 이전에 IoTivity Linux foundation에 회원가입을 한다. 이후 다음 링크로 이동해서 username으로 로그인을 해야 한다.

	https://gerrit.iotivity.org/gerrit/

그리고 소스코드를 내려받을 git 클라이언트 사이드에서 다음 명령어를 이용해서 공개키-비밀키를 생성한다.

	$ ssh-keygen

일부 질문과 답변을 응답한다. 그러면 클라이언트의 공개키-비밀키가 생성되게 된다. 그리고 다음 명령어의 표준 출력값을 gerrit 웹 사이트의 SSH-public key form에 붙여넣는다.

	 $ cat ~/.ssh/id_rsa.pub

그리고 클라이언트에서 ssh configuration을 설정해준다.
	
	$ gedit ~/.ssh/config
	
	
다음과 같은 양식으로 붙여넣는다. user 항목에는 gerrit ID값을 넣는다.

```
Host gerrit.iotivity.org
	Hostname gerrit.iotivity.org
	IdentityFile ~/.ssh/id_rsa
	User hangil
	Port 29418
```

그리고 다음 명령어로 git clone을 받는다.

	$ git clone ssh://gerrit.iotivity.org/iotivity
	
그리고 의존 라이브러리도 같이 받는다.(tinycbor)

	$ git clone https://github.com/01org/tinycbor.git extlibs/tinycbor/tinycbor
	$ git clone https://github.com/ARMmbed/mbedtls.git extlibs/mbedtls/mbedtls -b mbedtls-2.4.2

scons를 이용해서 빌드할 수 있다.

안드로이드나 라즈베리파이용 빌드를 할 수 있는데, 라즈베리파이용을 위한 빌드를 하는 도중 다음과 같은 에러 메시지를 확인했다.

```
\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\* Warning \*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*
\* You are trying cross build, please make sure cross (arm) libraries are
\* installed!
\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*

Checking for C++ library boost_thread... no
Did not find boost_thread, exiting!

```
`iotivity/extlibs/boost`에 boost arm 라이브러리가 설치되지 않은 것으로 보인다. SConscript를 보니, sourceforce에서 라이브러리를 받아오는 것으로 보인다.

Android 플랫폼으로 빌드를 하니 해당 경로에 알아서 라이브러리를 가져와서 빌드하는 반면, arm Linux 플랫폼으로 빌드를 하니 위 메시지와 함께 실패한다.

https://wiki.iotivity.org/iotivity_porting_to_arm_based_platforms    
https://lists.iotivity.org/pipermail/iotivity-dev/2016-September/005531.html    
위 문서들을 참조해서 IoTivity arm 크로스 컴파일에 대해서 좀 알아보고, 최후의 방법으로는 라즈베리 파이에서 빌드를 하는 방법도 있을 것이다.
학습자료에 따르면 라즈베리파이에서 빌드를 하는 식으로 해결한 것으로 보인다. 일단 해당 방법을 적용해보도록 하자.

라즈베리파이에서 빌드를 시도하다보면 extlib에 몇개의 레포지토리를 clone 하라는 내용이 뜬다. uuid package를 못찾는 다는 내용은 다음 명령어로 해결이 가능하다.

```
apt install -y uuid-dev
```
이후 `No package 'gio-unix-2.0' found`라는 에러 메시지가 나타난다.
다음 명령어로 해결해보자
```
sudo apt-get install libglib2.0-dev
```
https://askubuntu.com/questions/281984/how-install-gio-unix-2-28-0

이제는 `No package' sqlite3' found`가 뜬다
```
apt install -y sqlite3
apt install -y libsqlite3-dev
```

이번에는 `/bin/sh: 1: autoreconf: not found`가 뜬다.
autoconf를 깔면 autoreconf도 같이 깔리는 듯 하다.
```
apt install -y autoconf
```

뭐지.. autoconf에러가 나버렸다.
`autoreconf: /usr/bin/autoconf failed with exit status: 1`
그래서 libtool이란걸 설치해봤다.
```
apt install -y libtool
```
http://sojro.tistory.com/entry/autoreconf-%EC%84%A4%EC%B9%98-%EB%B0%8F-%EC%97%90%EB%9F%AC-%ED%99%98%EA%B2%BD-%EC%88%98%EC%A0%95
요런것들 해결이 되는듯..

이제는또 다른 에러가 나타났다.
`fatal error: curl/curl.h: No such file or directory`
이것도 설치..
```
sudo apt-get install libcurl-dev
```
이게 안되면
```
sudo apt-get install libcurl4-openssl-dev
```

https://stackoverflow.com/questions/11471690/curl-h-no-such-file-or-directory

이번에는 빌드시 시스템이 뻗어버린다. 아마 메모리가 터져서 그럴 것으로 생각되므로 Swap을 붙여서 빌드해봐야 할 것 같다.

http://www.spacek.xyz/mle/?p=375
위 글을 참조해서 2GB 스왑을 붙여보았다.

그리고 멀티 쓰레드로 실행하니 자꾸 에러가 나서, 싱글 쓰레드로 빌드를 해 보았다.

시간이 오래걸리긴 했지만, 정상적으로 완료가 되었다.

이제 각각에 필요한 플랫폼에서 빌드가 되었으므로, 추후 실행을 해 보고 소스코드를 작성하도록 하자.

<a name="m3.3.3.p5" />

#### [실습-05 IoTivity 심플 서버와 심플클라이언트](https://wiki.tizen.org/images/7/71/Practice-05-Simple_Server_and_Simple_Client.pdf)

IoTivity에서 센서나 구동기같은 엔티티는 OCF 리소스 모델에 따라 리소스로써 다루어진다. 각각의 리소스는 장치 타입, CRUDN 연산, URI 정보를 갖는다. 그리고 유저는 리소스를 URI와 RESTful 인터페이스로 접근할 수 있다.    
IoTivity 네트워크 토폴로지에서 OCF 서버는 CRUDN 요청을 처리한다. 그리고 OCF 클라이언트는 리소스를 찾고 CRUDN 요청을 보낸다.    
여기서 만들어볼 심플 서버, 클라이언트 구성은 다음과 같다. 서버는 온도, 습도 센서이다. 클라이언트는 안드로이드에 설치된 어플리케이션이다. 해당 소스의 레포지토리는 다음과 같다.    
https://github.com/gudbooy/IoTivity_Sample_Things.git

해당 리포지토리의 readme 파일에는 다음과 같은 프로세스로 실시하라는 간략한 설명이 있다.

```
$ cd {iotivity_dir}/resource/examples
$ git clone https://github.com/gudbooy/IoTivity_Sample_Things.git
$ vim {iotivity_dir}/resource/examples/SConscript
Insert SConscript('IoTivity_Sample_Things/SConscript') in example's SConscript
```

IoTivity 프로젝트 디렉토리에 해당 Simple Server, Client 프로젝트를 클론한 뒤, SConscript에 스크립트를 삽입하라고 되어있다.    
하지만 여기서 스크립트를 마지막 줄에 삽입한 이후 다시 빌드를 시도하면 제대로 되지않는다. 서브 프로젝트에서 scons로 빌드를 시도할 시, SConstruct를 찾지 못하고, 따라서 SConstruct가 있는 IoTivity 프로젝트 루트에서 시도를 할 시, thread_env를 찾지 못한다는 알 수 없는 에러가 발생하면서 빌드가 되지 않는다. 아직 원인은 찾지 못하였고 따라서 해결하지도 못했다.    
해당 예제 코드를 참조하지 않더라도 IoTivity 프로젝트 자체에서 제공하는 예제 코드들이 있으므로 해당 예제 코드를 통해서 학습을 진행하기로 결정했다.


<a name="m3.3.4" />    

### 3.3.4 관련 툴 스터디

<a name="m3.3.4.1" />

#### 3.3.4.1 SCons
[공식 문서 링크](http://scons.org/doc/production/HTML/scons-user.html)    
SCons는 Software Construction Tool의 약자로, 크로스 플랫폼 빌드 툴이다. 고전적인 빌드 툴인 Make와는 다르게 기본적으로 C, C++, D, Java, Fortra, Yacc, Lex, Qt, SWIG, TeX와 같은 프로그램밍 언어와 문서들을 지원하고, 유저가 다른 언어에 대한 빌드를 원할 때에는 유저가 빌더를 정의해서 확장이 가능하다. 또한 다양한 OS, CPU 아키텍쳐를 아우르는 크로스 플랫폼 빌드를 지원한다.
구현은 실제로 파이썬으로 되어있으며, 파이썬과 유사한 SConstruct와 SConscript라는 파이썬 스크립트로 빌드 환경과 방식 등을 기술한다.    
Make와는 다르게 쉘에 있는 환경을 그대로 가져오지 않으며, 스콘스크립트 내에서 환경을 정의하고 다른 스콘스크립트로 주고받을 수 있도록 되어있다.

**[환경 설정](http://scons.org/doc/production/HTML/scons-user.html#chap-environments)**    
환경이란 프로그램 실행에 영향을 끼칠 수 있는 모든 값들의 집합으로 정의된다. SCons에서는 이러한 환경을 3가지로 분류한다. SCons에서 분류되는 환경의 종류는 다음과 같다.
1. External 환경    
유저가 SCons를 실행할 때의 환경을 뜻한다. 스콘 스클비트에서는 파이썬의 os.environ에 저장된 변수들과 관계가 있다.
2. Construction 환경    
Construction 환경은 SCons이 빌드를 하는 도중의 환경으로, 스콘스크립트 내에 생성된 객체들에 그 환경이 정의되어 있다. Construction 환경에는 타겟을 빌드하기 위해서 어떠한 행위를 해야하는지, 그리고 어떤 소스로 부터 빌드가 되어야 되는지도 포함된다. SCons의 강력한 기능 중 하나는 여러개의 Construction 환경을 만들 수 있다는 것이다. 기존의 환경을 복제하는 것도 가능하므로 기존 환경을 복제한 뒤 입맞에 맞게 바꾸어서 사용할 수 있다는 뜻이다.
3. Execution 환경    
Execution 환경은 SCons가 타겟을 빌드하기 위해 외부 커맨드(컴파일러나 링커)를 실행할 때 사용되는 값들이다. 이 값들은 External 환경과는 다르다는 것을 알고 있어야 한다.


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

**IoTivity 시뮬레이터 구동 환경 구축**    

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

**bash 쉘 환경변수 고정 설정**    
LD_LIBRARY_PATH 환경변수를 고정적으로 설정하기 위해서는 /etc/bash.bashrc 파일에 값을 지정해주어야 한다. 다음 명령어를 실행하여 설정할 수 있다.

```
$ sudo echo "export LD_LIBRARY_PATH=/home/bobgil/Desktop/iotivity/out/linux/x86_64/release:$LD_LIBRARY_PATH" >> /etc/bash.bashrc
```
위 명령어를 적용한 후 새로 여는 터미널은 해당 환경변수가 설정된 채로 생성되게 된다. 
해당 환경변수가 제대로 설정되었는지 확인하고 싶으면 다음 두가지 명령어 중 하나로 확인할 수 있다.
```
$ echo $LD_LIBRARY_PATH
```
혹은 다음 명령어로도 확인 가능하다.
```
$ env | grep LD_LIBRARY_PATH
```
env 명령어에 grep을 조합해서 사용하는 경우 LD_LIBRARY_PATH 내용을 전부 치지 않고, LD_ 정도의 해당 변수명의 부분문자열만 입력하여도 확인이 가능하다는 장점이 있다.    

bashrc 파일에 입력한 환경변수 설정은 현재 열려있는 터미널에는 곧바로 적용되지 않는다. 만약 이미 열려있는 터미널에도 해당 설정을 적용하고 싶으면 다음 명령어를 입력하면 된다.

```
$ source /etc/bash.bashrc
```

IoTivity 샘플 코드에 대한 내용은 다음 링크에 담겨있다.    
[IoTivity Examples](https://wiki.iotivity.org/examples)    

샘플 코드 중 리소스 디스커버리 관련 샘플 소스코드를 확인해보자. 해당 소스코드는 다음 경로에 존재한다.    
`iotivity/out/linux/x86_64/release/resource/examples`    
사실 일반화되면 해당 경로는 다음과 같다.    
`[ProjectDir]/[TargetOS]/[TargetArch]/[BuildMode]/resource/examples`

해당 디랙터리에 simpleclient 바이너리와 simpleserver 바이너리가 존재한다. 각각의 바이너리를 각자 다른 터미널에서 로컬 환경에서 구동해보면 어떤식으로 동작하는지 확인할 수 있다. 해당 예제를 인자 없이 실행하면, 다음과 같은 동작을 보인다.    
1. 서버는 클라이언트 요청이 오기 전까지는 Idle한 상태로 있는다. 클라이언트 요청이 오면 그에 맞는 응답을 준다.
2. 클라이언트는 실행되면 리소스(서버)를 찾는다. 리소스를 찾고 나서 Notify 요청을 통해서 Observer에 등록한다. 이후 주기적으로 서버로부터 상태를 응답받는다.    
3. 이후 등록된 옵저버가 사라지면, 서버는 다시 Idle한 상태가 된다.

simpleserver의 소스코드를 한번 참조해보았다. example이 있는 SConscript, 즉 /iotivity/resource/example/SConscript를 확인해보니, example_names라는 리스트의 엔트리들을 examples_env.Program이라는 명령어로 빌드를 한다. 단일 cpp 파일로 빌드된다고 볼 수 있다. 따라서 simpleserver 바이너리는 simpleserver.cpp를 단일 빌드한 결과물로 볼 수 있다.    
시작부분의 주석을 읽어보면, 이 샘플은 리소스를 위해 인터페이스를 정의하는 방법과, 리소스를 서버에서 제공하는 방법들을 보여준다고 한다.    

소스코드는 740라인이지만, main 함수는 100줄이 채 되지 않는다. 처음 수십 은, 커맨드라인 인자를 통해 변수를 설정하고, PlatformConfig 객채를 만든 뒤 적절한 값을 넣고, OCPlatform::Configure 함수를 이용해서 Configure을 한다.    
그리고 중간에 OC_VERIFY라는 함수가 있는데 이 함수는 No Debug 환경설정이 되지 않은 경우 assert를 하고, No Debug 환경이 설정되어 있는 경우 아무런 동작도 하지 않는 코드가 된다.    
직접 선언한 SetPlatformInfo를 통해서 스트링들로 이루어진 플랫폼 정보를 지정한 뒤, OCPlatform::registerPlatformInfo 함수를 호출해서 플랫폼 정보를 등록한다.    
그리고 SetDeviceInfo라는 함수를 호출해서 디바이스 정보를 하나하나 등록한다. SetDeviceInfo함수는 내부적으로 OCPlatform::setPropertyValue라는 함수를 값마다 등록한다.    
이후 LightResource 클래스를 통해서 리소스를 생성하고, 타입과 인터페이스를 설정한다. 그리고 mutex, condition_variable, unique_lock을 이용하는 코드가 있는데, 코드 내용을 찾아보고 해당 코드 바로 위에 있는 주석들을 확인해보니. while(true); 와 같은 pending 코드와 같지만, non-processor intensive version이라고 한다. 즉, CPU Computing Power를 잡아먹지 않는, 코드의 진행을 방해하는 코드이다.    

메인 함수를 확인해 보았을 때, simpleserver의 핵심 코드는 LightResource 클래스와 관련이 있어 보인다.    
LightResource 클래스에는 createResource라는 멤버함수가 있는데, 주석에 따르면 이 함수는 궂이 멤버 함수로 지정할 필요가 없고, free function(non-member function)이어도 상관없다고 한다. 그리고 내부적으로 OCPatform::registerResource 함수를 호출한다. 해당 함수를 호출하는 것이 리소스 등록의 핵심 로직으로 보인다. 여태까지 계속 확인해온 바로는 OCPlatform 네임스페이스의 함수들이 리소스, 플랫폼, 디바이스 등록의 핵심적인 역할을 한다.    
요청이 왔을 때 처리하는 부분은 entityHandler라는 함수로 정의한다. 특정 조건에 맞는 요청일 경우 쓰레드를 하나 만들어서 그 쓰레드에서 요청을 처리하도록 프로그래밍 되어있다.