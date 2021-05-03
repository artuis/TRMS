package com.revature.services;

import java.util.List;

import com.revature.beans.Form;

public interface FormService {
	public List<Form> getAllForms();
	public List<Form> getPendingForms(String employeeId);
	public List<Form> getFormsNeedingApproval(String employeeId);
	public Form getFormById(String id);
	public void addForm(Form f);
	public void updateForm(Form f);
	
}
