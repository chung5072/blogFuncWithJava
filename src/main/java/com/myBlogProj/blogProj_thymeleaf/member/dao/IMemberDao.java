package com.myBlogProj.blogProj_thymeleaf.member.dao;

import com.myBlogProj.blogProj_thymeleaf.member.Member;

import java.util.List;

public interface IMemberDao {
    int memberInsert(Member member);
    Member memberSelect(Member member);
    List<Member> findAll();
    int changeAdminNickname(String adminID, String adminNickName);
    int changeAdminIntroWord(String adminID, String adminIntroWord);
    String findMemberID(Member member);
    int resetMemberPW(String memberID, Member member);
    int modifyMemberInfo(String loginSessionID, Member member);
    int removeMember(String loginMemberID);
}
