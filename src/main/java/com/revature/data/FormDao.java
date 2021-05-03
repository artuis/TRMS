package com.revature.data;

import java.util.List;

import com.revature.beans.Form;

public interface FormDao {
	List<Form> getForms();
	void addForm(Form f);
	void updateForm(Form f);
}
