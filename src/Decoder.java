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
		File f = new File(args[0]);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		BufferedReader bf = new BufferedReader(new FileReader(args[1]));
		String s;

		while (!(s = bf.readLine()).startsWith("extra")) {
			String[] table = s.split(" ");
			map.put(table[1], Integer.parseInt(table[0]));
		}
		// the number of shift space for the last bit
		int shift = Integer.parseInt(s.split(" ")[1]);
		long star = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		byte[] encodeArray = new byte[(int) f.length()];

		while (in.available() >= 1) {
			in.read(encodeArray, 0, encodeArray.length);

			for (int i = 0; i < encodeArray.length; i++) {
				// System.out.println(encodeArray[i]);

				// System.out.println("b: "+b);
				String encodeString = Integer
						.toBinaryString(encodeArray[i] & 0xff);
				// System.out.println("encodeString: "+encodeString);
				int shiftLeng = 8 - encodeString.length();
				for (int t = 0; t < shiftLeng; t++) {
					encodeString = '0' + encodeString;
				}
				sb.append(encodeString);
			}

		}
		long start2 = System.currentTimeMillis();

		System.out.println(start2 - star);
		sb = sb.delete(sb.length() - shift, sb.length());

		Integer t;
		int start = 0;
		StringBuilder outtext = new StringBuilder();
		int sblength = sb.length();
		for (int end = 1; end <= sblength; end++) {
			if ((t = map.get(sb.substring(start, end))) != null) {

				outtext.append((char) (int) t);

				start = end;
			}
		}
		Decoder.write(args[2], outtext.toString());

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

			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
