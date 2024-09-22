package com.raghav.ethglobalsingapore2024.javaapp;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;

public class MessageSigner {
    private Signature ecdsaSign;
    private PrivateKey privateKey;
    public MessageSigner(String message, PrivateKey privateKey) {
        try {
            ecdsaSign = Signature.getInstance("SHA256withECDSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        this.privateKey = privateKey;
    }

    public byte[] signMessage(String message) {
        try {
            ecdsaSign.initSign(privateKey);
            ecdsaSign.update(message.getBytes());
            return ecdsaSign.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
