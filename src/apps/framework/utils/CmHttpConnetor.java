package apps.framework.utils;

import apps.framework.object.CmMap;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CmHttpConnetor {

	/**
	 * OJH : HTTP Get 메서드
	 *
	 * @return : CmMap -> status = HTTP Response Code, result = HTTP Response Body
	 */
	public static <T> T httpGet(Class<T> responseType, String targetUrl, CmMap headers, CmMap params) {
		HttpURLConnection connection = null;

		try {
			targetUrl = targetUrl + "?" + createParamString(params);

			URL url = new URL(targetUrl);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			setHeaders(connection, headers);

			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 200
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
				String responseStr = response.toString();

				return objectMapper.readValue(responseStr, responseType);
			} else {
				responseException(connection);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return null;
	}

	/**
	 * OJH : Map 데이터를 Query String 으로 변환 생성
	 */
	private static String createParamString(CmMap params) {
		StringBuilder paramString = new StringBuilder();

		if (params != null) {
			for (Object key : params.keySet()) {
				paramString
					.append(key)
					.append("=")
					.append(params.getString(key.toString()))
					.append("&");
			}
		}

		return paramString.toString();
	}

	/**
	 * OJH : Header 설정
	 */
	private static void setHeaders(HttpURLConnection connection, CmMap headers) {
		if (headers != null) {
			for (Object key : headers.keySet()) {
				connection.setRequestProperty(key.toString(), headers.getString(key.toString()));
			}
		}
	}

	/**
	 * OJH : response 예외 처리
	 */
	private static void responseException(HttpURLConnection connection) throws IOException {
		int responseCode = connection.getResponseCode();
		switch (responseCode) {
			case HttpURLConnection.HTTP_NOT_FOUND:
				throw new IOException("HTTP_NOT_FOUND : " + responseCode + " : " + connection.getResponseMessage());
			case HttpURLConnection.HTTP_INTERNAL_ERROR:
				throw new IOException("HTTP_INTERNAL_ERROR : " + responseCode + " : " + connection.getResponseMessage());
			case HttpURLConnection.HTTP_BAD_REQUEST:
				throw new IOException("HTTP_BAD_REQUEST : " + responseCode + " : " + connection.getResponseMessage());
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				throw new IOException("HTTP_UNAUTHORIZED : " + responseCode + " : " + connection.getResponseMessage());
			default:
				throw new IOException("HTTP_ERROR : " + responseCode + " : " + connection.getResponseMessage());
		}
	}
}