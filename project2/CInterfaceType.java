//public class CInterfaceType{
//    public String locality;
//
//}

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class CTranslator implements Observer {//    Based on pages 228 and 229 in "Programming Languages, Principles and Practice"
    
    public static Hashtable symbolTable;
    public static String currentScope;
    private CParser parser = null;
    
    @Override
    public void update(Observable observed, Object arg){
        
        parser = (CParser) observed;
        CToken t = parser.getCurrentToken();
        System.out.println("currentToken:"+t.token);
        
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
        
        System.out.format("Entering Translator: %s\n",args[0]);
        
        File file = new File(args[0]);//read in a .x source file

        
        try {
            FileInputStream f = new FileInputStream(file);
            symbolTable = new Hashtable();
            CParser parser = new CParser(); 
            //add extends Observable to CParser
            //add public static String currentToken to CParser
            //implement getToken and setToken in CParser 
            // to get currentToken
            //and to set currentToken
            CTranslator translator = new CTranslator(parser);
//            parser.addObserver(translator);
          
//            if(program(f)){
//                System.out.format("%s is a valid X program!\n",args[0]);
//            }
        
        }
        catch (FileNotFoundException e) {
          System.err.println("No file.");
        } 
    }   
}



