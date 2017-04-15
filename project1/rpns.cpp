#include <cstdio>
#include <stdio.h>
#include <iostream>
#include <string>

#include <stack>
#include <algorithm>

using namespace std;

// Operand is not an integer!
// Invalid number of inputs!
// Invalid number of operands!
// Operand out of range!

string concatenate(string arg1, string arg2);

string concatenate(string arg1, string arg2){
	string concated = "";

	concated.append(arg1);
	concated.append(arg2);

	return concated;
}//concatenate (~)
//takes the two strings that are on the stack and combines them

// int left(stack<string> stacc){

// 	return 0;
// }//left(<-)
// // returns the first or last N characters of the string 

// int right(stack<string> stacc){

// 	return 0;
// }//right(->)
// // returns the first or last N characters of the string 


// int len_func(stack<string> stacc){

// 	return 0;
// }//len(#)
// // replaces the stiring on the top of the stack with the length of the string

// int find_func(stack<string> stacc){

// 	return 0;
// }//find(?)
// // finds the first occurrence of the substring in the string (zero indexed)
// // top of the stack is the string being searched, second string represents
// //the substring being searched for

// int add(stack<string> stacc){

// 	return 0;
// }//add(+)
// //takes the two top strings and interprets them as integers for the addition

// int subtract(stack<string> stacc){

// 	return 0;
// }//subtract(-)
// //takes the two top strings and interprets them as integers for the subtraction
// //second value on the stack will be subtracted from the top value of the stack

int string_manip(stack<string> main_stack){
	while(!main_stack.empty()){

		string cur_char = main_stack.top();
		main_stack.pop();

		if(cur_char.compare("~") == 0){

			//grab the arguments
			if(main_stack.size() < 2){
				cout << "Error: Invalid number of operands!";
				main_stack.
				break;
			}

			string arg1 = main_stack.top();
			main_stack.pop();
			string arg2 = main_stack.top();
			main_stack.pop();


			main_stack.push(concatenate(arg1,arg2));
		}

		// // else if(top.compare("<-") == 0){
		// // 	// left()
		// // }

		// // else if(top.compare("->") == 0){
		// // 	// right()
		// // }

		// // else if(top.compare("#") == 0){
		// // 	// len()
		// // }

		// // else if(top.compare("?") == 0){
		// // 	// find()
		// // }

		// // else if(top.compare("+") == 0){
		// // 	// add()
		// // }

		// // else if(top.compare("-") == 0){
		// // 	// subtract()
		// // }
	}

	cout << main_stack.top() << endl;
	// need to not return anything if an error happens
	return 0;
}

// int list_option(stack<string> main_stack){
// 	return 0;
// }

int main(int argc, char const *argv[])
{
	
	stack<string> main_stack;
	string inp = " ";


	// Get input and fill the stack
	while(getline(cin,inp)){
	
		if(inp.empty() != 1){

			transform(inp.begin(), inp.end(),inp.begin(), ::toupper);
			//utilized: 
			//http://stackoverflow.com/questions/735204/convert-a-string-in-c-to-upper-case
			main_stack.push(inp);

		}
		else{
			break;
		}
		
	}

	
	//check for -l option
	return 0;
}
