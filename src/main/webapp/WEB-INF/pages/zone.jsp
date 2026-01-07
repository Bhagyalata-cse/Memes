<%@ page import="com.chs.domain.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>动态</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/zone.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>
    <script src="${pageContext.request.contextPath}/js/zone.js"></script>
</head>
<body>
<header>
    <div class="header-top">
        <div class="container">
            <a href="/">
                <h1>Chs_memes</h1>
            </a>
            <c:if test="${not empty user}">
                <div class="user-menu">
                    <button class="upload-button" onclick="openModal('uploadModal')">上传</button>
                    <img src="${pageContext.request.contextPath}/profile_pics/${user.profile_picture}" alt="${user.nickname}" class="user-avatar" onclick="toggleUserMenu()">
                    <div id="userDropdown" class="user-dropdown">
                        <a href="#" onclick="editProfile()">修改个人资料</a>
                        <a href="${pageContext.request.contextPath}/user/profile?id=${user.id}">查看个人主页</a>
                        <a href="#" onclick="logout()">登出</a>
                    </div>
                </div>
            </c:if>

            <c:if test="${empty user}">
                <div class="login-register-buttons">
                    <button class="upload-button" onclick="promptLogin()">上传</button>
                    <button onclick="openModal('loginModal')">登录</button>
                    <button onclick="openModal('registerModal')">注册</button>
                </div>
            </c:if>
        </div>
    </div>
</header>

<div id="editProfileModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('editProfileModal')">&times;</span>
        <h2>修改个人资料</h2>
        <form id="editProfileForm" enctype="multipart/form-data">
            <div class="form-group">
                <label for="editNickname">昵称</label>
                <input type="text" id="editNickname" name="nickname" required>
            </div>
            <div class="form-group">
                <label for="editPassword">密码</label>
                <input type="text" id="editPassword" name="password" required>
            </div>
            <div class="form-group">
                <label for="editProfilePicture">头像</label>
                <input type="file" id="editProfilePicture" name="profilePicture" accept="image/*">
                <div id="profileImagePreview" class="image-preview-container"></div>
            </div>
            <button type="submit">保存修改</button>
        </form>
    </div>
</div>

<div id="uploadModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('uploadModal')">&times;</span>
        <h2>上传表情包</h2>
        <form id="uploadForm" enctype="multipart/form-data">
            <div class="form-group">
                <label for="memeFile">表情包文件</label>
                <div id="dropZone" class="drop-zone">
                    <p>拖放文件到这里或点击选择文件</p>
                    <input type="file" id="memeFile" name="memeFile" accept="image/*" required>
                </div>
                <div id="imagePreviewContainer" class="image-preview-container"></div>
            </div>
            <div class="form-group">
                <label for="memeName">表情包名字</label>
                <input type="text" id="memeName" name="name" required>
            </div>
            <div class="form-group">
                <label for="memeIntroduction">简要介绍</label>
                <textarea id="memeIntroduction" name="introduction" required></textarea>
            </div>
            <div class="form-group">
                <label for="memeTagInput">标签（最多5个）</label>
                <div class="tag-input-container">
                    <input type="text" id="memeTagInput" placeholder="输入标签并按回车">
                    <div id="tagContainer" class="tag-container"></div>
                </div>
                <input type="hidden" id="memeTags" name="tags">
                <div id="tagSuggestions" class="tag-suggestions"></div>
            </div>
            <div class="progress-bar-container">
                <div id="uploadProgress" class="progress-bar"></div>
            </div>
            <button type="submit">上传</button>
        </form>
    </div>
</div>

<div id="loginModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('loginModal')">&times;</span>
        <h2>登录</h2>
        <form id="loginForm">
            <div class="form-group">
                <label for="loginEmail">邮箱</label>
                <input type="email" id="loginEmail" name="email" required>
            </div>
            <div class="form-group">
                <label for="loginPassword">密码</label>
                <input type="password" id="loginPassword" name="password" required>
            </div>
            <button type="submit">登录</button>
        </form>
    </div>
</div>

<div id="registerModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('registerModal')">&times;</span>
        <h2>注册</h2>
        <form id="registerForm" enctype="multipart/form-data">
            <div class="form-group">
                <label for="nickname">昵称</label>
                <input type="text" id="nickname" name="nickname" required>
            </div>
            <div class="form-group">
                <label for="email">邮箱</label>
                <input type="email" id="email" name="email" required>
                <button type="button" id="getVerificationCode">获取验证码</button>
            </div>
            <div class="form-group">
                <label for="verificationCode">验证码</label>
                <input type="text" id="verificationCode" name="verificationCode" required>
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">确认密码</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <div class="form-group">
                <label for="profile_picture">头像</label>
                <input type="file" id="profile_picture" name="profile_picture" accept="image/*" required>
            </div>
            <button type="submit">注册</button>
        </form>
    </div>
</div>

<form>
    <div class="zone-container">
        <div class="left">
            <button type="reset" value="0" class="zone-button zone-button-class"><br>
                <img id="all" src="${pageContext.request.contextPath}/images/all.png"
                     alt="头像" class="head"><br>
                <label class="zone-label">全部动态</label>
            </button><br>
            <c:forEach items="${followee}" var="follow">
                <button type="reset" value="${follow.id}" class="zone-button zone-button-class"><br>
                    <img id="head" src="${pageContext.request.contextPath}/profile_pics/${follow.profile_picture}"
                         alt="头像" class="head"><br>
                    <label class="zone-label">${follow.nickname}</label>
                </button><br>
            </c:forEach>
        </div>
        <div class="right">
            <iframe id="update" src="${pageContext.request.contextPath}/update/0"></iframe>
        </div>
    </div>
</form>
</body>
</html>
