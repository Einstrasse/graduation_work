# IoTivity Cloud

IoTivity Cloud를 통해서 Local Network가 아닌 Global Network에서 IoT 리소스에 접근할 수 있다.

### Resource Directory
IoTivity Project에 cloud/resourcedirectory에 보면 예제 코드들이 있다. readme를 참조해보면, mongoDB와 maven에 의존성이 있으므로 이들을 설치하도록 하자.

Maven 설치
```
apt install -y maven
```

MongoDB 설치
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