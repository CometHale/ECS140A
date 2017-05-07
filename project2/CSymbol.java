public class CSymbol{
    public String type;
    public String protectionLevel; //private,public
    public String memberType;//function, data
    public String groupType; //Implementation, Interface, Storage,Data
    public String value;
    public String scope;
    public int lineNum;
    boolean complete;
    
    
    public CSymbol(){
        type = "";
        protectionLevel = "";
        memberType = "";
        groupType = "";
        value = "";
        scope = "";
        lineNum = 0;
        complete = false;
    }
    
}
