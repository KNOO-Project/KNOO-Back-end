package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.entity.Image;
import com.woopaca.knoo.exception.post.impl.ImageNotReadable;
import com.woopaca.knoo.exception.post.impl.InvalidFileExtensionException;
import com.woopaca.knoo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    private Path path;
    @Value(value = "${file.path}")
    private String location;
    @Value(value = "${image.url}")
    private String imageUrl;

    @PostConstruct
    private void init() {
        this.path = Paths.get(location).toAbsolutePath().normalize();

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    @Transactional
    public List<Image> multipartFilesStoreAndConvertToImages(final List<MultipartFile> multipartFiles) {
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return List.of();
        }

        return multipartFiles.stream()
                .map(multipartFile -> {
                    String storedFileName;
                    try {
                        storedFileName = storeMultipartFile(multipartFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return Image.of(imageUrl + storedFileName);
                })
                .collect(Collectors.toList());
    }

    private String storeMultipartFile(MultipartFile multipartFile) throws IOException {
        String extension = getFileExtension(multipartFile);
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid.concat(extension);
        File file = new File(path + File.separator + fileName);
        multipartFile.transferTo(file);
        file.setWritable(true);
        file.setReadable(true);
        return fileName;
    }

    private String getFileExtension(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (contentType.contains("image/jpeg")) {
            return ".jpg";
        } else if (contentType.contains("image/png")) {
            return ".png";
        }

        throw new InvalidFileExtensionException();
    }

    public byte[] getPostImageBytes(String imageName) {
        return imageConvertToBytes(imageName);
    }

    private byte[] imageConvertToBytes(String imageName) {
        try (FileInputStream fileInputStream =
                     new FileInputStream(location + File.separator + imageName)) {
            return fileInputStream.readAllBytes();
        } catch (IOException e) {
            throw new ImageNotReadable();
        }
    }

    public void removeImageFiles(List<Image> images) {
        images.stream()
                .map(image -> image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1))
                .forEach(imageName -> {
                    try {
                        Files.delete(Paths.get(location + File.separator + imageName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
