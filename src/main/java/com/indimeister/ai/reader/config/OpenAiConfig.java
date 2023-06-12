package com.indimeister.ai.reader.config;

import java.util.Arrays;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Random;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "openai")
public class OpenAiConfig {
  private String keys;

  private String model;

  private Integer maxTokens;

  private Double temperature;

  private String openaiApi;

  private String imageApi;

  private String tips;

  public String getApiKey() {
    String[] keyArray = keys.split(",");
    List<String> keyList = Arrays.asList(keyArray);
    if (keyList.size() == 1) {
      return keyList.get(0);
    }
    if(keyList.size() == 1){
      return keyList.get(0);
    }
    Random random = new Random();
    int index = random.nextInt(keyList.size());
    return keyList.get(index);
  }
}
