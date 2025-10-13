package com.example.demo.imageProcc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/image")
@Slf4j
public class ImageProcessingControl {

    @Autowired
   private ImageProcessingService service;

    @PostMapping("/compare")
    public ResponseEntity<?> customerSignature (
            @RequestParam("image1") MultipartFile image1,
            @RequestParam ("image2") MultipartFile image2) {

        log.info(image1.getContentType());
        log.info(image1.getOriginalFilename());
        log.info("-----------------------");
        log.info(image2.getContentType());
        log.info(image2.getOriginalFilename());
        if (image1.isEmpty()&& image2.isEmpty()){
            log.warn("the images files are null");
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }

        return ResponseEntity.ok(service.compareSignatures(image1, image2));
    }
}
