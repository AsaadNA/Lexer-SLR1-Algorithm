import java.util.Scanner;  // Import the Scanner class

public class main {
   public static void main(String args[]) {
      int code = -1;
      Scanner s = new Scanner(System.in);
      System.out.print("\nLexical Analysis (0) or Lexical and Syntax Analysis (1): ");
      code = s.nextInt();
      switch(code) {
         case 0: {
            System.out.println("\n!!! Performing lexical analysis on input.txt !!!");
            lex l = new lex("input.txt",true);
            l.parse();
            l.printTokens();
         } break;
         case 1: {
            System.out.print("Enter expression to compile: ");
            String expression = "";
            expression = s.nextLine();
            expression += s.nextLine();
            s.close();
            System.out.println("\n!!! Compiling " + expression + " !!!");
            lex l = new lex(expression,false);
            if(l.parse()) {
               l.printTokens();
               parser p = new parser(l.getTokens());
               if(p.compile()) {System.out.println("\n!!! Compiled Successfully !!!");}
               else {System.out.println("\n!!! Compilation Failed !!!");}
            } else {
               l.printTokens();
            }
         } break;

         default: {
            System.out.println("Error: Invalid Menu Code...");
         } break;
      } 
   }
}