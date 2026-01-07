<%@ page import="com.chs.domain.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人主页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>
    <script src="${pageContext.request.contextPath}/js/profile.js"></script>
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

<main class="container">
<c:choose>
    <c:when test="${empty otherUser}">
        <!-- 当前用户主页 -->
        <div class="profile-container">
            <!-- 左侧内容 -->
            <div class="profile-left">
                <div class="titlename">${user.nickname} 的个人主页</div>
                <div class="tab-buttons">
                    <button id="favoriteBtn" onclick="loadMemeTab('favorite')">我的收藏</button>
                    <button id="userMemeBtn" onclick="loadMemeTab('userMeme')">我的meme</button>
                </div>

                <!-- 我的收藏 -->
                <div id="favoriteTab" class="meme-tab">
                    <h3>我的收藏</h3>
                    <div id="favoriteMemeGrid" class="meme-grid">
                        <c:forEach var="favorite" items="${favorite}" varStatus="status">
                            <c:set var="meme" value="${memes[status.index]}"/>
                            <a href="/meme/${meme.id}">
                                <div class="meme-item">
                                    <img src="${pageContext.request.contextPath}/images/${meme.file}"
                                         alt="${meme.name}">
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </div>

                <!-- 我的meme -->
                <div id="userMemeTab" class="meme-tab" style="display:none;">
                    <h3>我的meme</h3>
                    <div id="userMemeGrid" class="meme-grid">
                        <c:forEach var="meme" items="${userMemeList}">
                            <a href="/meme/${meme.id}">
                                <div class="meme-item">
                                    <img src="${pageContext.request.contextPath}/images/${meme.file}"
                                         alt="${meme.name}">
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <!-- 右侧内容 -->
            <div class="profile-right">
                <div class="follow-info">
                    <p>
                        <strong>关注:</strong> ${followeeNum}
                        <a href="javascript:void(0);" onclick="showFollowees()">展开</a>
                    </p>
                    <div id="followeeList" class="follow-list" style="display:none;">
                        <h3>关注的人</h3>
                        <ul>
                            <c:forEach var="followee" items="${followeeUsers}">
                                <li>
                                    <a href="/user/profile?id=${followee.id}">
                                        <img src="${pageContext.request.contextPath}/profile_pics/${followee.profile_picture}"
                                             alt="${followee.nickname}" class="user-avatar"/>
                                            ${followee.nickname}
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                    <p>
                        <strong>粉丝:</strong> ${followerNum}
                        <a href="javascript:void(0);" onclick="showFollowers()">展开</a>
                    </p>
                    <div id="followerList" class="follow-list" style="display:none;">
                        <h3>粉丝</h3>
                        <ul>
                            <c:forEach var="follower" items="${followerUsers}">
                                <li>
                                    <a href="/user/profile?id=${follower.id}">
                                        <img src="${pageContext.request.contextPath}/profile_pics/${follower.profile_picture}"
                                             alt="${follower.nickname}'s avatar" class="user-avatar"/>
                                            ${follower.nickname}
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
    <%--其他用户主页--%>
        <div class="profile-container">
            <!-- 左侧内容 -->
            <div class="profile-left">
                <div class="profile-avatar-wrapper">
                    <img src="${pageContext.request.contextPath}/profile_pics/${otherUser.profile_picture}"
                         alt="${otherfollowee.nickname}" class="user-avatar"/></div>
                <div class="titlename">${otherUser.nickname} 的个人主页</div>

                <!-- 判断当前用户是否已关注该用户 -->
                <c:if test="${isFollowing}">
                    <!-- 已关注，显示取消关注按钮 -->
                    <form action="/user/follow" method="post">
                        <input type="hidden" name="followeeId" value="${otherUser.id}"/>
                        <div class="tab-buttons">
                            <button class="unfollow-btn" type="submit">取消关注</button>
                        </div>
                    </form>
                </c:if>
                <c:if test="${not isFollowing}">
                    <!-- 未关注，显示关注按钮 -->
                    <form id="followForm" action="/user/follow" method="post">
                        <input type="hidden" name="followeeId" value="${otherUser.id}"/>
                        <div class="tab-buttons">
                            <button class="follow-btn" type="submit">关注</button>
                        </div>
                    </form>
                </c:if>

                <!-- meme -->
                <c:if test="${not empty otherUserMemeList}">
                <div class="meme-tab">
                    <h3>${otherUser.nickname}的meme</h3>
                    <div class="meme-grid">
                        <c:forEach var="othermeme" items="${otherUserMemeList}">
                            <a href="/meme/${othermeme.id}">
                                <div class="meme-item">
                                    <img src="${pageContext.request.contextPath}/images/${othermeme.file}"
                                         alt="${othermeme.name}">
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>
        <!-- 右侧内容 -->
        <div class="profile-right">
            <div class="follow-info">
                <p>
                    <strong>关注:</strong> ${otherFolloweeNum}
                    <a href="javascript:void(0);" onclick="showOtherFollowees()">展开</a>
                </p>
                <div id="otherFolloweeList" class="follow-list" style="display:none;">
                    <h3>关注的人</h3>
                    <ul>
                        <c:forEach var="otherfollowee" items="${otherfolloweeUsers}">
                            <li>
                                <a href="/user/profile?id=${otherfollowee.id}">
                                    <img src="${pageContext.request.contextPath}/profile_pics/${otherfollowee.profile_picture}"
                                         alt="${otherfollowee.nickname}" class="user-avatar"/>
                                        ${otherfollowee.nickname}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <p>
                    <strong>粉丝:</strong> ${otherFollowerNum}
                    <a href="javascript:void(0);" onclick="showOtherFollowers()">展开</a>
                </p>
                <div id="otherFollowerList" class="follow-list" style="display:none;">
                    <h3>粉丝</h3>
                    <ul>
                        <c:forEach var="otherfollower" items="${otherfollowerUsers}">
                            <li>
                                <a href="/user/profile?id=${otherfollower.id}">
                                    <img src="${pageContext.request.contextPath}/profile_pics/${otherfollower.profile_picture}"
                                         alt="${otherfollower.nickname}'s avatar" class="user-avatar"/>
                                        ${otherfollower.nickname}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
        </div>
    </c:otherwise>
</c:choose>
</main>

<footer>
    <div class="container">
        <p>&copy; 2024 Chs_memes.CHS版权所有.</p>
    </div>
</footer>
</body>
</html>
