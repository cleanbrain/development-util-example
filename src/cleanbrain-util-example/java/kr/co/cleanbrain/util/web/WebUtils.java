package kr.co.cleanbrain.util.web;

import kr.co.cleanbrain.util.string.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Company: NANDSOFT
 * User: clean_brain
 * Date: 2022-04-12
 * Time: 오전 10:17
 */
public class WebUtils {


    /**
     * <p>http 주소인 경우 https로 변환</p>
     *
     * @param request
     * @return
     */
    public static String getHttpsUrl(HttpServletRequest request) {
        if (request.isSecure() || "https".equals(request.getHeader("X-Forwarded-Proto"))) {
            ;
        } else {
            String reqUrl = request.getRequestURL().toString();
            Set<String> localSet = new HashSet<>();
            localSet.add("localhost");
            localSet.add("127.0.0.1");

            if (localSet.contains(request.getRemoteHost())) {
                ;
            } else if ("http".equals(reqUrl.substring(0, reqUrl.indexOf("://")))) {
                if (request.getQueryString() == null || request.getQueryString().length() == 0) {
                    return "redirect:" + "https".concat(reqUrl.substring(reqUrl.indexOf("://")));
                } else {
                    return "redirect:" + "https".concat(reqUrl.substring(reqUrl.indexOf("://"))) + "?" + request.getQueryString();
                }
            }
        }

        return null;
    }

    /**
     * <p>접근 가능한 IP 인지 확인</p>
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static boolean isAccessibleIp(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        List<String> accessControlList = new ArrayList<>();
        accessControlList.add("192.168.0.2");
        accessControlList.add("192.168.0.3");
        accessControlList.add("192.168.0.4");
        Set<String> accessControlSet = new HashSet<String>();

        if (accessControlList != null && accessControlList.size() > 0) {
            for (String accessControlUrl : accessControlList) {
                accessControlSet.add(accessControlUrl);
            }
        }

        return accessControlSet.contains(ip);
    }

    /**
     * <p>접속한 사용자 IP 확인</p>
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * <p>웹 취약점 점검 조치(XSS, 검증되지 않은 리다이렉트와 포워드)</p>
     *
     * @param returnUrl
     * @param doReEncode
     * @return
     * @throws IOException
     */
    public static String getSecurityReturnUrl(String returnUrl, boolean doReEncode) throws IOException {
        String mainUrl = "https://www.naver.com";

        returnUrl = URLDecoder.decode(returnUrl, "UTF-8");

        // XSS 조치
        returnUrl = preventXss(returnUrl,
                mainUrl,
                StringUtils.asSet(StringUtils.split("> < javascript:", " ")));

        // 검증되지 않은 리다이렉트와 포워드 조치
        returnUrl = preventNotVerificatedRedirectAndForward(returnUrl,
                mainUrl,
                StringUtils.asSet(StringUtils.split("^(\\\\S+\\\\.|)(naver)\\\\.com$", " ")),
                StringUtils.asSet(StringUtils.split("naver.com", " ")));

        if (doReEncode) {
            return URLEncoder.encode(returnUrl, "UTF-8");
        } else {
            return returnUrl;
        }
    }

    /**
     * <p>
     * 웹취약점(XSS) 조치
     * returnUrl에 허용되지 않는 문자열 포함시 defaultReturnUrl 반환
     * </p>
     *
     * @param returnUrl
     * @param defaultReturnUrl
     * @param notAllowedStrings
     * @return
     */
    private static String preventXss(String returnUrl, String defaultReturnUrl, Set<String> notAllowedStrings) {
        boolean isReturnable = true;

        for (String notAllowedString : notAllowedStrings) {
            if (returnUrl.contains(notAllowedString)) {
                isReturnable = false;
                break;
            }
        }

        if (isReturnable)
            return returnUrl;
        else
            return defaultReturnUrl;
    }

    /**
     * <p>
     * 웹취약점(검증되지 않은 리다이렉트와 포워드) 조치
     * 허용된 도메인 정규식에 부합하거나 허용된 도메인에 해당되면 returnUrl 반환
     * 그렇지 않으면 defaultReturnUrl 반환
     * </p>
     *
     * @param returnUrl
     * @param defaultReturnUrl
     * @param allowDomainRegexps
     * @param allowDomains
     * @return
     */
    private static String preventNotVerificatedRedirectAndForward(String returnUrl, String defaultReturnUrl, Set<String> allowDomainRegexps, Set<String> allowDomains) {
        boolean isReturnable = false;

        for (String regexp : allowDomainRegexps) {
            if (getDomain(returnUrl).matches(regexp)) {
                isReturnable = true;
                break;
            }
        }

        if (!isReturnable) {
            for (String allowDomain : allowDomains) {
                if (getDomain(returnUrl).equals(allowDomain)) {
                    isReturnable = true;
                    break;
                }
            }
        }

        if (isReturnable)
            return returnUrl;
        else
            return defaultReturnUrl;
    }

    /**
     * <p>URL에서 Domain만 추출</p>
     *
     * @param url
     * @return
     */
    private static String getDomain(String url) {
        // 프로토콜 제거
        if (url.indexOf("://") > -1) {
            url = url.substring(url.indexOf("://") + 3);
        }

        // 도메인 하위 경로 제거
        if (url.indexOf("/", 0) > -1) {
            url = url.substring(0, url.indexOf("/"));
        }

        // 포트 제거
        if (url.lastIndexOf(":") > -1) {
            url = url.substring(0, url.lastIndexOf(":"));
        }

        return url;
    }

}
