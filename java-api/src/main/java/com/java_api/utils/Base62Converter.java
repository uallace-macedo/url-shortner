package com.java_api.utils;

public class Base62Converter {
    private static final Long baseNumber = 14_776_336L;
    private static final String b62 = "w8N1D7zkYp5L9vM4uG2tRjH6fS3dA0sQeWqErbVcXyZxiToUuIomKLPBJChngF";

    public static String GenerateShortURL(Long genID) {
        long id = baseNumber + genID;
        StringBuilder s = new StringBuilder();
        while(id > 0) {
            int idx = (int) (id % 62);
            s.append(b62.charAt(idx));
            id /= 62;
        }

        return s.reverse().toString();
    }
}
