# IoTivity

## lightserver

## alarmserver

#### GET
설정된 weekly alarm 데이터를 불러온다.    
no param

#### POST
새로운 알람을 추가한다.    
4 params
- name
- hour
- min
- day

#### PUT
설정된 알람의 설정 값을 변경하거나 삭제한다.  
6 params
- name
- hour
- min
- day
- m_id
- enabled
- delete

delete 파라메터가 활성화 되어 있으면 m_id에 해당하는 알람을 삭제한다.
