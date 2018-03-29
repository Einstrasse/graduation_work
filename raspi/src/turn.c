#include <stdio.h>
#include <string.h>
#include <errno.h>

#include <wiringSerial.h>

void hexdump(char* str) {
	for (int i=0; i < strlen(str); i++) {
		printf("0x%x [%c] ", str[i], str[i]);
	}
	putchar('\n');
	return;
}
char buf[128];
int main(int argc, char** argv) {

	int fd;

	if (argc != 2) {
		fprintf(stderr, "Invalid number of arguments\n");
		return -1;
	}
	sprintf(buf, "%s", argv[1]);
	fd = serialOpen("/dev/ttyAMA0", 9600);
	if (fd < 0) {
		fprintf(stderr, "Unable to open serial device: %s\n", strerror(errno));
		return -2;
	}
	serialPuts(fd, buf);
	serialClose(fd);
	return 0;
	
}
