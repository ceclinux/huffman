import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Encoder {
	private static final int BYTELONG = 8;
	private static final int BINARY = 2;
//	private static final String COMPRESSED_FILE = "abc.dat";
//	private static final String DIC_FILE = "abc.dic";
//	private static final String FILE_NAME = "莎士比亚全集英文版.txt";
	// The map to store frequency

	static int[] freqArr = new int[127];
	// The map to store encoding
	static String[] encodeMap = new String[127];

	static int delLen;

	public static void main(String[] args) throws IOException {
		// 100ms之内
		long start = System.currentTimeMillis();
		char[] content = readCode(freqArr,args[0]);
		long end = System.currentTimeMillis();
		System.out.println(end - start);

		CLinkedList c = iniHuffList();

		Node root = buildHuffTree(c);

		getHuffEncode(root, "", encodeMap);
		// 600s
		start = System.currentTimeMillis();

		// char[] i=content.toString().toCharArray();
		// 比上面大约省了5毫秒= =
		delLen = fulltoWrite(content,args[1]);
		end = System.currentTimeMillis();

		System.out.println(end - start);

		// 经过测试，此时间忽略不计
		Encoder.write(args[2], writeFormat(encodeMap));

	}

	private static Node buildHuffTree(CLinkedList c) {
		Node z = null;
		int length = c.getLength();
		for (int t = 1; t < length; t++) {

			Node x = c.pop();
			Node y = c.pop();
			z = new Node();
			z.setLeft(x);
			z.setRight(y);
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

	private static char[] readCode(int[] freqArr,String filename) throws IOException {
		char[] a = null;
		// StringBuilder content = null;
		try {

			File file = new File(filename);
			// System.out.println(file.length());
			// content = new StringBuilder((int) file.length());
			BufferedReader br = new BufferedReader(new FileReader(file));
			a = new char[(int) file.length()];
			// int i;
			br.read(a, 0, a.length);
			for (int i = 0; i < a.length; i++) {
				freqArr[a[i]]++;
			}

		} catch (FileNotFoundException f) {
			System.out.println("no file " + filename + " found");
			f.printStackTrace();
		}
		// System.out.println(content.length());
		return a;

	}

	private static int fulltoWrite(char[] c,String compressedFile) throws IOException {
		BufferedOutputStream bf = new BufferedOutputStream(
				new FileOutputStream(compressedFile), 1024 * 1024);
		StringBuilder t = new StringBuilder();
		// StringBuilder s = null;
		/**
		 * 不断的写入8bit长的字，最后多余不到的另外考虑
		 */
		// long start = System.currentTimeMillis();

		ArrayList<Integer> a = new ArrayList<>();
		for (int i = 0; i < c.length; i++) {
			writeBuffer(t.append(encodeMap[c[i]]), bf);
		}

		int s1 = Integer.parseInt(t.toString(), BINARY);
		delLen = BYTELONG - t.length();
		bf.write(s1 << delLen);

		bf.close();
		return delLen;
	}

	private static void writeBuffer(StringBuilder i, BufferedOutputStream bf)
			throws IOException {
		int requirelen = i.length() - BYTELONG;
		int q = 0;
		while (q < requirelen) {
			bf.write(toBin(i, q, q += BYTELONG));
		}
		i.delete(0, q);
	}

	private static int toBin(StringBuilder i, int q, int j) {
		int binnum = i.charAt(q) - 48;
		for (int m = q + 1; m < j; m++) {
			binnum = (binnum << 1) + (i.charAt(m) - 48);
		}
		return binnum;
	}

	private static String writeFormat(String[] map) throws IOException {
		// TODO Auto-generated method stub
		StringBuilder s = new StringBuilder();
		for (int c = 0; c < map.length; c++) {
			s.append((int) c + " " + map[c] + "\n");
		}
		return s.toString() + "extra" + " " + delLen;
	}

	static void getHuffEncode(Node root, String encode, String[] encodeMap) {
		if (root != null) {
			if (root.right == null && root.right == null) {
//				 System.out.println(root.key + ": " + encode);
				encodeMap[(int) root.key] = encode;
			}
			if (root.right != null) {
				getHuffEncode(root.right, encode + 1, encodeMap);
			}
			if (root.left != null) {
				getHuffEncode(root.left, encode + 0, encodeMap);
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
