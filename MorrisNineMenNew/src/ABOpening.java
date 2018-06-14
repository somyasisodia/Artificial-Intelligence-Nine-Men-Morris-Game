import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class ABOpening {
	
	static public char[] inputBoard = new char[23];
	static public char[] outputBoard = new char[23];
	public static int maxStep;
	public static int counter = 0;
	//public ArrayList<char[]> list = new ArrayList<char[]>();
	
	
	
	public static void main(String[] args) throws IOException{
		ABOpening ob = new ABOpening();
		FileReader fr = new FileReader(args[0]);
		FileWriter fw = new FileWriter(args[1]);
		int depth = Integer.parseInt(args[2]);
		Board b = new Board();
		fr.read(inputBoard);
		//System.out.println(inputBoard);
		//char[] input = fr.read(inputBoard);
		maxStep = depth;
		TreeNode root = new TreeNode();
		//root = new TreeNode();
		root.setboard(inputBoard);
		//String s = "WWWBxxWxWxxBBWxxBBxxWBx";
		//char[] sChar = s.toCharArray();
		//root.setboard(sChar);
		//Board br = new Board();
		//root.setboard(sChar);
		System.out.println("Playing AlphaBeta Opening ");
		System.out.println("Input : "+String.valueOf(root.getboard()));
		System.out.println("Depth : "+depth);
		System.out.println("Input Board : ");
		b.printBoard(root.getboard());
		//System.out.println(root.getboard());
		//System.out.println(depth);
		//insertChild(root, depth, inputBoard);
		insertChild(root, depth);
		char[] nextMove = AlphaBetaSearch(root);
		System.out.println("Next Move : "+String.valueOf(nextMove));
		System.out.println("Output Board : ");
		fw.write(nextMove);
		b.printBoard(nextMove);
		fr.close();
		fw.close();
	}
	
	/*public static char[] AlphaBetaSearch(TreeNode root){
		int sValue = AlphaBeta(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
		return AlphaBetaNodeSearch(root, sValue);
	}*/
	
	public static char[] AlphaBetaSearch(TreeNode root){
		//int v = AlphaBeta(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
		//int sValue = AlphaBeta(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
		int sValue = AlphaBeta(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
		System.out.println("AlphaBeta search");
		System.out.println("postions evaluated:"+counter);
		System.out.println("AlphaBeta estimation:"+sValue);
		
		return AlphaBetaNodeSearch(root, sValue);
	}
	
	public static void insertChild(TreeNode node, int depth){
		//System.out.println(node.getboard());
		//System.out.println(depth);
		char[] board = node.getboard();
		if(depth>0){
			ArrayList<char[]> boardList = null;
			if((maxStep-depth)%2 == 0){
				boardList = generateMovesOpeningWhite(board);
			} else{
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
			int staticOpening = staticEstimation(node.board);
			node.setstaticValue(staticOpening);
		}
	}
	
	public static int AlphaBeta(TreeNode node, int alpha, int beta){
		
		if(node.childNodes.isEmpty()){
			counter++;
			return node.getstaticValue();
		} else{
			//int v = -1000005;
			int v = Integer.MIN_VALUE;
			//for(TreeNode child : node.childNodes){
			for(int i=0; i<node.childNodes.size(); i++){
				v = Math.max(v, BetaAlpha(node.childNodes.get(i), alpha, beta));
				if(v >= beta){
					node.staticValue = v;
					return v;
				} else{
					alpha = Math.max(v, alpha);
				}
			}
			counter++;
			node.staticValue = v;
			return v;
		}
	}
	
	public static int BetaAlpha(TreeNode node, int alpha, int beta){
		if(node.childNodes.isEmpty()){
			counter++;
			return node.getstaticValue();
		} else{
			//int v = 1000005;
			int v = Integer.MAX_VALUE;
			//for(TreeNode child : node.childNodes){
			for(int i=0; i<node.childNodes.size(); i++){
				v = Math.min(v, AlphaBeta(node.childNodes.get(i), alpha, beta));
				if(v <= alpha){
					node.staticValue = v;
					return v;
				} else{
					beta = Math.min(v, beta);
				}
			}
			counter++;
			node.staticValue = v;
			return v;
		}
	}
	
	public static char[] AlphaBetaNodeSearch(TreeNode node, int v){
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
				if(i<node.childNodes.size())
					return AlphaBetaNodeSearch(node.childNodes.get(i), sValue);
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
		//boolean flag = false;
		for(int i=0;i<inputBoard.length;i++){
			if(inputBoard[i]=='B'){
				if (!checkCloseMill(inputBoard, i)){
					char[] b = inputBoard.clone();
					b[i] = 'x';
					list.add(b);
					//flag = true;
				}
			}
		}
		/*if(flag==false){
			list.add(inputBoard);
		}*/
		list.add(inputBoard);
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
		/*if(flag==false){
			list.add(inputBoard);
		}*/
		list.add(inputBoard);
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
		//int numBlackMoves = list.size();
		
		return (numWhitePieces - numBlackPieces);
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
