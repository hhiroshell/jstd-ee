<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="jp.gr.java_conf.hhiroshell.jstd.server.sample.servlet.LoginUtil" %>
<%
    java.util.Date nowTime = new java.util.Date();
    String username = LoginUtil.getUserName(request, response);
    if (username == null) {
        return;
    }
%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet/less" type="text/css" href="style.less">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/less.js/2.7.2/less.min.js"></script>
    <title>インデックス</title>
</head>

<body>
    <form action="./logout" method="post">
        <h1>Welcome <strong><%= username %></strong> !</h1>
        <h2><%= nowTime %></h2>
        <button type="submit">Sign Out</button>
    <form>
</body>
</html>