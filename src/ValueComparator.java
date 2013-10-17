import java.util.Comparator;
import java.util.HashMap;

class ValueComparator implements Comparator<Character> {
	HashMap<Character, Integer> t;

	public ValueComparator(HashMap<Character, Integer> t) {
		this.t = t;
	}

	@Override
	public int compare(Character o1, Character o2) {
		// TODO Auto-generated method stub
		return (t.get(o1) < t.get(o2)) ? -1 : 1;
	}

}