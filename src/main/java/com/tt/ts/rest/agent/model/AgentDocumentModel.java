package com.tt.ts.rest.agent.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

@Entity(name="agentDocModelRest")
@Table(name = "TT_TS_AGENT_DOCUMENT")
public class AgentDocumentModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGENT_ID")
	private UserModal user;

	@Id
	@Column(name = "SEQUENCE")
	private Integer sequence;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DOCUMENT_TYPE")
	private Integer docType;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "PATH")
	private String path;

	@Column(name = "ABSTRACT")
	private String abstrct;

	@Column(name = "CREATED_BY", insertable = true, updatable = false)
	private Integer createdBy = 0;

	@Column(name = "CREATION_TIME", insertable = true, updatable = false)
	private Date creationTime;

	@Column(name = "LAST_UPDATED_BY", insertable = true)
	private Integer lastUpdatedBy;

	@Column(name = "LAST_MOD_TIME", insertable = true)
	private Date lastModTime;

	@Transient
	private transient List<MultipartFile> docList = new ArrayList<>();

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDocType() {
		return docType;
	}

	public void setDocType(Integer docType) {
		this.docType = docType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAbstrct() {
		return abstrct;
	}

	public void setAbstrct(String abstrct) {
		this.abstrct = abstrct;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(Date lastModTime) {
		this.lastModTime = lastModTime;
	}

	@JsonIgnore
	public List<MultipartFile> getDocList() {
		return docList;
	}

	public void setDocList(List<MultipartFile> docList) {
		this.docList = docList;
	}

	@JsonIgnore
	public Integer getCreatedBy() {
		return createdBy;
	}

	public UserModal getUser() {
		return user;
	}

	public void setUser(UserModal user) {
		this.user = user;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
