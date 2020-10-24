package com.grailslab

import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import javax.imageio.plugins.jpeg.JPEGImageWriteParam
import javax.imageio.stream.ImageOutputStream
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage

/**
 * Created by aminul on 7/11/2015.
 */
class ImageUtils {
    /**
     * Crops an image so it fits inside a certain square
     */
    public static BufferedImage crop(BufferedImage img, int w, int h) {
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        // crop ...
        float ratio = img.getWidth() / (float) img.getHeight();
//		System.out.println(ratio < w / (float) h);
        if (ratio < w / (float) h) // scale factor is w/img.getWidth()
        {
            result.getGraphics().drawImage(img.getScaledInstance(w, img.getHeight() * w / img.getWidth(), BufferedImage.SCALE_AREA_AVERAGING), 0, (h - img.getHeight() * w / img.getWidth()) / 2, null);
        } else // scale factor is h/img.getHeight
        {
            result.getGraphics().drawImage(img.getScaledInstance(img.getWidth() * h / img.getHeight(), h, BufferedImage.SCALE_AREA_AVERAGING), (w - img.getWidth() * h / img.getHeight()) / 2, 0, null);
        }

        return result;
    }

    /**
     * Fits an image inside a square
     */
    public static BufferedImage fit(BufferedImage img, int w, int h) {
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        // crop ...
        float ratio = img.getWidth() / (float) img.getHeight();
//		System.out.println(ratio < w / (float) h);
        if (ratio > w / (float) h) // scale factor is w/img.getWidth()
        {
            result.getGraphics().drawImage(img.getScaledInstance(w, img.getHeight() * w / img.getWidth(), BufferedImage.SCALE_AREA_AVERAGING), 0, (h - img.getHeight() * w / img.getWidth()) / 2, null);
        } else // scale factor is h/img.getHeight
        {
            result.getGraphics().drawImage(img.getScaledInstance(img.getWidth() * h / img.getHeight(), h, BufferedImage.SCALE_AREA_AVERAGING), (w - img.getWidth() * h / img.getHeight()) / 2, 0, null);
        }

        return result;
    }


    /**
     * Creates an input stream that can be passed on to any kind of output
     * (e.g. to mongodb's blob, play-framework's renderBinary, etc.)
     * <p/>
     * The only way to fuck this up is you plug in a wrong image type.
     * You'll get a null object as a result in that case.
     *
     * @param img  The image you'd like to output.
     * @param type The image type you'd like. This uses the ImageIO library, so sticking to png, jpg, gif might be a good idea.
     * @return An input stream with the image in binary format, or null if an invalid image or type was specified
     */
    public static ByteArrayInputStream toInputStream(BufferedImage img, String type) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, type, baos);
        } catch (Exception e) {
            return null;
        }

        return new ByteArrayInputStream(baos.toByteArray());
    }


    /**
     * Write the image to the stream as a JPEG with highest quality / low compression.
     *
     * @param os
     * @param image
     * @return
     */
    public static boolean writeJPEGImageStream(OutputStream os, RenderedImage image) {
        try {
            compressJpegStream(os, image, 1.0f);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param outFile
     * @param image
     * @param compressionQuality
     * @throws IOException
     */
    public static void compressJpegFile(File outFile, RenderedImage image, float compressionQuality) throws IOException {
        OutputStream outStream = new FileOutputStream(outFile);
        compressJpegStream(outStream, image, compressionQuality);
    }

    /**
     * @param outStream
     * @param image
     * @param compressionQuality 0.0 for low quality / high compression to 1.0 for high quality / low compression
     * @throws IOException
     */
    public static void compressJpegStream(OutputStream outStream, RenderedImage image, float compressionQuality) throws IOException {
        // Find a jpeg writer
        ImageWriter writer = null;
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
        if (iter.hasNext()) {
            writer = iter.next();
        }

        // Set the compression quality
        ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault()) {
            public void setCompressionQuality(float quality) {
                if (quality < 0.0F || quality > 1.0F) {
                    throw new IllegalArgumentException("Quality out-of-bounds!");
                }
                this.compressionQuality = quality;
            }
        };
        iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwparam.setCompressionQuality(compressionQuality);

        // Prepare output file
        ImageOutputStream ios = ImageIO.createImageOutputStream(outStream);
        writer.setOutput(ios);

        // Write the image
        writer.write(null, new IIOImage(image, null, null), iwparam);

        // Cleanup
        ios.flush();
        writer.dispose();
        ios.close();
    }
}
