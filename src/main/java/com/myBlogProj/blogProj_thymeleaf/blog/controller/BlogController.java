package com.myBlogProj.blogProj_thymeleaf.blog.controller;

import com.myBlogProj.blogProj_thymeleaf.article.controller.ArticleController;
import com.myBlogProj.blogProj_thymeleaf.blog.Background;
import com.myBlogProj.blogProj_thymeleaf.blog.dao.BlogDao;
import com.myBlogProj.blogProj_thymeleaf.member.Member;
import com.myBlogProj.blogProj_thymeleaf.member.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/*Spring Container에 해당 클래스에 대한 Bean 객체를 생성*/
@Controller
@RequestMapping("/blog") // @RequestMapping에 공통된 부분
public class BlogController {
    /*블로그 만들기 프로젝트
     * 기반 > Spring MVC + Thymeleaf + Oracle Database*/

    @Autowired
    BlogDao blogDao;
    @Autowired
    MemberDao memberDao;

    public static List<String> hello(BlogDao blogDao) throws UnsupportedEncodingException {
        List<Background> backgrounds = blogDao.findAllBackground();
        List<String> backgroundImages = new ArrayList<>();

        for(Background background: backgrounds) {
            byte[] encode = Base64.getEncoder().encode(background.getBackgroundImage());
            String backgroundEncodes = new String(encode, "UTF-8");
            backgroundImages.add(backgroundEncodes);
        }

        return backgroundImages;
    }

    /*@RequestMapping(value = "/사용자가 누른 URL", method = Requested.GET / POST)
     * 해당 링크를 눌렀을 때, 아래의 메서드에 있는 기능이 실행됨.
     * URL은 ""로 둘러쌓음*/
    @RequestMapping(value = "/blogSetting", method = RequestMethod.GET)
    public String goToBlogSettingPage(Model model) {
        String blogTitle = blogDao.getBlogTitle();

        List<Member> members = memberDao.findAll();

        String adminNickname = members.get(0).getMemberNickname();
        String adminIntroWord = members.get(0).getMemberIntroWord();

        model.addAttribute("blogTitle", blogTitle);
        model.addAttribute("adminNickname", adminNickname);
        model.addAttribute("adminIntroWord", adminIntroWord);

        return "/blog/blogSetting";
    }

    @RequestMapping(value = "/blogBackgroundSetting", method = RequestMethod.GET)
    public String goToBlogBackgroundSettingPage(Model model) throws UnsupportedEncodingException {
        List<String> hello = hello(blogDao);
        model.addAttribute("images", hello);
        return "/blog/blogBackgroundSetting";
    }

    @RequestMapping(value = "/blogBackgroundUploadPage", method = RequestMethod.GET)
    public String goToBlogBackgroundUploadPage() {
        return "/blog/blogBackgroundUploadPage";
    }

    @RequestMapping(value = "/blogBackgroundUpload", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, headers = ("content-type=multipart/*"))
    public String blogBackgroundUpload(@RequestParam("backgroundID") int backgroundID,
                                       @RequestParam("backgroundTitle") String backgroundTitle,
                                       @RequestParam("backgroundImage") MultipartFile backgroundImage)
            throws IOException {
        int resultInsert = blogDao.imageInsert(backgroundID, backgroundTitle, backgroundImage);

        if(resultInsert == 0) {
            System.out.println("Background insert failed!");
        }
        else {
            System.out.println("Background insert success!");
        }

        return "/blog/blogBackgroundUploadPage";
    }

