package com.revature.services;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.beans.AuthResponse;
import com.revature.beans.Employee;
import com.revature.data.EmployeeDao;
import com.revature.data.EmployeeDaoImpl;
import com.revature.factories.TokenFactory;
import com.revature.factories.TokenFactoryImpl;

import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.NotFoundResponse;

public class EmployeeServiceImpl implements EmployeeService {
	private static Logger log = LogManager.getLogger(EmployeeServiceImpl.class);
	private EmployeeDao ed;
	private static TokenFactory tf = TokenFactoryImpl.getTokenFactory();
	
	public EmployeeServiceImpl() {
		super();
		ed = new EmployeeDaoImpl();
	}

	public EmployeeServiceImpl(EmployeeDao ed) {
		this.ed = ed;
	}

	@Override
	public Employee getEmployee(String id) {
		Employee e = ed.getEmployeeById(id);
		if (e == null) {
			throw new NotFoundResponse();
		}
		return e;
	}

	@Override
	public boolean addEmployee(Employee e) {
		Random rand = new Random();
		String eId = "" + e.getFirstName().toUpperCase().charAt(0) + e.getLastName().toUpperCase().charAt(0) + (rand.nextInt(9) + 1)
						+ (rand.nextInt(9) + 1)
						+ (rand.nextInt(9) + 1)
						+ (rand.nextInt(9) + 1)
						+ (rand.nextInt(9) + 1);
		e.setEmployeeId(eId);
		while (true) {
			if (ed.existsEmployee(eId)) {
				log.warn("id already exists " + e.getEmployeeId());
				eId = "" + e.getFirstName().toUpperCase().charAt(0) + e.getLastName().toUpperCase().charAt(0) + (rand.nextInt(9) + 1)
						 + (rand.nextInt(9) + 1)
						 + (rand.nextInt(9) + 1)
						 + (rand.nextInt(9) + 1)
						 + (rand.nextInt(9) + 1);
				e.setEmployeeId(eId);
			} else {
				ed.addEmployee(e);
				return true;
			}
		}
	}

	@Override
	public void updateEmployee(Employee e) {
		if (!ed.existsEmployee(e.getEmployeeId())) {
			throw new ForbiddenResponse();
		}
		ed.updateEmployee(e);
	}

	@Override
	public List<Employee> getEmployees() {
		return ed.getEmployees();
	}

	@Override
	public AuthResponse loginEmployee(Employee e, Context ctx) {
		String token = tf.issueToken(e);
		AuthResponse authResponse = new AuthResponse(token, e.getEmployeeId(), e.getRole().toString());
		return authResponse;
	}

	@Override
	public String getBenCo(String eId) {
		log.trace(eId);
		List<Employee> benCo = ed.getEmployees()
				.stream()
				.filter(e -> e.isBenCo() && !e.getEmployeeId().equals(eId))
				.collect(Collectors.toList());
		log.trace(benCo.toString());
		return benCo.get(ThreadLocalRandom.current().nextInt(0, benCo.size())).getEmployeeId();
	}

	public void setEmployeeDao(EmployeeDao dao) {
		ed = dao;
		
	}
}
