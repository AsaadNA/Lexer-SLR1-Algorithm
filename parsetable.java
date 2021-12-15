import java.util.ArrayList;

class Item {
   public String state,input,data;
   public Item(String state , String input , String data) {
      this.state = state;
      this.input = input;
      this.data = data;
   }
}

public class parsetable {
   
   private ArrayList<Item> map = new ArrayList<Item>();

   public void put(Item i) { map.add(new Item(i.state,i.input,i.data));}
   public String get(String state , String input) {
      for(Item i : map) {
         if(i.state.equals(state) && i.input.equals(input)) return i.data;
      } return null;
   }

}