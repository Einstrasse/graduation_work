/*
 * lightserver.cpp
 * Author: Einstrasse
 * Create date: 2018-4-12
 * Comment: Alarm Server to handle scheduled light switch mainipulation
 */

#include <iostream>
#include <string>
#include <sqlite3.h>

#include <vector>
#include "iotivity_config.h"

#include <functional>
#ifdef HAVE_UNISTD_H
#include <unistd.h>
#endif
#ifdef HAVE_PTHREAD_H
#include <pthread.h>
#endif
#include <mutex>
#include <condition_variable>

#include "OCPlatform.h"
#include "OCApi.h"
#ifdef HAVE_WINDOWS_H
#include <windows.h>
#endif

#include "ocpayload.h"

using namespace OC;
using namespace std;
namespace PH = std::placeholders;
static const char* SVR_DB_FILE_NAME = "./alarm_svr_db.dat";
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
string  deviceName = "IoTivity Alarm Server";
string  deviceType = "oic.wk.d";
string  specVersion = "ocf.1.1.0";
vector<string> dataModelVersions = {"ocf.res.1.1.0", "ocf.sh.1.1.0"};
string  protocolIndependentID = "fa008167-3bbf-4c9d-8604-c9bcb96cb712";

// OCPlatformInfo Contains all the platform info to be stored
OCPlatformInfo platformInfo;

void PrintIntro() {
    cout << endl;
    cout << "------ IoTivity Alarm Server -----\n";
    cout << "Manipulate Alarm Schedules\n";
    cout << "Supported Method: GET / POST / PUT / DELETE\n";
    cout << "1. GET\t- Fetch scheduled alarm data\n";
    cout << "2. POST\t- Add new scheduled alarm\n";
    cout << "3. PUT\t- Modify Alarm schedule\n";
    cout << "4. DELETE\t- Delete previously set Alarm schedule\n";
}

void DeletePlatformInfo()
{
    delete[] platformInfo.platformID;
    delete[] platformInfo.manufacturerName;
    delete[] platformInfo.manufacturerUrl;
    delete[] platformInfo.modelNumber;
    delete[] platformInfo.dateOfManufacture;
    delete[] platformInfo.platformVersion;
    delete[] platformInfo.operatingSystemVersion;
    delete[] platformInfo.hardwareVersion;
    delete[] platformInfo.firmwareVersion;
    delete[] platformInfo.supportUrl;
    delete[] platformInfo.systemTime;
}

void DuplicateString(char ** targetString, std::string sourceString) {
    *targetString = new char[sourceString.length() + 1];
    strncpy(*targetString, sourceString.c_str(), (sourceString.length() + 1));
}

OCStackResult SetPlatformInfo(std::string platformID, std::string manufacturerName,
        std::string manufacturerUrl, std::string modelNumber, std::string dateOfManufacture,
        std::string platformVersion, std::string operatingSystemVersion,
        std::string hardwareVersion, std::string firmwareVersion, std::string supportUrl,
        std::string systemTime) {
    DuplicateString(&platformInfo.platformID, platformID);
    DuplicateString(&platformInfo.manufacturerName, manufacturerName);
    DuplicateString(&platformInfo.manufacturerUrl, manufacturerUrl);
    DuplicateString(&platformInfo.modelNumber, modelNumber);
    DuplicateString(&platformInfo.dateOfManufacture, dateOfManufacture);
    DuplicateString(&platformInfo.platformVersion, platformVersion);
    DuplicateString(&platformInfo.operatingSystemVersion, operatingSystemVersion);
    DuplicateString(&platformInfo.hardwareVersion, hardwareVersion);
    DuplicateString(&platformInfo.firmwareVersion, firmwareVersion);
    DuplicateString(&platformInfo.supportUrl, supportUrl);
    DuplicateString(&platformInfo.systemTime, systemTime);

    return OC_STACK_OK;
}

