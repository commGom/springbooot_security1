package com.cos.security1.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    //후처리는 loadUser 메소드에서 진행된다.

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest:"+userRequest);
        System.out.println("getAccessToken:"+userRequest.getAccessToken());
        System.out.println("getClientRegistration:"+userRequest.getClientRegistration());
        System.out.println("getClientRegistration().getClientName():"+userRequest.getClientRegistration().getClientName());


        OAuth2User oAuth2User=super.loadUser(userRequest);
        //구글 로그인 버튼-> 구글 로그인 창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리)->Access Token 요청
        //userRequest 정보 -> loadUser함수 호출 -> 구글로 부터 회원 프로필 받아준다
        System.out.println("getAttributes:"+super.loadUser(userRequest).getAttributes());

        //회원가입 진행시키기
        String provider=userRequest.getClientRegistration().getClientId(); //google
        String providerId=oAuth2User.getAttribute("sub");
        String username=provider+"_"+providerId; //google_sub번호
        String password=bCryptPasswordEncoder.encode("겟인데어");
//        String password="겟인데어";
        String email=oAuth2User.getAttribute("email");
        String role="ROLE_USER";
        //회원가입되어있는지 id 찾기
        User userEntity = userRepository.findByUsername(username);
        if (userEntity==null){
            //회원가입시키기 (Builder Pattern을 이용하여 생성자에 값 넣기)
            userEntity=User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }else{

        }
        return new PrincipalDetail(userEntity,oAuth2User.getAttributes());//Authentication 객체 안에 들어간다.
    }
}
