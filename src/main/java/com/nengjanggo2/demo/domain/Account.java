package com.nengjanggo2.demo.domain;

import lombok.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString(of = "id")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    //TODO: 암호 해시화 후 JPA통하여 비밀번호 DB에 설정
    private String password;

    // 이메일 인증 후 True 반환
    private boolean emailVerified;

    // 이메일 인증 요청시 비교될 값
    private String emailCheckToken;

    // authentication 이메일 전송시 전송 가능 유무 확인 시 사용
    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt;

    // 1:n 관계 매핑 (Account :1 = Food : n)
    @OneToMany(mappedBy = "account")
    private List<Food> foods;

    // 랜덤값 생성 후 String 타입으로 저장
    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }
    // authentication 이메일 컨펌 후 회원가입 완료
    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }
    // authentication 이메일 전송 가능 유무 확인 1시간 마다 가능
    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }
}
