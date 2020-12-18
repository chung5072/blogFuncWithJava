package com.myBlogProj.blogProj_thymeleaf.member.service;

import com.myBlogProj.blogProj_thymeleaf.member.Member;
import com.myBlogProj.blogProj_thymeleaf.member.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

/*Spring Container에 해당 클래스에 대한 Bean 객체를 생성*/
@Controller
public class MemberService implements IMemberService{
    /*블로그 만들기 프로젝트
     * 기반 > Spring MVC + Thymeleaf + Oracle Database*/

    /*Spring container에 있는 MemberDao를 사용
    * MemberDao > DB 연동과 관련된 객체(클래스)*/
    @Autowired
    MemberDao memberDao;

    /*인터페이스인 IMemberService에서 정의된 메서드를 기능 구현
    * 커맨드 객체인 Member를 활용*/
    public void memberRegister(Member member) {
        /*사용자 등록
        * 반환이 int형 > 0이면 값이 안들어간 것*/
        int resultRegister = memberDao.memberInsert(member);

        if (resultRegister == 0){
            System.out.println("Register Failed!");
        }
        else{
            System.out.println("Register Success!");
        }
    }

    public Member memberSearch(Member member) {
        /*사용자 검색 > 로그인된 사용자의 정보를 보여줌*/
        Member resultSearch = memberDao.memberSelect(member);

        if (resultSearch == null) {
            System.out.println("Login Failed!");
        }
        else {
            System.out.println("Login Success");
            System.out.println(member.getMemberID() + " User Login");
        }

        return resultSearch;
        /*MemberController에 값이 넘어감
        * Member member_loginData = memberService.memberSearch(member);*/
    }
}
