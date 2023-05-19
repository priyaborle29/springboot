package com.rest.api.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.rest.api.entity.User;
import com.rest.api.payload.UserVO;
import com.rest.api.repository.UserRepository;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

@Service
public class UserReport {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	public String exportReport(String reportFormat) throws FileNotFoundException, JRException, SQLException, ClassNotFoundException
	{
		final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
		
		String path = "D:\\DPAX\\Jasper\\";

		try {

			UserVO userVO =new UserVO();

			List<User> user = userRepository.findAll();

			//loading file and compiling it
			File file = ResourceUtils.getFile("classpath:Users.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			//		User users = (User) userRepository.findAll();

			ArrayList<Object> userList = new ArrayList<>();

			userList.add(user);

			JRBeanCollectionDataSource dataSource =new JRBeanCollectionDataSource(userList);

			//		dataSource = (JRBeanCollectionDataSource) userRepository.findAll();

//			Connection conn;
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restapi?useSSL=false&serverTimezone=UTC","root","nitor@abhi");


			Map<String, Object> parameter = new HashMap<>();
			parameter.put("createdBy", "abhi@nitor");

			LOGGER.info("Jasper report generation",Thread.currentThread().getStackTrace()[1].getMethodName());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,dataSource);

			if(reportFormat.equalsIgnoreCase("html"))
			{
				JasperExportManager.exportReportToHtmlFile(jasperPrint,path +"UserReport.html");
			}
			if(reportFormat.equalsIgnoreCase("pdf"))
			{
				JasperExportManager.exportReportToPdfFile(jasperPrint,path +"UserReport.pdf");
			}


		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "Report generated at path : " + path;
	}
	
	
	//trying new method
	public String report(HttpServletResponse response) throws Exception {
		
	    response.setContentType("text/html");
	    List data = userRepository.findAll();
	    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
	    Map parameters = new HashMap();
	    parameters.put("test", data.get(0));
	    parameters.put("DS1", dataSource);
	    InputStream inputStream = this.getClass().getResourceAsStream("Users.jrxml");
	    JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
	    HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
	    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	    exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
	    exporter.exportReport();
	    
		return "Report generated";
	}
	
	
	

}
