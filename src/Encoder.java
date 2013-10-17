import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.TreeMap;

public class Encoder {
	static HashMap<Character, String> freqmap = new HashMap<Character, String>();
	static int pianyi;

	public static void main(String[] args) throws IOException {
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		ValueComparator vcomp = new ValueComparator(map);
		TreeMap<Character, Integer> huffMap = new TreeMap<Character, Integer>(
				vcomp);
		BufferedReader br = new BufferedReader(new FileReader(
				"莎士比亚全集英文版.txt"));
		int i;
		StringBuilder l = new StringBuilder("");
		while ((i = br.read()) != -1) {
			char c=(char) i;
			l.append(c);
			Integer freq = map.get((char) i);
			map.put(c, freq == null ? 1 : freq + 1);
		}
		// String s;

		// while ((s = br.readLine()) != null) {
		// l.append(s+'\n');
		// // System.out.println(l);
		// for (int i = 0; i < s.length(); i++) {
		// char u = s.charAt(i);
		// Integer freq = map.get(u);
		// map.put(u, freq == null ? 1 : freq + 1);
		// }
		// Integer freq = map.get(10);
		// map.put((char) 10, freq == null ? 0 : freq + 1);
		// }
		// l.deleteCharAt(l.length()-1);
//		 System.out.println(map);

		huffMap.putAll(map);
		Node[] node = new Node[huffMap.size()];

		int n = 0;
		for (Character m : huffMap.keySet()) {
			node[n++] = new Node(m, map.get(m));
		}
		CLinkedList c = new CLinkedList();
		for (Node no : node) {
			c.addLast(no);
		}
		// System.out.println("c: "+c.getLength());
		// System.out.println("c: first: "+c.getFirst().elem);
		Node z = null;
		int length = c.getLength();
		for (int t = 1; t < length; t++) {
			z = new Node();
			Node x = c.pop();
			// System.out.println("After pop once: " + c);
			z.left = x;
			Node y = c.pop();
			// System.out.println("After pop twice: " + c);
			z.right = y;
			z.setValue(x.value + y.value);

			int q;
			Node o;
			int p = c.getLength();
			int state = 0;
			for (q = 0, o = c.getFirst(); q < p; q++) {
				// System.out.println("current o is: " + o.value);
				// System.out.println("current z is: "+z.elem);
				// System.out.println("current c is: " + c);
				if (z.value <= o.value) {
					// System.out.print(o.elem);

					c.insert(z, o);
					// System.out.println("z.prev.next: "+z.prev.next.elem);
					// System.out.println("z.next.prev: "+z.next.prev.elem);
					// System.out.println("find somewhere to insert: " + c);
					state = 1;
					break;
				}
				o = o.next;

			}
			if (state == 0) {
				c.addLast(z);
				// System.out.println(c);
				// System.out.println("nowhere to insert: " + c);
			}
		}
		printHuffEncode(z, "", freqmap);
		// System.out.println(map);
		// writeEncode("abc.bat", l.toString(), map);
		// writeFormat(map, new File("abc.dic"));
		// writeFormat(map, new File("abc.dic"));

		// char[] c1 = l.toString().toCharArray();
		pianyi = fulltoWrite(l.toString().toCharArray());
		// bf.close();
		write("abc.dic", writeFormat(freqmap));

	}

	private static int fulltoWrite(char[] c) throws IOException {
		BufferedOutputStream bf = new BufferedOutputStream(
				new FileOutputStream("abc.dat"));
		String t = "";
		int q;

		for (int i = 0; i < c.length; i++) {
			String m = (freqmap.get(c[i]));
			// System.out.println("m: "+m);
			t = writeBuffer(t + m, bf);
			// System.out.println("t value: "+t);
		}
		try {
			int s = Integer.parseInt(t, 2);

			for (pianyi = 0; pianyi < (8 - t.length()); pianyi++) {
				s = (s << 1);
//				System.out.println(s);
			}
			bf.write(s);
			// bf.write(-1);
//			System.out.println(Integer.toBinaryString(s));
		} catch (Exception e) {
			bf.write(0);
		}
		bf.close();
		return pianyi;
	}

	private static int getlog2(int t) {
		// TODO Auto-generated method stub
		int degree = 1;

		for (int i = 1; i < 8; i++) {
			if ((t >>> i) > 0) {
				degree++;
			} else {
				break;
			}
		}
		return degree;
	}

	private static String writeBuffer(String i, BufferedOutputStream bf)
			throws IOException {
		// TODO Auto-generated method stub
		int leng = i.length();
		if (leng >= 8) {
			// System.out.println(i & 0xff);
			// System.out.print(i.substring(0,8));
			bf.write(Integer.parseInt(i.substring(0, 8), 2));

			return writeBuffer(i.substring(8, leng), bf);
		} else {
			return i;
		}

	}

	private static String writeFormat(HashMap<Character, String> map)
			throws IOException {
		// TODO Auto-generated method stub

		StringBuilder s = new StringBuilder();
		for (Character c : map.keySet()) {
			s = s.append((int) c + " " + map.get(c) + "\n");
		}
		return s.toString() + "extra" + " " + pianyi;
	}

	static void printHuffEncode(Node root, String encode,
			HashMap<Character, String> h) {
		if (root != null) {
			if (root.right == null && root.right == null) {
//				System.out.println(root.key + ": " + encode);
				h.put(root.key, encode + "");
			}
			if (root.right != null) {
				printHuffEncode(root.right, encode + 1, h);
			}
			if (root.left != null) {
				printHuffEncode(root.left, encode + 0, h);
			}

		}
	}

	public static void writeEncode(String filename, String text,
			HashMap<Character, Integer> t) {
		try {
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(filename));
			try {

				// out.write(text);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void write(String filename, String text) {
		try {
			PrintWriter out = new PrintWriter(
					new File(filename).getAbsoluteFile());
			try {
				out.print(text);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
