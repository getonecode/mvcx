package guda.mvcx;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private static Pattern pattern;
    private static List<String> groups=new ArrayList<>();
    private boolean useNormalisedPath;
    private static final Pattern RE_OPERATORS_NO_STAR = Pattern.compile("([\\(\\)\\$\\+\\.])");

    public static void main(String[] args){
        String path=createPatternRegex("/abc/:p/:d");
        System.out.println(path);
        groups.forEach(s -> {
                    System.out.println(s);
                }
        );

        System.out.println(pattern.matcher("/abc/11/dd").find());
        System.out.println(pattern.matcher("/abc/11").find());
        System.out.println(pattern.matcher("/abc").find());
        System.out.println(pattern.matcher("/abc?abc=123").find());
    }


    private static String createPatternRegex(String path) {
        path = RE_OPERATORS_NO_STAR.matcher(path).replaceAll("\\\\$1");
        if(path.charAt(path.length() - 1) == 42) {
            path = path.substring(0, path.length() - 1) + ".*";
        }

        Matcher m = Pattern.compile(":([A-Za-z][A-Za-z0-9_]*)").matcher(path);
        StringBuffer sb = new StringBuffer();
        groups = new ArrayList();

        for(int index = 0; m.find(); ++index) {
            String param = "p" + index;
            String group = m.group().substring(1);
            if(groups.contains(group)) {
                throw new IllegalArgumentException("Cannot use identifier " + group + " more than once in pattern string");
            }

            m.appendReplacement(sb, "(?<" + param + ">[^/]+)");
            groups.add(group);
        }

        m.appendTail(sb);
        path = sb.toString();
        pattern = Pattern.compile(path);
        return path;
    }

}
