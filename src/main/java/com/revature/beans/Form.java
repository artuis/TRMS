package com.revature.beans;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Form {
	
	private String FormId;
	private String submitterId;
	private String benCoId;
	private String supervisorId;
	private String deptHeadId;
	private EventType eventType;
	private StringBuilder description;
	private GradingFormat gradingFormat;
	private StringBuilder justification;
	private StringBuilder statusReason;
	private String location;
	private LocalDate trainingStart;
	private LocalDate trainingEnd;
	private LocalTime trainingTimeStart;
	private LocalTime trainingTimeEnd;
	private LocalDate submissionDate;
	private BigDecimal reimbursement;
	private BigDecimal cost;
	private boolean supervisorApproval;
	private boolean deptHeadApproval;
	private boolean benCoApproval;
	private FormStatus status;
	private List<String> attachments;
	
	public String getFormId() {
		return FormId;
	}
	
	public void setFormId(String formId) {
		FormId = formId;
	}
	
	public String getSubmitterId() {
		return submitterId;
	}
	
	public void setSubmitterId(String submitterId) {
		this.submitterId = submitterId;
	}
	
	public String getBenCoId() {
		return benCoId;
	}
	
	public void setBenCoId(String benCoId) {
		this.benCoId = benCoId;
	}
	
	public String getSupervisorId() {
		return supervisorId;
	}
	
	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}
	
	public String getDeptHeadId() {
		return deptHeadId;
	}
	
	public void setDeptHeadId(String deptHeadId) {
		this.deptHeadId = deptHeadId;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
	public StringBuilder getDescription() {
		return description;
	}
	
	public void setDescription(StringBuilder description) {
		this.description = description;
	}
	
	public GradingFormat getGradingFormat() {
		return gradingFormat;
	}
	
	public void setGradingFormat(GradingFormat gradingFormat) {
		this.gradingFormat = gradingFormat;
	}
	
	public StringBuilder getJustification() {
		return justification;
	}
	
	public void setJustification(StringBuilder justification) {
		this.justification = justification;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public LocalDate getTrainingStart() {
		return trainingStart;
	}

	public void setTrainingStart(LocalDate localDate) {
		this.trainingStart = localDate;
	}

	public LocalDate getTrainingEnd() {
		return trainingEnd;
	}

	public void setTrainingEnd(LocalDate trainingEnd) {
		this.trainingEnd = trainingEnd;
	}

	public LocalTime getTrainingTimeStart() {
		return trainingTimeStart;
	}

	public void setTrainingTimeStart(LocalTime localTime) {
		this.trainingTimeStart = localTime;
	}

	public LocalTime getTrainingTimeEnd() {
		return trainingTimeEnd;
	}

	public void setTrainingTimeEnd(LocalTime localTime) {
		this.trainingTimeEnd = localTime;
	}

	public BigDecimal getCost() {
		return cost;
	}
	
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	public boolean isSupervisorApproval() {
		return supervisorApproval;
	}
	
	public void setSupervisorApproval(boolean supervisorApproval) {
		this.supervisorApproval = supervisorApproval;
	}
	
	public boolean isDeptHeadApproval() {
		return deptHeadApproval;
	}
	
	public void setDeptHeadApproval(boolean deptHeadApproval) {
		this.deptHeadApproval = deptHeadApproval;
	}
	
	public boolean isBenCoApproval() {
		return benCoApproval;
	}
	
	public void setBenCoApproval(boolean benCoApproval) {
		this.benCoApproval = benCoApproval;
	}
	
	public List<String> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}

	public FormStatus getStatus() {
		return status;
	}

	public void setStatus(FormStatus status) {
		this.status = status;
	}

	public StringBuilder getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(StringBuilder statusReason) {
		this.statusReason = statusReason;
	}

	public LocalDate getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(LocalDate submissionDate) {
		this.submissionDate = submissionDate;
	}

	public BigDecimal getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(BigDecimal calculatedReimbursement) {
		this.reimbursement = calculatedReimbursement;
	}
	
}
