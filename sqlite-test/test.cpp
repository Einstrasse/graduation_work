#include <cstdio>
#include <sqlite3.h>

using namespace std;

const char* db_file_path = "./test.db";
int main() {
	sqlite3 *db;
	char *zErrMsg = 0;
	int rc = sqlite3_open(db_file_path, &db);

	if (rc) {
		fprintf(stderr, "Cannot open db file :%s\n", db_file_path);
		return 0;
	} else {
		printf("DB file Success\n");
	}
	sqlite3_close(db);
}