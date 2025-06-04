package ClassRenamer;

import java.io.*;

public class Utils {
	private static final int BUFFER_SIZE = 1024 * 16;

	public static void streamCopy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];

		int bytesRead;
		try {
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.flush();
		} finally {
			// Nothing to do
		}
	}
}