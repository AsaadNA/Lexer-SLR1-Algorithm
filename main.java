public class main {
   public static void main(String args[]) {
      lex l = new lex("input.txt");
      l.parse();
      l.printTokens();
      l.printSymbolTable();
   }
}