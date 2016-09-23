package tankWar;

import java.io.IOException;
import java.util.Properties;

public class ProperMgr {
	// Singleton Pattern
	// Create an object to read config file
	static Properties props = new Properties();

	static {
		try {
			props.load(ProperMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private ProperMgr() {
		
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

}
