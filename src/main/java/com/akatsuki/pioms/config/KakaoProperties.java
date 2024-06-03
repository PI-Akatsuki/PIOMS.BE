package com.akatsuki.pioms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kakao.api")
@Getter
@Setter
public class KakaoProperties {
    private String url;
    private String token;
    private String senderNumber;
    private String key;
    private String userId;
    private String sender;
    private String clientSecret;
    private String templateId;


}
