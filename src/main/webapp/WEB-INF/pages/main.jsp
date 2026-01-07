<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <title>Chs.memes</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>
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
                    <button onclick="window.location.href='${pageContext.request.contextPath}/zone/${user.id}'">动态</button>
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
    <div class="header-bottom">
        <div class="container">
            <form id="searchForm">
                <select name="searchType" id="searchType">
                    <option value="name">按名字搜索</option>
                    <option value="tag">按标签搜索</option>
                </select>
                <input type="search" name="query" id="searchQuery" placeholder="搜索表情包...">
                <button type="submit">搜索</button>
            </form>
            <div id="searchSuggestions" class="search-suggestions"></div>
        </div>
    </div>
</header>

<main class="container">
    <div class="sort-options">
        <span>排序方式：</span>
        <a href="#" class="sort-option ${sortBy == 'newest' ? 'active' : ''}" data-sort="newest">最新上传</a>
        <a href="#" class="sort-option ${sortBy == 'oldest' ? 'active' : ''}" data-sort="oldest">最早上传</a>
        <a href="#" class="sort-option ${sortBy == 'views' ? 'active' : ''}" data-sort="views">最多浏览</a>
    </div>

    <div id="memeGrid" class="meme-grid">
        <c:forEach items="${memes}" var="meme">
            <a href="/meme/${meme.id}">
                <div class="meme-item">
                    <img src="${pageContext.request.contextPath}/images/${meme.file}" alt="${meme.name}">
                </div>
            </a>
        </c:forEach>
    </div>
    <div id="pagination" class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="javascript:void(0);" data-page="${currentPage - 1}" class="button">上一页</a>
        </c:if>
        <span>第 ${currentPage} 页，共 ${totalPages} 页</span>
        <c:if test="${currentPage < totalPages}">
            <a href="javascript:void(0);" data-page="${currentPage + 1}" class="button">下一页</a>
        </c:if>
    </div>
</main>

<footer>
    <div class="container">
        <p>&copy; 2024 Chs_memes.CHS版权所有.</p>
    </div>
</footer>

<!-- Modals -->
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

</body>
</html>