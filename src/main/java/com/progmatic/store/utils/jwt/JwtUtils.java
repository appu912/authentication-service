package com.progmatic.store.utils.jwt;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtUtils {
  private static final String SECRET = "a-string-secret-at-least-256-bits-long";
  private static final String SIGNING_ALGO = "HS256";
  private static final String TOKEN_TYPE = "JWT";
  private final ObjectMapper objectMapper;

  public JwtUtils(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String getJwtToken(String userId) {
    String header = getJsonHeader();
    String payload = getJsonClaims(userId);
    String base64UrlEncodedHeader = getBase64UrlEncoded(header);
    String base64UrlEncodedPayload = getBase64UrlEncoded(payload);
    String message = base64UrlEncodedHeader + "." + base64UrlEncodedPayload;
    String signature = getBase64UrlEncoder(getHmacSha256Signature(message));
    return message + "." + signature;
  }

  public boolean validateToken(String token) {
    String[] tokenParts = token.split("\\.");
    return tokenParts.length == 3;
  }

  private String getJsonHeader() {
    Header header = new Header.Builder().alg(SIGNING_ALGO).typ(TOKEN_TYPE).build();
    return objectMapper.writeValueAsString(header);
  }

  private String getJsonClaims(String userId) {
    Claims claims =
        new Claims.Builder().name("John Doe").sub(userId).admin(true).iat(1516239022L).build();
    return objectMapper.writeValueAsString(claims);
  }

  private byte[] getHmacSha256Signature(String message) {
    try {
      Mac hmacSha256 = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec =
          new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      hmacSha256.init(secretKeySpec);
      return hmacSha256.doFinal(message.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException exception) {
      System.out.println("The provided algorithm doesn't exists. " + exception.getMessage());
    } catch (InvalidKeyException exception) {
      System.out.println("Invalid Secret Key. " + exception.getMessage());
    }
    return new byte[0];
  }

  private String getBase64UrlEncoded(String string) {
    return getBase64UrlEncoder(string.getBytes(StandardCharsets.UTF_8));
  }

  private String getBase64UrlEncoder(byte[] byteArray) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(byteArray);
  }
}
