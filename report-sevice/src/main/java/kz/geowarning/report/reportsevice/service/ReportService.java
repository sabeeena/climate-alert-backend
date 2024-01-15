package kz.geowarning.report.reportsevice.service;


import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ReportService {

    @Autowired
    @Lazy
    private ResourceLoader resourceLoader;

    @Value("classpath:reports/jrxml/img.png")
    private String coatOfArms;

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.login}")
    private String databaseLogin;

    @Value("${database.password}")
    private String databasePassword;

    @Value("classpath:reports/jrxml/fire/realTime/fire_real_time_overall_report.jrxml")
    private String fireRealTimeOverall;

    @Value("classpath:reports/jrxml/fire/realTime/fire_real_time_economic_damage.jrxml")
    private String fireRealTimeEconomicDamage;



    public byte[] exportReportFireRealTimeOverall(Long reportId, String lang) throws IOException, JRException, SQLException {
        JasperReport compileReport = null;
        if (lang.equals("ru")) {
            compileReport = JasperCompileManager.compileReport(resourceLoader.getResource(fireRealTimeOverall).getInputStream());
        }
//        else {
//            compileReport = JasperCompileManager.compileReport(resourceLoader.getResource(DENIAL_ACT_COUNTING_COMMITTEE_KZ).getInputStream());
//        }
        InputStream imgInputStream = resourceLoader.getResource(coatOfArms).getInputStream();
        Map<String, Object> parameters = new HashMap<>();
        Connection conn = DriverManager.getConnection(databaseUrl, databaseLogin, databasePassword);
        parameters.put("reportId", reportId);
        parameters.put("logo", imgInputStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, conn);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] exportReportFireRealTimeEDamage(Long reportId, String lang) throws IOException, JRException, SQLException {
        JasperReport compileReport = null;
        if (lang.equals("ru")) {
            compileReport = JasperCompileManager.compileReport(resourceLoader.getResource(fireRealTimeEconomicDamage).getInputStream());
        }
//        else {
//            compileReport = JasperCompileManager.compileReport(resourceLoader.getResource(DENIAL_ACT_COUNTING_COMMITTEE_KZ).getInputStream());
//        }
        InputStream imgInputStream = resourceLoader.getResource(coatOfArms).getInputStream();
        Map<String, Object> parameters = new HashMap<>();
        Connection conn = DriverManager.getConnection(databaseUrl, databaseLogin, databasePassword);
        parameters.put("reportId", reportId);
        parameters.put("logo", imgInputStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, conn);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