OCStackResult SetDeviceInfo()
{
    OCStackResult result = OC_STACK_ERROR;

    OCResourceHandle handle = OCGetResourceHandleAtUri(OC_RSRVD_DEVICE_URI);
    if (handle == NULL)
    {
        cout << "Failed to find resource " << OC_RSRVD_DEVICE_URI << endl;
        return result;
    }

    result = OCBindResourceTypeToResource(handle, deviceType.c_str());
    if (result != OC_STACK_OK)
    {
        cout << "Failed to add device type" << endl;
        return result;
    }

    result = OCPlatform::setPropertyValue(PAYLOAD_TYPE_DEVICE, OC_RSRVD_DEVICE_NAME, deviceName);
    if (result != OC_STACK_OK)
    {
        cout << "Failed to set device name" << endl;
        return result;
    }

    result = OCPlatform::setPropertyValue(PAYLOAD_TYPE_DEVICE, OC_RSRVD_DATA_MODEL_VERSION,
                                          dataModelVersions);
    if (result != OC_STACK_OK)
    {
        cout << "Failed to set data model versions" << endl;
        return result;
    }

    result = OCPlatform::setPropertyValue(PAYLOAD_TYPE_DEVICE, OC_RSRVD_SPEC_VERSION, specVersion);
    if (result != OC_STACK_OK)
    {
        cout << "Failed to set spec version" << endl;
        return result;
    }

    result = OCPlatform::setPropertyValue(PAYLOAD_TYPE_DEVICE, OC_RSRVD_PROTOCOL_INDEPENDENT_ID,
                                          protocolIndependentID);
    if (result != OC_STACK_OK)
    {
        cout << "Failed to set piid" << endl;
        return result;
    }

    return OC_STACK_OK;
}

static FILE* client_open(const char* path, const char* mode)
{
    char const * filename = path;
    if (0 == strcmp(path, OC_SECURITY_DB_DAT_FILE_NAME))
    {
        filename = SVR_DB_FILE_NAME;
    }
    else if (0 == strcmp(path, OC_INTROSPECTION_FILE_NAME))
    {
        filename = "lightserver_introspection.dat";
    }
    return fopen(filename, mode);
}

class WeeklyAlarmHandler;
class WeeklyAlarm;
class AlarmHandler;
static int sqliteSelectCallback(void* arg, int num_col, char** col_val, char** col_name);
static int sqliteInsertCallback(void* arg, int num_col, char** col_val, char** col_name);
static int sqliteUpdateCallback(void* arg, int num_col, char** col_val, char** col_name);

//Class for Genarlly used for Alarm Data
class AlarmHandler {
public:
	static string serialize(vector<WeeklyAlarm>& weeklyList);
	
};

//Weekly Alarm Item Object
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
	string serialize() {
		return "{\"id\":" + to_string(m_id) + ",\"name\":\"" + m_name + "\",\"hour\":" + to_string(m_hour)+
		",\"min\":" + to_string(m_min) + ",\"day\":" + to_string(m_day) + ",\"enabled\":" + (m_enabled ? "true" : "false") +"}";
	}


};

//Actually OIC Resource object. Contains and Handle many Weekly alarm data.
class WeeklyAlarmHandler {
private:
	string m_name;
	int m_alarmCount;
	// int m_hour;
	// int m_min;
	// int m_day;
	string m_serializedData;
	string m_resourceURI;
	OCResourceHandle m_resourceHandle;
    OCRepresentation m_resourceRep;

