package base;

import util.Convert;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * ID3V2
 * data
 * ID3V1
 * extra
 */
public class MP3 {
    private static final int HEAD_LENGTH = 10;
    private static final int ID3V1_LENGTH = 128;
    private static final int NONE_LABEL = 10;
    private static int END_OFFSET = 0;

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        SeekableByteChannel seekableByteChannel = null;
        try {
            seekableByteChannel = Files.newByteChannel(Paths.get("res/hello.mp3"), StandardOpenOption.READ);
            System.out.println("hasID3V1 : " + hasID3V1());

            ByteBuffer byteBuffer = ByteBuffer.allocate(HEAD_LENGTH);
            seekableByteChannel.position(0);
            int temp = seekableByteChannel.read(byteBuffer);
            if (temp != HEAD_LENGTH) throw new Exception("Not enough bytes read");
            ID3V2 id3V2 = ID3V2.parse(byteBuffer.array());
            System.out.printf("ID3V2 TAG: %s\n", id3V2.toString());
            if (id3V2.hasExtended()) throw new Exception("can't handle extended header !");
            END_OFFSET += HEAD_LENGTH;

            seekableByteChannel.position(END_OFFSET);
            while (END_OFFSET != id3V2.getSize() + HEAD_LENGTH) {
                LabelFrame labelFrame = LabelFrame.parse(seekableByteChannel);
                System.out.println("" + labelFrame);
                END_OFFSET += labelFrame.total_Size;
                if (labelFrame.total_Size == NONE_LABEL) break;
            }
            END_OFFSET = id3V2.getSize() + HEAD_LENGTH;
            seekableByteChannel.position(END_OFFSET);
            byteBuffer.clear();

            byteBuffer = ByteBuffer.allocate(4 + 32);
            seekableByteChannel.read(byteBuffer);
            System.out.println("data:\n\t" + Convert.convertByte2HexStr(byteBuffer.array()));
            byteBuffer.clear();

            byteBuffer = ByteBuffer.allocate(4);
            seekableByteChannel.read(byteBuffer);
            System.out.println("\t" + new String(byteBuffer.array(), Charset.forName("ASCII")));

            byteBuffer.clear();
            seekableByteChannel.read(byteBuffer);
            System.out.println("\t" + parseFrameLength(byteBuffer.array()));

            byteBuffer.clear();
            seekableByteChannel.read(byteBuffer);
            System.out.println("\t" + parseFrameLength(byteBuffer.array()));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (seekableByteChannel != null && seekableByteChannel.isOpen()) {
                try {
                    seekableByteChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int parseFrameLength(byte[] array) {
        return (array[0] << 24) + (array[1] << 16) + (array[2] << 8) + array[3];
    }

    private static boolean hasID3V1() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(ID3V1_LENGTH);
        SeekableByteChannel seekableByteChannel = Files.newByteChannel(Paths.get("res/hello.mp3"), StandardOpenOption.READ);
        long FILE_LENGTH = seekableByteChannel.size();
        seekableByteChannel.position(FILE_LENGTH - ID3V1_LENGTH);

        int bytesRead = seekableByteChannel.read(byteBuffer);
        if (bytesRead != ID3V1_LENGTH) throw new Exception("Not enough bytes read");
        return hasID3V1Bytes(byteBuffer.array());
    }

    private static boolean hasID3V1Bytes(byte[] buffer) {
        if (!(new String(buffer, 0, 3, Charset.forName("ASCII")).equals("TAG"))) return false;
        // other defination ...
        return true;
    }

}
