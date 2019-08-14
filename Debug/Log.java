package Debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Log {
	public static File errorLog = new File("error.txt");
	public static PrintWriter writer;
	
	public Log() {
		try {
			writer = new PrintWriter(errorLog);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void error(String error) {
		writer.println(error);
	}
}
