package com.cos.security1.auth;

// 시큐리티가 /login을 낚아채서 로그인을 진행시키는데,
// 로그인 진행이 완료가 되면 session을 만들어준다. (security만의 session공간이 있다) -> key 값 Security ContextHolder에 세션정보 저장
// 세션에 들어갈 수 있는 정보(Object) Authentication 타입의 객체만 들어갈 수 있다.
// Authentication 안에는 User정보가 있어야 함
// User오브젝트 타입 => UserDetails 타입 객체여야한다.

import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//Security Session 영역에 들어가는 객체 Authentication 안에 유저정보의 타입은 UserDetails 객체이어야한다. (->PrincipalDetail)
@Data
public class PrincipalDetail implements UserDetails, OAuth2User {
    private User user; //Composition 하기
    private Map<String,Object> attributes;

    //생성자에 user를 받아서 user를 넣어주기 -> 일반 로그인 시에
    public PrincipalDetail(User user) {
        this.user = user;
    }

    //OAuth 로그인시에는 Map<String,Object> attributes-> getAttributes로 받는다.
    public PrincipalDetail(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    //해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //휴면계정 등록하고 싶으면

        //현재시간-로그인시간 1년이 초과하면 return false, 아니면 true
        return true;
    }



    @Override
    public String getName() {
        return null;
    }
}
