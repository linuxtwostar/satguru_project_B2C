package com.tt.ts.rest.pax.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@Table(name = "TT_TS_PAX_FREQUENT_FLYER")
public class PaxFrequentFlyer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAX_ID")
    private PaxModel paxModel;

    @Id
    @Column(name = "SEQUENCE")
    private Integer sequence;

    @Column(name = "FREQUENT_FLYER")
    private String freqFlyr;

    @Column(name = "FREQUENT_FLYER_NUMBER")
    private String frequentFlyerNumber;

    @JsonBackReference
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

    public String getFrequentFlyerNumber() {
	return frequentFlyerNumber;
    }

    public void setFrequentFlyerNumber(String frequentFlyerNumber) {
	this.frequentFlyerNumber = frequentFlyerNumber;
    }

    public String getFreqFlyr() {
	return freqFlyr;
    }

    public void setFreqFlyr(String freqFlyr) {
	this.freqFlyr = freqFlyr;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

}
