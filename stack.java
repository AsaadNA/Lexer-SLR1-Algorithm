public class stack {

   private char[] arr;
   private int top = -1;

   public stack(int size) {
      arr = new int[];
   }

   public void push(char data) {
      top += 1;  
      arr[top] = data;
   }

   public int pop() {
      if(top <= 0) {
         int t = arr[top];
         top = -1;
         return t;
      } else {
         int t = arr[top];
         top -=1;
         return t;
      }
   }

   public boolean isEmpty() {
      return (top == -1);
   }

   public void print() {
      while(!isEmpty()) {
         System.out.println(pop());
      }
   }
}