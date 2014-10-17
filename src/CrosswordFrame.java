import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.SpringLayout;

/**
 * This is a Frame which contains the Panels and all
 * 
 * @author mak1g11
 * 
 */
public class CrosswordFrame extends JFrame {
	/**
	 * Constructor for the frame
	 */
	public CrosswordFrame() {
		super("Crossword Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initiate() {
		SpringLayout layout = new SpringLayout(); // SpringLayout is set
		CrosswordPanel cp = new CrosswordPanel(); // CrosswordPanel is added
		Container c = getContentPane();
		c.setLayout(layout);
		c.add(cp);
		layout.putConstraint("North", cp, 0, "North", this);
		layout.putConstraint("West", cp, 0, "West", this);
		setVisible(true);
		setSize(900, 500); // a specific size is set
		setLocationRelativeTo(null); // the frame appears in the centre of the
										// screen
	}
}