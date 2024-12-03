package com.jovisco.um.imagescalerui.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageScalerServiceAdapter {

    private final ImageScalerService imageScalerService;

    public ImageScalerResponse execute(Map<String, Object> requestMap) {
        // map from request map
        var sourceDir = (String) requestMap.get("sourceDir");
        var targetDir = (String) requestMap.get("targetDir");
        var filter = "*.{jpg,png}";
        if (requestMap.get("jpg").toString().equals("false")) {
            filter = filter.replace("jpg", "");
        }
        if (requestMap.get("png").toString().equals("false")) {
            filter = filter.replace("png", "");
        }
        var widths = new ArrayList<Integer>();
        requestMap.forEach((key, value) -> {
            if (key.contains("resolution") && value.toString().equals("true")) {
                var size = Integer.parseInt(key.replace("resolution", ""));
                widths.add(size);
            }
        });
        // invoke service
        return imageScalerService.execute(sourceDir, targetDir, filter, widths);
    }

}
