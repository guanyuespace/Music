package base;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class Lyric {
    public String kalaokLyric;
    @SerializedName("kalaokVersion")
    public int version;
    @SerializedName("lyric")
    public String lyricStr;
    @SerializedName("musicId")
    public int id;
    public int lyricVersion;

    @Override
    public String toString() {
        return "Lyric{" +
                "kalaokLyric='" + kalaokLyric + '\'' +
                ", version=" + version +
                ", lynic='" + lyricStr + '\'' +
                ", id=" + id +
                ", lyricVersion=" + lyricVersion +
                '}';
    }

    public Lyric() {
    }
}
