import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This class makes the whole Crossword Application
 * 
 * @author Mohammad Ali
 * 
 */
public class CrosswordApp {
	public static void main(String[] args) {
		CrosswordFrame cf = new CrosswordFrame(); // a new crossword frame is
													// created
		cf.initiate();
	}
}

/**
 * This is a Frame which contains the Panels and all
 * 
 * @author mak1g11
 * 
 */
class CrosswordFrame extends JFrame {
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

/**
 * This is a class to find anagrams for any word the user enters
 * 
 * @author jr2
 * 
 */
class AnagramFrame extends JFrame {

	public AnagramFrame(String title) {
		super(title);
	}

	void init() {

		Container panel = this.getContentPane();
		panel.setLayout(new FlowLayout());
		JTextField input = new JTextField(8);
		JTextArea output = new JTextArea(20, 20);
		JScrollPane sp = new JScrollPane(output);
		JButton anagram = new JButton("Get anagrams");
		panel.add(input);
		panel.add(anagram);
		panel.add(sp);
		ActionListener rl = new AnagramListener(input, output);
		anagram.addActionListener(rl);
		input.addActionListener(rl);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // this is done
																// so that the
																// program does
																// not exit
		this.setSize(300, 450);
		this.setVisible(true);
	}
}

class AnagramListener implements ActionListener {

	JTextField input;
	JTextArea output;

	public AnagramListener(JTextField input, JTextArea output) {
		this.input = input;
		this.output = output;

	}

	public void actionPerformed(ActionEvent e) {

		final String s;
		s = input.getText();
		Random r = new Random();

		class AnagramSwingWorker extends SwingWorker<String, String> {

			int size = s.length();
			char[] chars = new char[size];
			StringBuffer sb = new StringBuffer();

			public String doInBackground() {
				for (int j = 0; j < size; j++) {
					chars[j] = s.charAt(j);
				}
				doAnagram(size);
				return ("");
			}

			private void doAnagram(int rsize) {
				if (rsize == 1) {
					return;
				}
				for (int i = 0; i < rsize; i++) {
					doAnagram(rsize - 1);
					if (rsize == 2) {
						for (int j = 0; j < size; j++) {
							sb.append(chars[j]);
						}
						sb.append("\n");
						publish(sb.toString());
						sb.delete(0, size + 1);
						try {
							Thread.sleep(100);
						} catch (InterruptedException ie) {
						}
					}
					rotate(rsize);
				}
			}

			private void rotate(int rsize) {
				int i;
				int pos = size - rsize;
				char tmp = chars[pos];
				for (i = pos + 1; i < size; i++) {
					chars[i - 1] = chars[i];
				}
				chars[i - 1] = tmp;
			}

			protected void process(List<String> cs) {
				for (String c : cs) {
					output.append(c);
				}
			}

			protected void done() {
				output.append("\nAll done for word " + s + "\n");
			}

		}

		final SwingWorker sw = new AnagramSwingWorker();
		sw.execute();
		output.setBackground(new Color(r.nextInt(256), r.nextInt(256), r
				.nextInt(256)));
	}
}

/**
 * This is a custom class to write or read Crossword objects
 * 
 * @author mak1g11
 * 
 */
class CrosswordIO {
	/**
	 * Writes a Crossword object into a file
	 * 
	 * @param c
	 *            : The Crossword to be written into the file
	 * @param f
	 *            : The file to which the Crossword is to be written
	 * @throws IOException
	 */
	public static void writePuzzle(Crossword c, File f) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(f);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(c);
		out.flush();
		out.close();
		fileOut.close();
	}

	/**
	 * Reads a Crossword object from a file
	 * 
	 * @param f
	 *            : The file from which the Crossword is to be read
	 * @return The Crossword read from the file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Crossword readPuzzle(File f) throws IOException,
			ClassNotFoundException {
		Crossword c = null;
		FileInputStream fileIn = new FileInputStream(f);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		c = (Crossword) in.readObject();
		in.close();
		fileIn.close();
		return c;
	}
}

/**
 * Represents a cell in a Crossword
 * 
 * @author mak1g11
 * 
 */
class Cell extends JPanel {
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

/**
 * This class makes a Crossword panel which is to contain the crossword and all
 * the other buttons and things.
 * 
 * @author mak1g11
 * 
 */
class CrosswordPanel extends JPanel {
	private Crossword c;
	private JPanel userPanel;
	private CellPanel cp;
	private JLabel user, title;
	private boolean logged;
	private HashMap<Clue, String> answers;
	private HashMap<String, Clue> acMap, dwMap;
	private JList<String> acrossList, downList;
	private JScrollPane clueScroller;
	private JCheckBox clueSupport;
	private JTextArea solvedClues;
	private JButton login, logout, save, load, anagrams;
	private SpringLayout spLay;
	private JTextField username;

