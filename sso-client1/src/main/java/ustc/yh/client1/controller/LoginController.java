package ustc.yh.client1.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/client1")
public class LoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${sso.server.url}")
    private String ssoServer;


    @RequestMapping("/employee")
    public String getEmployee(Model model,HttpSession session,
                              @RequestParam(value = "token",required = false)String token){
        if(!StringUtils.isEmpty(token)){
            // 从sso服务器跳回来的
//            String s = redisTemplate.opsForValue().get(token);
            session.setAttribute("loginUser","张三");
        }
        // 此处写一个验证逻辑 如果没有客户跳转到 服务器 通过服务器进行登陆操作再然后跳转回到此处
        Object loginUser = session.getAttribute("loginUser");
        // 如果没有登录用户 跳转服务器进行登陆
        // 再添加一个redirect_url 来指引server跳转位置
        if(loginUser == null)return "redirect:" + ssoServer + "?redirect_url=http://client1.com:8001/client1/employee";
        List<String> emps = new ArrayList<>();
        emps.add("yanghuan");
        emps.add("xumin");
        model.addAttribute("users",emps);
        return "employee";
    }
}
