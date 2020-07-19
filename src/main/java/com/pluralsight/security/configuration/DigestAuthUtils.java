//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.pluralsight.security.configuration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

final class DigestAuthUtils {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    DigestAuthUtils() {
    }

    static String encodePasswordInA1Format(String username, String realm, String password) {
        String a1 = username + ":" + realm + ":" + password;
        return md5Hex(a1);
    }

    static String[] splitIgnoringQuotes(String str, char separatorChar) {
        if (str == null) {
            return null;
        } else {
            int len = str.length();
            if (len == 0) {
                return EMPTY_STRING_ARRAY;
            } else {
                List<String> list = new ArrayList();
                int i = 0;
                int start = 0;
                boolean match = false;

                while(true) {
                    while(i < len) {
                        if (str.charAt(i) == '"') {
                            ++i;

                            while(i < len) {
                                if (str.charAt(i) == '"') {
                                    ++i;
                                    break;
                                }

                                ++i;
                            }

                            match = true;
                        } else if (str.charAt(i) == separatorChar) {
                            if (match) {
                                list.add(str.substring(start, i));
                                match = false;
                            }

                            ++i;
                            start = i;
                        } else {
                            match = true;
                            ++i;
                        }
                    }

                    if (match) {
                        list.add(str.substring(start, i));
                    }

                    return (String[])list.toArray(new String[list.size()]);
                }
            }
        }
    }

    static String generateDigest(boolean passwordAlreadyEncoded, String username, String realm, String password, String httpMethod, String uri, String qop, String nonce, String nc, String cnonce) throws IllegalArgumentException {
        String a2 = httpMethod + ":" + uri;
        String a2Md5 = md5Hex(a2);
        String a1Md5;
        if (passwordAlreadyEncoded) {
            a1Md5 = password;
        } else {
            a1Md5 = encodePasswordInA1Format(username, realm, password);
        }

        String digest;
        if (qop == null) {
            digest = a1Md5 + ":" + nonce + ":" + a2Md5;
        } else {
            if (!"auth".equals(qop)) {
                throw new IllegalArgumentException("This method does not support a qop: '" + qop + "'");
            }

            digest = a1Md5 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + a2Md5;
        }

        return md5Hex(digest);
    }

    static Map<String, String> splitEachArrayElementAndCreateMap(String[] array, String delimiter, String removeCharacters) {
        if (array != null && array.length != 0) {
            Map<String, String> map = new HashMap();
            String[] var4 = array;
            int var5 = array.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String s = var4[var6];
                String postRemove;
                if (removeCharacters == null) {
                    postRemove = s;
                } else {
                    postRemove = StringUtils.replace(s, removeCharacters, "");
                }

                String[] splitThisArrayElement = split(postRemove, delimiter);
                if (splitThisArrayElement != null) {
                    map.put(splitThisArrayElement[0].trim(), splitThisArrayElement[1].trim());
                }
            }

            return map;
        } else {
            return null;
        }
    }

    static String[] split(String toSplit, String delimiter) {
        Assert.hasLength(toSplit, "Cannot split a null or empty string");
        Assert.hasLength(delimiter, "Cannot use a null or empty delimiter to split a string");
        if (delimiter.length() != 1) {
            throw new IllegalArgumentException("Delimiter can only be one character in length");
        } else {
            int offset = toSplit.indexOf(delimiter);
            if (offset < 0) {
                return null;
            } else {
                String beforeDelimiter = toSplit.substring(0, offset);
                String afterDelimiter = toSplit.substring(offset + 1);
                return new String[]{beforeDelimiter, afterDelimiter};
            }
        }
    }

    static String md5Hex(String data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var3) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(data.getBytes())));
    }
}
