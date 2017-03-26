package guda.mvcx.core.util;

/**
 * Created by well on 2017/3/26.
 */
public class PatternUtil {

    public static boolean isPattern(String path) {
        return path.indexOf(42) != -1 || path.indexOf(63) != -1;

    }
}
