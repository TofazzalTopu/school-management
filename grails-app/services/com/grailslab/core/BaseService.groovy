package com.grailslab.core

import com.grailslab.CommonVariables
import com.grailslab.report.JasperExportFormat
import grails.gorm.transactions.Transactional

import javax.sql.DataSource

@Transactional
class BaseService implements CommonVariables {
    DataSource dataSource

    JasperExportFormat printFormat(String printFormat){
        if(!printFormat){
            printFormat = "PDF"
        }
        if(printFormat == "PDF"){
            return JasperExportFormat.PDF_FORMAT
        } else if(printFormat == "DOCX"){
            return JasperExportFormat.DOCX_FORMAT
        } else if(printFormat == "XLSX"){
            return JasperExportFormat.XLSX_FORMAT
        }
        return JasperExportFormat.PDF_FORMAT
    }
}
