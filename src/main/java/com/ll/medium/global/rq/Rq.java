package com.ll.medium.global.rq;

import com.ll.medium.domain.exceptions.GlobalException.GlobalException;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rsData.RsData;
import com.ll.medium.global.security.SecurityUser;
import com.ll.medium.standard.util.Ut;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.URLEncoder;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequestScope
@Component
@RequiredArgsConstructor
@Slf4j
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;
    private SecurityUser securityUser;
    private Member member;

    @PostConstruct
    public void init() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof SecurityUser) {
            this.securityUser = (SecurityUser) authentication.getPrincipal();
        }
    }

    public String redirect(String path) {
        return "redirect:" + path;
    }

    public String redirect(String path, RsData<?> rsData) {
        return redirect(path, rsData.getMsg());
    }

    public String redirect(String path, String msg) {
        if (msg == null) return "redirect:" + path;

        boolean containsTtl = msg.contains(";ttl=");

        if (containsTtl) {
            msg = msg.split(";ttl=", 2)[0];
        }

        msg = URLEncoder.encode(msg, UTF_8);
        msg += ";ttl=" + (new Date().getTime() + 1000 * 5);

        return redirect(path) + "?msg=" + msg;
    }

    public String historyBack(String msg) {
        resp.setStatus(400);
        req.setAttribute("msg", msg);

        return "global/js";
    }

    public String historyBack(RsData<?> rs) {
        return historyBack(rs.getMsg());
    }

    public String historyBack(GlobalException ex) {
        String exStr = Ut.exception.toString(ex);

        req.setAttribute("exStr", exStr);
        log.debug(exStr);

        return historyBack(ex.getRsData().getMsg());
    }

    public String redirectOrBack(String url, RsData<?> rs) {
        if (rs.isFail()) return historyBack(rs);
        return redirect(url, rs);
    }

    public boolean isLogined() {
        return securityUser != null;
    }

    public String getMemberUsername() {
        return securityUser.getUsername();
    }

    public Member getMember() {
        if (!isLogined()) {
            return null;
        }

        if (member == null)
            member = memberService.findByUsername(getMemberUsername()).get();


        return member;
    }

    public String getEncodedCurrentUrl() {
        return Ut.url.encode(getCurrentUrl());
    }

    private String getCurrentUrl() {
        String url = req.getRequestURI();
        String queryString = req.getQueryString();

        if (queryString != null) {
            url += "?" + queryString;
        }

        return url;
    }
}
