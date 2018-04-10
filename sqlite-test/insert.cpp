#include <cstdio>
#include <sqlite3.h>

using namespace std;

static int callback(void *NotUsed, int argc, char **argv, char **azColName) {
	for (int i=0; i < argc; i++) {
		printf("%s = %s\n", azColName[i], argv[i] ? argv[i] : "NULL");
	}
	putchar('\n');
	return 0;
}
const char* db_file_path = "./test.db";
int main() {
	sqlite3 *db;
	char *zErrMsg = 0;
	int rc = sqlite3_open(db_file_path, &db);

	if (rc) {
		fprintf(stderr, "Cannot open db file :%s\n", db_file_path);
		return 0;
	} else {
		printf("DB file Open Success\n");
	}

	char* sql;

	sql = "INSERT INTO temporal_alarm (`tag`, `hour`, `min`, `enabled`) " \
		"VALUES" \
		"('', 9, 30, 1)," \
		"('NAP', 16, 30, 1);";

	rc = sqlite3_exec(db, sql, callback, 0, &zErrMsg);

	if (rc != SQLITE_OK) {
		fprintf(stderr, "SQL Error: %s\n", zErrMsg);
		sqlite3_free(zErrMsg);
	} else {
		printf("Record created Successfully\n");
	}
	sqlite3_close(db);

	return 0;
}