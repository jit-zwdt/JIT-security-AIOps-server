package com.jit.server.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description:获取验证码
 * @Author: zengxin_miao
 * @Date: 2020/08/25
 */
public class RandomUtil {
    public static final String BASE_CHAR_NUMBER = "QWERTYUPLKJHGFDSAZXCVBNMqwertyupkjhgfdsazxcvbnm123456789";

    public RandomUtil() {
    }

    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    public static String randomString(int length) {
        return randomString(BASE_CHAR_NUMBER, length);
    }

    public static String randomString(String baseString, int length) {
        StringBuilder sb = new StringBuilder();
        if (length < 1) {
            length = 1;
        }

        int baseLength = baseString.length();

        for (int i = 0; i < length; ++i) {
            int number = getRandom().nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }

        return sb.toString();
    }


    /**
     * @deprecated
     */
    @Deprecated
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

}
