package apps.project.controller.autoreceipt;

import apps.framework.object.CmMap;
import apps.project.controller.autopay.AutoPayController;
import apps.project.service.autoreceipt.AutoReceiptService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AutoReceiptController implements Job {

	private static final Log logger = LogFactory.getLog(AutoReceiptController.class);

	private AutoReceiptService autoReceiptService = new AutoReceiptService();

	public AutoReceiptController() {logger.info("AutoReceiptController init");}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		CmMap reqVo = new CmMap();

		// 동서 데이터 rs로 이관
        getAutoReceiptDataAfterInsert(null);
	}

    public void getAutoReceiptDataAfterInsert(CmMap reqVo) {
        autoReceiptService.transferDataFromMsSqlToPostGresql(reqVo);
   	}

   	public static void main(String[] args) throws JobExecutionException {
   		new AutoReceiptController().execute(null);
   	}

}
