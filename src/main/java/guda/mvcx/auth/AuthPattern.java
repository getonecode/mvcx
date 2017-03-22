package guda.mvcx.auth;





import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by well on 15/10/15.
 */
public  class  AuthPattern {

    private final String patternName;
    private final Pattern pattern;

    public AuthPattern(String patternName) {
        this.patternName = normalizePatternName(patternName);
        this.pattern = compilePattern(this.patternName);
    }

    protected String normalizePatternName(String patternName) {
        patternName = StringUtils.trim(patternName);
        // 对于相对路径，自动在前面加上/，变成绝对路径。
        if (!patternName.startsWith("/")) {
            patternName = "/" + patternName;
        }

        return patternName;
    }

    public static Pattern compilePattern(String pattern) throws PatternSyntaxException {
        return Pattern.compile(PathNameWildcardCompiler.pathNameToRegex(pattern, PathNameWildcardCompiler.FORCE_MATCH_PREFIX | PathNameWildcardCompiler.FORCE_ABSOLUTE_PATH));
    }

    public String getPatternName() {
        return patternName;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Matcher matcher(String s) {
        return pattern.matcher(s == null ? "" : s);
    }

    @Override
    public int hashCode() {
        return 31 + patternName.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        return patternName.equals(((AuthPattern) other).patternName);
    }

    @Override
    public String toString() {
        return patternName;
    }
}
