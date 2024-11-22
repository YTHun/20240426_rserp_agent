package apps.project.servlet;

import apps.framework.object.CmMap;
import apps.project.controller.ftpfileupload.MmsFtpFileUploadController;
import apps.project.util.AgentProperties;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@MultipartConfig
@SuppressWarnings("rawtypes")
public class MmsFtpFileUploadServlet extends HttpServlet {
	
	private static final Log logger = LogFactory.getLog(MmsFtpFileUploadServlet.class);

	private static final long serialVersionUID = 2643543543543543543L;

	MmsFtpFileUploadController mmsFtpFileUploadController = new MmsFtpFileUploadController();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// GET
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// POST
		JSONObject result = new JSONObject();
		CmMap reqVo = new CmMap();

		try {
			mmsFtpFileUploadController.mmsFtpfileUpload(req, reqVo);
			
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("utf-8");
		
			result.put("r_code", reqVo.getString("r_code"));
			result.put("r_msg", reqVo.getString("r_msg"));
			result.put("fileName1", reqVo.getString("fileName1"));
			result.put("fileName2", reqVo.getString("fileName2"));
			result.put("fileName3", reqVo.getString("fileName3"));
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			try {
				result.put("code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (JSONException e1) {
			}
		} finally {
			resp.getWriter().print(result.toString());
		}
	}
	
}
