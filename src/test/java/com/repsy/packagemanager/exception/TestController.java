package com.repsy.packagemanager.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestController
public class TestController {

    @GetMapping("/test-custom-exception")
    public void throwCustomException() {
        throw new CustomException("Test error message");
    }

    @GetMapping("/test-max-size-exception")
    public void throwMaxSizeException() {
        throw new MaxUploadSizeExceededException(1024L);
    }

    @GetMapping("/test-generic-exception")
    public void throwGenericException() {
        throw new RuntimeException("Test generic error");
    }
} 