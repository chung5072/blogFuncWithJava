package com.myBlogProj.blogProj_thymeleaf.member;

public class Member {
    /*블로그 만들기 프로젝트
     * 기반 > Spring MVC + Thymeleaf + Oracle Database*/
    
    /*커맨드 객체로써 활용 > getter, setter가 반드시 필요
    * view - controller
    * view: th:text="${session.member.memberID}" / th:object="${member}"
    * controller - 메서드(Member member, 세션)*/
    private String memberID;
    private String memberPW;
    private String memberNickname;
    private String memberBirth;
    private String memberIntroWord;

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getMemberPW() {
        return memberPW;
    }

    public void setMemberPW(String memberPW) {
        this.memberPW = memberPW;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public String getMemberBirth() {
        return memberBirth;
    }

    public void setMemberBirth(String memberBirth) {
        this.memberBirth = memberBirth;
    }

    public String getMemberIntroWord() {
        return memberIntroWord;
    }

    public void setMemberIntroWord(String memberIntroWord) {
        this.memberIntroWord = memberIntroWord;
    }
}
