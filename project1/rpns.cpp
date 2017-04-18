#include <stdio.h>
#include <iostream>
#include <string>
#include <stdexcept>
#include <exception>
#include <stack>
#include <algorithm>

using namespace std;

void string_manip(stack<string> & stacc, stack<string> & extra);
void list_option(stack<string> & stacc, int indent_num);
void error_num_inputs(stack<string> & stacc, stack<string> & extra);
void error_non_exception_operand_not_int(string arg1, string arg2);

void error_num_inputs(stack<string> & stacc, stack<string> & extra){

	cout << "Error: Invalid number of inputs!" << endl;

	while(!extra.empty()){
		stacc.push(extra.top());
		extra.pop();
	}

	cout << "->"<< stacc.top() << endl;
	stacc.pop();

	while(!stacc.empty()){
		cout << stacc.top() << endl;
		stacc.pop();
	}
}

void error_non_exception_operand_not_int(string arg1, string arg2){

	string operators = "~ <- -> # ? + -";

	if(arg1.find(".") != arg1.npos || arg2.find(".") != operators.npos ){
		cout << "Error: Operand not an integer!" << endl;
		exit(-1);
	}// arg may be a float, so not an integer

}

void string_manip(stack<string> & stacc, stack<string> & extra){

	if(stacc.size() == 1){
	 	return;
	}

	string result,str, arg1,arg2 = "";
	string operators = "~ <- -> # ? + -";
	string top_str = stacc.top();
	stacc.pop();

	int num_args = 2; // all but # take 2 arguments
	int operand;

	if(top_str.compare("#") == 0){
		num_args = 1;
	}

	if(operators.find(top_str) != operators.npos){ // top_str is an operator

		if(top_str.compare("~") == 0){ //concatenate
			//takes the two strings that are on the stack and combines them

			//grab the arguments
			if(stacc.size() < 2){
				cout << "Error: Invalid number of operands!" << endl;
				exit(-1);
			}

			for(int k = 0; k < num_args; k++){

				if(operators.find(stacc.top()) != operators.npos){

					string_manip(stacc,extra);

				}

				result.append(stacc.top());
				stacc.pop();
			}

			stacc.push(result);
		}

		else if(top_str.compare("<-") == 0 || top_str.compare("->") == 0 ){// left() and right()
			//returns the first N characters from the beginning or end of the string
			//on top of the stack

			if(stacc.size() < 2){
				cout << "Error: Invalid number of operands!" << endl;
				exit(-1);
			}

			if(operators.find(stacc.top()) != operators.npos){
				string_manip(stacc, extra);
			}

			str = stacc.top();
			stacc.pop();

			try{

				if(operators.find(stacc.top()) != operators.npos){
					string_manip(stacc, extra);
				}

				error_non_exception_operand_not_int(stacc.top(), " ");

				operand = stoi(stacc.top());
				stacc.pop();

				
				if(operand > static_cast<int>(str.size())){
					cout << "Error: Operand out of range!";
					exit(-1);
				}
				else{

					if(top_str.compare("<-") == 0){
						stacc.push(str.substr(0,operand));
					}
					else{
						stacc.push(str.substr(str.size() - operand));
					}
					
				}

			} catch(invalid_argument & ia) {
				cout << "Error: Operand not an integer!" << endl;
			}	
		}
 
		else if(top_str.compare("#") == 0){// len()

			if(stacc.size() < 1){
				cout << "Error: Invalid number of operands!" << endl;
				exit(-1);
			}

			if(operators.find(stacc.top()) != operators.npos){
				string_manip(stacc, extra);
			}

			result = stacc.top();
			stacc.pop();
			stacc.push(to_string(result.size()));
		}

		else if(top_str.compare("?") == 0){// find()

			if(stacc.size() < 2){
				cout << "Error: Invalid number of operands!" << endl;
				exit(-1);
			}

			if(operators.find(stacc.top()) != operators.npos){
				string_manip(stacc, extra);
			}

			string str_to_search = stacc.top();
			stacc.pop();

			if(operators.find(stacc.top()) != operators.npos){
				string_manip(stacc, extra);
			}

			string query = stacc.top();
			stacc.pop();

			if(str_to_search.size() < query.size()){
				stacc.push(to_string(-1));
			}
			else{
				stacc.push(to_string(str_to_search.find(query)));
			}
			
		}

		else if(top_str.compare("+") == 0 || top_str.compare("-") == 0 ){ // add and subtract

			if(stacc.size() < 2){
				cout << "Error: Invalid number of operands!" << endl;
				exit(-1);
			}

			if(operators.find(stacc.top()) != operators.npos){
				string_manip(stacc, extra);
			}

			arg1 = stacc.top();
			stacc.pop();

			if(operators.find(stacc.top()) != operators.npos){
				string_manip(stacc, extra);
			}

			arg2 = stacc.top();
			stacc.pop();


			error_non_exception_operand_not_int(arg1, arg2);

			try{

				if(top_str.compare("+") == 0){
					stacc.push(to_string(stoi(arg1) + stoi(arg2)));
					
				}else{
					stacc.push(to_string(stoi(arg1) - stoi(arg2)));
				}

			}catch(invalid_argument & ia){
				cout << "Error: Operand not an integer!" << endl;
			}

		}
		
		return;
	}
	else{

		extra.push(top_str);
		string_manip(stacc, extra);
		
	}//a non operator is at the top of the stack
}

void list_option(stack<string> &stacc, int indent_num){


	if(stacc.empty()){

	 	return;
	}

	string operators = "~ <- -> # ? + -";

	string top_str = stacc.top();
	stacc.pop();


	int num_args = 2;

	if(top_str.compare("#") == 0){
		num_args = 1;
	}


	if(operators.find(top_str) != operators.npos){

		for(int i = 0; i < indent_num; i++){
			cout << "    ";
		}
		cout << "(" << top_str << endl;

		for(int k = 0; k < num_args; k++){

			if(operators.find(stacc.top()) != operators.npos){
				list_option(stacc, indent_num + 1);

			}
			else{

				for(int i = 0; i < indent_num + 1; i++){
					cout << "    ";
				}
				cout << stacc.top() << endl;
				stacc.pop();

			}
			
		}

		for(int i = 0; i < indent_num; i++){
			cout << "    ";
		}
		cout << ")" << endl;
		
		return;
	}
}

int main(int argc, char const *argv[]){

	stack<string> main, main_copy, residual;
	string inp = " ";
	string operators = "~ <- -> # ? + -";
	int num_args = 0, num_ops = 0, num_lens = 0;

	while(getline(cin,inp) && !inp.empty()){

		transform(inp.begin(), inp.end(),inp.begin(), ::toupper);
		//utilized: 
		//http://stackoverflow.com/questions/735204/convert-a-string-in-c-to-upper-case
		main.push(inp);
		main_copy.push(inp);

		if(operators.find(inp) != operators.npos){

			if(inp.compare("#") == 0){
				num_lens++;
			}else{
				num_ops++;
			}
			
		}
		else{
			num_args++;
		}



	}

	string_manip(main_copy, residual);

	if(main_copy.size() + residual.size() == 1){

		if(argc > 1){

			if(strcmp(argv[1],"-l") == 0){

				list_option(main, 0);
			}
			else {
				cout << main_copy.top() << endl;
			}

		}
		else {
			cout << main_copy.top() << endl;
		}

		
	}
	else{
		error_num_inputs(main_copy, residual);
	}
	
	return 0;
}
