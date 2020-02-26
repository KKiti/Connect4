package coursework;

import javax.swing.JOptionPane;
import javax.swing.JFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Main {
	/*
	 CONNECT 4 GAME
	 by 40429874
	 for Software Development 1, SET11102
	 */

	/* PSEUDOCODE FOR BASIC GAMEPLAY:
	 0. Prompt for new game or saved game
	 1. assign colours
	 2. create empty grid
	 3. let player choose column
	 4. add disc to column
	 5. check for wins
	   a. rows
	   b. columns
	   c. diagonals
	 6. check for full grid
	 7. display resulting grid
	 8. swap player
	 9. repeat steps 3-7
	 */

	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		
		//prompt for saved game, if true, then start savedGame method
		Boolean openSavedGame = startMenu();
		Grid gameGrid = new Grid();

		if (openSavedGame) {
			//run previously saved game
			gameGrid = startSavedGame();
		} else {
			//if a saved game isn't opened, continue by running a new game
			//initialise game grid variables
			//set game to start with Player 1

			gameGrid.p1colour = Disc.player1colour();
			gameGrid.p2colour = Disc.player2colour();
			gameGrid.currentPlayerColour = gameGrid.p1colour;

			//validation loop to check p1colour is not the same as p2colour
			while (gameGrid.p1colour == gameGrid.p2colour) {
				JOptionPane.showMessageDialog(frame, "Player 2, please choose a different colour to Player 1!", "Connect 4", JOptionPane.ERROR_MESSAGE);
				gameGrid.p2colour = Disc.player2colour();
			}

		}

		boolean firstTurn = true;
		//do while loop to run game
		do {
			//since we start with player 1, we don't want to change players before the first go.
			if(!firstTurn) {
				gameGrid.currentPlayerColour= changePlayer(gameGrid.currentPlayerColour, gameGrid.p1colour, gameGrid.p2colour);	
			}
			//start by printing the grid
			gameGrid.printGrid();
			
			//prompt player for turn
			if(gameGrid.currentPlayerColour == gameGrid.p1colour) {
				System.out.println("Player 1, it's your turn:");
			}else {
				System.out.println("Player 2, it's your turn:");
			}

			addDisc(gameGrid.currentPlayerColour, gameGrid);
			
			//check for full grid
			gameGrid.gridFull();
			
			firstTurn = false;
		}
		while(!gameGrid.win());

		//print winning result
		if(gameGrid.win()) {
			gameGrid.printGrid();
			if(gameGrid.currentPlayerColour == gameGrid.p1colour) {
				System.out.println("Game Over. Player 1 is the winner!");
			}else {
				System.out.println("Game Over. Player 2 is the winner!");
			}//end if loop	
			System.exit(0);
		}//end win loop

	}//end main

	//method to prompt user for saved game- returns boolean true if user wants to reload a saved game
	public static boolean startMenu() {
		
		JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		
		Object[] options = {"Begin New Game", "Reload Saved Game", "Exit"};
		int n = JOptionPane.showOptionDialog(frame, 
				"Welcome to Connect 4, please choose an option",
				"Connect 4", 
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE, 
				null,     
				options, 
				options[0]); 

		if (n==1) {
			return true; 
		} else if (n==2) {
			System.exit(0);
		}
		
		return false;
	}//end startMenu method

	//method to open previously saved game
	public static Grid startSavedGame() {
		
		JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		
		//either opens saved file or displays error message 
		try {
			FileInputStream file = new FileInputStream(new File("connect4SavedGame.txt"));
			ObjectInputStream objects = new ObjectInputStream(file);

			// Read objects
			Grid gameGrid = (Grid) objects.readObject();

			objects.close();
			file.close();

			//method returns the Grid which has been deserialized from the file
			return gameGrid;

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "Sorry, no saved game exists!", "Connect 4", JOptionPane.ERROR_MESSAGE);
			startMenu();
		} catch (IOException e) {
			startMenu();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}//end try

		//method needs a Grid return, so add in a dummy return of an empty Grid
		return new Grid();

	}//end savedGame method

	//method to allow player to input disc in specified column 
	public static void addDisc(char currentPlayerColour, Grid gameGrid) {
		Disc disc = new Disc();
		disc.setColour(currentPlayerColour);

		int c = gameGrid.selectColumn();

		//if the user has input 0, re-direct them to the end game menu
		while (c < 0) {
			//if user has quit the game, we won't return to this method.
			//if user has decided to cancel leaving the game, we repeat back to them choosing a column and continue
			endGame(gameGrid);
			c = gameGrid.selectColumn();
		}//end loop dealing with game quit

		//otherwise proceed with finding the row index of the disc
		int r = 0;

		//validation loop to see if column is full, or to continue and find row index
		while(gameGrid.countColumn(c) == Grid.NUM_ROWS) {
			System.out.println("This column is full, please choose a different column.");
			c = gameGrid.selectColumn();
		}

		r = gameGrid.findRow(c);

		gameGrid.addDiscToGrid(r, c, disc);

	}//end addDisc method

	//method to swap between 2 players
	public static char changePlayer(char currentPlayerColour, char p1colour, char p2colour) {
		//checks what colour the last turn taken was, and swaps to other colour.
		if(currentPlayerColour == p1colour) {
			currentPlayerColour = p2colour;
		}else {
			currentPlayerColour = p1colour;
		}

		return currentPlayerColour;
	}//end changePlayer method

	//method which is called when a user wants to finish a game before a result
	public static void endGame(Grid gameGrid) {
		
		JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		
		Object[] options = {"Save Game", "Quit without Saving", "Cancel"};

		//add a JFrame to ensure that the JOptionPane window appears on top of the Java App
		//JFrame jf=new JFrame();
		//jf.setAlwaysOnTop(true);	
		int n = JOptionPane.showOptionDialog(frame, 
				"Do you want to save your Game before going?",
				"Connect 4", 
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE, 
				null,     
				options, 
				options[0]); 

		if(n==0) {
			//case if Save Game chosen
			saveGame(gameGrid);
			System.exit(0);
		} else if (n==1) {
			// case if Quit without Saving chosen
			// end game and exit application
			System.exit(0);
		} else {
			//case if cancel is pressed we return to the addDisc method
			return;
		}//end if/else	

	}//end endGame method

	//method to save gameGrid as a text file 
	public static void saveGame(Grid gameGrid) {
		//uses a file to save a list of objects, which in this case is simply the gameGrid

		JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		
		try 
		{
			FileOutputStream file = new FileOutputStream(new File("connect4SavedGame.txt"));
			ObjectOutputStream objects = new ObjectOutputStream(file);	

			//adding the objects

			objects.writeObject(gameGrid);

			objects.close();
			file.close();

			//display message to say file has been saved
			JOptionPane.showMessageDialog(frame, "Game Saved", "Connect 4", JOptionPane.INFORMATION_MESSAGE);

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}//end saveGame
	
}//end Main.java class
