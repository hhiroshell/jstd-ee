package jp.gr.java_conf.hhiroshell.jstd.server.sample.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "web/*")
public class UpgradeRedirectUrlFilter implements Filter {

    private static final boolean upgradeUrl =
            Boolean.valueOf(System.getenv("JSTD_UPGRADE_REDIRECT_URL"));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing.
    }

    @Override
    public void destroy() {
        // do nothing.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (upgradeUrl) {
            response = new UpgradeRedirectUrlResponse(
                    (HttpServletRequest)request, (HttpServletResponse)response);
        }
        chain.doFilter(request, response);
    }

}
