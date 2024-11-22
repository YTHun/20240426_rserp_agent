package apps.project.controller.accthisexp;

import apps.framework.object.CmMap;
import apps.project.service.accthisexp.AcctHisExpService;
import apps.project.service.issaccthis.IssAcctHisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AcctHisExpController implements Job {

	private static final Log logger = LogFactory.getLog(AcctHisExpController.class);

	private AcctHisExpService acctHisExpService = new AcctHisExpService();

	public AcctHisExpController() {logger.info("AcctHisExpController init");}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		CmMap reqVo = new CmMap();

		// 동서 데이터 rs로 이관
        getAcctHisExpDataAfterInsert(null);
	}

    public void getAcctHisExpDataAfterInsert(CmMap reqVo) {
        acctHisExpService.transferDataFromMsSqlToPostGresql(reqVo);
   	}

   	public static void main(String[] args) throws JobExecutionException {
   		new AcctHisExpController().execute(null);
   	}

}
