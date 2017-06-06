package com.twobig.sivale.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.twobig.sivale.bd.to.CatClassificationCampaign;
import com.twobig.sivale.bd.to.CatClient;
import com.twobig.sivale.bd.to.CatPublicationType;
import com.twobig.sivale.bd.to.TCampaign;
import com.twobig.sivale.bd.to.TPublication;
import com.twobig.sivale.bd.to.TUser;
import com.twobig.sivale.beans.CampaignDetailAdminBean;
import com.twobig.sivale.beans.CampaignDetailBean;
import com.twobig.sivale.beans.FormNewCampaignBean;
import com.twobig.sivale.beans.PublicationBean;
import com.twobig.sivale.beans.SearchCampaignBean;
import com.twobig.sivale.beans.SelectClassificationCampaignBean;
import com.twobig.sivale.beans.UpdateCampaignBean;
import com.twobig.sivale.beans.ViewPublicationBean;
import com.twobig.sivale.constants.PathConstants;
import com.twobig.sivale.service.CatClassificationCampaignService;
import com.twobig.sivale.service.FilterCampaignService;
import com.twobig.sivale.service.TAttachedFileService;
import com.twobig.sivale.service.TCampaignsService;
import com.twobig.sivale.service.TPublicationService;
import com.twobig.sivale.service.ViewPublicationService;
import com.twobig.sivale.service.impl.CompanyServiceImpl;



