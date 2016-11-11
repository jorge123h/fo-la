package com.twobig.sivale.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.twobig.sivale.bd.to.CatClassificationCampaign;
import com.twobig.sivale.bd.to.TCampaign;
import com.twobig.sivale.bd.to.TReportMovements;
import com.twobig.sivale.beans.AccountStatusBean;
import com.twobig.sivale.beans.AccountStatusFilterBean;
import com.twobig.sivale.beans.ExcelBean;
import com.twobig.sivale.constants.RMConstants;
import com.twobig.sivale.dao.CatClassificationCampaignDAO;
import com.twobig.sivale.dao.TReportMovementsDAO;
import com.twobig.sivale.service.CatClassificationCampaignService;
import com.twobig.sivale.service.TReportMovementsService;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TReportMovementsServiceImpl implements TReportMovementsService {
	
	@Autowired
	public TReportMovementsDAO tReportMovementsDAO;
	
	@Autowired
	public CatClassificationCampaignDAO catClassificationCampaignDAO;
	
	@Autowired
	public CatClassificationCampaignService catClassificationCampaignService;
	
	private static final Logger logger = LogManager.getLogger(TReportMovementsServiceImpl.class);

	@Override
	public String uploadRMFile(TCampaign tCampaign, File file) {
		
		ExcelServiceImpl excelservice = new ExcelServiceImpl();
		ExcelBean excelBean = excelservice.getExcelData(file);
		
		List<HashMap<String, String>> rows = excelBean.getRows();
		
		TReportMovements tReportMovements;
		
		tReportMovementsDAO.deleteAllByCampaign(tCampaign);
		
		for (HashMap<String, String> hashMap : rows) {
			
			tReportMovements = new TReportMovements();
			tReportMovements.setCampaign(tCampaign);
			tReportMovements.setMonth(hashMap.get(RMConstants.MES_EJECUCION));
			tReportMovements.setYear(Integer.parseInt(hashMap.get(RMConstants.YEAR)));
			tReportMovements.setIdStars(hashMap.get(RMConstants.ID_STARS));
			tReportMovements.setPuestoEnStars(hashMap.get(RMConstants.PUESTO_EN_STARS));
			tReportMovements.setEmployeeName(hashMap.get(RMConstants.NOMBRE_DEL_EMPLEADO));
			tReportMovements.setSivaleName(hashMap.get(RMConstants.NOMBRE_SIVALE));
			tReportMovements.setCardNumber(hashMap.get(RMConstants.NUMERO_DE_TARJETA_TITULAR));
			tReportMovements.setBid(hashMap.get(RMConstants.BID));
			tReportMovements.setRazonSocial(hashMap.get(RMConstants.RAZÓN_SOCIAL));
			tReportMovements.setMonto(Integer.parseInt(hashMap.get(RMConstants.MONTO)));
			tReportMovements.setCompania(hashMap.get(RMConstants.CAMPAIGN));
			tReportMovements.setMovements(hashMap.get(RMConstants.MOVIMIENTO));
			tReportMovements.setObservaciones(hashMap.get(RMConstants.OBSERVACIONES));
			
			tReportMovementsDAO.insertRM(tReportMovements);
		}
		
		return null;
	}

	@Override
	public List<TReportMovements> getAllTReportMovementsByCampaignId(Integer campaignId) {
		
		return tReportMovementsDAO.getAllTReportMovementsByCampaignId(campaignId);
	}
	
	@Override
	public List<AccountStatusBean> getAllAccountStatusByCampaignId(Integer campaignId) {
		
		List<TReportMovements> listTReportMovements = tReportMovementsDAO.getAllTReportMovementsByCampaignId(campaignId);
		
		List<AccountStatusBean> listAccountStatusBean = new ArrayList<AccountStatusBean>(); 
		
		addToListAccountStatusBean(listAccountStatusBean,listTReportMovements);
		
		return listAccountStatusBean; 
	}

	@Override
	public List<AccountStatusBean> getAccountStatusPendingByCampaignId(Integer campaignId) {
		
		List<AccountStatusBean> listAccountStatusBean = new ArrayList<AccountStatusBean>();
		
		List<TReportMovements> listTReportMovements = tReportMovementsDAO.getTReportMovementsNoRepeatByCampaignId(campaignId);
		
		int pendiente = 0;
		int ganado = 0;
		int pagado = 0; 
		
		for (TReportMovements tReportMovements : listTReportMovements) {
			
			List<TReportMovements> listTReportGanado = 
					tReportMovementsDAO.getTReportMovementsByIdStarsCampaignIdMovement(campaignId, tReportMovements.getIdStars(), RMConstants.MOVIMIENTO_GANADO);
			
			int ganadoPorUsuario = 0; 
			
			for (TReportMovements tReportMovements2 : listTReportGanado) {
				ganadoPorUsuario += tReportMovements2.getMonto();
			} 
			
			List<TReportMovements> listTReportDispersado = 
				tReportMovementsDAO.getTReportMovementsByIdStarsCampaignIdMovement(campaignId, tReportMovements.getIdStars(), RMConstants.MOVIMIENTO_DISPERSADO);
			
			int dispersadoPorUsuario = 0;
			
			for (TReportMovements tReportMovements2 : listTReportDispersado) {
				dispersadoPorUsuario += tReportMovements2.getMonto();
			} 
			
			if (dispersadoPorUsuario < ganadoPorUsuario) {
				pendiente += ganadoPorUsuario - dispersadoPorUsuario;
				addToListAccountStatusBean(listAccountStatusBean,listTReportGanado);
				addToListAccountStatusBean(listAccountStatusBean,listTReportDispersado);
				ganado += ganadoPorUsuario;
				pagado += dispersadoPorUsuario; 
			}
			
		}
		
		if(!listAccountStatusBean.isEmpty()){
			listAccountStatusBean.get(0).setPagado(pagado);
			listAccountStatusBean.get(0).setPendiente(pendiente);
			listAccountStatusBean.get(0).setGanado(ganado);
		}
		
		return listAccountStatusBean;
	}
	
	@Override
	public List<AccountStatusBean> getAccountStatusWithoutPendingByCampaignId(Integer campaignId) {
		
		List<AccountStatusBean> listAccountStatusBean = new ArrayList<AccountStatusBean>();
		
		List<TReportMovements> listTReportMovements = tReportMovementsDAO.getTReportMovementsNoRepeatByCampaignId(campaignId);
		
		int ganado = 0;
		int pagado = 0;
		
		for (TReportMovements tReportMovements : listTReportMovements) {
			
			List<TReportMovements> listTReportGanado = 
					tReportMovementsDAO.getTReportMovementsByIdStarsCampaignIdMovement(campaignId, tReportMovements.getIdStars(), RMConstants.MOVIMIENTO_GANADO);
			
			int ganadoPorUsuario = 0; 
			
			for (TReportMovements tReportMovements2 : listTReportGanado) {
				ganadoPorUsuario += tReportMovements2.getMonto();
			} 
			
			List<TReportMovements> listTReportDispersado = 
				tReportMovementsDAO.getTReportMovementsByIdStarsCampaignIdMovement(campaignId, tReportMovements.getIdStars(), RMConstants.MOVIMIENTO_DISPERSADO);
			
			int dispersadoPorUsuario = 0;
			
			for (TReportMovements tReportMovements2 : listTReportDispersado) {
				dispersadoPorUsuario += tReportMovements2.getMonto();
			}
			
			if(dispersadoPorUsuario == ganadoPorUsuario){
				ganado += ganadoPorUsuario;
				pagado += dispersadoPorUsuario; 
				addToListAccountStatusBean(listAccountStatusBean,listTReportGanado);
				addToListAccountStatusBean(listAccountStatusBean,listTReportDispersado);
				
			}
			
		}
		
		if(!listAccountStatusBean.isEmpty()){
			listAccountStatusBean.get(0).setPagado(pagado);
			listAccountStatusBean.get(0).setPendiente(0);
			listAccountStatusBean.get(0).setGanado(ganado);
		}
		
		
		return listAccountStatusBean;
	}
	
	private void addToListAccountStatusBean(List<AccountStatusBean> listAccountStatusBean, 
			List<TReportMovements> listTReport){
		
		for (TReportMovements tReportMovements : listTReport) {
			AccountStatusBean accountStatusBean = new AccountStatusBean();
			
			CatClassificationCampaign catClassificationCampaignUnidad = 
					catClassificationCampaignDAO.getCatClassificationCampaignById(tReportMovements.getCampaign().getClassificationId());
			
			CatClassificationCampaign catClassificationCampaignSubprograma = 
					catClassificationCampaignDAO.getCatClassificationCampaignById(catClassificationCampaignUnidad.getCatClassificationCampaignsIdParent());
			
			CatClassificationCampaign catClassificationCampaignPrograma = 
					catClassificationCampaignDAO.getCatClassificationCampaignById(catClassificationCampaignSubprograma.getCatClassificationCampaignsIdParent());
			
			CatClassificationCampaign catClassificationCampaignCompania = 
					catClassificationCampaignDAO.getCatClassificationCampaignById(catClassificationCampaignPrograma.getCatClassificationCampaignsIdParent());
			
			accountStatusBean.setMonto(tReportMovements.getMonto());
			accountStatusBean.setMovements(tReportMovements.getMovements());
			accountStatusBean.setStartDate(tReportMovements.getCampaign().getStartDate());
			accountStatusBean.setEndDate(tReportMovements.getCampaign().getEndDate());
			accountStatusBean.setCampaignName(tReportMovements.getCampaign().getCampaignName());
			accountStatusBean.setCompania(catClassificationCampaignCompania.getClassName());
			accountStatusBean.setPrograma(catClassificationCampaignPrograma.getClassName());
			accountStatusBean.setSubprograma(catClassificationCampaignSubprograma.getClassName());
			accountStatusBean.setUnidadDeNegocio(catClassificationCampaignUnidad.getClassName());
			accountStatusBean.setNombre(tReportMovements.getEmployeeName());
			accountStatusBean.setBid(tReportMovements.getBid());
			accountStatusBean.setIdStars(tReportMovements.getIdStars());
			accountStatusBean.setObservaciones(tReportMovements.getObservaciones());
			
			listAccountStatusBean.add(accountStatusBean);
		} 
	}

	@Override
	public List<AccountStatusBean> getAccountStatusByCampaignIdAndFilter(Integer campaignId,
			AccountStatusFilterBean filterBean) {
		
		
		List<TReportMovements> listTReportMovements = tReportMovementsDAO.getAllTReportMovementsByCampaignIdAndFilter(campaignId, filterBean);
		
		List<AccountStatusBean> listAccountStatusBean = new ArrayList<AccountStatusBean>(); 
		
		addToListAccountStatusBean(listAccountStatusBean,listTReportMovements);
		
		return listAccountStatusBean;
	}

}
