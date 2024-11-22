package apps.project.controller.test;

import apps.project.service.test.TestService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestConnectController implements Job {

	private static final Log logger = LogFactory.getLog(TestConnectController.class);

	TestService testService = new TestService();

	public TestConnectController() {
		logger.info("TestConnectController");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("job execute");
	}
}
