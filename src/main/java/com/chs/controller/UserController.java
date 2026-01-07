package com.chs.controller;

import com.chs.domain.User;
import com.chs.response.ErrorResponse;
import com.chs.service.EmailService;
import com.chs.service.UserService;
import com.chs.utils.fileManipulation;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,@RequestParam String password, HttpSession session) {
        User user = userService.login(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.ok().body(new ErrorResponse("请检查邮箱或者密码是否正确！"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String nickname,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam String verificationCode,
                                      @RequestParam MultipartFile profile_picture,
                                      HttpSession session) {
        if(!fileManipulation.isPicture(profile_picture)){
            return ResponseEntity.ok().body(Map.of("message", "不支持的文件类型，请上传图片文件！"));
        }

        String storedCode = (String) session.getAttribute("verificationCode");
        Long storedTime = (Long) session.getAttribute("verificationCodeTime");

        if (storedCode == null || storedTime == null || !storedCode.equals(verificationCode) ||
                System.currentTimeMillis() - storedTime > 600000) {
            return ResponseEntity.ok().body(Map.of("message", "验证码错误！"));
        }

        try {
            if(userService.isEmailExists(email)){
                return ResponseEntity.ok().body(Map.of("message", "该邮箱已被注册！"));
            }

            String fileName = UUID.randomUUID() + "." + fileManipulation.getExtension(profile_picture);
            User user = userService.register(nickname, email, password, fileName);
            if (user != null) {
                session.setAttribute("user", user);

                String uploadDir = "D:\\Study\\JavaEnterprise\\GroupWork\\memes\\src\\main\\webapp\\static\\profile_pics";
                String uploadDir1 = "D:\\Study\\JavaEnterprise\\GroupWork\\memes\\target\\memes\\static\\profile_pics";
                File dest = new File( uploadDir+ File.separator + fileName);
                File dest1 = new File( uploadDir1+ File.separator + fileName);
                profile_picture.transferTo(dest);
                profile_picture.transferTo(dest1);

                return ResponseEntity.ok().body(user);
            } else {
                return ResponseEntity.ok().body(Map.of("message", "未知错误！"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(Map.of("message", "头像上传失败！"));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String email, HttpSession session) {
        if(userService.isEmailExists(email)){
            return ResponseEntity.ok().body(Map.of("success", false, "message", "该邮箱已被注册！"));
        }

        String code = generateVerificationCode();
        emailService.sendVerificationCode(email, code);
        session.setAttribute("verificationCode", code);
        session.setAttribute("verificationCodeTime", System.currentTimeMillis());
        return ResponseEntity.ok().body(Map.of("success", true));
    }

    private String generateVerificationCode() {
        return String.format("%06d", (int)(Math.random() * 1000000));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.ok().body(Map.of("message", "用户未登录！"));
        }
        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateUserProfile(@RequestParam String nickname,
                                               @RequestParam String password,
                                               @RequestParam(required = false) MultipartFile profilePicture,
                                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.ok().body(Map.of("success", false, "message", "用户未登录！"));
        }

        try {
            user.setNickname(nickname);
            user.setPassword(password);

            if (profilePicture != null && !profilePicture.isEmpty()) {
                if(!fileManipulation.isPicture(profilePicture)){
                    return ResponseEntity.ok().body(Map.of("message", "不支持的文件类型，请上传图片文件！"));
                }

                if (profilePicture.getSize() > 5 * 1024 * 1024) { // 5MB limit
                    return ResponseEntity.ok().body(Map.of("success", false, "message", "文件大小不能超过5MB！"));
                }

                String fileName = user.getProfile_picture();
                String uploadDir = "D:\\Study\\JavaEnterprise\\GroupWork\\memes\\src\\main\\webapp\\static\\profile_pics";
                String uploadDir1 = "D:\\Study\\JavaEnterprise\\GroupWork\\memes\\target\\memes\\static\\profile_pics";
                File dest = new File( uploadDir+ File.separator + fileName);
                File dest1 = new File( uploadDir1+ File.separator + fileName);
                profilePicture.transferTo(dest);
                profilePicture.transferTo(dest1);

                user.setProfile_picture(fileName);
            }

            userService.updateUser(user);
            session.setAttribute("user", user);

            return ResponseEntity.ok(Map.of("success", true, "message", "个人资料更新成功！"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(Map.of("success", false, "message", "更新失败！"));
        }
    }
}

