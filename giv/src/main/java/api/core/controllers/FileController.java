package api.core.controllers;

import api.core.entities.UploadFileResponse;
import api.core.services.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class allows you to upload & download a non multi-part file - you can upload your company's logo as an image.
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-09-26
 */
@Slf4j
@RestController
public class FileController {
  @Autowired
  private FileStorageService fileStorageService;

  @PostMapping("/uploadFile")
  public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
    String fileName = fileStorageService.storeFile(file);

    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/downloadFile/")
        .path(fileName)
        .toUriString();

    return new UploadFileResponse(fileName, fileDownloadUri,
        file.getContentType(), file.getSize());
  }

  @PostMapping("/uploadMultipleFiles")
  public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws Exception {
    List<UploadFileResponse> list = new ArrayList<>();
    for (MultipartFile file : Arrays.asList(files)) {
      UploadFileResponse uploadFileResponse = uploadFile(file);
      list.add(uploadFileResponse);
    }
    return list;
  }

  @GetMapping("/downloadFile/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws Exception {
    // Load file as Resource
    Resource resource = fileStorageService.loadFileAsResource(fileName);

    // Try to determine file's content type
    String contentType = null;
    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (IOException ex) {
      log.info("Could not determine file type.");
    }

    // Fallback to the default content type if type could not be determined
    if(contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }
}
