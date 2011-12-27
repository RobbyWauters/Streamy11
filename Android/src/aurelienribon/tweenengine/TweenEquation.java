package aurelienribon.tweenengine;

/**
 * Base class for every easing equation. You can create your own equations
 * and directly use them in the Tween static methods by deriving from this
 * class.
 * 
 * @author Aurelien Ribon (aurelien.ribon@gmail.com)
 */
public abstract class TweenEquation {
	/**
	 * Computes the next value of the interpolation.
	 * @param t Current time, in seconds.
	 * @param b Initial value.
	 * @param c Offset to the initial value.
	 * @param d Total duration, in seconds.
	 * @return
	 */
    public abstract float compute(float t, float b, float c, float d);

	/**
	 * Returns true if the given string is the name of this equation (the name
	 * is returned in the toString() method, don't forget to override it).
	 * This method is usually used to save/load a tween to/from a text file.
	 */
	public boolean isValueOf(String str) {
		return str.equals(toString());
	}
}
