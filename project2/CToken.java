public class CToken {
  public String token;
  public String type;
  public int lineNum;
  public int colNum;

  public CToken() {
    token = "";
    type = "";
    lineNum = 0;
    colNum = 0;
  }

  public CToken(String t, String ty, int l, int c) {
    token = t;
    type = ty;
    lineNum = l;
    colNum = c;
  }
}