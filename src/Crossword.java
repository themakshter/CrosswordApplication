import java.io.Serializable;
import java.util.ArrayList;



public class Crossword implements Serializable {
	final ArrayList<Clue> acrossClues, downClues;
	final String title;
	final int size;

	/**
	 * Constructor for a Crossword object
	 * 
	 * @param title
	 *            : The title of the Crossword
	 * @param size
	 *            : The size of the Crossword
	 * @param acrossClues
	 *            : The ArrayList containing the Across Clues
	 * @param downClues
	 *            : The ArrayList containing the Down Clues
	 */
	Crossword(String title, int size, ArrayList<Clue> acrossClues,
			ArrayList<Clue> downClues) {
		this.title = title;
		this.size = size;
		this.acrossClues = acrossClues;
		this.downClues = downClues;
	}
}

