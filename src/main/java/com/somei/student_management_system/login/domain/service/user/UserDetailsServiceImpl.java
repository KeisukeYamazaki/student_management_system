package com.somei.student_management_system.login.domain.service.user;

import com.somei.student_management_system.login.domain.model.LoginUser;
import com.somei.student_management_system.login.domain.model.User;
import com.somei.student_management_system.login.domain.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userDao.selectOneByName(userName);

        if (user == null) {
            throw new UsernameNotFoundException("userName" + userName + "was not found in the database");
        }

        // 権限のリスト DB上で権限テーブル、ユーザ権限テーブルを作成し管理が必要
        // GENERALのみ今回作成
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("GENERAL");
        grantedAuthorityList.add(authority);

        // BCryptアルゴリズムで暗号化
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserDetails userDetails = (UserDetails) new LoginUser(user.getUserName(), encoder.encode(user.getPassword()),
                grantedAuthorityList);
        return userDetails;
    }
}
