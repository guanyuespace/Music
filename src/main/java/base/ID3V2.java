package base;

import util.Convert;

import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * byte Header[3];   必须为“ID3”否则认为标签不存在
 * byte Ver;      版本号ID3V2.3 就记录3
 * byte Revision;    副版本号此版本记录为0
 * byte Flag;      标志字节，只使用高三位，其它位为0
 * byte Size[4];    标签大小---> 标签帧大小
 * size=后续非数据帧大小
 */
public class ID3V2 {
    private static final Logger logger = Logger.getLogger("ID3V2");
    private static final boolean DBG = false;

    private String header = "";
    private byte ver;
    private byte reVision;
    private byte flag;
    private int size;//每字节首位不计入

    public static ID3V2 parse(byte[] bytes) {
        log(Convert.convertByte2HexStr(bytes));
        ID3V2 result = new ID3V2();
        result.header = new String(bytes, 0, 3, Charset.forName("ASCII"));
        result.ver = bytes[3];
        result.reVision = bytes[4];
        result.flag = bytes[5];
        result.size = calcSize(bytes, 6, 4);
        return result;
    }

    private static void log(String msg) {
        if (DBG)
            logger.log(Level.INFO, msg);
    }

    private static boolean hasID3V2() {
        return true;
    }

    private static int calcSize(byte[] bytes, int offset, int length) {
        log(Convert.convertByte2HexStr(bytes, offset, length));
        int ret = (bytes[offset + length - 1] & 0x7f) + ((bytes[offset + length - 2] & 0x7f) << 7) + ((bytes[offset + length - 3] & 0x7f) << 14) + ((bytes[offset] & 0x7f) << 21);
        return ret;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "ID3V2{" +
                "header=" + header +
                ", ver=" + ver +
                ", reVision=" + reVision +
                ", flag=" + flag +
                ", size=" + size +
                '}';
    }

    public boolean hasExtended() {
        return (flag & 0x40) == 1;
    }
};

class ExtendedHeader {
    int size;
    short flag;
    int size_padding;
}