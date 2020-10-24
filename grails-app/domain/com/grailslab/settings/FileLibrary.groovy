package com.grailslab.settings

import com.grailslab.exceptions.FileUploadException
import com.grailslab.gschoolcore.ActiveStatus

class FileLibrary {
    String filePath
    String fileExtension
    ActiveStatus activeStatus = ActiveStatus.ACTIVE
    String identifier = UUID.randomUUID().toString().replace("-", "")[0..12]
    static constraints = {
        identifier(unique: true)
        activeStatus nullable: true
        filePath(nullable: true, blank: true, size: 0..255)
    }

    public String createPath(String folderName, String fileExtension) {
        String ext = fileExtension ? fileExtension : this.fileExtension
        if (ext) {
            return "/" + folderName + '/' + identifier + '.' + ext
        } else {
            throw new FileUploadException("Set fileExtension first");
        }
    }

    public String getContentType() {
        String contentType = ""
        switch (fileExtension?.toLowerCase()) {
            case "jpg":
            case "jpeg":
                contentType = "image/jpeg"
                break
            case "gif":
                contentType = "image/gif"
                break
            case "png":
                contentType = "image/x-png"
                break
            case "pdf":
                contentType = "file/pdf"
                break
            default:
                log.error "Unsupported image type. Image: ${imagePath}. ImageId: ${id}"
        }

        return contentType
    }
}
