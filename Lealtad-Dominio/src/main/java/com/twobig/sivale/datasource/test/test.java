package com.twobig.sivale.datasource.test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.twobig.sivale.bd.to.CatClassificationCampaign;
import com.twobig.sivale.bd.to.TAttachedFile;
import com.twobig.sivale.bd.to.TPublication;
import com.twobig.sivale.beans.CampaignDetailBean;
import com.twobig.sivale.beans.PublicationBean;
import com.twobig.sivale.beans.SearchCampaignBean;
import com.twobig.sivale.beans.TUserLogin;
import com.twobig.sivale.exceptions.TravelsNotFoundException;
import com.twobig.sivale.hd.to.UserBean;
import com.twobig.sivale.service.CatClassificationCampaignService;
import com.twobig.sivale.service.FilterCampaignService;
import com.twobig.sivale.service.LoginService;
import com.twobig.sivale.service.TCampaignsService;
import com.twobig.sivale.service.TPublicationService;
import com.twobig.sivale.service.TransactionService;
import com.twobig.sivale.service.ViewPublicationService;

import ws.sivale.com.mx.messages.types.TypeTransaccion;


public class test {
	
	
	public static void login(ClassPathXmlApplicationContext context){
		
		LoginService loginService = (LoginService) context.getBean("loginServiceImpl");
		
		String user   = "admin_1";
		String passwd = "adm-1111";

		
		TUserLogin tUserLogin;

		UserBean userBean = new UserBean();
		userBean.setUser(user);
		userBean.setPass(passwd);
		Map<String, Object> userDetails =  new HashMap<String, Object>();
    	
		if ((tUserLogin = loginService.validateUserWeb(userBean)) != null) {			 						
						
			userDetails.put("email", 			tUserLogin.getEmail());
			userDetails.put("functionalities", 	tUserLogin.getFunctionalities());
			userDetails.put("userId", 			tUserLogin.getUserId());
			userDetails.put("rfcClient", 		tUserLogin.gettCompanies().getDescription());
			userDetails.put("clientName", 		tUserLogin.gettCompanies().getName());
			userDetails.put("userName", 		tUserLogin.getFirstName()+" "+tUserLogin.getLastName1()+" "+tUserLogin.getLastName2());
			userDetails.put("cardNumber", 		tUserLogin.getTjCardNumber());
			userDetails.put("clientId", 		tUserLogin.gettCompanies().getIdCompany());	
			System.out.println(userDetails.toString());
			
		} else {
			System.out.println("Usuario o password incorrectos!");
		}
		
	}
	
	public static void loginWebService(ClassPathXmlApplicationContext context){
		
		LoginService loginService = (LoginService) context.getBean("loginServiceImpl");
		
		String user   = "5273740100060052";
		//user   = "5452900200030677";
		//user   = "5349261200148580";


		String passwd = "inicial1";

		
		TUserLogin tUserLogin;

		UserBean userBean = new UserBean();
		userBean.setUser(user);
		userBean.setPass(passwd);
		Map<String, Object> userDetails =  new HashMap<String, Object>();
    	
		if ((tUserLogin = loginService.validateUserWeb(userBean)) != null) {			 						
						
			userDetails.put("email", 			tUserLogin.getEmail());
			userDetails.put("functionalities", 	tUserLogin.getFunctionalities());
			userDetails.put("userId", 			tUserLogin.getUserId());
			userDetails.put("rfcClient", 		tUserLogin.gettCompanies().getDescription());
			userDetails.put("clientName", 		tUserLogin.gettCompanies().getName());
			userDetails.put("userName", 		tUserLogin.getFirstName()+" "+tUserLogin.getLastName1()+" "+tUserLogin.getLastName2());
			userDetails.put("cardNumber", 		tUserLogin.getTjCardNumber());
			userDetails.put("clientId", 		tUserLogin.gettCompanies().getIdCompany());	
			System.out.println(userDetails.toString());
			
		} else {
			System.out.println("Usuario o password incorrectos!");
		}
	}
	
	public static void clasificaciones(ClassPathXmlApplicationContext context){
		
		CatClassificationCampaignService cccs = 
				(CatClassificationCampaignService) context.getBean("catClassificationCampaignServiceImpl");
		
		List<CatClassificationCampaign> clasificaciones = cccs.getCatClassificationCampaignByUserId(7);
		
		for (CatClassificationCampaign catClassificationCampaign : clasificaciones) {
			System.out.println(catClassificationCampaign.toString());
			System.out.println(catClassificationCampaign.getCatViews().getLogos());
		}
		
	}
	
