public class Node {
	public Node prev, next, right, left;
	public int value;
	public char key;

	// private int value;
	public Node(char key,int value) {
		this.key=key;
		this.value = value;
	}

	public Node() {
	}

	public void setLeft(Node l) {
		left = l;
	}

	public void setRight(Node r) {
		right = r;
	}

	public void setValue(int v) {
		value = v;
	}
	// public int getValue(){
	// return value;
	// }

}