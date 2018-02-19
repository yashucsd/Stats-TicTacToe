package hw1;

import java.util.Scanner;
import java.util.Random;

/**
 * an intelligent console game of tic-tac-toe that keeps statistics
 *
 * @author Yash Nevatia
 * @version 1.0
 * @since 2016-09-27
 */

public class TicTacToe_Extra {

	static char[][] board = new char[3][3];
	static Scanner input = new Scanner(System.in);

	// Object of Stats class to maintain statistics
	static Stats stat = new Stats();

	/**
	 * Prints the TicTacToe board
	 * 
	 * @param arr The board so far
	 */
	public static void printBoard(char[][] arr) {
		System.out.println();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(arr[i][j]);
				if (j != 2)

					// Print the | for readable output
					System.out.print(" " + "|" + " ");
			}
			System.out.println();
			if (i != 2) {
				System.out.print("_   _   _ "); // Print _ for readability
				System.out.println();
				;
			}
		}
	}

	/**
	 * Clear the TicTacToe board before starting a new game 
	 * 
	 * @param arr The board so far
	 */
	public static void clearBoard(char[][] arr) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				arr[i][j] = ' ';
			}
		}
	}

	/**
	 * Determines if the player with the specified token wins
	 * 
	 * @param symbol Specifies whether the player is X or O
	 * @return true if player has won, false otherwise
	 */
	public static boolean isWon(char symbol) {

		for (int i = 0; i < 3; i++) // horizontal
			if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol)
				return true;

		for (int i = 0; i < 3; i++) // vertical
			if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)
				return true;

		// diagonals
		if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol)
			return true;
		if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)
			return true;

		return false;
	}

	/**
	 * Determines if the cell is occupied
	 * 
	 * @param row Row of the cell to be checked
	 * @param col Column of the cell to be checked
	 * @return true if the cell is occupied, false otherwise
	 */
	public static boolean isOccupied(int row, int col) {
		if (board[row][col] == ' ')
			return false;
		else
			return true;
	}

	/**
	 * Determines who starts the game
	 */
	public static int whoStarts() {
		if (Math.random() < 0.5)
			return 0;
		else
			return 1;
	}

	/**
	 * takes care of the human's move 1. Prompt for a cell, then column 2. Puts
	 * a symbol (X or O) on the board 3. Prints the updated board 4. If a human
	 * wins: prints, updates stats and returns true 5. If not a win yet, returns
	 * false
	 * 
	 * @param symbol X or O in use by the human
	 */
	public static boolean humanTurn(char symbol) {
		// Prompt for a cell. User must enter
		// row and column with a space in between.
		int row, col;

		while(true){			
			System.out.print("\n\nEnter your move: (row column): ");
			row = input.nextInt();
			col = input.nextInt();

			if (!isOccupied(row, col))
				break;
			else 
				System.out.println("This spot is occupied, please try again.");
		}

		board[row][col] = symbol;

		printBoard(board);

		if (isWon(symbol)) {
			stat.incrementUserWins();
			return true;
		}

		return false;
	}

	/**
	 * takes care of the computer's move 1. Generates numbers until finds an
	 * empty cell 2. Puts a symbol (X or O) on the board 3. Prints the updated
	 * board 4. If a comp wins: prints, updates stats and returns true 5. If not
	 * a win yet, returns false
	 * 
	 * @param symbol X or O in use by computer
	 */
	public static boolean compTurn(char symbol) {

		int row, col;
		
		row = 2;
		col = 2;

		System.out.println("\n\nMy Move is: " + row + " " + col);

		board[row][col] = symbol;

		printBoard(board);

		if (isWon(symbol)) {
			stat.incrementComputerWins();
			return true;
		}

		return false;

	}

	/**
	 * If human goes first: We have 9 moves in total (max). 8 moves will be in a
	 * loop and the last human move is outside of the loop: 1. human goes first,
	 * with a X 2. If the returned value is true (human won), then boolean
	 * flag=true and we break out of the loop. done indicates that the game is
	 * over. 3. If the game is not over, then it is computer's turn. 4. If the
	 * returned value is true (comp won), then boolean flag=true and we break
	 * out of the loop. done indicates that the game is over 5. Repeat the two
	 * steps above 3 more times. 6. If the done is still false, then a human
	 * performs one more move and we check if the move led to the win or tie.
	 */
	public static void humanFirst() {
		boolean done = false;

		for (int i = 0; i < 4; i++) {
			if (humanTurn('X')) {
				done = true;
				break;
			}
			if (compTurn('O')) {
				done = true;
				break;
			}
		} // end of for loop;
		if (!done) {
			if (!humanTurn('X')) {
				System.out.println("\n\nA tie!");
				stat.incrementTies();
			}
		}
	}

	/**
	 * Same logic as above, only the first computer's move happens before the
	 * loop. We do not need to check for winning combination here, since comp
	 * can't win after one move. After the loop we check if the game is done. If
	 * not, report a tie and update statistics.
	 */
	public static void compFirst() {
		compTurn('X');

		boolean done = false;

		for (int i = 0; i < 4; i++) {
			if (humanTurn('O')) {
				done = true;
				break;
			}
			if (compTurn('X')) {
				done = true;
				break;
			}
		} // end of for loop;
		if (!done) {
			System.out.println("\n\nA tie!");
			stat.incrementTies();
		}

	}

	public static void main(String[] args) {

		// input from the user, if he wants to play another game
		String playAgain = "";

		// input from the user, if he wants to clear stats
		String clearStats = "";

		do { // play until 'n' is pressed
			
			clearBoard(board); // clear the baord

			// Generate Random Assignment, determines who goes first;
			int move = whoStarts();
			if (move == 0) {
				System.out.println("\nI start first. I choose X and you get O");
				compFirst();
			} else {
				System.out.println("\nYou start first. You get X and I get O");
				humanFirst();
			}

			stat.printStats();

			System.out.println("Would you like to play again?");
			playAgain = input.next();

			if (playAgain.charAt(0) == 'y') {
				System.out.println("Should I clear statistics?");
				clearStats = input.next();

				if (clearStats.charAt(0) == 'y')
					stat.reset();
			}

		} while (playAgain.charAt(0) != 'n'); // done with the outer loop

		System.out.println("\nBye, see you later!");
	}
}
