package ustc.yh.ssoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("username") String username, @RequestParam("password") String password,
                          @RequestParam("url") String url,
                          HttpServletResponse response){


        // 处理登陆请求
        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){
            String token = UUID.randomUUID().toString().replace("-", "");
            redisTemplate.opsForValue().set(token,username);
            Cookie sso_token1 = new Cookie("sso_token",token);
            response.addCookie(sso_token1);
            return "redirect:" + url +"?token="+token;
        }
        // 继续登陆
        System.out.println("---");
        return "login.html";
    }

//redirect_url
    @GetMapping("/login.html")
    public String login(@RequestParam("redirect_url")String url, Model model,
                        @CookieValue(value = "sso_token",required = false)String sso_token){
        if(sso_token!=null)return "redirect:" + url +"?token="+sso_token;
        model.addAttribute("url",url);
        return "login";
    }
}
