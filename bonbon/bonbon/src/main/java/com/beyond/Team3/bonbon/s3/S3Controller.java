package com.beyond.Team3.bonbon.s3;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "S3", description = "파일 관리")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = s3Service.uploadFile(file);
            return ResponseEntity.ok( imageUrl);
        } catch (Exception e) {
            log.error("파일 업로드 실패", e); // 중요
            return ResponseEntity.ok( "파일 업로드에 실패했습니다.");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String filename) {
        try{
            s3Service.deleteFiles(filename);
            return ResponseEntity.ok("파일 삭제에 성공했습니다.");
        } catch (Exception e) {
            return ResponseEntity.ok( "파일 삭제에 실패했습니다.");
        }
    }
}
