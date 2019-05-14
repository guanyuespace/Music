package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */
public class Convert {
    public static void convert() {
        try {
            FileOutputStream outputStream = new FileOutputStream("D:/IdeaProjects/Music/res/65538.mp3");
            FileInputStream inputStream = new FileInputStream("D:/IdeaProjects/Music/res/65538.mp3.uc!");
            byte[] bytes = new byte[1024];
            int size = 0;
            while ((size = inputStream.read(bytes)) != -1) {
                covertBytes(bytes, size);
                outputStream.write(bytes, 0, size);
                outputStream.flush();
            }

            if (outputStream != null)
                outputStream.close();
            if (inputStream != null)
                inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void covertBytes(byte[] bytes, int size) {
        for (int i = 0; i < size; i++) {
            bytes[i] ^= 0xa3;
        }
    }


    public static String convertByte2HexStr(byte[] bytes) {
        return convertByte2HexStr(bytes, 0, bytes.length);
    }


    public static String convertByte2HexStr(byte[] bytes, int offset, int length) {
        StringBuilder stringBuilder = new StringBuilder(2 * length);
        String str = "";
        for (int i = offset; i < offset + length; i++) {
            int temp = bytes[i] & 0xff;
            str = Integer.toHexString(temp).toUpperCase();
            if (str.length() == 1)
                stringBuilder.append("0" + str);
            else
                stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        convert();
    }
}
