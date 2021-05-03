package com.revature.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.revature.beans.Employee;
import com.revature.data.EmployeeDao;
import com.revature.services.EmployeeServiceImpl;

public class EmployeeServiceImplTest {
	private EmployeeServiceImpl es;

	@Before
	public void setUp() throws Exception {
		es = new EmployeeServiceImpl(mock(EmployeeDao.class));
	}
	
	@Test
	public void getEmployeeByIdReturnsEmployeeWithId() {
		Employee tester = new Employee();
		tester.setEmployeeId("test");
		EmployeeDao dao = mock(EmployeeDao.class);
		es.setEmployeeDao(dao);
		when(dao.getEmployeeById("test")).thenReturn(tester);
		
		Employee e = es.getEmployee(tester.getEmployeeId());
		assertEquals("Employee should have id: " + tester.getEmployeeId(), tester.getEmployeeId(), e.getEmployeeId());
	}
	
	@Test
	public void addEmployeeWithCorrectlyGeneratedId() {
		Employee tester = new Employee();
		tester.setFirstName("A");
		tester.setLastName("B");
		EmployeeDao ed = mock(EmployeeDao.class);
		es.setEmployeeDao(ed);
		es.addEmployee(tester);
		when(ed.existsEmployee(tester.getEmployeeId())).thenReturn(false);
		assertTrue("ID must have first and last initial as first two characters ", tester.getEmployeeId().indexOf("A") == 0 && tester.getEmployeeId().indexOf("B") == 1);
	}
	
	@Test
	public void updateEmployeeUpdatesEmployee() {
		Employee tester = new Employee();
		tester.setEmployeeId("AB");
		tester.setFirstName("A");
		tester.setLastName("B");
		EmployeeDao ed = mock(EmployeeDao.class);
		es.setEmployeeDao(ed);
		when(ed.existsEmployee(tester.getEmployeeId())).thenReturn(true);
		es.updateEmployee(tester);
		verify(ed).updateEmployee(tester);
	}
	
	@Test
	public void getBenCoGetsBenco() {
		Employee benCo = new Employee();
		Employee notBenCo = new Employee();
		benCo.setBenCo(true);
		benCo.setEmployeeId("benco");
		notBenCo.setBenCo(false);
		notBenCo.setEmployeeId("notbenco");
		List<Employee> employees = Arrays.asList(benCo, notBenCo);
		EmployeeDao ed = mock(EmployeeDao.class);
		es.setEmployeeDao(ed);
		when(ed.getEmployees()).thenReturn(employees);
		assertEquals("method should return BenCo", benCo.getEmployeeId(), es.getBenCo("a"));
	}

}
