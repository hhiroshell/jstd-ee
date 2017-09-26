package jp.gr.java_conf.hhiroshell.jstd.server.sample.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This Servlet verifies credentials sent from login form,
 * and marks the session as authenticated.
 *
 * @author hhiroshell
 */
@WebServlet("/login")
public class LoginServer extends HttpServlet {

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
     * Handle GET/POST requests.
     *
     * This Method verifies credentials sent from login form,
     * and marks the session as authenticated.
     *
     * If a verification fails, the request will be redirected to login form.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doIt(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String authenticatedUser = null;
        if (username != null && !username.isEmpty()) {
            authenticatedUser = authenticate(username, password);
        }
        if (authenticatedUser == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("./login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        request.getSession().setAttribute("username", authenticatedUser);
        response.sendRedirect("./index.jsp");
    }

    private String authenticate(String username, String password) {
        if ("password".equals(password)) {
            return username;
        }
        return null;
    }

}
