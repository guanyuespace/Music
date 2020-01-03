package util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Convert {
    private static void convertMusic(String src, String dest) {
        try (FileInputStream inputStream = new FileInputStream(src);
             FileOutputStream outputStream = new FileOutputStream(dest)) {
            byte[] bytes = new byte[1024];
            int size = 0;
            while ((size = inputStream.read(bytes)) != -1) {
                covertBytes(bytes, size);
                outputStream.write(bytes, 0, size);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void covertBytes(byte[] bytes, int size) {
        for (int i = 0; i < size; i++) {
            bytes[i] ^= 0xa3;
        }
    }

    @Deprecated
    public static String convertByte2HexStr(byte[] bytes) {
        return convertByte2HexStr(bytes, 0, bytes.length);
    }

    @Deprecated
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
        String[] sfiles = null;
        File sdir = new File("E:/CloudMusic/Cache/Cache");
        if (sdir.exists() && sdir.isDirectory()) {
            sfiles = sdir.list((File dir, String name) -> {
                if (name.endsWith("uc"))
                    return true;
                return false;
            });
        }

        if (sfiles != null) {
            ExecutorService executorService = Executors.newFixedThreadPool(5);

            for (int i = 0; i < sfiles.length; i++) {
                executorService.execute(new MRun("E:/CloudMusic/Cache/Cache/" + sfiles[i], "E:\\ConvertedMusic\\" + sfiles[i]+".mp3"));
            }

            executorService.shutdown();
        }
    }

    static class MRun implements Runnable {
        String sname, dname;


        public MRun(@NotNull String sname, @NotNull String dname) {
            this.sname = sname;
            this.dname = dname;
        }

        @Override
        public void run() {
            convertMusic(sname, dname);
        }
    }

}
