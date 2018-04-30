# Resource Directory

[리소스 디렉토리 문서](https://wiki.iotivity.org/resource-directory_rd)    


### 1. Resource Hosting과 Resource Directory 비교
- Resource Hosting : 리소스 호스팅은 Rich device가 Light Device에 대하여 subscribe를 해서 상태가 변할 때 알림을 받고, 그 리소스에 대한 정보를 자신이 캐싱하고 있는 것이다(Mirror Server). Consumer, Resource Hosting, Provider의 3개의 컴포넌트로 이루어진 아키텍쳐로 이해하면 된다. Resource Hosting이 Provider의 정보를 가지고 있다가(Caching) Consumer가 필요로 할 때 Hosting이 알려준다. 실제 리소스와 같은 정보를 같이 가지고 있는다. request에 대한 업무를 분담해준다.

- Resource Hosting과 유사하지만 thin 장비를 위해 요청을 처리한다. Resource Directory가 Thin Device에게 오는 request를 대신 response한다. 대신해서 일을 처리한다.(Cached Server)

### 2. Resource Directory
IoTivity Resource Directory에 대한 설명이다.

현재 IoTivity에서 Resource Directory 특징은 IoTivity Cloud에 적용되어 있다. 하지만 IoTivity에는 Resource Directory 관련된 API가 없다. 우리가 RD와 관련된 새로운 API를 지원함에 따라, 제 3자는 D2S와 로컬(D2D)에서 API를 사용할 수 있다. 현재 RD API는 D2S를 위해 개발되었다. 로컬 RD 서버 API는 현재 지원되지 않는다.

리소스 디렉토리는 thin-클라이언트를 대신하여 행동하는 서버이다. 리소스 디렉토리는 자신의 리소스를 publish한 thin-client를 대신하여 요청에 응답하게 된다. 리소스 디렉토리로서 동작하는 장비는 리소스를 가지고 있을 수 있다.
thin-클라이언트는 파워와 리소스가 제한된 OIC 서버이다. thin-클라이언트는 자신의 리소스를 발행하여 다른 유저의 네트워크에서도 discoverable하게 하도록 하기 위해 리소스 디렉토리 서비스를 이용한다.
쿼리를 보내는 클라이언트는 리소스를 찾는 OIC 클라이언트 장비이다. 이 클라이언트는 /oic/res에서 리소스를 찾기 위해 멀티케스트 패킷을 보낸다.
리소스 디렉토리는 thin-client 를 대신하여 응답한다. 리소스 응답 구조는 OIC /oic/res 응답 구조에 기반한다.