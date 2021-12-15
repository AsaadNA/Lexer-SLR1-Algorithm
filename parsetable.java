import java.util.ArrayList;

class Item {
   public char state;
   public char input;
   public String data;
   public Item(char state , char input , String data) {
      this.state = state;
      this.input = input;
      this.data = data;
   }
}

public class parsetable {

   private ArrayList<Item> map = new ArrayList<Item>();

   public void put(Item i) {
      map.add(new Item(i.state,i.input,i.data));
   }

   public String get(char state , char input) {
      for(Item i : map) {
         if(i.state == state && i.input == input) return i.data;
      } return null;
   }
}