package com.revature.beans;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class Employee {
	
	private String firstName;
	private String lastName;
	private String employeeId;
	private String directSupervisorId;
	private String deptHeadId;
	private EmployeeStatus status;
	private Role role;
	private BigDecimal availableReimbursement;
	private BigDecimal pendingReimbursement;
	private Instant lastUpdated;
	private boolean isBenCo;
	private List<String> pastForms;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmployeeId() {
		return employeeId;
	}
	
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getDirectSupervisorId() {
		return directSupervisorId;
	}
	
	public void setDirectSupervisorId(String directSupervisorId) {
		this.directSupervisorId = directSupervisorId;
	}
	
	public String getDeptHeadId() {
		return deptHeadId;
	}
	
	public void setDeptHeadId(String deptHeadId) {
		this.deptHeadId = deptHeadId;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public BigDecimal getAvailableReimbursement() {
		return availableReimbursement;
	}
	
	public void setAvailableReimbursement(BigDecimal availableReimbursement) {
		this.availableReimbursement = availableReimbursement;
	}
	
	public BigDecimal getPendingReimbursement() {
		return pendingReimbursement;
	}
	
	public void setPendingReimbursement(BigDecimal pendingReimbursement) {
		this.pendingReimbursement = pendingReimbursement;
	}
	
	public Instant getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(Instant instant) {
		this.lastUpdated = instant;
	}

	public boolean isBenCo() {
		return isBenCo;
	}

	public void setBenCo(boolean isBenCo) {
		this.isBenCo = isBenCo;
	}

	public List<String> getPastForms() {
		return pastForms;
	}

	public void setPastForms(List<String> pastForms) {
		this.pastForms = pastForms;
	}

	public EmployeeStatus getStatus() {
		return status;
	}

	public void setStatus(EmployeeStatus status) {
		this.status = status;
	}
	
	public String toString() {
		return employeeId;
	}
	
}
