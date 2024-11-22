package apps.project.controller.ftpfileupload;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import javax.servlet.http.HttpServletRequest;

import apps.framework.object.CmMap;
import apps.project.service.ftpfileupload.MmsFtpFileUploadService;

public class MmsFtpFileUploadController implements Job {

	private static final Log logger = LogFactory.getLog(MmsFtpFileUploadController.class);

	MmsFtpFileUploadService mmsFtpFileUploadService = new MmsFtpFileUploadService();

	public MmsFtpFileUploadController() {
		logger.info("MmsFtpFileUploadController");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("job execute");
	}
	
	/* 홈택스연동 */
	@SuppressWarnings("rawtypes")
	public void mmsFtpfileUpload(HttpServletRequest req, CmMap reqVo) {
		try {
			mmsFtpFileUploadService.mmsFtpfileUpload(req, reqVo);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
}
