package com.somei.student_management_system;

import com.somei.student_management_system.login.domain.service.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // パスワードエンコーダーのBean定義
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // データソース
    @Autowired
    private DataSource dataSource;

    // ユーザーIDとパスワードを取得するSQL文
    private static final String USER_SQL = "SELECT"
            + " user_id,"
            + " password,"
            + " true"
            + " FROM"
            + " s_user"
            + " WHERE"
            + " user_id = ?";

    // ユーザーのロールを取得するSQL文
    private static final String ROLE_SQL = "SELECT"
            + " user_id,"
            + " role"
            + " FROM"
            + " s_user"
            + " WHERE"
            + " user_id = ?";

    @Override
    public void configure(WebSecurity web) throws Exception {

        // 静的リソースへのアクセスには、セキュリティを適用しない
        web.ignoring().antMatchers("/webjars/**", "/css/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 直リンクの禁止：ログイン不要ページの設定
        http
                .authorizeRequests()
                    .antMatchers("/webjars/**").permitAll()  // webjarsへのアクセス許可
                    .antMatchers("/css/**").permitAll()  // cssへのアクセス許可
                    .antMatchers("/login").permitAll()  // ログインページは直リンクOK
                    .antMatchers("/admin").hasAuthority("ROLE_ADMIN")
                    .anyRequest().authenticated();  // それ以外は直リンク禁止

        // ログイン処理
        http
                .formLogin()
                    .loginProcessingUrl("/login")  // ログイン処理のパス
                    .loginPage("/login")  // ログインページの指定
                    .failureUrl("/login")  // ログイン失敗時の遷移先
                    .usernameParameter("userId")  // ログインページのユーザーID
                    .passwordParameter("password")  // ログインページのパスワード
                    .defaultSuccessUrl("/home", true);

        // ログアウト処理
        http
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // ログイン処理時のユーザー情報を、DBから取得する
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(USER_SQL)
                .authoritiesByUsernameQuery(ROLE_SQL)
                .passwordEncoder(passwordEncoder());

    }

}
