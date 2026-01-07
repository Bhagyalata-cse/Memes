$(document).ready(function () {
    // 为所有按钮添加 hover 事件
    $(".zone-button").mouseenter(function () {
        $(this).css({color: 'gray'});
    });

    $(".zone-button").mouseleave(function () {
        $(this).css({color: 'black'});
    });

    // 为特定类名的按钮绑定点击事件
    $(".zone-button-class").click(function () {
        var user_Id = $(this).val();
        $("#update").attr('src', '/update/' + user_Id);
    });
});