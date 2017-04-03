package guda.mvcx.core.util;

import io.vertx.core.json.JsonObject;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by well on 2017/3/18.
 */
public class JsonConfigUtil {

    public static final String freemarkerToolKey="freemarker.tool";

    public static final String serverKey="server";

    public static final String templateDirKey="template.dir";
    public static final String assetsDirKey="assets.dir";

    public static final String cookieKey="cookie";
    public static final String cookieExcludeKey="excludePath";

    public static final String authKey="auth";
    public static final String envKey="env";
    public static final String authExcludeKey="excludePath";
    public static final String usePageAuthKey="usePageAuth";
    public static final String pageAuthFailUrlKey="pageAuthFailUrl";

    public static final String extendsKey="_extends";

    public static final String assetsVersionKey ="asserts.version";

    public static JsonObject getConfig() {
        return getConfig("config.json");

    }

    public static JsonObject getConfig(Class clazz) {
        return getConfig(clazz,"config.json");

    }

    public static JsonObject getConfig(String resourceName) {
        return getConfig(JsonConfigUtil.class,resourceName);
    }

    public static JsonObject getConfig(Class clazz,String resourceName) {
        InputStream resourceAsStream = clazz.getClassLoader().getResourceAsStream(resourceName);
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
