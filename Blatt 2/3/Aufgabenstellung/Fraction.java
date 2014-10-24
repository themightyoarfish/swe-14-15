
public class Fraction {

    private int numerator;
    private int denominator;

    public Fraction(int numerator , int denominator) {
        this.numerator = numerator ;
        this.denominator = denominator;
        
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void multiply(int multiplier) {
        this.multiply(new Fraction(multiplier,1));
    }

    public void multiply(Fraction fract) {
        numerator *= fract.getNumerator();
        denominator *= fract.getDenominator();
    }
    public void divide(Fraction fract) {
        this.multiply(new Fraction(fract.getDenominator(),
                                   fract.getNumerator()));
    }
}

