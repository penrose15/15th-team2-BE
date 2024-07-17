package com.depromeet.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccessTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") Integer expiresIn,
        @JsonProperty("scope") String scope,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("id_token") String idToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("refresh_token_expires_in") Integer refreshTokenExpiresIn) {}
