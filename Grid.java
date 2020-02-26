package coursework;
import java.util.Scanner;
import java.io.Serializable;

public class Grid implements Serializable {

	//serialization used to save a copy of the Grid to a file to be able to open saved games
	//this is done in the saveGame and startSavedGame methods
	private static final long serialVersionUID = 1L;

	//declare these basic variables as public so they can be called by every method in the project
	public static final int NUM_COLUMNS= 7; 
	public static final int NUM_ROWS= 6;
	public static final int NUM_CONNECT= 4;

	public Disc[][] gameGrid;
	public char p1colour;
	public char p2colour;
	public char currentPlayerColour;

	//let 'c' represent the column index and 'r' represent the row index
	//this means from now on any position in the grid can be represented as gameGrid [r] [c]

	//constructor 
	public Grid() {

		gameGrid = new Disc[NUM_ROWS][NUM_COLUMNS];

	}//end constructor

	public Disc[][] getGameGrid() {
		return this.gameGrid;
	}

	public void setGameGrid(Disc[][] gameGrid) {
		this.gameGrid = gameGrid;
	}

	//method to place a disc at a position [r][c] in the grid.
	public void addDiscToGrid(int r, int c, Disc disc) {
		gameGrid [r][c] = disc;
	}

	//method for displaying the grid to players
	public void printGrid() {
		/* PRINT DESCRIPTION:
		 this method adds an initial vertical dividing line and then loops across the grid and adds a dividing line 
		 after each column, before adding a final horizontal line to form the base of the grid
		 The method replaces a 'null' value with a blank space
		 */
		System.out.println("|1 2 3 4 5 6 7|");
		for(int r  = 0; r < NUM_ROWS; r++) {

			for(int c = 0; c < (NUM_COLUMNS); c++) {

				if(c==0) {
					System.out.print("|");
				}
				if(gameGrid [r][c] == null) {
					System.out.print(" |");
				}else {
					char colour = gameGrid[r][c].getColour();
					System.out.print(colour + "|");}

			}//end column loop

			System.out.println();	
		}//end row loop

		System.out.println("---------------");

	}//end print method

	//method to prompt for user input to select column
	public int selectColumn(){

		System.out.println("Please enter a column number to drop your Disc into, or enter 0 to quit game :");

		//added a warning suppression to the scanner as the code leaves the scanner open throughout.
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);

		//prompt for user input 
		int input = scan.nextInt();

		//validation loop to check column number is valid
		while (input > NUM_COLUMNS || input < 0)
		{
			System.out.println("Please choose a valid column number, between 1 and " +NUM_COLUMNS+ ".\nTo quit game, enter 0.");
			input = scan.nextInt();
		}

		//subtract -1 from the input to get the column index starting from 0.
		return (input-1);

	}//end selectColumn method

	//method to prevent player from adding disc to full column
	public int countColumn(int c) {
		//counter counts how many non-null elements there are in a selected column
		//returns either number of elements

		int counter=0;
		for(int r  = 0; r < NUM_ROWS; r++) {
			if(gameGrid[r][c]!= null) {
				counter++;
			}
		}//end row loop

		return counter;

	}//end countColumn 

	//method to return row number once a column is chosen
	public int findRow(int c) {
		/*if loop uses countColumn method to see how many Discs are in the selected column
		 * adds Disc at index NUM_ROWS if the column is empty (index 0 is at top of grid)
		 * adds Disc at index NUM_ROWS-number of elements in column -1
		 * where the -1 is to take into account that the indexes start at 0, not 1. */

		if (countColumn(c) == 0){
			return (NUM_ROWS-1);
		} else {
			return (Grid.NUM_ROWS - countColumn(c)-1);
		}//end if else loop

	}//end findRow

	//method to end the game when the grid is full and reset to start menu
	public void gridFull() {
		//loops across each column in grid and checks whether countColumn = NUM_ROWS
		//counter counts number of columns which are full
		//if all columns are full then returns message and ends game

		int counter = 0;
		for(int c=0; c < NUM_COLUMNS; c++) {
			if(countColumn(c) == NUM_ROWS) {
				counter++;
			}
		}

		if(counter == NUM_COLUMNS) {
			System.out.println("No more moves! The game has ended in a draw.");
			System.exit(0);
		}

		return;
	}//end gridFull

	//this method compares 4 discs, and returns boolean true if they are equal.
	private boolean checkDiscs(Disc d1, Disc d2, Disc d3, Disc d4) {
		//check aren't null, to ensure that getColour method works
		//check that getColour is equal for all Discs

		boolean check = false;
		if((d1!=null && d2!=null && d3!=null && d4!=null) 
				&& (d1.getColour()==d2.getColour()) 
				&& (d2.getColour()==d3.getColour()) 
				&& (d3.getColour()==d4.getColour())) {
			check = true;
			return check;
		}

		return check;
	}//end checkDiscs

	//this method detects a win using 3 separate win methods- row, column, diagonal.
	public boolean win() {
		if(rowWin() || columnWin() || diagonalWin()) {
			return true;

		}else {
			return false;
		}

	}//end win method

	//checks rows for a connect4
	private boolean rowWin() {
		//method looks at each row, and checks whether each set of 4 consecutive discs in the row are equal

		//c<=NUM_COLUMNS-NUM_CONNECT because we are choosing discs which work across a row consecutively
		//so the highest number column we can start with is 4 columns from the end of the grid

		for(int r=0; r<NUM_ROWS; r++) {
			for(int c = 0; c <= (NUM_COLUMNS-NUM_CONNECT); c++) {
				if(checkDiscs(gameGrid[r][c], gameGrid[r][c+1], gameGrid[r][c+2], gameGrid[r][c+3])) {
					return true;
				}
			}	
		}//end loop

		return false;

	}//end rowWin

	//checks columns for connect4
	private boolean columnWin() {
		//similar to rowWin, except looping across columns and rows has swapped

		//r<NUM_ROWS-3 because we choose are choosing discs which build upwards consecutively
		for(int c=0; c<NUM_COLUMNS; c++) {
			for(int r=0; r<=(NUM_ROWS-NUM_CONNECT); r++) {
				if(checkDiscs(gameGrid[r][c], gameGrid[r+1][c], gameGrid[r+2][c], gameGrid[r+3][c])) {
					return true;
				}

			}	
		}//end loop

		return false;

	}//end columnWin

	//EXTRA: checks for diagonal win
	private boolean diagonalWin() {
		//in two parts- a diagonal line with a positive slope
		//and a diagonal with a negative slope

		//step 1- negative sloped diagonal win
		for(int c=0; c<=(NUM_COLUMNS-NUM_CONNECT); c++) {
			for(int r=0; r<=(NUM_ROWS-NUM_CONNECT); r++) {
				if(checkDiscs(gameGrid[r][c], gameGrid[r+1][c+1], gameGrid[r+2][c+2], gameGrid[r+3][c+3])) {
					return true;
				}
			}
		}//end diagonal 1 loop

		//step 2 positive sloped diagonal win
		//we start the row loop at NUM_CONNECT-1 since the row index starts at 0 rather than 1
		//r>4 because we need 4 consecutive discs
		for(int c=0; c<=(NUM_COLUMNS-NUM_CONNECT); c++) {
			for(int r=(NUM_CONNECT-1); r<NUM_ROWS; r++) {
				if(checkDiscs(gameGrid[r][c], gameGrid[r-1][c+1], gameGrid[r-2][c+2], gameGrid[r-3][c+3])) {
					return true;
				}
			}
		}//end diagonal 2 loop

		//if neither diagonal returns a win
		return false;
	}//end diagonalWin


}//end class