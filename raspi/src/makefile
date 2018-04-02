all: I O turn

I: sin.c
	gcc -o I sin.c -lwiringPi -Wall
O: sout.c
	gcc -o O sout.c -lwiringPi -Wall

turn: turn.c
	gcc -o turn turn.c -lwiringPi -Wall
	
install:
	cp turn /usr/bin/turn
	
clean:
	rm turn I O