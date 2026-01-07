package com.chs.controller;

import com.chs.domain.Meme;
import com.chs.domain.User;
import com.chs.service.MainService;
import com.chs.service.PageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class PageController {
    private void sendInfo(int meme_id,int user_id,int meme_user_id,Model model) {
        Meme meme = pageService.getMeme(meme_id);
        User meme_user=pageService.getUser(meme_user_id);
        model.addAttribute("meme", meme);
        model.addAttribute("memeUser", meme_user);
        if(user_id!=-1){
            model.addAttribute("favorite", pageService.favorite(user_id,meme_id));
        }
        model.addAttribute("tags", pageService.getTagName(meme_id));
        model.addAttribute("introduction", meme.getIntroduction());
    }
    private void findSimilar(int meme_id,Model model){
        List<String> tagName=pageService.getTagName(meme_id);
        String memeName=pageService.getMeme(meme_id).getName();
        List<String> nameList= Arrays.asList(memeName.trim().split(" "));
        List<Integer> ids=new ArrayList<>();
        if(!tagName.isEmpty()){
            ids=pageService.findSimilar(tagName);
        }
        List<Integer> ids2=pageService.findSimilar2(nameList);
        ids.addAll(ids2);
        if(ids.isEmpty()){
            model.addAttribute("similar_memes", null);
            return;
        }
        List<Meme> memes=new ArrayList<Meme>();
        for (Integer i : ids) {
            if(i==meme_id) continue;
            memes.add(pageService.getMeme(i));
            if(memes.size()>=3){
                break;
            }
        }
        model.addAttribute("similar_memes", memes);
    }
    @Autowired
    private PageService pageService;
    @Autowired
    private MainService mainService;
    @GetMapping("/meme/{id}")
    public String page(@PathVariable int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        Meme meme = pageService.getMeme(id);
        if (user != null) {
            int user_Id = user.getId();
            sendInfo(id,user_Id,meme.getUser_Id(),model);
        }
        else{
            sendInfo(id,-1,meme.getUser_Id(),model);
        }
        findSimilar(id, model);
        mainService.incrementViews(id);
        model.addAttribute("meme", meme);
        return "page";
    }



    @PostMapping("/meme/favorite")
    public String favorite(
            @RequestParam("user_id") int user_id,
            @RequestParam("meme_id") int meme_id,
            Model model){
        boolean favorite=pageService.favorite(user_id,meme_id);
        Meme meme = pageService.getMeme(meme_id);
        if(favorite){
            pageService.deleteFavorite(user_id,meme_id);
        }else {
            pageService.insertFavorite(user_id,meme_id);
        }
        sendInfo(meme_id,user_id,meme.getUser_Id(),model);
        findSimilar(meme_id, model);
        return "page";
    }
}
