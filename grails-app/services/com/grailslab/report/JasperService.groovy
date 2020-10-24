package com.grailslab.report

import com.grailslab.core.BaseService
import grails.gorm.transactions.Transactional
import groovy.sql.Sql
import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.*
import net.sf.jasperreports.engine.export.oasis.JROdsExporter
import net.sf.jasperreports.engine.export.oasis.JROdtExporter
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter
import net.sf.jasperreports.export.*
import org.springframework.core.io.Resource

import javax.sql.DataSource
import java.lang.reflect.Field
import java.sql.Connection

@Transactional(readOnly = true)
class JasperService extends BaseService{

    /**
     * Generate a report based on a single jasper file.
     * @param reportDef CusJasperReportDef
     * @return ByteArrayOutputStream
     */
    ByteArrayOutputStream generateReport(CusJasperReportDef reportDef) {
        reportDef.validate()
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream()
        Exporter exporter = generateExporter(reportDef)
        //exporter output
        exporter.setExporterOutput(getExporterOutput(reportDef.reportFormat, byteArray))
        //exporter input
        JasperPrint jasperPrint = generatePrinter(reportDef)
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint))
        //export
        exporter.exportReport()

        return byteArray
    }

    /**
     * Generate a report based on a single jasper file.
     * @param reportDefs List<CusJasperReportDef>
     * @return ByteArrayOutputStream
     */
    ByteArrayOutputStream generateReport(List<CusJasperReportDef> reportDefs) {
        reportDefs.each { it.validate() }
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream()
        Exporter exporter = generateExporter(reportDefs.first())
        //exporter output
        exporter.setExporterOutput(getExporterOutput(reportDefs.first().reportFormat, byteArray))
        //exporter input
        List<JasperPrint> printers = reportDefs.collect { reportDef -> generatePrinter(reportDef) }
        exporter.setExporterInput(SimpleExporterInput.getInstance(printers))
        //export
        exporter.exportReport()

        return byteArray
    }

    /**
     * Generate a exporter with for a CusJasperReportDef. Note that SUBREPORT_DIR an locale have default
     * values.
     * @param reportDef
     * @return Exporter
     */
    private static Exporter generateExporter(CusJasperReportDef reportDef) {
        if (reportDef.parameters.SUBREPORT_DIR == null) {
            reportDef.parameters.SUBREPORT_DIR = reportDef.reportDir
        }

        if (reportDef.locale) {
            reportDef.parameters.REPORT_LOCALE = reportDef.locale
        } else {
            reportDef.parameters.REPORT_LOCALE = Locale.getDefault()
        }

        Exporter exporter = getExporter(reportDef.reportFormat)

        if (reportDef.useDefaultConfiguration) {
            applyDefaultConfiguration(exporter, reportDef)
        }
        if (reportDef.reportConfiguration) {
            Field[] fields = getExporterFields(reportDef.reportFormat)
            applyCustomConfiguration(fields, exporter, reportDef.reportConfiguration)
        }
        return exporter
    }

    /**
     * Generate a JasperPrint object for a given report.
     * @param reportDefinition , the report
     * @param parameters , additional parameters
     * @return JasperPrint , jasperreport printer
     */
    private JasperPrint generatePrinter(CusJasperReportDef reportDef) {
        JasperPrint jasperPrint = null
        Resource resource = reportDef.getReport()
        JRDataSource jrDataSource = reportDef.dataSource

        if (jrDataSource == null && reportDef.reportData != null && !reportDef.reportData.isEmpty()) {
            jrDataSource = new JRBeanCollectionDataSource(reportDef.reportData)
        }

        if (jrDataSource != null) {
            if (resource.getFilename().endsWith('.jasper')) {
                jasperPrint = JasperFillManager.fillReport(resource.inputStream, reportDef.parameters, jrDataSource)
            } else {
                jasperPrint = JasperFillManager.fillReport(JasperCompileManager.compileReport(resource.inputStream), reportDef.parameters, jrDataSource)
            }
        } else {

            Sql sql = new Sql(dataSource as DataSource)
            Connection connection = dataSource?.getConnection()
            try {
                if (resource.getFilename().endsWith('.jasper')) {
                    jasperPrint = JasperFillManager.fillReport(resource.inputStream, reportDef.parameters, connection)
                } else {
                    jasperPrint = JasperFillManager.fillReport(JasperCompileManager.compileReport(resource.inputStream), reportDef.parameters, connection)
                }
            }
            finally {
                sql.close()
                connection.close()
            }
        }

        return jasperPrint
    }

    private static void applyCustomConfiguration(Field[] fields, Exporter exporter, Map configuration) {
        //TODO: based on format apply appropriate user defined configuration
        /*def fieldNames = fields.collect { it.getName() }

        configuration.each { p ->
            if (fieldNames.contains(p.getKey())) {
                def fld = Class.forName(fields.find { it.name = p.getKey() }.clazz.name).getField(p.getKey())
                exporter.setParameter(fld.get(fld.root.class), p.getValue())
            }
        }*/
    }

    private static void applyDefaultConfiguration(Exporter exporter, CusJasperReportDef format) {
        //TODO: based on format apply common configuration
    }

    /**
     * Return the suitable Exporter for a given file format.
     * @param format
     * @return exporter
     */
    private static Exporter getExporter(JasperExportFormat format) {
        switch (format) {
            case JasperExportFormat.PDF_FORMAT: return new JRPdfExporter()
            case JasperExportFormat.HTML_FORMAT: return new HtmlExporter()
            case JasperExportFormat.CSV_FORMAT: return new JRCsvExporter()
            case JasperExportFormat.XLS_FORMAT: return new JRXlsExporter()
            case JasperExportFormat.RTF_FORMAT: return new JRRtfExporter()
            case JasperExportFormat.ODT_FORMAT: return new JROdtExporter()
            case JasperExportFormat.ODS_FORMAT: return new JROdsExporter()
            case JasperExportFormat.DOCX_FORMAT: return new JRDocxExporter()
            case JasperExportFormat.XLSX_FORMAT: return new JRXlsxExporter()
            case JasperExportFormat.PPTX_FORMAT: return new JRPptxExporter()
            default: throw new Exception("Invalid format")
        }
    }

    /**
     * Return the suitable Exporter for a given file format.
     * @param format
     * @return exporter
     */
    private
    static ExporterOutput getExporterOutput(JasperExportFormat format, ByteArrayOutputStream byteArrayOutputStream) {
        switch (format) {
        //common cases
            case JasperExportFormat.PDF_FORMAT:
            case JasperExportFormat.XLS_FORMAT:
            case JasperExportFormat.XLSX_FORMAT:
            case JasperExportFormat.PPTX_FORMAT:
            case JasperExportFormat.DOCX_FORMAT:
            case JasperExportFormat.ODT_FORMAT:
            case JasperExportFormat.ODS_FORMAT: return new SimpleOutputStreamExporterOutput(byteArrayOutputStream)

        //specific cases
            case JasperExportFormat.HTML_FORMAT: return new SimpleHtmlExporterOutput(byteArrayOutputStream)
            case JasperExportFormat.CSV_FORMAT: return new SimpleWriterExporterOutput(byteArrayOutputStream)
            case JasperExportFormat.RTF_FORMAT: return new SimpleWriterExporterOutput(byteArrayOutputStream)
            default: throw new Exception("Invalid format")
        }
    }

    /**
     * Return the available Fields for a given
     * @param format
     * @return Field[] , null if no fields are available for the format
     */
    private static Field[] getExporterFields(JasperExportFormat format) {
        switch (format) {
            case JasperExportFormat.PDF_FORMAT: return PdfExporterConfiguration.getFields()
            case JasperExportFormat.HTML_FORMAT: return HtmlExporterConfiguration.getFields()
            case JasperExportFormat.CSV_FORMAT: return CsvExporterConfiguration.getFields()
            case JasperExportFormat.XLS_FORMAT: return XlsExporterConfiguration.getFields()
            case JasperExportFormat.XLSX_FORMAT: return XlsxExporterConfiguration.getFields()
            case JasperExportFormat.RTF_FORMAT: return RtfExporterConfiguration.getFields()
            case JasperExportFormat.DOCX_FORMAT: return DocxExporterConfiguration.getFields()
            default: return []
        }
    }
}
