package com.revature.data;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

public class EmployeeDaoTest {
	private EmployeeDaoImpl ed;
	
	@Before
	public void setUp() throws Exception {
		ed = new EmployeeDaoImpl(mock(CqlSession.class));
	}
	
	@Test
	public void getEmployeeByIdGetsEmployeeById() {
		CqlSession session = mock(CqlSession.class);
		SimpleStatement s = mock(SimpleStatement.class);
		ResultSet rs = mock(ResultSet.class);
		Row data = mock(Row.class);
	}
}
