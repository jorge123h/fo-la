package com.twobig.sivale.dao;

import java.util.List;

import com.twobig.sivale.bd.to.TPublication;

public interface TPublicationDAO {
	
	public List<TPublication> getTCampaignByPublicationId(List<Integer> publicationId);
	
	//SE AGREGO ESTA SOBRECARGA DE METODO
	public List<TPublication> getTCampaignByPublicationId(int campaign);
	
}
