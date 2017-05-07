import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class CParser{//    Based on pages 228 and 229 in "Programming Languages, Principles and Practice"

    private static CTranslator observer = null;
    
    public static CToken getUsePeek(FileInputStream f) {
        
        if(CScanner.needToUsePeekedToken) {
            if(observer != null){
                observer.parser.setCurrentToken(CScanner.peekedToken);
            }
            return CScanner.peekedToken;
        } //we peeked before this call, so use it
        if(observer != null){
            observer.parser.setCurrentToken(CScanner.peekNextToken(f));
            return CScanner.peekedToken;
        }
                
        return CScanner.peekNextToken(f);
       
    }
    
    public static boolean parameter(FileInputStream f) {
        if(observer != null){
            observer.parser.setCurrentRule("parameter", true);
        }
        CToken t = new CToken();

        if (!dataType(f)) {
            return false;
        } //no data type
        if(observer != null){
            observer.parser.setCurrentRule("parameter", true);
        }
        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        if (!t.type.equals("Identifier")) {
            System.err.format("Syntax Error: In rule Parameter unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }
        if(observer != null){
            observer.parser.setCurrentRule("parameter", false);
        }
        return true;
    }

    public static boolean parameterBlock(FileInputStream f) {
        // ParameterBlock := ( [Parameter {, Parameter}] )
        if(observer != null){
            observer.parser.setCurrentRule("parameterBlock", true);
        }
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
            if(observer != null){
                observer.parser.setCurrentRule("parameterBlock", true);
            }
            t = CScanner.peekNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
            while (t.token.equals(",")) {
                CScanner.needToUsePeekedToken = false;
                if (!parameter(f)) {
                    System.err.format("Syntax Error: In rule ParameterBlock unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);   
                    System.exit(0);
                } //if theres a comma but no parameter after, then syntax is wrong
                if(observer != null){
                    observer.parser.setCurrentRule("parameterBlock", true);
                }
                t = CScanner.peekNextToken(f);
                if(observer != null){
                    observer.parser.setCurrentToken(t);
                }
            } //if there is a comma, then there are more parameters

        } //not a closing parentheses -> parameter

        if (!t.token.equals(")")) {
            System.err.format("Syntax Error: In rule ParameterBlock unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //must close, if no close, its wrong af
        if(observer != null){
            observer.parser.setCurrentRule("parameterBlock", false);
        }
        return true;
    }

    public static boolean functionDefinition(FileInputStream f) {
        // FunctionDefinition := DeclarationType ParameterBlock Block
        if(observer != null){
            observer.parser.setCurrentRule("functionDefinition", true);
        }
        CToken t = new CToken();


        if (!declarationType(f)) {
            return false;
        }
        if(observer != null){
            observer.parser.setCurrentRule("functionDefinition", true);
        }
        t = CScanner.peekNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        
        if (!parameterBlock(f)) {
            System.err.format("Syntax Error: In rule FunctionDefinition unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }
        if(observer != null){
            observer.parser.setCurrentRule("functionDefinition", true);
        }
        t = CScanner.peekNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        if (!block(f)) {
            System.err.format("Syntax Error: In rule FunctionDefinition unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }
        if(observer != null){
            observer.parser.setCurrentRule("functionDefinition", true);
        }
        return true;
    }

    public static boolean functionDeclaration(FileInputStream f) {
        //FunctionDeclaration := ParameterBlock ;
        if(observer != null){
            observer.parser.setCurrentRule("functionDeclaration", true);
        }
        CToken t = new CToken();

        if (!parameterBlock(f)) {
            return false;
        }
        if(observer != null){
            observer.parser.setCurrentRule("functionDeclaration", true);
        }
        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }

        if (!t.token.equals(";")) {
            System.err.format("Syntax Error: In rule FunctionDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
        }
        if(observer != null){
            observer.parser.setCurrentRule("functionDeclaration", false);
        }
        return true;
    }

    public static boolean dataDeclaration(FileInputStream f){
        //DataDeclaration := ;
        if(observer != null){
            observer.parser.setCurrentRule("dataDeclaration", true);
        }
        CToken t = new CToken();

        t = getUsePeek(f);

        if(!t.token.equals(";")){
            return false;
        }
        
        CScanner.needToUsePeekedToken = false;
        if(observer != null){
            observer.parser.setCurrentRule("dataDeclaration", false);
        }
        return true;
    }
    
    public static boolean instanceType(FileInputStream f){
        //InstanceType := instance Identifier
        if(observer != null){
            observer.parser.setCurrentRule("instanceType", true);
        }
        CToken t = new CToken();

        t = getUsePeek(f);
        
        if(!t.token.equals("instance")){
            return false;
        }
        CScanner.needToUsePeekedToken = false;        
        //token == instance
        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        //identifier
        if(!t.type.equals("Identifier")){
            System.err.format("Syntax Error: In rule InstanceType unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //if not an identifer
        //else, we all good
        if(observer != null){
            observer.parser.setCurrentRule("instanceType", false);
        }
        return true;
    }
    
    public static boolean floatType(FileInputStream f){
        //FloatType := float | double
        if(observer != null){
            observer.parser.setCurrentRule("floatType", true);
        }
        CToken t = new CToken();

        t = getUsePeek(f);
        
        if(!t.token.equals("float") && !t.token.equals("double")){
            return false;
        }
       
        CScanner.needToUsePeekedToken = false;
        if(observer != null){
            observer.parser.setCurrentRule("floatType", false);
        }
        return true;
    }
    
    public static boolean integerType(FileInputStream f){
        //IntegerType := [unsigned] ( char | short | int | long )
        if(observer != null){
            observer.parser.setCurrentRule("integerType", true);
        }
        CToken t = new CToken();

        t = getUsePeek(f);
        
        String[] types = {"char","short","int","long"};
        
        //unsigned (optional)
        if(t.token.equals("unsigned")){
            CScanner.needToUsePeekedToken = false; //consumed token
            t = CScanner.getNextToken(f); //get and check next token
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
            if(!Arrays.asList(types).contains(t.token)){
                System.err.format("Syntax Error: In rule IntegerType unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
        }
        else if(!Arrays.asList(types).contains(t.token)){
            if(observer != null){
                observer.parser.setCurrentRule("integerType", false);
            }
            return false;
        } // char | short | int | long
        
        CScanner.needToUsePeekedToken = false;
        if(observer != null){
            observer.parser.setCurrentRule("integerType", false);
        }
        return true;
    }
    
    public static boolean dataType(FileInputStream f){

        if(instanceType(f) || floatType(f) || integerType(f)) {
            if(observer != null){
                observer.parser.setCurrentRule("dataType", false);
            }
            return true;
        } //if there is a data type 
        if(observer != null){
            observer.parser.setCurrentRule("dataType", false);
        }
        return false;
    }
    
    public static boolean declarationType(FileInputStream f){
        //DeclarationType := DataType Identifier
           //check for incorrect expected stuff
        if(observer != null){
            observer.parser.setCurrentRule("declarationType", true);
        }
        CToken t = new CToken();

        //DataType
        if(!dataType(f)){
            return false;
        }
        if(observer != null){
            observer.parser.setCurrentRule("declarationType", true);
        }
        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        //Identifier
        if (!t.type.equals("Identifier")) {
            System.err.format("Syntax Error: In rule DeclarationType unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }
        if(observer != null){
            observer.parser.setCurrentRule("declarationType", false);
        }
        return true;
    }
    
    public static boolean memberDeclaration(FileInputStream f){
        //MemberDeclaration := DeclarationType ( DataDeclaration | FunctionDeclaration)
        if(observer != null){
            observer.parser.setCurrentRule("memberDeclaration", true);
        }
        CToken t = new CToken();

        // DeclarationType
        if(!declarationType(f)){
            return false;
        }
        if(observer != null){
            observer.parser.setCurrentRule("memberDeclaration", true);
        }
        t = CScanner.peekNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        //DataDeclaration and FunctionDeclaration
        if(!dataDeclaration(f) && !functionDeclaration(f)) {
            System.err.format("Syntax Error: In rule MemberDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);      
            System.exit(0);
        } //if both are not true, then error
        if(observer != null){
            observer.parser.setCurrentRule("memberDeclaration", false);
        }
        return true;
    }
  
    public static boolean interfaceDeclaration(FileInputStream f){
        //InterfaceDeclaration := ( interface Identifier { {MemberDeclaration} } ) | MemberDeclaration
        if(observer != null){
            observer.parser.setCurrentRule("interfaceDeclaration", true);
        }
        CToken t = new CToken();
        t = getUsePeek(f);
        
        if(t.token.equals("interface")){ //we peeked in main
            CScanner.needToUsePeekedToken = false;
            t = CScanner.getNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
            if(!t.type.equals("Identifier")){
                System.err.format("Syntax Error: In rule InterfaceDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            } 

            t = CScanner.getNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
            if(!t.token.equals("{")){
                System.err.format("Syntax Error: In rule InterfaceDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
           
            t = CScanner.peekNextToken(f); //peek since this is an optional branch 
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
            while (!t.token.equals("}")) {
                if (!memberDeclaration(f)) {
                    return false;
                }
                if(observer != null){
                    observer.parser.setCurrentRule("interfaceDeclaration", true);
                }
                t = CScanner.peekNextToken(f);
                if(observer != null){
                    observer.parser.setCurrentToken(t);
                }
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
            if(observer != null){
                observer.parser.setCurrentRule("interfaceDeclaration", true);
            }
        }
        if(observer != null){
            observer.parser.setCurrentRule("interfaceDeclaration", false);
        }
        return true;
    }
    
    public static boolean constant(FileInputStream f) {
        // Constant := IntConstant | FloatConstant | InstanceConstant
        if(observer != null){
            observer.parser.setCurrentRule("constant", true);
        }
        CToken t = new CToken();
        t = getUsePeek(f);

        if (t.type.equals("IntConstant") || t.type.equals("FloatConstant") || t.token.equals("nil")) {
            CScanner.needToUsePeekedToken = false;
            if(observer != null){
                observer.parser.setCurrentRule("constant", false);
            }
            return true;
        }
        if(observer != null){
            observer.parser.setCurrentRule("constant", false);
        }
        return false;
    }

    public static boolean allocator(FileInputStream f) {
        // Allocator := new Identifier
        if(observer != null){
            observer.parser.setCurrentRule("allocator", true);
        }
        CToken t = new CToken();
        t = getUsePeek(f);
            
        if (!t.token.equals("new")) {
            return false;
        }
 
        CScanner.needToUsePeekedToken = false;
        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        if (!t.type.equals("Identifier")) {
            System.err.format("Syntax Error: In rule InterfaceDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //not identifier
        if(observer != null){
            observer.parser.setCurrentRule("allocator", false);
        }
        return true;
    }

    public static boolean factor(FileInputStream f) {
        // Factor := ( ( Expression ) ) | Constant | (Descriptor [ ( [ Expression {, Expression}] ) ] ) | Allocator
        if(observer != null){
            observer.parser.setCurrentRule("factor", true);
        }
        CToken t = new CToken();

        if (allocator(f) || constant(f)) {
            if(observer != null){
                observer.parser.setCurrentRule("factor", false);
            }
            return true;
        } //allocator | constant
        else if (descriptor(f)) {
            if(observer != null){
                observer.parser.setCurrentRule("factor", true);
            }
            t = getUsePeek(f);
            
            if (t.token.equals("(")) {
                t = CScanner.peekNextToken(f); //get bc we need that closing paranthese (NOT OPTIONAL)  
                if(observer != null){
                    observer.parser.setCurrentToken(t);
                }
                if (!t.token.equals(")")) {
                   if (!expression(f)) {
                      System.err.format("Syntax Error: In rule Factor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                      System.exit(0);       
                    }
                   if(observer != null){
                    observer.parser.setCurrentRule("factor", true);
                   }
                   t = getUsePeek(f);

                   while (t.token.equals(",")) {
                      CScanner.needToUsePeekedToken = false;
                      if (!expression(f)) {
                        System.err.format("Syntax Error: In rule Factor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                        System.exit(0);
                      }
                      if(observer != null){
                        observer.parser.setCurrentRule("factor", true);
                      }
                      t = getUsePeek(f);
                      if(observer != null){
                        observer.parser.setCurrentToken(t);
                      }
                    }

                   if (!t.token.equals(")")) {
                      System.err.format("Syntax Error: In rule Factor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                      System.exit(0);  
                   } //no closing parantheses

                   CScanner.needToUsePeekedToken = false;
                   if(observer != null){
                        observer.parser.setCurrentRule("factor", false);
                   }
                   return true;
                } //expression inside
            CScanner.needToUsePeekedToken = false;
            if(observer != null){
                observer.parser.setCurrentRule("factor", false);
            }
            return true;
            } //optional parantheses
            if(observer != null){
                observer.parser.setCurrentRule("factor", false);
            }
            return true;
        } //( Descriptor [ ( Expression {, Expression} ) ] )
        else if (t.token.equals("(")) {
            CScanner.needToUsePeekedToken = false;
            if (!expression(f)) {
                System.err.format("Syntax Error: In rule Factor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);  
            }
            if(observer != null){
                observer.parser.setCurrentRule("factor", true);
            }
            t = CScanner.getNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
            if (!t.token.equals(")")) {
               System.err.format("Syntax Error: In rule Factor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
               System.exit(0);  
            }
            if(observer != null){
                observer.parser.setCurrentRule("factor", false);
            }
            return true;
        } // (Expression)
        if(observer != null){
            observer.parser.setCurrentRule("factor", false);
        }
        return false;
    } 

    public static boolean term(FileInputStream f) {
        // Term := Factor { MultOperator Factor }
        if(observer != null){
            observer.parser.setCurrentRule("term", true);
        }
        CToken t = new CToken();

        if (!factor(f)) {
            return false;
        }
        if(observer != null){
            observer.parser.setCurrentRule("term", true);
        }
        t = getUsePeek(f);

        while (t.token.equals("*") || t.token.equals("/")) {
            CScanner.needToUsePeekedToken = false;
            if (!factor(f)) {
               System.err.format("Syntax Error: In rule Term unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
               System.exit(0); 
            }
            if(observer != null){
                observer.parser.setCurrentRule("term", true);
            }
            t = getUsePeek(f);
        } //while multopeartor
        if(observer != null){
            observer.parser.setCurrentRule("term", false);
        }
        return true;
    }

    public static boolean simpleExpression(FileInputStream f) {
        // SimpleExpression := Term { AddOperator Term }
        if(observer != null){
            observer.parser.setCurrentRule("simpleExpression", true);
        }
        CToken t = new CToken();

        if (!term(f)) {
            return false;
        }
        if(observer != null){
            observer.parser.setCurrentRule("simpleExpression", true);
        }
        t = getUsePeek(f);

        while (t.token.equals("+") || t.token.equals("-")) {
            CScanner.needToUsePeekedToken = false;
            if (!term(f)) {
               System.err.format("Syntax Error: In rule SimpleExpression unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
               System.exit(0); 
            }
            if(observer != null){
                observer.parser.setCurrentRule("simpleExpression", true);
            }
            t = getUsePeek(f);
        } //while addopeartor
        if(observer != null){
            observer.parser.setCurrentRule("simpleExpression", false);
        }
        return true;
    }

    public static boolean expression(FileInputStream f) {
        // Expression := SimpleExpression [ RelationOperator SimpleExpression ] 
        if(observer != null){
            observer.parser.setCurrentRule("expression", true);
        }
        CToken t = new CToken();

        String[] relOps = {"==", ">", "<", "<=", ">=", "!="};

        if (!simpleExpression(f)) {
            return false;
        }
        if(observer != null){
            observer.parser.setCurrentRule("expression", true);
        }
        
        t = getUsePeek(f);

        if (Arrays.asList(relOps).contains(t.token)) {
            CScanner.needToUsePeekedToken = false;
            if (!simpleExpression(f)) {
               System.err.format("Syntax Error: In rule Expression unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
               System.exit(0);  
            }
            if(observer != null){
                observer.parser.setCurrentRule("expression", true);
            }
            t = getUsePeek(f);
        } //has the optional relationOperator
        if(observer != null){
            observer.parser.setCurrentRule("descriptor", false);
        }
        return true;
    } 

    public static boolean descriptor(FileInputStream f) {
        // Descriptor := (Identifier | self | global) {. Identifier}
        if(observer != null){
            observer.parser.setCurrentRule("descriptor", true);
        }
        CToken t = new CToken();
        t = getUsePeek(f);

        if (!t.type.equals("Identifier") && !t.token.equals("self") && !t.token.equals("global")) {
            return false;
        } //neither an identifier, self, or global

        CScanner.needToUsePeekedToken = false;
        t = CScanner.peekNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }

        while (t.token.equals(".")) {
            t = CScanner.getNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
            if (!t.type.equals("Identifier")) {
                System.err.format("Syntax Error: In rule Descriptor unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0); 
            }
            t = CScanner.peekNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
        } //more identifiers
        if(observer != null){
            observer.parser.setCurrentRule("descriptor", false);
        }
        return true;
    }

    public static boolean assignment(FileInputStream f) {
        // Assignment := let Descriptor = Expression ;
        if(observer != null){
            observer.parser.setCurrentRule("assignment", true);
        }
        
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
        if(observer != null){
            observer.parser.setCurrentRule("assignment", true);
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
        if(observer != null){
            observer.parser.setCurrentRule("assignment", true);
        }
        if (CScanner.needToUsePeekedToken) {
            t = CScanner.peekedToken;
        } //we peeked before this call, so use it
        else {
            t = CScanner.getNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
        } //get token

        if (!t.token.equals(";")) {
            System.err.format("Syntax Error: In rule Assignment unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);  
        }
        if(observer != null){
            observer.parser.setCurrentRule("assignment", false);
        }
        return true;
    }

    public static boolean whileLoop(FileInputStream f) {
        // WhileLoop := while ( Expression ) Block
        if(observer != null){
            observer.parser.setCurrentRule("whileLoop", true);
        }
        CToken t = new CToken();
        t = getUsePeek(f);

        if (!t.token.equals("while")) {
            return false;
        }

        CScanner.needToUsePeekedToken = false;
        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        if (!t.token.equals("(")) {
            System.err.format("Syntax Error: In rule WhileLoop unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0); 
        }

        if (!expression(f)) {
            System.err.format("Syntax Error: In rule WhileLoop unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }
        if(observer != null){
            observer.parser.setCurrentRule("whileLoop", true);
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
        if(observer != null){
            observer.parser.setCurrentRule("whileLoop", false);
        }
        return true;
    }

    public static boolean ifStatement(FileInputStream f) {
        // IfStatement := if ( Expression ) Block
        if(observer != null){
            observer.parser.setCurrentRule("ifStatement",true);
        }
        CToken t = new CToken();
        t = getUsePeek(f);

        if (!t.token.equals("if")) {
            return false;
        }

        CScanner.needToUsePeekedToken = false;
        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        
        if (!t.token.equals("(")) {
            System.err.format("Syntax Error: In rule IfStatement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0); 
        }

        if (!expression(f)) {
            System.err.format("Syntax Error: In rule IfStatement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }
        if(observer != null){
            observer.parser.setCurrentRule("ifStatement",true);
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
        if(observer != null){
            observer.parser.setCurrentRule("ifStatement",false);
        }
        
        return true;
    }

    public static boolean returnStatement(FileInputStream f) {
        // ReturnStatement := return Expression ;
        if(observer != null){
            observer.parser.setCurrentRule("returnStatement",true);
        }
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
        if(observer != null){
            observer.parser.setCurrentRule("returnStatement",true);
        }
        
        t = getUsePeek(f);

        if (!t.token.equals(";")) {
            System.err.format("Syntax Error: In rule ReturnStatement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //need semicolon
        if(observer != null){
            observer.parser.setCurrentRule("returnStatement", false);
        }
        return true;
    }

    public static boolean statement(FileInputStream f) {
        // Statement := Assignment | WhileLoop | IfStatement | ReturnStatement | (Expression ;)
        if(observer != null){
            observer.parser.setCurrentRule("statement",true);
        }
        
        CToken t = new CToken();
                        
        if (assignment(f) || whileLoop(f) || ifStatement(f) || returnStatement(f)) {
            if(observer != null){
                observer.parser.setCurrentRule("statement", false);
            }
            return true;
        }
        else if (expression(f)) {
            if(observer != null){
                observer.parser.setCurrentRule("statement",true);
            }
            t = getUsePeek(f);

            if (!t.token.equals(";")) {
                System.err.format("Syntax Error: In rule Statement unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            if(observer != null){
                observer.parser.setCurrentRule("statement", false);
            }
            return true;
        } //(Expression ;)
        if(observer != null){
            observer.parser.setCurrentRule("statement", false);
        }
        return false;
    }

    public static boolean variableDeclaration(FileInputStream f) {
        // VariableDeclaration := DeclarationType [= Constant] ;
        CToken t = new CToken();
        if(observer != null){
            observer.parser.setCurrentRule("variableDeclaration",true);
        }
        
        if (!declarationType(f)) {
            return false;
        }
        if(observer != null){
            observer.parser.setCurrentRule("mainDeclaration",true);
        }
        
        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        
        if (t.token.equals("=")) {
            t = CScanner.peekNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
            if (!constant(f)) {
                System.err.format("Syntax Error: In rule VariableDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            if(observer != null){
                observer.parser.setCurrentRule("variableDeclaration",true);
            }
            t = CScanner.getNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }

            if (!t.token.equals(";")) {
                System.err.format("Syntax Error: In rule VariableDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            } //if not semi colon
            if(observer != null){
                observer.parser.setCurrentRule("variableDeclaration",true);
            }
        } //must follow with constant
        else if (!t.token.equals(";")) {
            System.err.format("Syntax Error: In rule VariableDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //if not semicolon nor equals
        if(observer != null){
            observer.parser.setCurrentRule("variableDeclaration", false);
        }
        return true;
    }

    public static boolean block(FileInputStream f) {
        // Block := { { VariableDeclaration } {Statement} }
        if(observer != null){
            observer.parser.setCurrentRule("block",true);
        }
        
        CToken t = new CToken();
        t = getUsePeek(f);

        String[] dataTypes = {"unsigned", "char","short", "int", "long", "float", "double", "instance"};

        if (!t.token.equals("{")) {
            System.err.format("Syntax Error: In rule Block unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //{

        CScanner.needToUsePeekedToken = false;
        t = CScanner.peekNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }

        while (Arrays.asList(dataTypes).contains(t.token)) {
            if (!variableDeclaration(f)) {
                System.err.format("Syntax Error: In rule Block unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            if(observer != null){
                observer.parser.setCurrentRule("block",true);
            }
            t = CScanner.peekNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
        } //some datatype -> { VariableDeclaration }

        while (!t.token.equals("}")) {
            if (!statement(f)) {
                System.err.format("Syntax Error: In rule Block unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            if(observer != null){
                observer.parser.setCurrentRule("block",true);
            }
            t = CScanner.peekNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }

        } //{Statement}

        if (!t.token.equals("}")) {
            System.err.format("Syntax Error: In rule Block unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //}
        if(observer != null){
            observer.parser.setCurrentRule("block",false);
        }
        
        return true;
    }

    public static boolean mainDeclaration(FileInputStream f) {
        // void main ( ) Block 
        if(observer != null){
            observer.parser.setCurrentRule("mainDeclaration",true);
        }
        
        CToken t = new CToken();
        t = getUsePeek(f);

        if (!t.token.equals("void")) {
            System.err.format("Syntax Error: In rule MainDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //if not void, error

        CScanner.needToUsePeekedToken = false; //we have consumed the token
        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        
        if (!t.token.equals("main")) {
            System.err.format("Syntax Error: In rule MainDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        
        if (!t.token.equals("(")) {
            System.err.format("Syntax Error: In rule MainDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        
        if (!t.token.equals(")")) {
            System.err.format("Syntax Error: In rule MainDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        if (!block(f)) {
            System.err.format("Syntax Error: In rule MainDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }
        if(observer != null){
            observer.parser.setCurrentRule("mainDeclaration",false);
        }
        return true;
    }

    public static boolean storageDeclaration(FileInputStream f) {
        // StorageDeclaration := storage ( Identifier | global ) { {VariableDeclaration}}
        //we consume the storage beforehand so next token to check is Identifer/global
        if(observer != null){
            observer.parser.setCurrentRule("storageDeclaration",true);
        }
        
        String[] dataTypes = {"unsigned", "char","short", "int", "long", "float", "double", "instance"};    
        CToken t = new CToken();
        
        t = getUsePeek(f);

        if (!t.token.equals("storage")) {
            if(observer != null){
                observer.parser.setCurrentRule("storageDeclaration",false);
            }
            return false;
        }

        CScanner.needToUsePeekedToken = false;
        t = CScanner.getNextToken(f);
        
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }

        if (!t.type.equals("Identifier") && !t.token.equals("global")) {
            System.err.format("Syntax Error: In rule StorageDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        } //(Identifier | global)

        t = CScanner.getNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }
        
        if (!t.token.equals("{")) {
            System.err.format("Syntax Error: In rule StorageDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }

        t = CScanner.peekNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }

        while (Arrays.asList(dataTypes).contains(t.token)) {
            if (!variableDeclaration(f)) {
                System.err.format("Syntax Error: In rule StorageDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            if(observer != null){
                observer.parser.setCurrentRule("storageDeclaration",true);
            }
            t = CScanner.peekNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
        } //some datatype -> { VariableDeclaration }

        if (!t.token.equals("}")) {
            System.err.format("Syntax Error: In rule StorageDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);
        }
        
        if(observer != null){
            observer.parser.setCurrentRule("implementationDeclaration",false);
        }
        
        return true;
    }

    public static boolean implementationDeclaration(FileInputStream f) {
        // ImplementationDeclaration := ( implementation Identifier {{FunctionDefinition} } ) | FunctionDefinition
        String[] dataTypes = {"unsigned", "char","short", "int", "long", "float", "double", "instance"}; 
        CToken t = new CToken();

        t = getUsePeek(f);
        if(observer != null){
            observer.parser.setCurrentRule("implementationDeclaration", true);
        }
        
        if (t.token.equals("implementation")) {
            CScanner.needToUsePeekedToken = false;
            t = CScanner.getNextToken(f);
            
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
            
            if (!t.type.equals("Identifier")) {
                System.err.format("Syntax Error: In rule ImplementationDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            
            t = CScanner.getNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
            
            if (!t.token.equals("{")) {
                System.err.format("Syntax Error: In rule ImplementationDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }

            t = CScanner.peekNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }

            while (Arrays.asList(dataTypes).contains(t.token)) {
                if (!functionDefinition(f)) {
                    System.err.format("Syntax Error: In rule ImplementationDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                    System.exit(0);
                }
                if(observer != null){
                    observer.parser.setCurrentRule("implementationDeclaration", true);
                }
                t = CScanner.peekNextToken(f);
                if(observer != null){
                    observer.parser.setCurrentToken(t);
                }
            } //{ FunctionDefinitoin }

            if (!t.token.equals("}")) {
                System.err.format("Syntax Error: In rule ImplementationDeclaration unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);
            }
            if(observer != null){
                observer.parser.setCurrentRule("storageDeclaration",true);
            }
            return true;
        } //( implementation Identifier {{FunctionDefinition} } ) 
        else if (functionDefinition(f)) {
            if(observer != null){
                observer.parser.setCurrentRule("implementationDeclaration",false);
            }
            return true;
        }
        if(observer != null){
            observer.parser.setCurrentRule("implementationDeclaration",false);
        }
        return false;
    }

    public static boolean program(FileInputStream f){
//   Program := {InterfaceDeclaration} MainDeclaration {StorageDeclaration}{ImplementationDeclaration}   
        
        if(observer != null){
            observer.parser.setCurrentRule("program",true);
        }
       
        CToken t = new CToken();
        t = CScanner.peekNextToken(f);

        String[] dataTypes = {"unsigned", "char","short", "int", "long", "float", "double", "instance"};   
        
        //InterfaceDeclaration (optional)
        while (!t.token.equals("void")) {
            if(!interfaceDeclaration(f)){
                System.err.format("Syntax Error: In rule Program unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);    
            }//Interface Declaration failed
            if(observer != null){
                observer.parser.setCurrentRule("program",true);
            }
            t = CScanner.peekNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
        }
             
        // MainDeclaration
        if (!mainDeclaration(f)) {
            System.err.format("Syntax Error: In rule Program unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
            System.exit(0);     
        }
        if(observer != null){
            observer.parser.setCurrentRule("program",true);
        }
        t = CScanner.peekNextToken(f);
        if(observer != null){
            observer.parser.setCurrentToken(t);
        }

        while (t.token.equals("storage")) {
            if (!storageDeclaration(f)) {
                System.err.format("Syntax Error: In rule Program unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);    
            }
            if(observer != null){
                observer.parser.setCurrentRule("program",true);
            }
            t = CScanner.peekNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
        } //Storage Declaration

        while (!t.type.equals("None")) {
            if (!implementationDeclaration(f)) {
                System.err.format("Syntax Error: In rule Program unexpected token \"%s\" of type %s on line %d.\n", t.token, t.type, t.lineNum);
                System.exit(0);    
            }
            if(observer != null){
                observer.parser.setCurrentRule("program",true);
            }
            t = CScanner.peekNextToken(f);
            if(observer != null){
                observer.parser.setCurrentToken(t);
            }
        } //Implementation Declaration
        
        if(observer != null){
            observer.parser.setCurrentRule("program",false);
        }
        
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
            
            if(args.length == 2 && args[1].equals("Translator")){
                observer = new CTranslator();
            }
            
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



