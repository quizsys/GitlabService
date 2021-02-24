package com.example.demo.auth;

public class AuthReturnDto {

	private String token;

	private String retCode;

	private String errorMessage;

	private String gitUrl;

	private String groupId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getGitUrl() {
		return gitUrl;
	}

	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public AuthReturnDto(String token, String retCode, String errorMessage, String gitUrl, String groupId) {
		this.token = token;
		this.retCode = retCode;
		this.errorMessage = errorMessage;
		this.gitUrl = gitUrl;
		this.groupId = groupId;
	}




}
