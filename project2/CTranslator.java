import java.util.*;

public class CTranslator implements Observer {//    Based on pages 228 and 229 in "Programming Languages, Principles and Practice"
    
    public static Hashtable symbolTable;
    public static String currentScope;
    public static ObservableParser parser;
    
    public CTranslator(){
        this.symbolTable = new Hashtable();
        this.parser = new ObservableParser();
        parser.addObserver(this);
    }
    
    public void update(Observable observed, Object arg){
        
        parser = (ObservableParser) observed;
        CToken t = parser.getCurrentToken();
        String r = parser.getCurrentRule();
        boolean s = parser.getRuleStatus();
        
        if(t != null){
             System.out.println("currentToken:"+t.token);
        }
        if(r != null){
            System.out.println("currentRule:"+ r +" Status:"+s);
        }
       
        
//        
//        if(!s){
//            System.out.println("exiting rule "+ r);
//            parser.currentRule = "";
//        }//the rule was just exited
        
//        if(t.type.equals("Identifier")){
//            if(symbolTable.contains(t.token)){
//                
//            }
//            else{
////                symbolTable.put(t.token,)
//            }
//        }
        //each time an identifier appears, check if it's in symbolTable
        //if not add it and it's attribute to symbolTable
        //else check if it's already in the current scope or not
        //if not then collect it's attributes and put it on the stack 
        //for the identifier
        
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
        symbolTable = new Hashtable();
        args[1] = "Translator";
        p.main(args);
            
    }   
}