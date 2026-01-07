<%@ page import="org.springframework.http.ResponseEntity" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>表情包</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/page.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>
    <script src="${pageContext.request.contextPath}/js/page.js"></script>
</head>
<body>
<header>
    <div class="header-top">
        <div class="container">
            <a href="/">
                <h1>Chs_memes</h1>
            </a>
            <c:if test="${not empty user}">
                <input type="hidden" name="user_id" value="${user.id}">
                <div class="user-menu">
                    <button class="upload-button" onclick="openModal('uploadModal')">上传</button>
                    <button onclick="window.location.href='${pageContext.request.contextPath}/zone/${user.id}'">动态</button>
                    <img src="${pageContext.request.contextPath}/profile_pics/${user.profile_picture}"
                         alt="${user.nickname}" class="user-avatar" onclick="toggleUserMenu()">
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

<form action="${pageContext.request.contextPath}/meme/favorite" method="post">
    <c:if test="${not empty user}">
        <input type="hidden" name="user_id" value="${user.id}">
    </c:if>
    <input type="hidden" name="meme_id" value="${meme.id}">
    <div class="page-container">
        <div class="left">
            <div>
                <br>
                <label style="font-size: 25px"><b>${meme.name}</b></label><br><br>
                <img src="${pageContext.request.contextPath}/images/${meme.file}" alt="${meme.name}"
                     style="width: 50%"><br><br>
                <label style="color: gray">${introduction}</label><br>
                <label style="color: gray;font-size: 10px;float: right;margin-right: 15%">views:${meme.views}</label>
            </div>
            <div>
                <c:forEach items="${tags}" var="tag">
                    <button type="button" class="tags">${tag}</button>
                </c:forEach>
            </div>
            <div class="down">
                <div class="down-left">
                    <div>
                        <a href="${pageContext.request.contextPath}/user/profile?id=${memeUser.id}">
                            <img id="head" src="${pageContext.request.contextPath}/profile_pics/${memeUser.profile_picture}"
                                 alt="头像" style="width:60px;height:60px;border-radius: 50%">
                        </a>
                    </div>
                    <div>
                        <a href="${pageContext.request.contextPath}/user/profile?id=${memeUser.id}"
                           style="text-decoration: none;color: black">
                            <label id="username">${memeUser.nickname}</label>
                        </a>
                    </div>
                </div>
                <div class="down-right">
                    <input title="下载" type="button" onclick="download('${meme.file}')"
                           style="background-image: url('${pageContext.request.contextPath}/static/images/download.png')"
                           class="button">
                    <c:if test="${not empty user}">
                        <c:if test="${favorite==false}">
                            <input id="collect" title="收藏" type="submit"
                                   style="background-image: url('${pageContext.request.contextPath}/static/images/love1.png')"
                                   class="button">
                        </c:if>
                        <c:if test="${favorite}">
                            <input id="collect" title="收藏" type="submit"
                                   style="background-image: url('${pageContext.request.contextPath}/static/images/love2.png')"
                                   class="button">
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>
        <div>
            <br><br>
            <label>相似的图片</label><br><br>
            <c:if test="${empty similar_memes}">
                <br><br>
                <label style="color: gray">没有相似的图片</label>
            </c:if>
            <c:if test="${not empty similar_memes}">
                <c:forEach items="${similar_memes}" var="similar_meme">
                    <a href="${pageContext.request.contextPath}/meme/${similar_meme.id}">
                        <img src="${pageContext.request.contextPath}/images/${similar_meme.file}"
                             alt="${similar_meme.name}" style="width: 25%"><br>
                    </a>
                </c:forEach>
            </c:if>
        </div>
    </div>
    <br><br>
</form>
<footer>
    <div class="container">
        <p>&copy; 2024 Chs_memes.CHS版权所有.</p>
    </div>
</footer>
</body>
</html>