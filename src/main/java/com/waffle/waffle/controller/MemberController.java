package com.waffle.waffle.controller;

import com.waffle.waffle.domain.DTO.LoginDTO;
import com.waffle.waffle.domain.DTO.MemberDTO;
import com.waffle.waffle.domain.DTO.TokenDTO;
import com.waffle.waffle.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 메인 페이지
    @GetMapping("/main")
    public ResponseEntity<String> main() {
        return ResponseEntity.ok("메인 로그인 페이지");
    }

    // 로그인, id와 pw가 저장된 request body를 자바 객체로 변환하고 로그인 과정에 사용
    @PostMapping("/main")
    // @RequestBody: HTTP Request Body 내용을 통째로 자바 객체로 변환해서 매핑된 메소드 파라미터로 전달
    public TokenDTO login(@RequestBody LoginDTO memberLoginRequestDto) {
        String memberId = memberLoginRequestDto.getMemberId();
        String password = memberLoginRequestDto.getPassword();
        TokenDTO tokenDTO = memberService.login(memberId, password);
        return tokenDTO;
    }

    // 로그인 시 권한에 해당하는 화면으로 리다이렉션
    @GetMapping("/login")
    public RedirectView login2(Authentication authentication) throws Exception{
        RedirectView redirectView = new RedirectView();
        if(authentication.getAuthorities().toArray()[0].toString().equals("ROLE_ADMIN")) {
            redirectView.setUrl("/admin");
            return redirectView;
        }
        else if(authentication.getAuthorities().toArray()[0].toString().equals("ROLE_MEMBER")) {
            redirectView.setUrl("/member");
            return redirectView;
        }
        else {
            throw new Exception("권한 오류");
        }
    }

    // 멤버 전용 페이지
    @GetMapping("/member")
    public ResponseEntity<MemberDTO> user(Principal principal) {
        return ResponseEntity.ok(memberService.info(principal));
    }

}