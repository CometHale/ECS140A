public class CSymbol{
    
    public String type;
    public String value;
    public String scope;
    public int lineNum;
    public boolean isFunction;// for named functions
    
    
    public CSymbol(){
        type = "";
//        protectionLevel = "";
//        memberType = "";
//        groupType = "";
        value = "";
        scope = "";
        lineNum = 0;
        isFunction = false;
    }
    
}
