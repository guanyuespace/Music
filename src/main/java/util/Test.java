package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        File file=new File("./res/kg.kgtemp");
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        for (int i = 0; i < 1097; i++) {
            fileOutputStream.write(0x0);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
