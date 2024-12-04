package com.jovisco.um.imagescalerui.services;

import com.jovisco.um.imagescalerui.ProgressCounter;
import lombok.Getter;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class ImageScalerServiceImpl implements ImageScalerService {

    @Override
    public ImageScalerResponse execute(String sourceDir, String targetDir, String filter, List<Integer> widths) {

        var imagesFilePath = Path.of(sourceDir);
        var resizedImagesDirectory = new File(targetDir);
        var counter = ProgressCounter.getInstance();

        try(var es = Executors.newFixedThreadPool(4);
            var imageFiles = Files.newDirectoryStream(imagesFilePath, filter);) {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (Path file : imageFiles) {
                // create target directory if not exists
                if (!resizedImagesDirectory.exists()) {
                    var created = resizedImagesDirectory.mkdir();
                }
                // resize images
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        var originalImage = ImageIO.read(file.toFile());
                        counter.incrementOriginalImages();
                        // do not try to resize images to dimensions larger than the original
                        var restrictedWidths = getRestrictedWidths(originalImage, widths);
                        for (var width : restrictedWidths) {
                            var resizedImage = Scalr.resize(originalImage, width);
                            var targetFilename = generateTargetFilename(resizedImagesDirectory, file.toString(), width);
                            var imageFormat = getFileExtension(targetFilename).substring(1);
                            ImageIO.write(resizedImage, imageFormat, new File(targetFilename));
                            counter.incrementResizedImages();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, es);
                futures.add(future);
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageScalerResponse(counter.getCountOriginalImages(), counter.getCountResizedImages());
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
        filename = targetPathName + filename + "_" + targetWidth;
        var extension = inputFilename.substring(inputFilename.lastIndexOf("."));
        return filename + extension;
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }
}
