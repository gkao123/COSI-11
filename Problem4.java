//Name: Geoffrey Kao
//Email: gkao123@brandeis.edu
//Date: 20 Nov 2016
//PA #4- This program allows a user to play tic-tac-toe against a computer.
//Bugs: None

import java.util.*;
public class Problem4{
	public static void main(String [] args){
		System.out.println("Welcome to tic-tac-toe.");
		game();
	}
	//method game is the actual game structure
	public static void game(){
		Scanner console = new Scanner(System.in);
		int check = 0;
		String userResponse2 = "";
		//I run a dowhile loop so the game keeps running if the user wants to play again
		do{
			//moveCount keeps track of the number of moves in one game
			//turnCheck keeps track of whether is the players turn/computers turn
			int moveCount = 0;
			int turnCheck = 0;
			//currentState is the array of the currentState of the game
			String[][] currentState = initialPic();
			//method display displays the array so the user has a visual of the game
			display(currentState);
			do{	
			//asks the user if it wants to go first and keeps asking into the user enters in a word the computer recognizes
				System.out.println("Do you want to go first?");
				String userResponse = console.next();
				if (userResponse.equalsIgnoreCase("YES")){
					turnCheck = 1;
					check = 0;
				}
				else if (userResponse.equalsIgnoreCase("NO")){
					turnCheck = 2;
					check = 0;
				}
				else{
					System.out.println("The computer did not understand your input. Please try again.");
					check = 1;
				}
			} while (check == 1);
			//gameRounds plays the alternating rounds between the player and computer
			gameRounds(currentState, turnCheck);
			//gives the user the option to play again; if the computer doesn't recognize the user input it will repeat question
			do {
				check = 0;
				System.out.println("Do you want to play again?");
				userResponse2 = console.next();
				if (!userResponse2.equalsIgnoreCase("NO")&&!userResponse2.equalsIgnoreCase("YES")){
					System.out.println("Sorry the computer did not understand your input. Please try again.");
					check = 1;
				}
			} while (check == 1);
			if (userResponse2.equalsIgnoreCase("NO")){
				check = 1;
			}
		} while (check == 0);
		
	}
	//intialPic is the empty tic-tac-toe board represented by a five by five array
	//I use several forloops for brevity
	public static String[][] initialPic(){
		String [][] initialPic = new String[5][5];
		for(int j=0; j <=4; j+=2){
			for(int i= 1; i <= 3; i+=2){
				initialPic[j][i] = "|";
			}
		}
		for(int j=1; j<=3; j+=2){
			for(int i= 0; i<=4; i+=2){
				initialPic[j][i] = "-";
			}
		}
		for(int j=0; j<=4; j+=2){
			for(int i= 0; i <= 4; i+=2){
				initialPic[j][i] = " ";
			}
		}
		for(int j=1; j<=3; j+=2){
			for(int i=1; i<=3; i+=2){
				initialPic[j][i] = " ";
			}
		}
		return initialPic;
	}
	//method display displays the array so that the user can understand what is going on
	public static void display(String[][] currentState){
		for (int j=0; j<=4; j++){
			for (int i=0; i<=4; i++){
				System.out.print(currentState[j][i]);
			}
			System.out.println();
		}
	}
	//method gameRounds plays the actual game
	public static void gameRounds(String[][] currentState, int turnCheck){
		int moveCount = 0;
		//once moveCount becomes 9 that means the game is over as the tic tac board is filled
		//turnCheck represents whose turn is it
		while (moveCount < 9){
			if (turnCheck == 2){
				//computerMove is the method which returns the computers move
				//the program then displays the move, increments moveCount, and swithces turnCheck into 1 to signal player's turn
				computerMove(currentState);
				display(currentState);
				moveCount++;
				turnCheck = 1;
			}
			else{
				//the format of the player's turn follows the computer turn's format
				playerMove(currentState);
				display(currentState);
				moveCount++;
				turnCheck = 2;
			}
			//method checkWin checks if either the either the computer or player has won after each's turn and returns an integer value
			//based on the value of checkWin, the program knows if either has won
			int checkWin = checkWin(currentState);
			if (checkWin == 0 || checkWin == 2){
				if (checkWin == 2){
					System.out.println("You have won. Game over.");
				}
				else {
					System.out.println("Computer has won. Game over");
				}
				//moveCount becomes 10 if either wins to distinguish between when there is a tie
				moveCount = 10;
			}
			//game ends once a tie occurs
			if (moveCount == 9){
				System.out.println("Game is tied. Game over.");
			}
		}
	}
	//playerMove asks for and if a valid response, stores the value of the player's move
	public static void playerMove(String[][] currentState){
		Scanner console = new Scanner(System.in);
		System.out.println("Your turn. Choose a move.");
		int check = 0;
		int row = 0;
		int column = 0;
		//I set a do while so that if the user enters in an invalid move, the program asks again
		do{
			check = 0;
			System.out.println("What row?");
			row = console.nextInt();
			System.out.println("What column?");
			column = console.nextInt();
			//two possibilities for invalid moves: 1. a move that doesn't exist 2. a move that has already been played
			if (((row > 3)||row < 0)||((column > 3 || column < 0))){
				System.out.println("You did not choose a valid move. Please try again.");
				check = 1;
			}
			if (check == 0){
				//checkGuess finds if the move has been played already
				check = checkGuess(row, column, currentState);
				if (check == 1){
					System.out.println("Move was already chosen. Please try again.");
				}
			}
		} while (check == 1);
		currentState[2*row-2][2*column-2] = "X";
	}
	//computerMove is the alogirthm behind the computers move
	//computer has move "O" and will try its best to complete/block two-in-a-rows
	public static void computerMove(String[][] currentState){
		Random rand = new Random();
		System.out.println("Computer's turn.");
		System.out.println("Computer has chosen.");
		//check is set at 1 so that if condition 1 is not met, it will check condition 2, and so on
		int check = 1;
		int i= 0;
		while (i < 3){
			//this represents the horizontal check for two-in-a rows, namely situation with X, X, then empty space
			//if condition is met, it will play a move in that empty space slot then change check into 0 so that none of the other checks are checked
			if (((currentState[2*i][0].equalsIgnoreCase("X")&& currentState[2*i][2].equalsIgnoreCase("X"))&& currentState[2*i][4].equalsIgnoreCase(" "))||((currentState[2*i][0].equalsIgnoreCase("O")&& currentState[2*i][2].equalsIgnoreCase("O"))&& currentState[2*i][4].equalsIgnoreCase(" "))){
				currentState[2*i][4] = "O";
				check = 0;
				i = 3;
			}
			else{
				i++;
			}
		}
		//i is set at 0 again so that the next check can run
		i = 0;
		if (check == 1){
			while (i < 3){
				//represents horizontal check of sitaution empty space, x, x
				if (((currentState[2*i][0].equalsIgnoreCase(" ")&& currentState[2*i][2].equalsIgnoreCase("X"))&& currentState[2*i][4].equalsIgnoreCase("X"))||((currentState[2*i][0].equalsIgnoreCase(" ")&& currentState[2*i][2].equalsIgnoreCase("O"))&& currentState[2*i][4].equalsIgnoreCase("O"))){
					currentState[2*i][0] = "O";
					check = 0;
					i = 3;
				}
				else{
					i++;
				}
			}
		}
		i=0;
		if (check == 1){
			while (i < 3){
				//represents horizontal check of situation X, empty space, X
				if (((currentState[2*i][0].equalsIgnoreCase("X")&& currentState[2*i][2].equalsIgnoreCase(" "))&& currentState[2*i][4].equalsIgnoreCase("X"))||((currentState[2*i][0].equalsIgnoreCase("O")&& currentState[2*i][2].equalsIgnoreCase(" "))&& currentState[2*i][4].equalsIgnoreCase("O"))){
					currentState[2*i][2] = "O";
					check = 0;
					i = 3;
				}
				else{
					i++;
				}
			}
		}
		i=0;
		if (check == 1){
			while (i < 3){
				//represents vertical check of situation X, X, empty space
				if (((currentState[0][2*i].equalsIgnoreCase("X")&& currentState[2][2*i].equalsIgnoreCase("X"))&& currentState[4][2*i].equalsIgnoreCase(" "))||((currentState[0][2*i].equalsIgnoreCase("O")&& currentState[2][2*i].equalsIgnoreCase("O"))&& currentState[4][2*i].equalsIgnoreCase(" "))){
					currentState[4][2*i] = "O";
					check = 0;
					i = 3;
				}
				else{
					i++;
				}
			}
		}
		i=0;
		if (check == 1){
			while (i < 3){
				//represents vertical check of situation empty space, X, X
			if (((currentState[0][2*i].equalsIgnoreCase(" ")&& currentState[2][2*i].equalsIgnoreCase("X"))&& currentState[4][2*i].equalsIgnoreCase("X"))||((currentState[0][2*i].equalsIgnoreCase(" ")&& currentState[2][2*i].equalsIgnoreCase("O"))&& currentState[4][2*i].equalsIgnoreCase("O"))){
					currentState[0][2*i] = "O";
					check = 0;
					i = 3;
				}
				else{
					i++;
				}
			}
		}
		i=0;
		if (check == 1){
			while (i < 3){
				//represents vertical check of situation X, empty space, X
				if (((currentState[0][2*i].equalsIgnoreCase("X")&& currentState[2][2*i].equalsIgnoreCase(" "))&& currentState[4][2*i].equalsIgnoreCase("X"))||((currentState[0][2*i].equalsIgnoreCase("O")&& currentState[2][2*i].equalsIgnoreCase(" "))&& currentState[4][2*i].equalsIgnoreCase("O"))){
					currentState[2][2*i] = "O";
					check = 0;
					i = 3;
				}
				else{
					i++;
				}
			}
		}
		//the following are all the diagonal checks
		if (check == 1){
			//diagonal check from up to down of X, X, empty space
			if (((currentState[0][0].equalsIgnoreCase("X")&& currentState[2][2].equalsIgnoreCase("X"))&& currentState[4][4].equalsIgnoreCase(" "))||((currentState[0][0].equalsIgnoreCase("O")&& currentState[2][2].equalsIgnoreCase("O"))&& currentState[4][4].equalsIgnoreCase(" "))){
				currentState[4][4] = "O";
				check = 0;
			}
		}
		if (check == 1){
			//diagonal check from up to down of empty space, X, X
			if (((currentState[0][0].equalsIgnoreCase(" ")&& currentState[2][2].equalsIgnoreCase("X"))&& currentState[4][4].equalsIgnoreCase("X"))||((currentState[0][0].equalsIgnoreCase(" ")&& currentState[2][2].equalsIgnoreCase("O"))&& currentState[4][4].equalsIgnoreCase("O"))){
				currentState[0][0] = "O";
				check = 0;
			}
		}
		if (check == 1){
			//diagonal check from up to down of X, empty space, X
			if (((currentState[0][0].equalsIgnoreCase("X")&& currentState[2][2].equalsIgnoreCase(" "))&& currentState[4][4].equalsIgnoreCase("X"))||((currentState[0][0].equalsIgnoreCase("O")&& currentState[2][2].equalsIgnoreCase(" "))&& currentState[4][4].equalsIgnoreCase("O"))){
				currentState[2][2] = "O";
				check = 0;
			}
		}
		if (check == 1){
			///diagonal check from down to up of X, X, empty space
			if (((currentState[0][4].equalsIgnoreCase("X")&& currentState[2][2].equalsIgnoreCase("X"))&& currentState[4][0].equalsIgnoreCase(" "))||((currentState[0][4].equalsIgnoreCase("O")&& currentState[2][2].equalsIgnoreCase("O"))&& currentState[4][0].equalsIgnoreCase(" "))){
				currentState[4][0] = "O";
				check = 0;
			}
		}	
		if (check == 1){
			///diagonal check from down to up of empty space, X, X
			if (((currentState[0][4].equalsIgnoreCase(" ")&& currentState[2][2].equalsIgnoreCase("X"))&& currentState[4][0].equalsIgnoreCase("X"))||((currentState[0][4].equalsIgnoreCase(" ")&& currentState[2][2].equalsIgnoreCase("O"))&& currentState[4][0].equalsIgnoreCase("O"))){
				currentState[0][4] = "O";
				check = 0;
			}
		}
		if (check == 1){
			///diagonal check from down to up of X, empty space, X
			if (((currentState[0][4].equalsIgnoreCase("X")&& currentState[2][2].equalsIgnoreCase(" "))&& currentState[4][0].equalsIgnoreCase("X"))||((currentState[0][4].equalsIgnoreCase("O")&& currentState[2][2].equalsIgnoreCase(" "))&& currentState[4][0].equalsIgnoreCase("O"))){
				currentState[2][2] = "O";
				check = 0;
			}
		}
		//if none of these conditions are met, a guess is randomly generated
		if (check == 1){
			while(check == 1){
				int row = rand.nextInt(3) + 1;
				int column = rand.nextInt(3) + 1;
				//the program checks to make sure the move hasn't already been playe
				check = checkGuess(row, column, currentState);
				if (check == 0){
					currentState[2*row-2][2*column-2] = "O";
				}
			}
		}
		
	}
	//checkWin determines if either has won and returns 0, 1, 2
	//0=computer win; 2=player win; 1= no one has won
	//follows similar code structure to computerMove
	public static int checkWin(String [][] currentState){
		int check = 1;
		//check = 2 if player wins; check = 1 if computer wins
		int i= 0;
		while (i < 3){
			if ((currentState[2*i][0].equalsIgnoreCase("X")&& currentState[2*i][2].equalsIgnoreCase("X"))&& currentState[2*i][4].equalsIgnoreCase("X")){
				check = 2;
				i = 3;
			}
			else{
				i++;
			}
		}
		i = 0;
		if (check == 1){
			while (i < 3){
				if ((currentState[2*i][0].equalsIgnoreCase("O")&& currentState[2*i][2].equalsIgnoreCase("O"))&& currentState[2*i][4].equalsIgnoreCase("O")){
					check = 0;
					i = 3;
				}
				else{
					i++;
				}
			}
		}
		i = 0;
		if (check == 1){
			while (i < 3){
				if ((currentState[0][2*i].equalsIgnoreCase("X")&& currentState[2][2*i].equalsIgnoreCase("X"))&& currentState[4][2*i].equalsIgnoreCase("X")){
					check = 2;
					i = 3;
				}
				else{
					i++;
				}
			}
		}
		i=0;
		if (check == 1){
			while (i < 3){
				if ((currentState[0][2*i].equalsIgnoreCase("O")&& currentState[2][2*i].equalsIgnoreCase("O"))&& currentState[4][2*i].equalsIgnoreCase("O")){
					check = 0;
					i = 3;
				}
				else{
					i++;
				}
			}
		}
		if (check == 1){
			if ((currentState[0][0].equalsIgnoreCase("X")&& currentState[2][2].equalsIgnoreCase("X"))&& currentState[4][4].equalsIgnoreCase("X")){
				check = 2;
				i = 3;
			}
			else{
				i++;
			}
		}
		if (check == 1){
			if ((currentState[0][0].equalsIgnoreCase("0")&& currentState[2][2].equalsIgnoreCase("0"))&& currentState[4][4].equalsIgnoreCase("O")){
				check = 0;
				i = 3;
			}
			else{
				i++;
			}
		}
		if (check == 1){
			if ((currentState[0][4].equalsIgnoreCase("X")&& currentState[2][2].equalsIgnoreCase("X"))&& currentState[4][0].equalsIgnoreCase("X")){
				check = 2;
				i = 3;
			}
			else{
				i++;
			}
		}
		if (check == 1){
			if ((currentState[0][4].equalsIgnoreCase("0")&& currentState[2][2].equalsIgnoreCase("0"))&& currentState[4][0].equalsIgnoreCase("O")){
				check = 0;
				i = 3;
			}
			else{
				i++;
			}
		}
		return check;
	}
	//checkGuess checks if either computer or player has made a valid move and returns an integer
	//(0 if valid, 1 if invalid)
	public static int checkGuess(int row, int column, String[][] currentState){
		String x = currentState[2*row-2][2*column-2];
		int check = 0;
		//compares the move to the corresponding slot in the array
		if (x.equalsIgnoreCase("X")||x.equalsIgnoreCase("O")){
			check = 1;
		}
		return check;
	}
}