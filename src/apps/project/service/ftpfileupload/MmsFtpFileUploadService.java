package apps.project.service.ftpfileupload;

import apps.framework.object.CmMap;
import apps.framework.object.CmResMap;
import apps.framework.service.CmService;
import apps.project.util.AgentProperties;

import java.util.Collection;
//import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
//import org.json.JSONArray;
import org.json.JSONObject;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.servlet.annotation.MultipartConfig;

@MultipartConfig
@SuppressWarnings("rawtypes")
public class MmsFtpFileUploadService extends CmService {

	private static final Log logger = LogFactory.getLog(MmsFtpFileUploadService.class);
	
	public void mmsFtpfileUpload(HttpServletRequest req, CmMap reqVo) {

		logger.info("MmsFtpFileUploadService => mmsFtpfileUpload Start ....");

		
        BufferedReader br = null;
		String readLine = null;
		
		
		
     // properties 파일에서 동서 암호화 키, 암호화 nonce, agency code 가져오기
		Properties prop 				= AgentProperties.getProp();
        String server	= prop.getProperty("DONGSEO_MMS_FTP_SERVER"); // FTP 서버 주소
        int port		= Integer.parseInt(prop.getProperty("DONGSEO_MMS_FTP_PORT")); // 기본 FTP 포트
        String user		= prop.getProperty("DONGSEO_MMS_FTP_USER"); // FTP 사용자명
        String pass		= prop.getProperty("DONGSEO_MMS_FTP_PASS"); // FTP 비밀번호
        JSch jsch = new JSch();
   	    Session session = null;
        ChannelSftp channelSftp = null;

		try {

			
	        if (! ServletFileUpload.isMultipartContent(req)) {
	        	// FIXME: 예외처리 
	        	return;
	        }

            // Create a factory for disk-based file items
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();
            // DiskFileItemFactory factory = new DiskFileItemFactory(yourMaxMemorySize, yourTempDirectory); // 한번에 구성시

            // Configure a repository (to ensure a secure temp location is used)
            diskFactory.setSizeThreshold(1024 * 1024 * 1024); // Your Max Memory Size
//            diskFactory.setRepository(new File("D:/eclipse_rserp/workspace/20240806_rserp/WebContent/UPLOAD/" + "/temp"));

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(diskFactory);

            // Set overall request size constraint :: 파라메터 (long)를 안넣으면 0
            upload.setSizeMax((long) 1024 * 1024 * 10); // 2TB : Your Max Request Size

            // Parse the request​​
            List<FileItem> items = upload.parseRequest(req);
	        
            String subdir	= "";
            
            // 경로 확인
            for (FileItem item: items) {
            	if (item.isFormField()) {
            		String fieldName = item.getFieldName();
            		if (fieldName.equals("subdir")) {
                        subdir	= item.getString();
            		}
            	}
            }
            
	     // 1. SFTP 서버에 연결
	        session = jsch.getSession(user, server, port);
            session.setPassword(pass);
            
         // 2. 호스트 키 확인을 건너뛰기 (보안에 주의)
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();
            
         // 3. SFTP 채널 열기
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
   	        
         // 원격 디렉토리 확인 및 생성
            try {
                channelSftp.mkdir(subdir); // 서브디렉토리 생성
            } catch (Exception e) {
                // 일반적인 예외 처리 아무 작업도 하지 않음
            }
            
            int	uploadFileCnt				= 0;
            for (FileItem item: items) {
            	
            	if (!item.isFormField() && item.getSize() > 0) {
	            	InputStream inputStream = item.getInputStream();
	
	            	// 현재 시간 가져오기
	                LocalDateTime now = LocalDateTime.now();

	                // 원하는 포맷으로 포맷터 생성
	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");

	                // 현재 시간을 포맷에 맞게 문자열로 변환
	                String formattedDateTime = now.format(formatter);

	                String fileName = item.getName(); // 파일 전체 이름
	                String fileExtension = ""; // 파일 확장자 초기화

	                // 파일 이름과 확장자 분리
	                int lastDotIndex = fileName.lastIndexOf('.');
	                if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
	                    fileExtension = fileName.substring(lastDotIndex + 1); // 확장자 추출
	                    fileName = fileName.substring(0, lastDotIndex); // 파일명 추출
	                }

	            	String saveFileName	= fileName + "_" + formattedDateTime + "." + fileExtension;
	            	
	            	channelSftp.put(inputStream, subdir + saveFileName); // 파일 업로드
	            	
	            	inputStream.close(); // InputStream 닫기
	            	uploadFileCnt ++;
	            	
	            	reqVo.put("fileName"+uploadFileCnt, subdir + saveFileName);
            	}
            }

			reqVo.put("r_code", "OK");
			reqVo.put("r_msg", "처리되었습니다.");
		} catch (Exception ex) {
			reqVo.put("r_code", "EX");
			reqVo.put("r_msg", "ERROR");
    		logger.info("MmsFtpFileUploadService => mmsFtpfileUpload carch Error : " + ex.getMessage());
			//logger.error(e);
		} finally {
			if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
		}
		
		logger.info("MmsFtpFileUploadService => mmsFtpfileUpload End ....");
	}
}
