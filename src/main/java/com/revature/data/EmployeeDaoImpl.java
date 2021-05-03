package com.revature.data;

import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.beans.Employee;
import com.revature.beans.Role;
import com.revature.utils.CassandraUtil;

public class EmployeeDaoImpl implements EmployeeDao {
	private CqlSession session;
	
	public EmployeeDaoImpl() {
		super();
		session = CassandraUtil.getInstance().getSession();
	}
	
	public EmployeeDaoImpl(CqlSession session) {
		this.session = session;
	}
	
	@Override
	public List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList<>();
		String query = "Select * from employee;";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		ResultSet rs = session.execute(s);
		rs.forEach(data -> {
			Employee e = new Employee();
			e = new Employee();
			e.setEmployeeId(data.getString("employee_id"));
			e.setFirstName(data.getString("first_name"));
			e.setLastName(data.getString("last_name"));
			e.setDeptHeadId(data.getString("dept_head_id"));
			e.setDirectSupervisorId(data.getString("direct_supervisor_id"));
			e.setRole(Role.valueOf(data.getString("role")));
			e.setBenCo(data.getBoolean("is_benco"));
			e.setPastForms(data.getList("form_history", String.class));
			e.setAvailableReimbursement(data.getBigDecimal("available_reimbursement"));
			e.setPendingReimbursement(data.getBigDecimal("pending_reimbursement"));
			e.setLastUpdated(data.getInstant("last_updated"));
			employees.add(e);
		});
		return employees;
	}

	@Override
	public Employee getEmployeeById(String id) {
		String query = "Select * from employee where employee_id = ?;";
		Employee e = null;
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(id);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if (data != null) {
			e = new Employee();
			e.setEmployeeId(data.getString("employee_id"));
			e.setFirstName(data.getString("first_name"));
			e.setLastName(data.getString("last_name"));
			e.setDeptHeadId(data.getString("dept_head_id"));
			e.setDirectSupervisorId(data.getString("direct_supervisor_id"));
			e.setRole(Role.valueOf(data.getString("role")));
			e.setBenCo(data.getBoolean("is_benco"));
			e.setPastForms(data.getList("form_history", String.class));
			e.setAvailableReimbursement(data.getBigDecimal("available_reimbursement"));
			e.setPendingReimbursement(data.getBigDecimal("pending_reimbursement"));
			e.setLastUpdated(data.getInstant("last_updated"));
		}
		return e;
	}

	@Override
	public void addEmployee(Employee e) {
		String query = "Insert into Employee (employee_id, first_name, last_name, direct_supervisor_id, dept_head_id, role, available_reimbursement,"
				+ "pending_reimbursement, last_updated, is_benco, form_history) values (?,?,?,?,?,?,?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(e.getEmployeeId(), e.getFirstName(), e.getLastName(), e.getDirectSupervisorId(), e.getDeptHeadId(),
														e.getRole().toString(), e.getAvailableReimbursement(), e.getPendingReimbursement(), e.getLastUpdated(),
														e.isBenCo(), e.getPastForms());
		session.execute(bound);
	}

	@Override
	public void updateEmployee(Employee e) {
		String query = "Update Employee Set \"first_name\" = ?, \"last_name\" = ?, \"direct_supervisor_id\" = ?, \"dept_head_id\" = ?, \"role\" = ?,"
				+ "\"available_reimbursement\" = ?, \"pending_reimbursement\" = ?, \"last_updated\" = ?, \"is_benco\" = ?, \"form_history\" = ?"
				+ "Where employee_id = ? IF EXISTS;";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(e.getFirstName(), e.getLastName(), e.getDirectSupervisorId(), e.getDeptHeadId(),
														e.getRole().toString(), e.getAvailableReimbursement(), e.getPendingReimbursement(), e.getLastUpdated(),
														e.isBenCo(), e.getPastForms(), e.getEmployeeId());
		session.execute(bound);
	}

	@Override
	public boolean existsEmployee(String id) {
		String query = "Select employee_id from employee where employee_id = ?;";
		BoundStatement bound = session.prepare(query).bind(id);
		ResultSet rs = session.execute(bound);
		if (rs.one() == null) {
			return false;
		} else {
			return true;
		}
	}

}
