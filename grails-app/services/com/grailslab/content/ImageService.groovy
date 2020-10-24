package com.grailslab.content

import com.grailslab.exceptions.GrailsLabServiceException
import com.grailslab.settings.Image
import grails.gorm.transactions.Transactional
import grails.web.context.ServletContextHolder

import javax.imageio.IIOException
import javax.servlet.http.HttpServletResponse

@Transactional
class ImageService {
    def fileService
    def schoolService

    public void streamImageFromIdentifier(HttpServletResponse response, Image image) throws GrailsLabServiceException {
        String filePath = schoolService.storageImagePath(image.imagePath)
        try {
            File originalImage = new File(filePath)
            if(originalImage.exists()){
                response.contentType = image.getContentType()
                response.outputStream << originalImage?.newInputStream()
            }else {
                originalImage = new File(getNoSliderImage())
                response.contentType = "image/jpeg"
                response.outputStream << originalImage?.newInputStream()
            }
        }catch (IIOException e) {
            log.error("Error: streamImageFromIdentifier - Image ${image.imagePath} does not exist.")
            throw new GrailsLabServiceException("Image ${image.imagePath} does not exist." )
        }
    }

    public void streamImageFromIdentifierThumb(HttpServletResponse response, Image image) throws GrailsLabServiceException {
        String filePath = schoolService.storageImagePath(image.imagePathThumb)
        try {
            File originalImage = new File(filePath)
            if(originalImage.exists()){
                response.contentType = image.getContentType()
                response.outputStream << originalImage?.newInputStream()
            }else {
                log.error("Error: streamImageFromIdentifierThumb - Image ${image.imagePath} does not exist.")
                throw new GrailsLabServiceException("Image ${image.imagePath} does not exist." )
            }
        }catch (IIOException e) {
            log.error("Error: streamImageFromIdentifierThumb - Image ${image.imagePath} does not exist.")
            throw new GrailsLabServiceException("Image ${image.imagePath} does not exist." )
        }
    }

    private String getNoSliderImage() {
        return ServletContextHolder.servletContext.getRealPath('/images') +File.separator+ "noSliderImage.jpg"
    }

}
