package apps.project.service.autopay;

import apps.framework.object.CmMap;
import apps.framework.object.CmResMap;
import apps.framework.service.CmService;
import apps.framework.utils.CmHttpConnetor;
import apps.framework.utils.CmSecretUtil;
import apps.project.dto.AutoPayDto;
import apps.project.dto.AutoPayResponseDto;
import apps.project.util.AgentProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

@SuppressWarnings("rawtypes")
public class AutoPayService extends CmService {

	private static final Log logger = LogFactory.getLog(AutoPayService.class);

	// 동서 데이터 요청 및 응답 처리
	public void getAutoPayDataAfterInsert(CmMap reqVo) {
		// FIXME : 임시 데이터 ==========
		String dummyCompanyCode = "100";
		String[] dummyDanjis 	= {"9703"};
		String dummyDong 		= "207";
		String dummyHo 			= "ALL";
		// ============================
		// 세션 오픈
		SqlSession postGresqlSession = openSession();

		try {

			// properties 파일에서 동서 암호화 키, 암호화 nonce, agency code 가져오기
			Properties prop 				= AgentProperties.getProp();
			String dongseoEncryptionKey 	= prop.getProperty("DONGSEO_ENCRYPTION_KEY");
			String dongseoEncryptionNonce 	= prop.getProperty("DONGSEO_ENCRYPTION_NONCE");
			String dongseoAgencyCode 		= prop.getProperty("DONGSEO_AGENCY_CODE");
			String dongseoDomain 			= prop.getProperty("DONGSEO_DOMAIN");

			CmMap selectParam = new CmMap();
			List<CmMap> targetDanjiList = new ArrayList<>();

			//TODO danji 정보 rs DB 에서 가져오기
//		selectParam.put("company_code", companyCode);
//		selectParam.put("proj_code", danji);
//		List<CmResMap> targetDanjiList = cmDao.getObject(postGresqlSession, "AutoPayDao.getDanjis", selectParam);

			// FIXME : 임시 데이터 ==========
			for (String dummyDanji : dummyDanjis) {

				CmResMap tempMap = new CmResMap();
				tempMap.put("company_code"	, dummyCompanyCode);
				tempMap.put("proj_code"		, dummyDanji);
				tempMap.put("dong"			, dummyDong);
				tempMap.put("ho"			, dummyHo);

				targetDanjiList.add(tempMap);
			}
			// ============================

			for (CmMap targetDanji : targetDanjiList) {
				CmMap headers = new CmMap();

				//TODO dong, ho 를 ALL or 지정 구분할 로직
				// ........

				// 동서 데이터 요청
				String companyCode 	= targetDanji.getString("company_code");
				String projCode 	= targetDanji.getString("proj_code"); // = dancode
				String dong 		= targetDanji.getString("dong");
				String ho 			= targetDanji.getString("ho");
				String plainText 	= projCode + dong + ho;

				String requestUrl = dongseoDomain + dongseoAgencyCode + "/getAutoPay" + "/" + projCode + "/" + dong + "/" + ho;
				String encodedAccessToken = CmSecretUtil.encodeHmacSha512(plainText, dongseoEncryptionKey, dongseoEncryptionNonce);

				headers.put("Content-Type", "application/json");
				headers.put("Accept", "application/json");
				headers.put("X-Authorization", encodedAccessToken);

				// response : response_code, message, result
				AutoPayResponseDto autoPayResponseDto = CmHttpConnetor.httpGet(AutoPayResponseDto.class, requestUrl, headers, null);

				logger.info("AutoPayResponseDto : " + autoPayResponseDto.toString());

				if (!autoPayResponseDto.getResponse_code().equals("OK")) {
					// response_code 가 OK 가 아닌 경우 로그 남기고 다음으로 넘어감
					logger.error("AutoPayResponseDto : " + autoPayResponseDto.toString());
					continue;
				}
				logger.info("찾아봐랑" + autoPayResponseDto.getResult().size());
				for (AutoPayDto autoPay : autoPayResponseDto.getResult()) {
					try {

						CmMap insertParam = new CmMap();
						insertParam.put("company_code"	, companyCode				);
						insertParam.put("dancode"		, projCode					);
						insertParam.put("dongcode"		, autoPay.getDongCode()		);
						insertParam.put("roomno"		, autoPay.getRoomNo()		);
						insertParam.put("gojicode"		, autoPay.getGojiCode()		);
						insertParam.put("accountno"		, autoPay.getAccountNo()	);
						insertParam.put("admincode"		, autoPay.getAdminCode()	);
						insertParam.put("bankname"		, autoPay.getBankName()		);
						insertParam.put("depositname"	, autoPay.getDepositName()	);
						//insertParam.put("RecKind"		, autoPay.getDepositName()	);
						//insertParam.put("RecBank"		, autoPay.getDepositName()	);
						insertParam.put("ReqDate"		, autoPay.getReqDate());
						insertParam.put("ReqKind"		, autoPay.getReqKind());
						//insertParam.put("WDate"		, autoPay.getDepositName()	);

						cmDao.insert(postGresqlSession, "AutoPay_Dao.insertAutoPay.insert", insertParam);
						postGresqlSession.commit();
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(e);
						// 실패시 해당건만 롤백
						postGresqlSession.rollback();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (postGresqlSession != null) {
				postGresqlSession.close();
			}
		}
	}
	
	// 동서 데이터 요청 및 응답 처리
	public void getAutoPayDataAfterInsertDb(CmMap reqVo) {
		// dataerp postgresql
		SqlSession postGresqlSession = openSession();
		// 동서 mssql
		SqlSession msSqlSession = openSession2();
        //Mssql 총 건수
        CmMap selectRows = new CmMap();

		try {
            // 추후에 RS 운영 중인 단지 리스트로 돌려야
            // 동서 AutoReceipt 데이터 총 건수
			logger.info("1.[PGS]AutoPay 제거");
            cmDao.delete(postGresqlSession, "AutoPay_PostGresql_Dao.deleteAutoPay.delete", reqVo);
			logger.info("2.[MS]AutoPay 카운트 (1000개씩 끊어서 저장)");
            int getCount = cmDao2.getCount(msSqlSession, "AutoPay_MsSql_Dao.getAutoPayCnt.select", reqVo);

            // 500건식 나눠서
            int i = 1;
            while (((i-1)*1000) < getCount){
                try {
                    selectRows.put("strrows"    , (i - 1)*1000);
                    selectRows.put("endrows"    , 1000);
                    selectRows.put("comp"       , reqVo.getString("comp"));

                    List<CmResMap> autoPayDatas = cmDao2.getList(msSqlSession, "AutoPay_MsSql_Dao.getAutoPay.select", selectRows);
                    cmDao.insert(postGresqlSession, "AutoPay_PostGresql_Dao.insertAutoPayList.insert", autoPayDatas);

                    //1000건씩 Commit
                    postGresqlSession.commit();
                    msSqlSession.commit();
                } catch (Exception e) {
                    logger.error(e);
                    rollBack(postGresqlSession, msSqlSession);
                }
                i++;
            }

			logger.info("3.[PGS]AutoPay 등록완료");

			logger.info("4.[PGS]AutoPay_del 삭제 (autopay 이전내용 알기위해 재저장을 위해)");
            cmDao.delete(postGresqlSession, "AutoPay_PostGresql_Dao.deleteAutoPayDel.delete", reqVo);
            // 500건식 나눠서
            // if_autopay에 있는 단지만 Del 가져옴
			logger.info("5.[MS]AutoPay_delete 카운트 (1000개씩 끊어서 저장)");
            List<CmResMap> autoPayDanCodes = cmDao.getList(postGresqlSession, "AutoPay_PostGresql_Dao.getDanCode.select", reqVo);
            for (CmMap data : autoPayDanCodes) {

                selectRows.put("dancode", data.getString("dancode"));
                getCount = cmDao2.getCount(msSqlSession, "AutoPay_MsSql_Dao.getAutoPayDelCnt.select", selectRows);

                i = 1;
                while (((i - 1) * 1000) < getCount) {
                    try {
                        selectRows.put("strrows", (i - 1) * 1000);
                        selectRows.put("endrows", 1000);
                        selectRows.put("comp"   , reqVo.getString("comp"));

                        List<CmResMap> autoPayDatas = cmDao2.getList(msSqlSession, "AutoPay_MsSql_Dao.getAutoPayDel.select", selectRows);
                        cmDao.insert(postGresqlSession, "AutoPay_PostGresql_Dao.insertAutoPayDelList.insert", autoPayDatas);

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
			logger.info("6.[PGS]AutoPay_del 등록완료");


/*            List<CmResMap> autoPayDatas = cmDao2.getList(msSqlSession, "AutoPay_MsSql_Dao.getAutoPay.select", reqVo);
			for (CmMap data : autoPayDatas) {
				try {
					cmDao.insert(postGresqlSession, "AutoPay_PostGresql_Dao.insertAutoPay.insert", data);

					postGresqlSession.commit();
					msSqlSession.commit();
				} catch (Exception e) {
					logger.error(e);
					rollBack(postGresqlSession, msSqlSession);
				}
			}*/

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
	}
}