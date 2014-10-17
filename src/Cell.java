import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;



/**
 * Represents a cell in a Crossword
 * 
 * @author mak1g11
 * 
 */
public class Cell extends JPanel {
	private int xPos;
	private int yPos;
	private CellPanel cp;
	private Clue ac, dw;
	private JLabel letter;
	public SpringLayout sp;

	/**
	 * Constructor for the Cell
	 * 
	 * @param x
	 *            : The x co-ordinate of the Cell.
	 * @param y
	 *            : The y co-ordinate of the Cell.
	 * @param cp
	 *            : The CellPanel the Cell belongs to
	 */
	public Cell(int x, int y, CellPanel cp) {
		xPos = x;
		yPos = y;
		this.cp = cp;
		sp = new SpringLayout(); // the SpringLayout is initialised
		setLayout(sp);
		setPreferredSize(new Dimension(35, 35)); // the size of a cell is set
		setBackground(Color.black);
		addMouseListener(new CellListener(this)); // a MouseListener is added
		letter = new JLabel(""); // a JLabel is added which can be edited later
		this.add(letter);
		sp.putConstraint("North", letter, 9, "North", this);
		sp.putConstraint("East", letter, -10, "East", this);

	}

	/**
	 * Sets the JLabel as the string entered
	 * 
	 * @param s
	 *            : The String to be added to the JLabel.
	 */
	public void addLetter(String s) {
		letter.setFont(new Font(letter.getFont().getFontName(), Font.BOLD, 16));
		letter.setText(s);
	}

	/**
	 * 
	 * @return The letter contained by the cell
	 */
	public String getLetter() {
		return letter.getText();
	}

	/**
	 * 
	 * @return The x co-ordinate of the cell.
	 */
	public int getXPos() {
		return xPos;
	}

	/**
	 * 
	 * @return The y co-ordinate of the cell
	 */
	public int getYPos() {
		return yPos;
	}

	/**
	 * Calls the CellPanel's method for the next cell
	 */
	public void nextCell() {
		cp.nextCell();
	}

	/**
	 * Links an Across Clue to the cell
	 * 
	 * @param c
	 *            : The Across Clue to be added to the Cell.
	 */
	public void linkAc(Clue c) {
		ac = c;
	}

	/**
	 * Links an Down Clue to the cell
	 * 
	 * @param c
	 *            : The Down Clue to be added to the Cell.
	 */
	public void linkDwn(Clue c) {
		dw = c;
	}

	/**
	 * 
	 * @return The Across Clue linked to the cell, if any
	 */
	public Clue getAcLink() {
		return ac;
	}

	/**
	 * 
	 * @return The Down Clue linked to the cell, if any
	 */
	public Clue getDwLink() {
		return dw;
	}

	/**
	 * This is a class to handle the mouse events called on the Cell
	 * 
	 * @author mak1g11
	 * 
	 */
	class CellListener implements MouseListener {
		Cell c;

		/**
		 * Constructor for the Listener
		 * 
		 * @param c
		 *            : The Cell to be manipulated
		 */
		public CellListener(Cell c) {
			this.c = c;
		}

		public void mouseClicked(MouseEvent e) {
			cp.requestFocus(); // gets focus of the keyboard on it
			if (!(c.getBackground() == Color.black)) { // checks if the color of
														// the cell is black or
														// not
				setBackground(Color.cyan); // sets the color of the cell to blue
				cp.selectCell(c); // the CellPanel calls a method
				cp.highlightCells(); // CellPanel highlights the cells

			}
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}

}