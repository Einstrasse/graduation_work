#include <stdio.h>
#include <string.h>
#include <errno.h>

#include <wiringSerial.h>

int main() {
	int fd;
	char buf[128];
	fd = serialOpen("/dev/ttyAMA0", 9600);
	if (fd < 0) {
		fprintf(stderr, "Unable to open serial device: %s\n", strerror(errno));
		return 1;
	}

	for(;;) {
		scanf("%s", buf);
		for (int i=0; i < strlen(buf); i++) {
			printf("0x%x ", buf[i]); 
		}
		putchar('\n');
		serialPuts(fd, buf);
		printf("===================================\n");
	}
	return 0;
}