	/**
	 * Creates an instance of a CrosswordPanel. In it, the HashMaps are
	 * intialised and the crossword is set to the example puzzle. Logged is set
	 * to false and the initiate method is called.
	 */
	public CrosswordPanel() {
		spLay = new SpringLayout();
		this.setLayout(spLay);
		new HashMap<Clue, ArrayList<Cell>>();
		answers = new HashMap<Clue, String>();
		acMap = new HashMap<String, Clue>();
		dwMap = new HashMap<String, Clue>();
		c = new CrosswordExample().getPuzzle();
		logged = false;
		initiate();
	}

	/**
	 * Adds all the things to the panel such as the crossword, lists etc.
	 */
	public void initiate() {
		addTitle();
		addLogin();
		addLists();
		addButtons();
		addCrossword();
		addClueSupport();
		setPreferredSize(new Dimension(900, 500)); // sets a specific size for
													// the panel
	}

	/**
	 * Creates a JLabel containing the title of the Crossword and adds it to the
	 * CrosswordPanel.
	 */
	public void addTitle() {
		title = new JLabel(c.title); // title is retrieved from the
										// crossword
		this.add(title);
		spLay.putConstraint("North", title, 20, "North", this);
		spLay.putConstraint("West", title, 140, "West", this);
		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 16));
	}

	/**
	 * Adds the JLists and the ScrollPane to the CrosswordPanel. Fills the
	 * JLists with the clues.
	 */
	public void addLists() {
		JLabel across = new JLabel("Across");
		JLabel down = new JLabel("Down");
		ListListener ll = new ListListener();
		DefaultListModel<String> acrossModel = new DefaultListModel<String>();
		// a default model is used to be able to only add the Clue part to the
		// list
		// at the same time, there is a Hashmap which allocates each string to a
		// clue
		for (Clue cl : c.acrossClues) {
			String value = "" + cl.number + ". " + cl.clue + " ("
					+ cl.clue.length() + ")";
			acMap.put(value, cl);
			acrossModel.addElement(value);
		}
		acrossList = new JList<String>();
		acrossList.setModel(acrossModel);
		acrossList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		acrossList.setLayoutOrientation(JList.VERTICAL);
		acrossList.setVisibleRowCount(2);
		acrossList.addListSelectionListener(ll);
		DefaultListModel<String> downModel = new DefaultListModel<String>();
		for (Clue cl : c.downClues) {
			String value = "" + cl.number + ". " + cl.clue + " ("
					+ cl.clue.length() + ")";
			dwMap.put(value, cl);
			downModel.addElement(value);
		}
		downList = new JList<String>();
		downList.setModel(downModel);
		downList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		downList.setLayoutOrientation(JList.VERTICAL);
		downList.setVisibleRowCount(-1);
		downList.addListSelectionListener(ll);
		JScrollPane acList = new JScrollPane(acrossList);
		JScrollPane dwList = new JScrollPane(downList);
		dwList.setPreferredSize(new Dimension(200, 190)); // set to a specific
															// size
		acList.setPreferredSize(new Dimension(200, 190));
		this.add(across);
		this.add(down);
		this.add(acList);
		this.add(dwList);
		spLay.putConstraint("North", down, 80, "North", userPanel);
		spLay.putConstraint("East", down, -165, "East", this);
		spLay.putConstraint("North", across, 80, "North", userPanel);
		spLay.putConstraint("East", across, -195, "East", down);
		spLay.putConstraint("North", acList, 20, "North", across);
		spLay.putConstraint("East", acList, -220, "East", dwList);
		spLay.putConstraint("North", dwList, 20, "North", down);
		spLay.putConstraint("East", dwList, -40, "East", this);

	}

