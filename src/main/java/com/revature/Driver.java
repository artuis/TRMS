package com.revature;

import com.revature.controllers.EmployeeController;
import com.revature.controllers.FormController;
import com.revature.db.DBCreate;

import io.javalin.Javalin;

public class Driver {
	
	public static void main(String[] args) {
		Javalin app = Javalin.create().start(8080);
        app.get("/", ctx -> ctx.result("Project One"));
        app.get("/employees", EmployeeController::getEmployees);
        app.post("/employees", EmployeeController::addEmployee);
        app.post("/employees/login", EmployeeController::loginEmployee);
        app.post("/employees/logout", EmployeeController::logoutEmployee);
        app.put("/employees/:id", EmployeeController::updateEmployee);
        app.get("/employees/validbenco", EmployeeController::getValidBenCo);
        
        
        app.get("/forms", FormController::getAllForms);
        app.post("/forms", FormController::createForm);
        app.get("/forms/pending", FormController::getPendingForms);
        app.get("/forms/unapproved", FormController::getFormsNeedingApproval);
        app.get("/forms/:id", FormController::getFormById);
        app.put("/forms/:id", FormController::updateForm);
        app.put("/forms/:id/approve", FormController::approveForm);
        app.put("/forms/:id/deny", FormController::denyForm);
        app.post("/forms/:id/attachments", FormController::uploadAttachment);
        app.get("/forms/:id/attachments/", FormController::getAttachments);
        app.get("/forms/:id/attachments/:key", FormController::getAttachment);
//        DBCreate.formTable();

	}
	
	
}
