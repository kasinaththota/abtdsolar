package com.abtd.solarbackend.file.service;

import com.abtd.solarbackend.file.dto.FileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    FileResponse uploadFile(MultipartFile file);

    Resource downloadFile(String fileName);

    void deleteFile(String fileName);
}