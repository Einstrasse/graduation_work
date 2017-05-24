# graduation_work

### 1. 주제
IoTivity를 활용한 스마트 알람 시스템 구축

### 2. 진행
#### 1. IoTivity 빌드
IoTivity를 활용하여 프로젝트가 진행 될 것이므로, IoTivity를 빌드 및 실행해보아야 한다.    
IoTivity 빌드는 우분투 16.04 환경에서 진행한다.    
[IoTivity 우분투 빌드 가이드](https://wiki.iotivity.org/build_iotivity_with_ubuntu_build_machine)    
[IoTivity 빌드 가이드](https://wiki.iotivity.org/build_for_your_system)    

**빌드 환경 구축 커맨드**

    $ sudo apt-get install \
    build-essential git scons libtool autoconf \
	valgrind doxygen wget zip
	
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

**Executing Samples**

    $ LD_LIBRARY_PATH=/your/library/path ./IotivitySample
	
To execute sample code, you should specify the path of shared object files. You can specify the path with enviromenta variable name "LD_LIBRARY_PATH"