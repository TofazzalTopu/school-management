package com.grailslab.content

import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.Image

class ImageController {
    def fileService
    def imageService
    def streamImageFromIdentifier() {
        String identifier = params.imagePath
        Image image = Image.findByIdentifier(identifier)
        if (image && image.activeStatus==ActiveStatus.ACTIVE) {
            try {
                return imageService.streamImageFromIdentifier(response, image)
            } catch (Exception e) {
                log.error("ERROR: ImageController.streamImageFromIdentifier "+e.getMessage())
            }
        }
        return response.sendError(404, "Image not found")
    }

    def streamImageFromIdentifierThumb() {
        String identifier = params.imagePath
        Image image = Image.findByIdentifier(identifier)
        if (image && image.activeStatus==ActiveStatus.ACTIVE) {
            try {
                return imageService.streamImageFromIdentifierThumb(response, image)
            } catch (Exception e) {
                log.error("ERROR: ImageController.streamImageFromIdentifierThumb "+e.getMessage())
            }
        }
        return response.sendError(404, "Image not found")
    }
}
