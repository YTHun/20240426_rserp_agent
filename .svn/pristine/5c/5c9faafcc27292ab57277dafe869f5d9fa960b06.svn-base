package apps.project.service.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FTPService {

	protected final Logger logger = LogManager.getLogger(this.getClass());
	
/*	public static int download(String filePath, String fileName, HttpServletRequest request) throws Exception {

		  FTPClient client = null;
		  BufferedOutputStream bos = null;
		  File fPath  = null;
		  File fDir = null;
		  File f   = null; 
		  
		  String url  = PropertiesHandler.getValue("FTP_URL"); //서버 ip 
		  String id  = PropertiesHandler.getValue("FTP_ID"); // ftp 접속 id
		  String pwd  = PropertiesHandler.getValue("FTP_PWD"); // ftp 접속 비밀번호
		  String port = PropertiesHandler.getValue("FTP_PORT"); //ftp 포트
		  String downloadPath = request.getSession().getServletContext().getRealPath("/") + PropertiesHandler.getValue("FTP_PATH"); //다운로드 경로
		  
		  int result = -1;
		  
		  try{
		   // download 경로에 해당하는 디렉토리 생성  
		   downloadPath = downloadPath + filePath;  
		   fPath     = new File(downloadPath);
		   fDir   = fPath;
		   fDir.mkdirs();
		   
		   f = new File(downloadPath, fileName);
		   
		   client = new FTPClient();
		   client.setControlEncoding("UTF-8");
		   client.connect(url, Integer.parseInt(port));
		   
		   int resultCode = client.getReplyCode();

		   if (FTPReply.isPositiveCompletion(resultCode) == false){
		    client.disconnect();
		    throw new CommonException("FTP 서버에 연결할 수 없습니다.");
		   }
		   else 
		   {
		    client.setSoTimeout(5000);
		    boolean isLogin = client.login(id, pwd);
		    
		    if (isLogin == false){
		     throw new CommonException("FTP 서버에 로그인 할 수 없습니다.");
		    }
		    
		    client.setFileType(FTP.BINARY_FILE_TYPE);
		    client.changeWorkingDirectory(filePath);
		    
		    bos = new BufferedOutputStream(new FileOutputStream(f));
		    boolean isSuccess = client.retrieveFile(fileName, bos);
		    
		    if (isSuccess){
		     result = 1; // 성공     
		    }
		    else{
		     throw new CommonException("파일 다운로드를 할 수 없습니다.");
		    }    
		    
		    client.logout();    
		   } // if ~ else
		  } 
		  catch (Exception e){
		   throw new CommonException("FTP Exception : " + e);
		  }
		  finally{  
		   if (bos != null) try {bos.close();} catch (Exception e) {}     
		   if (client != null && client.isConnected()) try {client.disconnect();} catch (Exception e) {}
		   
		   return result;
		  } // try ~ catch ~ finally
		 } // download()

		 */


	public int download(String ftpHost, String ftpUser, String ftpPass, int ftpPort, String ftpWorkingDir,
		String filePath, String fileName, boolean isPassive) throws Exception {

		FTPClient client = null;
		BufferedOutputStream bos = null;
		File fPath = null;
		File fDir = null;
		File f = null;

		int result = -1;

		try {
			// download 경로에 해당하는 디렉토리 생성
			fPath = new File(filePath);
			fDir = fPath;
			fDir.mkdirs();

			f = new File(filePath, fileName);

			client = new FTPClient();
			client.setControlEncoding("UTF-8");
			client.connect(ftpHost, ftpPort);

			int resultCode = client.getReplyCode();

			if (FTPReply.isPositiveCompletion(resultCode) == false) {
				client.disconnect();
				throw new Exception("FTP 서버에 연결할 수 없습니다.");
			} else {
				client.setSoTimeout(5000);
				boolean isLogin = client.login(ftpUser, ftpPass);

				if (isLogin == false) {
					throw new Exception("FTP 서버에 로그인 할 수 없습니다.");
				}

				if (isPassive) {
					client.enterLocalPassiveMode(); // Passive Mode 접속일때
				}

				client.setFileType(FTP.BINARY_FILE_TYPE);
				client.changeWorkingDirectory(ftpWorkingDir);

				bos = new BufferedOutputStream(new FileOutputStream(f));
				boolean isSuccess = client.retrieveFile(fileName, bos);

				if (isSuccess) {
					result = 1; // 성공
				} else {
					throw new Exception("파일 다운로드를 할 수 없습니다.");
				}

				client.logout();
			} // if ~ else
		} catch (Exception e) {
			throw new Exception("FTP Exception : " + e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
			if (client != null && client.isConnected()) {
				try {
					client.disconnect();
				} catch (Exception e) {
				}
			}

		} // try ~ catch ~ finally

		return result;
	} // download()


	public int upload(String ftpHost, String ftpUser, String ftpPass, int ftpPort, String ftpWorkingDir,
		String filePath, String fileName, boolean isPassive) throws Exception {

		// 전송 안되게 테스트
//		if (true) {
//			return 1;
//		}

		FTPClient ftp = null; // FTP Client 객체

		int result = -1;

		try {
			ftp = new FTPClient(); // FTP Client 객체 생성
			ftp.setControlEncoding("ASCII"); // 문자 코드를 UTF-8로 인코딩

			ftp.connect(ftpHost, ftpPort); // 서버접속 " "안에 서버 주소 입력 또는 "서버주소", 포트번호

			ftp.login(ftpUser, ftpPass); // FTP 로그인 ID, PASSWORLD 입력

			if (isPassive) {
				ftp.enterLocalPassiveMode(); // Passive Mode 접속일때
			}

			ftp.changeWorkingDirectory(ftpWorkingDir); // 작업 디렉토리 변경
			ftp.setFileType(FTP.BINARY_FILE_TYPE); // 업로드 파일 타입 셋팅

			File uploadFile = new File(filePath + "/" + fileName);
			FileInputStream fis = null;

			try {
				fis = new FileInputStream(uploadFile);
				boolean isSuccess = ftp.storeFile(uploadFile.getName(), fis); // File 업로드

				if (isSuccess) {
					result = 1; // 성공
				} else {
					result = -1;
				}
			} catch (IOException e) {
				logger.error(e); //e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close(); // Stream 닫기
						return result;

					} catch (IOException e) {
						logger.error(e); //e.printStackTrace();
					}
				}
			}

			ftp.logout(); // FTP Log Out
		} catch (IOException e) {
			logger.error(e); //e.printStackTrace();
		} finally {
			if (ftp != null && ftp.isConnected()) {
				try {
					ftp.disconnect(); // 접속 끊기
				} catch (IOException e) {
				}
			}
		}
		return result;
	}
	
	
	

