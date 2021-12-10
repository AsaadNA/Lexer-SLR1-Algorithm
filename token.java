enum TOKEN_TYPE {
   RO,OO,AO,
   ID,SL,IN,
   INT,CHAR,STRING,IF,ELSE,DO,WHILE;
}

public class token {
   
   public TOKEN_TYPE type;
   public String data;

   public int lineNumber = -1;
   
   public token() {}
   public token(String data,int lineNumber) { this.data = data; this.lineNumber = lineNumber;}
   public token(TOKEN_TYPE type , String data) {
      this.type = type;
      this.data = data;
   }

   public void print() {
      System.out.println("Type: " + type + " | data: " + data);
   }
}