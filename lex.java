import java.util.ArrayList; // import the ArrayList class

public class lex {

   private InputSource inputProgram = null;
   private ArrayList <token> tokens = new ArrayList<token>();

   public lex(String pathToFile) {
      inputProgram = new InputSource(utils.readfile(pathToFile));
   }

   private boolean isDigit(char c) { return (c >= '0' && c <= '9'); }
   private boolean isAlphabet(char c) {return ( (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));}

   private token getDigits() {
      int state = 0;
      String theInt = "";
      while(!inputProgram.isEOF()) {
         switch(state) {
            case 0: {
               char c = inputProgram.getCurrChar(); theInt += c;
               if(isDigit(c)) state = 1;
               else return null;
            } break;
            
            case 1: {
               char c = inputProgram.nextChar();
               if(isDigit(c)) { theInt += c; state = 1; } //if it's a digit then append to the resultant string otherwise not
               else state = 2;
            } break;

            case 2: { 
               inputProgram.retract(); 
               return new token(TOKEN_TYPE.IN,theInt);
            }

            default: { return null; }
         }
      } return null;
   }

   //This handles String and Identifiers in a single transition state
   //When it returns then we differentiate the identifiers from the keywords..
   private token getStringAndIdentifier() {
      int state = 0;
      String theString = ""; //to store string
      while(!inputProgram.isEOF()) {
         switch(state) {
            case 0: {
               char c = inputProgram.getCurrChar(); theString += c; //to store string
               if(c == '"') state = 1;
               else if(isAlphabet(c)) state = 4;
               else return null;
            } break;

            case 1: {
               char c = inputProgram.nextChar(); theString += c; //to store string
               if(isAlphabet(c)) state = 2;
               else return null;
            } break;

            case 2: {
               char c = inputProgram.nextChar(); theString += c; //to store string
               if(isAlphabet(c) || isDigit(c)) state = 2;
               else if(c == '"') state = 3;
               else return null;
            } break;

            case 3: { return new token(TOKEN_TYPE.SL,theString); }

            case 4: {
               char c = inputProgram.nextChar(); 
               if(isAlphabet(c) || isDigit(c) && c != ';') { theString += c; state = 4; } //; is added if in the end , so we only append if ; is not there
               else state = 5;
            } break;

            case 5: { inputProgram.retract(); return new token(TOKEN_TYPE.ID,theString); }
            
            default: { return null; }
         }
      } return null;
   }

   //This handles all the operators in a single transition state...
   private token getOperator() {
      int state = 0;
      while(!inputProgram.isEOF()) {
         switch(state) {
            case 0: {
               char c = inputProgram.getCurrChar();
               if(c == '<') state = 1;
               else if(c == '=') state = 5;
               else if(c == '>') state = 8;
               else if(c == ')') state = 11;
               else if(c == '(') state = 12;
               else if(c == '{') state = 13;
               else if(c == '}') state = 14;
               else if(c == ';') state = 15;
               else if(c == '+') state = 16;
               else if(c == '-') state = 17;
               else if(c == '/') state = 18;
               else if(c == '*') state = 19;
            } break;

            case 1: {
               char c = inputProgram.nextChar();
               if(c == '=') state = 2;
               else if(c == '>') state = 3;
               else state = 4;
            } break;

            case 2: { return new token(TOKEN_TYPE.RO,"LE"); }
            case 3: { return new token(TOKEN_TYPE.RO,"NE"); }

            case 4: {
               inputProgram.retract();
               return new token(TOKEN_TYPE.RO,"LT");
            }

            case 5: {
               char c = inputProgram.nextChar();
               if(c == '=') state = 6;
               else state = 7;
            } break;

            case 6: {  return new token(TOKEN_TYPE.RO,"EQ"); }

            case 7: {
               inputProgram.retract();
               return new token(TOKEN_TYPE.OO,"AS");
            }

            case 8: {
               char c = inputProgram.nextChar();
               if(c == '=') state = 9;
               else state = 10;
            } break;

            case 9: { return new token(TOKEN_TYPE.RO,"GE"); }

            case 10: {
               inputProgram.retract();
               return new token(TOKEN_TYPE.RO,"GT");
            }

            case 11: { return new token(TOKEN_TYPE.OO,"CP"); }
            case 12: { return new token(TOKEN_TYPE.OO,"OP"); }
            case 13: { return new token(TOKEN_TYPE.OO,"OB"); }
            case 14: { return new token(TOKEN_TYPE.OO,"CB"); }
            case 15: { return new token(TOKEN_TYPE.OO,"TR"); }

            case 16: { return new token(TOKEN_TYPE.AO,"AD"); }
            case 17: { return new token(TOKEN_TYPE.AO,"SB"); }
            case 18: { return new token(TOKEN_TYPE.AO,"DV"); }
            case 19: { return new token(TOKEN_TYPE.AO,"ML"); }

            default: { return null; }
         }
      } return null;
   }

   public void parse() {
      token ct = new token();
      while(!inputProgram.isEOF()) {
         switch(inputProgram.nextChar()) {
            case '<':
            case '=':
            case '>':
            case '+':
            case '-':
            case '*':
            case '/':
            case '(':
            case ')':
            case '{':
            case '}':
            case ';': {
               ct = getOperator();
               if(ct != null) tokens.add(ct);
            } break;

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
               ct = getDigits();
               if(ct != null) tokens.add(ct);
            } break;

            default: {
               ct = getStringAndIdentifier();
               if(ct != null) {
                  if(ct.type == TOKEN_TYPE.ID) { //identifying keywords from identifiers...
                     if(ct.data.equals("int") || ct.data.equals("int ")) {ct.data = "0"; ct.type = TOKEN_TYPE.INT; }
                     if(ct.data.equals("char") || ct.data.equals("char ")) {ct.data = "1"; ct.type = TOKEN_TYPE.CHAR; }
                     if(ct.data.equals("string") || ct.data.equals("string ")) {ct.data = "2"; ct.type = TOKEN_TYPE.STRING; }
                     if(ct.data.equals("if") || ct.data.equals("if ")) {ct.data = "3"; ct.type = TOKEN_TYPE.IF; }
                     if(ct.data.equals("else") || ct.data.equals("else ")) {ct.data = "4"; ct.type = TOKEN_TYPE.ELSE; }
                     if(ct.data.equals("do") || ct.data.equals("do ")) {ct.data = "5"; ct.type = TOKEN_TYPE.DO; }
                     if(ct.data.equals("while") || ct.data.equals("while ")) {ct.data = "6"; ct.type = TOKEN_TYPE.WHILE; }
                  } tokens.add(ct);
               }
            } break;
         }
      }
   }

   public void printTokens() {
     for(token t : tokens) t.print();
   }
}