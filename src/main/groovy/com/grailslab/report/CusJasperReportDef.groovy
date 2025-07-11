package com.grailslab.report

import grails.validation.Validateable
import net.sf.jasperreports.engine.JRDataSource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource

import java.lang.annotation.Annotation

class CusJasperReportDef implements Serializable, Validateable {

//required information
    String reportName
    String reportDir

    //report parameters
    Map parameters = [:]

    //report format
    JasperExportFormat reportFormat = JasperExportFormat.PDF_FORMAT

    //data source
    Collection reportData
    JRDataSource dataSource

    //additional configuration
    Boolean useDefaultConfiguration
    Map reportConfiguration = [:]

    //local
    Locale locale

    //generated byte array
    byte[] content

    //output filename
    String outputFilename

    /**
     * get report as resource from report path
     * @return
     */
    Resource getReport() {
        String reportPath = reportDir + File.separator + reportName

        Resource result = new FileSystemResource(reportPath + ".jasper")
        if (result.exists()) {
            return result
        }

        result = new FileSystemResource(reportPath + ".jrxml")
        if (result.exists()) {
            return result
        }
        throw new FileNotFoundException("Report [${reportPath + ".jasper"}] or [${reportPath + ".jrxml"}] file not found")
    }

//    @Override
    boolean validate() {
        if (!reportName?.trim()) {
            throw new RuntimeException("Invalid report name provided: ${reportName}")
        }
        else if (!reportDir?.trim()) {
            throw new RuntimeException("Invalid report directory provided: ${reportDir}")
        }
        return true
    }
}
