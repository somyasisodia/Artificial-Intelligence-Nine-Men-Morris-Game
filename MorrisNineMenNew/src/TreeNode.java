import java.util.ArrayList;


public class TreeNode {

		int staticValue;
		ArrayList<TreeNode> childNodes;// = new ArrayList<TreeNode>();
		char[] board;
		
		public TreeNode(){
			staticValue=0;
			childNodes=new ArrayList<TreeNode>();
			board=null;
		}
		
		public int getstaticValue() {
			return staticValue;
		}

		public void setstaticValue(int staticValue) {
			this.staticValue = staticValue;
		}

		public ArrayList<TreeNode> getChildNodes() {
			return childNodes;
		}

		public void setChildNodes(ArrayList<TreeNode> childNodes) {
			this.childNodes = childNodes;
		}

		public char[] getboard() {
			return board;
		}

		public void setboard(char[] board) {
			this.board = board;
		}
		
}
