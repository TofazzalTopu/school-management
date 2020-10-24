package com.grailslab.myapp.domain;

import org.hibernate.dialect.MySQLInnoDBDialect;

/**
 * Created by Aminul on 4/28/2017.
 */
public class MySQLUTF8InnoDBDialect extends MySQLInnoDBDialect {
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
