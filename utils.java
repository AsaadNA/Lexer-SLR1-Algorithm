import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

//This will contain all the basic i/o or other functions
public class utils {
   public static String readfile(String pathToFile) {
      String res = "";
      try {
         File obj = new File(pathToFile);
         Scanner reader = new Scanner(obj);
         while(reader.hasNextLine()) {
            res += reader.nextLine();
         } reader.close();
      } catch(FileNotFoundException e) {
         System.out.println("Error Occured: \n");
         e.printStackTrace();
      } return res;
   }
}