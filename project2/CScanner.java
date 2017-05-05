import java.io.*;

public class CScanner {
  public static int lineNum;
  public static int colNum;
  public static char peek;
  public static boolean needToUsePeek;
  public static CToken prevToken;
  public static CToken peekedToken;
  public static boolean needToUsePeekedToken;

  public static boolean isOp(char c) {
    String[] ops = { "=", "==", "<", ">", "<=", ">=", "!=", "+", "-", "*", "/", "(", ")", "{", "}", ";", ".", ","};
    String s = Character.toString(c);

    for (int i = 0; i < ops.length; i++) {
      if (s.equals(ops[i])) {
        return true;
      }
    }
    return false;
  }

  public static CToken getNextToken(FileInputStream f) {
      String[] keywords = { "interface", "main", "void", "storage", "global", "implementation", "let", "while", "if", "return", "nil", "unsigned", "char",
                            "short", "int", "long", "float", "double", "instance", "self", "new" };
      
      char buffer = 'a';
      int readByte;
      String token = "";
      String type = "";
      int line = lineNum;
      int colStart = colNum;

      try {
        while (buffer != ' ') { 
          if (needToUsePeek) {
            buffer = peek;
            needToUsePeek = false;
            colNum++;
          }
          else {
            while (true) {
              readByte = f.read();
              colNum++; 
              buffer = (char)readByte; //ignore white space 1

              if (buffer == '\n' && token.length() == 0) {
                  lineNum++;
                  colNum = 1;
                  line = lineNum;
                  colStart = colNum;
                  break;
              } //newline character
              else if (token.length() == 0 && buffer == ' ') {
                colStart = colNum;
              }
              else if (buffer != ' ' || token.length() != 0) {
                break;
              }
            }  

            if (readByte == -1) {
              type = "None";
              token = "";
              break;
            } //end of file   
            
          }

          //starting tokens
          if (token.length() == 0 && buffer != ' ') {
            if (Character.isLetter(buffer) || buffer == '_') {
              token += buffer; //add onto string
              type = "Identifier";
            } //identifer; later, check if its a keyword
           else if (Character.isDigit(buffer) || buffer == '-') {
              if (buffer == '-' && !prevToken.type.equals("Identifier") ) {
                token += buffer;
                type = "IntConstant";
              } //if - and the previous token was not an identifer, then it is part of Digit/Floaat
              else if (buffer == '-' && prevToken.type.equals("Identifier") ){
                token += buffer;
                type = "Operator";
                break;
              } //it's an operator
              else if (buffer != '-') {
                token += buffer;
                type = "IntConstant";
              }
            } //token starts with number (default to int but hcange to float when need be)
            else if (!Character.isLetterOrDigit(buffer) && isOp(buffer)) {
              token += buffer;
              type = "Operator";
              if (buffer != '<' && buffer != '>' && buffer != '=' && buffer != '!') {
                break;
              } //these may have double
            } //an operator
            else if (!Character.isLetterOrDigit(buffer) && !isOp(buffer) && !Character.isWhitespace(buffer)){
              token += buffer;
              type = "Invalid";
              break;
            } //totally invalid
            continue;
          }
       
          //continuing tokens
          if (token.length() != 0 && buffer != ' ') {
            if (type.equals("Identifier") && (Character.isLetterOrDigit(buffer) || buffer == '_')) {
             token += buffer; //add onto string
            } //continue adding to token
            else if (type.equals("Identifier") && (!Character.isLetterOrDigit(buffer) || buffer != '_')) {
              peek = buffer;
              needToUsePeek = true;
              colNum--; //subract since we have not consumed the character yet
              break;
            } //not part of token   
            else if (type.equals("Operator") && (!Character.isLetterOrDigit(buffer) && isOp(buffer))) {
              token += buffer;
            } //is ">-", "<-", etcc
            else if(type.equals("Operator") && (Character.isLetterOrDigit(buffer) || buffer == '_')) {
              peek = buffer;
              needToUsePeek = true;  
              colNum--; //subract since we have not consumed the character yet
              break;
            } //not part of token
            else if (type.equals("IntConstant") && buffer == '.') {
              token += buffer;
              type = "FloatConstant";
            } // . makes int to float 
            else if ((type.equals("IntConstant") || type.equals("FloatConstant")) && Character.isDigit(buffer)) {
              token += buffer;
            }
            else if ((type.equals("IntConstant") || type.equals("FloatConstant")) && (Character.isLetter(buffer) || (buffer == '_'))) {
              token += buffer;
              type = "Invalid";
            }
            else if ((type.equals("IntConstant") || type.equals("FloatConstant")) && (!Character.isLetterOrDigit(buffer))) {
              peek = buffer;
              needToUsePeek = true;
              colNum--; //subract since we have not consumed the character yet
              break;
            } //operator after number ex. print(10)
            else if (buffer == '\n') {
              lineNum++;
              colNum = 1;
              break;
            } //newline character
          }
        }
      }
      catch (IOException e) {
        System.err.println("Could not read file.");
      }

      if (type.equals("Identifier")) {
        for (int i = 0; i < keywords.length; i++) {
          if (token.equals(keywords[i])) {
            type = "Keyword";
          }
        }
      } //check if keyword

      //create token
      CToken t = new CToken(token, type, line, colStart);
      prevToken = t;

      return t;
  }

  public static CToken peekNextToken(FileInputStream f) {
      peekedToken = getNextToken(f);
      needToUsePeekedToken = true;

      return peekedToken;
  }

  public static void main(String[] args) {
    lineNum = 1;
    colNum = 1;
    needToUsePeek = false;
    CToken t = new CToken();
    prevToken = t;

    if (args.length == 0) {
      System.exit(0);
    }

    File file = new File(args[0]);

    try {
      FileInputStream f = new FileInputStream(file);

      while (!t.type.equals("None")) {
        t = getNextToken(f);  
        System.out.format("@%4d,%4d%14s \"%s\"\n", t.lineNum, t.colNum, t.type, t.token);
      }
    }
    catch (FileNotFoundException e) {
      System.err.println("No file.");
    }  
  }
} 