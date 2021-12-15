import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;

class InputSource {
   private String source;
   private int counter = -1;
   public InputSource(String source , boolean isFile) {
      this.source = source;
      if(isFile) {if(this.source.charAt(source.length()-1) != ' ') { this.source += ' '; this.source += ' ';this.source += ' '; this.source += ' '; } }
   }

   public boolean isEOF() { return (counter == source.length()-1); }
   public void retract() { counter -= 1; }
   public char getCurrChar() { return source.charAt(counter); }
   public char nextChar() { counter += 1; return source.charAt(counter); }
}

class LexSource {
   private ArrayList<token> tokens;
   private int counter = -1;
   public LexSource(ArrayList<token> tokens) {
      this.tokens = tokens;
   }

   public String nextToken() {
      counter += 1;
      token t = tokens.get(counter);
      switch(t.tokenName) {
         case OO:
         case EOF:
         case AO: {
            return t.lexeme;
         }
      }
      String tokenType = t.tokenName.toString().toLowerCase();
      return tokenType;
   }

   public String getCurrentToken() {
      token t = tokens.get(counter);
       switch(t.tokenName) {
         case OO:
         case EOF:
         case AO: {
            return t.lexeme;
         }
      }
      String tokenType = t.tokenName.toString().toLowerCase();
      return tokenType;
   }

   public String getCurrentTokenLexeme() {
      token t= tokens.get(counter);
      return t.lexeme;
   }
}

public class utils {
   
   public static String CharToString(char c) {
      String s = "";
      s += c;
      return s;
   }

   public static String readfile(String pathToFile) {
      String res = "";
      try {
         File obj = new File(pathToFile);
         Scanner reader = new Scanner(obj);
         while(reader.hasNextLine()) {
            res += "\n"; //Add new line literally
            res += reader.nextLine();
         } reader.close();
      } catch(FileNotFoundException e) {
         System.out.println("Error Occured: \n");
         e.printStackTrace();
      } return res;
   }
}