import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main{
	//number of queens and size of the board
	public static int SIZE;
	//the 8x8 board
	private int[][] board;
	//positions of the queens
	private int[] queenPos;

	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the length of the board: ");
		SIZE = sc.nextInt();
		System.out.println();
		
		int c;
		System.out.println("1.Steepest Ascent");
		System.out.println("2.Sideways");
		System.out.println("3.Random Restart without Sideways");
		System.out.println("4.Random Restart with Sideways");
		System.out.println("Enter your choice:");
		c = sc.nextInt();
		
		
		switch(c){
		case 1:	
			System.out.println("********** Steepest Ascent **********");
			steepestAscent();
			System.out.println();
			break;
		case 2:
			System.out.println("********** Sideways Move **********");
			sidewaysMove();
			System.out.println();
			break;
		case 3:
			System.out.println("********** Random Restart **********");
			randomRestart();
			System.out.println();
			break;
		case 4:
			System.out.println("********** Random Restart w/ Sideways Move **********");
			randomRestartAndSideways();
			System.out.println();
			break;
		}
		sc.close();
	}
	
	public static void steepestAscent(){
		Random r = new Random();
		int[][] arr1 = new int[SIZE][SIZE];
		int success = 0;
		int r1 = r.nextInt(50);
		int r2 = r1 + 1;
		int r3 = r2 + 1;
		int successSteps = 0;
		int totalSuccessSteps = 0;
		int totalFailSteps = 0;
		int failSteps = 0;
		
		//100 Iterations
		for(int i1 = 0; i1 < 100; i1++){
			boolean check = true;
			Main board = new Main(new int[SIZE][SIZE], new int[SIZE]);
			board.queensOnBoard();
			int h = board.conflicts();
			successSteps = 0;
			failSteps = 0;
			
			if(i1 == r1 || i1 == r2 || i1 == r3){
				System.out.println("NEW BOARD: ");
				board.displayBoard();
				System.out.println("Heuristic: " + board.conflicts());
				System.out.println();
			}
			
			while(check){
				//getting the heuristic of the complete board
				h = board.conflicts();
				for(int i = 0; i < board.board.length; i++){
					board.changeQueen(i);
					
					for(int j = 0; j < board.board.length; j++){
						board.placeQueen(j, i);
						arr1[j][i] = board.conflicts();
						board.removeQueen(j, i);
					}

					board.replaceQueen(i);
				}
				
				int l2 = 99;
				
				//lowest heuristic
				for(int i = 0; i < board.board.length; i++){
					for(int j = 0; j < board.board.length; j++){
						if(arr1[j][i] < l2){
							l2 = arr1[j][i];
						}
					}
				}
				
				ArrayList<Integer> row = new ArrayList<Integer>(SIZE*SIZE); 
				ArrayList<Integer> col = new ArrayList<Integer>(SIZE*SIZE);
				
                //Getting the Position of Lowest heuristic
				for(int i = 0; i < board.board.length; i++){
					for(int j = 0; j < board.board.length; j++){
						if(arr1[j][i] == l2){
							row.add(j);
							col.add(i);
						}
					}
				}
				
				//Picking a random Position from the arraylist of all lowest heuristic
				int rand1 = r.nextInt(row.size());
				int row1 = row.get(rand1);
				int col1 = col.get(rand1);

				board.changeQueen(col1);
				board.placeQueen(row1, col1);
				board.queenPos[col1] = row1;
				int l = board.conflicts();
				
				//Check the success state for the changed board
				if(l == 0){
					success++;
					h = 0;
					check = false;
					
					if(i1 == r1 || i1 == r2 || i1 == r3){
						board.displayBoard();
						System.out.println("Heuristic: " + board.conflicts());
						System.out.println("Success!");
						System.out.println();
					}
					
					successSteps++;
					totalSuccessSteps += successSteps;
				}
				
				//check the heuristic of current and previous board
				if(l < h){
					h = board.conflicts();
					check = true;
					
					if(i1 == r1 || i1 == r2 || i1 == r3){
						board.displayBoard();
						System.out.println("Heuristic: " + board.conflicts());
						System.out.println();
					}
					
					successSteps++;
					failSteps++;
				} else {
					totalFailSteps += failSteps;
					check = false;
				}		
			}	
		}

		System.out.println("Average Steps for Success: " + (float)(totalSuccessSteps / success));
		System.out.println("Average Steps for Failure: " + (float)(totalFailSteps / (100 - success)));
		System.out.println("Success Rate: " + (success / 100.0) * 100.0);
		System.out.println("Failure Rate: " + (100.0 - (success / 100.0) * 100.0));
	}
	
	public static void sidewaysMove(){
		Random r = new Random();
		int[][] arr1 = new int[SIZE][SIZE];
		int success = 0;
		int r1 = r.nextInt(50);
		int r2 = r1 + 1;
		int r3 = r2 + 1;
		int totalSuccessSteps = 0;
		int successSteps = 0;
		int totalFailSteps = 0;
		int failSteps = 0;
		
		for(int t = 0; t < 100; t++){
			Main board = new Main(new int[SIZE][SIZE], new int[SIZE]);
			boolean check = true;
			int sideways = 0;
			
			board.queensOnBoard();
			int h = board.conflicts();
			
			if(t == r1 || t == r2 || t == r3){
				System.out.println("NEW BOARD:");
				board.displayBoard();
				System.out.println("Board Heursitic: " + board.conflicts());
				System.out.println();
			}
			
			successSteps = 0;
			failSteps = 0;
			
			while(check){
				if(sideways == 100){
					totalFailSteps += failSteps;
					break;
				}

				//getting the heuristic of the complete board
				h = board.conflicts();
				for(int i = 0; i < board.board.length; i++){
					board.changeQueen(i);
					for(int j = 0; j < board.board.length; j++){
						board.placeQueen(j, i);
						arr1[j][i] = board.conflicts();
						board.removeQueen(j, i);
					}
					board.replaceQueen(i);
				}
				
				int l2 = 99;
				
				//lowest heuristic
				for(int i = 0; i < board.board.length; i++){
					for(int j = 0; j < board.board.length; j++){
						if(arr1[j][i] < l2){
							l2 = arr1[j][i];
						}
					}
				}
				
				ArrayList<Integer> row= new ArrayList<Integer>(SIZE*SIZE); 
				ArrayList<Integer> col= new ArrayList<Integer>(SIZE*SIZE);
				
				for(int i = 0; i < board.board.length; i++){
					for(int j = 0; j < board.board.length; j++){
						if(arr1[j][i] == l2){
							row.add(j);
							col.add(i);
						}
					}
				}
				
				int rand1 = r.nextInt(row.size());
				int row1 = row.get(rand1);
				int col1 = col.get(rand1);
				
				board.changeQueen(col1);
				board.placeQueen(row1, col1);
				board.queenPos[col1] = row1;
				int l = board.conflicts();
				
				if(l == 0){
					if(t == r1 || t == r2 || t == r3){
						board.displayBoard();
						System.out.println("Board Heuristic: " + board.conflicts());
						System.out.println("Success!");
						System.out.println();
					}
					
					success++;
					h = 0;
					check = false;
					successSteps++;
					totalSuccessSteps += successSteps;
					break;
				}

				if(l <= h){
					if(t == r1 || t == r2 || t == r3){
						board.displayBoard();
						System.out.println("Board Heuristic: " + board.conflicts());
						System.out.println();
					}
					
					h = board.conflicts();
					check = true;
					sideways++;
					successSteps++;
					failSteps++;
				} else {
					totalFailSteps += failSteps;
					check = false;
				}		
			}	
		}

		System.out.println("Average Success Step: " + (float)(totalSuccessSteps / success));
		System.out.println("Average Failure Step: " + (float)(totalFailSteps / (100 - success)));
		System.out.println("Success Rate: " + (success / 100.0) * 100.0);
		System.out.println("Failure Rate: " + (100.0 - (success / 100.0) * 100.0));
	}
	
	public static void randomRestart(){
		Random r = new Random();
		int[][] arr1 = new int[SIZE][SIZE];
		int step = 0;
		int totalStep = 0;

		int restart = 0;
		for(int i1 = 0; i1 < 100; i1++){
			Main board = new Main(new int[SIZE][SIZE], new int[SIZE]);
			boolean check = true;
			totalStep += step;
			step = 0;
			
			board.queensOnBoard();
			int h = board.conflicts();


			while(check){
				h = board.conflicts();

				//getting the heuristic of the complete board 
				for(int i = 0; i < board.board.length; i++){
					board.changeQueen(i);

					for(int j = 0; j < board.board.length; j++){
						board.placeQueen(j, i);
						arr1[j][i] = board.conflicts();
						board.removeQueen(j, i);
					}

					board.replaceQueen(i);
				}
				
				int l2 = 99;
				
				//find lowest heuristic
				for(int i = 0; i < board.board.length; i++){
					for(int j = 0; j < board.board.length; j++){
						if(arr1[j][i] < l2){
							l2 = arr1[j][i];
						}
					}
				}
				
				ArrayList<Integer> row= new ArrayList<Integer>(SIZE*SIZE); 
				ArrayList<Integer> col= new ArrayList<Integer>(SIZE*SIZE);
				
				for(int i = 0; i < board.board.length; i++){
					for(int j = 0; j < board.board.length; j++){
						if(arr1[j][i] == l2){
							row.add(j);
							col.add(i);
						}
					}
				}
				
				int rand1 = r.nextInt(row.size());
				int row1 = row.get(rand1);
				int col1 = col.get(rand1);
				
				board.changeQueen(col1);
				board.placeQueen(row1, col1);
				board.queenPos[col1] = row1;
				
				int l = board.conflicts();
				
				if(l == 0){
					h = 0;
					check = false;
				}
				
				if(l < h){
					h = board.conflicts();
					check = true;
					step++;
				} else {
					restart++;
					board.queensOnBoard();
				}		
			}	
		}

		System.out.println("Average Number of Restarts: " + (float)(restart / 100));
		System.out.println("Average Number of Steps: " + (float)(totalStep / 100));
	}
	
	public static void randomRestartAndSideways(){
		Random r = new Random();
		int[][] arr1 = new int[SIZE][SIZE];
		int restart = 0;
		int step = 0;
		int totalStep = 0;

		for(int i1 = 0; i1 < 100; i1++){
			Main board = new Main(new int[SIZE][SIZE], new int[SIZE]);
			boolean check = true;
			int sideways = 0;
			step = 0;
			
			board.queensOnBoard();
			int h = board.conflicts();

			while(check){
				if(sideways == 100){
					board.queensOnBoard();
					restart++;
					sideways = 0;
					check = true;
					step++;
				}
				
				//getting the heuristic of the board
				h = board.conflicts();
				for(int i = 0;i<board.board.length; i++){
					board.changeQueen(i);
					for(int j = 0; j < board.board.length; j++){
						board.placeQueen(j, i);
						arr1[j][i] = board.conflicts();
						board.removeQueen(j, i);
					}

					board.replaceQueen(i);
				}
				
				int l2 = 99;
				
				//find lowest heuristic
				for(int i = 0; i < board.board.length; i++){
					for(int j = 0; j < board.board.length; j++){
						if(arr1[j][i] < l2){
							l2 = arr1[j][i];
						}
					}
				}
				
				ArrayList<Integer> row= new ArrayList<Integer>(SIZE*SIZE); 
				ArrayList<Integer> col= new ArrayList<Integer>(SIZE*SIZE);

				for(int i = 0; i < board.board.length; i++){
					for(int j = 0; j < board.board.length; j++){
						if(arr1[j][i] == l2){
							row.add(j);
							col.add(i);
						}
					}
				}
				
				int rand1 = r.nextInt(row.size());
				int row1 = row.get(rand1);
				int col1 = col.get(rand1);

				board.changeQueen(col1);
				board.placeQueen(row1, col1);
				board.queenPos[col1] = row1;
				
				int l = board.conflicts();
				
				if(l == 0){
					h = 0;
					totalStep += step;
					check = false;
					break;
				}
				
				if(l <= h){
					h = board.conflicts();
					check = true;
					sideways++;
					step++;

				}
			}	
		}

		System.out.println("Average Number of Restarts with Sideways Move: "+ (float)restart / 100);
		System.out.println("Average Number of Steps: " + (float)(totalStep / 100));
	}
	
	//constructor
	public Main(int[][] board, int[] positions){
		this.board = board;
		this.queenPos = positions;
	}
	
	//calculate heuristic
	public int conflicts(){
		int h = 0;
		int[] queenPos = new int[SIZE];
		
		//locate the current positions of the queens
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(board[j][i] == 1){
					queenPos[i] = j;
				}
			}
		}
		
		//check for conflicts
		for(int i = 0; i < SIZE - 1; i++){
			for(int j = i + 1; j < SIZE; j++){
				int q1_x = i;
				int q1_y = queenPos[i];
				int q2_x = j;
				int q2_y = queenPos[j];
				
				if(q1_x == q2_x)
					h += 1;
				else if(q1_y == q2_y)
		            h += 1;
				else if((q1_x + q1_y) == (q2_x + q2_y))
		            h += 1;
				else if((q1_x - q1_y) == (q2_x - q2_y))
		            h += 1;
			}
		}
		return h;
	}
	
	//get random queen positions for the board
	private int[] randQueenPositions(){
		List<Integer> randomPos = new ArrayList<Integer>();
		Random r = new Random();
		
		for(int i = 0; i < SIZE; i++){
			randomPos.add(r.nextInt(SIZE));
		}

		int[] randomPositions = new int[SIZE];

		for(int i = 0; i < randomPos.size(); i++){
			randomPositions[i] = randomPos.get(i);
		}

		return randomPositions;
	}
	
	//place the queens in their random positions
	public void queensOnBoard(){
		//get the random starting positions of each queen
		queenPos = randQueenPositions();
		for(int i = 0; i < board.length; i++){
			board[queenPos[i]][i] = 1;
		}
	}
	
	public void changeQueen(int row) {
		board[queenPos[row]][row] = 0;
	}
	
	public void replaceQueen(int row) {
		board[queenPos[row]][row] = 1;
	}
	
	//change i, j back to 0
	public void removeQueen(int i,int j) {
		board[i][j] = 0;
	}
	
	//change i, j to hold a queen
	public void placeQueen(int i,int j) {
		board[i][j] = 1;
	}
	
	//displaying the board
	public void displayBoard(){
		for(int i = 0; i < board.length; i++){
			boolean first = true;
			for(int j = 0; j < board[i].length; j++){
				if(first){
					System.out.print(board[i][j]);
					first = false;
				} else {
					System.out.print(", " + board[i][j]);
				}
			}
			System.out.println();
		}
	}
}

