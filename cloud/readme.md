# IoTivity Cloud

IoTivity Cloud를 통해서 Local Network가 아닌 Global Network에서 IoT 리소스에 접근할 수 있다.

### Resource Directory
IoTivity Project에 cloud/resourcedirectory에 보면 예제 코드들이 있다. readme를 참조해보면, mongoDB와 maven에 의존성이 있으므로 이들을 설치하도록 하자.

Maven 설치
```
apt install -y maven
```

MongoDB 설치
Resource Directory는 Thin-Client의 리소스 정보를 MongoDB로 저장하므로, MongoDB가 필요하다.
Ubuntu 16.04에 Community Edition을 설치하는 것을 가정한다.
[MongoDB 설치 Docs](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/)    

홈페이지 docs를 보면 glibc가 2.23-0ubuntu5 이상 버전을 사용하라고 되어있다. 오래된 glibc 버전 패키지와는 버그가 나타난다고 한다.
glibc 버전 확인은 다음과 같이 수행할 수 있다.
```
ldd --version
```
MongoDB를 설치해 보자
```
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2930ADAE8CAF5059EE73BB4B58712A2291FA4AD5
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.6 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.6.list
sudo apt-get update
sudo apt-get install -y mongodb-org

```

MongoDB가 사용하는 db 디렉토리를 만들어준다.
```
mkdir -p /data/db
```
이후 서비스에서 몽고 데몬을 확인해볼 수 있다.
```
service mongod status
```

`systemctl`을 이용해서도 확인할 수 있다.    
[systemctl 관련 웹 문서](https://www.digitalocean.com/community/tutorials/how-to-use-systemctl-to-manage-systemd-services-and-units)    
부팅 시 mongod가 시작되도록 하려면 다음과 같이 하면된다.
```
systemctl enable mongod.service
```

서비스 시작/종료/재시작/상태조회 시 다음과 같이 하면된다.
```
systemctl start mongod.service
systemctl stop mongod.service
systemctl restart mongod.service
systemctl status mongod.service
systemctl is-active mongod.service
systemctl is-enabled mongod.service
systemctl is-failed mongod.service
```

## IoTIvity Cloud 예제 실행
예제부터 시작해보자. [예제 문서](https://wiki.iotivity.org/iotivity_cloud_-_programming_guide)    

의존성 중에서 보면 apache kafka와 zookeeper가 있다. kafka자체가 zookeeper에 의존성을 갖는다. 그러므로 apache zookeeper부터 설치하자

### Apache Zookeeper
아파치 주키퍼를 설치하자. [다운로드](http://mirror.apache-kr.org/zookeeper/stable/)    

```
tar xvf zookeeper-3.4.12.tar.gz

cd zookeeper-3.4.12
bin/zkServer.sh start conf/zoo_sample.cfg
```

서버 상태 확인 시
```
bin/zkServer.sh status conf/zoo_sample.cfg
```
### Apache Kafka
아파치 카프카를 설치하자 [다운로드](http://mirror.apache-kr.org/kafka/1.1.0/kafka_2.11-1.1.0.tgz)    

```
tar xvf kafka_2.11-1.1.0.tgz
```
kafka 서버 실행 시
```
bin/zookeeper-server-start.sh config/zookeeper.properties
```

### IoTivity Cloud Sample
Account server 실행 시 조심해야 할 부분이, target경로에서 실행하면 properties/config.properties를 찾지 못하므로 다음 경로에서 실행한다.

```
cd /home/bobgil/Desktop/graduation_work/iotivity/cloud/account/
java -jar target/CloudAccount-0.0.1-SNAPSHOT.jar 5685 127.0.0.1 27017 0
```

그리고 log4j 설정이 제대로 되지 않아서 다음과 같은 워닝이 발생하므로 주의한다.
```
WARN No appenders could be found for logger (org.iotivity.cloud.util.Log).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

```

IoTivity Cloud Sample 빌드 시 다음 커맨드로 할 수 있다.
```
scons cloud WITH_TCP=yes RELEASE=yes TARGET_TRANSPORT=IP WITH_CLOUD=yes WITH_MQ=PUB,SUB SECURED=0
```
SECURED=0는 TLS를 적용하지 않는 다는 뜻이며, 인증서와 같이 보안 설정이 제대로 되지 않았으므로 SECURED=0를 설정 하지 않은채로 실행 시 UNAUTHORIZED 에러가 날 수 있다.

uid는 user_id로 추정된다. 같은 값을 갖는다.