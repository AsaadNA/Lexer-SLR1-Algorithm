import java.util.Stack;
import java.util.ArrayList;

public class parser {

   private lex l = null;
   private LexSource lexSource;
   private ArrayList<token> tokens;
   private Stack<String> inputStack;

   private parsetable parseTable = new parsetable();
   private ArrayList<String> productionRules = new ArrayList<String>();

   public parser(ArrayList<token> tokens) {

      this.tokens = tokens;
      this.tokens.add(new token(TOKEN_NAME.EOF,"$",null)); //add EOI Sentinel

      lexSource = new LexSource(this.tokens);
      lexSource.nextToken();

      //Inserting our Grammer's productionr rules
      productionRules.add("E->E+T");
      productionRules.add("E->T");
      productionRules.add("T->T*F");
      productionRules.add("T->F");
      productionRules.add("F->(E)");
      productionRules.add("F->id");

      //Initializing our parsing table
      parseTable.put(new Item("0","id","s5"));
      parseTable.put(new Item("4","id","s5"));
      parseTable.put(new Item("6","id","s5"));
      parseTable.put(new Item("7","id","s5"));

      parseTable.put(new Item("1","+","s6"));
      parseTable.put(new Item("2","+","r2"));
      parseTable.put(new Item("3","+","r4"));
      parseTable.put(new Item("5","+","r6"));
      parseTable.put(new Item("8","+","s6"));
      parseTable.put(new Item("9","+","r1"));
      parseTable.put(new Item("10","+","r3"));
      parseTable.put(new Item("11","+","r5"));

      parseTable.put(new Item("2","*","s7"));
      parseTable.put(new Item("3","*","r4"));
      parseTable.put(new Item("5","*","r6"));
      parseTable.put(new Item("9","*","s7"));
      parseTable.put(new Item("10","*","r3"));
      parseTable.put(new Item("11","*","r5"));

      parseTable.put(new Item("0","(","s4"));
      parseTable.put(new Item("4","(","s4"));
      parseTable.put(new Item("6","(","s4"));
      parseTable.put(new Item("7","(","s4"));

      parseTable.put(new Item("2",")","r2"));
      parseTable.put(new Item("3",")","r4"));
      parseTable.put(new Item("5",")","r6"));
      parseTable.put(new Item("8",")","s11"));
      parseTable.put(new Item("9",")","r1"));
      parseTable.put(new Item("10",")","r3"));
      parseTable.put(new Item("11",")","r5"));

      parseTable.put(new Item("1","$","accept"));
      parseTable.put(new Item("2","$","r2"));
      parseTable.put(new Item("3","$","r4"));
      parseTable.put(new Item("5","$","r6"));
      parseTable.put(new Item("9","$","r1"));
      parseTable.put(new Item("10","$","r3"));
      parseTable.put(new Item("11","$","r5"));

      parseTable.put(new Item("0","E","1"));
      parseTable.put(new Item("4","E","8"));

      parseTable.put(new Item("0","T","2"));
      parseTable.put(new Item("4","T","2"));
      parseTable.put(new Item("6","T","9"));

      parseTable.put(new Item("0","F","3"));
      parseTable.put(new Item("4","F","3"));
      parseTable.put(new Item("6","F","3"));
      parseTable.put(new Item("7","F","10"));

      //Initializing input stack...
      inputStack = new Stack<>();
      inputStack.push("0");
   }

   private String getLHS(int productionRuleNumber) {
      String result = productionRules.get(productionRuleNumber-1);
      int IndexOfSeperator = result.indexOf("->");
      return result.substring(IndexOfSeperator + 2);
   }

   private int getLHSL(int productionRuleNumber) {
      String result = getLHS(productionRuleNumber);
      if(result.equals("id")) return 1; //because ID is a single thing
      return result.length();
   }

   private String getRHS(int productionRuleNumber) {
      String result = "";
      char c = productionRules.get(productionRuleNumber-1).charAt(0);
      result += c;
      return result;
   }

   public boolean compile() {
      System.out.println("\n==== PARSER TABLE STACK TRACE ==== \n");
      int stepsCounter = 0;
      boolean isS = false;
      while(true) {

         String state = inputStack.peek();
         String input = lexSource.getCurrentToken();
         String data = parseTable.get(state,input);

         if(data == null) {
            
            {
               if(isS) System.out.println(""); //printing thing
               else System.out.println("");
            }

            if(input.equals("$")) { System.out.println("Invalid Expression Due To: Unexpected End Of Input");  }
            else {System.out.println("Invalid Expression Due To: " + lexSource.getCurrentTokenLexeme()); }
            return false; 
         }
         
         else if(data.equals("accept")) { return true; }
         else if(data.charAt(0) == 'r') {

            isS = false; //printing thing
            stepsCounter++;
            
            char stateNumber = data.charAt(1); //Assuming we have number of rules not exceeding double digits...
            int popCap = getLHSL(Character.getNumericValue(stateNumber)) * 2;

            for(int i = 1; i <= popCap; i++) { inputStack.pop(); } //Pop from stack according to Length of L.H.S...
            inputStack.push(getRHS(Character.getNumericValue(stateNumber))); //Now get the R.H.S From the Rule And Push to stack...
            String peekBelow = inputStack.get(inputStack.size()-2); //this will peek below top , to get the state number...

            String theData = parseTable.get(peekBelow,inputStack.peek()); //get the state the variable , we should now go to
            if(theData.length() == 2) { //if goto has double digit state .. extract both
               String r = "";
               r += theData.charAt(0);
               r += theData.charAt(1);
               inputStack.push(r);
            }  else {
               inputStack.push(utils.CharToString(theData.charAt(0))); //if goto has single digit state
            }

            System.out.println(stepsCounter + ". REDUCE " + data + " " + inputStack + " --> " + lexSource.getCurrentToken());

         } else if(data.charAt(0) == 's') {
            isS = true; //printing thing
            stepsCounter++;
            String stateNumber = "";
            if(data.length() == 3) { //here we extract the digits so if we have a double digit state we do this ... i know it's hacky :p 
               stateNumber += data.charAt(1);
               stateNumber += data.charAt(2);
            } else {
               stateNumber += data.charAt(1);
            }

            inputStack.push(lexSource.getCurrentToken());
            inputStack.push(stateNumber);
            System.out.println(stepsCounter + ". SHIFT " + data + " " + inputStack + " --> " + lexSource.getCurrentToken());
            lexSource.nextToken();      
         }         
      }
   }
}