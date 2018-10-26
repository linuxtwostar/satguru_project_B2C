package com.tt.ts.rest.pax.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@Table(name = "TT_TS_PAX_RELATION")
public class PaxRelationModel {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "SEQUENCE_ID", strategy = GenerationType.TABLE)
    @TableGenerator(name = "SEQUENCE_ID", table = "TT_SEQUENCES", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_NEXT_HI_VALUE", pkColumnValue = "PAX_RELATION_UID", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CO_PAX_ID")
    private PaxModel paxModel;

    @Column(name = "PAX_ID")
    private Integer paxId;

    @Column(name = "RELATION")
    private String relation;

    @Column(name = "PAX_TYPE")
    private Integer paxType;

    @Transient
    private Integer coPaxId;

    @Transient
    private PaxModel pax;
    
    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getPaxId() {
	return paxId;
    }

    public void setPaxId(Integer paxId) {
	this.paxId = paxId;
    }
    
    @JsonBackReference
    public PaxModel getPaxModel() {
	return paxModel;
    }

    public void setPaxModel(PaxModel paxModel) {
	this.paxModel = paxModel;
    }

    public String getRelation() {
	return relation;
    }

    public void setRelation(String relation) {
	this.relation = relation;
    }

    public Integer getPaxType() {
	return paxType;
    }

    public void setPaxType(Integer paxType) {
	this.paxType = paxType;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

    public Integer getCoPaxId() {
	return coPaxId;
    }

    public void setCoPaxId(Integer coPaxId) {
	this.coPaxId = coPaxId;
    }

	public PaxModel getPax() {
		return pax;
	}

	public void setPax(PaxModel pax) {
		this.pax = pax;
	}

}
