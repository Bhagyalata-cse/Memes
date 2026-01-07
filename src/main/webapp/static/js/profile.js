// 切换“我的收藏”和“我的meme”选项卡
function loadMemeTab(tab) {
    const url = new URL(window.location.href);
    const id = url.searchParams.get('id');  // 获取URL中的id参数
    url.searchParams.set('tab', tab);  // 设置tab参数，切换收藏或meme

    // 更新按钮的样式
    document.getElementById('favoriteBtn').classList.remove('active');
    document.getElementById('userMemeBtn').classList.remove('active');
    if (tab === 'favorite') {
        document.getElementById('favoriteBtn').classList.add('active');
    } else {
        document.getElementById('userMemeBtn').classList.add('active');
    }

    // 显示目标tab，隐藏其他tab
    document.getElementById('favoriteTab').style.display = (tab === 'favorite') ? 'block' : 'none';
    document.getElementById('userMemeTab').style.display = (tab === 'userMeme') ? 'block' : 'none';

    // 通过AJAX加载数据
    fetch(url.toString(), {  // 使用修改后的URL
        method: 'GET',
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
        .then(response => response.text())
        .then(html => {
            // 不重新加载整个页面，只更新部分内容
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');

            // 获取并更新对应tab的memeGrid内容
            const newMemeGrid = doc.getElementById(tab === 'favorite' ? 'favoriteMemeGrid' : 'userMemeGrid');
            if (newMemeGrid) {
                document.getElementById(tab === 'favorite' ? 'favoriteMemeGrid' : 'userMemeGrid').innerHTML = newMemeGrid.innerHTML;
            }
            console.log('当前tab:', tab);

            // 重新绑定图片错误处理
            handleImageErrors();
        })
        .catch(error => console.error('加载数据时出错:', error));
}


// 图片加载错误处理
function handleImageErrors() {
    const images = document.querySelectorAll('.meme-grid img');
    images.forEach(img => {
        img.onerror = () => {
            img.src = 'src/main/webapp/static/profile_pics/nikola.jpg'; // 默认图片路径
        };
    });
}


// 显示关注列表
function showFollowees() {
    document.getElementById("followeeList").style.display = "block";  // 显示关注列表
    document.getElementById("followerList").style.display = "none";   // 隐藏粉丝列表
}

// 隐藏关注列表
function hideFollowees() {
    document.getElementById("followeeList").style.display = "none";   // 隐藏关注列表
}

// 显示粉丝列表
function showFollowers() {
    document.getElementById("followerList").style.display = "block";  // 显示粉丝列表
    document.getElementById("followeeList").style.display = "none";   // 隐藏关注列表
}

// 隐藏粉丝列表
function hideFollowers() {
    document.getElementById("followerList").style.display = "none";   // 隐藏粉丝列表
}

// 显示其他用户的关注列表
function showOtherFollowees() {
    document.getElementById("otherFolloweeList").style.display = "block";  // 显示其他用户的关注列表
    document.getElementById("otherFollowerList").style.display = "none";   // 隐藏其他用户的粉丝列表
}

// 隐藏其他用户的关注列表
function hideOtherFollowees() {
    document.getElementById("otherFolloweeList").style.display = "none";   // 隐藏其他用户的关注列表
}

// 显示其他用户的粉丝列表
function showOtherFollowers() {
    document.getElementById("otherFollowerList").style.display = "block";  // 显示其他用户的粉丝列表
    document.getElementById("otherFolloweeList").style.display = "none";   // 隐藏其他用户的关注列表
}

// 隐藏其他用户的粉丝列表
function hideOtherFollowees() {
    document.getElementById("otherFollowerList").style.display = "none";   // 隐藏其他用户的粉丝列表
}

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('followForm').addEventListener('submit', function(event) {
        event.preventDefault();

        fetch('/user/checkLogin')
            .then(response => {
                if (!response.ok) {
                    promptLogin();
                } else {
                    this.submit();
                }
            })
            .catch(error => {
                console.error('请求失败:', error);
                showAlert('请求失败！');
            });
    });
});
