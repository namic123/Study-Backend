package spring.springsecuritybasicutilize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 해당 클래스를 Spring 설정 클래스로 등록
@EnableWebSecurity(debug = true) // Spring Security를 활성화하며, 내부적으로 FilterChain을 자동 구성함
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 🔐 URL 별 인가(Authorization) 규칙 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );

        // 🧾 Form 로그인 설정
        http
                .formLogin((auth) -> auth
                        // 사용자 정의 로그인 페이지 경로 지정 (GET 요청)
                        .loginPage("/login")
                        // 로그인 form 데이터를 처리할 URL (POST 요청)
                        .loginProcessingUrl("/loginProc")
                        // 로그인 페이지 및 처리 URL은 모두 인증 없이 접근 가능
                        .permitAll()
                );

        // ❌ CSRF 보호 기능 비활성화 (개발 또는 API 서버 환경에서 주로 사용)
        http
                .csrf((auth) -> auth.disable());

        // 구성된 SecurityFilterChain 반환

        http
                .httpBasic(Customizer.withDefaults());  // HTTP Basic 인증 활성화

        return http.build();
    }

    // 세션 관련 보안 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        // 다중 로그인 제한
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));
        // 새로운 세션 부여
//        http
//                .sessionManagement((session) - session
//                        .sessionFixation((sessionFixation) -> sessionFixation
//                                .newSession()
//                        )
//                );


        // 세션 ID 변경
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());

        return http.build();
    }

    // 🔐 BCryptPasswordEncoder Bean 등록
    // 스프링 시큐리티의 로그인 인증 시, 입력한 비밀번호를 해시하여 DB의 해시값과 비교할 때 사용
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(); // 내부적으로 솔트를 포함한 해시값 생성
    }


}
