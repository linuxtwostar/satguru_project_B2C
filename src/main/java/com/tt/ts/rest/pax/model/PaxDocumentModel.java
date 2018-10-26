package com.tt.ts.rest.pax.model;

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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "TT_TS_PAX_DOCUMENT")
public class PaxDocumentModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAX_ID")
    private PaxModel paxModel;

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
    private Integer createdBy;

    @Column(name = "CREATION_TIME", insertable = true, updatable = false)
    private Date creationTime;

    @Column(name = "LAST_UPDATED_BY", insertable = true)
    private Integer lastUpdatedBy;

    @Column(name = "LAST_MOD_TIME", insertable = true)
    private Date lastModTime;

    private transient List<MultipartFile> docList = new ArrayList<>();

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

    public void setCreatedBy(Integer createdBy) {
	this.createdBy = createdBy;
    }

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

    public Integer getCreatedBy() {
	return createdBy;
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
    public PaxModel getPaxModel() {
	return paxModel;
    }

    public void setPaxModel(PaxModel paxModel) {
	this.paxModel = paxModel;
    }
}
