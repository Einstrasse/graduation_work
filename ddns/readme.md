# DDNS

IoTivity Cloud 기능을 활용하기 위해서 공인 아이피가 필요하다. 따라서 DDNS를 달았다.

도메인은 가비아에서 구매하였고, DDNS은 DNSEver를 통하여 서비스를 받았다.
    
[DNSEver](https://kr.dnsever.com/)    
[가비아](https://gabia.com/)    

구매한 도메인은 bobgil.xyz이다.

DDNS는 리눅스용 프로그램이 별도로 있으나 라즈베리파이 2에서 구동시킬 것이므로 crontab으로 30분 마다 wget을 이용하기로 하였다.    
[DNSEver DDNS 가이드](https://support.dnsever.com/hc/ko/articles/219666827)    

crontab은 `crontab -e` 명령어로 설정할 수 있다.

```
15,45 * * * * /home/pi/Projects/ddns.sh
```

`/home/pi/Projects/ddns.sh` 파일은 다음과 같다.
```
#!/bin/bash

IP=`curl ipinfo.io/ip`
echo $IP
HOST=http://dyna.dnsever.com/update.php?host[bobgil.xyz]=$IP
echo $HOST
USER_ID=hg9587
PASSWORD=AAAAAAAAAA

CMD="-O --http-user=$USER_ID --http-passwd=$PASSWORD '$HOST'"
echo $CMD
wget "${HOST}" -O - --http-user=$USER_ID --http-passwd=$PASSWORD
```