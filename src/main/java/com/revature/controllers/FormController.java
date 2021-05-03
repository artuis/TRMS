package com.revature.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.beans.Employee;
import com.revature.beans.EventType;
import com.revature.beans.Form;
import com.revature.beans.FormStatus;
import com.revature.beans.GradingFormat;
import com.revature.beans.Role;
import com.revature.factories.TokenFactory;
import com.revature.factories.TokenFactoryImpl;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;
import com.revature.services.FormService;
import com.revature.services.FormServiceImpl;
import com.revature.utils.S3Util;

import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UploadedFile;
import software.amazon.awssdk.core.sync.RequestBody;

public class FormController {
	private static Logger log = LogManager.getLogger(EmployeeController.class);
	private static EmployeeService es = new EmployeeServiceImpl();
	private static FormService fs = new FormServiceImpl();
	private static TokenFactory tf = TokenFactoryImpl.getTokenFactory();
	
	public static void getAllForms(Context ctx) {
		ctx.json(fs.getAllForms());
	}
	
	public static void getFormById(Context ctx) {
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		if (tf.authorize(ctx.cookie("token"), es.getEmployee(decoded.get("employeeId")))) {
			log.debug("getting form by id: " + ctx.pathParam("id"));
			Form found = fs.getFormById(ctx.pathParam("id"));
			if (found == null) {
				log.trace("did not find file");
			}
			//interested parties only
			if (decoded.get("employeeId").equals(found.getSubmitterId()) || decoded.get("employeeId").equals(found.getDeptHeadId()) ||
				decoded.get("employeeId").equals(found.getSupervisorId()) || decoded.get("employeeId").equals(found.getBenCoId()) ||
				Role.valueOf(decoded.get("role")).equals(Role.ADMIN)) {
				found.setAttachments(found.getAttachments().stream().map(s -> ctx.url() + "/attachments/" + s).collect(Collectors.toList()));
				ctx.json(found);
			}
		} else {
			log.trace("auth failed");
			throw new ForbiddenResponse();
		}
	}
	
	
	public static void createForm(Context ctx) {
		log.trace("creating form");
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		Form f = new Form();
		try {
			Employee e = es.getEmployee(decoded.get("employeeId"));
			if (e == null) {
				throw new ForbiddenResponse();
			}
			List<Form> forms = fs.getAllForms();
			StringBuilder id = new StringBuilder();
			id.append(forms.size());
			while (id.length() < 10) {
				id.insert(0, "0");
			}
			String benCoId = es.getBenCo(decoded.get("employeeId"));
			f.setFormId(id.toString());
			f.setSubmitterId(decoded.get("employeeId"));
			f.setBenCoId(benCoId);
			f.setSupervisorId(e.getDirectSupervisorId());
			f.setDeptHeadId(e.getDeptHeadId());
			f.setEventType(EventType.valueOf(ctx.formParam("eventType")));
			f.setDescription(new StringBuilder(ctx.formParam("description")));
			f.setGradingFormat(GradingFormat.valueOf(ctx.formParam("gradingFormat")));
			f.setJustification(new StringBuilder(ctx.formParam("justification")));
			f.setStatusReason(new StringBuilder(""));
			f.setLocation(ctx.formParam("location"));
			f.setTrainingStart(LocalDate.parse(ctx.formParam("trainingStart")));
			f.setTrainingEnd(LocalDate.parse(ctx.formParam("trainingEnd")));
			f.setTrainingTimeStart(LocalTime.parse(ctx.formParam("trainingTimeStart")));
			f.setTrainingTimeEnd(LocalTime.parse(ctx.formParam("trainingTimeEnd")));
			f.setSubmissionDate(LocalDate.now());
			f.setCost(new BigDecimal(ctx.formParam("cost")));
			f.setReimbursement(f.getCost().multiply(f.getEventType().reimbursementPercentage).setScale(2, RoundingMode.HALF_UP).min(e.getAvailableReimbursement()));
			f.setSupervisorApproval(false);
			if (e.getRole().equals(Role.DEPT_HEAD)) {
				f.setDeptHeadApproval(true);
			} else {
				f.setDeptHeadApproval(false);
			}
			f.setBenCoApproval(false);
			f.setStatus(FormStatus.PENDING);
			f.setAttachments(new ArrayList<>());
			fs.addForm(f);
			f.setAttachments(f.getAttachments().stream().map(s -> ctx.url() + "/attachments/" + s).collect(Collectors.toList()));
			ctx.json(f);
		} catch (Exception e) {
			log.debug("error in form");
			ctx.status(400).result(e.getMessage());
		}
	}
	
