import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.HashMap;

public class Decoder {
	public static void main(String[] args) throws IOException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				"abc.dat"));
		BufferedReader bf = new BufferedReader(new FileReader("abc.dic"));
		String s;

		while (!(s = bf.readLine()).startsWith("extra")) {
			String[] table = s.split(" ");
			map.put(table[1], Integer.parseInt(table[0]));
		}
		
		int pianyi=Integer.parseInt(s.split(" ")[1]);
		System.out.println(pianyi);
		int ch;
		StringBuilder sb = new StringBuilder();

		while (in.available() >= 1) {
			ch = in.read();
			String bs = Integer.toBinaryString(ch);
			int bslength = bs.length();
			if (bslength < 8) {
				for (int i = 0; i < 8 - bslength; i++) {
					bs = '0' + bs;
				}
			}
			
			sb.append(bs);
			
			// int t=in.read();
			// sb.append(Integer.toBinaryString(t));
			

			// Integer t;
			// if((t=map.get(ch))==null){
			// t=map.get((map.get(ch)>>8)+t);
			// }
			// int m=t;
			// sb.append((char) m);
			// System.out.println(sb);

		}
//		System.out.println(sb);
//		System.out.println( sb);
//		int z=in.read();
//		sb.append(Integer.toBinaryString(z));
//		System.out.println(sb);
		// int s1=in.read();
		// sb.append(Integer.toBinaryString(s1));
		sb=sb.delete(sb.length()-pianyi, sb.length());
//		 System.out.println( sb);
		// System.out.println(sb);
		Integer t;
		int start = 0;
		StringBuilder outtext = new StringBuilder();
		for (int end = 1; end <= sb.length(); end++) {
			// System.out.println(sb.substring(start, end));
			// System.out.println(map.get(sb.substring(start, end)));
			if ((t = map.get(sb.substring(start, end))) != null) {

				outtext.append((char) (int) t);

//				System.out.println(t);
				start = end;
			}
		}
		// outtext.deleteCharAt(outtext.length()-1);
//		System.out.println(outtext);
		Decoder.write("abc-final", outtext.toString());

	}

	public static String read(String fileName) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(
					fileName).getAbsolutePath()));
			try {
				String s;
				while ((s = in.readLine()) != null) {
					sb.append(s);
					// sb.append("\n");
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();

	}

	public static void write(String filename, String text) {
		try {
			PrintWriter out = new PrintWriter(
					new File(filename).getAbsoluteFile());
			try {
				out.print(text);
//				out.print('\n');
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
