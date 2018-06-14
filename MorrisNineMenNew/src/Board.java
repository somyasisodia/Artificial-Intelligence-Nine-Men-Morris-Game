
public class Board {
	
	public void printBoard(char[] board){
		System.out.println((board[20])+" - - - - - - "+board[21]+" - - - - - - "+board[22]);
		System.out.println("|             |             |");
		System.out.println("|     "+board[17]+" - - - "+board[18]+" - - - "+board[19]+"     |");
		System.out.println("|     |       |       |     |");
		System.out.println("|     |   "+board[14]+" - "+board[15]+" - "+board[16]+"   |     |");
		System.out.println("|     |   |       |   |     |");
		System.out.println(board[8]+" - - "+board[9]+"- -"+board[10]+"       "+board[11]+"- -"+board[12]+" - - "+board[13]);
		System.out.println("|     |   |       |   |     |");
		System.out.println("|     |   "+board[6]+" - - - "+board[7]+"   |     |");
		System.out.println("|     |               |     |");
		System.out.println("|     "+board[3]+" - - - "+board[4]+" - - - "+board[5]+"     |");
		System.out.println("|             |             |");
		System.out.println((board[0])+" - - - - - - "+board[1]+" - - - - - - "+board[2]);
	}

	/*public char printPos(char[] board, int loc){
		if(board[loc]) 
	}*/
}
