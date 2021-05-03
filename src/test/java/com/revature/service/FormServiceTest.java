package com.revature.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.revature.beans.Form;
import com.revature.beans.FormStatus;
import com.revature.data.FormDaoImpl;
import com.revature.services.FormServiceImpl;

public class FormServiceTest {
	
	private FormServiceImpl fs;
	
	@Before
	public void setUp() {
		fs = mock(FormServiceImpl.class);
	}
	
	@Test
	public void getPendingFormsGetsOnlyPendingForms() {
		Form a = new Form();
		Form b = new Form();
		a.setSubmitterId("a");
		a.setStatus(FormStatus.PENDING);
		b.setSubmitterId("a");
		b.setStatus(FormStatus.DENIED);
		List<Form> forms = Arrays.asList(a, b);
		FormDaoImpl fd = mock(FormDaoImpl.class);
		fs.setFormDao(fd);
		when(fd.getForms()).thenReturn(forms);
		assertEquals(a, fs.getPendingForms("a").get(0));
	}
	
	@Test
	public void getFormsNeedingApproval() {
		Form a = new Form();
		Form b = new Form();
		a.setBenCoId("a");
		a.setBenCoApproval(false);
		a.setStatus(FormStatus.PENDING);
		b.setBenCoId("b");
		a.setBenCoApproval(false);
		b.setStatus(FormStatus.PENDING);
		List<Form> forms = Arrays.asList(a, b);
		FormDaoImpl fd = mock(FormDaoImpl.class);
		fs.setFormDao(fd);
		when(fd.getForms()).thenReturn(forms);
		assertEquals(a, fs.getFormsNeedingApproval("a").get(0));
	}
	
	
}
