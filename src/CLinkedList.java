public class CLinkedList {
	private Node dummy;
	private int length;
	public static void main(String[] args){
		CLinkedList c=new CLinkedList();

	
		System.out.println(c);
	}
	@Override
	public String toString(){
		Node n=getFirst();
		String returnString="";
		for(int i=1;i<=length;i++){
			returnString+=((i)+":"+n.value+"\t");
			n=n.next;
		}
		return returnString;
	}
	public int getLength() {
		return length;
	}

	public CLinkedList() {
		length = 0;
		dummy = new Node();
		dummy.next = dummy;
		dummy.prev = dummy;
	}

	void insert(Node p, Node q) {
		length++;
		p.next = q;
		p.prev = q.prev;
		p.prev.next = p;
		p.next.prev = p;
	}

	private void addBefore(int x, Node q) {
		length++;
		Node p = new Node();
		p.value = x;
		insert(p, q);
	}

	public void addFirst(Node n) {
		length++;
		n.prev = dummy;
		n.next = dummy.next;
		n.prev.next = n;
		n.next.prev = n;

	}

	public void addLast(Node n) {
		length++;
		n.prev = dummy.prev;
		n.next = dummy;
		n.prev.next = n;
		n.next.prev = n;
		
	}

	public Node getFirst() {
		return dummy.next;
	}
	public void delete(Node p){
		p.next.prev=p.prev;
		p.prev.next=p.next;
		length--;
	}
	public Node pop(){
		Node t=getFirst();
		delete(t);
		return t;
	}

}
