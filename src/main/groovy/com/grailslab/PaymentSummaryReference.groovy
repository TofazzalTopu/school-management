package com.grailslab

import com.grailslab.enums.FeeItemType

/**
 * Created by aminul on 3/16/2015.
 */
class PaymentSummaryReference {
    Long refId     //chartOfAccountId if Fee
    FeeItemType feeItemType
    String name     //feeName
    Double january
    Double february
    Double march
    Double april
    Double may
    Double june
    Double july
    Double august
    Double september
    Double october
    Double november
    Double december

}