    OCEntityHandlerResult entityHandler(shared_ptr<OCResourceRequest> request) {
    	cout << "\tIn Server CPP entity handler:\n";
    	OCEntityHandlerResult ehResult = OC_EH_ERROR;
    	if (request) {
    		string requestType = request->getRequestType();
    		int requestFlag = request->getRequestHandlerFlag();

    		if (requestFlag & RequestHandlerFlag::RequestFlag) {
    			cout << "\t\trequestFlag : Request\n";
    			auto pResponse = std::make_shared<OC::OCResourceResponse>();
                pResponse->setRequestHandle(request->getRequestHandle());
                pResponse->setResourceHandle(request->getResourceHandle());

                // Check for query params (if any)
                QueryParamsMap queries = request->getQueryParameters();

                if (!queries.empty()) {
                    std::cout << "\nQuery processing upto entityHandler" << std::endl;
                }
                for (auto it : queries) {
                    std::cout << "Query key: " << it.first << " value : " << it.second
                            << std:: endl;
                }

                // If the request type is GET
                if(requestType == "GET") {
                    cout << "\t\t\trequestType : GET\n";
                    pResponse->setResponseResult(OC_EH_OK);
                    pResponse->setResourceRepresentation(get());
                    if(OC_STACK_OK == OCPlatform::sendResponse(pResponse)) {
                        ehResult = OC_EH_OK;
                    }
                } else if (requestType == "POST") {
                	cout << "\t\t\trequestType : POST\n";
                	OCRepresentation rep = request->getResourceRepresentation();

                	pResponse->setResponseResult(OC_EH_OK);
                	// pResponse->setResourceRepresentation(post(rep));
                	pResponse->setResourceRepresentation(post(queries));
                	if (OC_STACK_OK == OCPlatform::sendResponse(pResponse)) {
                		ehResult = OC_EH_OK;
                	}
                } else if (requestType == "PUT") {
                	cout << "\t\t\trequestType : PUT\n";
                	OCRepresentation rep = request->getResourceRepresentation();

                	pResponse->setResponseResult(OC_EH_OK);
                	pResponse->setResourceRepresentation(put(rep));
                	if (OC_STACK_OK == OCPlatform::sendResponse(pResponse)) {
                		ehResult = OC_EH_OK;
                	}
                } else if (requestType == "DELETE") {
                	cout << "\t\t\trequestType : DELETE\n";
                	OCRepresentation rep = request->getResourceRepresentation();

                	pResponse->setResponseResult(OC_EH_OK);
                	pResponse->setResourceRepresentation(delete_(rep));
                	if (OC_STACK_OK == OCPlatform::sendResponse(pResponse)) {
                		ehResult = OC_EH_OK;
                	}
                }
    		}
    	} else {
    		cout << "Invalid Request" << endl;
    	}
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
	static string serialize(vector<WeeklyAlarm>& arr) {
		string ret = "[";
		for (size_t i=0; i < arr.size(); i++) {
			auto& it = arr[i];
			ret += it.serialize();
			if ( i < arr.size() - 1) {
				ret += ",";
			}
		}
		ret += "]";
		return ret;
	}

	OCRepresentation get() {
		sqlite3 *db;
		char *errMsg = 0;
		int rc = sqlite3_open(SQLITE3_ALARM_DB_PATH, &db);
		vector<WeeklyAlarm> weeklyAlarmList;

		if (rc) {
			fprintf(stderr, "SQLite3 file open failed... :%s\n", SQLITE3_ALARM_DB_PATH);
			m_resourceRep.setValue("alarmCount", -1);
			m_resourceRep.setValue("serializedData", "{}");
			return m_resourceRep;
		} else {
			printf("SQLite3 file open success!\n");
		}

		const char *sql = "SELECT `id`, `name`, `hour`, `min`, `day`, `enabled` FROM weekly_alarm;";

		rc = sqlite3_exec(db, sql, sqliteSelectCallback, (void*) &weeklyAlarmList, &errMsg);
		printf("Sqlite3 Exec function returned..\n");
		if (SQLITE_OK != rc) {
			fprintf(stderr, "Sqlite3 sql query failed.. %s\n", errMsg);
			sqlite3_free(errMsg);
			sqlite3_close(db);
			m_resourceRep.setValue("alarmCount", -1);
			m_resourceRep.setValue("serializedData", "{}");
			return m_resourceRep;
		} else {
			printf("Sqlite query success\n");
		}

		
		sqlite3_close(db);

		m_resourceRep.setValue("alarmCount", (int)weeklyAlarmList.size());
		m_resourceRep.setValue("serializedData", AlarmHandler::serialize(weeklyAlarmList));
		// TODO: dummy for iotivity simulator. if it is not need, erase it!
		m_resourceRep.setValue("hour", 0);
		m_resourceRep.setValue("min", 0);
		m_resourceRep.setValue("day", 0);
		m_resourceRep.setValue("m_id", 0);
		m_resourceRep.setValue("enabled", 0);
		return m_resourceRep;
	}

	OCRepresentation post(QueryParamsMap& qry) {
		sqlite3 *db;
		char *errMsg = 0;
		int rc = sqlite3_open(SQLITE3_ALARM_DB_PATH, &db);
		if (rc) {
			fprintf(stderr, "SQLite3 file open failed... :%s\n", SQLITE3_ALARM_DB_PATH);
			m_resourceRep.setValue("alarmCount", -1);
			m_resourceRep.setValue("serializedData", "{}");
			return m_resourceRep;
		} else {
			printf("SQLite3 file open success!\n");
		}
		char sql[512];
		string name = "Alarm";
		string hour = "";
		string min = "";
		string day = "";

		if (qry.find("name") == qry.end()) {
			name = "Alarm";
		} else {
			name = qry["name"];
		}

		if (qry.find("hour") == qry.end() || qry.find("min") == qry.end() || qry.find("day") == qry.end()) {
			sqlite3_close(db);
			return get();
		} else {
			hour = qry["hour"];
			min = qry["min"];
			day = qry["day"];
		}
		// if(!rep.getValue("name", name)) {
		// 	name = "Alarm";
		// }
		// if (!rep.getValue("hour", hour) || !rep.getValue("min", min) || !rep.getValue("day", day)) {
		// 	sqlite3_close(db);
		// 	return get();
		// }


		snprintf(sql, 511, "INSERT INTO weekly_alarm(`name`, `hour`, `min`, `day`) VALUES(\"%s\", %s, %s, %s);"
			, name.c_str(), hour.c_str(), min.c_str(), day.c_str());

		rc = sqlite3_exec(db, sql, sqliteInsertCallback, (void*)0, &errMsg);
		printf("Sqlite3 Exec function returned..\n");
		if (SQLITE_OK != rc) {
			fprintf(stderr, "Sqlite3 sql query failed.. :%s\n", errMsg);
			sqlite3_free(errMsg);
			sqlite3_close(db);
			return get();
			// m_resourceRep.setValue("alarmCount", -1);
			// m_resourceRep.setValue("serializedData", "{}");
			// return m_resourceRep;
		} else {
			printf("Sqlite query success\n");
		}

		sqlite3_close(db);

		return get();
	}

	OCRepresentation put(OCRepresentation& rep) {
		sqlite3 *db;
		char *errMsg = 0;
		int rc = sqlite3_open(SQLITE3_ALARM_DB_PATH, &db);
		if (rc) {
			fprintf(stderr, "SQLite3 file open failed... :%s\n", SQLITE3_ALARM_DB_PATH);
			m_resourceRep.setValue("alarmCount", -1);
			m_resourceRep.setValue("serializedData", "{}");
			return m_resourceRep;
		} else {
			printf("SQLite3 file open success!\n");
		}
		string name = "-1";
		int id = -1;
		int hour = -1;
		int min = -1;
		int day = -1;
		int enabled = -1;
		rep.getValue("name", name);
		rep.getValue("enabled", enabled);

		if (!rep.getValue("m_id", id) || !rep.getValue("hour", hour) || !rep.getValue("min", min) || !rep.getValue("day", day)) {
			sqlite3_close(db);
			return get();
		}

		string sql = "UPDATE weekly_alarm SET ";

		vector<string> params;
		if ("-1" != name) {
			params.push_back("`name`=\"" + name + "\"");
		}
		if (-1 != hour) {
			params.push_back("`hour`=" + to_string(hour));
		}
		if (-1 != min) {
			params.push_back("`min`=" + to_string(min));
		}
		if (-1 != day) {
			params.push_back("`day`=" + to_string(day));
		}
		if (-1 != enabled) {
			params.push_back("`enabled`=" + to_string(enabled));
		}
		for (size_t i = 0; i < params.size(); i++) {
			sql += params[i];
			if (i < params.size() - 1) {
				sql += ", ";
			}
		}
		sql += " WHERE `id`=" + to_string(id) + ";";
		// UPDATE weekly_alarm
		// SET `name`="asdf", `hour`=8
		// WHERE `id`=5;

		cout << "SQL!! " << sql << endl;

		rc = sqlite3_exec(db, sql.c_str(), sqliteUpdateCallback, (void*)0, &errMsg);
		printf("Sqlite3 Exec function returned..\n");
		if (SQLITE_OK != rc) {
			fprintf(stderr, "Sqlite3 sql query failed %s\n", errMsg);
			sqlite3_free(errMsg);
			sqlite3_close(db);
			return get();
			// m_resourceRep.setValue("alarmCount", -1);
			// m_resourceRep.setValue("serializedData", "{}");
			// return m_resourceRep;
		} else {
			printf("Sqlite query success\n");
		}

		sqlite3_close(db);

		return get();
	}

	OCRepresentation delete_(OCRepresentation& rep) {
		sqlite3 *db;
		char *errMsg = 0;
		int rc = sqlite3_open(SQLITE3_ALARM_DB_PATH, &db);
		if (rc) {
			fprintf(stderr, "SQLite3 file open failed... :%s\n", SQLITE3_ALARM_DB_PATH);
			m_resourceRep.setValue("alarmCount", -1);
			m_resourceRep.setValue("serializedData", "{}");
			return m_resourceRep;
		} else {
			printf("SQLite3 file open success!\n");
		}
		int id = -1;
		if (!rep.getValue("m_id", id)) {
			sqlite3_close(db);
			return get();
		}
		string sql = "DELETE FROM weekly_alarm WHERE `id`=" + to_string(id) + ";";

		rc = sqlite3_exec(db, sql.c_str(), sqliteUpdateCallback, (void*)0, &errMsg);
		printf("Sqlite3 Exec function returned..\n");
		if (SQLITE_OK != rc) {
			fprintf(stderr, "Sqlite3 sql query failed %s\n", errMsg);
			sqlite3_free(errMsg);
			sqlite3_close(db);
			return get();
			// m_resourceRep.setValue("alarmCount", -1);
			// m_resourceRep.setValue("serializedData", "{}");
			// return m_resourceRep;
		} else {
			printf("Sqlite query success\n");
		}

		sqlite3_close(db);

		return get();
	}

	void addType(const std::string& type) const {
        OCStackResult result = OCPlatform::bindTypeToResource(m_resourceHandle, type);
        if (OC_STACK_OK != result)
        {
            cout << "Binding TypeName to Resource was unsuccessful\n";
        }
    }

    void addInterface(const std::string& iface) const {
        OCStackResult result = OCPlatform::bindInterfaceToResource(m_resourceHandle, iface);
        if (OC_STACK_OK != result)
        {
            cout << "Binding TypeName to Resource was unsuccessful\n";
        }
    }
};

int main() {

	PrintIntro();
	OCPersistentStorage ps {client_open, fread, fwrite, fclose, unlink };
    //Platform setting and start
    PlatformConfig cfg {
        OC::ServiceType::InProc,
        OC::ModeType::Server,
        &ps
    };
    cfg.transportType = static_cast<OCTransportAdapter>(OCTransportAdapter::OC_ADAPTER_IP | 
                                                        OCTransportAdapter::OC_ADAPTER_TCP);
    cfg.QoS = OC::QualityOfService::LowQos;
    OCPlatform::Configure(cfg);
    OC_VERIFY(OCPlatform::start() == OC_STACK_OK);
    std::cout << "Setting Platform Info finished. Server Starts....\n";

    OCStackResult result = SetPlatformInfo(gPlatformId, gManufacturerName, gManufacturerLink,
            gModelNumber, gDateOfManufacture, gPlatformVersion, gOperatingSystemVersion,
            gHardwareVersion, gFirmwareVersion, gSupportLink, gSystemTime);

    result = OCPlatform::registerPlatformInfo(platformInfo);

    if (result != OC_STACK_OK)
    {
        std::cout << "Platform Registration failed\n";
        return -1;
    } else {
        std::cout << "Platform Registration succeed\n";
    }

    result = SetDeviceInfo();

    if (result != OC_STACK_OK)
    {
        std::cout << "Device Registration failed\n";
        return -1;
    } else {
        std::cout << "Device Registration succeed\n";
    }

    try
    {
        // Create the instance of the resource class
        // (in this case instance of class 'LightResource').
        WeeklyAlarmHandler weeklyAlarmHandler;

        // Invoke createResource function of class light.
        weeklyAlarmHandler.createResource();
        std::cout << "Created resource." << std::endl;

        weeklyAlarmHandler.addType(std::string("core.alarm"));
        weeklyAlarmHandler.addInterface(std::string(LINK_INTERFACE));
        std::cout << "Added Interface and Type" << std::endl;

        DeletePlatformInfo();

        // A condition variable will free the mutex it is given, then do a non-
        // intensive block until 'notify' is called on it.  In this case, since we
        // don't ever call cv.notify, this should be a non-processor intensive version
        // of while(true);
        std::mutex blocker;
        std::condition_variable cv;
        std::unique_lock<std::mutex> lock(blocker);
        std::cout <<"Waiting" << std::endl;
        cv.wait(lock, []{return false;});
    }
    catch(OCException &e)
    {
        std::cout << "OCException in main : " << e.what() << endl;
    }

	//debug mode
	// WeeklyAlarmHandler weeklyAlarmHandler;

	// weeklyAlarmHandler.get();

	OC_VERIFY(OCPlatform::stop() == OC_STACK_OK);
	return 0;
}

static int sqliteSelectCallback(void* arg, int num_col, char** col_val, char** col_name) {
	(void)col_name;
	(void)num_col;
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

static int sqliteInsertCallback(void* arg, int num_col, char** col_val, char** col_name) {
	(void)arg;
	//debug output
	cout << "========== sqliteInsertCallback" << '\n';
	for (int i=0; i < num_col; i++) {
		printf("%s: %s\n", col_name[i], col_val[i]);
	}
	cout << "sqliteInsertCallback ============" << '\n';
	return 0;
}

static int sqliteUpdateCallback(void* arg, int num_col, char** col_val, char** col_name) {
	(void)arg;
	(void)num_col;
	(void)col_val;
	(void)col_name;
	return 0;
}

string AlarmHandler::serialize(vector<WeeklyAlarm>& weeklyList) {
	string ret = "{\"weekly\":";
	ret += WeeklyAlarmHandler::serialize(weeklyList);
	ret += "}";
	return ret;
}
