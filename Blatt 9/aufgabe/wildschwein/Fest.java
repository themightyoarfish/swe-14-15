package wildschwein;

public class Fest {

   public static void main(String[] args){
      
      Tisch t = new Tisch();
      Schweinbrater s = new Schweinbrater(t);
      Obelix o = new Obelix(t);
      s.start();
      o.start();
      
   }
}
