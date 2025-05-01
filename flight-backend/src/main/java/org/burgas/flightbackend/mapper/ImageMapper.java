package org.burgas.flightbackend.mapper;

import jakarta.servlet.http.Part;
import org.burgas.flightbackend.entity.Image;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public final class ImageMapper {

    public Image uploadImagePart(final Part part) throws IOException {
        return Image.builder()
                .name(part.getSubmittedFileName())
                .contentType(part.getContentType())
                .size(part.getSize())
                .format(part.getContentType().split("/")[1])
                .data(part.getInputStream().readAllBytes())
                .build();
    }

    public Image changeImagePart(final Long imageId, final Part part) throws IOException {
        return Image.builder()
                .id(imageId)
                .name(part.getSubmittedFileName())
                .contentType(part.getContentType())
                .size(part.getSize())
                .format(part.getContentType().split("/")[1])
                .data(part.getInputStream().readAllBytes())
                .build();
    }
}
