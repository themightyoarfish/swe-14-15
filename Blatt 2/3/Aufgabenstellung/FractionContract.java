import static de.vksi.c4j.Condition.ignored;
import static de.vksi.c4j.Condition.unchanged;
import static de.vksi.c4j.Condition.result;
import static de.vksi.c4j.Condition.old;
import static de.vksi.c4j.Condition.postCondition;
import static de.vksi.c4j.Condition.preCondition;
import de.vksi.c4j.ClassInvariant;
import de.vksi.c4j.Target;
import de.vksi.c4j.PureTarget;

// http://c4j-team.github.io/C4J/syntax.html
	
public class FractionContract extends Fraction {

	@Target
	private Fraction target;

	@ClassInvariant
	public void classInvariant() {
		assert getDenominator() > 0 : "denominator <= 0!";
	}

	public FractionContract(int numerator, int denominator) {
		super(numerator, denominator);
		if (preCondition()) {
			assert denominator > 0 : "denominator <= 0!";
		}
		if (postCondition()) {
			assert target.getNumerator() == numerator  : "numerator set incorrectly";
			assert target.getDenominator() == denominator  : "denominator set incorrectly";
		}
	}

	@Override
   @PureTarget
	public int getNumerator() {
		if (postCondition()) {
	        assert unchanged(target.getNumerator()) : "numerator was changed";
	        assert unchanged(target.getDenominator()) : "denominator was changed";
		}
		return ignored();
	}

	@Override
   @PureTarget
	public int getDenominator() {
		if (postCondition()) {
			assert unchanged(target.getNumerator()) : "numerator was changed";
	    	assert unchanged(target.getDenominator()) : "denominator was changed";
		}
		return ignored();
	}

	@Override
	public void multiply(int multiplier) {
		if (postCondition()) {
         assert target.getNumerator() == old(target.getNumerator()) * multiplier : "numerator calculated incorrectly";
         assert unchanged(target.getDenominator()) : "denominator was changed";
		}
	}

	@Override
	public void multiply(Fraction fract) {
		if (preCondition()) {
			assert target.getDenominator() > 0 : "target denominator <= 0";
			assert fract.getDenominator() > 0 : "aargument denominator <= 0";
		}
		if (postCondition()) {
			assert target.getNumerator() == old(target.getNumerator()) * fract.getNumerator() : "numerator calculated incorrectly";
			assert target.getDenominator() == old(target.getDenominator()) * fract.getDenominator() : "denominator calculated incorrectly";
		}
	}

	@Override
	public void divide(Fraction fract) {
		if (preCondition()) {
			assert target.getDenominator() > 0 : "target denominator <= 0";
			assert fract.getDenominator() > 0 : "fract denominator <= 0";
		}
		if (postCondition()) {
			assert target.getNumerator() == old(target.getNumerator()) * fract.getDenominator() : "numerator calculated incorrectly";
			assert target.getDenominator() == old(target.getDenominator()) * fract.getNumerator() : "denominator calculated incorrectly";
		}
	}

}
