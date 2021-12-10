import java.util.ArrayList; // import the ArrayList class

public class lex {

   private String inputProgram = null;
   private ArrayList <token> tokens = new ArrayList<token>();

   public lex(String pathToFile) {
      inputProgram = utils.readfile(pathToFile);
   }

   private boolean popToken() {
      try {
         tokens.remove(tokens.size()-1);
      } catch (Exception e) {
         return false;
      } return true;
   }

   private void pushToken(token ct , token pt) {
      pt.type = ct.type; pt.data = ct.data; //setting up prev token
      tokens.add(new token(ct.type,ct.data)); //pushing to tokens list

      //resetting the currentToken
      ct.type = TOKEN_TYPE.WHITESPACE;
      ct.data = "";
   }

   //string -> keyword
   //x -> identifier

   public void parse() {
      token ct = new token(TOKEN_TYPE.WHITESPACE,""); //current token
      token pt = new token(TOKEN_TYPE.WHITESPACE,""); //prev token
      for(char currChar : inputProgram.toCharArray()) {
         switch(currChar) {

            case '=': {

               //TODO: This is here error handling for more then one assignments or eq
               if(pt.type == TOKEN_TYPE.ASSIGNMENT || (pt.type == TOKEN_TYPE.ASSIGNMENT && pt.type == TOKEN_TYPE.EQ)) { 
                  System.out.println("Unknow lexme: " + pt.data);
               } else if(pt.type == TOKEN_TYPE.EQ && ct.type == TOKEN_TYPE.WHITESPACE) {
                  ct.type = TOKEN_TYPE.ASSIGNMENT;
                  ct.data += currChar;
                  ct.data += currChar;
                  popToken(); //this will pop if the EQ is pushed because now = has become ==
                  pushToken(ct,pt);
               } else if(ct.type != TOKEN_TYPE.EQ) {
                  ct.type = TOKEN_TYPE.EQ;
                  ct.data += currChar;
                  pushToken(ct,pt);
               }
            } break;

            case ';': {
               ct.type = TOKEN_TYPE.TERMINATOR;
               ct.data += currChar; 
               pushToken(ct,pt);
            } break;

            case '"': {
               if(ct.type != TOKEN_TYPE.STRING_LITERAL) {
                  ct.type = TOKEN_TYPE.STRING_LITERAL;
               } else if(ct.type == TOKEN_TYPE.STRING_LITERAL) {
                 pushToken(ct,pt);
               }
            } break;

            default: {
               if(ct.type == TOKEN_TYPE.STRING_LITERAL) {
                  ct.data += currChar;
               }
            }
         }
      }
   }

   public void printTokens() {
    for(token t : tokens) t.print();
   }
}