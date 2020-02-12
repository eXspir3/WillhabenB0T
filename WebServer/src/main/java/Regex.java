import java.util.regex.Pattern;


public class Regex {
    static String EQUAL = "=";
    static String FIRST_QUESTION_MARK = "\\?";
    static String VALID_PARAM = "\\w+[=]\\w+";

    static Pattern getPattern(String regex) {
        return Pattern.compile(regex);
    }
}
