/*
 * lightserver.cpp
 * Author: Einstrasse
 * Create date: 2018-4-12
 * Comment: Alarm Server to handle scheduled light switch mainipulation
 */

#include <iostream>
#include <string>
#include <sqlite3.h>
#include <functional>
#include <vector>
#include "iotivity_config.h"
#include "OCPlatform.h"
#include "OCApi.h"

using namespace OC;
using namespace std;
namespace PH = std::placeholders;

static const char* PERSISTENT_STORAGE_PATH = "./alarmserver_persistent_storage.dat";
static const char* SQLITE3_ALARM_DB_PATH = "./sqlite3_alarm_db.db";

// Set of strings for each of platform Info fields
string gPlatformId = "DEADBEEF-CAFE-EEEE-0000-000000000001";
string gManufacturerName = "OCF";
string gManufacturerLink = "http://eine.tistory.com";
string gModelNumber = "myModelNumber";
string gDateOfManufacture = "2018-03-24";
string gPlatformVersion = "myPlatformVersion";
string gOperatingSystemVersion = "myOS";
string gHardwareVersion = "myHardwareVersion";
string gFirmwareVersion = "1.0";
string gSupportLink = "http://eine.tistory.com";
string gSystemTime = "2018-03-24T11.01";

// Set of strings for each of device info fields
string  deviceName = "IoTivity Light Server";
string  deviceType = "oic.wk.d";
string  specVersion = "ocf.1.1.0";
vector<string> dataModelVersions = {"ocf.res.1.1.0", "ocf.sh.1.1.0"};
string  protocolIndependentID = "fa008167-3bbf-4c9d-8604-c9bcb96cb712";

// OCPlatformInfo Contains all the platform info to be stored
OCPlatformInfo platformInfo;

class WeeklyAlarm;
static int sqliteExecCallback(void* arg, int num_col, char** col_val, char** col_name);

class WeeklyAlarm {
private:
	int m_id;
	string m_name;
	int m_hour, m_min, m_day;
	bool m_enabled;

public:
	WeeklyAlarm()
		:m_id(-1), m_name("Default Weekly Alarm"), m_hour(8), m_min(0),
		m_day(0), m_enabled(false){}
	WeeklyAlarm(int id, string name, int hour, int min, int day, bool enabled) {
		m_id = id;
		m_name = name;
		m_hour = hour;
		m_min = min;
		m_day = day;
		m_enabled = enabled;
	}
	int getId() { return m_id; }
	string getName() { return m_name; }
	int getHour() { return m_hour; }
	int getMin() { return m_min; }
	int getDay() { return m_day; }
	bool getEnabled() { return m_enabled; }

	void setId(int id) { m_id = id; }
	void setName(string name) { m_name = name; }
	void setHour(int hour) {
		if (hour < 0 || hour > 24) {
			fprintf(stderr, "Invalid setHour value (%d)\n", hour);
			return;
		}
		m_hour = hour;
	}
	void setMin(int min) {
		if (min < 0 || min > 60) {
			fprintf(stderr, "Invalid setMin value (%d)\n", min);
			return;
		}
		m_min = min;
	}
	void setDay(int day) {
		if (day < 0 || day >= 128) {
			fprintf(stderr, "Invalid setDay value (%d)\n", day);
			return;
		}
		m_day = day;
	}
	void setEnabled(bool enabled) { m_enabled = enabled; }
	void dumpData() {
		cout << "--------------------------------\n";
		cout << "\tid: " << m_id << '\n';
		cout << "\tname: " << m_name << '\n';
		cout << "\thour: " << m_hour << '\n';
		cout << "\tmin: " << m_min << '\n';
		cout << "\tday: " << m_day << '\n';
		cout << "\tenabled: " << m_enabled << '\n';
		cout << "--------------------------------\n";
	}


};
class WeeklyAlarmHandler {
private:
	string m_name;
	int m_alarmCount;
	string m_serializedData;
	string m_resourceURI;
	OCResourceHandle m_resourceHandle;
    OCRepresentation m_resourceRep;

    OCEntityHandlerResult entityHandler(shared_ptr<OCResourceRequest> request) {
    	cout << "\tIn Server CPP entity handler:\n";
    	OCEntityHandlerResult ehResult = OC_EH_ERROR;

    	return ehResult;
    }

public:
    

	WeeklyAlarmHandler()
		:m_name("Weekly Alarms"), m_alarmCount(0), m_serializedData("{}"), 
		m_resourceURI("/eine/alarm/weekly"), m_resourceHandle(nullptr) {
		//Initialize representation
		m_resourceRep.setUri(m_resourceURI);
		m_resourceRep.setValue("alarmCount", m_alarmCount);
		m_resourceRep.setValue("serializedData", m_serializedData);
		m_resourceRep.setValue("name", m_name);
	}

	OCStackResult createResource() {
		string resourceURI = m_resourceURI;
		string resourceTypeName = "core.alarm.weekly";
		string resourceInterface = DEFAULT_INTERFACE;
		uint8_t resourceProperty = OC_DISCOVERABLE;

		EntityHandler cb = bind(&WeeklyAlarmHandler::entityHandler, this, PH::_1);

		OCStackResult result = OCPlatform::registerResource(
									m_resourceHandle, resourceURI, resourceTypeName,
									resourceInterface, cb, resourceProperty);

		if (OC_STACK_OK != result) {
			cout << "Resource creation was Failed\n";
		} else {
			cout << "Resource creation success!\n";
		}

		return result;
	}

	OCRepresentation get() {
		sqlite3 *db;
		char *errMsg = 0;
		int rc = sqlite3_open(SQLITE3_ALARM_DB_PATH, &db);
		vector<WeeklyAlarm> alarmList;

		if (rc) {
			fprintf(stderr, "SQLite3 file open failed... :%s\n", SQLITE3_ALARM_DB_PATH);
			m_resourceRep.setValue("alarmCount", -1);
			return m_resourceRep;
		} else {
			printf("SQLite3 file open success!\n");
		}

		char *sql = "SELECT `id`, `name`, `hour`, `min`, `day`, `enabled` FROM weekly_alarm;";

		rc = sqlite3_exec(db, sql, sqliteExecCallback, (void*) &alarmList, &errMsg);
		printf("Sqlite3 Exec function returned..\n");
		if (SQLITE_OK != rc) {
			fprintf(stderr, "Sqlite3 sql query failed\n");
			sqlite3_free(errMsg);
			m_resourceRep.setValue("alarmCount", -1);
			return m_resourceRep;
		} else {
			printf("Sqlite query success\n");
		}

		//debug dump sql queried rows
		for (size_t i=0; i < alarmList.size(); i++) {
			alarmList[i].dumpData();
		}

		//TODO : JSON serialization of queried row

		sqlite3_close(db);
		return m_resourceRep;
	}


};

int main() {
	//debug mode
	WeeklyAlarmHandler weeklyAlarmHandler;

	weeklyAlarmHandler.get();
	return 0;
}

static int sqliteExecCallback(void* arg, int num_col, char** col_val, char** col_name) {
	vector<WeeklyAlarm> *listPtr = (vector<WeeklyAlarm>*) arg;
	WeeklyAlarm item;
	item.setId(atoi(col_val[0]));
	item.setName(string(col_val[1]));
	item.setHour(atoi(col_val[2]));
	item.setMin(atoi(col_val[3]));
	item.setDay(atoi(col_val[4]));
	item.setEnabled(strcmp(col_val[5], "1") == 0 ? true : false);
	listPtr->push_back(item);
	return 0;
}
