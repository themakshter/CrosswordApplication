import java.io.Serializable;

/**
 * Represents a Clue
 * 
 * @author mak1g11
 * 
 */
public class Clue implements Serializable {
	final int number, x, y;
	final String clue, answer;
	private boolean selected = false;

	/**
	 * 
	 * @param number
	 *            : The number of the Clue.
	 * @param x
	 *            : The x-coordinate of the Clue.
	 * @param y
	 *            : The y-coordinate of the Clue.
	 * @param clue
	 *            : The string containing the clue
	 * @param answer
	 *            : The string containing the answer to the clue
	 */
	Clue(int number, int x, int y, String clue, String answer) {
		this.number = number;
		this.x = x;
		this.y = y;
		this.clue = clue;
		this.answer = answer;
	}

	/**
	 * sets the Selected boolean to true
	 */
	public void select() {
		selected = true;
	}

	/**
	 * sets the Selected boolean to false
	 */
	public void deselect() {
		selected = false;
	}

	/**
	 * 
	 * @return If the value of Selected is true or not
	 */
	public boolean isSelected() {
		return selected;
	}

}