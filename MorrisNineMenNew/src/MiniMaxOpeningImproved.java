import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MiniMaxOpeningImproved {

	static public char[] inputBoard = new char[23];
	static public char[] outputBoard = new char[23];
	public static int maxStep;
	public static int counter = 0;
	//public ArrayList<char[]> list = new ArrayList<char[]>();

	public static void main(String[] args) throws IOException{
		//MiniMaxOpening ob = new MiniMaxOpening();
		FileReader fr = new FileReader(args[0]);
		FileWriter fw = new FileWriter(args[1]);
		int depth = Integer.parseInt(args[2]);
		Board b = new Board();
		//int depth = 4;
		fr.read(inputBoard);
		//System.out.println(inputBoard);
		//char[] input = fr.read(inputBoard);
		maxStep = depth;
		TreeNode root = new TreeNode();
		//root = new TreeNode();
		//String s = "WBWxxxBxxxxxxWxxxWxxxxB";
		//char[] sChar = s.toCharArray();
		System.out.println("Playing MiniMax Opening Improved ");
		root.setboard(inputBoard);
		System.out.println(root.getboard());
		//System.out.println(depth);
		root.setboard(inputBoard);
		System.out.println("Input : "+String.valueOf(root.getboard()));
		System.out.println("Depth : "+depth);
		System.out.println("Input Board : ");
		b.printBoard(root.getboard());
		//System.out.println(depth);
		//insertChild(root, depth, inputBoard);
		insertChild(root, depth);
		//fr.close();
		//fw.close();
		char[] nextMove = MiniMaxSearch(root);
		System.out.println("Next Move : "+String.valueOf(nextMove));
		System.out.println("Output Board : ");
		b.printBoard(nextMove);
		fw.write(nextMove);
		fr.close();
		fw.close();
	}
	
	public static char[] MiniMaxSearch(TreeNode root){
		int sValue = MaxMin(root);
		System.out.println("Minimax value : "+sValue);
		System.out.println("Evaluated positions : "+counter);
		System.out.println("MinMax static estimation value : "+sValue);
		//char[] nextMove;
		return nodeSearch(root, sValue);
		//System.out.println(nextMove);
	}
	
	//public static void insertChild(TreeNode node, int depth, char[] board){
	public static void insertChild(TreeNode node, int depth){
		//System.out.println(node.getboard());
		//System.out.println(depth);
		char[] board = node.getboard();
		if(depth>0){
			//System.out.println("depth : "+depth);
			ArrayList<char[]> boardList = null;
			if((maxStep-depth)%2 == 0){
				//System.out.println("White move");
				boardList = generateMovesOpeningWhite(board);
			} else{
				//System.out.println("Black move");
				boardList = generateMovesOpeningBlack(board);
			}
			for(int i=0; i<boardList.size(); i++){
				TreeNode newNode = new TreeNode();
				newNode.setstaticValue(0);
				newNode.setboard(boardList.get(i));
				node.getChildNodes().add(newNode);
				insertChild(node.getChildNodes().get(i), depth-1);
			}
		} else{
			int staticOpening = staticEstimation(node.getboard());
			node.setstaticValue(staticOpening);
		}
	}
	
	public static int MaxMin(TreeNode node){
		int v;
		if(node.childNodes.isEmpty()){
		//if(node.childNodes== null){
			counter++;
			return node.staticValue;
		} else{
			v = Integer.MIN_VALUE;
			for(TreeNode child : node.childNodes){
				v = Math.max(v,  MinMax(child));
			}
			counter++;
			node.staticValue = v;
			return v;
		}
	}
	
	public static int MinMax(TreeNode node){
		if(node.childNodes.isEmpty()){
		//if(node.childNodes== null){
			counter++;
			return node.staticValue;
		} else{
			int v = Integer.MAX_VALUE;
			for(TreeNode child : node.childNodes){
				v = Math.min(v,  MaxMin(child));
			}
			counter++;
			node.staticValue = v;
			return v;
		}
	}
	
	public static char[] nodeSearch(TreeNode node, int v){
		if(node.childNodes.isEmpty()){
		//if(root.childNodes== null){
			if(node.staticValue == v)
				return node.board;
				else
				return null;
		} else{
			int i;
			int sValue=0;
			for(i=0; i<node.childNodes.size(); i++){
				if((sValue=node.childNodes.get(i).staticValue)==v)
					break;
			}
				if(i<=node.childNodes.size())
					return nodeSearch(node.childNodes.get(i), sValue);
				 else
					return null;
		}
	}
	
	public static ArrayList<char[]> generateMovesOpeningWhite(char[] inputBoard){
		//System.out.println("Generate White move");
		return generateAddWhite(inputBoard);
	}
	
	public static ArrayList<char[]> generateMovesOpeningBlack(char[] inputBoard){
		return generateAddBlack(inputBoard);
	}
	
	public static ArrayList<char[]> generateAddWhite(char[] inputBoard){
		//System.out.println("Generate add White");
		ArrayList<char[]> list = new ArrayList<char[]>();
		for(int i=0;i<inputBoard.length;i++){
			//System.out.println("for loop");
			char v = inputBoard[i];
			if(v == 'x'){
				char[] b = inputBoard.clone();
				b[i]= 'W';
				if (checkCloseMill(b, i)){
					//System.out.println("close mill");
					generateRemoveBlack(b,list);
				} else{
					//System.out.println("White move added to list");
					list.add(b);
				}
			}
		}
		return list;
	}
	
	public static ArrayList<char[]> generateAddBlack(char[] inputBoard){
		//public char[] list = new char[23];
		//return list;
		ArrayList<char[]> list = new ArrayList<char[]>();
		for(int i=0;i<inputBoard.length;i++){
			if(inputBoard[i] == 'x'){
				char[] b = inputBoard.clone();
				b[i]= 'B';
				if (checkCloseMill(b, i)){
					//System.out.println("close mill");
					generateRemoveWhite(b, list);
				}
				else
					list.add(b);
			}
		}
		return list;
	}
	
	
	
	public static void generateRemoveBlack(char[] inputBoard, ArrayList<char[]>list){
		boolean flag = false;
		for(int i=0;i<inputBoard.length;i++){
			if(inputBoard[i]=='B'){
				if (!checkCloseMill(inputBoard, i)){
					char[] b = inputBoard.clone();
					b[i] = 'x';
					list.add(b);
					flag = true;
				}
			}
		}
		if(flag==false){
			list.add(inputBoard);
		}
	}
	
	public static void generateRemoveWhite(char[] inputBoard, ArrayList<char[]> list){
		boolean flag = false;
		for(int i=0;i<inputBoard.length;i++){
			if(inputBoard[i]=='W'){
				if (!checkCloseMill(inputBoard, i)){
					char[] b = inputBoard.clone();
					b[i] = 'x';
					list.add(b);
					flag = true;
				}
			}
		}
		if(flag==false){
			list.add(inputBoard);
		}
	}
	
	
	public static boolean checkCloseMill(char[] move, int location){
		char ch = move[location];
		switch(location){
		case 0 :
			if(((move[1] == ch) && (move[2] == ch)) || ((move[3] == ch) && (move[6] == ch)) || ((move[8] == ch) && (move[20] == ch)))
				return true;
			else
				return false;
		case 1 :
			if((move[0] == ch) && (move[2] == ch))
				return true;
			else
				return false;
		case 2 :
			if(((move[1] == ch) && (move[0] == ch)) || ((move[13] == ch) && (move[22] == ch)) || ((move[5] == ch) && (move[7] == ch)))
				return true;
			else
				return false;
		case 3 :
			if(((move[4] ==ch) && (move[5] == ch)) || ((move[0] == ch) && (move[6] == ch)) || ((move[9] == ch) && (move[17] == ch)))
				return true;
			else
				return false;
		case 4 :
			if((move[3] == ch) && (move[5] == ch))
				return true;
			else
				return false;
		case 5 :
			if(((move[2] == ch) && (move[7] == ch)) || ((move[3] == ch) && (move[4] ==ch)) || ((move[12] == ch) && (move[19] == ch)))
				return true;
			else
				return false;
		case 6 :
			if(((move[3] == ch) && (move[0] == ch)) || ((move[10] == ch) && (move[14] ==ch)))
				return true;
			else
				return false;
		case 7 :
			if(((move[5] == ch) && (move[2] == ch)) || ((move[11] == ch) && (move[16] == ch)))
				return true;
			else
				return false;
		case 8 :
			if(((move[0] == ch) && (move[20] == ch)) || ((move[9] == ch) && (move[10] == ch)))
				return true;
			else
				return false;
		case 9 :
			if(((move[8] == ch) && (move[10] == ch)) || ((move[3] == ch) && move[17] == ch))
				return true;
			else
				return false;
		case 10 :
			if(((move[6] == ch) && (move[14] == ch)) || ((move[9] == ch) && move[8] == ch))
				return true;
			else
				return false;
		case 11 :
			if(((move[7] == ch) && (move[16] == ch)) || ((move[12] == ch) && move[13] == ch))
				return true;
			else
				return false;
		case 12 :
			if(((move[11] == ch) && (move[13] == ch)) || ((move[5] == ch) && move[19] == ch))
				return true;
			else
				return false;
		case 13 :
			if(((move[2] == ch) && (move[22] == ch)) || ((move[11] == ch) && move[12] == ch))
				return true;
			else
				return false;
		case 14 :
			if(((move[17] == ch) && (move[20] == ch)) || ((move[10] == ch) && (move[6] ==ch)) || ((move[15] == ch) && (move[16] == ch)))
				return true;
			else
				return false;
		case 15 :
			if(((move[14] == ch) && (move[16] == ch)) || ((move[18] == ch) && move[21] == ch))
				return true;
			else
				return false;
		case 16 :
			if(((move[15] == ch) && (move[14] == ch)) || ((move[11] == ch) && (move[7] ==ch)) || ((move[19] == ch) && (move[22] == ch)))
				return true;
			else
				return false;
		case 17 :
			if(((move[18] == ch) && (move[19] == ch)) || ((move[9] == ch) && (move[3] ==ch)) || ((move[20] == ch) && (move[14] == ch)))
				return true;
			else
				return false;
		case 18 :
			if(((move[17] == ch) && (move[19] == ch)) || ((move[15] == ch) && move[21] == ch))
				return true;
			else
				return false;
		case 19 :
			if(((move[18] == ch) && (move[17] == ch)) || ((move[12] == ch) && (move[5] ==ch)) || ((move[22] == ch) && (move[16] == ch)))
				return true;
			else
				return false;
		case 20 :
			if(((move[21] == ch) && (move[22] == ch)) || ((move[17] == ch) && (move[14] ==ch)) || ((move[0] == ch) && (move[8] == ch)))
				return true;
			else
				return false;
		case 21 :
			if(((move[20] == ch) && (move[22] == ch)) || ((move[15] == ch) && move[18] == ch))
				return true;
			else
				return false;
		case 22 :
			if(((move[19] == ch) && (move[16] == ch)) || ((move[2] == ch) && (move[13] ==ch)) || ((move[20] == ch) && (move[21] == ch)))
				return true;
			else
				return false;
		default : return false;
		}
	}
	
	public static int staticEstimation(char[] inputBoard){
		int numWhitePieces = numWhitePieces(inputBoard);
		int numBlackPieces = numBlackPieces(inputBoard);
		//ArrayList<char[]> L = list;
		int numMillsPossible = 0;
		for(int i=0; i<inputBoard.length; i++){
			if(inputBoard[i] == 'W'){
				if(checkCloseMill(inputBoard, i)){
					numMillsPossible++;
				}
			}
		}
		//numMillsPossible = ;
		//int numBlackMoves = list.size();
		
		return ((numWhitePieces + numMillsPossible) - numBlackPieces);
		/*For MidGame EndGame
		 if(numBlackMoves <= 2){
			return 100000;
		} else{
			if(numWhitePieces <= 2){
				return -100000;
			} else{
				if(numBlackPieces == 0){
					return 10000;
				} else{
					return (1000 * (numWhitePieces - numBlackPieces) - numBlackMoves);
				}
			}
		}*/
		
	}
	
	public static int numWhitePieces(char[] inputBoard){
		int count = 0;
		for(char ch : inputBoard){
			if(ch == 'W'){
				count++;
			}
		}
		return count;
	}
	
	public static int numBlackPieces(char[] inputBoard){
		int count = 0;
		for(char ch : inputBoard){
			if(ch == 'B'){
				count++;
			}
		}
		return count;
	}
}
