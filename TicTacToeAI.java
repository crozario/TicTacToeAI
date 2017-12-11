import java.util.Scanner;

public class TicTacToeAI {
	
	public static void main(String[] args) {
		int playerXTaken[] = new int[5];	//positions taken by AI
		int playerOTaken[] = new int[5];	//positions taken by you
		boolean winnerFound = false;
		boolean draw = false;
		char winner = ' '; //blank winner
		char currPlayer;

		final char playerX = 'X'; //AI
		final char playerO = 'O'; //you

		printBoardPositions();	//print board positions

		char first = pickFirstPlayer();
		// char first = playerO;

		// System.out.println("First Player is:  " + first);
		if(first == playerX) {
			System.out.println("X is first");
			int move = pickFirstMove(playerX);
			playerXTaken = addPos(move, playerXTaken);
			printScreen(playerXTaken, playerOTaken);
			currPlayer = playerO;
		} else {
			System.out.println("O is first");
			int move = pickFirstMove(playerO);
			playerOTaken = addPos(move, playerOTaken);
			printScreen(playerXTaken, playerOTaken);
			currPlayer = playerX;

		}

		while (winnerFound == false && draw == false) {
			if(currPlayer == playerX) {
				playerXTaken = playAI(playerXTaken, playerOTaken);
				printScreen(playerXTaken, playerOTaken);
				winnerFound = findWinner(playerXTaken);		
				if(winnerFound == true)
					winner = 'X';

				currPlayer = playerO;

			} else {
				playerOTaken = playUser(playerXTaken, playerOTaken);
				if(playerOTaken[0] == -1)
					break;
				printScreen(playerXTaken, playerOTaken);
				winnerFound = findWinner(playerOTaken);
				if(winnerFound == true)
					winner = 'O';

				currPlayer = playerX;

			}

			if(checkDraw(playerXTaken, playerOTaken) == true) {
				draw = true;
			}
				
		}

		if(winnerFound == true)
			System.out.println("The winner is Player " + winner);
		else if(draw == true) {
			System.out.println("Its a Draw!");
		} else {
			System.out.println("Goodbye!");
		}

	}

