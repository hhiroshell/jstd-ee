package jp.gr.java_conf.hhiroshell.jstd.server.sample.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Utilities for pages those require access control (e.g. login).
 *
 * @author hhiroshell
 */
public class LoginUtil {

    /**
     * Get user name from current session.
     *
     * If the returned value is null, the request will be redirected to the login form.
     * So be careful of response handling in succeeding codes.
     *
     * Typically in JSP, do "return" immediately after the use of this method like below.
     *
     * <pre>{@code
     * // in JSP.
     * String username = LoginUtil.getUserName(request, response);
     * if (username == null) {
     *     return;
     * }
     * ;
     * }</pre>
     *
     * @param request
     * @param response
     * @return User name of current session.
     * @throws IOException
     */
    public static String getUserName(
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        return getUserName(request, response, true);
    }

    /**
     * Get user name from current session.
     *
     * You can specify if the request will be redirected or not when the return value is null.
     * If you set "true" for the parameter "redirect", follow the notes of the method that can't
     * be specified the redirection behavior.
     *
     * @param request
     * @param response
     * @param redirect
     * @return User name of current session.
     * @throws IOException
     */
    public static String getUserName(
            HttpServletRequest request, HttpServletResponse response, boolean redirect)
            throws IOException {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null || username.isEmpty()) {
            if (redirect) {
                response.sendRedirect("./login.jsp");
            }
            return null;
        }
        return username;
    }

    public static String authenticate(String username, String password) {
        if ("password".equals(password)) {
            return username;
        }
        return null;
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("./index.jsp");
    }

}
