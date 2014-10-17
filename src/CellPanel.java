import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * This is a panel which contains the visual representation of a Crossword
 * 
 * @author mak1g11
 * 
 */
public class CellPanel extends JPanel {
	private Crossword cr;
	private Cell[][] crossword;
	private boolean logged;
	private Cell selectedCell;
	private HashMap<Clue, ArrayList<Cell>> clueMap;

	/**
	 * The Constructor for the CellPanel
	 * 
	 * @param c
	 *            : The Crossword which is to be drawn
	 */
	public CellPanel(Crossword c) {
		cr = c;
		logged = false;
		addKeyListener(new Writer());
		clueMap = new HashMap<Clue, ArrayList<Cell>>();
		drawCrossword();

	}

	/**
	 * Sets the value of logged to true
	 */
	public void loggedIn() {
		logged = true;
	}

	/**
	 * Sets the value of logged to false
	 */
	public void loggedOut() {
		logged = false;
	}

	/**
	 * Creates the Crossword
	 */
	public void drawCrossword() {
		setLayout(new GridLayout(cr.size, cr.size)); // sets the layout of the
														// Grid layout for a
														// clean good setting
		crossword = new Cell[cr.size][cr.size]; // a two-dimensional array is
												// created
		for (int y = 0; y < cr.size; y++) { // this loop goes through all the
											// spaces to be occupied by the
											// crossword and creates one for
											// each space
			for (int x = 0; x < cr.size; x++) {
				Cell cell = new Cell(x, y, this);
				crossword[x][y] = cell;
				for (Clue c : cr.acrossClues) { // if the x and y of the cell
												// match that of the across
												// clue, then a number is added
												// to the top left of the cell
					if (x == c.x && y == c.y) {
						JLabel num = new JLabel("" + c.number);
						cell.add(num, BorderLayout.NORTH);
						cell.setBackground(Color.white);
						cell.setBorder(javax.swing.BorderFactory
								.createLineBorder(Color.BLACK));

						cell.linkAc(c); // cell is linked to the clue
					}
				}
				for (Clue c : cr.downClues) { // same for the down clue
					if (x == c.x && y == c.y) {
						JLabel num = new JLabel("" + c.number);
						cell.add(num, BorderLayout.NORTH);
						cell.setBackground(Color.white);
						cell.setBorder(javax.swing.BorderFactory
								.createLineBorder(Color.BLACK));
						cell.linkDwn(c);

					}
				}
				this.add(cell); // cell is added to the Panel
			}
		}
		for (int i = 0; i < cr.acrossClues.size(); i++) { // goes through the
															// across clues and
															// checks if the x
															// of the cells
															// contains the x of
															// the clue. If so,
															// the color is set
															// to white
			ArrayList<Cell> cells = new ArrayList<Cell>();
			for (int k = 0; k < cr.acrossClues.get(i).answer.length(); k++) {
				crossword[cr.acrossClues.get(i).x + k][cr.acrossClues.get(i).y]
						.setBackground(Color.white);
				crossword[cr.acrossClues.get(i).x + k][cr.acrossClues.get(i).y]
						.setBorder(javax.swing.BorderFactory
								.createLineBorder(Color.BLACK));
				cells.add(crossword[cr.acrossClues.get(i).x + k][cr.acrossClues
						.get(i).y]);
				crossword[cr.acrossClues.get(i).x + k][cr.acrossClues.get(i).y]
						.linkAc(cr.acrossClues.get(i));

			}
			clueMap.put(cr.acrossClues.get(i), cells); // the cell is linked to
														// a clue
		}
		for (int i = 0; i < cr.downClues.size(); i++) { // same for the down
														// clues but instead,
														// the y is checked
			ArrayList<Cell> cells = new ArrayList<Cell>();
			for (int k = 0; k < cr.downClues.get(i).answer.length(); k++) {
				crossword[cr.downClues.get(i).x][cr.downClues.get(i).y + k]
						.setBackground(Color.white);
				crossword[cr.downClues.get(i).x][cr.downClues.get(i).y + k]
						.setBorder(javax.swing.BorderFactory
								.createLineBorder(Color.BLACK));
				cells.add(crossword[cr.downClues.get(i).x][cr.downClues.get(i).y
						+ k]);
				crossword[cr.downClues.get(i).x][cr.downClues.get(i).y + k]
						.linkDwn(cr.downClues.get(i));

			}
			clueMap.put(cr.downClues.get(i), cells); // the cell is linked to
														// the clue
		}
	}

