import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CecBufferedOutPutStream extends BufferedOutputStream {

	public CecBufferedOutPutStream(OutputStream out, int size) {
		super(out, size);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Writes the specified byte to this buffered output stream.
	 * 
	 * @param b
	 *            the byte to be written.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public synchronized void write(int[] b) throws IOException {

		out.write(buf, 0, count);
		count = 0;

		for (int i = 0; i < b.length; i += buf.length) {
			for (int s = 0; s <= i; s++) {
				buf[count++] = (byte) b[s];
			}
			out.write(buf, 0, count);
			count = 0;
		}
	}

}