/*
		파일 삭제 코드
		public static int delete(String localFilePath, String remoteFilePath, String fileName, HttpServletRequest request) throws Exception {
		  
		  FTPClient ftp = null; // FTP Client 객체
		  FileInputStream fis = null; // File Input Stream
		  
		  String url  = PropertiesHandler.getValue("FTP_URL");  
		  String id  = PropertiesHandler.getValue("FTP_ID");
		  String pwd  = PropertiesHandler.getValue("FTP_PWD"); 
		  String port = PropertiesHandler.getValue("FTP_PORT");
		  
		  int result = -1;

		  try{          
		      ftp = new FTPClient(); // FTP Client 객체 생성
		      ftp.setControlEncoding("UTF-8"); // 문자 코드를 UTF-8로 인코딩
		      ftp.connect(url, Integer.parseInt(port)); // 서버접속 " "안에 서버 주소 입력 또는 "서버주소", 포트번호
		      ftp.login(id, pwd); // FTP 로그인 ID, PASSWORLD 입력
		      ftp.enterLocalPassiveMode(); // Passive Mode 접속일때 
		      ftp.changeWorkingDirectory(remoteFilePath); // 작업 디렉토리 변경
		      ftp.setFileType(FTP.BINARY_FILE_TYPE); // 업로드 파일 타입 셋팅
		      
		      try{          
		          boolean isSuccess = ftp.deleteFile(fileName);//파일삭제
		          
		          if (isSuccess){
		     result = 1; // 성공     
		    }
		    else{
		     throw new CommonException("파일을 삭제 할 수 없습니다.");
		    }
		      } catch(IOException ex){
		      }finally{
		          if (fis != null){
		              try{
		                  fis.close(); // Stream 닫기
		                  return result;
		                  
		              }
		              catch(IOException ex){
		              }
		          }
		      }
		      ftp.logout(); // FTP Log Out
		  }catch(IOException e){
		  }finally{
		      if (ftp != null && ftp.isConnected()){
		          try{
		              ftp.disconnect(); // 접속 끊기
		              return result;
		          }
		          catch (IOException e){
		          }
		      }
		  }
		  return result;  
		 }
*/

	public List<String> listFiles(String ftpHost, String ftpUser, String ftpPass, int ftpPort, String ftpWorkingDir, boolean isPassive) throws Exception {

		FTPClient ftp = null; // FTP Client 객체

		List<String> result = new ArrayList<>();

		try {
			ftp = new FTPClient(); // FTP Client 객체 생성

			ftp.connect(ftpHost, ftpPort); // 서버접속 " "안에 서버 주소 입력 또는 "서버주소", 포트번호

			ftp.login(ftpUser, ftpPass); // FTP 로그인 ID, PASSWORLD 입력

			if (isPassive) {
				ftp.enterLocalPassiveMode(); // Passive Mode 접속일때
			}

			ftp.changeWorkingDirectory(ftpWorkingDir); // 작업 디렉토리 변경

			FTPFile[] files = ftp.listFiles();

			if (null != files) {
				for (FTPFile file : files) {
					if (file.isFile()) {
						result.add(file.getName());
					}
				}
			}

			ftp.logout(); // FTP Log Out
		} catch (IOException e) {
			logger.error(e); //e.printStackTrace();
		} finally {
			if (ftp != null && ftp.isConnected()) {
				try {
					ftp.disconnect(); // 접속 끊기
				} catch (IOException e) {
				}
			}
		}
		return result;
	}


	public boolean renameFile(String ftpHost, String ftpUser, String ftpPass, int ftpPort, String ftpWorkingDir, String filenameFrom, String filenameTo, boolean isPassive) throws Exception {

		FTPClient ftp = null; // FTP Client 객체

		boolean result = false;

		try {
			ftp = new FTPClient(); // FTP Client 객체 생성

			ftp.connect(ftpHost, ftpPort); // 서버접속 " "안에 서버 주소 입력 또는 "서버주소", 포트번호

			ftp.login(ftpUser, ftpPass); // FTP 로그인 ID, PASSWORLD 입력

			if (isPassive) {
				ftp.enterLocalPassiveMode(); // Passive Mode 접속일때
			}

			ftp.changeWorkingDirectory(ftpWorkingDir); // 작업 디렉토리 변경

			result = ftp.rename(filenameFrom, filenameTo);

			ftp.logout(); // FTP Log Out
		} catch (IOException e) {
			logger.error(e); //e.printStackTrace();
		} finally {
			if (ftp != null && ftp.isConnected()) {
				try {
					ftp.disconnect(); // 접속 끊기
				} catch (IOException e) {
				}
			}
		}
		return result;
	}


}
