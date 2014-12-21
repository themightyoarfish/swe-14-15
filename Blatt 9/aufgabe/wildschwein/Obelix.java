package wildschwein;

public class Obelix extends Thread {

   private Tisch t;
   
   public Obelix(Tisch t){
      this.t=t;
   }
   
   public void run(){
      try{         
         while(true){     
            sleep(100);
            t.essen();
            System.out.println("lecker!");         
         }         
      }catch(InterruptedException e){
         e.printStackTrace();
      }
   }
}
