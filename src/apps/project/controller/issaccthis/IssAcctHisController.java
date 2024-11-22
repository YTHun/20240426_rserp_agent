package apps.project.controller.issaccthis;

import apps.framework.object.CmMap;
import apps.project.service.issaccthis.IssAcctHisService;
import apps.project.util.AgentProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Properties;

public class IssAcctHisController implements Job {

	private static final Log logger = LogFactory.getLog(IssAcctHisController.class);

	private IssAcctHisService issAcctHisService = new IssAcctHisService();

	public IssAcctHisController() {logger.info("IssAcctHisController init");}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Properties prop = AgentProperties.getProp();
        CmMap reqVo = new CmMap();
        reqVo.put("comp" , prop.getProperty("COMPCODE"));

		// 동서 데이터 rs로 이관
        getIssAcctHisDataAfterInsert(reqVo);
	}

    public void getIssAcctHisDataAfterInsert(CmMap reqVo) {
        issAcctHisService.transferDataFromMsSqlToPostGresql(reqVo);
   	}

   	public static void main(String[] args) throws JobExecutionException {
   		new IssAcctHisController().execute(null);
   	}

}
