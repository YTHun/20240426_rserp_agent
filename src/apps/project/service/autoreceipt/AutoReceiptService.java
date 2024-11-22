package apps.project.service.autoreceipt;

import apps.framework.object.CmMap;
import apps.framework.object.CmResMap;
import apps.framework.service.CmService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

@SuppressWarnings("rawtypes")
public class AutoReceiptService extends CmService {

	private static final Log logger = LogFactory.getLog(AutoReceiptService.class);

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

            int getCount = cmDao2.getCount(msSqlSession, "AutoReceipt_MsSql_Dao.getAutoReceiptCnt.select", reqVo);
            // 500건식 나눠서
            int i = 1;
            CmMap selectRows = new CmMap();
			// 동서 AutoReceipt 데이터
            while (((i-1)*1000) < getCount) {
                selectRows.put("strrows" , (i - 1)*1000);
                selectRows.put("endrows" , 1000);

                List<CmResMap> autoReceiptDatas = cmDao2.getList(msSqlSession, "AutoReceipt_MsSql_Dao.getAutoReceipt.select", selectRows);
                for (CmMap data : autoReceiptDatas) {
                    try {
                        List<CmResMap> checklist = cmDao.getList(postGresqlSession, "AutoReceipt_PostGresql_Dao.getReceiptAuto.select", data);
                        if (checklist.get(0).get("chk").toString().equals("Y")) {
                            cmDao.update(postGresqlSession, "AutoReceipt_PostGresql_Dao.updateReceiptAuto.update", data);
                        } else {
                            cmDao.insert(postGresqlSession, "AutoReceipt_PostGresql_Dao.insertReceiptAuto.insert", data);
                        }
                        //updateDatas.put("receipt_yn", "Y");
                        cmDao2.update(msSqlSession, "AutoReceipt_MsSql_Dao.updateReceiptAuto.update", data);
                        postGresqlSession.commit();
                        msSqlSession.commit();
                    } catch (Exception e) {
                        logger.error(e);
                        rollBack(postGresqlSession, msSqlSession);
                    }
                }
                i++;
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
