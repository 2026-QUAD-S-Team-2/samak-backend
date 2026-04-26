package com.oaosis.samak.infra.gcs.service;

import com.oaosis.samak.infra.gcs.config.GcsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GcsUrlBuilder {
    private final GcsProperties gcsProperties;

    public String buildImageUrlWithFilePath(String filePath, String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }
        String urlPrefix = gcsProperties.getUrlPrefix();
        String prefix = urlPrefix.endsWith("/") ? urlPrefix : urlPrefix + "/";
        String path = (filePath == null || filePath.isEmpty()) ? "" :
                (filePath.endsWith("/") ? filePath : filePath + "/");
        String name = imageName.startsWith("/") ? imageName.substring(1) : imageName;
        return prefix + path + name;
    }

    public String buildImageUrl(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }
        String urlPrefix = gcsProperties.getUrlPrefix();
        String prefix = urlPrefix.endsWith("/") ? urlPrefix : urlPrefix + "/";
        String name = imageName.startsWith("/") ? imageName.substring(1) : imageName;
        return prefix + name;
    }
}
