package wildschwein;

public class Schweinbrater extends Thread {

   private Tisch t;
   
   public Schweinbrater(Tisch t){
      this.t = t;
   }
   
   public void run(){
      try{        
         while(true){           
            sleep(50);
            t.braten();
            System.out.println("fertig!");
         }
      }catch(InterruptedException e){
         e.printStackTrace();
      }
   }
}