	public static void updateForm(Context ctx) {
		log.trace("updatomg form " + ctx.pathParam("id"));
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		if (tf.authorize(ctx.cookie("token"), es.getEmployee(decoded.get("employeeId")))) {
			Form found = fs.getFormById(ctx.pathParam("id"));
			Map<String, List<String>> params = ctx.formParamMap();
			//interested parties only
			if (decoded.get("employeeId").equals(found.getSubmitterId())) {
				if (params.containsKey("description")) {
					found.setDescription(new StringBuilder(ctx.formParam("description")));
				} 
				if (params.containsKey("justification")) {
					found.setJustification(new StringBuilder(ctx.formParam("justificiation")));
				}
			}
			if (decoded.get("employeeId").equals(found.getBenCoId()) || decoded.get("employeeId").equals(found.getDeptHeadId()) || decoded.get("employeeId").equals(found.getSupervisorId())) {
				if (params.containsKey("statusReason")) {
					found.setStatusReason(new StringBuilder(ctx.formParam("statusReason")));
					found.setStatus(FormStatus.REQUIRES_MORE_INFO);
				} else if (params.containsKey("reimbursement")) {
					found.setReimbursement(new BigDecimal(ctx.formParam("reimbursement")));
				}
			}
			if (Role.valueOf(decoded.get("role")).equals(Role.ADMIN)) {
				if (params.containsKey("supervisorId")) {
					found.setSupervisorId(ctx.formParam("supervisorId"));
				} 
				if (params.containsKey("deptHeadId")) {
					found.setDeptHeadId(ctx.formParam("deptHeadId"));
				}
			}
			fs.updateForm(found);
			found.setAttachments(found.getAttachments().stream().map(s -> ctx.url() + "/attachments/" + s).collect(Collectors.toList()));
			ctx.json(found);
		}
	}
	
	public static void getPendingForms(Context ctx) {
		
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		log.trace("getting pending forms for employee " + decoded.get("employeeId"));
		if (tf.authorize(ctx.cookie("token"), es.getEmployee(decoded.get("employeeId")))) {
			ctx.json(fs.getPendingForms(decoded.get("employeeId"))
					.stream()
					.peek(f -> f.setAttachments(f.getAttachments()
							.stream()
							.map(s -> ctx.url() + "/attachments/" + s)
							.collect(Collectors.toList())))
					.collect(Collectors.toList())
					.stream()
					.peek(f -> f.setFormId(ctx.url().substring(0, ctx.url().length() - ctx.path().length()) + "/forms/" + f.getFormId()))
					.collect(Collectors.toList()));
		}
	}
	
	public static void getFormsNeedingApproval(Context ctx) {
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		if (tf.authorize(ctx.cookie("token"), es.getEmployee(decoded.get("employeeId")))) {
			ctx.json(fs.getFormsNeedingApproval(decoded.get("employeeId"))
					.stream()
					.peek(f -> f.setAttachments(f.getAttachments()
							.stream()
							.map(s -> ctx.url() + "/attachments/" + s)
							.collect(Collectors.toList())))
					.collect(Collectors.toList())
					.stream()
					.peek(f -> f.setFormId(ctx.url().substring(0, ctx.url().length() - ctx.path().length()) + "/forms/" + f.getFormId()))
					.collect(Collectors.toList()));
		}
	}
	
	public static void approveForm(Context ctx) {
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		Employee loggedEmployee = es.getEmployee(decoded.get("employeeId"));
		if (tf.authorize(ctx.cookie("token"), loggedEmployee)) {
			Form f = fs.getFormById(ctx.pathParam("id"));
			log.trace("Approves: " + " " + f.getSupervisorId() + " " + f.getDeptHeadId() + " " + f.getBenCoId());
			if (!loggedEmployee.getEmployeeId().equals(f.getBenCoId()) && !loggedEmployee.getEmployeeId().equals(f.getSupervisorId()) && !loggedEmployee.getEmployeeId().equals(f.getDeptHeadId())) {
				throw new ForbiddenResponse();
			}
			if (loggedEmployee.getEmployeeId().equals(f.getBenCoId())) {
				if (f.isBenCoApproval()) {
					ctx.status(204).result("has BenCo approval");
				} else {
					f.setBenCoApproval(true);
				}
			} 
			if (loggedEmployee.getEmployeeId().equals(f.getSupervisorId())) {
				if (f.isSupervisorApproval()) {
					ctx.status(204).result("has supervisor approval or skipped");
				} else {
					f.setSupervisorApproval(true);
				}
			} 
			if (loggedEmployee.getEmployeeId().equals(f.getDeptHeadId())) {
				if (f.isDeptHeadApproval()) {
					ctx.status(204).result("has dept head approval or skipped");
				} else {
					f.setDeptHeadApproval(true);
				}
			}
			if (f.isBenCoApproval() && f.isDeptHeadApproval() && f.isSupervisorApproval()) {
				Employee e = es.getEmployee(f.getSubmitterId());
				BigDecimal reimbursement = e.getAvailableReimbursement().min(f.getReimbursement());
				e.setAvailableReimbursement(e.getAvailableReimbursement().subtract(reimbursement));
				f.setStatus(FormStatus.APPROVED);
				e.getPastForms().add(ctx.url().substring(0, ctx.url().length() - ctx.path().length()) + "/forms/" + f.getFormId());
				es.updateEmployee(e);
			}
			fs.updateForm(f);
			ctx.json(f);
		}
	}
	