    @RequestMapping(value = "/getSelectedImageSrc", method = RequestMethod.POST)
    public String getSelectedImageSrc(@RequestParam("inputImageSrc") String imageLabel) {
        int selectedImageCount = Integer.parseInt(imageLabel);
        /*일단 기존의 테이블 정보를 받아서 t/f를 확인
        * 새로 받은 숫자의 t/f를 확인
        * 다를 경우에 기존의 t를 f로, default의 f를 t로 변경
        * 변경한 t의 인덱스 값을 전송?
        * 그리고 t의 인덱스 값의 html로 받아서 배경화면으로 변경*/

        List<Background> backgrounds = blogDao.findAllBackground();

        int backgroundCount = 0;
        int backgroundSize = backgrounds.size();

        while(backgroundCount < backgroundSize) {
            if(backgrounds.get(backgroundCount).getBackgroundSetting().equals("T")) {
                if(backgrounds.get(backgroundCount).getBackgroundID() == selectedImageCount) {
                    break;
                }
                else {
                    int resultReleaseBackground = blogDao.releaseBackground(backgrounds.get(backgroundCount).getBackgroundID());

                    if(resultReleaseBackground == 0) {
                        System.out.println("Background Release failed!");
                    }
                    else {
                        System.out.println("Background Release success!");
                    }
                }
            }
            else{
                if(backgrounds.get(backgroundCount).getBackgroundID() == selectedImageCount) {
                    int resultSetBackground = blogDao.setAsBackground(selectedImageCount);

                    if(resultSetBackground == 0) {
                        System.out.println("Background Set failed!");
                    }
                    else {
                        System.out.println("Background Set success!");
                    }
                }
            }

            backgroundCount = backgroundCount + 1;
        }

        return "redirect: /index";
    }

    @RequestMapping(value = "/blogBackgroundReset", method = RequestMethod.GET)
    public String blogBackgroundReset() {
        int resultReset = blogDao.resetBackground();

        if(resultReset == 0) {
            System.out.println("Background Reset failed!");
        }
        else {
            System.out.println("Background Reset success!");
        }


        return "redirect: /index";
    }

    @RequestMapping(value = "/adminNicknameSetting", method = RequestMethod.POST)
    public String changeAdminNickname(HttpSession httpSession, Member member) {
        String adminNickname = member.getMemberNickname();

        List<Member> memberList = memberDao.findAll();
        Member modifiedMember = new Member();

        String adminID = memberList.get(0).getMemberID();

        for (Member member1 : memberList) {
            if (member1.getMemberID().equals(adminID)) {
                modifiedMember.setMemberID(member1.getMemberID());
                modifiedMember.setMemberPW(member1.getMemberPW());
                modifiedMember.setMemberNickname(adminNickname);
                modifiedMember.setMemberBirth(member1.getMemberBirth());
                modifiedMember.setMemberIntroWord(member1.getMemberIntroWord());
            }
        }

        int resultChangeAdminNickname = memberDao.changeAdminNickname(adminID, adminNickname);
        httpSession.setAttribute("member", modifiedMember);

        if(resultChangeAdminNickname == 0) {
            System.out.println("Admin's nickname change failed!");
        }
        else {
            System.out.println("Admin's nickname change success!");
        }

        return "redirect: /index";
    }

    @RequestMapping(value = "/adminIntroWordSetting", method = RequestMethod.POST)
    public String changeAdminIntroWord(HttpSession httpSession, Member member) {
        String adminIntroWord = member.getMemberIntroWord();

        List<Member> memberList = memberDao.findAll();
        Member modifiedMember = new Member();

        String adminID = memberList.get(0).getMemberID();

        for (Member member1 : memberList) {
            if (member1.getMemberID().equals(adminID)) {
                modifiedMember.setMemberID(member1.getMemberID());
                modifiedMember.setMemberPW(member1.getMemberPW());
                modifiedMember.setMemberNickname(member1.getMemberNickname());
                modifiedMember.setMemberBirth(member1.getMemberBirth());
                modifiedMember.setMemberIntroWord(adminIntroWord);
            }
        }

        int resultChangeAdminIntroWord = memberDao.changeAdminIntroWord(adminID, adminIntroWord);
        httpSession.setAttribute("member", modifiedMember);

        if(resultChangeAdminIntroWord == 0) {
            System.out.println("Admin's Intro word change failed!");
        }
        else {
            System.out.println("Admin's Intro word change success!");
        }

        return "redirect: /index";
    }

    @RequestMapping(value = "/blogTitleSetting", method = RequestMethod.POST)
    public String changeBlogTitle(@RequestParam("newBlogTitle") String newBlogTitle) {
        int resultChangeBlogTitle = blogDao.changeBlogTitle(newBlogTitle);

        if(resultChangeBlogTitle == 0) {
            System.out.println("Blog's Title change failed!");
        }
        else {
            System.out.println("Blog's Title change success!");
        }

        return "redirect: /index";
    }
}
