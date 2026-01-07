<%@ page import="com.chs.domain.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>动态</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/update.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>
</head>
<body>
<form>
    <c:if test="${empty updates}">
        <p style="text-align: center;color: gray">
            <br><br>
            该用户暂无更新...
        </p>
    </c:if>
    <c:forEach items="${updates}" var="update">
        <br>
        <div class="main">
            <div class="piece">
                <div>
                    <img id="head" src="${pageContext.request.contextPath}/profile_pics/${update.profile_picture}"
                         alt="头像" class="head">
                    <br><br>
                </div>
                <div>
                    <label class="update-label">${update.nickname}</label>
                </div>
            </div>
            <img id="pic" src="${pageContext.request.contextPath}/images/${update.file}"
                 alt="表情包" class="pic"><br><br>
            <label class="time">${update.upload_time}</label><br>
        </div>
        <br>
    </c:forEach>
</form>
</body>
</html>
