package com.somei.student_management_system.login.domain.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class SessionData implements Serializable {
    private static final long serialVersionUID = 1L;
    String str1;
    String str2;
    String str3;
    double num35;
    double num44;
    double num53;
}
