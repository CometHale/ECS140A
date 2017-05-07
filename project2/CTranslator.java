import java.util.*;

public class CTranslator implements Observer {
    //Things to do
    //how to determine currentGroup while in global
    
    //Possible errors to check for
        // int G;  char G(){}
        //storage and implementation for anonymous interface
        //storage and implementation for function-defined interface
    
    public static Hashtable<String,Stack<CSymbol>> symbolTable;
    public static Stack<String> scopes;
    public static String lastIdentifier;
    public static boolean inFunctionCall;
    public static String currentGroup; //Implementation, Interface, Storage
    public static ObservableParser parser;
    public static CSymbol incomplete;//incomplete declaration
    
    public CTranslator(){
        this.symbolTable = new Hashtable<String,Stack<CSymbol>>();
        this.scopes = new Stack<String>();
        this.scopes.push("global");
        this.lastIdentifier = "";
        this.incomplete = new CSymbol();
        this.parser = new ObservableParser();
        parser.addObserver(this);
    }
    
    public void update(Observable observed, Object arg){
        
        parser = (ObservableParser) observed;
        CToken t = parser.getCurrentToken();
        String r = parser.getCurrentRule();
        boolean s = parser.getRuleStatus();
        CSymbol tmp;
        String[] dataTypes = {"unsigned", "char","short", "int", "long", 
            "float", "double", "instance","void"};
        String[] builtinFuncs = {"printi","printd","printc","scani","scanc","scand"};
        String[] mathOps = {"+","-","*","/","==",">=","<=","<",">","!="};
        String capGroup;
               
        if(scopes.empty()){
            scopes.push("global");
        }
        String currentScope = scopes.pop();
        scopes.push(currentScope);
        
//        System.out.println("currentScope:" + currentScope);
//        if(r != null){
//            System.out.println("currentRule:"+ r +" Status:"+s);
//        }
                
        if(r.equals("storageDeclaration") || r.equals("dataDeclaration")
                || r.equals("interfaceDeclaration") || r.equals("implementationDeclaration") ){

            currentGroup = r.substring(0,r.indexOf("Declaration"));
        }// there may be other times when currentGroup needs to change
                        
        if(!lastIdentifier.equals("") && (r.equals("functionDeclaration") || r.equals("functionDefinition"))){
            //the last identifier added to the symbol table was a function declaration
            tmp = symbolTable.get(lastIdentifier).pop();
            tmp.memberType = "function";
            tmp.groupType = currentGroup;
            symbolTable.get(lastIdentifier).push(tmp);
            scopes.push(lastIdentifier);// we're about to enter a new scope
            lastIdentifier = "";
        }

        if((r.equals("dataDeclaration") ||r.equals("variableDeclaration")|| r.equals("parameterBlock") 
                || r.equals("parameter") || r.equals("block")) && !lastIdentifier.equals("")){
            //the last identifier added to the symbol table was a data declaration
            tmp = symbolTable.get(lastIdentifier).pop();
            tmp.groupType = currentGroup;
            symbolTable.get(lastIdentifier).push(tmp);
            lastIdentifier = "";
        }
            
        
//        if(r.equals("block") && !s){
//            if(!scopes.empty()){
//                scopes.pop(); //exiting a scope
//            }
//            else{
//                scopes.push("global"); //global should always be on the stack
//            }                   
//        }
        
        if(t != null){
            
//            System.out.println("currentToken:"+t.token);
            
            if(t.type.equals("Identifier") && !Arrays.asList(builtinFuncs).contains(t.token)){
                
                lastIdentifier = t.token;
                
                if(!symbolTable.containsKey(t.token)){//identifier not in symbol table
                    symbolTable.put(t.token,new Stack<CSymbol>());
                    tmp = new CSymbol();
                    tmp.lineNum = t.lineNum;
                    tmp.scope = currentScope;
                    symbolTable.get(t.token).push(tmp);
                }
                else{//identifier in symbol table
                    
                    tmp = symbolTable.get(t.token).pop();
                    symbolTable.get(t.token).push(tmp);//make sure to replace it
                    //may not be out of the current scope yet
                    
                    if(tmp.scope.equals(currentScope) && !inFunctionCall && !r.equals("parameterBlock")){
                        //this identifier is already being used in this scope
                        //and this isn't being used in a function call or parameter block
                        
                        if(tmp.memberType.equals("function")){
                            System.err.format("%s member already declared a %s!\n", 
                                tmp.protectionLevel, tmp.memberType
                            );
                        }
                        
                        if(tmp.groupType.length() > 1){
                            capGroup = tmp.groupType.substring(0,1).toUpperCase() + tmp.groupType.substring(1);
                        }
                        else{
                            capGroup = tmp.groupType;
                        }
                        
                        System.err.format("Semantic Error: %s member %s %s on "
                                + "line %d has already been declared in %s!.\n", 
                                capGroup, tmp.memberType, t.token, 
                                t.lineNum,tmp.groupType
                        );
                        //Data member G on line 1 has already been declared in interface!
                        System.exit(0);
                    }
                    else{//add another CSymbol to the stack for the identifier
                        tmp = new CSymbol();
                        tmp.lineNum = t.lineNum;
                        tmp.scope = currentScope;

                        symbolTable.get(t.token).push(tmp);
                    }
                   //addition, subtraction, etc. for checking return types
                }

                if(!incomplete.complete){
                    incomplete.lineNum = t.lineNum;
                    incomplete.scope = currentScope;
                    symbolTable.get(t.token).push(incomplete);
                    incomplete = new CSymbol();
                }//there's an incomplete symbol definition

            }

            if(t.type.equals("Keyword")){
                
                if(Arrays.asList(dataTypes).contains(t.token)){
                    incomplete.type = t.token;
                    incomplete.scope = currentScope;
                }
                
//                if(t.token.equals("self")){
//                    
//                }//scope stuff
                
                if(t.token.equals("global")){
                    scopes.push("global");
                }
                
                if(t.token.equals("main")){
                    //no need to check in symbol table
                    //double main error is caught by syntax
                    if(!symbolTable.contains(t.token)){//identifier not in symbol table
                        symbolTable.put(t.token,new Stack<CSymbol>());
                        tmp = new CSymbol();
                        tmp.lineNum = t.lineNum;
                        tmp.memberType = "function";
                        tmp.scope = currentScope;
                        symbolTable.get(t.token).push(tmp);
                    }
                   
                    scopes.push(t.token);
                }//main declaration is coming
                
                if(t.token.equals("instance")){
                    
                }
                
                if(t.token.equals("implementation")){
                    
                }
                
                if(t.token.equals("storage")){
                    
                }
                
                if(t.token.equals("interface")){
                    
                }
                
//                if(Arrays.asList(builtinFuncs).contains(t.token)){
//                    
//                }//builtin functions, prob need for part 4
                
//                if(t.token.equals("let")){
//                }
                

                
//                if(t.token.equals("while")){
//                    
//                }
//                
//                if(t.token.equals("if")){
//                    
//                }
//                
//                if(t.token.equals("new")){
//                    
//                }
//                
//                if(t.token.equals("return")){
//                    
//                }
            }

            if(t.type.equals("Operator")){
                
                if(t.token.equals("}")){
                    //need for keeping track of scope
                    
                    if(!scopes.empty()){
                        scopes.pop(); //exiting a scope
                    }
                    else{
                        scopes.push("global"); //global should always be on the stack
                    }
                    
                }
                
                if(t.token.equals("(")){
                    inFunctionCall = true;
                }
                
                if(t.token.equals(")")){
                    inFunctionCall = false;
                }
//                if(t.token.equals("=")){
//                    
//                }
//                
//                if(t.token.equals(";")){
//                    
//                    
//                }
//                
//                if(Arrays.asList(mathOps).contains(t.token)){
//                    
//                }
                                
            }

//            if(t.type.contains("Constant")){
//
////                if(incomplete.value.equals("")){
////                    
////                }
//                incomplete.value = t.token;
//            }
        
            parser.setCurrentToken(null);
        }
        
        

       
//        if(!s){
//            System.out.println("exiting rule "+ r);
//            parser.currentRule = "";
//        }//the rule was just exited
        
      
        
    }
    
    public static void main(String[] args) {
        
//verifies that all identifiers are only declared once per block,and that interface 
//storage, and implementations are only declared once. 
//This class will also verify that an interface is declared before the 
//corresponding storage or implementation declaration. 
//This class will also verify that data members and member functions should 
//only be declared once per interface, 
//storage or implementation declaration.

        if (args.length == 0) {
            System.exit(0);
        }
        
        CParser p = new CParser(); 

        String[] newArgs = {args[0], "Translator"};
        p.main(newArgs);
        System.out.println("Success");
    }   
}