package com.twobig.sivale.beans;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.twobig.sivale.bd.to.TCampaign;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignDetailBean extends TCampaign {
	
	private List<String> classification;

	public List<String> getClassification() {
		return classification;
	}

	public void setClassification(List<String> classification) {
		this.classification = classification;
	}
	
	
}
