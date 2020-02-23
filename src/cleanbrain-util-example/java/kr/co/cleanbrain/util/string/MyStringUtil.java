package kr.co.cleanbrain.util.string;

/**
 * Created with IntelliJ IDEA.
 * Company: NANDSOFT
 * User: 노상현
 * Date: 2020-02-18
 * Time: 오후 2:16
 */
public class MyStringUtil {
    public static String wrapWithQuotationMarks(String str) {
        if (str != null && str.length() != 0) {
            str = "'" + str + "'";
        }
        return str;
    }

    public static String[] wrapWithQuotationMarks(String[] strs) {
        for (int idx = 0; idx < strs.length; idx++) {
            strs[idx] = "'" + strs[idx] + "'";
        }
        return strs;
    }

    public static String wrapWithDoubleQuotationMarks(String str) {
        if (str != null && str.length() != 0) {
            str = "\"" + str + "\"";
        }
        return str;
    }

    public static String[] wrapWithDoubleQuotationMarks(String[] strs) {
        for (int idx = 0; idx < strs.length; idx++) {
            strs[idx] = "\"" + strs[idx] + "\"";
        }
        return strs;
    }
}
