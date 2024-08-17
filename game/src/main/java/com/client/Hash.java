package com.client;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author Arthur Behesnilian 11:51 PM
 */
public class Hash {

    private static int getTrailingZeroBits(byte[] bigNumber) {
        int bits = 0;
        for (byte var4 : bigNumber) {
            int n = getTrailingZeroBits(var4);
            bits += n;
            if (n != 8) {
                break;
            }
        }
        return bits;
    }

    private static int getTrailingZeroBits(byte v) {
        if (v == 0) {
            return 8;
        }
        int bits = 0;
        int t = v & 255;
        while ((t & 128) == 0) {
            bits++;
            t <<= 1;
        }
        return bits;
    }

    public static long run(int unknown, int difficultyBits, String seed) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String prefix = Integer.toHexString(unknown) + Integer.toHexString(difficultyBits) + seed;
            long answer = 0L;
            while (true) {
                String str2 = prefix + Long.toHexString(answer);
                md.reset();
                md.update(str2.getBytes(StandardCharsets.UTF_8));
                byte[] digest = md.digest();
                //complexity == 16: if(digest[0] == 0 && digest[1] == 0) break;
                if (getTrailingZeroBits(digest) >= difficultyBits) {
                    break;
                }
                answer++;
            }
            return answer;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
