import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MiniMaxGameBlack {

		static public char[] inputBoard = new char[23];
		static public char[] outputBoard = new char[23];
		public static int maxStep;
		public static int counter = 0;
		//public ArrayList<char[]> list = new ArrayList<char[]>();
		
		
		
		public static void main(String[] args) throws IOException{
			//int depth = 1;
			FileReader fr = new FileReader(args[0]);
			FileWriter fw = new FileWriter(args[1]);
			int depth = Integer.parseInt(args[2]);
			maxStep = 50;
			//String s = "BxxBxBWxWxxWxxxBBxxxxBx";
			//char[] sChar = s.toCharArray();
			//char[] inputBoard = sChar;
			TreeNode root = new TreeNode();
			Board b = new Board();
			//root.setboard(sChar);
			fr.read(inputBoard);
			root.setboard(inputBoard);
			System.out.println("Playing MiniMax Game Black ");
			System.out.println("Input : "+String.valueOf(root.getboard()));
			System.out.println("Depth : "+depth);
			System.out.println("Input Board : ");
			b.printBoard(root.getboard());
			insertChild(root, depth);
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
			//System.out.println("Minimax value : "+sValue);
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
				ArrayList<char[]> boardList;
				if((maxStep-depth)%2 == 0){
					boardList = generateMovesMidGameEndGameWhite(board);
				} else{
					boardList = generateMovesMidGameEndGameBlack(board);
				}
				//System.out.println("Moves List size : "+boardList.size());
				for(int i=0; i<boardList.size(); i++){
					TreeNode newNode = new TreeNode();
					newNode.setstaticValue(0);
					newNode.setboard(boardList.get(i));
					node.getChildNodes().add(newNode);
					insertChild(node.getChildNodes().get(i), depth-1);
				}
			} else{
				if(numBlackPieces(node.board) <= 2)
					node.staticValue = -100000;
				else if(numWhitePieces(node.board) <= 2)
					node.staticValue = 100000;
				else if(numWhiteMoves(node.board) == 0)
					node.staticValue = 1000000;
				else
					node.staticValue = ((1000 * (numBlackPieces(node.board) - numWhitePieces(node.board))) - numWhiteMoves(node.board));
			}
		}
		
		public static int MaxMin(TreeNode node){
			if(node.childNodes.isEmpty()){
				counter++;
				return node.staticValue;
			} else{
				//int v = Integer.MIN_VALUE;
				int v = Integer.MIN_VALUE;
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
				counter++;
				return node.staticValue;
			} else{
				//int v = Integer.MAX_VALUE;
				int v = Integer.MAX_VALUE;
				for(TreeNode child : node.childNodes){
					v = Math.min(v,  MaxMin(child));
				}
				counter++;
				node.staticValue = v;
				return v;
			}
		}
		
		public static char[] nodeSearch(TreeNode root, int v){
			if(root.childNodes.isEmpty()){
				if(root.staticValue == v){
					return root.board;
				} else{
					return null;
				}
			} else{
				int i;
				int sValue=0;
				for(i=0; i<root.childNodes.size(); i++){
					if((sValue=root.childNodes.get(i).staticValue)==v)
						break;
				}
					if(i<root.childNodes.size())
						return nodeSearch(root.childNodes.get(i), sValue);
					 else
						return null;
			}
		}
		
		public static ArrayList<char[]> generateMovesMidGameEndGameWhite(char[] board){
			//ArrayList<char[]> list = new ArrayList<char[]>();
			int numWhite = numWhitePieces(board);
			if(numWhite == 3){
				return generateHoppingWhite(board);
			} else{
				return generateMoveWhite(board);
			}
		}
		
		public static ArrayList<char[]> generateMovesMidGameEndGameBlack(char[] board){
			//ArrayList<char[]> list = new ArrayList<char[]>();
			int numBlack = numBlackPieces(board);
			if(numBlack == 3){
				return generateHoppingBlack(board);
			} else{
				return generateMoveBlack(board);
			}
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
		
		public static ArrayList<char[]> generateHoppingWhite(char[] board){
			ArrayList<char[]> list = new ArrayList<char[]>();
			for(int i=0; i<board.length; i++){
				if(board[i] == 'W'){
					for(int j=0; j<board.length; j++){
						if(board[j] == 'x'){
							char[] b = board.clone();
							b[i] = 'x';
							b[j] = 'W';
							if(checkCloseMill(b, j)){
								generateRemoveBlack(b, list);
							} else{
								list.add(b);
							}
						}
					}
				}
			}
			return list;
		}
		
		public static ArrayList<char[]> generateMoveWhite(char[] board){
			//System.out.println("Move White");
			ArrayList<char[]> list = new ArrayList<char[]>();
			for(int i=0; i<board.length; i++){
				if(board[i] == 'W'){
					int[] neighbor = neighborsList(i);
					for(int j=0; j<neighbor.length; j++){
						if(board[neighbor[j]] == 'x'){
							char[] b = board.clone();
							b[i] = 'x';
							b[neighbor[j]] = 'W';
							if(checkCloseMill(b, neighbor[j])){
								generateRemoveBlack(b, list);
							} else{
								list.add(b);
							}
						}
					}
				}
			}
			return list;
		}
		
		public static ArrayList<char[]> generateHoppingBlack(char[] board){
			ArrayList<char[]> list = new ArrayList<char[]>();
			for(int i=0; i<board.length; i++){
				if(board[i] == 'B'){
					for(int j=0; j<board.length; j++){
						if(board[j] == 'x'){
							char[] b = board.clone();
							b[i] = 'x';
							b[j] = 'B';
							if(checkCloseMill(b, j)){
								generateRemoveWhite(b, list);
							} else{
								list.add(b);
							}
						}
					}
				}
			}
			return list;
		}
		
		public static ArrayList<char[]> generateMoveBlack(char[] board){
			//System.out.println("Move Black");
			ArrayList<char[]> list = new ArrayList<char[]>();
			for(int i=0; i<board.length; i++){
				if(board[i] == 'B'){
					int[] neighbor = neighborsList(i);
					for(int j=0; j<neighbor.length; j++){
						if(board[neighbor[j]] == 'x'){
							char[] b = board.clone();
							b[i] = 'x';
							b[neighbor[j]] = 'B';
							if(checkCloseMill(b, neighbor[j])){
								generateRemoveWhite(b, list);
							} else{
								list.add(b);
							}
						}
					}
				}
			}
			return list;
		}
		
		public static void generateRemoveBlack(char[] inputBoard, ArrayList<char[]>list){
			//System.out.println("Remove Black");
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
			/*if(flag==false){
				list.add(inputBoard);
			}*/
			list.add(inputBoard);
		}
		
		public static void generateRemoveWhite(char[] inputBoard, ArrayList<char[]> list){
			//System.out.println("Remove Black");
			
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
		
		public static int[] neighborsList(int location){
			switch(location) {
			case 0 : 
				int[] list0 = {1, 3, 8};
				return list0;
			//return list0;
			case 1 : 
				int[] list1 = {0, 4, 2};
				return list1;
			case 2 : 
				int[] list2 = {1, 5, 13};
				return list2;
			case 3 : 
				int[] list3 = {9, 4, 6, 0};
				return list3;
			case 4 : 
				int[] list4 = {1, 3, 5};
				return list4;
			case 5 : 
				int[] list5 = {4, 7, 12, 2};
				return list5;
			case 6 : 
				int[] list6 = {10, 3, 7};
				return list6;
			case 7 : 
				int[] list7 = {11, 6, 5};
				return list7;
			case 8 : 
				int[] list8 = {0, 9, 20};
				return list8;
			case 9 : 
				int[] list9 = {8, 10, 3, 17};
				return list9;
			case 10 : 
				int[] list10 = {9, 6, 14};
				return list10;
			case 11 : 
				int[] list11 = {7, 16, 12};
				return list11;
			case 12 : 
				int[] list12 = {11, 13, 5, 19};
				return list12;
			case 13 : 
				int[] list13 = {12, 2, 22};
				return list13;
			case 14 : 
				int[] list14 = {17, 10, 15};
				return list14;
			case 15 : 
				int[] list15 = {14, 16, 18};
				return list15;
			case 16 : 
				int[] list16 = {11, 15, 19};
				return list16;
			case 17 : 
				int[] list17 = {14, 9, 18, 20};
				return list17;
			case 18 : 
				int[] list18 = {17, 15, 19, 21};
				return list18;
			case 19 : 
				int[] list19 = {16, 12, 18, 22};
				return list19;
			case 20 : 
				int[] list20 = {17, 21, 8};
				return list20;
			case 21 : 
				int[] list21 = {20, 22, 18};
				return list21;
			case 22 : 
				int[] list22 = {21, 13, 19};
				return list22;
			default : return null;
			}
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
		
		public static int staticEstimation(char[] board){
			if(numWhitePieces(board) <= 2){
				return Integer.MAX_VALUE;
			} else{
				if(numBlackPieces(board) <= 2){
					return Integer.MIN_VALUE;
				} else{
					if(numWhiteMoves(board) == 0){
						return Integer.MAX_VALUE;
					} else{
						return ((1000 * (numBlackPieces(board) - numWhitePieces(board))) - numWhiteMoves(board));
					}
				}
			}
		}
		
		public static int numWhiteMoves(char[] board){
			ArrayList<char[]> list = generateMovesMidGameEndGameWhite(board);
			return list.size();
		}
}
