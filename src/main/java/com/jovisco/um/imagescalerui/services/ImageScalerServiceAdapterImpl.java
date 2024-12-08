package com.jovisco.um.imagescalerui.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javafx.concurrent.Task;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageScalerServiceAdapterImpl extends javafx.concurrent.Service<ImageScalerResponse> implements ImageScalerServiceAdapter {

    private final ImageScalerService imageScalerService;

    private String sourceDir;
    private String targetDir;
    private String filter;
    private List<Integer> widths;

    @Override
    public ImageScalerResponse execute(Map<String, Object> requestMap) {
        // map from request map
        if (requestMap == null) throw new IllegalArgumentException("requestMap cannot be null");
        mapRequestToParams(requestMap);
        // invoke service
        return imageScalerService.execute(sourceDir, targetDir, filter, widths);
    }

    private void mapRequestToParams(Map<String, Object> requestMap) {
        // map from request map
        sourceDir = (String) requestMap.get("sourceDir");
        targetDir = (String) requestMap.get("targetDir");
        filter = "*.{jpg,JPG,jpeg,JPEG,png,PNG}";
        if (requestMap.get("jpg").toString().equals("false")) {
            filter = filter.replaceAll("(?i)jpg", "");
            filter = filter.replaceAll("(?i)jpeg", "");
        }
        if (requestMap.get("png").toString().equals("false")) {
            filter = filter.replaceAll("(?i)png", "");
        }
        widths = new ArrayList<>();
        requestMap.forEach((key, value) -> {
            if (key.contains("resolution") && value.toString().equals("true")) {
                var size = Integer.parseInt(key.replace("resolution", ""));
                widths.add(size);
            }
        });
    }

    @Override
    protected Task<ImageScalerResponse> createTask() {
       return new Task<>() {
           @Override
           protected ImageScalerResponse call() throws Exception {
               var future = CompletableFuture.supplyAsync(() -> imageScalerService.execute(sourceDir, targetDir, filter, widths))
                       .exceptionally(e -> new ImageScalerResponse(0, 0));
               return future.get();
           }
        };
    }
}
