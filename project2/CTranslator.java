import java.util.*;

public class CTranslator implements Observer {
    //Things to do
    //how to determine currentGroup while in global
    
    //Possible errors to check for
        // int G;  char G(){}
        //storage and implementation for anonymous interface
        //storage and implementation for function-defined interface
        // two storages for global
    
    public static Hashtable<String,ArrayList<CSymbol>> symbolTable;
    public static Stack<String> scopes;
    public static CToken lastToken;
    public static String lastIdentifier;
    public static boolean inFunctionCall;
    public static boolean inStorageDef;
//    public static boolean instanceCall;
    public static boolean inImplementation;
    public static ObservableParser parser;
    public static CSymbol incomplete;//incomplete declaration
    
    public CTranslator(){
        this.symbolTable = new Hashtable<String,ArrayList<CSymbol>>();
        this.scopes = new Stack<String>();
        this.lastToken = new CToken();
        this.scopes.push("global");
        this.incomplete = new CSymbol();
        this.parser = new ObservableParser();
        this.inFunctionCall = false;
        this.inImplementation = false;
        this.lastIdentifier = "";
        this.inStorageDef = false;
        parser.addObserver(this);
    }
    
    public void update(Observable observed, Object arg){
        
        parser = (ObservableParser) observed;
        CToken t = parser.getCurrentToken();
        String r = parser.getCurrentRule();
        boolean s = parser.getRuleStatus();
        
        CSymbol tmp = new CSymbol();
        String[] dataTypes = {"unsigned", "char","short", "int", "long", 
            "float", "double", "instance","void"};
        
//        String[] builtinFuncs = {"printi","printd","printc","scani","scanc","scand"};
//        String[] mathOps = {"+","-","*","/","==",">=","<=","<",">","!="};
//        String capGroup;
        
        if(scopes.empty()){
            scopes.push("global");
        }
        String currentScope = scopes.pop();
        scopes.push(currentScope);
        
        if(r.equals("interfaceDeclaration") && lastToken != null && t != null){
            //interface

            if(lastToken.token.equals("interface") && !symbolTable.containsKey(t.token)){
               symbolTable.put(t.token,new ArrayList<CSymbol>());
               
               tmp.type = lastToken.token; //tmp is an interface
               tmp.scope = currentScope;
               symbolTable.get(t.token).add(tmp);
               scopes.push(t.token);
            }
            else{
                
                if(t.type.equals("Identifier")){
                    System.err.format("Interface %s already defined.\n",t.token);
                    System.exit(0);
                }
               
            }
        }
        
        if(r.equals("memberDeclaration") && lastToken != null && t != null){
            
            if(lastToken.token.equals("instance")){
                incomplete.type = t.token;
            }
            else{
                
                if(Arrays.asList(dataTypes).contains(lastToken.token)){
                    incomplete.type = lastToken.token;
                }
                
                if(!Arrays.asList(dataTypes).contains(incomplete.type) && !symbolTable.containsKey(t.token) && t.type.equals("Identifier")){
                    symbolTable.put(t.token,new ArrayList<CSymbol>());
                    incomplete.scope = currentScope;
                    incomplete.lineNum = t.lineNum;
                    
                    symbolTable.get(t.token).add(incomplete);
                    incomplete = new CSymbol();
                    
                }//incomplete.type is an identifier
                else if(Arrays.asList(dataTypes).contains(incomplete.type) && !symbolTable.containsKey(t.token) && t.type.equals("Identifier")){
                    symbolTable.put(t.token,new ArrayList<CSymbol>());
                    incomplete.scope = currentScope;
                    incomplete.lineNum = t.lineNum;
                    symbolTable.get(t.token).add(incomplete);
                    incomplete = new CSymbol();
                }//incomplete.type is a data type
                else if(symbolTable.containsKey(t.token) && t.type.equals("Identifier")){
                    System.err.format("Data member %s on line %d already defined .\n",t.token, t.lineNum);
                    System.exit(0);
                } 
                
                if(t.type.equals("Operator") && lastToken.type.equals("Identifier")){
                    
                    if(lastToken != null && symbolTable.get(lastToken.token) != null && t.token.equals("(")){
                        // last token was a named function
                        //and we're entering it's scope
                        //scope is exited at the !s check at the end of this update() function
                        symbolTable.get(lastToken.token).get(symbolTable.get(lastToken.token).size() - 1).isFunction = true;
                        scopes.push(lastToken.token);
                        inFunctionCall = true;
                    }
                    
                }

                
            }
            
        }
        
        if(r.equals("parameterBlock") && lastToken != null && t != null){
            if(t.token.equals(")")){
                inFunctionCall = false;
            }
        }
        
        if(r.equals("parameter") && lastToken != null && t != null){
            
            if(Arrays.asList(dataTypes).contains(lastToken.token) && t.type.equals("Identifier")){
                
                if(!symbolTable.containsKey(t.token)){
                    symbolTable.put(t.token,new ArrayList<CSymbol>());
                    tmp.type = lastToken.token;
                    tmp.scope = currentScope;
                    symbolTable.get(t.token).add(tmp);
                }
                else{
                   
                    for(int i = 0; i < symbolTable.get(t.token).size(); i++ ){
                       
                       if(!symbolTable.get(t.token).get(i).scope.equals(currentScope)){
                            tmp.type = lastToken.token;
                            tmp.scope = currentScope;
                            symbolTable.get(t.token).add(tmp);
                       }
                       else{
                           System.err.format("Data member %s on line %d already defined .\n",t.token, t.lineNum);
                           System.exit(0);
                       }
                    }
                    
                }
                
            } 
        }
        
        if(r.equals("implementationDeclaration") && lastToken != null && t != null){
            //implementation
            
            if(s){
                inImplementation = true;
            }
            else{
                inImplementation = false;
            }
            
            if(lastToken.token.equals("implementation") && t.type.equals("Identifier")){
                incomplete.type = lastToken.token;
                
                if(symbolTable.containsKey(t.token)){
                    for(int i = 0; i < symbolTable.get(t.token).size(); i++ ){
                      
                       if(symbolTable.get(t.token).get(i).type.equals("implementation")){
                           //there can't be more than one implementation per interface
                            System.err.format("Implementation member %s on line %d already defined for interface.\n",t.token, t.lineNum);
                            System.exit(0);
                       }

                    }
                    
                    incomplete.scope = currentScope;
                    symbolTable.get(t.token).add(incomplete);

                }
                else{//there needs to be an interface already
                    System.err.format("Implementation member %s on line %d needs a defined interface .\n",t.token, t.lineNum);
                    System.exit(0);
                }
            }
            
        }
        
        if(r.equals("declarationType") && lastToken != null && t != null ){
                    
            if(inImplementation){                    
                if(Arrays.asList(dataTypes).contains(lastToken.token) && t.type.equals("Identifier")){

                     if(symbolTable.containsKey(t.token)){

                         for(int i = 0; i < symbolTable.get(t.token).size(); i++ ){

                            if(symbolTable.get(t.token).get(i).scope.equals(currentScope)){
                                 System.err.format("Data member %s on line %d already defined for implementation.\n",t.token, t.lineNum);
                                 System.exit(0);
                            }
                         }

                     }
 
                     symbolTable.put(t.token,new ArrayList<CSymbol>());
                     tmp = new CSymbol();
                     
                     tmp.lineNum = t.lineNum;
                     tmp.type = lastToken.token;
                     tmp.scope = currentScope;
                     symbolTable.get(t.token).add(tmp);

                }  
            }
            else{
                if(Arrays.asList(dataTypes).contains(t.token)){
                    incomplete.type = t.token;
                }
            }
            
        }
        
        if(r.equals("functionDefinition") && lastToken != null && t != null){
            if(t.token.equals("(")){
                
                if(lastToken.type.equals("Identifier")){
                    symbolTable.get(lastToken.token).get(symbolTable.get(lastToken.token).size() - 1).isFunction = true;
                }
            }
        }
        
        if(r.equals("storageDeclaration") && lastToken != null && t != null){
            //storage
            if(lastToken.token.equals("storage") && (t.type.equals("Identifier") || t.token.equals("global"))){
                
//
                
                incomplete.type = lastToken.token;
                
                if(t.token.equals("global")){
                    //add the variables within as global-scope members
                    if(symbolTable.containsKey(t.token)){
                        
                        for(int i = 0; i < symbolTable.get(t.token).size(); i++ ){

                           if(symbolTable.get(t.token).get(i).type.equals("storage")){
                               //there can't be more than one storage per interface
                                System.err.format("Storage member %s on line %d already defined for interface.\n",t.token, t.lineNum);
                                System.exit(0);
                           }

                        }
                        
                        incomplete.scope = currentScope;
                        symbolTable.get(t.token).add(incomplete);
                    }
                    else{
                        symbolTable.put(t.token,new ArrayList<CSymbol>());
                        
                        incomplete.scope = currentScope;
                        symbolTable.get(t.token).add(incomplete);
                    
                    }
                    
                }
                else{
                    if(symbolTable.containsKey(t.token)){
                        for(int i = 0; i < symbolTable.get(t.token).size(); i++ ){

                           if(symbolTable.get(t.token).get(i).type.equals("storage")){
                               //there can't be more than one storage per interface
                                System.err.format("Storage member %s on line %d already defined for interface.\n",t.token, t.lineNum);
                                System.exit(0);
                           }

                        }

                        incomplete.scope = currentScope;
                        symbolTable.get(t.token).add(incomplete);

                    }
                    else{//there needs to be an interface already
                        System.err.format("Implementation member %s on line %d needs a defined interface .\n",t.token, t.lineNum);
                        System.exit(0);
                    }
                }
//   
            }
            
        }
        
        if(r.equals("variableDeclaration") && t != null && lastToken != null){
            
            if(!lastIdentifier.equals("")){
                if(symbolTable.containsKey(lastIdentifier)){
                    for(int i = 0; i < symbolTable.get(lastIdentifier).size(); i++){
                    
                        if(symbolTable.get(lastIdentifier).get(i).scope.equals(currentScope)){
                            if(symbolTable.get(lastIdentifier).get(i).type.equals(incomplete.type)){
                                symbolTable.get(lastIdentifier).get(i).value = lastToken.token;
                            }
                            else{
                                System.err.format(" %s on line %d has different declared type.\n",lastIdentifier, t.lineNum);
                                System.exit(0);
                            }
                        }
                    }
                }
                else{
                    symbolTable.put(lastIdentifier,new ArrayList<CSymbol>());
                    incomplete.value = lastToken.token;
                    incomplete.scope = currentScope;
                    incomplete.lineNum = lastToken.lineNum;
                    symbolTable.get(t.token).add(incomplete);
                        
                }
            }
        }
        
        if( t != null && t.token.equals("=") && lastToken != null){
            if(lastToken.type.equals("Identifier")){
                lastIdentifier = lastToken.token;
            }
        }

        if(!s && !inFunctionCall) {
            
            if(!scopes.empty()){
                scopes.pop();
            }
            else{
                scopes.push("global"); //global should always be on the stack
            } 
        }
        
        if(t != null){
            
            lastToken = t;
        }
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
    }   
}