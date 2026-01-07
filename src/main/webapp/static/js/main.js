document.addEventListener('DOMContentLoaded', function() {
    console.log('网站加载完成');

    // 初始化页面相关事件
    initPageEvents();
});

function initPageEvents() {
    // 处理图片加载错误
    handleImageErrors();

    // 处理验证码按钮
    handleVerificationCode();

    // 处理排序选项
    handleSortOptions();

    // 绑定分页事件
    bindPaginationEvents();

    // 初始化上传功能
    initUploadForm();

    // 初始化搜索功能
    initSearchForm();
}

function handleImageErrors() {
    const memeImages = document.querySelectorAll('.meme-item img');
    memeImages.forEach(img => {
        img.onerror = function() {
            this.src = '/static/images/nikola.jpg';
            this.alt = '图片加载失败';
        }
    });
}

function handleVerificationCode() {
    const getVerificationCodeBtn = document.getElementById('getVerificationCode');
    if (getVerificationCodeBtn) {
        getVerificationCodeBtn.addEventListener('click', function() {
            const email = document.getElementById('email').value;
            if (!email) {
                showAlert('请输入邮箱地址！');
                return;
            }

            const params = new URLSearchParams();
            params.append('email', email);

            fetch('/api/user/sendVerificationCode', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: params
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok!');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        showAlert('验证码已发送到您的邮箱!');
                        disableVerificationButton(getVerificationCodeBtn);
                    } else {
                        showAlert(data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showAlert('发送验证码失败！请检查邮箱地址是否正确！');
                });
        });
    }
}

function disableVerificationButton(button) {
    button.disabled = true;
    let seconds = 60;
    const timer = setInterval(() => {
        button.textContent = `${seconds}秒后重新获取`;
        seconds--;
        if (seconds < 0) {
            clearInterval(timer);
            button.disabled = false;
            button.textContent = '获取验证码';
        }
    }, 1000);
}

function handleSortOptions() {
    const sortOptions = document.querySelectorAll('.sort-option');
    sortOptions.forEach(option => {
        option.addEventListener('click', function(e) {
            e.preventDefault();
            const sortBy = this.getAttribute('data-sort');
            // 获取当前的搜索条件
            const currentQuery = document.getElementById('searchQuery').value;
            const currentSearchType = document.getElementById('searchType').value;

            // 基于当前排序条件和搜索条件加载页面
            loadPage(1, sortBy, currentQuery, currentSearchType);
        });
    });
}

function loadPage(page, sortBy = getCurrentSortBy(), searchQuery = '', searchType = '') {
    const url = new URL(window.location.href);
    url.searchParams.set('page', page);
    url.searchParams.set('sortBy', sortBy);

    // 如果有搜索条件，保留搜索条件
    if (searchQuery) {
        url.searchParams.set('query', searchQuery);
    }
    if (searchType) {
        url.searchParams.set('searchType', searchType);
    }

    fetch(url, {
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
    })
        .then(response => response.text())
        .then(html => {
            const doc = new DOMParser().parseFromString(html, 'text/html');
            updatePageContent(doc, sortBy, page);
        })
        .catch(error => console.error('加载页面时出错:', error));
}

function updatePageContent(doc, sortBy, page) {
    // 更新表情包网格
    document.getElementById('memeGrid').innerHTML = doc.getElementById('memeGrid').innerHTML;

    // 更新分页控件
    document.getElementById('pagination').innerHTML = doc.getElementById('pagination').innerHTML;

    // 更新排序选项
    updateSortOptions(sortBy);

    // 更新URL
    const url = new URL(window.location.href);
    history.pushState({ page, sortBy }, '', url.toString());

    // 重新绑定图片错误处理和分页事件
    handleImageErrors();
    bindPaginationEvents();
}

function getCurrentSortBy() {
    const activeSortOption = document.querySelector('.sort-option.active');
    return activeSortOption ? activeSortOption.getAttribute('data-sort') : 'newest';
}

function updateSortOptions(currentSortBy) {
    document.querySelectorAll('.sort-option').forEach(option => {
        option.classList.toggle('active', option.getAttribute('data-sort') === currentSortBy);
    });
}

function bindPaginationEvents() {
    const paginationLinks = document.querySelectorAll('.pagination a');
    paginationLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const page = this.getAttribute('data-page');
            loadPage(page);
        });
    });
}

