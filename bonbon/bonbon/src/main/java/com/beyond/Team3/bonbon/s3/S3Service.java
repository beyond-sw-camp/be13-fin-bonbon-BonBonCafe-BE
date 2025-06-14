package com.beyond.Team3.bonbon.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Slf4j
@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.bucket}")
    private String bucket;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // 업로드 할 file 를 받아서 처리 ->
    public String uploadFile(MultipartFile file) throws IOException {
        // UUID -> 고유 ID + 원본 사진 => 고유 사진 파일 이름 생성
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        log.info("Uploading file " + fileName);

        // 업로드하는 파일은 문서(pdf)나 이미지만 허용되도록
        String contentType = file.getContentType();
        log.info("Content type " + contentType);
        if (contentType == null ||
                (!contentType.equals("application/pdf") &&
                  !contentType.startsWith("image/jpeg") &&
                  !contentType.startsWith("image/png"))) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다. PDF, JPEG, PNG 파일만 업로드 가능합니다.");
        }
        log.info("컨텐트 타입 확인 완료");
        // 파일의 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());    // 업로드 되는 파일의 MIME 타입 지정 ex) file.jpg, file.png
        metadata.setContentLength(file.getSize());     // 업로드 파일의 크기를 바이트 단위로 설정
        log.info("Metadata " + metadata);

        // S3에 파일 업로드 요청 생성
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata);
        log.info("PutObjectRequest " + putObjectRequest);
        // S3에 파일 업로드
        amazonS3.putObject(putObjectRequest);
        log.info("PutObjectRequest " + putObjectRequest);

        // 파일이 성공적으로 업로드된 경우 -> https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/<파일명> 의 이름으로 URL 생성
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), fileName);
    }

    // 파일 삭제
    public void deleteFiles(String filename){
        amazonS3.deleteObject(bucket, filename);

        // 확인용 리스트 요청
        ObjectListing objects = amazonS3.listObjects(bucket);
        for (S3ObjectSummary summary : objects.getObjectSummaries()) {
            log.info("남아 있는 객체: " + summary.getKey());
        }
    }
}