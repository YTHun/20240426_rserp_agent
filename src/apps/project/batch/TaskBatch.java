package apps.project.batch;


import apps.framework.service.CmService;
import apps.project.controller.mstva.MstVaController;
import apps.project.controller.accthisexp.AcctHisExpController;
import apps.project.controller.autopay.AutoPayController;
import apps.project.controller.autoreceipt.AutoReceiptController;
import apps.project.controller.issaccthis.IssAcctHisController;
import apps.project.servlet.TestConnectServlet;
import apps.project.servlet.MmsFtpFileUploadServlet;
import apps.project.servlet.ScrapingHometaxServlet;
import apps.project.servlet.autopay.AutoPayServlet;
import apps.project.util.AgentProperties;
import java.util.Date;
import java.util.EnumSet;
import java.util.Properties;
import javax.servlet.DispatcherType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class TaskBatch extends CmService {

	private static final Log logger = LogFactory.getLog(TaskBatch.class);


	private SchedulerFactory sf = null;
	private Scheduler sched = null;

	private Server jettyServer;


	public static void main(String[] args) throws Exception {
		try {
			new TaskBatch().init();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public TaskBatch() {
	}

	public void init() throws Exception {

		if (!isTaskServer()) {
			logger.info("ZeonsAgent runs only task server.");
			return;
		}

		logger.info("debug ZeonsAgent runs only task server.");
		logger.info("# ----------     Started ZeonsAgent Listening    ---------- #");

		Properties prop = AgentProperties.getProp();

		// 스케쥴러 생성
		sf = new StdSchedulerFactory();
		sched = sf.getScheduler();
		sched.start();

		// create task ==================================================================================

		// 상호, 성명 DS에 전달
		createTask(prop.getProperty("CRON_SR_ACCT_EXP"), AcctHisExpController.class);
		// 자동이체세대 받기
		createTask(prop.getProperty("CRON_SR_AUTO_PAY"), AutoPayController.class);
        // 자동수납내역 받기
        createTask(prop.getProperty("CRON_SR_AUTO_RCP"), AutoReceiptController.class);
        // 계좌내역 받기
        createTask(prop.getProperty("CRON_SR_ACCT_ISS"), IssAcctHisController.class);
        // 가상계좌내역 받기
        createTask(prop.getProperty("CRON_SR_MST_VA"), MstVaController.class);

		// task end ====================================================================================

		// web server 추가
		jettyServer = new Server(Integer.parseInt(prop.getProperty("HTTP_PORT")));

		// HTTP response header Server 제외
		for (Connector con : jettyServer.getConnectors()) {
			for (ConnectionFactory fac : con.getConnectionFactories()) {
				if (fac instanceof HttpConnectionFactory) {
					((HttpConnectionFactory) fac).getHttpConfiguration().setSendServerVersion(false);
				}
			}
		}

		ServletContextHandler context = new ServletContextHandler();

		context.setContextPath("/api");
		context.setResourceBase("");

		// endpoints
		context.addServlet(TestConnectServlet.class, "/test-connect"); // test
		//context.addServlet(AutoPayServlet.class, "/autopay/from-ds"); // 동서 데이터 요청
		context.addServlet(ScrapingHometaxServlet.class, "/scraping/scrapingHometax.do");		// 홈택스스크래핑
		context.addServlet(MmsFtpFileUploadServlet.class, "/mmsFtpFileUpload.do");		// FTP 파일업로드

		FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

		cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,PUT,DELETE,OPTION,HEAD");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

		context.addServlet(DefaultServlet.class, "/");

		jettyServer.setHandler(context);

		jettyServer.start();
		jettyServer.join();
	}

	public void createTask(String cronExpr, Class<? extends Job> clazz) throws SchedulerException {
		createTask(cronExpr, clazz, clazz.getSimpleName(), "ZeonsAgent");
	}

	public void createTask(String cronExpr, Class<? extends Job> clazz, String jobName, String groupName) throws SchedulerException {
		JobDetail task = JobBuilder.newJob(clazz)
			.withIdentity(jobName, groupName)
			.build();

		CronTrigger trigger = TriggerBuilder.newTrigger()
			.withIdentity(jobName, groupName)
			.withSchedule(CronScheduleBuilder.cronSchedule(cronExpr))
			.build();

		Date ft = sched.scheduleJob(task, trigger);

		logger.info(task.getKey() + " has been scheduled to run at: " + ft
			+ " and repeat based on expression: "
			+ trigger.getCronExpression());
	}

	public boolean isTaskServer() {
		return true;
	}


}