package com.twobig.sivale.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;


import com.opensymphony.xwork2.ActionSupport;
import com.twobig.sivale.bd.to.TCampaign;
import com.twobig.sivale.bd.to.TPublication;
import com.twobig.sivale.bd.to.TUser;
import com.twobig.sivale.constants.PathConstants;

@ParentPackage(value = "json-default")
@Namespace("/")
public class DownloadFileAction extends ActionSupport implements SessionAware {

	private Map<String, Object> session;
	private InputStream fileInputStream;
	private String fileName;
	private String campaignId;
	private byte[] file;
	Map<String, Object> reportMap;
	private static final Logger logger = LogManager.getLogger(DownloadFileAction.class);

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	@Action(value = "getFileAction", results = @Result(name = SUCCESS, type = "json", params = { "root", "file",
			"excludeNullProperties", "true", "noCache", "true" }) )
	public String getFileAction() {
		TCampaign campaign = (TCampaign) session.get("campaign");
		TPublication publication = (TPublication) session.get("publication");
		if (campaign == null || publication == null){
			logger.error("No existe una campaña o publicación");
			return ERROR;
		}
		String path = String.valueOf(campaign.getCampaignId()) + "/" + String.valueOf(publication.getPublicationId()) + "/"
				+ fileName;
		try {
			fileInputStream = new FileInputStream(new File(PathConstants.ATTACHED_DIRECTORY + path));
			file = IOUtils.toByteArray(fileInputStream);
		} catch (FileNotFoundException e) {
			logger.error("El archivo no existe");
			e.printStackTrace();
			return ERROR;
		} catch (IOException e) {
			logger.error("Error con el archivo");
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value = "getFileCampaignAction", results = @Result(name = SUCCESS, type = "json", params = { "root", "file",
			"excludeNullProperties", "true", "noCache", "true" }) )
	public String getFileCampaignAction() {
		String path = String.valueOf(campaignId + "/" + fileName);
		try {
			fileInputStream = new FileInputStream(new File(PathConstants.ATTACHED_IMAGE_CAMPAIGN + path));
			file = IOUtils.toByteArray(fileInputStream);
		} catch (FileNotFoundException e) {
			logger.error("El archivo no existe");
			e.printStackTrace();
			return ERROR;
		} catch (IOException e) {
			logger.error("Erro con el archivo");
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	@Action(value = "getStaticFilesHomeTH", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"reportMap", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getStaticFilesHomeTH() {		
		TUser user;
		user = (TUser) session.get("user");
		if(user == null){
			return ERROR; 
		}
		reportMap = new HashMap<>();
		String fileName = "Solicitud de Tarjetas";
		try {
			Path temp = Files.createTempFile("solicitud_de_tarjetas", ".xlsm");
			Files.copy(getClass().getClassLoader().getResourceAsStream("solicitud_de_tarjetas.xlsm"), temp, StandardCopyOption.REPLACE_EXISTING);
			fileInputStream = new FileInputStream(temp.toFile());
			file = IOUtils.toByteArray(fileInputStream);
		} catch (FileNotFoundException e) {
			logger.error("Error al leer el archivo");
			e.printStackTrace();
			return ERROR;
		} catch (IOException e) {
			logger.error("Error con el archivo");
			e.printStackTrace();
			return ERROR;
		}
		reportMap.put("valueCode", file);
		reportMap.put("resultCode", "100");
		reportMap.put("fileName", fileName);
		return SUCCESS;
	}
	
	@Action(value = "getFormatoAcuseFCMAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"reportMap", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getFormatoAcuseFCMAction() {	
		TUser user;
		user = (TUser) session.get("user");
		if(user == null){
			logger.error("No existe una sesión");
			return ERROR; 
		}
		reportMap = new HashMap<>();
		String fileName = "Formato_Acuse_FCM_2016";
		try {
			Path temp = Files.createTempFile("Formato_Acuse_FCM_2016", "pdf");
			Files.copy(getClass().getClassLoader().getResourceAsStream("Formato_Acuse_FCM_2016.pdf"), temp, StandardCopyOption.REPLACE_EXISTING);
			fileInputStream = new FileInputStream(temp.toFile());
			file = IOUtils.toByteArray(fileInputStream);
		} catch (FileNotFoundException e) {
			logger.error("No se puede leer el archivo");
			e.printStackTrace();
			return ERROR;
		} catch (IOException e) {
			logger.error("Error con el archivo");
			e.printStackTrace();
			return ERROR;
		}
		reportMap.put("valueCode", file);
		reportMap.put("resultCode", "100");
		reportMap.put("fileName", fileName);
		return SUCCESS;
	}
	
	@Action(value = "getFormatoAcuse2016FORDAction", results = @Result(name = SUCCESS, type = "json", params = { "root",
			"reportMap", "excludeNullProperties", "true", "noCache", "true" }) )
	public String getFormatoAcuse2016FORDAction() {	
		TUser user;
		user = (TUser) session.get("user");
		if(user == null){
			logger.error("No existe una sesión");
			return ERROR; 
		}
		reportMap = new HashMap<>();
		String fileName = "Formato_Acuse_2016_FORD";
		try {
			Path temp = Files.createTempFile("Formato_Acuse_2016_FORD", ".pdf");
			Files.copy(getClass().getClassLoader().getResourceAsStream("Formato_Acuse_2016_FORD.pdf"), temp, StandardCopyOption.REPLACE_EXISTING);
			fileInputStream = new FileInputStream(temp.toFile());
			file = IOUtils.toByteArray(fileInputStream);
		} catch (FileNotFoundException e) {
			logger.error("No existe una sesión");
			e.printStackTrace();
			return ERROR;
		} catch (IOException e) {
			logger.error("No existe una sesión");
			e.printStackTrace();
			return ERROR;
		}
		reportMap.put("valueCode", file);
		reportMap.put("resultCode", "100");
		reportMap.put("fileName", fileName);
		return SUCCESS;
	}
	
	public Map<String, Object> getReportMap() {
		return reportMap;
	}

	public void setReportMap(Map<String, Object> reportMap) {
		this.reportMap = reportMap;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
}
