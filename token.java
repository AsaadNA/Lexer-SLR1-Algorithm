enum TOKEN_TYPE {
   RO,OO,AO,
   ID,SL;
}

public class token {
   
   public TOKEN_TYPE type;
   public String data;
   
   public token() {}
   public token(TOKEN_TYPE type , String data) {
      this.type = type;
      this.data = data;
   }

   public void print() {
      System.out.println("Type: " + type + " | data: " + data);
   }
}