	static char pickFirstPlayer() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Does the AI (player X) go first? 0 for yes or 1 for no: ");
		int num= scan.nextInt();
		char player = ' ';
		if(num == 0){
			player = 'X';
		} else if(num == 1) {
			player = 'O';
		} else {
			System.out.print("Should've picked 0 or 1 but AI is first now");
			player = 'X';
		}
		return player;
	}

	static int pickFirstMove(char player) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Pick first move for player " + player + ": ");
		int move = scan.nextInt();
		return move;
	}


	static char whoGoesFirst() {
		int rand = (int)(Math.random() * 2);

		if(rand == 0)
			return 'X';
		else 
			return 'O';

	}

	static int pickRand() {
		return (int)(Math.random() * 10 + 1);
	}



	static int[] playAI(int playerX[], int playerO[]) {
		int strategy = 0;
		int tempStrategy = 0;

		//check if you have two in a row. get win

		//check if opponent has two in a row. Block it.

		//check if you can pick two way win scenerio

		//if opponent is in a corner. pick the opposite corner
		 

		if((tempStrategy = twoInARowWin(playerX, playerO)) != 0) { //pick if two in a row 
			strategy = tempStrategy;
			// System.out.println("checked twoInARowWin: " + tempStrategy);
		} else if((tempStrategy = twoInARowOpponentWin(playerX, playerO)) != 0) { //pick if center is empty
			strategy = tempStrategy;
			// System.out.println("checked twoInARowOpponentWin: " + tempStrategy);
		} else if((tempStrategy = pickCenter(playerX, playerO)) != 0) { //pick if center is empty
			strategy = tempStrategy;
			// System.out.println("checked pickedCenter: " + tempStrategy);
		} else if((tempStrategy = pickCorners(playerX, playerO)) != 0) { //pick an empty corner
			strategy = tempStrategy;
			// System.out.println("checked pickedCorners: " + tempStrategy);
		} else if((tempStrategy = pickSides(playerX, playerO)) != 0) { //pick an empty side
			strategy = tempStrategy;
			// System.out.println("checked pickedSides: " + tempStrategy);
		}


		// if strategy is found then pick it otherwise pick random
		if(strategy != 0) {  
			playerX = addPos(strategy, playerX);          
		} else {
			int rand = randomPick(playerX, playerO);
			playerX = addPos(rand, playerX);
		}
		// printArr(playerX);

		return playerX;
	}

	/*----------strategies----------*/

	
	//check if opponent has a corner
	static int opponentHasCorner(int playerX[], int playerO[]) {
		int corners[] = { 1, 3, 7, 9};
		int i, j=0;
		int newArr[] = new int[4];
		for(i=0; i<corners.length;i++) {
			if(isTaken(playerO, corners[i]) == false && isTaken(playerX, corners[i]) == false)
				newArr[j++] = corners[i];
		}

		newArr = cleanArr(newArr);
		return randomPick(newArr);
	}




	//check if opponent has two in a row
	static int twoInARowOpponentWin(int playerX[], int playerO[]) {
		int twoArr[][] = { 
		{1,2}, {1,3}, {2,3}, 
		{4,5}, {4,6}, {5,6}, 
		{7,8}, {7,9}, {8,9}, 
		{1,4}, {1,7}, {4,7}, 
		{2,5}, {2,8}, {5,8}, 
		{3,6}, {3,9}, {6,9}, 
		{1,5}, {1,9}, {5,9},
		{3,5}, {3,7}, {5,7} };

		int i, j, k, count, num = 0,strat = -1;

		for(i=0;i<twoArr.length; i++) {
			count = 0;
			for(j=0;j<twoArr[i].length; j++) {		
				for(k=0; k<playerO.length;k++) {
					if(twoArr[i][j] == playerO[k])
						count++;			
				}
				// System.out.println("count: " + count);
				if(count == 2) {
					num = isThirdPosTaken(playerX, i);
					if(isTaken(playerX, num) == false && num != 0) {
						strat = num;
					} 
				}


			}
		}

		if(strat == -1)
			strat = 0;

		
		return strat;

	}
	//check if you have two in a row 
	static int twoInARowWin(int playerX[], int playerO[]) {
		int twoArr[][] = { 
		{1,2}, {1,3}, {2,3}, 
		{4,5}, {4,6}, {5,6}, 
		{7,8}, {7,9}, {8,9}, 
		{1,4}, {1,7}, {4,7}, 
		{2,5}, {2,8}, {5,8}, 
		{3,6}, {3,9}, {6,9}, 
		{1,5}, {1,9}, {5,9},
		{3,5}, {3,7}, {5,7} };

		int i, j, k, count, num = 0,strat = -1;

		for(i=0;i<twoArr.length; i++) {
			count = 0;
			for(j=0;j<twoArr[i].length; j++) {
				for(k=0; k<playerX.length;k++) {
					if(twoArr[i][j] == playerX[k])
						count++;			
				}

				if(count == 2) {
					num = isThirdPosTaken(playerO, i);

					if(isTaken(playerO, num) == false && num != 0) {
						strat = num;
					} 
				}

			}
		}

		if(strat == -1)
			strat = 0;

		
		return strat;

	}


	static int isThirdPosTaken(int arr[], int pos) {
		// System.out.println("*" + pos +"*");
		int num;
		switch(pos) {
			case -1:
				num = 0;
				break;
			case 0:
				num = 3;
				break;
			case 1:
				num = 2;
				break;
			case 2:
				num = 1;
				break;
			case 3:
				num = 6;
				break;
			case 4:
				num = 5;
				break;
			case 5:
				num = 4;
				break;
			case 6:
				num = 9;
				break;
			case 7:
				num = 8;
				break;
			case 8:
				num = 7;
				break;
			case 9:
				num = 7;
				break;
			case 10:
				num = 4;
				break;
			case 11:
				num = 1;
				break;
			case 12:
				num = 8;
				break;
			case 13:
				num = 5;
				break;
			case 14:
				num = 2;
				break;
			case 15:
				num = 9;
				break;
			case 16:
				num = 6;
				break;
			case 17:
				num = 3;
				break;
			case 18:
				num = 9;
				break;
			case 19:
				num = 5;
				break;
			case 20:
				num = 1;
				break;
			case 21:
				num = 7;
				break;
			case 22:
				num = 5;
				break;
			case 23:
				num = 3;
				break;
			default:
				System.out.println("Error in finding two in a row");
				num = 0;
				break;
		}

		if(isTaken(arr, num))
			return 0;
		else
			return num;
	}
	//center strategy
	static int pickCenter(int playerX[], int playerO[]) {
		int center = 5;
		if(isTaken(playerX, playerO, center) == false) {
			return center;
		} else {
			return 0;
		}

	}

	//corner strategy
	static int pickCorners(int playerX[], int playerO[]) {
		
		int corner[] = {1,3,7,9};
		int merged[] =  merge(playerX, playerO);
		merged = removePositionsAlreadyPicked(corner, merged);
		
		return randomPick(merged);

	}

	//side strategy
	static int pickSides(int playerX[], int playerO[]) {
		
		int sides[] = {1,2,3,4,6,7,8,9};

		int merged[] =  merge(playerX, playerO);
		merged = removePositionsAlreadyPicked(sides, merged);
		
		

		return randomPick(merged);
	}

	/*----------strategies----------*/


	// adds position to array
	static int[] addPos(int pos, int arr[]) {
		int i;

		for(i=0;i<arr.length; i++){
			if(arr[i] == 0) {
				arr[i] = pos;
				break;
			}	
		}

		return arr;
	}


	static int randomPick(int arr[]) {
		int randP = (int)(Math.random() * arr.length-1);
		int rand = arr[randP];
		// System.out.println("Randomly picked: " + rand);

		return rand;
	}


	static int randomPick(int playerX[], int playerO[]) {
		int arrPos[] = { 1,2,3,4,5,6,7,8,9};
		int pickArr[];
		int i;

		int newArr[] = merge(playerX, playerO);
		newArr = removePositionsAlreadyPicked(arrPos, newArr);

		int randP = (int)(Math.random() * newArr.length-1);
		int rand = newArr[randP];
		// System.out.println("Randomly picked: " + rand);


		return rand;
	}


	//merges list and removes 0's
	static int[] merge(int arrOne[], int arrTwo[]) {
		int len = findLength(arrOne, arrTwo);		
		int mergeArr[] = new int[len];

		if(len == 0)
			return mergeArr;

		int i, x = 0;

		for(i=0;i<arrOne.length; i++){
			if(arrOne[i] != 0) {
				mergeArr[x++] = arrOne[i];
			}	
		}

		for(i=0; i<arrTwo.length; i++){
			if(arrTwo[i] != 0) {
				mergeArr[x++] = arrTwo[i];
			}	
		}

		return mergeArr;
	}

	// 
	static int[] removePositionsAlreadyPicked(int mainArr[], int picked[]) {
		int i, j;
		for(i=0;i<mainArr.length;i++){
			for(j=0;j<picked.length;j++){
				if(mainArr[i] == picked[j])
					mainArr[i] = 0;
			}
		}
		

		mainArr = cleanArr(mainArr);


		return mainArr;

	}

	// //remove 0's 
	static int[] cleanArr(int arr[]) {
		int i, newLen;
		newLen = 0;
		for(i=0;i<arr.length;i++){
			if(arr[i] != 0)
				newLen++;
		}
		if(newLen != 0) {
			int newArr[] = new int[newLen];
			newLen = 0;
			for(i=0;i<arr.length;i++){
				if(arr[i] != 0)
					newArr[newLen++] = arr[i];
			}

			return newArr;
		} else {
			return arr;
		}
		
		
	
	}

	//find length without 0's
	static int findLength(int arrOne[], int arrTwo[]) {
		int len = 0;
		int i;

		for(i=0; i<arrOne.length; i++){
			if(arrOne[i] != 0) {
				len++;
			}	
		}
		for(i=0; i<arrTwo.length; i++){
			if(arrTwo[i] != 0) {
				len++;
			}	
		}

		return len;
	}

	static int[] playUser(int playerX[], int playerO[]) {
		int i;
		Scanner scan = new Scanner(System.in);
		boolean taken = true;
		int userPos = -1;

		while(taken == true) {
			System.out.print("Pick a Position: ");
			userPos = scan.nextInt();
			if(userPos == 0) {
				playerO[0] = -1;	
				return playerO; 	
			}

			taken = isTaken(playerX, playerO, userPos);
			if(taken == true)
				System.out.println("Position already Picked");		
		}

		playerO = addPos(userPos, playerO);

		return playerO;
	}

	static boolean checkDraw(int playerX[], int playerO[]) {
		boolean draw = false;
		int count = 0;
		int i;
		for(i=0; i<playerX.length;i++) {
			if(playerX[i] != 0)
				count++;
		}
		for(i=0; i<playerO.length;i++) {
			if(playerO[i] != 0)
				count++;
		}

		if(count == 9)
			draw = true;

		// System.out.println("Draw count: " + count);
		return draw;
	}

	static boolean isTaken(int playerX[], int playerO[], int pos) {
		boolean taken = false;
		int i;

		//check if position picked is in playerX array
		for(i=0; i<playerX.length;i++) {
			if(playerX[i] == pos)
				taken = true;
		}

		//check if position picked is in playerO array
		for(i=0; i<playerO.length;i++) {
			if(playerO[i] == pos)
				taken = true;
		}

		return taken;
	}
	
	static boolean isTaken(int arr[], int pos) {
		boolean taken = false;
		int i;

		//check if position picked is in playerX array
		for(i=0; i<arr.length;i++) {
			if(arr[i] == pos)
				taken = true;
		}

		return taken;
	}
	
	

	//finds the winner, input array is the positions of player X or player O
	static boolean findWinner(int arr[]) {
		int i, j, k, in;
		int winningCombo[][] = new int[][]{ { 1, 5, 9 } , { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 1, 4, 7 }, 
		{ 2, 5, 8 }, { 3, 6, 9 }, {5, 7, 3} };

		for(i=0; i<8; i++) {
			in = 0;
      		for(j=0; j<3; j++) {
      			for(k=0; k<5; k++) {
      				if(winningCombo[i][j] == arr[k]) 
      					in++; 			
      			}
      			if(in == 3) {
      				return true;
      			}

      		}
    	}

    	return false;

	}

	//print current board layout
	static void printScreen(int playerX[], int playerO[]) {
		char screen[][] = new char[][]{ {' ',' ',' '}, {' ',' ',' '}, {' ',' ',' '} };  // blank screen
		int i, j;
		
		for(i=0; i<playerX.length; i++) {
			if(playerX[i] == 0)
				break;
			screen = placePos(screen, 'X', playerX[i]);

    	} 
    	for(i=0; i<playerO.length; i++) {
    		if(playerO[i] == 0)
    			break;
    		screen = placePos(screen, 'O', playerO[i]);
    	} 	

    	for(i=0; i<3; i++) {
     		System.out.println(" -------------");
      		for(j=0; j<3; j++) {
        		System.out.print(" | " + screen[i][j]);
      		}
      		System.out.println(" |");
    	}
    	System.out.println(" -------------");

	}

	static char[][] placePos(char screen[][], char player, int pos) {
		switch(pos){
			case 1:
				screen[0][0] = player;
				break;
			case 2:
				screen[0][1] = player;
				break;
			case 3:
				screen[0][2] = player;
				break;
			case 4:
				screen[1][0] = player;
				break;
			case 5:
				screen[1][1] = player;
				break; 
			case 6:
				screen[1][2] = player;
				break;
			case 7:
				screen[2][0] = player;
				break;
			case 8:
				screen[2][1] = player;
				break;
			case 9:
				screen[2][2] = player;
				break;
			default:
				System.out.println("Position out of Range: " + pos);
				break;
		}

		return screen;
	}


	//print position of the board
	static void printBoardPositions() {
		int i, j;
		int arr[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };

		System.out.println("Position of the screen is: ");

    	for(i=0; i<3; i++) {
     		System.out.println(" -------------");
      		for(j=0; j<3; j++) {
        		System.out.print(" | " + arr[i][j]);
      		}
      		System.out.println(" |");
    	}
    	System.out.println(" -------------");	
    }

    static void printArr(int arr[]) {
    	int i;
    	for(i=0; i<arr.length; i++) {
     		System.out.print(arr[i] + " - ");
    	}
    	System.out.println();
    }

}

