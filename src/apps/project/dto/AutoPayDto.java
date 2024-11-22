package apps.project.dto;

import org.codehaus.jackson.annotate.JsonProperty;

public class AutoPayDto {

	@JsonProperty("dongcode")
	private String dongCode;
	@JsonProperty("roomno")
	private String roomNo;
	@JsonProperty("gojicode")
	private String gojiCode;
	@JsonProperty("accountno")
	private String accountNo;
	@JsonProperty("admincode")
	private String adminCode;
	@JsonProperty("bankname")
	private String bankName;
	@JsonProperty("depositname")
	private String depositName;
	@JsonProperty("reqdate")
	private String reqDate;
	@JsonProperty("reqkind")
	private String reqKind;

	public AutoPayDto() {

	}

	public AutoPayDto(String dongCode, String roomNo, String gojiCode, String accountNo, String adminCode, String bankName, String depositName, String reqDate, String reqKind) {
		this.dongCode = dongCode;
		this.roomNo = roomNo;
		this.gojiCode = gojiCode;
		this.accountNo = accountNo;
		this.adminCode = adminCode;
		this.bankName = bankName;
		this.depositName = depositName;
		this.reqDate = reqDate;
		this.reqKind = reqKind;
	}

	public String getDongCode() {
		return dongCode;
	}

//	public void setDongCode(String dongCode) {
//		this.dongCode = dongCode;
//	}

	public String getRoomNo() {
		return roomNo;
	}

//	public void setRoomNo(String roomNo) {
//		this.roomNo = roomNo;
//	}

	public String getGojiCode() {
		return gojiCode;
	}

//	public void setGojiCode(String gojiCode) {
//		this.gojiCode = gojiCode;
//	}

	public String getAccountNo() {
		return accountNo;
	}

//	public void setAccountNo(String accountNo) {
//		this.accountNo = accountNo;
//	}

	public String getAdminCode() {
		return adminCode;
	}

//	public void setAdminCode(String adminCode) {
//		this.adminCode = adminCode;
//	}

	public String getBankName() {
		return bankName;
	}

//	public void setBankName(String bankName) {
//		this.bankName = bankName;
//	}

	public String getDepositName() {
		return depositName;
	}

//	public void setDepositName(String depositName) {
//		this.depositName = depositName;
//	}

	public String getReqDate() {
		return reqDate;
	}

//	public void setReqDate(String reqDate) {
//		this.reqDate = reqDate;
//	}

	public String getReqKind() {
		return reqKind;
	}

//	public void setReqKind(String reqKind) {
//		this.reqKind = reqKind;
//	}
}
