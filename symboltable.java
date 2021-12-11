import java.util.ArrayList;

public class symboltable {

   private ArrayList <token> tokens = new ArrayList<token>();
   private ArrayList <sToken> theTable = new ArrayList<sToken>();
   
   public symboltable(ArrayList<token> tokens) {
      this.tokens = tokens;
   }

   //we need to make sure that we generate the table only
   //one time and not call this method again and again
   public void generateTable() {
      if(theTable.size() == 0) {

         //initializing it with keywords
         theTable.add(new sToken(0,"int","-","-"));
         theTable.add(new sToken(1,"char","-","-"));
         theTable.add(new sToken(2,"string","-","-"));
         theTable.add(new sToken(3,"if","-","-"));
         theTable.add(new sToken(4,"else","-","-"));
         theTable.add(new sToken(5,"do","-","-"));
         theTable.add(new sToken(6,"while","-","-"));

         int startingCounter = 7;
         for(token t : tokens) {
            if(t.type == TOKEN_TYPE.ID || t.type == TOKEN_TYPE.SL || t.type == TOKEN_TYPE.IN) {
               theTable.add(new sToken(startingCounter,t.type.toString(),"-",t.data));
               startingCounter++;
            }
         }

      } else {
         System.out.println("ERROR: Symbol table already has been generated for tokens !");
      }
   }

   public void print() {
      System.out.println("\n\t=== SYMBOL TABLE ===\n");
      for(sToken t : theTable) t.print();
   }
}