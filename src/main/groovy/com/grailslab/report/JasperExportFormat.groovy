package com.grailslab.report

enum JasperExportFormat implements Serializable {
    PDF_FORMAT("application/pdf", "pdf", false),
    HTML_FORMAT("text/html", "html", true),
    XML_FORMAT("text/xml", "xml", false),
    CSV_FORMAT("text/csv", "csv", false),
    XLS_FORMAT("application/vnd.ms-excel", "xls", false),
    RTF_FORMAT("text/rtf", "rtf", false),
    TEXT_FORMAT("text/plain", "txt", true),
    ODT_FORMAT("application/vnd.oasis.opendocument.text", "odt", false),
    ODS_FORMAT("application/vnd.oasis.opendocument.spreadsheetl", "ods", false),
    DOCX_FORMAT("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx", false),
    XLSX_FORMAT("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx", false),
    PPTX_FORMAT("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx", false)

    String mimeType
    String extension
    boolean inline

    private JasperExportFormat(String mimeType, String extension, boolean inline) {
        this.mimeType = mimeType
        this.extension = extension
        this.inline = inline
    }

}