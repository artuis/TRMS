package com.revature.services;

import java.util.List;

import com.revature.beans.AuthResponse;
import com.revature.beans.Employee;

import io.javalin.http.Context;

public interface EmployeeService {
	
	Employee getEmployee(String id);
	
	boolean addEmployee(Employee e);
	
	void updateEmployee(Employee e);
	
	List<Employee> getEmployees();

	AuthResponse loginEmployee(Employee e, Context ctx);

	String getBenCo(String eId);
	
}
