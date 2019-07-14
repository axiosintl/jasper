package com.ai.demo.jasper.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ai.demo.jasper.repository.ProductService;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

@Controller
@RequestMapping("product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "product/index";
	}

	@RequestMapping(value = "report", method = RequestMethod.GET)
	public void report(HttpServletResponse response) throws Exception {
		response.setContentType("text/html");

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productService.report());
		InputStream inputStream = this.getClass().getResourceAsStream("/reports/sample.jrxml");

		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

//		JasperCompileManager.compileReportToFile("C:/axios/jasper/jasper/jasper/src/main/resources/reports/sample.jrxml", 
//				"C:/axios/jasper/jasper/jasper/src/main/resources/reports/sample.japser");
		
		Map parameters = new HashMap();
		String printFileName = JasperFillManager.fillReportToFile("C:/axios/jasper/jasper/jasper/src/main/resources/reports/sample.jasper", 
				parameters, dataSource);
		JasperExportManager.exportReportToPdfFile(printFileName, "C:/axios/junk/sample.pdf");
		System.out.println("**** PDF export complete");

		HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
		exporter.exportReport();
		System.out.println("**** HTML export complete");
	}

}
