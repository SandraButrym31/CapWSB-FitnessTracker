package com.capgemini.wsb.fitnesstracker.mail.internal;

import com.capgemini.wsb.fitnesstracker.notification.internal.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class EmailController {

  private final ReportService reportService;

  @Autowired
  public EmailController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping("/generate-and-send")
  public void generateAndSendReports() {
    reportService.generateReport();
  }
}

