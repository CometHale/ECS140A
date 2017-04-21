/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package xlanguageparser;
import java.io.File; // Needed for XLanguageTokenizer
import java.io.FileInputStream; // Needed for XLanguageTokenizer
import java.io.IOException;// Needed for XLanguageTokenizer
import java.lang.*;// Needed for XLanguageTokenizer
import java.util.*;
/**
 *
 * @author Haley Sanders-Turner & Sally Ly 
 */

//for Scope: have a class for a Function token (probably not in this file)
//each Function will have a hash table to keep track of the variables that are in scope
//for it.
//Or make a separate list of hash tables with one hash table for each function


class XToken{
    
    enum TokenType { XOperator, intConstant, floatConstant, XKeywords, XIdentifiers };
    
    TokenType type;
    int line_num; // need to figure out how to tell what line
    int char_position;
    String token;
    
    public XToken(){
        line_num = -1;
        token = "";
    }
    
}//Class to gather info about a particular kind of token
// enums restrict the possible values of an attribute to the predefined list

class XLanguageTokenizer {
    
    File languageFile;
    FileInputStream fis;
    Vector token_vector;
    
    
    public XLanguageTokenizer(String languageFileName) {
        
        languageFile = new File(languageFileName);
        token_vector = new Vector();
        
        try{
            fis = new FileInputStream(languageFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
                
    }
    
    public String GetNextToken(){
        
        try{
            
            int fileByte = 0;
            String tokenString = "";
            
            while( fileByte != -1 && !Character.isWhitespace((char)fileByte) ){
                
                fileByte = fis.read();
                tokenString = tokenString + (char)fileByte;   
            }
            
            XToken newToken = new XToken();
            newToken.token = tokenString;
//            newToken.line_num
//            newToken.char_position
            System.out.println(tokenString);
            return tokenString;
            
        } catch(IOException e) {
            e.printStackTrace();
        } 
        
        return "";
    }

    public void PeekNextToken(){
        
//        try{
//            
//            int fileByte = 0;
//            String tokenString = "";
//            
//            while( fileByte != -1 && !Character.isWhitespace((char)fileByte) ){
//                
//                fileByte = fis.read();
//                tokenString = tokenString + (char)fileByte;   
//            }
//            
//            XToken newToken = new XToken();
//            newToken.token = tokenString;
////            newToken.line_num
////            newToken.char_position
//            System.out.println(tokenString);
//            
//        } catch(IOException e) {
//            e.printStackTrace();
//        }  

    }
    
}

public class XLanguageParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String fileName = args[0];
        XLanguageTokenizer tokenizer = new XLanguageTokenizer(fileName);
        
        
    }
    
}
