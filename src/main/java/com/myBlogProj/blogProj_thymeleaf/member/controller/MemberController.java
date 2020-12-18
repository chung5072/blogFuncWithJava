package com.myBlogProj.blogProj_thymeleaf.member.controller;

import com.myBlogProj.blogProj_thymeleaf.member.Member;
import com.myBlogProj.blogProj_thymeleaf.member.dao.MemberDao;
import com.myBlogProj.blogProj_thymeleaf.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/*Spring Container에 해당 클래스에 대한 Bean 객체를 생성*/
@Controller
@RequestMapping("/member") // @RequestMapping에 공통된 부분
public class MemberController {
    /*블로그 만들기 프로젝트
     * 기반 > Spring MVC + Thymeleaf + Oracle Database*/
    
    /*MemberService > MemberDao를 통해 DB에 값을 넣거나 가져옴*/
    @Autowired
    MemberService memberService;
    @Autowired
    MemberDao memberDao;

    /*@RequestMapping(value = "/사용자가 누른 URL", method = Requested.GET / POST)
     * 해당 링크를 눌렀을 때, 아래의 메서드에 있는 기능이 실행됨.
     * URL은 ""로 둘러쌓음*/
    @RequestMapping(value = "/memberLoginPage", method = RequestMethod.GET)
    public String goToMemberLoginPage(){
        return "/member/memberLoginPage";
    }

    /*HttpSession을 활용하여 세션값을 이용
    * > 세션과 커맨드 객체를 활용하여 View에서 값을 보여줌
    * > th:text="${session.member.memberID}"*/
    @RequestMapping(value = "/memberLogin", method = RequestMethod.POST)
    public String memberLogin(Member member, HttpSession httpSession) {
        Member member_loginData = memberService.memberSearch(member);

        if (member_loginData == null) {
            System.out.println("Unknown User!");
            /*redirect 사용
            * > redirect:/이동할 URL*/
            return "/errorPage/failedMemberLogin";
        }

        List<Member> allMemberAccount = memberDao.findAll();
        List<Member> loginAccount = new ArrayList<>();

        for (Member member1 : allMemberAccount) {
            if (member1.getMemberID().equals(member_loginData.getMemberID())){
                loginAccount.add(member1);
            }
        }

        /*세션에 값을 넣음
        * > member라는 이름을 통해서 세션 사용
        * > th:text="${session.member.memberID}"*/
        httpSession.setAttribute("member", loginAccount.get(0));
//        httpSession.setAttribute("adminAccount", adminAccount);

        return "redirect:/index";
    }

    @RequestMapping(value = "/memberJoinPage", method = RequestMethod.GET)
    public String goToMemberJoinPage() {
        return "/member/memberJoinPage";
    }

    /*커맨드 객체 사용*/
    @RequestMapping(value = "/memberJoin", method = RequestMethod.POST)
    public String memberJoin(Member member) {
        /*MemberService > MemberDao를 통해서 DB에 값을 넣음*/
        memberService.memberRegister(member);

        return "/member/memberLoginPage";
    }

    /*HttpSession을 활용하여 세션값을 이용
     * > 세션과 커맨드 객체를 활용하여 View에서 값을 보여줌
     * > th:text="${session.member.memberID}"*/
    @RequestMapping(value = "/memberLogout", method = RequestMethod.GET)
    public String memberLogout(Member member, HttpSession httpSession) {
        /*세션에 있는 값을 삭제
        * > 해당 값을 삭제함으로 자연스럽게 
        * > th:if="${session.isEmpty()}" 조건이 실행되며
        * 로그아웃이 됨*/
        httpSession.invalidate();

        return "redirect:/index";
    }

    @RequestMapping(value = "/memberSearchPage", method = RequestMethod.GET)
    public String goToMemberSearchPage() {
        return "/member/memberSearchPage";
    }

    @RequestMapping(value = "/findMemberID", method = RequestMethod.POST)
    public String findMemberID(Member member, Model model) {
        List<Member> memberList = memberDao.findAll();

        for (Member value : memberList) {
            if (value.getMemberNickname().equals(member.getMemberNickname())
                    && value.getMemberBirth().equals(member.getMemberBirth())) {
                String getMemberID = memberDao.findMemberID(member);

                model.addAttribute("getMemberID", getMemberID);

                return "/member/searchMemberID";
            }
        }

        return "/errorPage/failedSearch";
    }

    @RequestMapping(value = "/resetMemberPWPage", method = RequestMethod.POST)
    public String resetMemberPWPage(Member member, Model model) {
        List<Member> memberList = memberDao.findAll();

        String memberID = member.getMemberID();

        for (Member member1 : memberList) {
            if (member1.getMemberID().equals(memberID)) {

                model.addAttribute("memberID", memberID);

                return "/member/resetMemberPWPage";
            }
        }

        return "/errorPage/failedSearch";
    }

    @RequestMapping(value = "/resetMemberPW", method = RequestMethod.POST)
    public String resetMemberPW(@RequestParam(value = "memberID", required=true)
                                            String memberID,
                                Member member, Model model) {
        int resultResetPW = memberDao.resetMemberPW(memberID, member);

        if (resultResetPW == 0){
            System.out.println("Reset Member's Password Failed!");
        }
        else{
            System.out.println("Reset Member's Password Success!");
        }

        return "/member/resetMemberPWPage";
    }

    @RequestMapping(value = "/modifyInfoPage", method = RequestMethod.GET)
    public String goToModifyInfoPage() {
        return "/member/modifyInfoPage";
    }

    @RequestMapping(value = "/modifyInfo", method = RequestMethod.POST)
    public String modifyInfo(HttpSession httpSession, Member member, Model model) {
        Member loginSession = (Member)httpSession.getAttribute("member");
        String loginSessionID = loginSession.getMemberID();

        List<Member> memberList = memberDao.findAll();

        for (Member member1 : memberList) {
            if (member1.getMemberID().equals(loginSessionID)) {
                if (member.getMemberPW().isEmpty()) {
                    member.setMemberPW(member1.getMemberPW());
                }
                if (member.getMemberNickname().isEmpty()) {
                    member.setMemberNickname(member1.getMemberNickname());
                }
                if (member.getMemberBirth().isEmpty()) {
                    member.setMemberBirth(member1.getMemberBirth());
                }
                if (member.getMemberIntroWord().isEmpty()) {
                    member.setMemberIntroWord(member1.getMemberIntroWord());
                }
            }
        }

        int resultModifyInfo = memberDao.modifyMemberInfo(loginSessionID, member);
        httpSession.invalidate();
//        httpSession.setAttribute("member", member);

        if (resultModifyInfo == 0){
            System.out.println("Modify Member's Info Failed!");
        }
        else{
            System.out.println("Modify Member's Info Success!");
        }

        return "redirect: /index";
    }

    @RequestMapping(value = "/removeMember", method = RequestMethod.GET)
    public String removeMember(HttpSession httpSession) {
        Member loginSession = (Member)httpSession.getAttribute("member");
        String loginMemberID = loginSession.getMemberID();

        List<Member> memberList = memberDao.findAll();

        for (Member member1 : memberList) {
            if (member1.getMemberID().equals(loginMemberID)) {
                int resultRemoveMember = memberDao.removeMember(loginMemberID);

                if (resultRemoveMember == 0){
                    System.out.println("Remove Member's Info Failed!");
                }
                else{
                    System.out.println("Remove Member's Info Success!");
                }
            }
        }

        httpSession.invalidate();

        return "redirect: /index";
    }
}
