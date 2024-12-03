package com.jovisco.um.imagescalerui.services;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageScalerServiceImpl implements ImageScalerService {

    @Override
    public ImageScalerResponse execute(String sourceDir, String targetDir, String filter, List<Integer> widths) {

        var imagesFilePath = Path.of(sourceDir);
        var resizedImagesDirectory = new File(targetDir);
        var countOriginalImages = 0;
        var countResizedImages = 0;

        try (var imageFiles = Files.newDirectoryStream(imagesFilePath, filter)) {
            for (Path file : imageFiles) {
                // create target directory if not exists
                if (!resizedImagesDirectory.exists()) {
                    var created = resizedImagesDirectory.mkdir();
                }
                // resize image
                var originalImage = ImageIO.read(file.toFile());
                countOriginalImages++;
                // do not try to resize images to dimensions larger than the original
                var restrictedWidths = getRestrictedWidths(originalImage, widths);
                for (var width : restrictedWidths) {
                    var resizedImage = Scalr.resize(originalImage, width);
                    var targetFilename = generateTargetFilename(resizedImagesDirectory, file.toString(), width);
                    ImageIO.write(resizedImage, getFileExtension(targetFilename), new File(targetFilename));
                    countResizedImages++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageScalerResponse(countOriginalImages, countResizedImages);
    }

    private List<Integer> getRestrictedWidths(BufferedImage originalImage, List<Integer> widths) {
        // do not try to resize images to dimensions larger than the original
        var maxDimension = Math.max(originalImage.getWidth(), originalImage.getHeight());
        return widths.stream().filter(width -> width <= maxDimension).toList();
    }

    private String generateTargetFilename(File resizedImagesDirectory, String inputFilename, int targetWidth) {
        var targetPathName = resizedImagesDirectory.getPath();
        var sourcePathName = inputFilename.substring(0, inputFilename.lastIndexOf("/"));
        var filename = inputFilename.substring(0, inputFilename.lastIndexOf("."));
        filename = filename.replace(sourcePathName, "");
        filename = targetPathName + "/" + filename + "_" + targetWidth;
        var extension = inputFilename.substring(inputFilename.lastIndexOf("."));
        return filename + extension;
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }
}
