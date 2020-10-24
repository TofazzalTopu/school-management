package com.grailslab.content

import com.grailslab.exceptions.FileUploadException
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.myapp.config.ConfigKey
import com.grailslab.settings.FileLibrary
import com.grailslab.settings.Image
import grails.gorm.transactions.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest
import java.awt.*
import java.awt.image.BufferedImage

@Transactional
class UploadService {
    def fileService
    def grailsApplication
    def springSecurityService
    def schoolService

    private static final int IMG_WIDTH = 30;
    private static final int IMG_HEIGHT = 30;

    Image uploadImage(HttpServletRequest request, String fieldName, String uploadCategory) throws FileUploadException {
        def f = request.getFile(fieldName)
        if (f.empty) {
            throw new FileUploadException('Upload Not successfull')
        }
        String extension = fileService.getExtensionFromPath(f.originalFilename)
//        todo: validate content type
        def allowedImageExtensions = ['png', 'jpg', 'jpeg', 'gif']
        if (!extension || !allowedImageExtensions.contains(extension.toLowerCase())) {
            throw new FileUploadException('upload.error.wrongDatatype: Extension "' + extension + '" is not allowed. Allowed extensions are: ' + allowedImageExtensions)
        }

        Image image = new Image(imageExtension: extension)
        image.imagePath = image.createImagePath(uploadCategory, extension)
        String absolutePath = schoolService.storageImagePath(image.imagePath)
        File uploadedFile = new File(absolutePath)
        //create parent directory if it does not exist
        new File(uploadedFile.getParent()).mkdirs()

        InputStream inputStream = selectInputStream(request, fieldName)
        upload(inputStream, uploadedFile)

        if (uploadedFile?.size() > 0) {
            image.save(flush: true)
            //log.info "Image UPLOAD OK"

        } else {
            image.discard()
            throw new FileUploadException('Upload Not successfull')
        }
        return image
    }
    String uploadImageWithThumbStr(HttpServletRequest request, String fieldName, String uploadCategory){
        Image uploadImage = uploadImageWithThumb(request, fieldName, uploadCategory)
        return uploadImage?.identifier
    }
    Image uploadImageWithThumb(HttpServletRequest request, String fieldName, String uploadCategory) throws FileUploadException {
        def f = request.getFile(fieldName)
        if (f.empty) {
            throw new FileUploadException('Upload Not successfull')
        }
        if (f.size > 1000000) {
            throw new FileUploadException('Maximum upload size exceds')
        }
        String extension = fileService.getExtensionFromPath(f.originalFilename)
        //todo: validate content type
        def allowedImageExtensions = ['png', 'jpg', 'jpeg', 'gif']
        if (!extension || !allowedImageExtensions.contains(extension.toLowerCase())) {
            throw new FileUploadException('upload.error.wrongDatatype: Extension "' + extension + '" is not allowed. Allowed extensions are: ' + allowedImageExtensions)
        }
        Image image = new Image(imageExtension: extension)
        image.imagePath = image.createImagePath(uploadCategory, extension)
        image.imagePathThumb = image.createImagePathThumb(uploadCategory, extension)


        String absolutePath = schoolService.storageImagePath(image.imagePath)
        String absolutePathThumb = schoolService.storageImagePath(image.imagePathThumb)
        File uploadedFile = new File(absolutePath)
        //create parent directory if it does not exist
        new File(uploadedFile.getParent()).mkdirs()

        InputStream inputStream = selectInputStream(request, fieldName)
        upload(inputStream, uploadedFile)

        try {

            BufferedImage originalImage = ImageIO.read(new File(absolutePath));
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

//            BufferedImage resizeImage = resizeImage(originalImage, type, height, width);
            ImageIO.write(originalImage, extension.toLowerCase(), new File(absolutePath));

            BufferedImage thumbImage = thumbImage(originalImage, type);
            ImageIO.write(thumbImage, extension.toLowerCase(), new File(absolutePathThumb));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (uploadedFile?.size() > 0) {

            image.validate()
            if (image.hasErrors()){
            }
            image.save(flush: true)
            //log.info "Image UPLOAD OK"

        } else {
            image.discard()
            throw new FileUploadException('Upload Not successfull')
        }
        return image
    }

    Image uploadImageWithThumb(HttpServletRequest request, String fieldName, String uploadCategory, int height, int width) throws FileUploadException {
        def f = request.getFile(fieldName)
        if (f.empty) {
            throw new FileUploadException('Upload Not successfull')
        }
        if (f.size > 1000000) {
            throw new FileUploadException('Maximum upload size exceds')
        }
        String extension = fileService.getExtensionFromPath(f.originalFilename)
        //todo: validate content type
        def allowedImageExtensions = ['png', 'jpg', 'jpeg', 'gif']
        if (!extension || !allowedImageExtensions.contains(extension.toLowerCase())) {
            throw new FileUploadException('upload.error.wrongDatatype: Extension "' + extension + '" is not allowed. Allowed extensions are: ' + allowedImageExtensions)
        }

        Image image = new Image(imageExtension: extension)
        image.imagePath = image.createImagePath(uploadCategory, extension)
        image.imagePathThumb = image.createImagePathThumb(uploadCategory, extension)


        String absolutePath = schoolService.storageImagePath(image.imagePath)
        String absolutePathThumb = schoolService.storageImagePath(image.imagePathThumb)
        File uploadedFile = new File(absolutePath)
        //create parent directory if it does not exist
        new File(uploadedFile.getParent()).mkdirs()

        InputStream inputStream = selectInputStream(request, fieldName)
        upload(inputStream, uploadedFile)

        try {

            BufferedImage originalImage = ImageIO.read(new File(absolutePath));
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

            BufferedImage resizeImage = resizeImage(originalImage, type, height, width);
            ImageIO.write(resizeImage, extension.toLowerCase(), new File(absolutePath));

            BufferedImage thumbImage = thumbImage(originalImage, type);
            ImageIO.write(thumbImage, extension.toLowerCase(), new File(absolutePathThumb));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (uploadedFile?.size() > 0) {
            image.save(flush: true)
            //log.info "Image UPLOAD OK"

        } else {
            image.discard()
            throw new FileUploadException('Upload Not successfull')
        }
        return image
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int height, int width) {

        int originalHeight = originalImage.getHeight()
        int originalWidth = originalImage.getWidth()
        int finalHeight
        int finalWidth
        double ratioWidth
        double ratioHeight

        if (originalHeight <= height || originalWidth <= width) {
            finalHeight = originalHeight
            finalWidth = originalWidth
        } else {
            ratioWidth = originalWidth / width
            ratioHeight = originalHeight / height

            if (ratioWidth > ratioHeight) {
                finalHeight = (int) originalHeight / ratioHeight
                finalWidth = (int) originalWidth / ratioHeight
            } else {
                finalHeight = (int) originalHeight / ratioWidth
                finalWidth = (int) originalWidth / ratioWidth
            }
        }

        BufferedImage resizeImage = new BufferedImage(finalWidth, finalHeight, type);
        Graphics2D g = resizeImage.createGraphics();
        g.drawImage(originalImage, 0, 0, finalWidth, finalHeight, null);
        g.dispose();

        return resizeImage;
    }

    private static BufferedImage thumbImage(BufferedImage originalImage, int type) {
        int originalHeight = originalImage.getHeight()
        int originalWidth = originalImage.getWidth()
        int finalHeight
        int finalWidth
        double ratioWidth
        double ratioHeight

        if (originalHeight < IMG_HEIGHT || originalWidth < IMG_WIDTH) {
            finalHeight = originalHeight
            finalWidth = originalWidth
        } else {
            ratioWidth = originalWidth / IMG_HEIGHT
            ratioHeight = originalHeight / IMG_WIDTH

            if (ratioWidth > ratioHeight) {
                finalHeight = (int) originalHeight / ratioHeight
                finalWidth = (int) originalWidth / ratioHeight
            } else {
                finalHeight = (int) originalHeight / ratioWidth
                finalWidth = (int) originalWidth / ratioWidth
            }
        }

        BufferedImage resizeImage = new BufferedImage(finalWidth, finalHeight, type);
        Graphics2D g = resizeImage.createGraphics();
        g.drawImage(originalImage, 0, 0, finalWidth, finalHeight, null);
        g.dispose();

        return resizeImage;
    }

    public InputStream selectInputStream(HttpServletRequest request, String fileUploadField = "qqfile") {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile(fileUploadField)
            return uploadedFile.inputStream
        }
        return request.inputStream
    }

    void upload(InputStream inputStream, File file) throws FileUploadException {
        try {
            file << inputStream
        } catch (Exception e) {
            throw new FileUploadException(e)
        }

    }
    def deleteImage(String imagePath) {
        Image image = Image.findByIdentifier(imagePath)
        if(image){
            image.activeStatus = ActiveStatus.DELETE
            image.save(flush: true)
            return true
        }
        return false
    }

    def deleteFile(String filePath) {
        FileLibrary fileLibrary = FileLibrary.findByIdentifier(filePath)
        if(fileLibrary){
            fileLibrary.activeStatus = ActiveStatus.DELETE
            fileLibrary.save(flush: true)
            return true
        }
        return false
    }

    def getImageInputStream(String imagePath){
        Image image = Image.findByIdentifier(imagePath)
        if(!image){
            return null
        }
        String absolutePath = grailsApplication.config.app.uploads.images.filesystemPath+image?.imagePath
        try {
            File fileImage = new File(absolutePath)
            if (!fileImage.exists()) {
                return null
            }
            return fileImage.newInputStream()
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null
    }

    FileLibrary uploadFile(HttpServletRequest request, String fieldName, String uploadCategory) throws FileUploadException {
        def f = request.getFile(fieldName)
        if (f.empty) {
            throw new FileUploadException('Upload Not successfull')
        }
        if (f.size > 15000000) {
            throw new FileUploadException('Maximum upload size exceds')
        }
        String extension = fileService.getExtensionFromPath(f.originalFilename)
        //todo: validate content type
        def allowedImageExtensions = ['pdf', 'PDF', 'png','jpg', 'jpeg', 'PNG', 'JPG', 'JPEG']
        if (!extension || !allowedImageExtensions.contains(extension.toLowerCase())) {
            throw new FileUploadException('upload.error.wrongDatatype: Extension "' + extension + '" is not allowed. Allowed extensions are: ' + allowedImageExtensions)
        }
        FileLibrary fileLibrary = new FileLibrary(fileExtension: extension)
        fileLibrary.filePath = fileLibrary.createPath(uploadCategory, extension)

        String absolutePath = schoolService.storageFilePath(fileLibrary.filePath)
        File uploadedFile = new File(absolutePath)
        //create parent directory if it does not exist
        new File(uploadedFile.getParent()).mkdirs()

        InputStream inputStream = selectInputStream(request, fieldName)
        upload(inputStream, uploadedFile)
        if (uploadedFile?.size() > 0) {
            fileLibrary.save(flush: true)
        } else {
            fileLibrary.discard()
            throw new FileUploadException('Upload Not successfull')
        }
        return fileLibrary
    }

    Boolean singleFileUpload(HttpServletRequest request, String fieldName, String fileName) throws FileUploadException {
        def f = request.getFile(fieldName)
        if (f.empty) {
            throw new FileUploadException('Upload Not successfull')
        }
        if (f.size > 10000000) {
            throw new FileUploadException('Maximum upload size exceds')
        }
        String extension = fileService.getExtensionFromPath(f.originalFilename)
        //todo: validate content type
        def allowedImageExtensions = ['png', 'jpg']
        if (!extension || !allowedImageExtensions.contains(extension.toLowerCase())) {
            throw new FileUploadException('upload.error.wrongDatatype: Extension "' + extension + '" is not allowed. Allowed extensions are: ' + allowedImageExtensions)
        }
        String absolutePath = grailsApplication.config.getProperty(ConfigKey.GRAILSLAP_GSCHOOL_IMAGE_PATH) + File.separator + fileName + '.' + extension
        File uploadedFile = new File(absolutePath)
        //create parent directory if it does not exist
        new File(uploadedFile.getParent()).mkdirs()
        f.transferTo(uploadedFile)
        if (uploadedFile?.size() > 0) {
            return true
        } else {
            return false
        }
        return true
    }

    def getFileInByte(String identifier, def response) {
        FileLibrary fileLibrary = FileLibrary.findByIdentifier(identifier)
        if(fileLibrary) {
            String absolutePath = grailsApplication.config.app.uploads.files.filesystemPath + fileLibrary.filePath
            File contentFile = new File(absolutePath)
            if (!contentFile.exists()) {
                return null
            }
            def fileInputStream = new FileInputStream(contentFile)
            def outputStream = response.getOutputStream()
            byte[] buffer = new byte[10000000]
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush()
            outputStream.close()
            fileInputStream.close()
            return buffer
        }
        return null
    }

    def getSingleFileInByte(String filPath, def response) {
        if(filPath) {
            String absolutePath = grailsApplication.config.getProperty(ConfigKey.GRAILSLAP_GSCHOOL_IMAGE_PATH) + filPath
            File contentFile = new File(absolutePath)
            if (!contentFile.exists()) {
                return null
            }
            def fileInputStream = new FileInputStream(contentFile)
            def outputStream = response.getOutputStream()
            byte[] buffer = new byte[10000000]
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush()
            outputStream.close()
            fileInputStream.close()
            return buffer
        }
        return null
    }

}
