package apps.project.service.test;

import apps.framework.object.CmMap;
import apps.framework.object.CmResMap;
import apps.framework.service.CmService;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("rawtypes")
public class TestService extends CmService {


	private static final Log logger = LogFactory.getLog(TestService.class);

	public CmResMap dbTest(CmMap reqVo) {
		SqlSession session = openSession();
		SqlSession session2 = openSession2();

		List<CmResMap> dataList = null;
		CmResMap resultMap = new CmResMap();

		// FIXME: 코드 데이터 하드코딩 -> 목적에 맞게 일반화
//		reqVo.put("company_code", "100");
//		reqVo.put("proj_code", "999999");
//		reqVo.put("sell_code", "20");
//
//		logger.debug(reqVo);
//
		try {

			dataList = cmDao.getList(session, "LM20000_Dao.LM20000_1.select", reqVo);

			cmDao2.update(session2, "LM20000_Dao.LM20000_1.update", reqVo);

			if (null == dataList || dataList.isEmpty()) {
				return resultMap;
			}

			for (CmMap data : dataList) {

				cmDao2.update(session2, "LM20000_Dao.LM20000_1.update", reqVo);
			}

			commit(session, session2);
		} catch (Exception e) {
			rollBack(session, session2);
			throw e;
		} finally {
			logger.debug(resultMap);
		}

		return resultMap;
	}
}
