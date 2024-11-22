package apps.project.controller.scraping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import javax.servlet.http.HttpServletRequest;

import apps.framework.object.CmMap;
import apps.project.service.scraping.ScrapingService;

public class ScripingController implements Job {

	private static final Log logger = LogFactory.getLog(ScripingController.class);

	ScrapingService scrapingService = new ScrapingService();

	public ScripingController() {
		logger.info("TestConnectController");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("job execute");
	}
	
	/* 홈택스연동 */
	@SuppressWarnings("rawtypes")
	public void hometaxResult(HttpServletRequest req, CmMap reqVo) {
		try {
			scrapingService.HometaxResult(req, reqVo);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
}
