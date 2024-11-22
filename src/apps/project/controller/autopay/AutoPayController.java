package apps.project.controller.autopay;

import apps.framework.object.CmMap;
import apps.project.service.autopay.AutoPayService;
import apps.project.util.AgentProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Properties;

public class AutoPayController implements Job {

	private static final Log logger = LogFactory.getLog(AutoPayController.class);

	private AutoPayService autoPayService = new AutoPayService();

	public AutoPayController() {
		logger.info("AutoPayController init");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
        Properties prop = AgentProperties.getProp();
        CmMap CmMap = new CmMap();
        CmMap.put("comp" , prop.getProperty("COMPCODE"));
		getAutoPayDataAfterInsert(CmMap);
	}

	public void getAutoPayDataAfterInsert(CmMap reqVo) {
		autoPayService.getAutoPayDataAfterInsertDb(reqVo);
	}

	public static void main(String[] args) throws JobExecutionException {
		new AutoPayController().execute(null);
	}

}
