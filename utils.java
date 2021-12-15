import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

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

public class utils {
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