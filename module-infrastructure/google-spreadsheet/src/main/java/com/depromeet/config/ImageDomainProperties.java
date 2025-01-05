package com.depromeet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud-front")
public record ImageDomainProperties(String domain) {}
