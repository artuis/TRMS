package com.revature.services;

import java.util.List;
import java.util.stream.Collectors;

import com.revature.beans.Form;
import com.revature.beans.FormStatus;
import com.revature.data.FormDao;
import com.revature.data.FormDaoImpl;

public class FormServiceImpl implements FormService {
	FormDao fd;
	
	public FormServiceImpl() {
		fd = new FormDaoImpl();
	}
	
	public FormServiceImpl(FormDao fd) {
		this.fd = fd;
	}
	
	@Override
	public List<Form> getAllForms() {
		return fd.getForms();
	}
	
	@Override
	public List<Form> getPendingForms(String employeeId) {
		return fd.getForms()
				.stream()
				.filter(f -> f.getSubmitterId().equals(employeeId) && !f.getStatus().equals(FormStatus.APPROVED) && !f.getStatus().equals(FormStatus.DENIED))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Form> getFormsNeedingApproval(String employeeId) {
		return fd.getForms()
				.stream()
				.filter(f -> ((f.getBenCoId().equals(employeeId) && !f.isBenCoApproval()) 
						|| (f.getDeptHeadId().equals(employeeId) && !f.isDeptHeadApproval()) 
						|| (f.getSupervisorId().equals(employeeId) && !f.isSupervisorApproval()))
						&& !f.getStatus().equals(FormStatus.APPROVED) && !f.getStatus().equals(FormStatus.DENIED))
				.collect(Collectors.toList());
	}
	
	@Override
	public Form getFormById(String formId) {
		return fd.getForms()
				.stream()
				.filter(f -> f.getFormId().equals(formId))
				.findFirst().orElse(null);
	}

	@Override
	public void addForm(Form f) {
		fd.addForm(f);
	}

	@Override
	public void updateForm(Form f) {
		fd.updateForm(f);
	}

	public void setFormDao(FormDaoImpl fd) {
		this.fd = fd;
	}

}
