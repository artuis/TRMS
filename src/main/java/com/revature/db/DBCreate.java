package com.revature.db;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.utils.CassandraUtil;

public class DBCreate {
	public static void dbcreate() {
		StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ").append("TRMS with replication = {")
				.append("'class':'SimpleStrategy','replication_factor':1};");
		CqlSession session = CassandraUtil.getInstance().getSession();
		SimpleStatement s = new SimpleStatementBuilder(sb.toString())
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		session.execute(s);
	}
	
	public static void employeeTable() {
		StringBuilder sb = new StringBuilder("Drop TABLE IF EXISTS Employee;");
		sb = new StringBuilder("CREATE TABLE IF NOT EXISTS Employee (")
				.append("employee_id text primary key, first_name text, last_name text, direct_supervisor_id text,")
				.append("dept_head_id text, role text, available_reimbursement decimal, pending_reimbursement decimal,")
				.append("last_updated timestamp, is_benco boolean, form_history list<text>);");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	public static void formTable() {
		StringBuilder sb = new StringBuilder("Drop TABLE IF EXISTS Form;");
		sb = new StringBuilder("CREATE TABLE IF NOT EXISTS Form (")
				.append("form_id text, submitter_id text, benco_id text, direct_supervisor_id text, dept_head_id text,")
				.append("event_type text, description text, grading_format text, justification text, status_reason text, location text,")
				.append("training_start date, training_end date, training_time_start time, training_time_end time, submission_date date,")
				.append("cost decimal, reimbursement decimal, supervisor_approval boolean, dept_head_approval boolean, ben_co_approval boolean, ")
				.append("status text,attachments list<text>, primary key (event_type, submission_date, form_id));");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
}