	/**
	 * Creates a HashMap from the string contained by each set of cells linked
	 * to a clue
	 * 
	 * @return The HashMap linking the clues and the strings
	 */
	public HashMap<Clue, String> getLetters() {
		HashMap<Clue, String> map = new HashMap<Clue, String>();
		ArrayList<Cell> cells = null;
		String answer = "";
		// the across clues and the down clues are iterated through the
		// ArrayLists of cells linked to them are retrieved and the answer from
		// the cells
		for (Clue cl : cr.acrossClues) {
			cells = clueMap.get(cl);
			for (Cell c : cells) {
				answer = answer + c.getLetter();
			}
			map.put(cl, answer);
			answer = "";
		}
		for (Clue cl : cr.downClues) {
			cells = clueMap.get(cl);
			for (Cell c : cells) {
				answer = answer + c.getLetter();
			}
			map.put(cl, answer);
			answer = "";
		}
		return map;

	}

	/**
	 * Sets the selectedCell to the Cell given
	 * 
	 * @param c
	 *            : The cell to become the SelectedCell
	 */
	public void selectCell(Cell c) {
		selectedCell = c;
	}

	/**
	 * Highlights all the cells in the arraylist of the selected cells
	 */
	public void highlightCells() {
		clearCellColours(); // first, the cell colours are cleared
		ArrayList<Cell> cells = null;
		if (!(selectedCell.getDwLink() == null)
				&& !(selectedCell.getAcLink() == null)) { // executed if Cell is
															// linked across and
															// down
			selectedCell.getAcLink().select(); // at the beginning one is
												// selected
			if (selectedCell.getDwLink().isSelected()) { // checked if selected
															// or not
				cells = clueMap.get(selectedCell.getAcLink()); // cells is set
																// to the
																// arraylist
				selectedCell.getAcLink().select();
				for (Cell cs : cells) { // iterated through the ArrayList and
										// all the cells are set to yellow
										// colour
					cs.setBackground(Color.yellow);
				}
				selectedCell.getAcLink().select();
				selectedCell.getDwLink().deselect();
			} else if ((selectedCell.getAcLink().isSelected())) { // checked
																	// again
				cells = clueMap.get(selectedCell.getDwLink());
				selectedCell.getDwLink().select();
				for (Cell cs : cells) {
					cs.setBackground(Color.yellow);
				}
				selectedCell.getDwLink().select();
				selectedCell.getAcLink().deselect();
			}
		} else if (!(selectedCell.getDwLink() == null)) { // executed if cells
															// only linked down
			cells = clueMap.get(selectedCell.getDwLink());
			for (Cell cs : cells) {
				cs.setBackground(Color.yellow);
			}
			selectedCell.getDwLink().select();
		} else if (!(selectedCell.getAcLink() == null)) { // linked across
			cells = clueMap.get(selectedCell.getAcLink());
			for (Cell cs : cells) {
				cs.setBackground(Color.yellow);
			}
			selectedCell.getAcLink().select();
		}

		selectedCell.setBackground(Color.cyan); // selectedCell colour is set to
												// cyan

	}

