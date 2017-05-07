
import java.util.*;

public class ObservableParser extends Observable {//    Based on pages 228 and 229 in "Programming Languages, Principles and Practice"
    
    public static CToken currentToken = null;
    public static String currentRule = null;
    public static boolean ruleStatus;
    
    public static CToken getCurrentToken(){
        
        return currentToken;
    }
    
    public void setCurrentToken(CToken t){
        this.currentToken = t;
        setChanged();
        notifyObservers();
    }
    
    public static String getCurrentRule(){
        return currentRule;
    }
    
    public void setCurrentRule(String rule, boolean status){
        //notifies CTranslator if a rule is being entered (status = true) or
        // if a rule is being exited (status = false)
        this.currentRule = rule;
        this.ruleStatus = status;
        setChanged();
        notifyObservers();
        
    }
      
    public static boolean getRuleStatus(){
        return ruleStatus;
    }
} 
