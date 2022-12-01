package org.whirlplatform.server.driver.multibase;

import javax.servlet.ServletContext;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.RandomSaltGenerator;

public class Encryptor {

    private static final String ENCRYPTOR_PASSWORD = "ENCRYPTOR_PASSWORD";

    private static Encryptor _instance;

    public StandardPBEStringEncryptor encryptor;

    private Encryptor(StandardPBEStringEncryptor enc) {
        encryptor = enc;
    }

    public static Encryptor get(ServletContext ctx) {
        if (_instance == null) {
            String password = (String) ctx.getAttribute(ENCRYPTOR_PASSWORD);
            if (password == null) {
                password = String.valueOf(System.currentTimeMillis());
                ctx.setAttribute(ENCRYPTOR_PASSWORD, password);
            }
            StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
            enc.setPassword(password);
            enc.setSaltGenerator(new RandomSaltGenerator());

            _instance = new Encryptor(enc);
        }
        return _instance;
    }

    public String encrypt(String str) {
        return encryptor.encrypt(str);
    }

    public String decrypt(String str) {
        return encryptor.decrypt(str);
    }
}
