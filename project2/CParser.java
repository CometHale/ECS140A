import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class CParser {//    Based on pages 228 and 229 in "Programming Languages, Principles and Practice"
    
    public static boolean dataDeclaration(FileInputStream f){
        //DataDeclaration := ;
        CScanner.getNextToken(f);
        
        if(!CScanner.prevToken.token.equals(";")){
            System.err.format("Data Declaration rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
        
        return true;
    }
    
    public static boolean instanceType(FileInputStream f){
        //InstanceType := instance Identifier
        
        CScanner.getNextToken(f);
        
        if(!CScanner.prevToken.token.equals("instance")){
            System.err.format("Instance Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
                
        //instance
        if(CScanner.prevToken.token.equals("instance")){
            CScanner.peekNextToken(f);
        }
        
        //identifier
        if(!CScanner.peekedToken.type.equals("Identifier")){
            System.err.format("Instance Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
        else{
//           if(!identifier(f)){
//                return false;
//            } 
        }
       
        return true;
    }
    
    public static boolean floatType(FileInputStream f){
        //FloatType := float | double
        CScanner.getNextToken(f);
        
        if(!CScanner.prevToken.token.equals("float") || !CScanner.prevToken.token.equals("double")){
            System.err.format("Float Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
       
        return true;
    }
    
    public static boolean integerType(FileInputStream f){
        //IntegerType := [unsigned] ( char | short | int | long )
        CScanner.getNextToken(f);
        
        String[] types = {"char","short","int","long"};
        
        if(!CScanner.prevToken.token.equals("unsigned") || !Arrays.asList(types).contains(CScanner.prevToken.token)){
            System.err.format("Integer Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
        
//        String fullToken = ""; // combine the unsigned with (char | short | int | long)
        
        //unsigned (optional)
        if(CScanner.prevToken.token.equals("unsigned")){
//            fullToken += "unsigned ";
            CScanner.getNextToken(f);
        }
        
        // char | short | int | long
        if(!Arrays.asList(types).contains(CScanner.prevToken.token)){
            System.err.format("Integer Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
        
        return true;
    }
    
    public static boolean dataType(FileInputStream f){
       
        CScanner.getNextToken(f);
        
        String[] integerTypes = {"char","short","int","long"};
        String[] floatTypes = {"float","double"};
        
        if(!CScanner.prevToken.token.equals("unsigned") || !Arrays.asList(integerTypes).contains(CScanner.prevToken.token) 
                || !Arrays.asList(floatTypes).contains(CScanner.prevToken.token)
                ||CScanner.prevToken.token.equals("instance")    
           ){
            System.err.format("Data Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
        
        CScanner.peekNextToken(f);
        
        //IntegerType
        if(CScanner.peekedToken.token.equals("unsigned") || Arrays.asList(integerTypes).contains(CScanner.peekedToken.token)){
            
            if(!integerType(f)){
                return false;
            }
        }
       
        //FloatType
        if(Arrays.asList(floatTypes).contains(CScanner.peekedToken.token)){
            
            if(!floatType(f)){
                return false;
            }
                        
        }
        
        //InstanceType
        if(CScanner.prevToken.token.equals("instance")){
            
            if(!instanceType(f)){
                return false;
            }

        }
        
        return true;
    }
    
    public static boolean declarationType(FileInputStream f){
        //DeclarationType := DataType Identifier
           //check for incorrect expected stuff
        
        CScanner.peekNextToken(f);
        
        //DataType
        if(!dataType(f)){
            return false;
        }
        
        //Identifier
//        if(!identifier(f)){
//            return false;
//        }

        return true;
    }
    
//    public static boolean identifier(FileInputStream f){
//        // Identifier := ( _ | Alpha ) { ( _ | Digit | Alpha )  }
//        CScanner.getNextToken(f);
//        
//        // Check to make sure token is legal
//        
//        
//        return true;
//    }
    
    public static boolean memberDeclaration(FileInputStream f){
        //MemberDeclaration := DeclarationType ( DataDeclaration | FunctionDeclaration)
        
        CScanner.peekNextToken(f);
        // do some checks for errors ??
        
        // DeclarationType
        if(!declarationType(f)){
            return false;
        }
        
        CScanner.peekNextToken(f);
        
        //DataDeclaration
        if(CScanner.prevToken.equals(";")){
            
//            if(!dataDeclaration(f)){
//                return false;
//            }
        }
        else{//FunctionDeclaration
//            if(!functionDeclaration(f)){
//                return false;
//            }
        }
        
        return true;
    }
    
//    public static boolean storageDeclaration(FileInputStream f){
//
//        return true;
//    }
    
//    public static boolean mainDeclaration(FileInputStream f){
//       
//       return true;
//    }
  
    public static boolean interfaceDeclaration(FileInputStream f){
       
        CScanner.getNextToken(f);
        
        if(CScanner.prevToken.token.equals("interface")){
            CScanner.peekNextToken(f);
            
            if(!CScanner.peekedToken.type.equals("Identifier")){
                System.err.format("Interface Declaration rule broken @%4d .",CScanner.prevToken.lineNum);
            }
            
            
//            if(!identifier(f)){
//                return false;
//            }
            
            if(!CScanner.peekNextToken(f).token.equals("{")){
                System.err.format("Interface Declaration rule broken @%4d .",CScanner.peekedToken.lineNum);
                return false;
            }
           
            CScanner.peekNextToken(f);
            
            if(!memberDeclaration(f)){
                return false;
            }
            
            if(!CScanner.peekNextToken(f).token.equals("}")){
                System.err.format("Interface Declaration rule broken @%4d .",CScanner.prevToken.lineNum);
                return false;
            }
            
        }
        else{ //MemberDeclaration
            
            if(!memberDeclaration(f)){
                return false;
            }
        }
       
        return true;
    }
    
    public static boolean program(FileInputStream f){
//   Program := {InterfaceDeclaration} MainDeclaration {StorageDeclaration}{ImplementationDeclaration}   
        CScanner.peekNextToken(f); // peek at the first token
        
        if(!CScanner.peekedToken.type.equals("Keyword")){
            System.err.format("Program rule broken @%4d .",CScanner.peekedToken.lineNum);
            return false;
        }
        
        String[] dataTypes = {"unsigned", "char","short", "int", "long", "float", "double"};
        boolean programValid = false;
        
        
        //InterfaceDeclaration (optional)
        if(CScanner.peekedToken.equals("interface") || Arrays.asList(dataTypes).contains(CScanner.peekedToken.token)){
            System.out.println("program --> interfaceDeclaration");
            
            if(!interfaceDeclaration(f)){
                 System.exit(0);
            }//Interface Declaration failed
        }
             
        // MainDeclaration
        if(CScanner.prevToken.equals("void")){
            System.out.println("program --> mainDeclaration");
            
//            if(!mainDeclaration(f)){
//                System.exit(0);
//            }
            //StorageDeclaration (optional)
            if(CScanner.prevToken.equals("storage")){
                System.out.println("program --> mainDeclaration --> storage");
//                if(!storageDeclaration(f)){
//                    System.exit(0);
//                }
            }

            //Implementation Declaration (optional)
            if(CScanner.prevToken.equals("implementation") || Arrays.asList(dataTypes).contains(CScanner.prevToken.token)){
                System.out.println("program --> mainDeclaration--> implementationDeclaration");
                
//                if(!implementationDeclaration(f)){
//                    System.exit(0);
//                }
            }
           
        }
        
        if(CScanner.prevToken.type.equals("None")){
            return true;
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        
        
        
        if (args.length == 0) {
            System.exit(0);
        }
        
        System.out.format("Entering Parser: %s\n",args[0]);
        
        File file = new File(args[0]);
        boolean result = false;
        
        try {
            FileInputStream f = new FileInputStream(file);
           
            result = program(f);
            
            if(result){
                System.out.format("%s is a valid X program!\n",args[0]);
            }
        
        }
        catch (FileNotFoundException e) {
          System.err.println("No file.");
        }  
       
    }   
}



