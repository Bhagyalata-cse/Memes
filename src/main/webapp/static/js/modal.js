// 打开弹窗
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

// 关闭弹窗
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}


document.addEventListener('DOMContentLoaded', function() {
    // 关闭弹窗按钮
    const closeButtons = document.querySelectorAll(".close");
    closeButtons.forEach(button => {
        button.addEventListener('click', function() {
            closeModal("loginModal");
            closeModal("registerModal");
        });
    });

    // 点击窗口外部关闭弹窗
    window.addEventListener('click', function(event) {
        if (event.target.matches("#loginModal")) {
            closeModal("loginModal");
        } else if (event.target.matches("#registerModal")) {
            closeModal("registerModal");
        } else if (event.target.matches("#uploadModal")) {
            closeModal("uploadModal");
        } else if (event.target.matches("#editProfileModal")) {
            closeModal("editProfileModal");
        }
    });

    // 登录表单提交
    document.getElementById('loginForm').onsubmit = function(e) {
        e.preventDefault();
        handleLoginFormSubmit(this);
    };

    // 注册表单提交
    document.getElementById('registerForm').onsubmit = function(e) {
        e.preventDefault();
        handleRegisterFormSubmit(this);
    };
});

// 登录表单提交逻辑
function handleLoginFormSubmit(form) {
    const formData = new FormData(form);

    fetch('/api/user/login', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok!');
            }
            return response.json();
        })
        .then(data => {
            if (data.id) {
                showAlert('登录成功！');
                setTimeout(function() {
                    closeModal('loginModal');
                    location.reload();
                }, 2000);
            } else {
                showAlert(data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert('错误：' + error.message);
        });
}

// 注册表单提交逻辑
function handleRegisterFormSubmit(form) {
    const formData = new FormData(form);

    if (formData.get('password') !== formData.get('confirmPassword')) {
        showAlert('两次输入的密码不一致！');
        return;
    }

    fetch('/api/user/register', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok!');
            }
            return response.json();
        })
        .then(data => {
            if (data.id) {
                showAlert('注册成功!');
                setTimeout(function() {
                    closeModal('registerModal');
                    location.reload();
                }, 2000);
            } else {
                showAlert('注册失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert('注册失败！');
        });
}

function logout() {
    fetch('/api/user/logout')
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                showAlert('登出失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert("登出失败！");
        });
}

// 打开用户菜单
function toggleUserMenu() {
    document.getElementById("userDropdown").classList.toggle("show");
}

// 关闭下拉菜单
window.onclick = function(event) {
    if (!event.target.matches('.user-avatar')) {
        var dropdowns = document.getElementsByClassName("user-dropdown");
        Array.from(dropdowns).forEach(dropdown => {
            if (dropdown.classList.contains('show')) {
                dropdown.classList.remove('show');
            }
        });
    }
}

// 编辑个人资料功能
function editProfile() {
    openModal('editProfileModal');

    const editProfileForm = document.getElementById('editProfileForm');
    const editProfilePicture = document.getElementById('editProfilePicture');
    const profileImagePreview = document.getElementById('profileImagePreview');

    loadUserProfile();

    // 处理表单提交
    editProfileForm.addEventListener('submit', function(e) {
        e.preventDefault();
        updateUserProfile();
    });

    // 处理头像预览
    editProfilePicture.addEventListener('change', function(e) {
        handleFileSelect(e.target.files[0], profileImagePreview);
    });
}

// 加载用户资料
function loadUserProfile() {
    fetch('/api/user/profile')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok!');
            }
            return response.json();
        })
        .then(data => {
            if (data.id) {
                document.getElementById('editNickname').value = data.nickname;
                document.getElementById('editPassword').value = data.password;
                if (data.profile_picture) {
                    const img = document.createElement('img');
                    img.src = '/profile_pics/' + data.profile_picture;
                    profileImagePreview.innerHTML = '';
                    profileImagePreview.appendChild(img);
                }
            } else {
                showAlert(data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert("加载资料失败！");
        });
}

// 更新用户资料
function updateUserProfile() {
    const formData = new FormData(document.getElementById('editProfileForm'));

    fetch('/api/user/updateProfile', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok!');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                showAlert('个人资料更新成功！');
                setTimeout(function() {
                    closeModal('editProfileModal');
                    location.reload();
                }, 2000);
            } else {
                showAlert('更新失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert('更新失败，请稍后重试。');
        });
}

// 处理文件预览
function handleFileSelect(file, previewContainer) {
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            previewContainer.innerHTML = '';
            previewContainer.appendChild(img);
        }
        reader.readAsDataURL(file);
    }
}
