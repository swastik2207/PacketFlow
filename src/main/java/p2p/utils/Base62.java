package p2p.utils;

 public class Base62 {
    private static final char[] BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final int BASE = 62;

    // Encode a byte array to a Base62 string
    public static String encode(byte[] input) {
        // Convert byte array to a big integer
        java.math.BigInteger value = new java.math.BigInteger(1, input); // Unsigned
        StringBuilder sb = new StringBuilder();

        while (value.compareTo(java.math.BigInteger.ZERO) > 0) {
            java.math.BigInteger[] divmod = value.divideAndRemainder(java.math.BigInteger.valueOf(BASE));
            sb.append(BASE62_ALPHABET[divmod[1].intValue()]);
            value = divmod[0];
        }

        // Add leading zeroes
        for (byte b : input) {
            if (b == 0) sb.append(BASE62_ALPHABET[0]);
            else break;
        }

        return sb.reverse().toString(); // reverse to correct order
    }

    // Decode a Base62 string back to a byte array
    public static byte[] decode(String input) {
        java.math.BigInteger value = java.math.BigInteger.ZERO;
        for (char c : input.toCharArray()) {
            int digit = indexOfChar(c);
            if (digit == -1) throw new IllegalArgumentException("Invalid character in Base62: " + c);
            value = value.multiply(java.math.BigInteger.valueOf(BASE)).add(java.math.BigInteger.valueOf(digit));
        }

        byte[] bytes = value.toByteArray();

        // Remove sign byte if present
        if (bytes.length > 1 && bytes[0] == 0) {
            byte[] tmp = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, tmp, 0, tmp.length);
            bytes = tmp;
        }

        return bytes;
    }

    private static int indexOfChar(char c) {
        for (int i = 0; i < BASE62_ALPHABET.length; i++) {
            if (BASE62_ALPHABET[i] == c) return i;
        }
        return -1;
    }
}

