package com.grailslab.student

import com.grailslab.accounting.FeePayments
import com.grailslab.accounting.Invoice
import com.grailslab.gschoolcore.ActiveStatus
import grails.gorm.transactions.Transactional
import org.grails.web.json.JSONException
import org.grails.web.json.JSONObject

import java.nio.charset.Charset

@Transactional
class PortalService {
      def springSecurityService
    private  String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public  JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {

        }
    }

    def getTransactionDetails(String transactionId) {
        def bKashNumber = '01993309253'
        def bkashUserName = 'BAILYSCHOOL'
        def bkashPassword = 'b@llys00ol'
        JSONObject json = null;
        String url = "https://www.bkashcluster.com:9081/dreamwave/merchant/trxcheck/sendmsg?" +
                "msisdn=" + bKashNumber+ "&trxid=" + transactionId + "&user=" + bkashUserName + "&pass=" + bkashPassword;
        json = readJsonFromUrl(url);
        json= json.getJSONObject("transaction");
        json
    }

    def deleteInvoice(def invoiceId) {
        String createOrUpdateBy = springSecurityService?.principal?.username
        Invoice invoice = Invoice.findById(invoiceId)
        def feePaymentsList = FeePayments.findAllByInvoice(invoice)
        feePaymentsList.each { FeePayments feePayments ->
            feePayments.activeStatus = ActiveStatus.DELETE
            feePayments.updatedBy = createOrUpdateBy
            feePayments.save(flush: true)
        }
        invoice.activeStatus = ActiveStatus.DELETE
        invoice.updatedBy = createOrUpdateBy
        invoice.save(flush: true)
    }
}
