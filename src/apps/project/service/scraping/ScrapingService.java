package apps.project.service.scraping;

import apps.framework.object.CmMap;
import apps.framework.object.CmResMap;
import apps.framework.service.CmService;
//import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
//import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("rawtypes")
public class ScrapingService extends CmService {

	private static final Log logger = LogFactory.getLog(ScrapingService.class);
	
	public void HometaxResult(HttpServletRequest req, CmMap reqVo) {
		SqlSession postGresqlSession = openSession();	// dataerp postgresql
		SqlSession msSqlSession      = openSession2();	// 동서 mssql
		
        BufferedReader br = null;
		String readLine = null;
		
		try {
        	//body내용 inputstream에 담는다.
    		StringBuilder reqSB = new StringBuilder();
            InputStream inputStream = req.getInputStream();

            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((readLine = br.readLine()) != null) {
            	reqSB.append(readLine);
            }

            String reqBody = reqSB.toString();
            //----logger.info("Data :" + reqBody);
            
			JSONObject reqObj = new JSONObject(reqBody);		// JsonObject로 변환
			JSONObject dataReq = reqObj.getJSONObject("datareq");		// datareq추출
			//logger.info("ScrapingService => HometaxResult => datareq : " + dataReq.toString());
			
			String ls_comp = dataReq.getString("s_comp");
			String ls_proj = dataReq.getString("s_proj");
			reqVo.put("s_bizno", dataReq.getString("s_bizno"));
			reqVo.put("s_frdt" , dataReq.getString("s_frdt"));
			reqVo.put("s_todt" , dataReq.getString("s_todt"));
			logger.info("ScrapingService => HometaxResult start : comp:"+ls_comp+", dancd:"+ls_proj);
			
			// 세금계산서
			List<CmResMap> etaxBillResultDatas = cmDao2.getList(msSqlSession, "Scraping_MsSql_Dao.ETAXBILL_RESULT_TB.select", reqVo);
			//logger.info("ScrapingService => ETAXBILL_RESULT_TB => after : ");
			for (CmMap data : etaxBillResultDatas) {
				try {
					data.put("company_code", ls_comp);
					data.put("proj_code", ls_proj);
					data.put("sell_code", "30");
                    cmDao.insert(postGresqlSession, "Scraping_PostGresql_Dao.scrap_ETAXBILL.insert", data);
					postGresqlSession.commit();
				} catch (Exception e) {
					logger.error(e);
					rollBack(postGresqlSession, msSqlSession);
				}
			}
			//logger.info("ScrapingService => HometaxResult => scrap_ETAXBILL.insert : ");
			
			// 현금영수증
			List<CmResMap> cashBillResultDatas = cmDao2.getList(msSqlSession, "Scraping_MsSql_Dao.CASHBILL_RESULT_TB.select", reqVo);
			for (CmMap data : cashBillResultDatas) {
				try {
					data.put("company_code", ls_comp);
					data.put("proj_code", ls_proj);
					data.put("sell_code", "30");
                    cmDao.insert(postGresqlSession, "Scraping_PostGresql_Dao.scrap_CASHBILL.insert", data);
					postGresqlSession.commit();
				} catch (Exception e) {
					logger.error(e);
					rollBack(postGresqlSession, msSqlSession);
				}
			}

			// 신용카드
			List<CmResMap> creditCardResultDatas = cmDao2.getList(msSqlSession, "Scraping_MsSql_Dao.CREDIT_CARD_RESULT_TB.select", reqVo);
			for (CmMap data : creditCardResultDatas) {
				try {
					data.put("company_code", ls_comp);
					data.put("proj_code", ls_proj);
					data.put("sell_code", "30");
                    cmDao.insert(postGresqlSession, "Scraping_PostGresql_Dao.scrap_CREDIT_CARD.insert", data);
					postGresqlSession.commit();
				} catch (Exception e) {
					logger.error(e);
					rollBack(postGresqlSession, msSqlSession);
				}
			}
			
			reqVo.put("r_code", "OK");
			reqVo.put("r_msg", "처리되었습니다.");
		} catch (Exception ex) {
			reqVo.put("r_code", "EX");
			reqVo.put("r_msg", "ERROR");
    		logger.info("ScrapingService => HometaxResult carch Error : " + ex.getMessage());
			//logger.error(e);
		} finally {
			if (null != postGresqlSession) {
				postGresqlSession.close();
			}
			if (null != msSqlSession) {
				msSqlSession.close();
			}
		}
		
		logger.info("ScrapingService => HometaxResult End ....");
	}
}