function initUploadForm() {
    const uploadForm = document.getElementById('uploadForm');
    const memeFileInput = document.getElementById('memeFile');
    const dropZone = document.getElementById('dropZone');
    const imagePreviewContainer = document.getElementById('imagePreviewContainer');
    const memeTagInput = document.getElementById('memeTagInput');
    const tagContainer = document.getElementById('tagContainer');
    const memeTagsHidden = document.getElementById('memeTags');
    const tagSuggestions = document.getElementById('tagSuggestions');

    let tags = [];
    if (uploadForm) {
        // 图片预览
        memeFileInput.addEventListener('change', function(e) {
            handleFileSelect(e.target.files[0], imagePreviewContainer);
        });

        // 拖放功能
        dropZone.addEventListener('dragover', function(e) {
            e.preventDefault();
            this.classList.add('dragover');
        });

        dropZone.addEventListener('dragleave', function() {
            this.classList.remove('dragover');
        });

        dropZone.addEventListener('drop', function(e) {
            e.preventDefault();
            this.classList.remove('dragover');
            handleFileSelect(e.dataTransfer.files[0], imagePreviewContainer);
        });

        // 标签输入处理
        memeTagInput.addEventListener('keydown', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                addTag(this.value.trim());
                this.value = '';
            }
        });

        // 表单提交
        uploadForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(this);
            const file = memeFileInput.files[0];

            // 文件大小验证（客户端）
            if (file && file.size > 5 * 1024 * 1024) { // 5MB 限制
                showAlert('文件大小不能超过5MB！');
                return;
            }

            // 添加标签到表单数据
            formData.set('tags', tags.join(','));

            // AJAX 上传带进度条
            uploadFile(formData);
        });

        // 标签建议
        if (memeTagInput) {
            memeTagInput.addEventListener('input', function() {
                const tagInput = this.value.trim();
                if (tagInput.length > 1) {
                    fetch(`/api/tags/suggest?query=${tagInput}`)
                        .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok!');
                    }
                    return response.json();
                })
                        .then(suggestions => {
                            displayTagSuggestions(suggestions);
                        });
                } else {
                    tagSuggestions.style.display = 'none';
                }
            });
        }
    }

    function displayTagSuggestions(suggestions) {
        tagSuggestions.innerHTML = '';
        if (suggestions.length > 0) {
            suggestions.forEach(tag => {
                const div = document.createElement('div');
                div.textContent = tag;
                div.className = 'tag-suggestion';
                div.addEventListener('click', () => {
                    addTag(tag);
                    memeTagInput.value = '';
                    tagSuggestions.style.display = 'none';
                });
                tagSuggestions.appendChild(div);
            });
            tagSuggestions.style.display = 'block';
        } else {
            tagSuggestions.style.display = 'none';
        }
    }

    function addTag(tag) {
        if (tag && !tags.includes(tag) && tags.length < 5) {
            tags.push(tag);
            updateTagDisplay();
        }
    }

    function removeTag(tag) {
        const index = tags.indexOf(tag);
        if (index > -1) {
            tags.splice(index, 1);
            updateTagDisplay();
        }
    }

    function updateTagDisplay() {
        tagContainer.innerHTML = '';
        tags.forEach(tag => {
            const tagElement = document.createElement('span');
            tagElement.className = 'tag';
            tagElement.innerHTML = `
            ${tag}
            <span class="tag-remove" onclick="removeTag('${tag}')">&times;</span>
        `;
            tagContainer.appendChild(tagElement);
        });
        memeTagsHidden.value = tags.join(',');
    }

    window.removeTag = removeTag;
}

function handleFileSelect(file, imagePreviewContainer) {
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            imagePreviewContainer.innerHTML = '';
            imagePreviewContainer.appendChild(img);
        };
        reader.readAsDataURL(file);
    }
}

function uploadFile(formData) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/meme/upload', true);

    xhr.upload.onprogress = function(e) {
        if (e.lengthComputable) {
            const percentComplete = (e.loaded / e.total) * 100;
            document.getElementById('uploadProgress').style.width = percentComplete + '%';
        }
    };

    xhr.onload = function() {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.success) {
                showAlert('表情包上传成功！');
                setTimeout(function() {
                    closeModal('uploadModal');
                    loadPage(1);
                }, 2000);
            } else {
                showAlert('上传失败: ' + response.message);
            }
        } else {
            showAlert('上传失败，请稍后重试。');
        }
    };

    xhr.send(formData);
}

function initSearchForm() {
    const searchForm = document.getElementById('searchForm');
    const searchQuery = document.getElementById('searchQuery');
    const searchType = document.getElementById('searchType');
    const searchSuggestions = document.getElementById('searchSuggestions');
    let searchTimeout;

    // 搜索表单提交
    searchForm.addEventListener('submit', function(e) {
        e.preventDefault();
        fetchMemes(1, searchQuery.value, searchType.value);
    });

    // 搜索建议显示位置
    window.addEventListener('resize', setSearchSuggestionPosition);

    // 搜索输入时获取建议
    searchQuery.addEventListener('input', function(e) {
        clearTimeout(searchTimeout);
        const query = e.target.value;

        setSearchSuggestionPosition();

        searchTimeout = setTimeout(() => {
            if (query.length > 1) {
                fetch(`/api/search-suggestions?query=${encodeURIComponent(query)}&type=${searchType.value}`)
                    .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok!');
                    }
                    return response.json();
                })
                    .then(suggestions => {
                        displaySearchSuggestions(suggestions);
                    });
            } else {
                searchSuggestions.style.display = 'none';
            }
        }, 300);
    });

    // 点击页面其他部分时隐藏搜索建议
    document.addEventListener('click', function(e) {
        if (!searchSuggestions.contains(e.target) && e.target !== searchQuery) {
            searchSuggestions.style.display = 'none';
        }
    });
}