@ParentPackage(value = "json-default")
@Namespace("/")
public class CampaignAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	CatClassificationCampaignService classificationCampaignService;
	@Autowired
	TCampaignsService campaignService;
	@Autowired
	FilterCampaignService filterCampaignService;
	@Autowired
	TPublicationService publicationService;
	@Autowired
	ViewPublicationService viewPublicationService;
	@Autowired
	CatClassificationCampaignService classificationService;
	@Autowired
	TAttachedFileService tAttachedFileService; 
	@Autowired
	CompanyServiceImpl companyServiceImpl;
	
	private Map<String, Object> session;
	private List<CatClassificationCampaign> classifications;
	private List<CampaignDetailBean> campaigns;
	private List<CampaignDetailAdminBean> campaignsAdmin;
	private List<CampaignDetailBean> searchCampaigns;
	private List<CampaignDetailAdminBean> searchCampaignsAdmin;
	private List<TPublication> publications;
	private PublicationBean publication;
	private List<SelectClassificationCampaignBean> classificationLevel;
	private Integer campaignId;
	private Integer classificationId;
	private String cardNumber;
	private List<UpdateCampaignBean> selectCampaign;
	private List<SelectClassificationCampaignBean> selectPublicationTypes;
	private Map<String, String> message;
	private List<CatClient> listCompany;

	public static final String CODE = "code";
	public static final String MESSAGE = "message";
	
	public static final String SUCCESS_CODE = "001";
	public static final String ERROR_CODE 	= "002";
	
	public static final String ERROR_CREATE_CAMPAIGN 	= "Se produjo un error al Crear la campaña";
	public static final String ERROR_UPDATE_CAMPAIGN 	= "Se produjo un error al actualizar la campaña";
	public static final String SUCCESS_CRATE_CAMPAIGN 	= "Campaña creada correctamente";
	public static final String SUCCESS_DELETE_CAMPAIGN 	= "Campaña eliminada correctamente";
	public static final String SUCCESS_UPDATE_CAMPAIGN 	= "Campaña actualizada correctamente";

	private static final Logger logger = LogManager.getLogger(CampaignAction.class);
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "updateSessionAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"session", "excludeNullProperties", "true", "noCache", "true" }) )
	public String updateSessionAction() {
		Integer mensaje = (Integer) session.get("mensaje");
		mensaje++;
		session.put("mensaje", mensaje);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "getMyClassificationsAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"classifications", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getMyClassificationsAction() {
		TUser user = (TUser)session.get("user");
		if (user==null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		classifications = classificationCampaignService.getCatClassificationCampaignByUserId(user.getUserId());
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "getClassificationsAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"classifications", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getClassificationsAction() {
		TUser user = (TUser)session.get("user");
		if (user==null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		classifications = classificationCampaignService.getClassificationCampaignByUserId(user.getUserId());
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	@Action(value = "getCampaignsAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"campaigns", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getCampaignsAction() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		String classificationCmpJSON = request.getParameter("classificationCmp");
		CatClassificationCampaign classificationCmp;
		if (!classificationCmpJSON.equals("undefined")) {
			classificationCmp = new CatClassificationCampaign();
			try {
				classificationCmp = new ObjectMapper().readValue(classificationCmpJSON,
						CatClassificationCampaign.class);
			} catch (IOException e) {
				e.printStackTrace();
				return ERROR;
			}
			session.put("classificationCmp", classificationCmp);
		} else {
			classificationCmp = (CatClassificationCampaign) session.get("classificationCmp");
			if (classificationCmp == null) {
				return ERROR;
			}
		}
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		campaigns = campaignService.getFullCampaignByUserIdAndClassificationId(user.getUserId(), classificationCmp.getCatClassificationCampaignsId());	 
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "getCampaignsByComanyAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"campaigns", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getCampaignsByComanyAction() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		String classificationCmpJSON = request.getParameter("classificationCmp");
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		campaigns = campaignService.getCampaignByUserIdAndClassificationCampaignsId(user.getUserId(), user.getCompany());
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	@Action(value = "getCampaignsAdminAction", results = { @Result(name = SUCCESS, type = "json", params = { "root",
			"campaignsAdmin", "excludeNullProperties", "true", "noCache", "true" }), @Result(name = ERROR, location = "/error.jsp") })
	public String getCampaignsAdminAction() {
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		campaignsAdmin = campaignService.getCampaingsSuper(user.getUserId());
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "searchCampaignsAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"searchCampaigns", "excludeNullProperties", "true", "noCache", "true" }) )
	public String searchCampaignsAction() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		String searchCampaignJSON = request.getParameter("searchCampaign");
		SearchCampaignBean searchCampaign;
		if (!searchCampaignJSON.equals("undefined")) {
			searchCampaign = new SearchCampaignBean();
			try {
				searchCampaign = new ObjectMapper().readValue(searchCampaignJSON,
						SearchCampaignBean.class);
				CatClassificationCampaign classification = (CatClassificationCampaign) session.get("classificationCmp");
				if(classification == null){
					logger.error("No existe la clasificación");
					return ERROR;
				}
				searchCampaign.setClassificationParentId(classification.getCatClassificationCampaignsId());
			} catch (IOException e) {
				logger.error("Error al crear JSON");
				e.printStackTrace();
				return ERROR;
			}
		} else {
			return ERROR;
		}
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		searchCampaigns = filterCampaignService.FilterCampaign(user.getUserId(), searchCampaign);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "searchCampaignsAdminAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"searchCampaignsAdmin", "excludeNullProperties", "true", "noCache", "true" }) )
	public String searchCampaignsAdminAction() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		String searchCampaignJSON = request.getParameter("searchCampaign");
		SearchCampaignBean searchCampaign;
		if (!searchCampaignJSON.equals("undefined")) {
			searchCampaign = new SearchCampaignBean();
			try {
				searchCampaign = new ObjectMapper().readValue(searchCampaignJSON,
						SearchCampaignBean.class);
			} catch (IOException e) {
				logger.error("Error al crear objeto JSON");
				e.printStackTrace();
				return ERROR;
			}
		} else {
			logger.error("Error al crear objeto JSON");
			return ERROR;
		}
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		searchCampaignsAdmin = filterCampaignService.FilterCampaignAdmin(user.getCompany(), searchCampaign);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "getPublicationsAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"publications", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getPubliationsAction() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		String campaignJSON = request.getParameter("campaign");
		TCampaign campaign;
		if (!campaignJSON.equals("undefined")) {
			campaign = new TCampaign();
			try {
				campaign = new ObjectMapper().readValue(campaignJSON, TCampaign.class);
			} catch (IOException e) {
				logger.error("Error con JSON");
				e.printStackTrace();
				return ERROR;
			}
			session.put("campaign", campaign);
		} else {
			campaign = (TCampaign) session.get("campaign");
			if (campaign == null) {
				logger.error("No existe la campaña");
				return ERROR;
			}
		}
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		publications = publicationService.getTPublicationCampaignId(campaign.getCampaignId(), user.getCatProfile());
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "getPublicationsAdminAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"publications", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getPubliationsAdminAction() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		String campaignJSON = request.getParameter("campaign");
		TCampaign campaign;
		if (!campaignJSON.equals("undefined")) {
			campaign = new TCampaign();
			try {
				campaign = new ObjectMapper().readValue(campaignJSON, TCampaign.class);
			} catch (IOException e) {
				logger.error("Error con JSON");
				e.printStackTrace();
				return ERROR;
			}
			session.put("campaign", campaign);
		} else {
			campaign = (TCampaign) session.get("campaign");
			if (campaign == null) {
				logger.error("No la campaña");
				return ERROR;
			}
		}
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		publications = publicationService.getTPublicationAdminCampaignId(campaign.getCampaignId(), user.getCatProfile());
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	@Action(value = "showPublicationAsTHAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"publication", "excludeNullProperties", "true", "noCache", "true" }) )
	public String showPublicationAsTHAction() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		String publicationJSON = request.getParameter("publication");
		ViewPublicationBean pub;
		if (!publicationJSON.equals("undefined")) {
			pub = new ViewPublicationBean();
			try {
				pub = new ObjectMapper().readValue(publicationJSON, ViewPublicationBean.class);
			} catch (IOException e) {
				logger.error("Error con JSON");
				e.printStackTrace();
				return ERROR;
			}
			session.put("publication", pub);
		} else {
			logger.error("Error con JSON");
			return ERROR;
		}
		this.publication = viewPublicationService.showPublicationByCardNumber(pub.getAcoundNumber(), pub.getPublicationId(), 1);
		if(this.publication.getListFiles()!=null)
			if(this.publication.getListFiles().size() == 0)
				this.publication.setListFiles(null);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "showPublicationAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"publication", "excludeNullProperties", "true", "noCache", "true" }) )
	public String showPublicationAction() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		String publicationJSON = request.getParameter("publication");
		TPublication pub;
		if (!publicationJSON.equals("undefined")) {
			pub = new TPublication();
			try {
				pub = new ObjectMapper().readValue(publicationJSON, TPublication.class);
			} catch (IOException e) {
				logger.error("Error con JSON");
				e.printStackTrace();
				return ERROR;
			}
			session.put("publication", pub);
		} else {
			pub = (TPublication) session.get("publication");
			if (pub == null) {
				logger.error("No existe publicación");
				return ERROR;
			}
		}
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		this.publication = viewPublicationService.showPublication(user.getUserId(), pub.getPublicationId(), user.getCatProfile());
		if(publication.getListFiles()!=null)
			if(publication.getListFiles().size() == 0)
				publication.setListFiles(null);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "deleteCampaignAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"message", "excludeNullProperties", "true", "noCache", "true" }) )
	public String deleteCampaignAction() {
		campaignService.deleteCampaign(campaignId);
		setMessage(SUCCESS_CODE, SUCCESS_DELETE_CAMPAIGN);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "getClassificationLevelAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"classificationLevel", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getClassificationLevelAction() {
		
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		if(classificationId < 0)
			classificationLevel = classificationService.getListClassificationParent(user.getUserId());
		else{
			classificationLevel = classificationService.getListClassificationChildren(classificationId);
		}
		
		// se agrega esta linea para obtener los subprogramas y unidad de negocio default, esto para el caso que no se requiera
		// seleccionarlos del combobox, y se jale los default en un catalogo precargado.
		logger.info("classificationLevel : "+classificationLevel.toString());
		
		if(classificationLevel != null && !classificationLevel.isEmpty()){
			SelectClassificationCampaignBean subprogram = classificationLevel.get(0);
			Integer subProgramId = subprogram.getId();
			if(subProgramId != null){
				List<SelectClassificationCampaignBean> unitBList = classificationService.getListClassificationChildren(subProgramId);
				if(unitBList != null && !unitBList.isEmpty()){
					SelectClassificationCampaignBean unitB = unitBList.get(0);
					Integer unitBId = unitB.getId();
					logger.info("subProgramId : " + subProgramId + "  unitCId : " + unitBId);
					session.put("unitBId", unitBId.toString());
				}
			}
		}
		
		logger.info("Session : "+session.toString());
		
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "getAllCompaniesAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"listCompany", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getAllCompaniesAction() {
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		listCompany = companyServiceImpl.getAllCompanies();
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "getPublicationTypesAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"selectPublicationTypes", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getPublicationTypesAction() {
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		List<CatPublicationType> publicationTypes = publicationService.getPublicationType();
		if(publicationTypes!=null){
			selectPublicationTypes = new ArrayList<SelectClassificationCampaignBean>();	
			for(CatPublicationType pType : publicationTypes){
				SelectClassificationCampaignBean select = new SelectClassificationCampaignBean();
				select.setId(pType.getPublicationTypeId());
				select.setName(pType.getName());
				selectPublicationTypes.add(select);
			}
			return SUCCESS;
		}
		return ERROR;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "addCampaignAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"message", "excludeNullProperties", "true", "noCache", "true" }) )
	public String addCampaignAction() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		String classificationCmpJSON = request.getParameter("formNewCampaign");
		FormNewCampaignBean formNewCampaign;
		if (!classificationCmpJSON.equals("undefined")) {
			formNewCampaign = new FormNewCampaignBean();
			try {
				formNewCampaign = new ObjectMapper().readValue(classificationCmpJSON,
						FormNewCampaignBean.class);
			} catch (IOException e) {
				e.printStackTrace();
				setMessage(this.ERROR_CODE, this.ERROR_CREATE_CAMPAIGN);
				return ERROR;
			}
		} else {
			setMessage(this.ERROR_CODE, this.ERROR_CREATE_CAMPAIGN);
			return ERROR;
		}
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			setMessage(this.ERROR_CODE, this.ERROR_CREATE_CAMPAIGN);
			return ERROR;
		}
		formNewCampaign.setCompanyId(user.getCompany());
		String idTCampaign = campaignService.insertCampaign(formNewCampaign);
		saveFile(formNewCampaign.getImageFile(), formNewCampaign.getNameFile(), idTCampaign);
		setMessage(this.SUCCESS_CODE, this.SUCCESS_CRATE_CAMPAIGN);
		return SUCCESS;
	}
	
	private void saveFile(String fileString, String fileName, String idTCampaign){
		String pathName = PathConstants.ATTACHED_IMAGE_CAMPAIGN + idTCampaign + File.separator + fileName; 
		byte[] fileBits = stringToBytes(fileString);
		File file = new File(pathName);
		try {
			FileUtils.writeByteArrayToFile(file, fileBits);
		} catch (IOException e) {
			logger.error("NO SE PUDO GUARDAR LA IMAGEN DE LA CAMPAÑA");
			e.printStackTrace();
		}
	}
	
	private byte[] stringToBytes(String string) {
		String [] bytes = string.split(",");
		final byte[] fileBytes = new byte[bytes.length];		
		int i=0;
		for (String byteStr : bytes) {
			fileBytes[i] = (byte) Integer.parseInt(byteStr);
			i++;
		}
		return fileBytes;		
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "updateCampaignAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"message", "excludeNullProperties", "true", "noCache", "true" }) )
	public String updateCampaignAction() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		String classificationCmpJSON = request.getParameter("formNewCampaign");
		FormNewCampaignBean formNewCampaign;
		if (!classificationCmpJSON.equals("undefined")) {
			formNewCampaign = new FormNewCampaignBean();
			try {
				formNewCampaign = new ObjectMapper().readValue(classificationCmpJSON,
						FormNewCampaignBean.class);
			} catch (IOException e) {
				e.printStackTrace();
				setMessage(ERROR_CODE, ERROR_UPDATE_CAMPAIGN);
				return ERROR;
			}
		} else {
			setMessage(ERROR_CODE, ERROR_UPDATE_CAMPAIGN);
			return ERROR;
		}
		TUser user = (TUser) session.get("user");
		if (user == null) {
			logger.error("No existe una sesión");
			setMessage(ERROR_CODE, ERROR_UPDATE_CAMPAIGN);
			return ERROR;
		}
		formNewCampaign.setCompanyId(user.getCompany());
		for(SelectClassificationCampaignBean classif : formNewCampaign.getClassificationList()){
			campaignService.updateCampaign(formNewCampaign);
		}
		setMessage(SUCCESS_CODE, SUCCESS_UPDATE_CAMPAIGN);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "getCampaignUpdateAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"selectCampaign", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getCampaignUpdateAction(){
		final HttpServletRequest request = ServletActionContext.getRequest();
		String classificationCmpJSON = request.getParameter("campaignDetail");
		CampaignDetailAdminBean campaignDetail;
		if (!classificationCmpJSON.equals("undefined")) {
			campaignDetail = new CampaignDetailAdminBean();
			try {
				campaignDetail = new ObjectMapper().readValue(classificationCmpJSON,
						CampaignDetailAdminBean.class);
			} catch (IOException e) {
				logger.error("Error con el JSON");
				e.printStackTrace();
				return ERROR;
			}
		} else {
			logger.error("Error con el JSON");
			return ERROR;			
		}
		TUser user = (TUser)session.get("user");
		if (user==null) {
			logger.error("No existe una sesión");
			return ERROR;
		}
		selectCampaign = new ArrayList<UpdateCampaignBean>();
		classificationLevel = classificationService.getListClassificationParent(user.getUserId());
		UpdateCampaignBean updateCampaignBean = new UpdateCampaignBean();
		updateCampaignBean.setAvailableOptions(classificationLevel);
		updateCampaignBean.setSelectedOption(this.getSelectedOption(classificationLevel, campaignDetail.getCatClassificationCampaign().get(0).getCatClassificationCampaignsId()));
		selectCampaign.add(updateCampaignBean);
		for(int i=0 ; i<campaignDetail.getCatClassificationCampaign().size() - 1 ; i++ ){
			Integer classId = campaignDetail.getCatClassificationCampaign().get(i).getCatClassificationCampaignsId();
			classificationLevel = classificationService.getListClassificationChildren(classId);
			updateCampaignBean = new UpdateCampaignBean();
			updateCampaignBean.setAvailableOptions(classificationLevel);
			updateCampaignBean.setSelectedOption(this.getSelectedOption(classificationLevel, campaignDetail.getCatClassificationCampaign().get(i+1).getCatClassificationCampaignsId()));
			selectCampaign.add(updateCampaignBean);
		}
		return SUCCESS;
	}
	
	public void setMessage(String code, String message){
		this.message = new HashMap <String, String>();
		this.message.put(CODE, code);
		this.message.put(MESSAGE, message);
	}
	
	public SelectClassificationCampaignBean getSelectedOption(List<SelectClassificationCampaignBean> listOption, Integer selectedId){	
		for(SelectClassificationCampaignBean option: listOption){
			if(option.getId().equals(selectedId))
				return option;
		}
		return null;
	}
	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public List<CatClassificationCampaign> getClassifications() {
		return classifications;
	}

	public List<CampaignDetailBean> getCampaigns() {
		return campaigns;
	}

	public List<TPublication> getPublications() {
		return publications;
	}

	public PublicationBean getPublication() {
		return publication;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public List<CampaignDetailBean> getSearchCampaigns() {
		return searchCampaigns;
	}

	public List<CampaignDetailAdminBean> getSearchCampaignsAdmin() {
		return searchCampaignsAdmin;
	}

	public List<CampaignDetailAdminBean> getCampaignsAdmin() {
		return campaignsAdmin;
	}
	
	public List<SelectClassificationCampaignBean> getClassificationLevel() {
		return classificationLevel;
	}

	public Integer getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(Integer classificationId) {
		this.classificationId = classificationId;
	}

	public List<UpdateCampaignBean> getSelectCampaign() {
		return selectCampaign;
	}

	public List<SelectClassificationCampaignBean> getSelectPublicationTypes() {
		return selectPublicationTypes;
	}

	public Map<String, String> getMessage() {
		return message;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public List<CatClient> getListCompany() {
		return listCompany;
	}

	public void setListCompany(List<CatClient> listCompany) {
		this.listCompany = listCompany;
	}
	
}
