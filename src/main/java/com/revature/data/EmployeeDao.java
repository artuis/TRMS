package com.revature.data;

import java.util.List;

import com.revature.beans.Employee;

public interface EmployeeDao {
	List<Employee> getEmployees();
	Employee getEmployeeById(String id);
	void addEmployee(Employee e);
	void updateEmployee(Employee e);
	boolean existsEmployee(String id);
}
