package apps.project.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AgentProperties {

	static Properties props;


	static {
		props = new Properties();

		try (FileInputStream fis = new FileInputStream("agent.properties")) {
			props.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String get(String propStr) {
		return props.getProperty(propStr);
	}

	public static Properties getProp() {
		return props;
	}

}