	public static void denyForm(Context ctx) {
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		Employee loggedEmployee = es.getEmployee(decoded.get("employeeId"));
		log.trace("denying form");
		if (tf.authorize(ctx.cookie("token"), es.getEmployee(decoded.get("employeeId")))) {
			Form f = fs.getFormById(ctx.pathParam("id"));
			log.trace("denying form " + f.getFormId());
			if (loggedEmployee.getEmployeeId().equals(f.getBenCoId()) || loggedEmployee.getEmployeeId().equals(f.getSupervisorId()) || loggedEmployee.getEmployeeId().equals(f.getDeptHeadId())) {
				try {
					f.setStatus(FormStatus.DENIED);
					f.setStatusReason(new StringBuilder(ctx.formParam("statusReason")));;
					fs.updateForm(f);
					ctx.json(f);
				} catch (Exception e) {
					ctx.status(400).result("must include reason");
				}
			} else {
				ctx.status(403);
				return;
			}
		}
	}
	
	public static void uploadAttachment(Context ctx) {
		log.trace("uploading attachment for form " + ctx.pathParam("id"));
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		Employee e = es.getEmployee(decoded.get("employeeId"));
		Form found = fs.getFormById(ctx.pathParam("id"));
		Set<String>	keys = ctx.formParamMap().keySet();
		if (found == null) {
			throw new NotFoundResponse("form not found");
		}
		for(UploadedFile file : ctx.uploadedFiles()) {
			if (file.getExtension().equals(".msg") && keys.contains("supervisorEmail")) {
				found.setSupervisorApproval(true);
			}
			if (file.getExtension().equals(".msg") && keys.contains("deptHeadEmail")) {
				found.setDeptHeadApproval(true);
			}
		  String key = e.getEmployeeId()+ "-" + found.getFormId() + "-" + file.getFilename();
		  S3Util.getInstance().uploadToBucket(key, 
		  RequestBody.fromInputStream(file.getContent(), file.getSize()));
		  found.getAttachments().add(key);
		  fs.updateForm(found);
		}
		found.setAttachments(found.getAttachments().stream().map(s -> ctx.url() + s).collect(Collectors.toList()));
		ctx.json(found);
	}
	
	public static void getAttachments(Context ctx) {
		log.trace("getting attachments for form " + ctx.pathParam("id"));
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		Employee loggedEmployee = es.getEmployee(decoded.get("employeeId"));
		if (tf.authorize(ctx.cookie("token"), es.getEmployee(decoded.get("employeeId")))) {
			Form f = fs.getFormById(ctx.pathParam("id"));
			if (f == null) {
				throw new NotFoundResponse();
			} else if (loggedEmployee.getEmployeeId().equals(f.getSubmitterId()) || loggedEmployee.getEmployeeId().equals(f.getBenCoId()) || loggedEmployee.getEmployeeId().equals(f.getSupervisorId()) || loggedEmployee.getEmployeeId().equals(f.getDeptHeadId())) {
				ctx.json(f.getAttachments().stream().map(s -> ctx.url() + "/" + s).collect(Collectors.toList()));
			} else {
				throw new ForbiddenResponse();
			}
		}
	}
	
	public static void getAttachment(Context ctx) {
		log.trace("getting an attachment");
		Map<String, String> decoded = tf.decode(ctx.cookie("token"));
		Employee loggedEmployee = es.getEmployee(decoded.get("employeeId"));
		if (tf.authorize(ctx.cookie("token"), es.getEmployee(decoded.get("employeeId")))) {
			Form f = fs.getFormById(ctx.pathParam("id"));
			if (f == null) {
				throw new NotFoundResponse();
			} else if (loggedEmployee.getEmployeeId().equals(f.getSubmitterId()) || loggedEmployee.getEmployeeId().equals(f.getBenCoId()) || loggedEmployee.getEmployeeId().equals(f.getSupervisorId()) || loggedEmployee.getEmployeeId().equals(f.getDeptHeadId())) {
				ctx.result(S3Util.getInstance().getObject(ctx.pathParam("key")));
			} else {
				throw new ForbiddenResponse();
			}
		}
		
	}
}
