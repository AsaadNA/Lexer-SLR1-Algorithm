import java.util.ArrayList;

public class lex {

   private int lineNumber = 0;
   private InputSource inputProgram = null;
   
   private ArrayList <token> tokens = new ArrayList<token>();
   private ArrayList <token> invalidTokens = new ArrayList<token>();
   private ArrayList <token> singlelineComments = new ArrayList<token>();
   private ArrayList <token> multilineComments = new ArrayList<token>();

   private symboltable symbolTable = new symboltable();

   public lex(String data,boolean isFile) {
      inputProgram = isFile ? new InputSource(utils.readfile(data),true) : new InputSource(data,true);
   }

   private boolean isDigit(char c) { return (c >= '0' && c <= '9'); }
   private boolean isAlphabet(char c) {return ( (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));}

   //This handles digits only...
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
               symbolTable.push(TOKEN_NAME.IN,theInt);
               return new token(TOKEN_NAME.IN,theInt,symbolTable.getCurrentAttribCounter());
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
               char c = inputProgram.getCurrChar();  //to store string
               if(c == '"') { state = 1; } //this means it's a string literal 
               else if(isAlphabet(c)) { theString += c; state = 4; } //this means it's a identifier
               else return null;
            } break;

            case 1: {
               char c = inputProgram.nextChar(); theString += c; //to store string
               state = 2; //it does not matter what comes in the " " so we move forward
            } break;

            case 2: {
               char c = inputProgram.nextChar();  //to store string
               if(c == '"') { state = 3; }
               else { theString += c; state = 2; } 
            } break;

            case 3: {
               symbolTable.push(TOKEN_NAME.SL,theString); //pushing to symbol table
               return new token(TOKEN_NAME.SL,theString,symbolTable.getCurrentAttribCounter());  
            }

            case 4: {
               char c = inputProgram.nextChar(); 
               if(isAlphabet(c) || isDigit(c) && c != ';') { theString += c; state = 4; } //; is added if in the end , so we only append if ; is not there
               else state = 5;
            } break;

            //NOTE: Identifier token cannot be inserted in the symbol table here
            //       we need to identify keyword etc so we are gonna do that further down                     
            case 5: { inputProgram.retract(); return new token(TOKEN_NAME.ID,theString,"No attrib"); }
            
            default: { return null; }
         }
      } return null;
   }

   //This handles all the operators and comments in a single transition state...
   private token getOperatorAndComments() {
      int state = 0;
      String comment = "";
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
               else if(c == '*') state = 18;
               else if(c == '/') state = 19;
            } break;

            case 1: {
               char c = inputProgram.nextChar();
               if(c == '=') state = 2;
               else if(c == '>') state = 3;
               else state = 4;
            } break;

            case 2: { return new token(TOKEN_NAME.RO,"<=","LE"); }
            case 3: { return new token(TOKEN_NAME.RO,"<>","NE"); }

            case 4: {
               inputProgram.retract();
               return new token(TOKEN_NAME.RO,"<","LT");
            }

            case 5: {
               char c = inputProgram.nextChar();
               if(c == '=') state = 6;
               else state = 7;
            } break;

            case 6: {  return new token(TOKEN_NAME.RO,"==","EQ"); }

            case 7: {
               inputProgram.retract();
               return new token(TOKEN_NAME.OO,"=","AS");
            }

            case 8: {
               char c = inputProgram.nextChar();
               if(c == '=') state = 9;
               else state = 10;
            } break;

            case 9: { return new token(TOKEN_NAME.RO,">=","GE"); }

            case 10: {
               inputProgram.retract();
               return new token(TOKEN_NAME.RO,">","GT");
            }

            case 11: { return new token(TOKEN_NAME.OO,")","CP"); }
            case 12: { return new token(TOKEN_NAME.OO,"(","OP"); }
            case 13: { return new token(TOKEN_NAME.OO,"{","OB"); }
            case 14: { return new token(TOKEN_NAME.OO,"}","CB"); }
            case 15: { return new token(TOKEN_NAME.OO,";","TR"); }
            case 16: { return new token(TOKEN_NAME.AO,"+","AD"); }
            case 17: { return new token(TOKEN_NAME.AO,"-","SB"); }
            case 18: { return new token(TOKEN_NAME.AO,"*","ML"); }
            
            case 19: {
               char c = inputProgram.nextChar();
               if(c == '/') state = 20;
               else if(c == '*') state = 24;
               else state = 23;
            } break;

            case 20: {
               char c = inputProgram.nextChar(); comment += c;
               state = 21;
            } break;

            case 21: {
               char c = inputProgram.nextChar(); comment += c;
               if(c == '\n') { state = 22; } //when a newline occured then we know comment is finished
               else {
                  state = 21;
                  if(inputProgram.isEOF()) { 
                      inputProgram.retract();
                      comment = comment.replace("\n", "").replace("\r", ""); //remove \n from comment
                      singlelineComments.add(new token(comment,lineNumber)); comment = ""; //Add a new comment
                      return null;
                  }
               }
            } break;

            case 22: {
               inputProgram.retract();
               comment = comment.replace("\n", "").replace("\r", ""); //remove \n from comment
               singlelineComments.add(new token(comment,lineNumber)); comment = ""; //Add a new comment
               return null;
            }

            /* / */

            case 23: {
               inputProgram.retract();
               return new token(TOKEN_NAME.AO,"/","DV");
            }

            /* MULIT LINE */

            case 24: {
               char c = inputProgram.nextChar(); comment += c;
               state = 25;
            } break;

            case 25: {
               char c = inputProgram.nextChar();
               if(c == '*' && inputProgram.nextChar() == '/') { state = 26; }
               else { comment += c; state = 25; }
            } break;

            case 26: {
               multilineComments.add(new token(comment,lineNumber)); comment = ""; //add a new multiline comment
               return null;
            }

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
               ct = getOperatorAndComments();
               if(ct != null) tokens.add(ct); //pushing operators
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
               if(ct != null) tokens.add(ct); //pusing numbers
            } break;

            //Handling some whitesapces (BLANKS , TABS , NEWLINES)...
            case ' ':
            case '\t':
               break;
            case '\n':
               lineNumber += 1;
               break;

            default: {
               char c = inputProgram.getCurrChar();
               if(isAlphabet(c) || c == '"') { //handles identifiers and also string literals
               ct = getStringAndIdentifier();
                  if(ct != null) {
                     if(ct.tokenName == TOKEN_NAME.ID) { //identifying keywords from identifiers...
                        if(ct.lexeme.equals("int") || ct.lexeme.equals("int ")) {ct.attribValue = "0"; ct.tokenName = TOKEN_NAME.INT; }
                        else if(ct.lexeme.equals("char") || ct.lexeme.equals("char ")) {ct.attribValue = "1"; ct.tokenName = TOKEN_NAME.CHAR; }
                        else if(ct.lexeme.equals("string") || ct.lexeme.equals("string ")) {ct.attribValue = "2"; ct.tokenName = TOKEN_NAME.STRING; }
                        else if(ct.lexeme.equals("if") || ct.lexeme.equals("if ")) {ct.attribValue = "3"; ct.tokenName = TOKEN_NAME.IF; }
                        else if(ct.lexeme.equals("else") || ct.lexeme.equals("else ")) {ct.attribValue = "4"; ct.tokenName = TOKEN_NAME.ELSE; }
                        else if(ct.lexeme.equals("do") || ct.lexeme.equals("do ")) {ct.attribValue = "5"; ct.tokenName = TOKEN_NAME.DO; }
                        else if(ct.lexeme.equals("while") || ct.lexeme.equals("while ")) {ct.attribValue = "6"; ct.tokenName = TOKEN_NAME.WHILE; }
                        else { //we do this here so we do not put keyword in symbol table ... since already pushed
                           symbolTable.push(ct.tokenName,ct.lexeme);
                           ct.attribCounter = symbolTable.getCurrentAttribCounter();
                        }
                     } tokens.add(ct); //pushing identifiers and string literals
                  } 
               } else { String s = "" + c; invalidTokens.add(new token(s,lineNumber)); } //Error handling invalid lexmes with line number...
            } break;
         }
      }
   }

   //This will print all the tokens parsed...
   public void printTokens() {

    //Print invalid lexemes if founded...
   if(invalidTokens.size() != 0) {
      System.out.println("\n=== INVALID LEXEMES FOUNDED (Kindly fix error for proper output)  ===\n");
      for(token t : invalidTokens) System.out.println("Invalid LEXEME FOUND @ Line " + t.lineNumber + " : " + t.lexeme);
   } else {
      
      System.out.println("\n====== PRINTING TOKENS ======\n");
      //Print tokens if founded...
      if(tokens.size() != 0) {
         for(token t : tokens) t.print();
      }
      //Print all comments if founded...
      if(singlelineComments.size() != 0) {
         System.out.println("\n=== SINGLE LINE COMMENTS FOUNDED ===\n");
         for(token t : singlelineComments) System.out.println("Found Comment @ Line " + t.lineNumber + " : " + t.lexeme);
      }     

      //Print all comments if founded...
      if(multilineComments.size() != 0) {
         System.out.println("\n=== MULTI LINE COMMENTS FOUNDED ===\n");
         for(token t : multilineComments) System.out.println("Found Comment @ Line " + t.lineNumber + " : " + t.lexeme);
      }
         symbolTable.print();
      }
   }
}