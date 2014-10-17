import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This class makes a Crossword panel which is to contain the crossword and all
 * the other buttons and things.
 * 
 * @author mak1g11
 * 
 */
public class CrosswordPanel extends JPanel {
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