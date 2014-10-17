import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This is a custom class to write or read Crossword objects
 * 
 * @author mak1g11
 * 
 */
public class CrosswordIO {
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