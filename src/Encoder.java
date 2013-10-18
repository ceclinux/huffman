import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.TreeMap;

public class Encoder {
	private static final int BYTELONG = 8;
	private static final int BINARY = 2;
	private static final String COMPRESSED_FILE = "abc.dat";
	private static final String DIC_FILE = "abc.dic";
	private static final String FILENAME = "莎士比亚全集英文版.txt";
	// The map to store encoding
	static int[] freqArr = new int[127];
	// The map to store frequency
	static HashMap<Character, String> encodeMap = new HashMap<Character, String>();

	static int delLen;

	public static void main(String[] args) throws IOException {
		// 400ms
		long start = System.currentTimeMillis();
		StringBuilder content = readCode(freqArr);
		long end = System.currentTimeMillis();
		System.out.println(end - start);

		CLinkedList c = iniHuffList();

		Node root = buildHuffTree(c);

		getHuffEncode(root, "", encodeMap);
		// 1200ms
		start = System.currentTimeMillis();

		// char[] i=content.toString().toCharArray();
		// 比上面大约省了5毫秒= =
		delLen = fulltoWrite(sbToCharArr(content));
		end = System.currentTimeMillis();
		System.out.println(end - start);

		// 经过测试，此时间忽略不计
		Encoder.write(DIC_FILE, writeFormat(encodeMap));

	}

	private static char[] sbToCharArr(StringBuilder content) {
		char[] i = new char[content.length()];
		for (int y = 0; y < i.length; y++)
			i[y] = content.charAt(y);
		return i;
	}

	private static Node buildHuffTree(CLinkedList c) {
		Node z = null;
		int length = c.getLength();
		for (int t = 1; t < length; t++) {
			z = new Node();
			Node x = c.pop();
			z.left = x;
			Node y = c.pop();
			z.right = y;
			z.setValue(x.value + y.value);
			sortTree(c, z);
		}
		return z;
	}

	private static void sortTree(CLinkedList c, Node z) {
		int p = c.getLength();
		int state = 0;
		Node o = c.getFirst();
		for (int q = 0; q < p; q++) {
			if (z.value <= o.value) {
				c.insert(z, o);
				state = 1;
				break;
			}
			o = o.next;
		}
		if (state == 0) {
			c.addLast(z);
		}
	}

	private static CLinkedList iniHuffList() {
		Node[] node = new Node[freqArr.length];

		for (int n = 0; n < node.length; n++) {
			node[n] = new Node((char) n, freqArr[n]);
		}
		CLinkedList c = new CLinkedList();
		for (Node no : node) {
			c.addLast(no);
		}
		return c;
	}

	private static StringBuilder readCode(int[] freqArr) throws IOException {
		StringBuilder content = new StringBuilder("");

		try {
			BufferedReader br = new BufferedReader(new FileReader(FILENAME));
			int i;
			while ((i = br.read()) != -1) {
				char c = (char) i;
				content.append(c);
				freqArr[c]++;
			}

		} catch (FileNotFoundException f) {
			System.out.println("no file " + FILENAME + " found");
			f.printStackTrace();
		}
		return content;

	}

	private static int fulltoWrite(char[] c) throws IOException {
		BufferedOutputStream bf = new BufferedOutputStream(
				new FileOutputStream(COMPRESSED_FILE));
		StringBuilder t = new StringBuilder();
		// StringBuilder s = null;
		/**
		 * 不断的写入8bit长的字，最后多余不到的另外考虑
		 */
		long start = System.currentTimeMillis();
		for (int i = 0; i < c.length; i++) {
			writeBuffer(t.append(encodeMap.get(c[i])), bf);
		}
		long end = System.currentTimeMillis();
		System.out.println("here" + (end - start));

		int s1 = Integer.parseInt(t.toString(), BINARY);
		delLen = BYTELONG - t.length();
		bf.write(s1 << delLen);

		bf.close();
		return delLen;
	}

	private static StringBuilder writeBuffer(StringBuilder i,
			BufferedOutputStream bf) throws IOException {
		// TODO Auto-generated method stub
		
		// if (leng >= BYTELONG) {
		// bf.write(Integer.parseInt(i.substring(0, BYTELONG), BINARY));
		// return writeBuffer(i.delete(0, BYTELONG), bf);
		// }
		int requirelen = i.length()-BYTELONG;
		int q;
		for (q = 0; q < requirelen;) {
			bf.write(Integer.parseInt(i.substring(q, q += BYTELONG), BINARY));
		}
		return i.delete(0, q);

		// for(int leng = i.length();leng>8;leng-=8){
		// bf.write(Integer.parseInt(i.substring(0, BYTELONG), BINARY));
		// }
		// return i;

	}

	private static String writeFormat(HashMap<Character, String> map)
			throws IOException {
		// TODO Auto-generated method stub
		StringBuilder s = new StringBuilder();
		for (Character c : map.keySet()) {
			s.append((int) c + " " + map.get(c) + "\n");
		}
		return s.toString() + "extra" + " " + delLen;
	}

	static void getHuffEncode(Node root, String encode,
			HashMap<Character, String> h) {
		if (root != null) {
			if (root.right == null && root.right == null) {
				// System.out.println(root.key + ": " + encode);
				h.put(root.key, encode + "");
			}
			if (root.right != null) {
				getHuffEncode(root.right, encode + 1, h);
			}
			if (root.left != null) {
				getHuffEncode(root.left, encode + 0, h);
			}

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
