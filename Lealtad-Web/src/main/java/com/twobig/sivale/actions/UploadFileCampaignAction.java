package com.twobig.sivale.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.twobig.sivale.bd.to.TAttachedFile;
import com.twobig.sivale.bd.to.TCampaign;
import com.twobig.sivale.bd.to.TUser;
import com.twobig.sivale.bd.to.TUserDataC;
import com.twobig.sivale.beans.ExcelBean;

import com.twobig.sivale.beans.ExcelUserCampaignBean;
import com.twobig.sivale.constants.CommonsConstants;
import com.twobig.sivale.constants.PathConstants;
import com.twobig.sivale.dao.UserDAO;
import com.twobig.sivale.service.TAttachedFileService;
import com.twobig.sivale.service.TCampaignsService;
import com.twobig.sivale.service.TUserDataCService;
import com.twobig.sivale.service.impl.ExcelServiceImpl;
import com.twobig.sivale.utils.FilesUtil;

public class UploadFileCampaignAction extends ActionSupport implements SessionAware{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LogManager.getLogger(UploadFileCampaignAction.class);
	
	private Map<String, Object> session;
	
	private String nombreIncentivo;
	private int incentivo; 
	private String programa; 
	private String subprograma; 
	private int unidadDeNegocio;
	private String[] filesFileName;
	private File[] files;
	
	@Autowired
	public TCampaignsService campaignService;
	
	@Autowired 
	public TAttachedFileService tAttachedFileService;
	
	@Autowired
	public UserDAO userDAO; 
	
	@Autowired
	public TUserDataCService tUserDataCService;
	
	@Action(value = "uploadFileCampaingAction", results = { @Result(name=SUCCESS, location="/secured/home_admin.jsp"),
			@Result(name = ERROR, location = "/secured/home_admin.jsp")},
	        interceptorRefs={
			        @InterceptorRef(params={"maximumSize","104857600"}, value="fileUpload"),
			        @InterceptorRef("defaultStack"),
			        @InterceptorRef("validation")}
	)
	
	public String uploadFileCampaingAction() {

		TUser user = (TUser) session.get("user");

		logger.info("uploadFileCampaingAction. CARGA DE ARCHIVOS DE CAMPAÑA");

		if (user == null) {
			return ERROR;
		}

		if (files == null || files.length == 0) {
			return ERROR;
		}

		TCampaign tCampaign = new TCampaign();
		tCampaign.setCampaignName(this.nombreIncentivo);
		tCampaign.setClassificationId(this.unidadDeNegocio);
		tCampaign.setStartDate(new Date());
		tCampaign.setEndDate(new Date());
		tCampaign.setCampaignName(this.nombreIncentivo);
		
		tCampaign.setCompanyId(user.getCompany());
		
		tCampaign.setImagePath(filesFileName[0]);
		tCampaign.setXlsPath(filesFileName[1]);

		String campaignId = campaignService.insertCampaign(tCampaign);

		saveFileOnDiskFile(campaignId);

		if (files.length > 2) {
			saveFileOnDataBase(campaignId);
		}
		
		loadDataExcel(campaignId);

		return SUCCESS;
	}
	
	private void loadDataExcel(String campaignId){
		
		String directory = PathConstants.ATTACHED_IMAGE_CAMPAIGN + campaignId + File.separator;
		
		ExcelServiceImpl excelservice = new ExcelServiceImpl();
		ExcelBean excelBean = excelservice.getExcelData(directory+filesFileName[1]);
		List<ExcelUserCampaignBean> excelCampaign = excelservice.getListUserCampaign(excelBean,CommonsConstants.COLUMN_ID_EXCEL);
		
		List<String> listAccountNumber = new ArrayList<String>();
		for (ExcelUserCampaignBean excelUserCampaignBean : excelCampaign) {
			listAccountNumber.add(excelUserCampaignBean.getUserId());
		}
		
		List<TUser> listUser= userDAO.getListUserByAccountNumber(listAccountNumber);
		
		for (TUser tUser : listUser) {
			
			TUserDataC userDataC = new TUserDataC();
			userDataC.setCampaignId(Integer.parseInt(campaignId));
			userDataC.setUserId(tUser.getUserId());
			tUserDataCService.insertTUserData(userDataC);
			
		}
		
	}
	
	private void saveFileOnDiskFile(String idCampaign){
		
		String directory = PathConstants.ATTACHED_IMAGE_CAMPAIGN + idCampaign + File.separator;
		
		for (int i = 0; i < files.length; i++) {
			try {
				FilesUtil.saveFile(files[i], filesFileName[i], directory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void saveFileOnDataBase(String idCampaign){
		
		List<TAttachedFile> attachedFiles = new ArrayList<TAttachedFile>();
		
		for (int i = 2; i < files.length; i++) {
			TAttachedFile attachedFile = new TAttachedFile();
			attachedFile.settCampaignId(Integer.parseInt(idCampaign));
			attachedFile.setFileName(filesFileName[i]);
			attachedFiles.add(attachedFile);
		}
		
		tAttachedFileService.insertTAttachedFile(attachedFiles);
	}

	public String getNombreIncentivo() {
		return nombreIncentivo;
	}

	public void setNombreIncentivo(String nombreIncentivo) {
		this.nombreIncentivo = nombreIncentivo;
	}

	public int getIncentivo() {
		return incentivo;
	}

	public void setIncentivo(int incentivo) {
		this.incentivo = incentivo;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	public String getSubprograma() {
		return subprograma;
	}

	public void setSubprograma(String subprograma) {
		this.subprograma = subprograma;
	}
	
	public int getUnidadDeNegocio() {
		return unidadDeNegocio;
	}

	public void setUnidadDeNegocio(int unidadDeNegocio) {
		this.unidadDeNegocio = unidadDeNegocio;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public String[] getFilesFileName() {
		return filesFileName;
	}

	public void setFilesFileName(String[] filesFileName) {
		this.filesFileName = filesFileName;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

}
