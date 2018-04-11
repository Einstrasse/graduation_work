all: test insert select

test: test.cpp
	g++ -o test test.cpp -lsqlite3

insert: insert.cpp
	g++ -o insert insert.cpp -lsqlite3

select: select.cpp
	g++ -o select select.cpp -lsqlite3 -std=c++11