	public static void campañas(ClassPathXmlApplicationContext context){
		
		TCampaignsService cccs = (TCampaignsService) context.getBean("TCampaignsServiceImpl");
		List<CampaignDetailBean> campaignDetailBeanList =  
				cccs.getCampaignByUserIdAndClassificationCampaignsId(7, 1);
		
		for (CampaignDetailBean campaignDetailBean2 : campaignDetailBeanList) {
			System.out.println(campaignDetailBean2.toString());
			System.out.println(campaignDetailBean2.getClassification());
		}
	}
	
	public static void publicaciones(ClassPathXmlApplicationContext context){
		
		TPublicationService cccs = 
		(TPublicationService) context.getBean("TPublicationServiceImpl");
		
		List<TPublication> publicaciones = cccs.getTPublicationCampaignId(1);
		
		for (TPublication tPublication : publicaciones) {
			System.out.println(tPublication.toString() + " ");
		}
	}
	
	public static void mostrarPublicaciones(ClassPathXmlApplicationContext context){
		
		ViewPublicationService cccs = 
				(ViewPublicationService) context.getBean("viewPublicationServiceImpl");
		
		PublicationBean publicationBean = cccs.showPublication(7, 1);
		
		if(publicationBean != null){
			
			for (TAttachedFile files : publicationBean.getListFiles()) {
				System.out.println(files.toString());
			}
			
			System.out.println(publicationBean.getHtml());
		}
	}
			
	public static void filterCampaigns(ClassPathXmlApplicationContext context) {


		FilterCampaignService cccs = (FilterCampaignService) context.getBean("filterCampaignServiceImpl");

		SearchCampaignBean searchCampaignBean = new SearchCampaignBean();

		String pattern = "yyyy-MM-dd";
		

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			Date startDate = format.parse("2015-12-01");
			Date endDate = format.parse("2016-01-01");
			System.out.println(endDate);
			
//			Date startDate = format.parse("2015-09-01 00:00:00");
//			Date endDate = format.parse("2015-10-01 00:00:00");
			
//			Date startDate = format.parse("2015-09-01 00:00:00");
//			Date endDate = format.parse("2015-10-01 00:00:00");

			searchCampaignBean.setClassificationParentId(1);
			searchCampaignBean.setCampaignName("camp");
			searchCampaignBean.setStartDate(startDate);
			searchCampaignBean.setEndDate(endDate);
			System.out.println(searchCampaignBean.toString());
			searchCampaignBean.setClassificationName1("");
			searchCampaignBean.setClassificationName2(" ");
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<CampaignDetailBean> listCampaignDetailBean =  cccs.FilterCampaign(7, searchCampaignBean);
		
		for (CampaignDetailBean campaignDetailBean : listCampaignDetailBean) {
			System.out.println(campaignDetailBean);
		}

	}
	
	public static void movimientos(ClassPathXmlApplicationContext context){
		
		TransactionService cccs = (TransactionService) context.getBean("transactionServiceImpl");
		
		List<TypeTransaccion> transactionsList = cccs.getLastTransactionByCard("5273740100060052");
		
		for (TypeTransaccion typeTransaccion : transactionsList) {
			 System.out.print(typeTransaccion.getNumberCard());
			 System.out.println(" "+typeTransaccion.getTransactionDate());
			 
		}
		
		try {
			
			Double saldo = cccs.getBalance("5273740100060052");
			System.out.println("Saldo: "+saldo);
		} catch (TravelsNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		//BasicConfigurator.configure();
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
			     "classpath:/application-context.xml");
		
		
//		//SERVICIO DE MOVIMIENTOS Y DE SALDO
//		movimientos(context);
//		
//		//SERVICIO DE LOGIN PARA EL PARTICIPANTE
//		loginWebService(context);
//		
//		
//		//SERVICIO DE LOGIN PARA ADMINISTRADOR
//		login(context);
//
//		
//		//SERVICIO DE CLASIFICACIONES 
//		clasificaciones(context);
//		
//				
//		//SERVICIO DE CAMPAÑAS
//		campañas(context);
//		
//		
//		//SERVICIO DE PUBLICACIONES
//		publicaciones(context);
//		
//		
//		//SERVICIO MOSTRAR PUBLICACIONES
//		mostrarPublicaciones(context);
//		
//		
//		//SERVICIO DE FILTRARCAMPAÑAS
//		filterCampaigns(context);
		
		
	}
}
