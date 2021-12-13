import java.util.Scanner;  // Import the Scanner class

public class main {
   public static void main(String args[]) {
      int code = -1;
      Scanner s = new Scanner(System.in);
      System.out.print("\nLexical Analysis (0) or Lexical and Syntax Analysis (1): ");
      code = s.nextInt();
      switch(code) {
         case 0: {
            System.out.println("\n!!! Performing analysis on input.txt !!!");
            lex l = new lex("input.txt",true);
            l.parse();
            l.printTokens();
         } break;
         case 1: {
            System.out.print("Enter expression to parse: ");
            String expression = " ";
            expression = s.nextLine();
            expression += s.nextLine();
            s.close();
            System.out.println("\n!!! Parsing " + expression + " !!!");
            lex l = new lex(expression,false);
            l.parse();
            l.printTokens();
         } break;

         default: {
            System.out.println("Error: Invalid Menu Code...");
         } break;
      }
   }
}