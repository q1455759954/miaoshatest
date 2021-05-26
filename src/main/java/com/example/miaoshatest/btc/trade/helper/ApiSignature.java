package com.example.miaoshatest.btc.trade.helper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;


public class ApiSignature {

  public static final String API_KEY = "vftwcr5tnh-5d184dc9-439346e2-6060b";
  public static final String SECRET_KEY = "20e51f74-f70e98ec-66928696-5b5de";
  public static final String op = "op";
  public static final String opValue = "auth";
  private static final String accessKeyId = "AccessKeyId";
  private static final String signatureMethod = "SignatureMethod";
  private static final String signatureMethodValue = "HmacSHA256";
  private static final String signatureVersion = "SignatureVersion";
  private static final String signatureVersionValue = "2";
  private static final String timestamp = "Timestamp";
  private static final String signature = "Signature";

  private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter
      .ofPattern("uuuu-MM-dd'T'HH:mm:ss");
  private static final ZoneId ZONE_GMT = ZoneId.of("Z");


  public void createSignature(String accessKey, String secretKey, String method, String host,
      String uri, UrlParamsBuilder builder) {
    StringBuilder sb = new StringBuilder(1024);

    if (accessKey == null || "".equals(accessKey) || secretKey == null || "".equals(secretKey)) {

    }

    sb.append(method.toUpperCase()).append('\n')
        .append(host.toLowerCase()).append('\n')
        .append(uri).append('\n');

    builder.putToUrl(accessKeyId, accessKey)
        .putToUrl(signatureVersion, signatureVersionValue)
        .putToUrl(signatureMethod, signatureMethodValue)
        .putToUrl(timestamp, gmtNow());

    sb.append(builder.buildSignature());
    Mac hmacSha256 = null;
    try {
      hmacSha256 = Mac.getInstance(signatureMethodValue);
      SecretKeySpec secKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
          signatureMethodValue);
      hmacSha256.init(secKey);
    } catch (NoSuchAlgorithmException e) {

    } catch (InvalidKeyException e) {

    }
    String payload = sb.toString();
    byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));

    String actualSign = Base64.getEncoder().encodeToString(hash);

    builder.putToUrl(signature, actualSign);

  }

  private static long epochNow() {
    return Instant.now().getEpochSecond();
  }

  static String gmtNow() {
    return Instant.ofEpochSecond(epochNow()).atZone(ZONE_GMT).format(DT_FORMAT);
  }
}