	/**
	 * Adds the buttons which give an option to load or save Puzzles and to find
	 * anagrams of a word.
	 */
	public void addButtons() {
		save = new JButton("Save Puzzle");
		load = new JButton("Load Puzzle");
		anagrams = new JButton("Anagrams");
		ButtonListener bl = new ButtonListener();
		save.addActionListener(bl);
		load.addActionListener(bl);
		anagrams.addActionListener(bl);
		this.add(save);
		this.add(anagrams);
		this.add(load);
		spLay.putConstraint("North", save, 45, "North", userPanel);
		spLay.putConstraint("East", save, -300, "East", this);
		spLay.putConstraint("North", load, 45, "North", userPanel);
		spLay.putConstraint("East", load, 120, "East", anagrams);
		spLay.putConstraint("North", anagrams, 45, "North", userPanel);
		spLay.putConstraint("East", anagrams, 110, "East", save);
	}

	/**
	 * Adds the JCheckBox and a TextArea to allow for ClueSupport if needed.
	 */
	public void addClueSupport() {
		clueSupport = new JCheckBox("Clue Support", false);
		solvedClues = new JTextArea("");
		solvedClues.setEditable(false);
		solvedClues.setLineWrap(true);
		solvedClues.setWrapStyleWord(true);
		clueScroller = new JScrollPane(solvedClues);
		clueScroller
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		clueScroller.setPreferredSize(new Dimension(415, 75));
		clueScroller.setVisible(false);
		clueSupport.addItemListener(new ClueSupport());
		this.add(clueScroller);
		this.add(clueSupport);
		spLay.putConstraint("South", clueSupport, -130, "South", this);
		spLay.putConstraint("East", clueSupport, -365, "East", this);
		spLay.putConstraint("South", clueScroller, -50, "South", this);
		spLay.putConstraint("East", clueScroller, -48, "East", this);

	}

	/**
	 * In case of loading a new puzzle, this gets the new crossword and displays
	 * it.
	 */
	public void getNewCrossword() {
		title.setText(c.title); // the title is set to the one of the loaded
								// puzzle
		cp.setVisible(false);
		cp = new CellPanel(c); // the CellPanel is created again so the new
								// Crossword is added
		addCrossword();
		this.addLists(); // JLists are added again

	}

	/**
	 * This retrieves all the answers entered in the cells of the crossword.
	 */
	public void retrieveLetters() {
		answers = cp.getLetters();
	}

	/**
	 * This adds a User Panel containing a textfield and a button to log in.
	 */
	public void addLogin() {
		userPanel = new JPanel();
		login = new JButton("Login");
		logout = new JButton("Logout");
		logout.setVisible(false);
		ButtonListener bl = new ButtonListener();
		login.addActionListener(bl);
		logout.addActionListener(bl);
		username = new JTextField(12);
		user = new JLabel("Enter your name:  ");
		userPanel.setLayout(new BorderLayout());
		userPanel.add(logout, BorderLayout.SOUTH);
		userPanel.add(user, BorderLayout.WEST);
		userPanel.add(login, BorderLayout.EAST);
		userPanel.add(username, BorderLayout.CENTER);
		this.add(userPanel);
		JLabel title = new JLabel("User Details");
		this.add(title);
		spLay.putConstraint("North", title, 20, "North", this);
		spLay.putConstraint("East", title, -330, "East", this);
		spLay.putConstraint("North", userPanel, 50, "North", this);
		spLay.putConstraint("East", userPanel, -100, "East", this);
	}

	/**
	 * This method checks through the clues and determines which of the clues
	 * have been correctly solved or not. The number of the correct ones is
	 * displayed
	 */
	public void setUpSupport() {
		String answer;
		String solved = "";
		// each clue in the arraylist is iterated through to check if the answer
		// is correct or not
		for (Clue cl : c.acrossClues) {
			// from the HashMap is returned the answer for each clue and is then
			// compared. If it is correct, the solved is set to indicate that
			answer = answers.get(cl);
			if (answer.equals(cl.answer.toUpperCase())) {
				solved = solved
						+ ("Clue number " + cl.number + " has been solved\n");
			}
		}
		for (Clue cl : c.downClues) {
			answer = answers.get(cl);
			if (answer.equals(cl.answer.toUpperCase())) {
				solved = solved
						+ ("Clue number " + cl.number + " has been solved\n");
			}
		}
		// Once the iteration are complete, the text area displays the solved
		// clues
		solvedClues.setText(solved);

	}

	/**
	 * This creates a CellPanel and adds it to the CrosswordPanel.
	 */
	public void addCrossword() {
		cp = new CellPanel(c); // creates a CellPanel and adds it
		this.add(cp);
		spLay.putConstraint("North", cp, 60, "North", this);
		spLay.putConstraint("West", cp, 20, "West", this);
	}

