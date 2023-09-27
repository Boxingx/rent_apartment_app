package com.example.rent_module.base64;

import java.util.Base64;

public class ApplicationEncoderDecoder {

    public static String decode(String value) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(value);
        return new String(decode);
    }

    public static String encode(String value) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(value.getBytes());
    }

    public static byte[] decodeToByte(String value) {
        Base64.Decoder decoderToByte = Base64.getDecoder();
        return decoderToByte.decode(value);
    }
}
