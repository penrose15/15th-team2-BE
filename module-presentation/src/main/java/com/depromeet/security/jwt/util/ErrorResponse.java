package com.depromeet.security.jwt.util;

public record ErrorResponse(Integer status, String code, String message) {}
