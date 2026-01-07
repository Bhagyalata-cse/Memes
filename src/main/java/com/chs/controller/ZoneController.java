package com.chs.controller;

import com.chs.domain.Update;
import com.chs.domain.User;
import com.chs.service.ZoneService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ZoneController {
    @Autowired
    private ZoneService zoneService;
    @GetMapping("/zone/{id}")
    public String page(@PathVariable int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";  // 跳转到首页
        }

        List<User> followee=zoneService.getFollowee(id);
        model.addAttribute("followee", followee);
        return "zone";
    }

    @GetMapping("/update/{id}")
    public String getUpdate(@PathVariable int id, Model model, HttpSession session) {
        List<Update> updates;

        if (id == 0) {
            User follower= (User) session.getAttribute("user");
            updates = zoneService.getAllInfo(follower.getId());
        }
        else{
            updates = zoneService.getInfoById(id);
        }

        model.addAttribute("updates", updates);
        return "update";
    }
}
