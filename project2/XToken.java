/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xlanguageparser;

/**
 *
 * @author cheesy
 */
public class XToken {
    
    enum TokenType { XOperator, intConstant, floatConstant, XKeywords, XIdentifiers };
    TokenType type;
    
    int line_num;
    String token;
    
}//Class to gather info about a particular kind of token
// enums restrict the possible values of an attribute to the predefined list
