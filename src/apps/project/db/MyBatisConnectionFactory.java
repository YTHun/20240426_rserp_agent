package apps.project.db;


import apps.project.util.AgentProperties;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisConnectionFactory {

	private static SqlSessionFactory sqlSessionFactory;
	private static SqlSessionFactory sqlSessionFactory2;

	static {
		try (Reader reader = Resources.getResourceAsReader("apps/project/db/mybatis_config.xml");
			Reader reader2 = Resources.getResourceAsReader("apps/project/db/mybatis_config2.xml")) {

			if (sqlSessionFactory == null) {
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, AgentProperties.getProp());
				sqlSessionFactory2 = new SqlSessionFactoryBuilder().build(reader2, AgentProperties.getProp());
			}
		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public static SqlSessionFactory getSqlSessionFactory2() {
		return sqlSessionFactory2;
	}
}
