package apps.project.controller.mstva;

import apps.framework.object.CmMap;
import apps.project.service.mstva.MstVaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MstVaController implements Job {

	private static final Log logger = LogFactory.getLog(MstVaController.class);

	private MstVaService MstVaService = new MstVaService();

	public MstVaController() {logger.info("MstVaController init");}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		CmMap reqVo = new CmMap();

		// 동서 데이터 rs로 이관
        getMstVaDataAfterInsert(null);
	}

    public void getMstVaDataAfterInsert(CmMap reqVo) {
        MstVaService.transferDataFromMsSqlToPostGresql(reqVo);
   	}

   	public static void main(String[] args) throws JobExecutionException {
   		new MstVaController().execute(null);
   	}

}
