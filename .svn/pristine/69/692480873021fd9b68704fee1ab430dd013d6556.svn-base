package apps.framework.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import apps.framework.dao.CmDao;
import apps.project.db.MyBatisConnectionFactory;

/**
 * IoC 컨테이너 개념 없이 서비스 레이어 하드코딩 함
 * 
 * */
public class CmService {

	protected Logger logger = LogManager.getLogger(this.getClass());
	
	
	protected CmDao cmDao;
	protected CmDao cmDao2;

	
	public CmService() {
		cmDao = new CmDao();
		cmDao2 = new CmDao();

		cmDao.setSqlSessionFactory(MyBatisConnectionFactory.getSqlSessionFactory());
		cmDao2.setSqlSessionFactory(MyBatisConnectionFactory.getSqlSessionFactory2());
	}


	/**
	 * XXX: 무의미한 공통코드일 수 있으나 묶어둠
	 * */
	protected SqlSession openSession() {
		return cmDao.openSession();
	}

	protected SqlSession openSession2() {
		return cmDao2.openSession();
	}

	protected void commit(SqlSession session, SqlSession session2) {
		session.commit();
		session.close();

		session2.commit();
		session2.close();
}

	protected void rollBack(SqlSession session, SqlSession session2) {
		session.rollback();
		session.close();

		session2.rollback();
		session2.close();
	}

}
