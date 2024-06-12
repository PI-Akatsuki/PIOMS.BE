package com.akatsuki.pioms.image.service;

import com.akatsuki.pioms.image.aggregate.Image;
import com.akatsuki.pioms.image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;

    @Value("${spring.cloud.aws.s3.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.s3.secret-key}")
    private String secretKey;

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private S3Client createS3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public String uploadImage(int productCode, MultipartFile file) {
        try {
            S3Client s3 = createS3Client();

            String fileName = UUID.randomUUID().toString().substring(0, 6);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .acl(ObjectCannedACL.PUBLIC_READ)  // Public Read 권한 부여
                    .build();

            s3.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);

            Image image = new Image(imageUrl, productCode);
            imageRepository.save(image);

            return imageUrl;
        } catch (S3Exception | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Image getImageByProductCode(int productCode) {
        List<Image> images = imageRepository.findByProductCode(productCode);
        if (images == null || images.isEmpty())
            return null;
        return images.get(0);
    }

    public String changeImage(int productCode, MultipartFile file) {
        try {
            Image image = imageRepository.findByProductCode(productCode).get(0);

            if (image == null) {
                return null;
            }

            S3Client s3 = createS3Client();

            String fileName = UUID.randomUUID().toString().substring(0, 6);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            image.setUrl(String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName));
            imageRepository.save(image);

            return image.getUrl();
        } catch (S3Exception | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
