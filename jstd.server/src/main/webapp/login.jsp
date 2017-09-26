<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="jp.gr.java_conf.hhiroshell.jstd.server.sample.servlet.LoginUtil" %>
<%
    String authenticatedUser = LoginUtil.getUserName(request, response, false);
    // ログイン済み
    if (authenticatedUser != null && !authenticatedUser.isEmpty()) {
        response.sendRedirect("./index.jsp");
        return;
    }
    // 未ログイン
    if ("POST".equals(request.getMethod())) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username != null && !username.isEmpty()) {
            authenticatedUser = LoginUtil.authenticate(username, password);
        }
        if (authenticatedUser == null) {
            response.sendRedirect("./login.jsp");
            return;
        }
        request.getSession().setAttribute("username", authenticatedUser);
        response.sendRedirect("./index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet/less" type="text/css" href="./style.less">
    <script src="http://cdnjs.cloudflare.com/ajax/libs/less.js/2.5.1/less.min.js"></script>
    <title>ログイン</title>
</head>

<body>
    <form  method="post">
        <h1>Sign In to your account...</h1>
        <input type="username" name="username" value="" placeholder="Username"/>
        <input type="password" name="password" value="" placeholder="Password"/>
        <button type="submit">Sign In</button>
    </form>
</body>
</html>
