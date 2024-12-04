package com.jovisco.um.imagescalerui.services;

import java.util.Map;

public interface ImageScalerServiceAdapter {
    ImageScalerResponse execute(Map<String, Object> requestMap);
}