	/**
	 * The next cell of the selected cell is chosen
	 */
	public void nextCell() {
		clearCellColours(); // cell colours cleared first
		ArrayList<Cell> cells = null;
		if (!(selectedCell.getAcLink() == null)
				&& !(selectedCell.getDwLink() == null)) { // executed if Cell is
															// linked across and
															// down
			if (selectedCell.getAcLink().isSelected()) { // checked which link
															// is selected
				cells = clueMap.get(selectedCell.getAcLink());
				selectedCell.getDwLink().deselect(); // somewhat similar thing
														// done to the
														// highlightCells method
				for (Cell cs : cells) {
					cs.setBackground(Color.yellow);
				}
				if ((selectedCell.getXPos() + 1 < cr.size) // checked if the
															// cell next to
															// it/below it
															// depending on the
															// case is not black
															// and is not more
															// than the size of
															// the crossword
						&& !(crossword[selectedCell.getXPos() + 1][selectedCell
								.getYPos()].getBackground() == Color.black)) {
					selectedCell = crossword[selectedCell.getXPos() + 1][selectedCell
							.getYPos()];
				}
			} else if (selectedCell.getDwLink().isSelected()) {
				cells = clueMap.get(selectedCell.getDwLink());
				selectedCell.getAcLink().deselect();
				for (Cell cs : cells) {
					cs.setBackground(Color.yellow);
				}
				if ((selectedCell.getYPos() + 1 < cr.size)
						&& !(crossword[selectedCell.getXPos()][selectedCell
								.getYPos() + 1].getBackground() == Color.black)) {
					selectedCell = crossword[selectedCell.getXPos()][selectedCell
							.getYPos() + 1];
				}
			}
		} else if (!(selectedCell.getAcLink() == null)) { // same if only linked
															// across
			if (selectedCell.getAcLink().isSelected()) {
				cells = clueMap.get(selectedCell.getAcLink());
				for (Cell cs : cells) {
					cs.setBackground(Color.yellow);
				}
				if ((selectedCell.getXPos() + 1 < cr.size)
						&& !(crossword[selectedCell.getXPos() + 1][selectedCell
								.getYPos()].getBackground() == Color.black)) {
					selectedCell = crossword[selectedCell.getXPos() + 1][selectedCell
							.getYPos()];
				}
			}
		} else if (!(selectedCell.getDwLink() == null)) { // same if only linked
															// down
			if (selectedCell.getDwLink().isSelected()) {
				cells = clueMap.get(selectedCell.getDwLink());
				for (Cell cs : cells) {
					cs.setBackground(Color.yellow);
				}
				if ((selectedCell.getYPos() + 1 < cr.size)
						&& !(crossword[selectedCell.getXPos()][selectedCell
								.getYPos() + 1].getBackground() == Color.black)) {
					selectedCell = crossword[selectedCell.getXPos()][selectedCell
							.getYPos() + 1];
				}
			}
		}
		selectedCell.setBackground(Color.cyan); // the new selected cell is set
												// to colour cyan
	}

	/**
	 * The cells linked to the clue are highlighted
	 * 
	 * @param c
	 *            : The Clue of which the cells are to be highlighted
	 */

	public void highlightClue(Clue c) {
		clearCellColours();
		ArrayList<Cell> cells = clueMap.get(c); // the HashMap comes in use
												// again
		for (Cell cs : cells) { // the cells in the arraylist are all set to
								// yellow colour
			cs.setBackground(Color.yellow);
		}
		selectedCell = cells.get(0);
		selectedCell.setBackground(Color.cyan); // the first cell is set to cyan
												// colour

	}

	/**
	 * Clears any colours of the cells
	 */
	public void clearCellColours() {
		// iterates through the whole two dimensional array and sets the colour
		// of all the cells whose colour are not black, to white
		for (int y = 0; y < cr.size; y++) {
			for (int x = 0; x < cr.size; x++) {
				if (!((crossword[x][y].getBackground() == Color.white) || (crossword[x][y]
						.getBackground() == Color.black))) {
					crossword[x][y].setBackground(Color.white);

				}
			}
		}
	}

	/**
	 * This class handles the events invoked by key pressing
	 * 
	 * @author mak1g11
	 * 
	 */
	class Writer implements KeyListener {

		public void keyPressed(KeyEvent e) {
			if (logged) { // checked if the user is logged on or not
				if (!(selectedCell == null)) {
					if (!(e.isActionKey())) { // checks if the key pressed has
												// been an action key or not
						String letter = Character.toString(e.getKeyChar())
								.toUpperCase();
						selectedCell.addLetter(letter); // letter is added to
														// the selectedCell
						selectedCell.nextCell(); // nextCell is brought into
													// focus
					}
				}
			} else {
				JOptionPane.showMessageDialog(selectedCell,
						"Please log in before typing", "Error!",
						JOptionPane.ERROR_MESSAGE);
			}

		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}

	}
}