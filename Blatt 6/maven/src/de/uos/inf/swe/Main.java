import a.A;
import b.B;

/**
 * Test-Class.
 */
public class Main{
    public static void main(String[] args){
        try{
            A a = new A();
            B b = new B();
            
            System.out.println("OK!");
        }
        catch(NoClassDefFoundError e){
            System.err.println("Forgotten to compile class 'A' or 'B'?");
        }
        catch(Throwable t){
             System.err.println("There went something wrong!");
        }
    }
}