import static de.vksi.c4j.Condition.ignored;
import static de.vksi.c4j.Condition.unchanged;
import static de.vksi.c4j.Condition.result;
import static de.vksi.c4j.Condition.old;
import static de.vksi.c4j.Condition.postCondition;
import static de.vksi.c4j.Condition.preCondition;
import de.vksi.c4j.ClassInvariant;
import de.vksi.c4j.Target;

// http://c4j-team.github.io/C4J/syntax.html
	
public class FractionContract extends Fraction {

	@Target
	private Fraction target;

	@ClassInvariant
	public void classInvariant() {
		assert getDenominator() > 0 : "denominator > 0";
	}

	public FractionContract(int numerator, int denominator) {
		super(numerator, denominator);
		if (preCondition()) {
			assert denominator > 0 : "denominator > 0";
		}
		if (postCondition()) {
			assert target.getNumerator() == numerator  : "numerator set";
			assert target.getDenominator() == denominator  : "denominator set";
		}
	}

	@Override
	public int getNumerator() {
		if (postCondition()) {
			int result = result();
			assert result == target.getNumerator() : "numerator unchanged"; // REDUNDANT! Bessere Lösung?!

	        assert unchanged(target.getNumerator()) : "numerator unchanged";
	        assert unchanged(target.getDenominator()) : "denominator unchanged";
		}
		return ignored();
	}

	@Override
	public int getDenominator() {
		if (postCondition()) {
			assert unchanged(target.getNumerator()) : "numerator unchanged";
	    	assert unchanged(target.getDenominator()) : "denominator unchanged";
		}
		return ignored();
	}

	@Override
	public void multiply(int multiplier) {
		if (postCondition()) {
			assert target.getNumerator() == old(target.getNumerator()) * multiplier : "numerator calculated correctly";
	        assert unchanged(target.getDenominator()) : "denominator unchanged";
		}
	}

	@Override
	public void multiply(Fraction fract) {
		if (preCondition()) {
			assert fract.getDenominator() > 0 : "denominator > 0";
		}
		if (postCondition()) {
			assert target.getNumerator() == old(target.getNumerator()) * fract.getNumerator() : "numerator calculated correctly";
			assert target.getDenominator() == old(target.getDenominator()) * fract.getDenominator() : "denominator calculated correctly";
		}
	}

	@Override
	public void divide(Fraction fract) {
		if (preCondition()) {
			assert fract.getDenominator() > 0 : "denominator > 0";
		}
		if (postCondition()) {
			assert target.getNumerator() == old(target.getNumerator()) * fract.getDenominator() : "numerator calculated correctly";
			assert target.getDenominator() == old(target.getDenominator()) * fract.getNumerator() : "denominator calculated correctly";
		}
	}

}
