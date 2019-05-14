package base;

public class Config {
    private long duration;
    private long size;
    private String filemd5;
    private int version;
    private String md5;

    @Override
    public String toString() {
        return "Config{" +
                "duration=" + duration +
                ", size=" + size +
                ", filemd5='" + filemd5 + '\'' +
                ", version=" + version +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
