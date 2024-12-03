package com.jovisco.um.imagescalerui.services;

import java.io.IOException;
import java.util.List;

public interface ImageScalerService {
    ImageScalerResponse execute(String sourceDir, String targetDir, String filter, List<Integer> widths);
}
