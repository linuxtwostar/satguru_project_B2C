package com.tt.ts.rest.dashboard.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "TT_TS_AGENT_TODOLIST")
public class AgentToDoListModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TASK_ID")
    private Integer taskId;
	
	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTaskMessage() {
		return taskMessage;
	}

	public void setTaskMessage(String taskMessage) {
		this.taskMessage = taskMessage;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreatedByAgentId() {
		return createdByAgentId;
	}

	public void setCreatedByAgentId(Integer createdByAgentId) {
		this.createdByAgentId = createdByAgentId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastUpdationTime() {
		return lastUpdationTime;
	}

	public void setLastUpdationTime(Date lastUpdationTime) {
		this.lastUpdationTime = lastUpdationTime;
	}

	public Integer getLastUpdatedByAgentId() {
		return lastUpdatedByAgentId;
	}

	public void setLastUpdatedByAgentId(Integer lastUpdatedByAgentId) {
		this.lastUpdatedByAgentId = lastUpdatedByAgentId;
	}

	@Column(name="TASK_MESSAGE")
	private String taskMessage;
	
	@Column(name = "TASK_DUE_TYPE")
    private Integer taskType;
	
	@Column(name = "TASK_DUE_DATE")
    private Date dueDate;

    @Column(name = "TASK_STATUS")
    private Integer status;

    @NotNull
    @Column(name = "CREATED_BY")
    private Integer createdByAgentId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "CREATION_TIME")
    private Date creationTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "LAST_MOD_TIME")
    private Date lastUpdationTime;

    @Column(name = "LAST_UPDATED_BY")
    private Integer lastUpdatedByAgentId;
    
    @Transient
    private String dueTypeName;

	public String getDueTypeName() {
		return dueTypeName;
	}

	public void setDueTypeName(String dueTypeName) {
		this.dueTypeName = dueTypeName;
	}

	

}
