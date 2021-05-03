package com.revature.data;


import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.beans.EventType;
import com.revature.beans.Form;
import com.revature.beans.FormStatus;
import com.revature.beans.GradingFormat;
import com.revature.utils.CassandraUtil;

public class FormDaoImpl implements FormDao {
	private CqlSession session = CassandraUtil.getInstance().getSession();
	
	public FormDaoImpl() {
		super();
		session = CassandraUtil.getInstance().getSession();
	}
	
	public FormDaoImpl(CqlSession session) {
		this.session = session;
	}
	
	@Override
	public List<Form> getForms() {
		List<Form> forms = new ArrayList<>();
		String query = "select * from form;";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		ResultSet rs = session.execute(s);
		rs.forEach(r -> {
			Form f = new Form();
			f.setFormId(r.getString("form_id"));
			f.setSubmitterId(r.getString("submitter_id"));
			f.setBenCoId(r.getString("benco_id"));
			f.setSupervisorId(r.getString("direct_supervisor_id"));
			f.setDeptHeadId(r.getString("dept_head_id"));
			f.setEventType(EventType.valueOf(r.getString("event_type")));
			f.setDescription(new StringBuilder(r.getString("description")));
			f.setGradingFormat(GradingFormat.valueOf(r.getString("grading_format")));
			f.setJustification(new StringBuilder(r.getString("justification")));
			f.setStatusReason(new StringBuilder(r.getString("status_reason")));
			f.setLocation(r.getString("location"));
			f.setTrainingStart(r.getLocalDate("training_start"));
			f.setTrainingEnd(r.getLocalDate("training_end"));
			f.setTrainingTimeStart(r.getLocalTime("training_time_start"));
			f.setTrainingTimeEnd(r.getLocalTime("training_time_end"));
			f.setSubmissionDate(r.getLocalDate("submission_date"));
			f.setCost(r.getBigDecimal("cost"));
			f.setReimbursement(r.getBigDecimal("reimbursement"));
			f.setSupervisorApproval(r.getBoolean("supervisor_approval"));
			f.setDeptHeadApproval(r.getBoolean("dept_head_approval"));
			f.setBenCoApproval(r.getBoolean("ben_co_approval"));
			f.setStatus(FormStatus.valueOf(r.getString("status")));
			f.setAttachments(r.getList("attachments", String.class));
			forms.add(f);
		});
		return forms;
	}

	@Override
	public void addForm(Form f) {
		String query = "Insert into Form (form_id, submitter_id, benco_id, direct_supervisor_id, dept_head_id,"
				+ "event_type, description, grading_format, justification, status_reason, location, training_start,"
				+ "training_end, training_time_start, training_time_end, submission_date, cost, reimbursement, supervisor_approval, dept_head_approval,"
				+ "ben_co_approval, status, attachments) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		
		BoundStatement bound = session.prepare(s).bind(f.getFormId(), f.getSubmitterId(), f.getBenCoId(), f.getSupervisorId(), f.getDeptHeadId(), 
														f.getEventType().toString(), f.getDescription().toString(), f.getGradingFormat().toString(), f.getJustification().toString(),
														f.getStatusReason().toString(), f.getLocation(), f.getTrainingStart(), f.getTrainingEnd(),
														f.getTrainingTimeStart(), f.getTrainingTimeEnd(), f.getSubmissionDate(), f.getCost(), f.getReimbursement(),
														f.isSupervisorApproval(), f.isDeptHeadApproval(), f.isBenCoApproval(), f.getStatus().toString(), f.getAttachments());
		session.execute(bound);
	}

	@Override
	public void updateForm(Form f) {
		String query = "Update form Set submitter_id = ?, benco_id = ?, direct_supervisor_id = ?, dept_head_id = ?,"
				+ "description = ?, grading_format = ?, justification = ?, status_reason = ?, location = ?, training_start = ?,"
				+ "training_end = ?, training_time_start = ?, training_time_end = ?, cost = ?, reimbursement = ?, supervisor_approval = ?, "
				+ "dept_head_approval = ?, ben_co_approval = ?, status = ?, attachments = ? Where event_type = ? AND submission_date = ? AND form_id = ? IF EXISTS;";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		
		BoundStatement bound = session.prepare(s).bind(f.getSubmitterId(), f.getBenCoId(), f.getSupervisorId(), f.getDeptHeadId(), 
														f.getDescription().toString(), f.getGradingFormat().toString(), f.getJustification().toString(),
														f.getStatusReason().toString(), f.getLocation(), f.getTrainingStart(), f.getTrainingEnd(),
														f.getTrainingTimeStart(), f.getTrainingTimeEnd(), f.getCost(), f.getReimbursement(),
														f.isSupervisorApproval(), f.isDeptHeadApproval(), f.isBenCoApproval(), f.getStatus().toString(), f.getAttachments(),
														f.getEventType().toString(), f.getSubmissionDate(),  f.getFormId());
		session.execute(bound);
	}

}
