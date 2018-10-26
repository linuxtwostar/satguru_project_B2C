package com.tt.ts.rest.agent.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "TT_TS_AGENT_CREDIT_LIMIT_SETTINGS")
public class AgentCreditLimitModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGENT_ID")
	private UserModal userModal;
	
	@Id
	@Column(name = "SEQUENCE")
	private Integer sequence;
	
	@Column(name = "MONTH")
	private String month;
	
	@Column(name = "YEAR")
	private Integer year;
	
	@Column(name = "AMOUNT")
	private BigInteger amount;
	
	@Column(name = "STATUS")
	private String status;

	@Transient
	private String monthString;
	
	public String getMonthString() {
		return monthString;
	}

	public void setMonthString(String monthString) {
		this.monthString = monthString;
	}

	@JsonIgnore
	public UserModal getUserModal() {
		return userModal;
	}

	public void setUserModal(UserModal userModal) {
		this.userModal = userModal;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	
	public BigInteger getAmount() {
		return amount;
	}

	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
