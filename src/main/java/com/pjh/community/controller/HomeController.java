package com.pjh.community.controller;

import com.pjh.community.entity.DiscussPost;
import com.pjh.community.entity.Page;
import com.pjh.community.entity.User;
import com.pjh.community.service.DiscussPostService;
import com.pjh.community.service.LikeService;
import com.pjh.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pjh.community.utils.CommunityConstant.ENTITY_TYPE_POST;

@Controller
@SuppressWarnings("all")
public class HomeController {

    @Autowired(required = false)
    private DiscussPostService discussPostService;

    @Autowired(required = false)
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page,
                               @RequestParam(name = "orderMode", defaultValue = "0") int orderMode){

        // 方法调用钱,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index?orderMode=" + orderMode);

        List<Map<String,Object>> discussPosts = new ArrayList<>();
        List<DiscussPost> list = discussPostService.selectDiscussPosts(0,page.getOffset(),page.getLimit(), orderMode);
        if(list!=null){
            for(DiscussPost post : list){
                Map<String, Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.selectById(post.getUserId());
                map.put("user",user);

                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPost",discussPosts);
        model.addAttribute("orderMode", orderMode);
        return "/index";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }
}
