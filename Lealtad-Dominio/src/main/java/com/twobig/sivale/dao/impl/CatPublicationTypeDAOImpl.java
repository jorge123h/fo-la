package com.twobig.sivale.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.twobig.sivale.bd.to.CatPublicationType;
import com.twobig.sivale.dao.CatPublicationTypeDAO;

@Repository
public class CatPublicationTypeDAOImpl extends GenericDAOImpl<CatPublicationType, Long> 
		implements CatPublicationTypeDAO {

	
	public CatPublicationTypeDAOImpl(){
		super(CatPublicationType.class);
	}

	@Override
	public List<CatPublicationType> getAllCatPublicationType() {
		return buscar();
	}
	
}
