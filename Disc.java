package coursework;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.Serializable;

public class Disc implements Serializable {
	private static final long serialVersionUID = 1L;
	

	private char colour;

	//constructor 
	public Disc() {
		print(colour);

	}

	public void print(char c) {
		System.out.print(c);
	}

	public void setColour(char colour) {
		this.colour= colour;
	}

	public char getColour() {
		return this.colour;
	}

	public static char player1colour() {

		JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		
		Object[] possibilities = {"Red", "Yellow", "Green", "Blue"};
		String player1colour = (String)JOptionPane.showInputDialog(
				frame, 
				"Player 1, please choose your colour.", 
				"Connect 4", 
				JOptionPane.PLAIN_MESSAGE, 
				null, 
				possibilities, 
				"Red");
		char p1colour = player1colour.charAt(0);
		return p1colour;

	}//end player1colour method

	public static char player2colour() {
		
		JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		
		Object[] possibilities = {"Red", "Yellow", "Green", "Blue"};
		String player2colour = (String)JOptionPane.showInputDialog(
				frame, 
				"Player 2, please choose your colour.", 
				"Connect 4", 
				JOptionPane.PLAIN_MESSAGE, 
				null, 
				possibilities, 
				"Red");
		char p2colour = player2colour.charAt(0);
		return p2colour;

	}//end player2colour method

}
