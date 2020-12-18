package com.myBlogProj.blogProj_thymeleaf.member.service;

import com.myBlogProj.blogProj_thymeleaf.member.Member;

public interface IMemberService {
    void memberRegister(Member member);
    Member memberSearch(Member member);
}
