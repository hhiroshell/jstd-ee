package jp.gr.java_conf.hhiroshell.jstd.server.sample.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This Servlet invalidates current session.
 *
 * @author hhiroshell
 */
@WebServlet("web/logout")
public class LogoutServer extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doIt(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doIt(request, response);
    }

    /**
     * Invalidate the current session, and redirect the request to top of the application.
     * After that, the request will be redirected to login form, because the session is invalidated.
     *
     * As a result, users will see the login form in their browser.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doIt(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect("./index.jsp");
    }

}
