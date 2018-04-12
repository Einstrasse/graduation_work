#include <cstdio>
#include <sqlite3.h>
#include <cstdlib>
#include <time.h>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

const char* db_file_path = "./test.db";

const int DAY_CODE[] = {64, 1, 2, 4, 8, 16, 32};

static int callback(void* arg, int num_col, char** col_val, char **col_name) {
	vector<vector<string> >* queried_data = (vector<vector<string> >*)arg;
	vector<string> item;

	printf("----------- Print Row ------------\n");
	for (int i=0; i < num_col; i++) {
		item.push_back(string(col_val[i]));
		printf("%s: %s\n", col_name[i], col_val[i]);
	}
	printf("======== Print Row End =========\n");
	queried_data->push_back(item);
	item.clear();
	return 0;
}

vector<vector<string> > queried_data;

int main(int argc, char** argv) {
	sqlite3 *db;
	char *zErrMsg = 0;
	int rc = sqlite3_open(db_file_path, &db);
	int day_code;
	time_t raw_time;
	tm * time_info;
	time(&raw_time);
	time_info = localtime(&raw_time);
	
	day_code = DAY_CODE[time_info->tm_wday];
	// cout << day_code << endl;
	int current_hour = time_info->tm_hour;
	int current_min = time_info->tm_min;
	
	if (rc) {
		fprintf(stderr, "Cannot open db file :%s\n", db_file_path);
		return 0;
	} else {
		printf("DB file open Success\n");
	}
	
	char *sql = "SELECT `id`, `name`, `hour`, `min`, `day`, `enabled` FROM weekly_alarm;";
	printf("SQL: %s\n", sql);
	rc = sqlite3_exec(db, sql, callback, (void*) &queried_data, &zErrMsg);
	printf("sqlite_exec function returned! (%d)\n", rc);
	if (SQLITE_OK != rc) {
		fprintf(stderr, "SQL Error: %s\n", zErrMsg);
		sqlite3_free(zErrMsg);
	} else {
		printf("SELECT Operation Done Successfully\n");
	}
	for (size_t i=0; i < queried_data.size(); i++) {
		auto& item = queried_data[i];
		for (size_t j=0; j < item.size(); j++) {
			cout << item[j] << '\n';
		}
	}
	sqlite3_close(db);
}