package components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Credentials {
	
	public String user;
	public String password;
	
	public Credentials() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\isourcing.config"));
			user = reader.readLine();
			password = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