	/**
	 * This class handles all the events triggered by the pressing of any button
	 * in the CrosswordPanel.
	 * 
	 * @author mak1g11
	 * 
	 */
	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			int returnVal;
			// code works if the login button as pressed
			if (e.getSource() == login) {
				logged = true; // logged is set to true
				// the buttons and the text field are now not visible. Instead,
				// the logout button and a JLabel indicating the current user
				login.setVisible(false);
				username.setVisible(false);
				logout.setVisible(true);
				cp.loggedIn(); // value of logged in CellPanel is also set to
								// true
				user.setText("Now logged in as " + username.getText());
			} else if (e.getSource() == logout) { // code executed if logout
													// button was pressed
				logged = false; // logged is set to false
				// the logout button and the JLabel are invisible once more and
				// the login button etc. are displayed
				logout.setVisible(false);
				login.setVisible(true);
				username.setVisible(true);
				username.setText("");
				user.setText("Enter your name:  ");
				cp.loggedOut(); // CellPanel also changes value of Logged to
								// false
			} else if (e.getSource() == save) { // code executed if save button
												// pressed
				if (logged) { // will only work if the user is logged in
					returnVal = jfc.showSaveDialog(null);
					File f = jfc.getSelectedFile(); // JFileChooser prompts user
													// to choose a name and
													// decide to save
					jfc.setAcceptAllFileFilterUsed(false);
					try {
						if (returnVal == JFileChooser.APPROVE_OPTION) { // works
																		// if
																		// the
																		// user
																		// decides
																		// to
																		// save
							File outputfile = new File(f.getPath() + ".cwd"); // a
																				// unique
																				// file
																				// type
																				// is
																				// added
																				// to
																				// the
																				// file
							CrosswordIO.writePuzzle(c, outputfile); // crosswordIO
																	// writes
																	// the file
							JOptionPane.showMessageDialog(
									cp, // if it is successful, a message is
										// generated to confirm
									"Your file was saved.", "Success!",
									JOptionPane.PLAIN_MESSAGE);
						}
					} catch (IOException ex) { // an IOException is thrown and
												// an error message is generated
						JOptionPane
								.showMessageDialog(
										cp,
										"The file could not be saved. We're having our best people look into it.",
										"Houston, we have a probelm",
										JOptionPane.WARNING_MESSAGE);
						ex.printStackTrace();
					}

				} else { // if the person is not logged in, he/she is not
							// allowed to save
					JOptionPane.showMessageDialog(cp, "Please log in first!",
							"Error!", JOptionPane.ERROR_MESSAGE);
				}
			} else if (e.getSource() == load) { // code executes if the load
												// button is pressed
				jfc.setAcceptAllFileFilterUsed(false);
				returnVal = jfc.showOpenDialog(null);
				File f = jfc.getSelectedFile(); // file chosen by the user
				try {
					// this is performed if the user opts to open a file
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						c = CrosswordIO.readPuzzle(f);
						// it is checked if the file selected to be opened is
						// the right format or not
						if ((f.getName().endsWith(".cwd"))) {
							getNewCrossword();
						}
						// if the file format is incorrect, an error message is
						// generated
						else {
							JOptionPane
									.showMessageDialog(
											cp,
											"The file could not opened. The image you selected was probably not a proper \n"
													+ "CWD format file. Shame on you!",
											"Houston, we have a probelm",
											JOptionPane.WARNING_MESSAGE);
						}
					}

				}
				// if there is an error in loading the file, an exception is
				// thrown
				catch (IOException ioe) {
					JOptionPane
							.showMessageDialog(
									cp,
									"The file could not opened. We're having our best people look into it.",
									"Houston, we have a probelm",
									JOptionPane.WARNING_MESSAGE);
				} catch (ClassNotFoundException cnfe) { // if the class is not
														// found
					System.out.println("Crossword class was not found");
					cnfe.printStackTrace();
				}
			} else if (e.getSource() == anagrams) { // code executes if the
													// anagram button is pressed
				AnagramFrame af = new AnagramFrame("Find an anagram"); // an
																		// AnagramFrame
																		// is
																		// created
																		// and
																		// intiated
				af.init();
				af.setLocationRelativeTo(null);
			}
		}

	}

	/**
	 * This class handles all the events triggered by the JLists.
	 * 
	 * @author mak1g11
	 * 
	 */
	class ListListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			if (e.getSource() == acrossList) { // code is executed if the
												// selection was in acrossList
				if (!(acrossList.isSelectionEmpty())) { // checked if the
														// selection is empty or
														// not
					downList.clearSelection(); // any selection in the other
												// list is cleared
					String s = (String) acrossList.getSelectedValue(); // the
																		// string
																		// contained
																		// by
																		// the
																		// value
																		// is
																		// stored
																		// in a
																		// variable
					Clue c = acMap.get(s); // the corresponding clue is
											// retrieved via a HashMap
					cp.highlightClue(c); // the CellPanel highlights the cells
											// corresponding to the clue
				}
			} else if (e.getSource() == downList) { // code is executed if the
													// selection was in downList
				if (!(downList.isSelectionEmpty())) { // checked if the
														// selection is empty or
														// not
					acrossList.clearSelection(); // any selection in the other
													// list is cleared
					String s = (String) downList.getSelectedValue(); // the
																		// string
																		// contained
																		// by
																		// the
																		// value
																		// is
																		// stored
																		// in a
																		// variable
					Clue c = dwMap.get(s); // the corresponding clue is
											// retrieved via a HashMap
					cp.highlightClue(c); // the CellPanel highlights the cells
											// corresponding to the clue
				}
			}
		}

	}

	/**
	 * This class handles the events triggered by the JCheckbox.
	 * 
	 * @author mak1g11
	 * 
	 */
	class ClueSupport implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			if (logged) { // checked if a user is logged on or not
				if (clueSupport.isSelected()) { // checked if the checkbox has
												// been set to true or false
					retrieveLetters(); // the retrieveLetters methods is called
					setUpSupport();
					clueScroller.setVisible(true); // textarea is set visible
				} else if (!(clueSupport.isSelected())) { // if checkbox is
															// unselected
					clueScroller.setVisible(false); // text area is invisible
													// again
				}

			} else { // error message if no user logged in
				JOptionPane.showMessageDialog(cp, "Please log in first!",
						"Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}

class CrosswordExample {

	Crossword getPuzzle() {

		Crossword c;
		String title = "A puzzle";
		int size = 11;
		ArrayList<Clue> acrossClues = new ArrayList<Clue>();
		ArrayList<Clue> downClues = new ArrayList<Clue>();

		acrossClues.add(new Clue(1, 1, 0, "Eager Involvement", "enthusiasm"));
		acrossClues.add(new Clue(8, 0, 2, "Stream of water", "river"));
		acrossClues.add(new Clue(9, 6, 2, "Take as one's own", "adopt"));
		acrossClues.add(new Clue(10, 0, 4, "Ball game", "golf"));
		acrossClues.add(new Clue(12, 5, 4, "Guard", "sentry"));
		acrossClues.add(new Clue(14, 0, 6, "Language communication", "speech"));
		acrossClues.add(new Clue(17, 7, 6, "Fruit", "plum"));
		acrossClues.add(new Clue(21, 0, 8, "In addition", "extra"));
		acrossClues.add(new Clue(22, 6, 8, "Boundary", "limit"));
		acrossClues.add(new Clue(23, 0, 10, "Executives", "management"));
		downClues.add(new Clue(2, 2, 0, "Pertaining to warships", "naval"));
		downClues.add(new Clue(3, 4, 0, "Solid", "hard"));
		downClues.add(new Clue(4, 6, 0, "Apportion", "share"));
		downClues.add(new Clue(5, 8, 0, "Concerning", "about"));
		downClues.add(new Clue(6, 10, 0, "Friendly", "matey"));
		downClues.add(new Clue(7, 0, 1, "Boast", "brag"));
		downClues.add(new Clue(11, 3, 4, "Enemy", "foe"));
		downClues.add(new Clue(13, 7, 4, "Doze", "nap"));
		downClues.add(new Clue(14, 0, 6, "Water vapour", "steam"));
		downClues.add(new Clue(15, 2, 6, "Consumed", "eaten"));
		downClues.add(new Clue(16, 4, 6, "Loud, resonant sound", "clang"));
		downClues.add(new Clue(18, 8, 6, "Yellowish, citrus fruit", "lemon"));
		downClues.add(new Clue(19, 10, 6, "Mongrel dog", "mutt"));
		downClues.add(new Clue(20, 6, 7, "Shut with force", "slam"));

		c = new Crossword(title, size, acrossClues, downClues);

		return c;
	}

}

class Crossword implements Serializable {
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

/**
 * Represents a Clue
 * 
 * @author mak1g11
 * 
 */
class Clue implements Serializable {
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

/**
 * This is a panel which contains the visual representation of a Crossword
 * 
 * @author mak1g11
 * 
 */
class CellPanel extends JPanel {
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