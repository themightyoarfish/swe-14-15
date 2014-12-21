package wildschwein;

public class Tisch {

   int anzahl;

   public synchronized void essen() throws InterruptedException {
      while (anzahl <= 0) {
         wait();
      }
      anzahl -= 1;
      notifyAll();
   }

   public synchronized void braten() throws InterruptedException {
      while (anzahl >= 10) {
         wait();
      }
      anzahl += 1;
      notifyAll();
   }

}
