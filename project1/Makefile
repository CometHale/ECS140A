rpns: rpns.o
	# g++ -g rpns.o -o rpns
	clang++ -g rpns.o -o rpns
	
rpns.o: rpns.cpp
	# g++ rpns.cpp -c
	clang++ -std=c++11 -stdlib=libc++ -Weverything rpns.cpp -c

clean: 
	rm rpns rpns.o