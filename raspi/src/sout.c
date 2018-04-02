#include <stdio.h>
#include <string.h>
#include <errno.h>

#include <wiringSerial.h>

#define BUFSIZE 128
int main() {
	int fd;
	char buf[BUFSIZE];
	fd = serialOpen("/dev/ttyAMA0", 9600);
	if (fd < 0) {
		fprintf(stderr, "Unable to open serial device: %s\n", strerror(errno));
		return 1;
	}

	for(;;) {
		if (serialDataAvail(fd) != -1) {
			char ch = -1;
			int i=0;
			do {
				ch = serialGetchar(fd);
				printf("0x%x [%c]\n", ch, ch);
				if (ch == 0xff || i >= BUFSIZE-2) ch = '\0';
				buf[i++] = ch;
			} while(ch != '\0');
			if (strlen(buf) > 0) {
				printf("%s\n", buf);
				fflush(stdout);
				serialFlush(fd);
			}
		}
	}
	return 0;
}
