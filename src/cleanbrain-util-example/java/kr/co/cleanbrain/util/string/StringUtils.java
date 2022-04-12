package kr.co.cleanbrain.util.string;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Company: NANDSOFT
 * User: 노상현
 * Date: 2020-02-18
 * Time: 오후 2:16
 */
public class StringUtils {

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

    public static Set<String> asSet(String... args) {
        Set<String> set = new HashSet<String>();
        for (String arg : args)
            set.add(arg);
        return set;
    }

    public static String[] split(String str, String delim) {
        List<String> list = splitToList(str, delim);
        return list.toArray(new String[list.size()]);
    }

    public static List<String> splitToList(String str, String delim) {
        List<String> list = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(str, delim);
        while (st.hasMoreTokens()) {
            String s = st.nextToken().trim();
            if (s.length() > 0)
                list.add(s);
        }
        return list;
    }

}
