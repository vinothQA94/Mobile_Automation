package com.browserstack.run_first_test;
import org.jboss.aerogear.security.otp.Totp;

public class TOTPGenerator {
    /**
     * Method is used to get the TOTP based on the security token
     * @return
     */
    public static String getTwoFactorCode(String Secret){
        Totp totp = new Totp(Secret); // 2FA secret key
        String twoFactorCode = totp.now(); //Generated 2FA code here
        return twoFactorCode;
    }
}