package apps.project.service.issaccthis;

import apps.framework.object.CmMap;
import apps.framework.object.CmResMap;
import apps.framework.service.CmService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class IssAcctHisService extends CmService {

	private static final Log logger = LogFactory.getLog(IssAcctHisService.class);

	public List<CmResMap> transferDataFromMsSqlToPostGresql(CmMap reqVo) {
		// dataerp postgresql
		SqlSession postGresqlSession = openSession();
		// 동서 mssql
		SqlSession msSqlSession     = openSession2();
		List<CmResMap> resultList   = new ArrayList<CmResMap>();
        CmMap selectRows = new CmMap();
        int getCount = 0;
        int i = 0;

		try {
			// 동서 IssAcctHis 데이터
            // 2024.11.01 운영하는 단지에서만 데이터 가져오도록 수정.

            //홈닷에 등록된 D사 코드값 추출
            List<CmResMap> se_code_proj = cmDao.getList(postGresqlSession, "IssAcctHis_PostGresql_Dao.se_code_proj.select", reqVo);

            //운영하는 단지만 Select 하도록 조정
            //getCount = cmDao2.getCount(msSqlSession, "IssAcctHis_MsSql_Dao.getIssAcctCnt.select", se_code_proj);
            try {

                //운영하는 단지만 삭제
                cmDao.delete(postGresqlSession, "IssAcctHis_PostGresql_Dao.deleteIssAcct.delete", se_code_proj);

                List<CmResMap> autoPayDatas = cmDao2.getList(msSqlSession, "IssAcctHis_MsSql_Dao.getIssAcct.select", se_code_proj);
                cmDao.insert(postGresqlSession, "IssAcctHis_PostGresql_Dao.insertIssAcct.insert", autoPayDatas);

                postGresqlSession.commit();
                msSqlSession.commit();

            } catch (Exception e) {
                logger.error(e);
                rollBack(postGresqlSession, msSqlSession);
            }

            //24.11.19 wdate 기준으로 배치 가져가기
            //Delete Insert 처리 (acct_txday,acct_txtime(?),acct_his_seq 조건으로)


            //전날,당일 delete
            //cmDao.delete(postGresqlSession, "IssAcctHis_PostGresql_Dao.deleteIssAcctHis.delete", reqVo);

            //운영 코드값만 되어 있는 계좌번호 추출해서 가져오기.
            List<CmResMap> acctNos = cmDao2.getList(msSqlSession, "IssAcctHis_MsSql_Dao.getIssAcctNo.select", se_code_proj);

            //bank_cd,acct_no로 His 전날,당일 가져오기
            for (CmMap data : acctNos) {

                selectRows.put("proj_bill",data.get("proj_bill"));
                selectRows.put("bank_cd",data.get("bank_cd"));
                selectRows.put("acct_no",data.get("acct_no"));

                //전날,당일 데이터 Select
                getCount = cmDao2.getCount(msSqlSession, "IssAcctHis_MsSql_Dao.getIssAcctHisCnt.select", selectRows);

                // 500건식 나눠서
                    i = 1;
                while (((i-1)*1000) < getCount){
                    try {
                        selectRows.put("strrows" , (i - 1)*1000);
                        selectRows.put("endrows" , 1000);

                        //전날,당일 데이터 Select
                        List<CmResMap> autoPayDatas = cmDao2.getList(msSqlSession, "IssAcctHis_MsSql_Dao.getIssAcctHis.select", selectRows);

                        //삭제
                        cmDao.delete(postGresqlSession, "IssAcctHis_PostGresql_Dao.deleteIssAcctHis.delete", autoPayDatas);

                        //등록
                        cmDao.insert(postGresqlSession, "IssAcctHis_PostGresql_Dao.insertIssAcctHis.insert", autoPayDatas);

                        //1000건씩 Commit
                        postGresqlSession.commit();
                        msSqlSession.commit();
                    } catch (Exception e) {
                        logger.error(e);
                        rollBack(postGresqlSession, msSqlSession);
                    }
                    i++;
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
