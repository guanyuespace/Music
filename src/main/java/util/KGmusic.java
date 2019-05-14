package util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/// <summary>
/// 酷狗缓存解密
/// </summary>
public class KGmusic {


    public static void main(String[] args) {
        String filename = "./res/kg.kgtemp";
        if (isSupported(filename)) {
            File dest = new File("./res/kg.music");
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                fileInputStream = new FileInputStream(filename);
                fileOutputStream = new FileOutputStream(dest);
                byte[] bytes = new byte[1024];// 1024=4*2^8
                int size = 0;
                while ((size = fileInputStream.read(bytes)) != -1) {
                    covertBytes(bytes, 0, size);
                    fileOutputStream.write(bytes, 0, size);
                    fileOutputStream.flush();
                }
            } catch (IOException e) {
                dest.delete();
                e.printStackTrace();
            } finally {
                if (fileOutputStream == null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private static void covertBytes(byte[] bytes, int base, int offset) {
        byte[] key = new byte[]{(byte) 0xac, (byte) 0xec, (byte) 0xdf, (byte) 0x57};//6c 2c 2f 27
        for (int i = base; i < offset; i++) {
            int k = key[i % key.length];
            byte keyHigh = (byte) (k >> 4);
            byte keyLow = (byte) (k & 0xf);
            byte encryptionData = bytes[i];
            byte low = (byte) (encryptionData & 0xf ^ keyLow);//解密后的低4位
            byte high = (byte) ((encryptionData >> 4) ^ keyHigh ^ low & 0xf);//解密后的高4位
            bytes[i] = (byte) (high << 4 | low);
        }
    }

    private static boolean isSupported(String filename) {
        return filename.endsWith(".kgtemp");
    }
}
