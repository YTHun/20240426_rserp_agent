package apps.project.service.mstva;

import apps.framework.object.CmMap;
import apps.framework.object.CmResMap;
import apps.framework.service.CmService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class MstVaService extends CmService {

	private static final Log logger = LogFactory.getLog(MstVaService.class);

	public List<CmResMap> transferDataFromMsSqlToPostGresql(CmMap reqVo) {
		// dataerp postgresql
		SqlSession postGresqlSession = openSession();
		// 동서 mssql
		SqlSession msSqlSession     = openSession2();
		List<CmResMap> resultList   = new ArrayList<CmResMap>();
        CmMap Crflag                = new CmMap();
        CmMap selectRows            = new CmMap();

		try {

            // 시작 전 기존에 '1' 인 건은 다시 '0' 으로 변경 예외
            cmDao.update(msSqlSession, "MstVa_MsSql_Dao.updateMSTvaDANCODEBf.update", reqVo);
            msSqlSession.commit();
            //별도 Stats flag 를 두면 좋을텐데

            String[] rflag = new String[2];
            rflag[0] = "0"; //신규
            rflag[1] = "7"; //삭제단지

            int ii = 0;
            while (ii < 2) {

                Crflag.put("rflag", rflag[ii]);
                // 대상 건수 체크
                int getCount = cmDao2.getCount(msSqlSession, "MstVa_MsSql_Dao.getMstVaDanCnt.select", Crflag);

                if(getCount > 0) {
                    // 동서 IssAcctHis 데이터
                    List<CmResMap> getMstVaDan = cmDao2.getList(msSqlSession, "MstVa_MsSql_Dao.getMstVaDanGroup.select", Crflag);
                    //작업중으로변경
                    cmDao.update(msSqlSession, "MstVa_MsSql_Dao.updateMSTvaDANCODE.update", Crflag);
                    msSqlSession.commit();
                    // 신규일떄
                    if (ii == 0) {
                        cmDao.delete(postGresqlSession, "MstVa_PostGresql_Dao.deleteMstVaDan.delete", getMstVaDan);
                        // 500건식 나눠서
                        int i = 1;
                        while (((i - 1) * 1000) < getCount) {
                            try {
                                selectRows.put("strrows", (i - 1) * 1000);
                                selectRows.put("endrows", 1000);

                                List<CmResMap> autoPayDatas = cmDao2.getList(msSqlSession, "MstVa_MsSql_Dao.getMstVaDan.select", selectRows);
                                cmDao.insert(postGresqlSession, "MstVa_PostGresql_Dao.insertMstVa.insert", autoPayDatas);

                                //1000건씩 Commit
                                postGresqlSession.commit();
                                msSqlSession.commit();
                            } catch (Exception e) {
                                logger.error(e);
                                rollBack(postGresqlSession, msSqlSession);
                            }
                            i++;
                        }

                        int getCount2 = cmDao2.getCount(msSqlSession, "MstVa_MsSql_Dao.getMstVaMemCnt.select", Crflag);

                        // 500건식 나눠서
                        i = 1;
                        while (((i - 1) * 1000) < getCount2) {
                            try {
                                selectRows.put("strrows", (i - 1) * 1000);
                                selectRows.put("endrows", 1000);

                                List<CmResMap> autoPayDatas = cmDao2.getList(msSqlSession, "MstVa_MsSql_Dao.getMstVaMem.select", selectRows);
                                cmDao.insert(postGresqlSession, "MstVa_PostGresql_Dao.insertMstVaMem.insert", autoPayDatas);

                                //1000건씩 Commit
                                postGresqlSession.commit();
                                msSqlSession.commit();
                            } catch (Exception e) {
                                logger.error(e);
                                rollBack(postGresqlSession, msSqlSession);
                            }
                            i++;
                        }
                        //작업완료 변경
                        cmDao.update(msSqlSession, "MstVa_MsSql_Dao.updateMSTvaDANCODEend.update", Crflag);
                        msSqlSession.commit();
                    } else {
                        //작업완료 삭제
                        cmDao.delete(postGresqlSession, "MstVa_PostGresql_Dao.deleteMstVaDan.delete", getMstVaDan);
                        cmDao.delete(msSqlSession, "MstVa_MsSql_Dao.deleteMSTvaDANCODE.delete", getMstVaDan);
                        postGresqlSession.commit();
                        msSqlSession.commit();
                    }
                }
                ii++;
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
