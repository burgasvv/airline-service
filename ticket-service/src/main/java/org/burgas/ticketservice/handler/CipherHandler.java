package org.burgas.ticketservice.handler;

import org.springframework.stereotype.Component;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.crypto.Cipher.*;

@SuppressWarnings("unused")
@Component
public final class CipherHandler {

    private final SecretKey secretKey;

    {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            this.secretKey = keyGenerator.generateKey();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String encode(String stringForEncode)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = getInstance("AES");
        cipher.init(ENCRYPT_MODE, secretKey);
        byte[] bytes = cipher.doFinal(stringForEncode.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String decode(String stringForDecode)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = getInstance("AES");
        cipher.init(DECRYPT_MODE, secretKey);
        byte[] decode = Base64.getDecoder().decode(stringForDecode.getBytes(UTF_8));
        return new String(cipher.doFinal(decode), UTF_8);
    }
}
