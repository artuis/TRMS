package com.revature.controllers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.beans.AuthResponse;
import com.revature.beans.Employee;
import com.revature.beans.EmployeeStatus;
import com.revature.beans.Role;
import com.revature.factories.TokenFactory;
import com.revature.factories.TokenFactoryImpl;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;

import io.javalin.http.Context;

public class EmployeeController {
	private static EmployeeService es = new EmployeeServiceImpl();
	private static Logger log = LogManager.getLogger(EmployeeController.class);
	private static TokenFactory tf = TokenFactoryImpl.getTokenFactory();
	
	public static void getEmployees(Context ctx) {
		ctx.json(es.getEmployees());
	}
	
	public static void getValidBenCo(Context ctx) {
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		ctx.json(es.getBenCo(decoded.get("employeeId")));
	}
	
	public static void addEmployee(Context ctx) {
		log.trace("adding new employee");
		Employee e = new Employee();
		try  {
			e.setFirstName(ctx.formParam("firstName"));
			e.setLastName(ctx.formParam("lastName"));
			e.setDirectSupervisorId(ctx.formParam("directSupervisorId"));
			e.setDeptHeadId(ctx.formParam("deptHeadId"));
			e.setStatus(EmployeeStatus.ACTIVE);
			e.setRole(Role.valueOf(ctx.formParam("role").toUpperCase()));
			e.setAvailableReimbursement(new BigDecimal(1000.0));
			e.setPendingReimbursement(new BigDecimal(0.0));
			e.setLastUpdated(Instant.ofEpochMilli(System.currentTimeMillis()));
			e.setBenCo(Boolean.parseBoolean(ctx.formParam("isBenCo")));
			e.setPastForms(new ArrayList<String>());
		} catch (Exception ex) {
			ex.printStackTrace();
			ctx.status(400);
			return;
		}
		if (es.addEmployee(e)) {
			ctx.json(e);
		} else {
			ctx.status(409);
		}
	}
	
	public static void updateEmployee(Context ctx) {
		log.trace("updating existing employee");
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		Employee updater = es.getEmployee(decoded.get("employeeId"));
		if (tf.authorizeAdmin(ctx.cookie("token"), updater)) {
			try {
				Employee e = es.getEmployee(ctx.pathParam("id"));
				e.setLastUpdated(Instant.ofEpochMilli(System.currentTimeMillis()));
				Map<String, List<String>> params = ctx.formParamMap();
				if (params.containsKey("role")) {
					e.setRole(Role.valueOf(ctx.formParam("role")));
				}
				if (params.containsKey("supervisorId")) {
					e.setDirectSupervisorId(ctx.formParam("supervisorId"));
				}
				if (params.containsKey("deptHeadId")) {
					e.setDeptHeadId(ctx.formParam("deptHeadId"));
				}
				es.updateEmployee(e);
				ctx.json(e);
			} catch (Exception ex) {
				ex.printStackTrace();
				ctx.status(400);
			}
		}
	}
	
	public static void loginEmployee(Context ctx) {
		String uId = ctx.formParam("employeeId");
		Employee e = es.getEmployee(uId);
		if (e == null) {
			ctx.status(404);
		} else {
			AuthResponse authResponse = es.loginEmployee(e, ctx);
			Cookie jWTCookie = new Cookie("token", authResponse.getToken());
			jWTCookie.setHttpOnly(true);
			ctx.json(e).cookie(jWTCookie);
		}
	}
	 
	public static void logoutEmployee(Context ctx) {
		ctx.removeCookie("token");
		ctx.status(204);
	}
}
