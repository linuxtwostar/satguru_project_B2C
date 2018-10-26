package com.tt.ts.rest.pax.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "TT_TS_PAX_AIRLINE_PREFERNCE")
public class PaxAirlinePrefernce implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAX_ID")
    private PaxModel paxModel;

    @Id
    @Column(name = "SEQUENCE")
    private Integer sequence;

    @Column(name = "AIRLINE")
    private String airline;

    @JsonIgnore
    public PaxModel getPaxModel() {
	return paxModel;
    }

    public void setPaxModel(PaxModel paxModel) {
	this.paxModel = paxModel;
    }

    public Integer getSequence() {
	return sequence;
    }

    public void setSequence(Integer sequence) {
	this.sequence = sequence;
    }

    public String getAirline() {
	return airline;
    }

    public void setAirline(String airline) {
	this.airline = airline;
    }

}
