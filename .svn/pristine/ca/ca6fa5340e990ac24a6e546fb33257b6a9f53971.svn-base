package apps.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import apps.project.util.AgentProperties;

public class CmPathInfo {


	protected final Logger	logger = LogManager.getLogger(this.getClass());


	private static String	WEB_ROOT;
	

	private static String	ROOT_PATH;
	private static String 	UPLOAD_PATH;
	
	private static String 	RM_GS_DIR_LOCAL_RECV;
	private static String 	RM_GS_DIR_LOCAL_SEND;
	private static String 	RM_GS_DIR_LOCAL_BAK;

	private static String 	RM_GS_DIR_REMOTE_RECV;
	private static String 	RM_GS_DIR_REMOTE_SEND;
	private static String 	RM_GS_DIR_REMOTE_BAK;

	private static String 	RM_GS_FTP_HOST;
	private static int	 	RM_GS_FTP_PORT;
	private static String 	RM_GS_FTP_ID;
	private static String 	RM_GS_FTP_PW;

	
	static {
		if (WEB_ROOT == null || WEB_ROOT.equals("")) {
			new CmPathInfo();
		}
	}
	
	public CmPathInfo() {
		if (WEB_ROOT == null || WEB_ROOT.equals("")) {
			this.setPath();
		}
	}
	

	public void setPath() {
		try {
			
			WEB_ROOT = "/";
			ROOT_PATH = AgentProperties.get("ROOT_PATH");
			UPLOAD_PATH = AgentProperties.get("UPLOAD_PATH");
			
			RM_GS_DIR_LOCAL_RECV = UPLOAD_PATH + AgentProperties.get("RM_GS_DIR_LOCAL_RECV");
			RM_GS_DIR_LOCAL_SEND = UPLOAD_PATH + AgentProperties.get("RM_GS_DIR_LOCAL_SEND");
			RM_GS_DIR_LOCAL_BAK = AgentProperties.get("RM_GS_DIR_LOCAL_BAK");

			RM_GS_DIR_REMOTE_RECV = AgentProperties.get("RM_GS_DIR_REMOTE_RECV");
			RM_GS_DIR_REMOTE_SEND = AgentProperties.get("RM_GS_DIR_REMOTE_SEND");
			RM_GS_DIR_REMOTE_BAK = AgentProperties.get("RM_GS_DIR_REMOTE_BAK");
			
			RM_GS_FTP_HOST = AgentProperties.get("FTP_HOST");
			RM_GS_FTP_PORT = Integer.parseInt(AgentProperties.get("FTP_PORT"));
			RM_GS_FTP_ID = AgentProperties.get("FTP_ID");
			RM_GS_FTP_PW = AgentProperties.get("FTP_PW");

		} catch (Exception e) {
			logger.error(e); //e.printStackTrace();
		}
	}



	public static String getROOT_PATH() {
		return ROOT_PATH;
	}

	public static String getUPLOAD_PATH() {
		return UPLOAD_PATH;
	}


	public static String getRM_GS_DIR_LOCAL_RECV() {
		return RM_GS_DIR_LOCAL_RECV;
	}


	public static String getRM_GS_DIR_LOCAL_SEND() {
		return RM_GS_DIR_LOCAL_SEND;
	}


	public static String getRM_GS_DIR_REMOTE_RECV() {
		return RM_GS_DIR_REMOTE_RECV;
	}


	public static String getRM_GS_DIR_REMOTE_SEND() {
		return RM_GS_DIR_REMOTE_SEND;
	}


	public static String getRM_GS_FTP_HOST() {
		return RM_GS_FTP_HOST;
	}


	public static int getRM_GS_FTP_PORT() {
		return RM_GS_FTP_PORT;
	}


	public static String getRM_GS_FTP_ID() {
		return RM_GS_FTP_ID;
	}


	public static String getRM_GS_FTP_PW() {
		return RM_GS_FTP_PW;
	}


	public static String getRM_GS_DIR_LOCAL_BAK() {
		return RM_GS_DIR_LOCAL_BAK;
	}


	public static String getRM_GS_DIR_REMOTE_BAK() {
		return RM_GS_DIR_REMOTE_BAK;
	}
}
