package com.example.demo.templateCreate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity                  /* エンティティクラス */
public class TemplateCreateExtraDto {

	@Id
	private int id;

	private int projectId;

	private String templateName;

	private String issueTitle;

	private String labels;

	private int createTerms;

	private int createTermsDetail;

	private int issueDate;

	private int issueDateDetail;

	private LocalDate nextCreateDate;

	private LocalDate beforeCreateDate;

	private boolean beforeSuccessFlg;

	private double estimateTime;

	private LocalDateTime updateDateTime;

	private String createTermsStr;
	private String createTermsDetailStr;
	private String issueDateStr;
	private String issueDateDetailStr;
	private String projectName;

	private String beforeResult;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getIssueTitle() {
		return issueTitle;
	}

	public void setIssueTitle(String issueTitle) {
		this.issueTitle = issueTitle;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public int getCreateTerms() {
		return createTerms;
	}

	public void setCreateTerms(int createTerms) {
		this.createTerms = createTerms;
	}

	public int getCreateTermsDetail() {
		return createTermsDetail;
	}

	public void setCreateTermsDetail(int createTermsDetail) {
		this.createTermsDetail = createTermsDetail;
	}

	public int getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(int issueDate) {
		this.issueDate = issueDate;
	}

	public int getIssueDateDetail() {
		return issueDateDetail;
	}

	public void setIssueDateDetail(int issueDateDetail) {
		this.issueDateDetail = issueDateDetail;
	}

	public LocalDate getNextCreateDate() {
		return nextCreateDate;
	}

	public void setNextCreateDate(LocalDate nextCreateDate) {
		this.nextCreateDate = nextCreateDate;
	}

	public LocalDate getBeforeCreateDate() {
		return beforeCreateDate;
	}

	public void setBeforeCreateDate(LocalDate beforeCreateDate) {
		this.beforeCreateDate = beforeCreateDate;
	}

	public boolean isBeforeSuccessFlg() {
		return beforeSuccessFlg;
	}

	public void setBeforeSuccessFlg(boolean beforeSuccessFlg) {
		this.beforeSuccessFlg = beforeSuccessFlg;
	}

	public double getEstimateTime() {
		return estimateTime;
	}

	public void setEstimateTime(double estimateTime) {
		this.estimateTime = estimateTime;
	}

	public String getCreateTermsStr() {
		return createTermsStr;
	}

	public void setCreateTermsStr(String createTermsStr) {
		this.createTermsStr = createTermsStr;
	}

	public String getCreateTermsDetailStr() {
		return createTermsDetailStr;
	}

	public void setCreateTermsDetailStr(String createTermsDetailStr) {
		this.createTermsDetailStr = createTermsDetailStr;
	}

	public String getIssueDateStr() {
		return issueDateStr;
	}

	public void setIssueDateStr(String issueDateStr) {
		this.issueDateStr = issueDateStr;
	}

	public String getIssueDateDetailStr() {
		return issueDateDetailStr;
	}

	public void setIssueDateDetailStr(String issueDateDetailStr) {
		this.issueDateDetailStr = issueDateDetailStr;
	}

	public String getBeforeResult() {
		return beforeResult;
	}

	public void setBeforeResult(String beforeResult) {
		this.beforeResult = beforeResult;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	@Override
	public String toString() {
		return "TemplateCreateExtraDto [id=" + id + ", projectId=" + projectId + ", templateName=" + templateName
				+ ", issueTitle=" + issueTitle + ", labels=" + labels + ", createTerms=" + createTerms
				+ ", createTermsDetail=" + createTermsDetail + ", issueDate=" + issueDate + ", issueDateDetail="
				+ issueDateDetail + ", nextCreateDate=" + nextCreateDate + ", beforeCreateDate=" + beforeCreateDate
				+ ", beforeSuccessFlg=" + beforeSuccessFlg + ", estimateTime=" + estimateTime + ", updateDateTime="
				+ updateDateTime + ", createTermsStr=" + createTermsStr + ", createTermsDetailStr="
				+ createTermsDetailStr + ", issueDateStr=" + issueDateStr + ", issueDateDetailStr=" + issueDateDetailStr
				+ ", projectName=" + projectName + ", beforeResult=" + beforeResult + "]";
	}



}