function setSearchSuggestionPosition() {
    const inputWidth = searchQuery.offsetWidth;
    const inputLeft = searchQuery.getBoundingClientRect().left;
    searchSuggestions.style.width = inputWidth + 'px';
    searchSuggestions.style.left = inputLeft + 'px';
}

function displaySearchSuggestions(suggestions) {
    searchSuggestions.innerHTML = '';
    if (suggestions.length > 0) {
        suggestions.forEach(suggestion => {
            const div = document.createElement('div');
            div.className = 'search-suggestion-item';
            div.textContent = suggestion;
            div.addEventListener('click', () => {
                searchQuery.value = suggestion;
                searchSuggestions.style.display = 'none';
                fetchMemes(1, suggestion, searchType.value);
            });
            searchSuggestions.appendChild(div);
        });
        searchSuggestions.style.display = 'block';
    } else {
        searchSuggestions.style.display = 'none';
    }
}

function fetchMemes(page, query, searchType, sortBy = 'newest') {
    const url = `/api/memes?page=${page}&query=${encodeURIComponent(query)}&searchType=${searchType}&sortBy=${sortBy}`;
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok!');
            }
            return response.json();
        })
        .then(data => {
            updateMemeGrid(data.memes, query);
            updatePagination(data.currentPage, data.totalPages, query, searchType, sortBy);
        })
        .catch(error => console.error('Error:', error));
}

function updateMemeGrid(memes) {
    memeGrid.innerHTML = '';
    if (memes.length === 0) {
        memeGrid.innerHTML = '<div class="empty-result">没有找到相关的表情包。试试其他的关键词。</div>';
        return;
    }
    memes.forEach(meme => {
        const memeLink = document.createElement('a');
        memeLink.href = `/meme/${meme.id}`;

        const memeItem = document.createElement('div');
        memeItem.className = 'meme-item';

        const memeImage = document.createElement('img');
        memeImage.src = `/images/${meme.file}`;
        memeImage.alt = meme.name;

        memeItem.appendChild(memeImage);
        memeLink.appendChild(memeItem);
        memeGrid.appendChild(memeLink);
    });
}

function updatePagination(currentPage, totalPages, query, searchType, sortBy) {
    pagination.innerHTML = '';
    if (currentPage > 1) {
        pagination.innerHTML += `<a href="javascript:void(0);" onclick="fetchMemes(${currentPage - 1}, '${query}', '${searchType}', '${sortBy}')" class="button">上一页</a>`;
    }
    pagination.innerHTML += `<span>第 ${currentPage} 页，共 ${totalPages} 页</span>`;
    if (currentPage < totalPages) {
        pagination.innerHTML += `<a href="javascript:void(0);" onclick="fetchMemes(${currentPage + 1}, '${query}', '${searchType}', '${sortBy}')" class="button">下一页</a>`;
    }
}

function promptLogin() {
    showAlert('请先登录！');
    openModal('loginModal');
}

function showAlert(message) {
    const floatingWindow = document.createElement('div');
    floatingWindow.classList.add('floating-window');
    floatingWindow.innerText = message;

    // 将悬浮窗添加到页面中
    document.body.appendChild(floatingWindow);

    // 显示悬浮窗
    setTimeout(function() {
        floatingWindow.classList.add('show');
    }, 10);

    // 鼠标悬浮时阻止消失
    floatingWindow.addEventListener('mouseenter', function() {
        floatingWindow.classList.add('hover');
    });

    // 鼠标离开时恢复消失
    floatingWindow.addEventListener('mouseleave', function() {
        floatingWindow.classList.remove('hover');
    });

    // 点击小窗时立即消失
    floatingWindow.addEventListener('click', function() {
        floatingWindow.classList.remove('show');
        setTimeout(function() {
            floatingWindow.remove();
        }, 500);
    });

    // 3秒后自动消失并从页面移除（如果没有点击）
    setTimeout(function() {
        if (!floatingWindow.classList.contains('hover')) {
            floatingWindow.classList.remove('show');
            setTimeout(function() {
                floatingWindow.remove();
            }, 500);
        }
    }, 2000);
}