#include <stdio.h>
#include <iostream>
#include <string>
#include <stdexcept>
#include <exception>
#include <stack>
#include <algorithm>

using namespace std;

// Operand is not an integer!
// Invalid number of inputs!
// Invalid number of operands!
// Operand out of range!


/*
Test Cases

concatenate():
a
b
~

1
a
~



add():
floating point addition
negative numbers


*/

int string_manip();
int list_option(stack<string> stacc, int indent_num);



int string_manip(){
	stack<string> args;
	string inp,arg1,arg2,result = " ";

	// non-arg
	while(getline(cin,inp) && !inp.empty()){
		
		if(inp.compare("~") == 0){ //concatenate
			//takes the two strings that are on the stack and combines them

			//grab the arguments
			if(args.size() < 2){
				cout << "Error: Invalid number of operands!" << endl;
				exit(-1);
			}

			arg1 = args.top();
			args.pop();
			arg2 = args.top();
			args.pop();

			args.push(arg1.append(arg2));

			// args.push(concatenate(arg1,arg2));

		}

		else if(inp.compare("<-") == 0 || inp.compare("->") == 0 ){// left() and right()
			//returns the first N characters from the beginning or end of the string
			//on top of the stack

			string str = args.top();
			args.pop();

			try{
				int operand = stoi(args.top());
				args.pop();
				

				if(operand > static_cast<int>(str.size())){
					cout << "Error: Operand out of range!";
					exit(-1);
				}
				else{

					if(inp.compare("<-") == 0){
						args.push(str.substr(0,operand));
					}
					else{
						args.push(str.substr(str.size() - operand));
					}
					
				}

			} catch(invalid_argument & ia) {
				cout << "Error: Operand not an integer!" << endl;
			}
			
		}
 
		else if(inp.compare("#") == 0){// len()
			string tmp = args.top();
			args.pop();
			args.push(to_string(tmp.size()));
		}

		else if(inp.compare("?") == 0){// find()

			string str_to_search = args.top();
			args.pop();

			string query = args.top();
			args.pop();

			if(str_to_search.size() < query.size()){
				args.push(to_string(-1));
			}
			else{
				args.push(to_string(str_to_search.find(query)));
			}
			
		}

		else if(inp.compare("+") == 0 || inp.compare("-") == 0 ){ // add and subtract

			if(args.size() < 2){
				cout << "Error: Invalid number of operands!" << endl;
				exit(-1);
			}
			
			arg1 = args.top();
			args.pop();
			arg2 = args.top();
			args.pop();


			try{

				int val1 = stoi(arg1);
				int val2 = stoi(arg2);

				if(inp.compare("+") == 0){
					args.push(to_string(val1 + val2));
				}else{
					args.push(to_string(val1 - val2));
				}
				

			}catch(invalid_argument & ia){
				cout << "Error: Operand not an integer!" << endl;
			}

		}

		else{
			transform(inp.begin(), inp.end(),inp.begin(), ::toupper);
			//utilized: 
			//http://stackoverflow.com/questions/735204/convert-a-string-in-c-to-upper-case
			args.push(inp);
		}

	}

	cout << args.top() << endl;

	return 0;
}

int list_option(stack<string> stacc, int indent_num){


	if(stacc.empty()){
		cout << ")" << endl;
		return 0;
	}

	string operators = "~ <- -> # ? + -";

	string top_str = stacc.top();
	stack<string> args;
	int num_args = 2; // all but # have 2 args

	if(top_str.compare("#") == 0){
		num_args = 1;
	}

	if(operators.find(top_str) != operators.npos){ // top_str is an operator
		stacc.pop();
		for(int i = 0; i < indent_num; i++){
				cout << "    ";
		}
		cout << "(" << top_str << endl;
		

		
		for(int i = 0; i < indent_num; i++){
				cout << "    ";
		}
		cout << ")" << endl;

		// return list_option(stacc, indent_num + 1);


		// if(top_str.compare("#") == 0){ // # only has one input
		// 	for(int i = 0; i < indent_num; i++){
		// 		cout << "    ";
		// 	}
		// 	cout << stacc.top();
		// 	stacc.pop();
		// 	cout << endl;
		// 	for(int i = 0; i < indent_num - 1; i++){
		// 		cout << "    ";
		// 	}
		// 	cout << ")" << endl;

		// 	// return list_option(stacc, indent_num);
		// }
		// else{
			// while(!stacc.empty()){

			// 	for(int i = 0; i < indent_num; i++){
			// 		cout << "    ";
			// 	}
			// 	if (operators.find(stacc.top()) == operators.npos)
			// 	{
					
			// 		cout << stacc.top() << endl;
			// 		stacc.pop();
			// 		// args.pop(stacc.top());
			// 		// stacc.pop();
					
			// 	}
			// 	else{
			// 		return list_option(stacc, indent_num + 1);
					
			// 	}
				
			// }

			// for(int i = 0; i < indent_num - 1; i++){
			// 	cout << "    ";
			// }

			// cout << ")" << endl;
		// }
		
	}
	else{
		stacc.pop();
	}



	return list_option(stacc, indent_num + 1);
}

// int argc, char const *argv[]
int main(int argc, char const *argv[]){
	
	if(argc > 1){
		if(strcmp(argv[1],"-l") == 0){

			stack<string> stacc;
			string inp = " ";

			while(getline(cin,inp) && !inp.empty()){
				transform(inp.begin(), inp.end(),inp.begin(), ::toupper);
				//utilized: 
				//http://stackoverflow.com/questions/735204/convert-a-string-in-c-to-upper-case
				stacc.push(inp);
			}
			
			list_option(stacc, 0);
		}
	}
	else{
		string_manip();
	}
	return 0;
}
