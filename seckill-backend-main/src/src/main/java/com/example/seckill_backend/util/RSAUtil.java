package com.example.seckill_backend.util;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    private static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDZ5YWrr3aA4DzATAV1hYzlUxgReFnZBUizkPn/Bss1H3lb3t1YhC/JSKqo06+KZLB3yNZmy4bhH+FpLeIc6p48NMwJbaD8HM4iWMrl0TE16MiKDDd6J+3z0h6t7YUE08RAMcfG7i0xrUHN6/xuPIFae/dXHDiEi58fDxM7qg78cam8hyIkQZCj3yRGTEEhFNLHtQJDNT2fi9KLVxoQcTutok8e9XoKtAZvOStznRDbtu9aapmzeLmvgp12bD80QPMso1Iu7vX0yoPQ4IZ5FzRPlxKXivb3FBKNnAquiHfkvisUYhtEltPpIZY2OGPAG5JslyjefmIPINNegzlL/Q0NAgMBAAECggEAK6MSYcduM1GVAQrIdhh0ltGNCqSgU7zB6kowssDkzcL7nik0CV/RpR0jJlPcfhn7mEWVoHkGjkvA6/w3IgTfgx2stf+pQ4wuQQUovx0lno77qF/ZdWKNPizYnYrpfoW9cgC2KlH1JZN1z/xAbcA6xIj6iAy8CdlfWMw/VQ3R2f63jWGMZeOJ+sMCHG8nbL5b0OnCDo6lapyIBaRdcaiPSF18q58TMGUNwLyV0ojAsgBgSFfwAmgKh0p9vqtVn8/cOWWPmWKgm0lRgdInM5jv+2WtYZXu+2J5pmu6egQ3oAeMcxmO8/XPSVeDGRznrmHTJ4RQGvoVh/4tco/yd+k8MQKBgQD7xD3awj+Zrn4BlamOeCGlr4RjCfjbIOdpLiM0FeQnhrf4FOhiI44ipbCMaPGmYIWx81UgbasFkhtNzaLAJ/RCdT62OMT+apJdrTuErj8zO3JClYy8TjriMY8d2t6G5ee+61lLWP277UbOzq2KbniNCjM3aXf8aX4DOlfRWDctcQKBgQDdj3uy9HLVbZ57TE0DvCvrmIhCVX/ueR+Qe61Eck2DPDbw2skRawW5EzjsEKu7uZLADvNVNfHjCai7DmwDH8uVyhaGf0pUvLR4Dt5TyDyz6iunRWRNlUVdGzuAH/MSuEs7+Y8LpQh30v7KnFF+QioLa10H9xxcFAftx4+tLcC7XQKBgHSjc+yXNZPjTSk6xvTkHuJ9sB2aDxql4c/Ouix1P+A3/58FQKvGwSC4FlTWga2M5iONEhEV/bd0+wVOkWnomLb5TnZpUopzoR+ZFAviFhGMJuFGG49ZxtnoD9/K+HESrgy7SAEqV1LrGHTTN378swjUgunXCOl9jg28x9aKwfVhAoGBAKJPefRoxjN6wc5b4FOrJ5XJwZCu6DDAxRHouM9a2VQZ6bna3dAH0kJ6RYoS0GyPD2XpzfOPuGt16QlYEGLdiRxr1sKjP8YCFEebxN4l4+p904sP9+IqR41k5NsBDOaa4mrGcTe7qA94IDY3/C99syV4esfaDQmblw9n0eaxjO71AoGBAJeXp7oPyOASp5nQYzD33n7DDWjhPHIqxmyd/Cy75B8jBTo2MKPEQHRkr6m2f1+BhCd70fmgPGhgJAjTRTyugvMWavhHR0deLCmXJ5+mfF8/TrSJdbHysfyUhUwYTrW0MuAD3BuR4YG+0ql8goekfOoNCabuseh0pa+fb4alCMwY";
    public static String decrypt(String encryptedData) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}

