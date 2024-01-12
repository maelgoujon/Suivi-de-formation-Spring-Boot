package com.webapp.ytb.webappytp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.webapp.ytb.webappytp.service.FormationServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FormationController {
    
    @Autowired
    private FormationServiceImpl formationService;

    @GetMapping("/excel")
    public void generateExcelReport(HttpServletResponse response) throws Exception {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=courses.xls";

		response.setHeader(headerKey, headerValue);
		
		formationService.generatedExcel(response);
		
		response.flushBuffer();
    }
}
