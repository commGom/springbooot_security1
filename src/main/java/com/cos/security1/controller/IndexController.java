package com.cos.security1.controller;

import com.cos.security1.auth.PrincipalDetail;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping({"","/"})
    public String index(){
        //머스테치-> 기본폴더 src/main/resources/
        //ViewResolver 설정할 때 : templates (prefix), 파일명.mustache -> 의존성 해두면 자동으로 설정된다.
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetail principalDetail){
        System.out.println("pricipalDetail;"+principalDetail.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }

    //Spring Security가 해당 주소로 낚아챈다 -> SecurityConfig 파일 작성 후 낚아 채지 않는다.
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword=user.getPassword();
        String encPassword=bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        System.out.println(user);
        userRepository.save(user); //이렇게 저장하면 1234로 저장되기 때문에 시큐리티로 로그인할 수 없다. 패스워드가 암호화 되지않아서
        return "redirect:/loginForm";
    }
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal UserDetails userDetails){//의존성 주입 어노테이션
        System.out.println("/test/login=======");
        PrincipalDetail principalDetail=(PrincipalDetail)authentication.getPrincipal();
        System.out.println("authentication:"+authentication.getPrincipal());
        System.out.println(principalDetail.getUser());

        System.out.println("userDetails:"+userDetails.getUsername());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oAuth2User){
        System.out.println("/test/oauth/login=======");
        OAuth2User oauth2User=(OAuth2User) authentication.getPrincipal();
        System.out.println("authentication:"+oauth2User.getAttributes());

        System.out.println("DI 받은 oAuth2User:"+oAuth2User.getAttributes());

        return "Oauth 세션 정보 확인하기";
    }
}
