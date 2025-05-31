package com.net;

public class inviteCode {
    private static final char[] INVITE_CHARS = "2NTUR0zErbqILs5QxmeBvDA9WXyCnZpYc+MtwgKaFhJHOoSfGdV3jkil147Pu68-".toCharArray();
    
    public static int ipToInt(String ip) {
        String[] parts = ip.split("\\.");
        int result = 0;
        for (String part : parts) {
            result = (result << 8) | Integer.parseInt(part);
        }
        return result;
    }

    public static String encodeInviteCode(String ip) {
        int ipInt = ipToInt(ip);
        char[] code = new char[6];

        for (int i = 5; i >= 0; i--) {
            code[i] = INVITE_CHARS[ipInt & 0b111111]; // 取6 bits
            ipInt >>= 6;
        }
        return new String(code);
    }

    public static String decodeInviteCode(String code) {
        int ipInt = 0;
        for (int i = 0; i < 6; i++) {
            ipInt <<= 6;
            ipInt |= indexOfChar(code.charAt(i));
        }

        return String.format("%d.%d.%d.%d",
            (ipInt >> 24) & 0xFF,
            (ipInt >> 16) & 0xFF,
            (ipInt >> 8) & 0xFF,
            ipInt & 0xFF
        );
    }

    private static int indexOfChar(char c) {
        for (int i = 0; i < INVITE_CHARS.length; i++) {
            if (INVITE_CHARS[i] == c) return i;
        }
        throw new IllegalArgumentException("Invalid character in invite code: " + c);
    }
}

