package util;

import base.Config;
import base.Lyric;
import base.MP3;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ParseUtils {
    private final static String TAG = "ParseUtils";
    private static Logger logger = Logger.getLogger(TAG);

    public static Lyric parseLyric() {
        try {
            BufferedReader fileReader = Files.newBufferedReader(Paths.get("D:/IdeaProjects/Music/res/65538"), Charset.forName("UTF-8"));//
            String tempLine = "";
            StringBuilder builder = new StringBuilder(1024);
            while ((tempLine = fileReader.readLine()) != null) {
                builder.append(tempLine);
            }
            if (fileReader != null)
                fileReader.close();
            logger.info(builder.toString());
            return new Gson().fromJson(builder.toString(), Lyric.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        logger.info(ParseUtils.parseLyric().toString());
        logger.info(ParseUtils.parseConfig().toString());
        logger.info(ParseUtils.parseMp3().toString());
    }

    private static MP3 parseMp3() {
        return null;
    }

    private static Config parseConfig() {
        try {
            BufferedReader fileReader = Files.newBufferedReader(Paths.get("D:/IdeaProjects/Music/res/65538.mp3.idx"), Charset.forName("UTF-8"));//
            String tempLine = "";
            StringBuilder builder = new StringBuilder(1024);
            while ((tempLine = fileReader.readLine()) != null) {
                builder.append(tempLine);
            }
            if (fileReader != null)
                fileReader.close();
            logger.info(builder.toString());
            return new Gson().fromJson(builder.toString(), Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
