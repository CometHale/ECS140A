import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class CParser {//    Based on pages 228 and 229 in "Programming Languages, Principles and Practice"
    
    public static CToken getUsePeek(FileInputStream f) {

        if (CScanner.needToUsePeekedToken) {
            return CScanner.peekedToken;
        } //we peeked before this call, so use it

        return CScanner.peekNextToken(f);
       
    }

    public static boolean parameter(FileInputStream f) {
        CToken t = new CToken();

        if (!dataType(f)) {
            return false;
        } //no data type

        t = CScanner.getNextToken(f);

        if (!t.type.equals("Identifier")) {
            System.err.format("Syntax Error: In rule Parameter unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        return true;
    }

    public static boolean parameterBlock(FileInputStream f) {
        // ParameterBlock := ( [Parameter {, Parameter}] )
        CToken t = new CToken();

        t = getUsePeek(f);

        if (!t.token.equals("(")) {
            return false;
        }

        CScanner.needToUsePeekedToken = false;
        t = CScanner.peekNextToken(f); //if peek is ")", then make sure to assign needToUsePeekedToken to false since we will consume it

        if (t.token.equals(")")) {
            CScanner.needToUsePeekedToken = false;
        } //if closing parenthese, then consume it
        else {
            if (!parameter(f)) {
                System.err.format("Syntax Error: In rule ParameterBlock unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            } //if no parameter AND we already checked that its not ")", then wrong syntax

            t = CScanner.peekNextToken(f);

            while (t.token.equals(",")) {
                CScanner.needToUsePeekedToken = false;
                if (!parameter(f)) {
                    System.err.format("Syntax Error: In rule ParameterBlock unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);   
                    System.exit(0);
                } //if theres a comma but no parameter after, then syntax is wrong

                t = CScanner.peekNextToken(f);
            } //if there is a comma, then there are more parameters

        } //not a closing parentheses -> parameter

        if (!t.token.equals(")")) {
            System.err.format("Syntax Error: In rule ParameterBlock unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //must close, if no close, its wrong af


        return true;
    }

    public static boolean functionDefinition(FileInputStream f) {
        // FunctionDefinition := DeclarationType ParameterBlock Block
        CToken t = new CToken();


        if (!declarationType(f)) {
            return false;
        }
        
        t = CScanner.peekNextToken(f);
        
        if (!parameterBlock(f)) {
            System.err.format("Syntax Error: In rule FunctionDefinition unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }
        
        t = CScanner.peekNextToken(f);
        
        if (!block(f)) {
            System.err.format("Syntax Error: In rule FunctionDefinition unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        return true;
    }

    public static boolean functionDeclaration(FileInputStream f) {
        //FunctionDeclaration := ParameterBlock ;
        CToken t = new CToken();

        if (!parameterBlock(f)) {
            return false;
        }

        t = CScanner.getNextToken(f);

        if (!t.token.equals(";")) {
            System.err.format("Syntax Error: In rule FunctionDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
        }

        return true;
    }

    public static boolean dataDeclaration(FileInputStream f){
        //DataDeclaration := ;
        CToken t = new CToken();

        t = getUsePeek(f);

        if(!t.token.equals(";")){
            return false;
        }
        
        CScanner.needToUsePeekedToken = false;
        return true;
    }
    
    public static boolean instanceType(FileInputStream f){
        //InstanceType := instance Identifier
        CToken t = new CToken();

        t = getUsePeek(f);
        
        if(!t.token.equals("instance")){
            return false;
        }
        CScanner.needToUsePeekedToken = false;        
        //token == instance
        t = CScanner.getNextToken(f);
        
        //identifier
        if(!t.type.equals("Identifier")){
            System.err.format("Syntax Error: In rule InstanceType unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //if not an identifer
        //else, we all good
       
        return true;
    }
    
    public static boolean floatType(FileInputStream f){
        //FloatType := float | double
        CToken t = new CToken();

        t = getUsePeek(f);
        
        if(!t.token.equals("float") && !t.token.equals("double")){
            return false;
        }
       
        CScanner.needToUsePeekedToken = false;
        return true;
    }
    
    public static boolean integerType(FileInputStream f){
        //IntegerType := [unsigned] ( char | short | int | long )
        CToken t = new CToken();

        t = getUsePeek(f);
        
        String[] types = {"char","short","int","long"};
        
        //unsigned (optional)
        if(t.token.equals("unsigned")){
            CScanner.needToUsePeekedToken = false; //consumed token
            t = CScanner.getNextToken(f); //get and check next token
            
            if(!Arrays.asList(types).contains(t.token)){
                System.err.format("Syntax Error: In rule IntegerType unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
        }
        else if(!Arrays.asList(types).contains(t.token)){
            return false;
        } // char | short | int | long
        
        CScanner.needToUsePeekedToken = false;

        return true;
    }
    
    public static boolean dataType(FileInputStream f){

        if(instanceType(f) || floatType(f) || integerType(f)) {
            return true;
        } //if there is a data type 
           
        return false;
    }
    
    public static boolean declarationType(FileInputStream f){
        //DeclarationType := DataType Identifier
           //check for incorrect expected stuff
        CToken t = new CToken();

        //DataType
        if(!dataType(f)){
            return false;
        }
        
        t = CScanner.getNextToken(f);

        //Identifier
        if (!t.type.equals("Identifier")) {
            System.err.format("Syntax Error: In rule DeclarationType unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        return true;
    }
    
    public static boolean memberDeclaration(FileInputStream f){
        //MemberDeclaration := DeclarationType ( DataDeclaration | FunctionDeclaration)
        CToken t = new CToken();

        // DeclarationType
        if(!declarationType(f)){
            return false;
        }
        t = CScanner.peekNextToken(f);

        //DataDeclaration and FunctionDeclaration
        if(!dataDeclaration(f) && !functionDeclaration(f)) {
            System.err.format("Syntax Error: In rule MemberDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);      
            System.exit(0);
        } //if both are not true, then error
        
        return true;
    }
  
    public static boolean interfaceDeclaration(FileInputStream f){
        //InterfaceDeclaration := ( interface Identifier { {MemberDeclaration} } ) | MemberDeclaration
        CToken t = new CToken();
        t = getUsePeek(f);
        
        if(t.token.equals("interface")){ //we peeked in main
            CScanner.needToUsePeekedToken = false;
            t = CScanner.getNextToken(f);

            if(!t.type.equals("Identifier")){
                System.err.format("Syntax Error: In rule InterfaceDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            } 

            t = CScanner.getNextToken(f);

            if(!t.token.equals("{")){
                System.err.format("Syntax Error: In rule InterfaceDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
           
            t = CScanner.peekNextToken(f); //peek since this is an optional branch 
            
            while (!t.token.equals("}")) {
                if (!memberDeclaration(f)) {
                    return false;
                }
                t = CScanner.peekNextToken(f);
            } //ends optional memberDeclartion if }
            
            if(!t.token.equals("}")){
                System.err.format("Syntax Error: In rule InterfaceDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            
        }
        else{ //MemberDeclaration
            CScanner.needToUsePeekedToken = true; //we have yet to consume this token
            if(!memberDeclaration(f)){
                return false;
            }
        }
       
        return true;
    }
    
    public static boolean constant(FileInputStream f) {
        // Constant := IntConstant | FloatConstant | InstanceConstant
        CToken t = new CToken();
        t = getUsePeek(f);

        if (t.type.equals("IntConstant") || t.type.equals("FloatConstant") || t.token.equals("nil")) {
            CScanner.needToUsePeekedToken = false;
            return true;
        }

        return false;
    }

    public static boolean allocator(FileInputStream f) {
        // Allocator := new Identifier
        CToken t = new CToken();
        t = getUsePeek(f);
            
        if (!t.token.equals("new")) {
            return false;
        }
 
        CScanner.needToUsePeekedToken = false;
        t = CScanner.getNextToken(f);

        if (!t.type.equals("Identifier")) {
            System.err.format("Syntax Error: In rule InterfaceDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //not identifier

        return true;
    }

    public static boolean factor(FileInputStream f) {
        // Factor := ( ( Expression ) ) | Constant | (Descriptor [ ( [ Expression {, Expression}] ) ] ) | Allocator
        CToken t = new CToken();

        if (allocator(f) || constant(f)) {
            return true;
        } //allocator | constant
        else if (descriptor(f)) {
            t = getUsePeek(f);
            
            if (t.token.equals("(")) {
                t = CScanner.peekNextToken(f); //get bc we need that closing paranthese (NOT OPTIONAL)  

                if (!t.token.equals(")")) {
                   if (!expression(f)) {
                      System.err.format("Syntax Error: In rule Factor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                      System.exit(0);       
                    }

                   t = getUsePeek(f);

                   while (t.token.equals(",")) {
                      CScanner.needToUsePeekedToken = false;
                      if (!expression(f)) {
                        System.err.format("Syntax Error: In rule Factor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                        System.exit(0);
                      }
                      
                      t = getUsePeek(f);
                    }

                   if (!t.token.equals(")")) {
                      System.err.format("Syntax Error: In rule Factor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                      System.exit(0);  
                   } //no closing parantheses

                   CScanner.needToUsePeekedToken = false;
                   return true;
                } //expression inside
            CScanner.needToUsePeekedToken = false;
            return true;
            } //optional parantheses

            return true;
        } //( Descriptor [ ( Expression {, Expression} ) ] )
        else if (t.token.equals("(")) {
            CScanner.needToUsePeekedToken = false;
            if (!expression(f)) {
                System.err.format("Syntax Error: In rule Factor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);  
            }
            t = CScanner.getNextToken(f);

            if (!t.token.equals(")")) {
               System.err.format("Syntax Error: In rule Factor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
               System.exit(0);  
            }
            return true;
        } // (Expression)

        return false;
    } 

    public static boolean term(FileInputStream f) {
        // Term := Factor { MultOperator Factor }
        CToken t = new CToken();

        if (!factor(f)) {
            return false;
        }

        t = getUsePeek(f);

        while (t.token.equals("*") || t.token.equals("/")) {
            CScanner.needToUsePeekedToken = false;
            if (!factor(f)) {
               System.err.format("Syntax Error: In rule Term unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
               System.exit(0); 
            }
            t = getUsePeek(f);
        } //while multopeartor

        return true;
    }

    public static boolean simpleExpression(FileInputStream f) {
        // SimpleExpression := Term { AddOperator Term }
        CToken t = new CToken();

        if (!term(f)) {
            return false;
        }

        t = getUsePeek(f);

        while (t.token.equals("+") || t.token.equals("-")) {
            CScanner.needToUsePeekedToken = false;
            if (!term(f)) {
               System.err.format("Syntax Error: In rule SimpleExpression unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
               System.exit(0); 
            }
            t = getUsePeek(f);
        } //while addopeartor
        return true;
    }

    public static boolean expression(FileInputStream f) {
        // Expression := SimpleExpression [ RelationOperator SimpleExpression ] 
        CToken t = new CToken();

        String[] relOps = {"==", ">", "<", "<=", ">=", "!="};

        if (!simpleExpression(f)) {
            return false;
        }

        t = getUsePeek(f);

        if (Arrays.asList(relOps).contains(t.token)) {
            CScanner.needToUsePeekedToken = false;
            if (!simpleExpression(f)) {
               System.err.format("Syntax Error: In rule Expression unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
               System.exit(0);  
            }

            t = getUsePeek(f);
        } //has the optional relationOperator

        return true;
    } 

    public static boolean descriptor(FileInputStream f) {
        // Descriptor := (Identifier | self | global) {. Identifier}
        CToken t = new CToken();
        t = getUsePeek(f);

        if (!t.type.equals("Identifier") && !t.token.equals("self") && !t.token.equals("global")) {
            return false;
        } //neither an identifier, self, or global

        CScanner.needToUsePeekedToken = false;
        t = CScanner.peekNextToken(f);

        while (t.token.equals(".")) {
            t = CScanner.getNextToken(f);
            if (!t.type.equals("Identifier")) {
                System.err.format("Syntax Error: In rule Descriptor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0); 
            }
            t = CScanner.peekNextToken(f);
        } //more identifiers

        return true;
    }

    public static boolean assignment(FileInputStream f) {
        // Assignment := let Descriptor = Expression ;
        CToken t = new CToken();
        t = getUsePeek(f);

        if (!t.token.equals("let")) {
            return false;
        }
        CScanner.needToUsePeekedToken = false;

        if (!descriptor(f)) {
            System.err.format("Syntax Error: In rule Assignment unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0); 
        }

        t = getUsePeek(f);

        if (!t.token.equals("=")) {
            System.err.format("Syntax Error: In rule Assignment unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0); 
        }

        CScanner.needToUsePeekedToken = false;
            
        if (!expression(f)) {
            System.err.format("Syntax Error: In rule Assignment unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0); 
        }

        if (CScanner.needToUsePeekedToken) {
            t = CScanner.peekedToken;
        } //we peeked before this call, so use it
        else {
            t = CScanner.getNextToken(f);
        } //get token

        if (!t.token.equals(";")) {
            System.err.format("Syntax Error: In rule Assignment unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);  
        }

        return true;
    }

    public static boolean whileLoop(FileInputStream f) {
        // WhileLoop := while ( Expression ) Block
        CToken t = new CToken();
        t = getUsePeek(f);

        if (!t.token.equals("while")) {
            return false;
        }

        CScanner.needToUsePeekedToken = false;
        t = CScanner.getNextToken(f);

        if (!t.token.equals("(")) {
            System.err.format("Syntax Error: In rule WhileLoop unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0); 
        }

        if (!expression(f)) {
            System.err.format("Syntax Error: In rule WhileLoop unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        t = getUsePeek(f);

        if (!t.token.equals(")")) {
            System.err.format("Syntax Error: In rule WhileLoop unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //closing 

        CScanner.needToUsePeekedToken = false;

        if (!block(f)) {
            System.err.format("Syntax Error: In rule WhileLoop unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        return true;
    }

    public static boolean ifStatement(FileInputStream f) {
        // IfStatement := if ( Expression ) Block
        CToken t = new CToken();
        t = getUsePeek(f);

        if (!t.token.equals("if")) {
            return false;
        }

        CScanner.needToUsePeekedToken = false;
        t = CScanner.getNextToken(f);

        if (!t.token.equals("(")) {
            System.err.format("Syntax Error: In rule IfStatement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0); 
        }

        if (!expression(f)) {
            System.err.format("Syntax Error: In rule IfStatement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        t = getUsePeek(f);

        if (!t.token.equals(")")) {
            System.err.format("Syntax Error: In rule IfStatement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //closing 

        CScanner.needToUsePeekedToken = false;

        if (!block(f)) {
            System.err.format("Syntax Error: In rule IfStatement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        return true;
    }

    public static boolean returnStatement(FileInputStream f) {
        // ReturnStatement := return Expression ;
        CToken t = new CToken();
        t = getUsePeek(f);

        if (!t.token.equals("return")) {
            return false;
        }
            
        CScanner.needToUsePeekedToken = false;

        if (!expression(f)) {
            System.err.format("Syntax Error: In rule ReturnStatement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        t = getUsePeek(f);

        if (!t.token.equals(";")) {
            System.err.format("Syntax Error: In rule ReturnStatement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //need semicolon

        return true;
    }

    public static boolean statement(FileInputStream f) {
        // Statement := Assignment | WhileLoop | IfStatement | ReturnStatement | (Expression ;)
        CToken t = new CToken();
                        
        if (assignment(f) || whileLoop(f) || ifStatement(f) || returnStatement(f)) {
            return true;
        }
        else if (expression(f)) {

            t = getUsePeek(f);

            if (!t.token.equals(";")) {
                System.err.format("Syntax Error: In rule Statement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }

            return true;
        } //(Expression ;)

        return false;
    }

    public static boolean variableDeclaration(FileInputStream f) {
        // VariableDeclaration := DeclarationType [= Constant] ;
        CToken t = new CToken();

        if (!declarationType(f)) {
            return false;
        }

        t = CScanner.getNextToken(f);

        if (t.token.equals("=")) {
            t = CScanner.peekNextToken(f);
            if (!constant(f)) {
                System.err.format("Syntax Error: In rule VariableDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }

            t = CScanner.getNextToken(f);

            if (!t.token.equals(";")) {
                System.err.format("Syntax Error: In rule VariableDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            } //if not semi colon
        } //must follow with constant
        else if (!t.token.equals(";")) {
            System.err.format("Syntax Error: In rule VariableDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //if not semicolon nor equals

        return true;
    }

    public static boolean block(FileInputStream f) {
        // Block := { { VariableDeclaration } {Statement} }
        CToken t = new CToken();
        t = getUsePeek(f);

        String[] dataTypes = {"unsigned", "char","short", "int", "long", "float", "double", "instance"};

        if (!t.token.equals("{")) {
            System.err.format("Syntax Error: In rule Block unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //{

        CScanner.needToUsePeekedToken = false;
        t = CScanner.peekNextToken(f);

        while (Arrays.asList(dataTypes).contains(t.token)) {
            if (!variableDeclaration(f)) {
                System.err.format("Syntax Error: In rule Block unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            t = CScanner.peekNextToken(f);
        } //some datatype -> { VariableDeclaration }

        while (!t.token.equals("}")) {
            if (!statement(f)) {
                System.err.format("Syntax Error: In rule Block unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            t = CScanner.peekNextToken(f);

        } //{Statement}

        if (!t.token.equals("}")) {
            System.err.format("Syntax Error: In rule Block unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //}

        return true;
    }

    public static boolean mainDeclaration(FileInputStream f) {
        // void main ( ) Block 
        CToken t = new CToken();
        t = getUsePeek(f);

        if (!t.token.equals("void")) {
            System.err.format("Syntax Error: In rule MainDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //if not void, error

        CScanner.needToUsePeekedToken = false; //we have consumed the token
        t = CScanner.getNextToken(f);

        if (!t.token.equals("main")) {
            System.err.format("Syntax Error: In rule MainDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        t = CScanner.getNextToken(f);

        if (!t.token.equals("(")) {
            System.err.format("Syntax Error: In rule MainDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        t = CScanner.getNextToken(f);

        if (!t.token.equals(")")) {
            System.err.format("Syntax Error: In rule MainDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        if (!block(f)) {
            System.err.format("Syntax Error: In rule MainDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        return true;
    }

    public static boolean storageDeclaration(FileInputStream f) {
        // StorageDeclaration := storage ( Identifier | global ) { {VariableDeclaration}}
        //we consume the storage beforehand so next token to check is Identifer/global
        String[] dataTypes = {"unsigned", "char","short", "int", "long", "float", "double", "instance"};    
        CToken t = new CToken();

        t = getUsePeek(f);

        if (!t.token.equals("storage")) {
            return false;
        }

        CScanner.needToUsePeekedToken = false;
        t = CScanner.getNextToken(f);

        if (!t.type.equals("Identifier") && !t.token.equals("global")) {
            System.err.format("Syntax Error: In rule StorageDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //(Identifier | global)

        t = CScanner.getNextToken(f);

        if (!t.token.equals("{")) {
            System.err.format("Syntax Error: In rule StorageDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        t = CScanner.peekNextToken(f);

        while (Arrays.asList(dataTypes).contains(t.token)) {
            if (!variableDeclaration(f)) {
                System.err.format("Syntax Error: In rule StorageDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            t = CScanner.peekNextToken(f);
        } //some datatype -> { VariableDeclaration }

        if (!t.token.equals("}")) {
            System.err.format("Syntax Error: In rule StorageDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        return true;
    }

    public static boolean implementationDeclaration(FileInputStream f) {
        // ImplementationDeclaration := ( implementation Identifier {{FunctionDefinition} } ) | FunctionDefinition
        String[] dataTypes = {"unsigned", "char","short", "int", "long", "float", "double", "instance"}; 
        CToken t = new CToken();

        t = getUsePeek(f);

        if (t.token.equals("implementation")) {
            CScanner.needToUsePeekedToken = false;
            t = CScanner.getNextToken(f);

            if (!t.type.equals("Identifier")) {
                System.err.format("Syntax Error: In rule ImplementationDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }

            t = CScanner.getNextToken(f);

            if (!t.token.equals("{")) {
                System.err.format("Syntax Error: In rule ImplementationDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }

            t = CScanner.peekNextToken(f);

            while (Arrays.asList(dataTypes).contains(t.token)) {
                if (!functionDefinition(f)) {
                    System.err.format("Syntax Error: In rule ImplementationDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                    System.exit(0);
                }
                t = CScanner.peekNextToken(f);
            } //{ FunctionDefinitoin }

            if (!t.token.equals("}")) {
                System.err.format("Syntax Error: In rule ImplementationDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            return true;
        } //( implementation Identifier {{FunctionDefinition} } ) 
        else if (functionDefinition(f)) {
            return true;
        }
        return false;
    }

    public static boolean program(FileInputStream f){
//   Program := {InterfaceDeclaration} MainDeclaration {StorageDeclaration}{ImplementationDeclaration}   

        CToken t = new CToken();
        t = CScanner.peekNextToken(f);

        String[] dataTypes = {"unsigned", "char","short", "int", "long", "float", "double", "instance"};   
        
        //InterfaceDeclaration (optional)
        while (!t.token.equals("void")) {
            if(!interfaceDeclaration(f)){
                System.err.format("Syntax Error: In rule Program unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);    
            }//Interface Declaration failed

            t = CScanner.peekNextToken(f);
        }
             
        // MainDeclaration
        if (!mainDeclaration(f)) {
            System.err.format("Syntax Error: In rule Program unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);     
        }

        t = CScanner.peekNextToken(f);

        while (t.token.equals("storage")) {
            if (!storageDeclaration(f)) {
                System.err.format("Syntax Error: In rule Program unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);    
            }
            t = CScanner.peekNextToken(f);
        } //Storage Declaration

        while (!t.type.equals("None")) {
            if (!implementationDeclaration(f)) {
                System.err.format("Syntax Error: In rule Program unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);    
            }
            t = CScanner.peekNextToken(f);
        } //Implementation Declaration
        
        return true;
    }
    
    public static void main(String[] args) {
        
        
        
        if (args.length == 0) {
            System.exit(0);
        }
        
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



