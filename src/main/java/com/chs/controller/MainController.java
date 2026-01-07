package com.chs.controller;

import com.chs.domain.Meme;
import com.chs.domain.User;
import com.chs.service.MainService;
import com.chs.utils.fileManipulation;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "newest") String sortBy,
                       @RequestParam(required = false) String query,
                       @RequestParam(defaultValue = "name") String searchType,
                       Model model, HttpSession session) {
        int pageSize = 49;
        List<Meme> memes = mainService.getMemes(page, pageSize, sortBy, query, searchType);
        int totalMemes = mainService.getMemeNumber(query, searchType);
        int totalPages = (int) Math.ceil((double) totalMemes / pageSize);

        model.addAttribute("memes", memes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("query", query);
        model.addAttribute("searchType", searchType);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        return "main";
    }

    @GetMapping("/api/memes")
    @ResponseBody
    public Map<String, Object> getMemes(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "newest") String sortBy,
                                        @RequestParam(required = false) String query,
                                        @RequestParam(defaultValue = "name") String searchType) {
        int pageSize = 49;
        List<Meme> memes = mainService.getMemes(page, pageSize, sortBy, query, searchType);
        int totalMemes = mainService.getMemeNumber(query, searchType);
        int totalPages = (int) Math.ceil((double) totalMemes / pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("memes", memes);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);

        return response;
    }

    @PostMapping("/api/meme/upload")
    public ResponseEntity<?> uploadMeme(@RequestParam("memeFile") MultipartFile file,
                                        @RequestParam("name") String name,
                                        @RequestParam("introduction") String introduction,
                                        @RequestParam("tags") String tags,
                                        HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.ok().body(Map.of("success", false, "message", "请先登录！"));
        }

        if(!fileManipulation.isPicture(file)){
            return ResponseEntity.ok().body(Map.of("success", false, "message", "不支持的文件类型，请上传图片文件！"));
        }

        try {
            String fileName = UUID.randomUUID() + "." + fileManipulation.getExtension(file);
            String uploadDir = "C:\\Users\\ohouw\\Desktop\\Project\\memes\\src\\main\\webapp\\static\\images";
            String uploadDir1 = "C:\\Users\\ohouw\\Desktop\\Project\\memes\\target\\memes\\static\\images";
            File dest = new File(uploadDir + File.separator + fileName);
            File dest1 = new File(uploadDir1 + File.separator + fileName);
            file.transferTo(dest);
            file.transferTo(dest1);

            Meme meme = new Meme();
            meme.setName(name);
            meme.setFile(fileName);
            meme.setIntroduction(introduction);
            meme.setUser_Id(user.getId());

            int memeId = mainService.addMeme(meme);

            // Handle tags
            String[] tagArray = tags.split(",");
            Set<String> uniqueTags = new HashSet<>(Arrays.asList(tagArray));
            if (uniqueTags.size() > 5) {
                return ResponseEntity.ok().body(Map.of("success", false, "message", "最多只能添加5个标签！"));
            }

            for (String tag : uniqueTags) {
                tag = tag.trim();
                if (!tag.isEmpty()) {
                    mainService.addTagToMeme(memeId, tag);
                }
            }

            return ResponseEntity.ok(Map.of("success", true, "message", "上传成功！"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(Map.of("success", false, "message", "上传失败！"));
        }
    }

    @GetMapping("/api/tags/suggest")
    public ResponseEntity<List<String>> suggestTags(@RequestParam String query) {
        List<String> suggestions = mainService.getSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/api/search-suggestions")
    @ResponseBody
    public List<String> getSearchSuggestions(@RequestParam String query, @RequestParam String type) {
        return mainService.getSearchSuggestions(query, type);
    }
}