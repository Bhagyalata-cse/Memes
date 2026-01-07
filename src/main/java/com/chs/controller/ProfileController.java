package com.chs.controller;

import com.chs.domain.Favorite;
import com.chs.domain.Following;
import com.chs.domain.Meme;
import com.chs.domain.User;
import com.chs.service.FavoriteService;
import com.chs.service.FindMemeService;
import com.chs.service.FollowService;
import com.chs.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private FollowService followService;
    @Autowired
    private FindMemeService findMemeService;
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/user/profile")
    public Object getProfile(
            @RequestParam(value = "id") Integer id,  // id参数必须传递
            Model model,
            HttpSession session,
            HttpServletRequest request,
            @RequestHeader(value = "Accept", required = false) String acceptHeader,
            @RequestParam(value = "tab", required = false, defaultValue = "favorite") String tab) {

        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");

        // 如果是访问自己的主页
        if (currentUser != null && id.equals(currentUser.getId())) {
            // 处理个人主页逻辑
            model.addAttribute("user", currentUser);
            boolean isFollowing = followService.isFollowing(currentUser.getId(), id);
            model.addAttribute("isFollowing", isFollowing);

            List<Following> followees = followService.getFollowee(currentUser.getId());
            List<Following> followers = followService.getFollower(currentUser.getId());

            List<User> followeeUsers = new ArrayList<>();
            List<User> followerUsers = new ArrayList<>();

            // 获取关注和粉丝的用户信息
            for (Following followee : followees) {
                User followeeUser = userService.getUserById(followee.getFollowee());
                followeeUsers.add(followeeUser);
            }
            for (Following follower : followers) {
                User followerUser = userService.getUserById(follower.getFollower());
                followerUsers.add(followerUser);
            }

            model.addAttribute("followeeUsers", followeeUsers);
            model.addAttribute("followerUsers", followerUsers);
            model.addAttribute("followeeNum", followService.getFolloweeNum(currentUser.getId()));
            model.addAttribute("followerNum", followService.getFollowerNum(currentUser.getId()));

            // 获取当前用户上传的所有meme
            List<Meme> userMemeList = findMemeService.getMemeByUid(currentUser.getId());
            model.addAttribute("userMemeList", userMemeList);

            // 获取用户的收藏
            List<Favorite> favorites = favoriteService.getFmemeByUid(id);
            List<Meme> memes = new ArrayList<>();
            for (Favorite favorite : favorites) {
                Meme meme = findMemeService.getMemeById(favorite.getMemeId());
                if (meme != null && !memes.contains(meme)) {
                    memes.add(meme);
                }
            }

            if ("favorite".equals(tab)) {
                model.addAttribute("favorite", favorites);
                model.addAttribute("memes", memes);
            } else if ("userMeme".equals(tab)) {
                model.addAttribute("userMemeList", userMemeList);
            }

            // 判断是否为AJAX请求
            if (acceptHeader != null && acceptHeader.contains("application/json")) {
                return ResponseEntity.ok(currentUser); // 返回JSON数据
            } else {
                return "profile";  // 返回视图
            }
        } else {
            // 查看其他用户的主页
            User otherUser = userService.getUserById(id);
            if (otherUser == null) {
                return "main";  // 返回主页面
            }

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("otherUser", otherUser);
            boolean isFollowing = currentUser != null ? followService.isFollowing(currentUser.getId(), id) : false;
            model.addAttribute("isFollowing", isFollowing);

            List<Following> otherFollowees = followService.getFollowee(id);
            List<Following> otherFollowers = followService.getFollower(id);

            List<User> otherfolloweeUsers = new ArrayList<>();
            List<User> otherfollowerUsers = new ArrayList<>();

            // 获取其他用户的关注和粉丝信息
            for (Following otherfollowee : otherFollowees) {
                User otherfolloweeUser = userService.getUserById(otherfollowee.getFollowee());
                otherfolloweeUsers.add(otherfolloweeUser);
            }
            for (Following otherfollower : otherFollowers) {
                User otherfollowerUser = userService.getUserById(otherfollower.getFollower());
                otherfollowerUsers.add(otherfollowerUser);
            }

            model.addAttribute("otherfolloweeUsers", otherfolloweeUsers);
            model.addAttribute("otherfollowerUsers", otherfollowerUsers);
            model.addAttribute("otherFolloweeNum", otherFollowees.size());
            model.addAttribute("otherFollowerNum", otherFollowers.size());

            // 获取其他用户上传的所有meme
            List<Meme> otherUserMemeList = findMemeService.getMemeByUid(id);
            model.addAttribute("otherUserMemeList", otherUserMemeList);

            return "profile";
        }
    }

    // 关注/取消关注的处理方法
    @PostMapping("/user/follow")
    public String followUser(
            @RequestParam("followeeId") Integer followeeId,
            HttpSession session,
            Model model) {

        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("user", currentUser);
        session.setAttribute("user", currentUser);

        if (currentUser == null) {
            model.addAttribute("errorMessage", "请先登录！");
        }

        Integer followerId = currentUser.getId();
        if (followerId.equals(followeeId)) {
            model.addAttribute("errorMessage", "不能关注自己！");
            return "redirect:/user/profile?id=" + followeeId;
        }

        boolean isFollowing = followService.isFollowing(followerId, followeeId);
        System.out.println("isFollowing: " + isFollowing);
        if (isFollowing) {
            followService.unfollowUser(followerId, followeeId);  // 取消关注
            System.out.println("取消关注"+isFollowing);

        } else {
            followService.followUser(followerId, followeeId);  // 关注
            System.out.println("关注"+isFollowing);
        }


        // 重定向回到该用户的主页
        return "redirect:/user/profile?id=" + followeeId;
    }

    @GetMapping("/user/checkLogin")
    @ResponseBody
    public ResponseEntity<?> checkLogin(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            System.out.println("fuckfuckfuckfuckfuckfuckfuckfuck1");
            return ResponseEntity.ok(currentUser);
        } else {
            System.out.println("fuckfuckfuckfuckfuckfuckfuckfuck2");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
    }
}
