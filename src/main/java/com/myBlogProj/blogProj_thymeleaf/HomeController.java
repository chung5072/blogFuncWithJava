package com.myBlogProj.blogProj_thymeleaf;

import com.myBlogProj.blogProj_thymeleaf.article.Article;
import com.myBlogProj.blogProj_thymeleaf.article.Comment;
import com.myBlogProj.blogProj_thymeleaf.article.FileContent;
import com.myBlogProj.blogProj_thymeleaf.article.dao.ArticleDao;
import com.myBlogProj.blogProj_thymeleaf.article.service.ArticleService;
import com.myBlogProj.blogProj_thymeleaf.blog.Background;
import com.myBlogProj.blogProj_thymeleaf.blog.dao.BlogDao;
import com.myBlogProj.blogProj_thymeleaf.category.Category;
import com.myBlogProj.blogProj_thymeleaf.category.service.CategoryService;
import com.myBlogProj.blogProj_thymeleaf.member.Member;
import com.myBlogProj.blogProj_thymeleaf.member.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.*;

/*Spring Container에 해당 클래스에 대한 Bean 객체를 생성*/
@Controller
public class HomeController {
    /*블로그 만들기 프로젝트
    * 기반 > Spring MVC + Thymeleaf + Oracle Database*/
    @Autowired
    CategoryService categoryService;
    @Autowired
    ArticleService articleService;
    @Autowired
    MemberDao memberDao;
    @Autowired
    BlogDao blogDao;
    @Autowired
    ArticleDao articleDao;

    /*@RequestMapping(value = "/사용자가 누른 URL", method = Requested.GET / POST)
    * 해당 링크를 눌렀을 때, 아래의 메서드에 있는 기능이 실행됨.
    * URL은 ""로 둘러쌓음*/
    /*여러 URL을 넣기 위해서 {"/1번 URL", "/2번 URL"}을 사용*/
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String goToIndexPage(HttpSession httpSession, Model model) throws UnsupportedEncodingException {
        Member loginSession = (Member)httpSession.getAttribute("member");

        String blogTitle = blogDao.getBlogTitle();

        List<Member> allMemberAccount = memberDao.findAll();

        /*DB 데이터 가져오기 > http://zetcode.com/springboot/thymeleaf/
         * CategoryService.java > findAll()*/
        List<Category> categories = categoryService.findAll();

        List<Article> articles = articleService.findAllArticle();
        List<Article> articlesResult_category = new ArrayList<>();
        List<Article> articlesResult_contents = new ArrayList<>();

        List<Comment> comments = articleDao.findAllComment();
        List<Comment> newComments = new ArrayList<>();

        int categorySize = categories.size();

        int articleSize = articles.size();
        int whileValue_article = 0;

        int commentSize = comments.size();
        int whileValue_comment = 0;

        Map<String, Object> allMember_dbData_map = new HashMap<>();
        Map<String, Object> category_dbData_map = new HashMap<>();
        Map<String, Object> article_dbData_map = new HashMap<>();
        /*categories를 어디에서 사용하는지는 의문
         * > 아마 index.html
         * > th:each="category : ${categories}"*/
        allMember_dbData_map.put("allMemberAccount", allMemberAccount);
        category_dbData_map.put("categories", categories);
        article_dbData_map.put("articles", articles);

        String adminID = allMemberAccount.get(0).getMemberID();
        String adminNickname = allMemberAccount.get(0).getMemberNickname();
        String adminIntroWord = allMemberAccount.get(0).getMemberIntroWord();

        for(int category1 = 0; category1 < categorySize; category1++) {
            if (loginSession == null) {
                if (categories.get(category1).getCategoryClosed().equals("비공개")) {
                    categories.remove(category1);
                    categorySize = categorySize - 1;
                    category1 = category1 - 1;
                }
            } else {
                if (!loginSession.getMemberID().equals(adminID)) {
                    if (categories.get(category1).getCategoryClosed().equals("비공개")) {
                        categories.remove(category1);
                        categorySize = categorySize - 1;
                        category1 = category1 - 1;
                    }
                }
            }
        }

        while(whileValue_article < articleSize) {
            if(articles.get(whileValue_article).getArticleCategory().equals(categories.get(0).getCategoryTitle())) {
                articlesResult_category.add(articles.get(whileValue_article));
            }
            whileValue_article = whileValue_article + 1;
        }

        for (Article article1 : articles) {
            if (article1.getArticleCategory().equals(categories.get(0).getCategoryTitle())) {
                articlesResult_contents.add(article1);
                break;
            }
        }

        String fileName = articlesResult_contents.get(0).getArticleFileName();

        while (whileValue_comment < commentSize) {
            if (comments.get(whileValue_comment).getCommentTitle().equals(articlesResult_contents.get(0).getArticleTitle())) {
                newComments.add(comments.get(whileValue_comment));
            }

            whileValue_comment = whileValue_comment + 1;
        }

        model.addAttribute("blogTitle", blogTitle);
        model.addAttribute("adminID", adminID);
        model.addAttribute("adminNickname", adminNickname);
        model.addAttribute("adminIntroWord", adminIntroWord);
        model.addAttribute("categories", categories);
        model.addAttribute("articles_category", articlesResult_category);
        model.addAttribute("articles_contents", articlesResult_contents);
        model.addAttribute("article_fileName", fileName);
        model.addAttribute("articleComments", newComments);

        /*ModelAndView > View의 이름 + 넘길 값*/
//        return new ModelAndView("/index", category_dbData_map);
        return "/index";
    }

    @RequestMapping(value = "/backgroundImage", method = RequestMethod.GET)
    public String backgroundImage(Model model) throws UnsupportedEncodingException {
        List<Background> backgrounds = blogDao.findAllBackground();
        List<String> backgroundImages = new ArrayList<>();

        int backgroundSize = backgrounds.size();
        int whileValue_background = 0;

        while(whileValue_background < backgroundSize) {
            if(backgrounds.get(whileValue_background).getBackgroundSetting().equals("T")) {
                byte[] encode = Base64.getEncoder().encode(backgrounds.get(whileValue_background).getBackgroundImage());
                String backgroundEncodes = new String(encode, "UTF-8");
                backgroundImages.add(backgroundEncodes);
            }
            whileValue_background = whileValue_background + 1;
        }

        try {
            model.addAttribute("backgroundImage", backgroundImages.get(0));
        } catch (IndexOutOfBoundsException ignored) {
            model.addAttribute("backgroundImage", "none");
        }

        return "/backgroundImage";
    }
}
