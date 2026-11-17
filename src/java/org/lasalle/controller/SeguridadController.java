package org.lasalle.controller;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.Instant;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.lasalle.model.*;

public final class SeguridadController {
  private static final SecureRandom RANDOM = new SecureRandom();
  private static final Base64.Encoder ENC = Base64.getUrlEncoder().withoutPadding();
  private static final Base64.Decoder DEC = Base64.getUrlDecoder();
  public static final long SESSION_SECONDS = 28800;

  private SeguridadController() {}

  public static String hash(String password) {
    byte[] salt = new byte[16];
    RANDOM.nextBytes(salt);
    return "pbkdf2$600000$"
        + ENC.encodeToString(salt)
        + "$"
        + ENC.encodeToString(derive(password, salt, 600000));
  }

  private static byte[] derive(String p, byte[] salt, int rounds) {
    PBEKeySpec spec = new PBEKeySpec(p.toCharArray(), salt, rounds, 256);
    try {
      return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    } finally {
      spec.clearPassword();
    }
  }

  public static boolean verify(String p, String hash) {
    try {
      String[] a = hash.split("\\$");
      if (a.length != 4 || !a[0].equals("pbkdf2")) return false;
      int rounds = Integer.parseInt(a[1]);
      if (rounds < 100000 || rounds > 1000000) return false;
      return MessageDigest.isEqual(DEC.decode(a[3]), derive(p, DEC.decode(a[2]), rounds));
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static String token(String id, int version) {
    return token(id, version, DatosController.env("AUTH_SECRET"), Instant.now().getEpochSecond());
  }

  static String token(String id, int version, String secret, long now) {
    String payload = id + ":" + version + ":" + (now + SESSION_SECONDS) + ":" + UUID.randomUUID();
    String body = ENC.encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    return body + "." + ENC.encodeToString(mac(body, secret));
  }

  public static String[] claims(String token) {
    return claims(token, DatosController.env("AUTH_SECRET"), Instant.now().getEpochSecond());
  }

  static String[] claims(String token, String secret, long now) {
    try {
      if (token == null || token.length() > 1024) throw new IllegalArgumentException();
      String[] p = token.split("\\.");
      if (p.length != 2 || !MessageDigest.isEqual(DEC.decode(p[1]), mac(p[0], secret)))
        throw new IllegalArgumentException();
      String[] a = new String(DEC.decode(p[0]), StandardCharsets.UTF_8).split(":");
      if (a.length != 4 || Long.parseLong(a[2]) <= now) throw new IllegalArgumentException();
      ValidacionController.uuid(a[0]);
      Integer.parseInt(a[1]);
      return a;
    } catch (RuntimeException e) {
      if (e instanceof IllegalStateException) throw e;
      throw new ErrorAplicacion(401, "Sesión no válida o vencida");
    }
  }

  private static byte[] mac(String body, String secret) {
    if (secret.length() < 32)
      throw new IllegalStateException("AUTH_SECRET debe tener al menos 32 caracteres");
    try {
      Mac m = Mac.getInstance("HmacSHA256");
      m.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      return m.doFinal(body.getBytes(StandardCharsets.UTF_8));
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    }
  }
}
