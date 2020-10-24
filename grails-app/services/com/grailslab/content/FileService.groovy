package com.grailslab.content

import grails.gorm.transactions.Transactional

@Transactional
class FileService {
    static transactional = false

    public String getPathWithoutExtension(String path) {
        if (!path) {
            return ""
        }
        List<String> splitPath = path.tokenize('/')
        String lastPath = splitPath.last()
        int index = lastPath.lastIndexOf(".")
        if (index == 0) {
            return ""
        } else if (index > 0) {
            return lastPath.substring(0, index)
        }
        return path
    }
    /*public String getPathWithoutExtension(String path) {
        if (!path) {
            return ""
        }
        int index = path.lastIndexOf(".")
        if (index == 0) {
            return ""
        } else if (index > 0) {
            return path.substring(0, index)
        }
        return path
    }*/
    public String getExtensionFromPath(String path) {
        int index = path.lastIndexOf(".")
        if (index > -1) {
            return path.substring(index + 1)?.toLowerCase()
        }
        return null
    }
}
