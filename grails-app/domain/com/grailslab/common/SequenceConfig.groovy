package com.grailslab.common

class SequenceConfig {
    long id
    String sequenceName
    Long sequenceNumber
    String sequenceScript
    Long tenantId

    static mapping = {
        version false
        sequenceScript type: 'text'
        table 'core_sequence_config'
    }

    static constraints = {
//        sequenceName unique: 'tenantId'
        tenantId nullable: true
        sequenceNumber nullable: true
    }
}
