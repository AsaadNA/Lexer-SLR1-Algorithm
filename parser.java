import java.util.Stack;
import java.util.ArrayList;

public class parser {

   private String inputBuffer;
   private InputSource inputSource;
   private Stack<Character> inputStack;

   private parsetable parseTable = new parsetable();
   private ArrayList<String> productionRules = new ArrayList<String>();


   public parser(String inputBuffer) {

      //Initializing our input buffer stuff...
      this.inputBuffer = inputBuffer;
      inputSource = new InputSource(this.inputBuffer,false);
      inputSource.nextChar();

      //Inserting our Grammer's production rules...
      productionRules.add("S->AA");
      productionRules.add("A->aA");
      productionRules.add("A->b");

      //Initiialzing Our Parse table...
      parseTable.put(new Item('0','a',"s3"));
      parseTable.put(new Item('2','a',"s3"));
      parseTable.put(new Item('3','a',"s3"));
      parseTable.put(new Item('4','a',"r3"));
      parseTable.put(new Item('5','a',"r1"));
      parseTable.put(new Item('6','a',"r2"));
      
      parseTable.put(new Item('0','b',"s4"));
      parseTable.put(new Item('2','b',"s4"));
      parseTable.put(new Item('3','b',"s4"));
      parseTable.put(new Item('4','b',"r3"));
      parseTable.put(new Item('5','b',"r1"));
      parseTable.put(new Item('6','b',"r2"));

      parseTable.put(new Item('1','$',"accept"));
      parseTable.put(new Item('4','$',"r3"));
      parseTable.put(new Item('5','$',"r1"));
      parseTable.put(new Item('6','$',"r2"));
      
      parseTable.put(new Item('0','A',"2"));
      parseTable.put(new Item('2','A',"5"));
      parseTable.put(new Item('3','A',"6"));
         
      parseTable.put(new Item('0','S',"1"));

      //Initializing our input stack;
      inputStack = new Stack<>();
      inputStack.push('0');
   }
 
   private String getLHS(int productionRuleNumber) {
      String s = productionRules.get(productionRuleNumber-1);
      int iofs = s.indexOf("->");
      return s.substring(iofs + 2);
   }

   private int getLHSL(int productionRuleNumber) {
      String result = getLHS(productionRuleNumber);
      return result.length();
   }

   private char getRHS(int productionRuleNumber) {
      return productionRules.get(productionRuleNumber-1).charAt(0);
   }

   public boolean parse() {

      while(true) {
         
         char   state = inputStack.peek();
         char   input = inputSource.getCurrChar();
         String data  = parseTable.get(state,input);

         if(data == null) { return false; }
         else if(data.equals("accept")) { return true; }
         else if(data.charAt(0) == 'r') {
            
            char _stateNumber = data.charAt(1);
            int popCap = getLHSL(Character.getNumericValue(_stateNumber)) * 2;

            for(int i = 1; i <= popCap; i++) { inputStack.pop(); } //Pop from stack according to Length of L.H.S
            inputStack.push(getRHS(Character.getNumericValue(_stateNumber))); //Now get the R.H.S From the Rule And Push to stack
            char peekBelow = inputStack.get(inputStack.size()-2); //this will peek below top , to get the state number

            String theData = parseTable.get(peekBelow,inputStack.peek()); //get the state the variable should now go to
            if(Character.isDigit(theData.charAt(0))) { //if it's a digit then it's a state then it should be pushed in stack
               inputStack.push(theData.charAt(0));
            }

            System.out.println("REDUCE " + data + " " + inputStack);

         } else if(data.charAt(0) == 's') {
            char stateNumber = data.charAt(1);
            inputStack.push(inputSource.getCurrChar());
            inputStack.push(stateNumber);
            inputSource.nextChar();
            System.out.println("SHIFT " + data + " " + inputStack);
         }
      }
   }
}