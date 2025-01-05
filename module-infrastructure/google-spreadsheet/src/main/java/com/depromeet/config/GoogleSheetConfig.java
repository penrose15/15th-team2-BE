package com.depromeet.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {SpreadSheetProperties.class, ImageDomainProperties.class})
public class GoogleSheetConfig {}
