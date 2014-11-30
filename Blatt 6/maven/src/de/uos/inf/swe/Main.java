package de.uos.inf.swe;
import de.uos.inf.swe.a.A;
import de.uos.inf.swe.b.B;

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
