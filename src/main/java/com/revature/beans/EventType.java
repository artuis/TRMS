package com.revature.beans;

import java.math.BigDecimal;

public enum EventType {
	UNIVERSITY_COURSE(0.8), 
	SEMINAR(0.65), 
	CERTIFICATION_PREPARATION_CLASS(0.75), 
	TECHNICAL_TRAINING(1), 
	OTHER(0.3);
	
	public final BigDecimal reimbursementPercentage;
	
	private EventType(double percentage) {
		this.reimbursementPercentage = new BigDecimal(percentage);
	}
	
}
