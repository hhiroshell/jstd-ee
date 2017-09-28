package jp.gr.java_conf.hhiroshell.jstd.server.sample.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Optional;

class UpgradeRedirectUrlResponse extends HttpServletResponseWrapper {

    private static final String containerRoot =
            Optional.ofNullable(System.getenv("ORA_APP_PUBLIC_URL"))
                    .orElse("https://localhost:7001") + "/";

    private String currentDirUrl;

    public UpgradeRedirectUrlResponse(HttpServletRequest request, HttpServletResponse response) {
        super(response);
        String requestUri = request.getRequestURI();
        if (requestUri.lastIndexOf("/") == 0) {
            currentDirUrl = containerRoot;
        } else {
            currentDirUrl = containerRoot +
                    requestUri.substring(1, requestUri.lastIndexOf("/") + 1);
        }
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        if (location == null) {
            // This request will end up with NullPointerException inside of super.sendRedirect().
            super.sendRedirect(location);
        } else if (isAbsolute(location)) {
            // No need to edit the location.
            super.sendRedirect(location);
        } else if (location.startsWith("/")) {
            // The location is "/" or "/something". These mean paths from container's root.
            super.sendRedirect(containerRoot + location.substring(1));
        } else {
            // The location starts with "../" or "./" or some other characters.
            super.sendRedirect(currentDirUrl + location);
        }
    }

    private boolean isAbsolute(String url) {
        url = url.toLowerCase();
        return (url.startsWith("http://") || url.startsWith("https://"));
    }

}
