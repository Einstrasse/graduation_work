all: test insert select check_for_alarm

test: test.cpp
	g++ -o test test.cpp -lsqlite3

insert: insert.cpp
	g++ -o insert insert.cpp -lsqlite3

select: select.cpp
	g++ -o select select.cpp -lsqlite3 -std=c++11

check_for_alarm: check_for_alarm.cpp
	g++ -o check_for_alarm check_for_alarm.cpp -lsqlite3 -std=c++11

clean:
	rm -f ./test ./insert ./select ./check_for_alarm