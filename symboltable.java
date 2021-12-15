import java.util.ArrayList;

public class symboltable {
   
   private ArrayList<token> table = new ArrayList<token>();
   private int attribCounter = 0;

   public symboltable() {
      push(TOKEN_NAME.INT,"-");
      push(TOKEN_NAME.CHAR,"-");
      push(TOKEN_NAME.STRING,"-");
      push(TOKEN_NAME.IF,"-");
      push(TOKEN_NAME.ELSE,"-");
      push(TOKEN_NAME.DO,"-");
      push(TOKEN_NAME.WHILE,"-");
   }

   public int getCurrentAttribCounter() { return this.attribCounter-1; }

   public void push(TOKEN_NAME tokenName , String lexeme) {
      table.add(new token(tokenName,lexeme,this.attribCounter));
      this.attribCounter += 1;
   }

   public void print() {
      System.out.println("\n====== SYMBOL TABLE ======\n");
      for(token t : table) {
         System.out.println(t.attribCounter + "\t" + t.tokenName + "\t" + "-" + "\t" + t.lexeme);
      }
   }
}