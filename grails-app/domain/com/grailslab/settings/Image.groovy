package com.grailslab.settings


import com.grailslab.exceptions.ImageException
import com.grailslab.gschoolcore.ActiveStatus

class Image {
    String imagePath
    String imagePathThumb
    String imagePathMedium
    String imageExtension
    ActiveStatus activeStatus = ActiveStatus.ACTIVE
    String identifier = UUID.randomUUID().toString().replace("-", "")[0..12]
    static constraints = {
        identifier(unique: true)
        activeStatus nullable: true
        imagePath(nullable: true, blank: true, size: 0..255)
        imagePathThumb(nullable: true, blank: true, size: 0..255)
        imagePathMedium(nullable: true, blank: true, size: 0..255)
    }
    public String createImagePath(String folderName, String imageExtension) {
        String ext = imageExtension ? imageExtension : this.imageExtension
        if (ext) {
            return "/" + folderName + '/' + identifier + '.' + ext
        } else {
            throw new ImageException("Set imageExtension first");
        }
    }

    public String createImagePathThumb(String folderName, String imageExtension) {
        String ext = imageExtension ? imageExtension : this.imageExtension
        if (ext) {
            return "/" + folderName + '/' +'thumb_'+ identifier + '.' + ext
        } else {
            throw new ImageException("Set imageExtension first");
        }
    }
    public String createImagePathMedium(String folderName, String imageExtension) {
        String ext = imageExtension ? imageExtension : this.imageExtension
        if (ext) {
            return "/" + folderName + '/' +'medium_'+ identifier + '.' + ext
        } else {
            throw new ImageException("Set imageExtension first");
        }
    }

    public String getContentType() {
        String contentType = ""
        switch (imageExtension?.toLowerCase()) {
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
            default:
                log.error "Unsupported image type. Image: ${imagePath}. ImageId: ${id}"
        }

        return contentType
    }

}
