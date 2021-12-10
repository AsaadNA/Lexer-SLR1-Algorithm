enum TOKEN_TYPE {
   LT,LTE,
   EQ,NE,
   GT,GE,
   ADD,SUB,MUL,DIV,
   INT,CHAR,STRING,IF,ELSE,DO,WHILE,
   IDENTIFIER,STRING_LITERAL,INTEGER_LITERAL,
   ASSIGNMENT,O_PARAN,C_PARAN,O_BRACKET,C_BRACKET,
   TERMINATOR,WHITESPACE;
}

public class token {
   
   public TOKEN_TYPE type = TOKEN_TYPE.WHITESPACE;
   public String data = "";
   public int lineNumber;

   public token(TOKEN_TYPE type , String data) {
      this.type = type;
      this.data = data;
   }

   public void print() {
      System.out.println("Type: " + type + " | data: " + data);
   }
}