package guda.mvcxdemo;

import io.vertx.core.json.JsonObject;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by well on 2017/3/18.
 */
public class JsonConfigUtil {

    public static JsonObject getConfig() {
        InputStream resourceAsStream = JsonConfigUtil.class.getClassLoader().getResourceAsStream("config.json");

        if (resourceAsStream == null) {
            throw new IllegalStateException("Cannot find config.json on classpath");
        }
        try {
            Scanner scanner = (new Scanner(resourceAsStream, "UTF-8")).useDelimiter("\\A");
            String scanString = scanner.hasNext() ? scanner.next() : "";
            return new JsonObject(scanString);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;

    }
}
