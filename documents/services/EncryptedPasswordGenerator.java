package com.portfolio.documents.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
To run this utility, use the following commands in your terminal:

cd /Users/pyawalkar/Documents/Personal\ Portfolio/backend/portfolio
./mvnw dependency:build-classpath -DincludeScope=compile -Dmdep.outputFile=target/classpath.txt -q
javac -cp "$(cat target/classpath.txt)" -d target/classes documents/services/EncryptedPasswordGenerator.java
java -cp "target/classes:$(cat target/classpath.txt)" com.portfolio.documents.services.EncryptedPasswordGenerator

*/

public class EncryptedPasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        // Change this to the password you want to encrypt
        String raw = "pratik123";

        // Change this to the password you want to decrypt
        String hash = "$2a$12$Lh6Fvga6kKi/iIRn.uqKueJJ2sTbcGEbmbwW8quaGRxdruARdT23u";

        String encryptedPassword = encrypt(encoder, raw);
        System.out.println(encryptedPassword);

        boolean match = isMatched(encoder, raw, hash);
        System.out.println(match);
    }

    private static String encrypt(BCryptPasswordEncoder encoder, String raw) {
        return encoder.encode(raw);
    }

    private static boolean isMatched(BCryptPasswordEncoder encoder, String raw, String encrypted) {
        return encoder.matches(raw, encrypted);
    }
}