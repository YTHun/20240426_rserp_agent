package apps.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AutoPayResponseDto {

	@JsonProperty("response_code")
	private String responseCode;
	@JsonProperty("message")
	private String message;
	@JsonProperty("result")
	private List<AutoPayDto> result;

	public String getResponse_code() {
		return responseCode;
	}

	public void setResponse_code(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<AutoPayDto> getResult() {
		return result;
	}

	public void setResult(List<AutoPayDto> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "AutoPayResponseDto [responseCode=" + responseCode + ",\n message=" + message + ",\n result=" + result + "]";
	}
}