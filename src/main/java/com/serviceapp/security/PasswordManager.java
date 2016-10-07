package com.serviceapp.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Class with methods for creating hash password with salt
 */
@Component
@PropertySource("classpath:movieApp.properties")
public class PasswordManager implements PasswordEncoder {

    private static final Logger LOGGER = LogManager.getLogger();
    private Environment environment;

    @Autowired
    public PasswordManager(Environment environment) {
        this.environment = environment;
    }

    /**
     * Encodes password by hashing with salt
     *
     * @param password password to encode
     * @return hashed password with salt
     * @throws IllegalArgumentException thrown when password is corrupted or has unsupported characters
     */
    @Override
    public String encode(CharSequence password) throws IllegalArgumentException {
        byte[] salt = generateSalt(password);
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }

    /**
     * Overridden method from Spring's <code>PasswordEncoder</code> interface. Performs match of encoded password
     * with raw password, encoding last one and comparing them.
     *
     * @param rawPassword     raw user password to encode and to compare
     * @param encodedPassword user encoded password
     * @return <b>true</b> if encoded passwords match. Otherwise returns <b>false</b>
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String encodedRaw = encode(rawPassword);
        return encodedRaw.equals(encodedPassword);
    }

    /**
     * Hashes given password with salt
     *
     * @param password password to hash
     * @param salt     password salt
     * @return string representing password in hashed form
     * @throws IllegalArgumentException thrown when password is corrupted or has unsupported characters
     */
    private String hash(CharSequence password, byte[] salt) throws IllegalArgumentException {
        SecretKeyFactory secretKeyFactory;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(environment.getProperty("password.secretKeyFactory"));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.fatal("No specified algorithm found. Error: " + e, e);
            throw new RuntimeException("No specified algorithm found. Error: " + e, e);
        }

        PBEKeySpec pbeKeySpec;
        try {
            pbeKeySpec = new PBEKeySpec(((String) password).toCharArray(), salt,
                    Integer.parseInt(environment.getProperty("password.hashIterations")),
                    Integer.parseInt(environment.getProperty("password.keyLength")));
        } catch (NumberFormatException e) {
            LOGGER.fatal("Can't parse property value. " + e, e);
            throw new RuntimeException("Can't parse property value. " + e, e);
        }

        SecretKey secretKey;
        try {
            secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
        } catch (InvalidKeySpecException e) {
            LOGGER.fatal("Can't find matching key spec. Error: " + e, e);
            throw new RuntimeException("Can't find matching key spec. Error: " + e, e);
        }

        return Base64.encodeBase64String(secretKey.getEncoded());
    }

    /**
     * Generates salt with given password
     *
     * @param password password to generate salt
     * @return byte array of password with salt
     */
    private byte[] generateSalt(CharSequence password) {
        Integer passwordMaxLength = Integer.valueOf(environment.getProperty("password.maxLength"));
        Integer passwordMinLength = Integer.valueOf(environment.getProperty("password.minLength"));
        if (password == null || ((String) password).trim().equals("")
                || password.length() < passwordMinLength || password.length() > passwordMaxLength) {
            LOGGER.error("Wrong password: " + password);
            throw new IllegalArgumentException(
                    "Password should not be empty and must have at least " + passwordMinLength +
                            " but no more than " + passwordMaxLength + " characters.");
        }
        byte[] bytes = String.valueOf(password.length() * 42 + 3).getBytes();
        byte[] bytes_ = ((String) password).getBytes();
        byte[] combined = new byte[bytes.length + bytes_.length];
        System.arraycopy(bytes, 0, combined, 0, bytes.length);
        System.arraycopy(bytes_, 0, combined, bytes.length, bytes_.length);
        return combined;
    }


}