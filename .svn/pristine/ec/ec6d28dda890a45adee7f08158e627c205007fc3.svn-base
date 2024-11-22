package apps.project.service.accthisexp;

import apps.framework.object.CmMap;
import apps.framework.object.CmResMap;
import apps.framework.service.CmService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class AcctHisExpService extends CmService {

	private static final Log logger = LogFactory.getLog(AcctHisExpService.class);

	public List<CmResMap> transferDataFromMsSqlToPostGresql(CmMap reqVo) {
		// dataerp postgresql
		SqlSession postGresqlSession = openSession();
		// 동서 mssql
		SqlSession msSqlSession     = openSession2();
        CmMap updateDatas           = new CmMap();
		List<CmResMap> resultList   = new ArrayList<CmResMap>();

        // 입금적요
        // 자동수납
		try {
			// 동서 getReceiptAutoJy 데이터
			List<CmResMap> autoReceiptJyDatas = cmDao.getList(postGresqlSession, "AutoReceipt_PostGresql_Dao.getReceiptAutoJy.select", reqVo);
			for (CmMap data : autoReceiptJyDatas) {
				try {
                    // DS 에 반영
                    if (data.get("status").equals("C")) {
                        cmDao2.insert(msSqlSession, "AutoReceipt_MsSql_Dao.insertAcctHisExp.insert", data);
                    }else if(data.get("status").equals("U")) {
                        logger.info(data.get("status"));
                        cmDao2.update(msSqlSession, "AutoReceipt_MsSql_Dao.updateAcctHisExp.update", data);
                    }else if(data.get("status").equals("D")) {
                        logger.info(data.get("status"));
                        cmDao2.delete(msSqlSession, "AutoReceipt_MsSql_Dao.deleteAcctHisExp.delete", data);
                    }
                    // 반영 후 상태 변경
                    cmDao.update(postGresqlSession, "AutoReceipt_PostGresql_Dao.getReceiptAutoJy.update", data);
					postGresqlSession.commit();
					msSqlSession.commit();
				} catch (Exception e) {
					logger.error(e);
					rollBack(postGresqlSession, msSqlSession);
				}
			}

		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (null != postGresqlSession) {
				postGresqlSession.close();
			}
			if (null != msSqlSession) {
				msSqlSession.close();
			}
		}
		return resultList;
	}
}
