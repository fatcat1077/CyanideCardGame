package com.net;

import java.security.SecureRandom;

public class inviteCode {
    private static final char[] INVITE_CHARS = "2NTUR0zErbqILs5QxmeBvDA9WXyCnZpYc+MtwgKaFhJHOoSfGdV3jkil147Pu68-".toCharArray();
    private static final SecureRandom random = new SecureRandom();

    // 將 IP 轉換為 int
    public static int ipToInt(String ip) {
        String[] parts = ip.split("\\.");
        int result = 0;
        for (String part : parts) {
            result = (result << 8) | Integer.parseInt(part);
        }
        return result;
    }

    // 將 int 轉換為 IP 字串
    public static String intToIp(int ipInt) {
        return String.format("%d.%d.%d.%d",
            (ipInt >> 24) & 0xFF,
            (ipInt >> 16) & 0xFF,
            (ipInt >> 8) & 0xFF,
            ipInt & 0xFF
        );
    }

    // 建立 salt 對應的混淆 key
    private static int saltKey(int salt) {
        return 0xA5A5A5A5 ^ (salt * 0x5A5A5A5A); // 簡單混淆公式
    }

    // 編碼：產生一組邀請碼
    public static String encodeInviteCode(String ip) {
        int ipInt = ipToInt(ip);
        int salt = random.nextInt(64); // 6-bit salt
        int encoded = ipInt ^ saltKey(salt); // 混淆

        char[] code = new char[7];
        code[0] = INVITE_CHARS[salt]; // 第一碼存 salt
        for (int i = 6; i > 0; i--) {
            code[i] = INVITE_CHARS[encoded & 0b111111];
            encoded >>= 6;
        }
        return new String(code);
    }

    // 解碼：還原出原始 IP
    public static String decodeInviteCode(String code) {
        int salt = indexOfCharSafe(code.charAt(0));
        int encoded = 0;
        for (int i = 1; i < 7; i++) {
            encoded <<= 6;
            encoded |= indexOfCharSafe(code.charAt(i));
        }

        int ipInt = encoded ^ saltKey(salt); // 反混淆
        return intToIp(ipInt);
    }

    public static boolean isValidInviteCode(String code) {
    if (code == null || code.length() != 7) {
        return false;
    }
    for (char c : code.toCharArray()) {
        if (indexOfCharSafe(c) == -1) {
            return false;
        }
    }
    return true;
}

    // 查找對應字元的索引值
    private static int indexOfCharSafe(char c) {
        for (int i = 0; i < INVITE_CHARS.length; i++) {
            if (INVITE_CHARS[i] == c) return i;
        }
        return -1;
    }
}
