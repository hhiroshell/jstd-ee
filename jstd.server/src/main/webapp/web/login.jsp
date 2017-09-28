<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="jp.gr.java_conf.hhiroshell.jstd.server.sample.servlet.LoginUtil" %>
<%
    String username = LoginUtil.getUserName(request, response, false);
    if (username != null && !username.isEmpty()) {
        response.sendRedirect("./index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet/less" type="text/css" href="./style.less">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/less.js/2.7.2/less.min.js"></script>
    <title>ログイン</title>
</head>

<body>
    <form action="./login" method="post">
        <h1>Sign In to your account...</h1>
        <input type="username" name="username" value="" placeholder="Username"/>
        <input type="password" name="password" value="" placeholder="Password"/>
        <button type="submit">Sign In</button>
    </form>
</body>
</html>
