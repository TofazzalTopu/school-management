package com.grailslab.enums

/**
 * Created by Hasnat on 9/11/2014.
 */
public enum SubjectType {

    COMPULSORY("Compulsory"),
    OPTIONAL("Optional"),
    ALTERNATIVE("Alternative"), //Subject group actually ie: religion
    INUSE("Use Subject") // Islam, Hindu

    final String value
    SubjectType(String value) {
        this.value = value
    }

    static Collection<SubjectType> nonOptional(){
        return [COMPULSORY, ALTERNATIVE]
    }

    static Collection<SubjectType> withOptional(){
        return [COMPULSORY, OPTIONAL, ALTERNATIVE]
    }

    static Collection<SubjectType> classUse(){
        return [COMPULSORY, OPTIONAL, INUSE]
    }
    static Collection<SubjectType> chooseOptCom(){
        return [OPTIONAL, INUSE]
    }

    String toString() { value }
    String getKey() { name() }
}