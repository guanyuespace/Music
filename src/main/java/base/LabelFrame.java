package base;

import util.Convert;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;

/**
 * 标签帧
 * FrameID　4　　　　帧标识符的Ascii码，常用标识符的意义见表5
 * Size　　 4　　　　帧内容及编码方式的合计长度，高位在前
 * Flags　　2　　　　标志，只使用了6位，详见表6，一般均＝0
 * encode　 4　　　　帧内容所用的编码方式。许多帧没有此项
 * 帧内容　　　　　　至少 1 个字节
 * <p>
 * flags:  只定义了 6 位,另外的 10 位为 0,但大部分的情况下 16 位都为 0 就可以了。格式如下:
 * abc00000 ijk00000
 *  a -- 标签保护标志,设置时认为此帧作废
 *  b -- 文件保护标志,设置时认为此帧作废
 *  c -- 只读标志,设置时认为此帧不能修改(但我没有找到一个软件理会这个标志)
 *  i -- 压缩标志,设置时一个字节存放两个 BCD 码表示数字
 *  j -- 加密标志(没有见过哪个 MP3 文件的标签用了加密)
 *  k -- 组标志,设置时说明此帧和其他的某帧是一组
 */
public class LabelFrame {
    public int total_Size;
    private String frameId;
    private int size;
    private String flags;
    private byte[] data;

    private static int calcSize(byte[] bytes, int offset, int length) {
        int ret = (bytes[offset + length - 1] & 0xff) + ((bytes[offset + length - 2] & 0xff) << 8) + ((bytes[offset + length - 3] & 0xff) << 16) + ((bytes[offset] & 0xff) << 24);
        return ret;
    }

    public static LabelFrame parse(SeekableByteChannel seekableByteChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        seekableByteChannel.read(byteBuffer);
        byte[] bytes = new byte[10];
        bytes = byteBuffer.array();
        LabelFrame result = new LabelFrame();
        result.frameId = new String(bytes, 0, 4, Charset.forName("ASCII"));
        result.size = calcSize(bytes, 4, 4);
        result.flags = Convert.convertByte2HexStr(bytes, 8, 2);
        byteBuffer.clear();


        byteBuffer = ByteBuffer.allocate((int) result.size);
        seekableByteChannel.read(byteBuffer);
        result.data = new byte[(int) result.size];
        result.data = byteBuffer.array();

        result.total_Size = 10 + result.data.length;
        return result;
    }

    @Override
    public String toString() {
//        if (frameId.trim().equals("")) return "";
//        if (data.length == 0) return "";
//        String desp = frameId.equals("COMM") ? new String(data, Charset.forName("ASCII")) : Convert.convertByte2HexStr(data);
        return "LabelFrame{" +
                "frameId='" + frameId + '\'' +
                "total_Size=" + total_Size +
                ", size=" + size +
                ", flags='" + flags + '\'' +
                ", data=" + Convert.convertByte2HexStr(data) +
                '}';
    }


    public boolean isZero() {
        return total_Size == 10;
    }

}
