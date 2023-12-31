package com.in4mation.festibook.controller.login;

import com.in4mation.festibook.dto.member.MemberDTO;
import com.in4mation.festibook.exception.LoginException;
import com.in4mation.festibook.jwt.JwtUtils;
import com.in4mation.festibook.service.login.LoginService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    // AuthenticationManager를 스프링에서 자동으로 주입받아 사용
    // 사용자 인증을 위해 필요합니다.
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    LoginService loginService;

    // JWT 토큰 생성을 위해 필요
    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody MemberDTO memberDTO){
        try {
            // member_id, password 체크
            MemberDTO authenticatedMember = loginService.checkLoin(memberDTO.getMember_id(), memberDTO.getMember_password());

            // JWT 토큰 생성 및 반환
            String jwt = jwtUtils.createAccessToken(authenticatedMember.getMember_id(), authenticatedMember.getMember_name());

            // 토큰을 응답 본문과 함께 전송
            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            response.put("memberId", authenticatedMember.getMember_id());//추가로 적은것
            System.out.println("Authenticated Member ID: " + authenticatedMember.getMember_id());
            return ResponseEntity.ok(response);
        }
        catch (LoginException e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (AuthenticationException e){
            // 인증 실패한 경우 에러 메세지 + 401 상태 코드 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패 : 아이디나 비밀번호 확인해주세요");
        }
        catch(Exception e){
            // 그 외 에러의 경우 500 메세지
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류");
        }
    }

    // JWT 토큰을 담을 내부 클래스를 정의
    @Getter
    @Setter
    class JwtResponse {
        private String token;

        // 생성자를 통해 토큰을 초기화
        public JwtResponse(String token) {
            this.token = token;
        }
    }

    @GetMapping("test")
    public String test() {
        return "test";
    }

}
