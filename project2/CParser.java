import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class CParser {//    Based on pages 228 and 229 in "Programming Languages, Principles and Practice"

    public static Tree<CToken> parseTree;
    public static Stack<Tree.Node<CToken>> parentNodes; //nodes that are still having children added to them
    
    public static boolean functionDeclaration(FileInputStream f){
        //FunctionDeclaration := ParameterBlock ;
        
        return true;
    }
    
    public static boolean dataDeclaration(FileInputStream f){
        //DataDeclaration := ;
        
        if(!CScanner.prevToken.token.equals(";")){
            System.err.format("Data Declaration rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
        
        Tree.Node<CToken> newNode = new Tree.Node<CToken>(CScanner.prevToken,parentNodes.pop());
        parseTree.insert(newNode);
        parentNodes.push(newNode.getNodeParent()); //parent may have more children
        
        CScanner.getNextToken(f);
        return true;
    }
    
    public static boolean instanceType(FileInputStream f){
        //InstanceType := instance Identifier
        
        if(!CScanner.prevToken.token.equals("instance")){
            System.err.format("Instance Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
                
        Tree.Node<CToken> newNode = new Tree.Node<CToken>(CScanner.prevToken,parentNodes.pop());
        parseTree.insert(newNode);
        parentNodes.push(newNode.getNodeParent()); //parent may have more children
        parentNodes.push(newNode);
                
        //instance
        if(!CScanner.prevToken.token.equals("instance")){
            CScanner.getNextToken(f);
        }
        
        //identifier
//        boolean result = identifier(f);
        
        CScanner.getNextToken(f);
        return true;
    }
    
    public static boolean floatType(FileInputStream f){
        //FloatType := float | double
        
        if(!CScanner.prevToken.token.equals("float") || !CScanner.prevToken.token.equals("double")){
            System.err.format("Float Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
                
        Tree.Node<CToken> newNode = new Tree.Node<CToken>(CScanner.prevToken,parentNodes.pop());
        parentNodes.push(newNode.getNodeParent()); //parent may have more children
       
        parseTree.insert(newNode);
        CScanner.getNextToken(f);
        return true;
    }
    
    public static boolean integerType(FileInputStream f){
        //IntegerType := [unsigned] ( char | short | int | long )
        String[] types = {"char","short","int","long"};
        
        if(!CScanner.prevToken.token.equals("unsigned") || !Arrays.asList(types).contains(CScanner.prevToken.token)){
            System.err.format("Integer Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
        
        Tree.Node<CToken> newNode = new Tree.Node<CToken>(CScanner.prevToken,parentNodes.pop());
        parentNodes.push(newNode.getNodeParent()); //parent may have more children
        String fullToken = ""; // combine the unsigned with (char | short | int | long)
        CToken newToken;
        
        //unsigned (optional)
        if(CScanner.prevToken.token.equals("unsigned")){
            fullToken += "unsigned ";
            CScanner.getNextToken(f);
        }
        
        // char | short | int | long
        if(!Arrays.asList(types).contains(CScanner.prevToken.token)){
            System.err.format("Integer Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
        else{
            fullToken += CScanner.prevToken.token;
            newToken = new CToken(fullToken,"Identifier", CScanner.prevToken.lineNum,CScanner.prevToken.colNum);
            newNode.setNodeData(newToken);
        }
        
        parseTree.insert(newNode);
        CScanner.getNextToken(f);
        return true;
    }
    
    public static boolean dataType(FileInputStream f){
        
        String[] integerTypes = {"char","short","int","long"};
        String[] floatTypes = {"float","double"};
        
        if(!CScanner.prevToken.token.equals("unsigned") || !Arrays.asList(integerTypes).contains(CScanner.prevToken.token) 
                || !Arrays.asList(floatTypes).contains(CScanner.prevToken.token)
                ||CScanner.prevToken.token.equals("instance")    
           ){
            System.err.format("Data Type rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
        
        Tree.Node<CToken> newNode = new Tree.Node<CToken>(CScanner.prevToken,parentNodes.pop());
        parseTree.insert(newNode);
        parentNodes.push(newNode.getNodeParent()); //parent may have more children
        parentNodes.push(newNode);
        boolean result;

        
        //IntegerType
        if(CScanner.prevToken.token.equals("unsigned") || Arrays.asList(integerTypes).contains(CScanner.prevToken.token)){
            result = integerType(f);
        }
       
        //FloatType
        if(Arrays.asList(floatTypes).contains(CScanner.prevToken.token)){
            result = floatType(f);
        }
        
        //InstanceType
        if(CScanner.prevToken.token.equals("instance")){
            result = instanceType(f);
        }
        
        parentNodes.pop();
        CScanner.getNextToken(f);
        return true;
    }
    
//    public static boolean declarationType(FileInputStream f){
//        //DeclarationType := DataType Identifier
          // check for incorrect expected stuff
//        Tree.Node<CToken> newNode = new Tree.Node<CToken>(CScanner.prevToken,parentNodes.pop());
//        parseTree.insert(newNode);
//        parentNodes.push(newNode.getNodeParent()); //parent may have more children
//        parentNodes.push(newNode);
//        boolean result;
//        
//        //DataType
//        result = dataType(f);
//        
//        //Identifier
//        result = identifier(f);
//        
//        CScanner.getNextToken(f);
//        return true;
//    }
    
//    public static boolean identifier(FileInputStream f){
//        // Identifier terminal
//        
//        // Check to make sure token is legal
//        
//        Tree.Node<CToken> newNode = new Tree.Node<CToken>();
//        newNode.setNodeData(CScanner.prevToken);
//        newNode.setNodeParent(parentNodes.pop());
//        parentNodes.push(newNode.getNodeParent()); //parent may have more children
//        parseTree.insert(newNode);
//        CScanner.getNextToken(f);
//        
//        return true;
//    }
    
//    public static boolean memberDeclaration(FileInputStream f){
//        //MemberDeclaration := DeclarationType ( DataDeclaration | FunctionDeclaration)
//        
//        // do some checks for errors
//        
//        Tree.Node<CToken> newNode = new Tree.Node<CToken>(CScanner.prevToken,parentNodes.pop());
//        parseTree.insert(newNode);
//        parentNodes.push(newNode.getNodeParent()); //parent may have more children
//        parentNodes.push(newNode);
//        
//        // DeclarationType
//        boolean result = declarationType(f);
//        
//        //DataDeclaration
//        if(CScanner.prevToken.equals(";")){
//            result = dataDeclaration(f);
//        }
//        else{//FunctionDeclaration
//            result = functionDeclaration(f);
//        }
//        
//        parentNodes.pop();
//        CScanner.getNextToken(f);
//        
//        return true;
//    }
    
//    public static boolean storageDeclaration(FileInputStream f){
////        Tree.Node<CToken> newNode = new Tree.Node<CToken>();
////        newNode.setNodeData(CScanner.prevToken);
////        newNode.setNodeParent(parentNodes.pop());
////        parentNodes.push(newNode.getNodeParent()); //parent may have more children
////        parentNodes.push(newNode);
////        
//        
////        Tree.Node<CToken> newNodeChild = new Tree.Node<CToken>();
////        newNodeChild.setNodeParent(newNode);
//        return true;
//    }
    
//    public static boolean mainDeclaration(FileInputStream f){
//       
////        Tree.Node<CToken> newNode = new Tree.Node<CToken>();
////        newNode.setNodeData(CScanner.prevToken);
////        newNode.setNodeParent(parentNodes.pop());
////        parentNodes.push(newNode.getNodeParent()); //parent may have more children
////        parentNodes.push(newNode);
//        
//                
////        Tree.Node<CToken> newNodeChild = new Tree.Node<CToken>();
////        newNodeChild.setNodeParent(newNode);
//       return true;
//    }
//    
    public static boolean interfaceDeclaration(FileInputStream f){
        
        boolean result = false;
        Tree.Node<CToken> newNode = new Tree.Node<CToken>(CScanner.prevToken,parentNodes.pop());
        parseTree.insert(newNode);
        parentNodes.push(newNode.getNodeParent()); //parent may have more children
        parentNodes.push(newNode);

        if(CScanner.prevToken.token.equals("interface")){
            CScanner.getNextToken(f);
            
            if(!CScanner.prevToken.type.equals("Identifier")){
                System.err.format("Interface Declaration rule broken @%4d .",CScanner.prevToken.lineNum);
            }
            
//            result = identifier(f);
            
            if(!result){
                System.exit(0);
            }
            
            CScanner.getNextToken(f);
            
            if(!CScanner.prevToken.token.equals("{")){
                System.err.format("Interface Declaration rule broken @%4d .",CScanner.prevToken.lineNum);
                return false;
            }
            
            newNode.addChild(new Tree.Node<CToken>(CScanner.prevToken,newNode));

//            result = memberDeclaration(f);
            
            CScanner.getNextToken(f);
            
            if(!CScanner.prevToken.token.equals("}")){
                System.err.format("Interface Declaration rule broken @%4d .",CScanner.prevToken.lineNum);
                return false;
            }
            
            newNode.addChild(new Tree.Node<CToken>(CScanner.prevToken,newNode));
            
            result = true;
        }
        else{ //MemberDeclaration
//            result = memberDeclaration(f);
            result = true;
        }
        
        parentNodes.pop(); // remove newNode from parentNodes
        CScanner.getNextToken(f);
        return result;
    }
    
    public static boolean program(FileInputStream f){
//   Program := {InterfaceDeclaration} MainDeclaration {StorageDeclaration}{ImplementationDeclaration}   
        
        if(!CScanner.prevToken.type.equals("Keyword")){
            System.err.format("Program rule broken @%4d .",CScanner.prevToken.lineNum);
            return false;
        }
        
        String[] dataTypes = {"unsigned", "char","short", "int", "long", "float", "double"};
        boolean programValid;
        Tree.Node newNode = null;
        
        //InterfaceDeclaration (optional)
        if(CScanner.prevToken.equals("interface") || Arrays.asList(dataTypes).contains(CScanner.prevToken.token)){
            System.out.println("program --> interfaceDeclaration");
            programValid = interfaceDeclaration(f);
            
        }
        
        if(newNode == null){
            System.exit(0);
        }
                    
        // MainDeclaration
        if(CScanner.prevToken.equals("void")){
//            programValid = mainDeclaration(f);
            System.out.println("program --> mainDeclaration");
//            if(!programValid){
//                System.exit(0);
//            }
            //StorageDeclaration (optional)
            if(CScanner.prevToken.equals("storage")){
//                programValid = storageDeclaration(f);
                System.out.println("program --> mainDeclaration --> storage");
            }

            //Implementation Declaration (optional)
            if(CScanner.prevToken.equals("implementation") || Arrays.asList(dataTypes).contains(CScanner.prevToken.token)){
//                programValid = implementationDeclaration(f);
                  System.out.println("program --> mainDeclaration--> implementationDeclaration");
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
            parseTree = new Tree<CToken>(CScanner.getNextToken(f)); // Get the first token
            parentNodes = new Stack<Tree.Node<CToken>>();
            parentNodes.push(parseTree.getRoot());
           
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



