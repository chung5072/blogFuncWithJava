package com.myBlogProj.blogProj_thymeleaf.article.controller;

import com.myBlogProj.blogProj_thymeleaf.article.Article;
import com.myBlogProj.blogProj_thymeleaf.article.Comment;
import com.myBlogProj.blogProj_thymeleaf.article.dao.ArticleDao;
import com.myBlogProj.blogProj_thymeleaf.article.service.ArticleService;
import com.myBlogProj.blogProj_thymeleaf.blog.controller.BlogController;
import com.myBlogProj.blogProj_thymeleaf.blog.dao.BlogDao;
import com.myBlogProj.blogProj_thymeleaf.category.Category;
import com.myBlogProj.blogProj_thymeleaf.category.service.CategoryService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/*Spring Container에 해당 클래스에 대한 Bean 객체를 생성*/
@Controller
@RequestMapping("/article") // @RequestMapping에 공통된 부분
public class ArticleController {
    /*블로그 만들기 프로젝트
     * 기반 > Spring MVC + Thymeleaf + Oracle Database*/

    @Autowired
    BlogDao blogDao;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleDao articleDao;
    @Autowired
    MemberDao memberDao;

//    SimpleDateFormat timeFormat = new SimpleDateFormat( "yyyy년 MM월dd일 HH시mm분ss초");
//    Date timeValue = new Date();
//    String currentTime = timeFormat.format(timeValue);

    /*@RequestMapping(value = "/사용자가 누른 URL", method = Requested.GET / POST)
     * 해당 링크를 눌렀을 때, 아래의 메서드에 있는 기능이 실행됨.
     * URL은 ""로 둘러쌓음*/
    @RequestMapping(value = "/newArticle", method = RequestMethod.GET)
    public ModelAndView goToNewArticlePage() throws UnsupportedEncodingException {
        List<String> hello = BlogController.hello(blogDao);
        String hello2 = hello.get(0);

        List<Category> categoryList = categoryService.findAll();

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/article/newArticle");
        modelAndView.addObject("backgroundImage", hello2);
        modelAndView.addObject("categoryList", categoryList);

        return modelAndView;
    }

    @RequestMapping(value = "/writeArticle", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, headers = ("content-type=multipart/*"))
    public String writeNewArticle(@RequestParam("articleTitle") String articleTitle,
                                  @RequestParam("articleAuthor") String articleAuthor,
                                  @RequestParam("articleDate") String articleDate,
                                  @RequestParam("articleCategory") String articleCategory,
                                  @RequestParam("articleContents") String articleContents,
                                  @RequestParam("articleFileName") String articleFileName,
                                  @RequestParam("articleFileContents") MultipartFile articleFileContents
                                  ) throws IOException {
        byte[] fileUploadBytes = articleFileContents.getBytes();

        if (articleFileName.equals("")) {
            articleFileName = null;
            fileUploadBytes = null;
        }

        Article newArticle = new Article();
        newArticle.setArticleTitle(articleTitle);
        newArticle.setArticleAuthor(articleAuthor);
        newArticle.setArticleDate(articleDate);
        newArticle.setArticleCategory(articleCategory);
        newArticle.setArticleContents(articleContents);
        newArticle.setArticleFileName(articleFileName);
        newArticle.setArticleFileContents(fileUploadBytes);

        articleService.articleRegister(newArticle);

        return "redirect:/index";
    }

    @RequestMapping(value ="/showArticleCategory")
    public String showArticleCategory(
            @RequestParam(value = "articleCategory_json", required=true)
                    String articleCategory_json,
            @RequestParam(value = "articleTitle_json", required = true)
                    String articleTitle_json,
            Model model, HttpSession httpSession, HttpServletRequest request)
            throws Exception {
        /*THYMELEAF URL을 통한 값을 전달 받음
        * - 참고 사이트: https://nahosung.tistory.com/80
        * - showCategoriesTitle.html
        * > th:href="@{/article/showArticleCategory(articleCategory_json=${category.getCategoryTitle()})}"
        * - ArticleController.java
        * > @RequestParam(value="articleCategory_json", required=true) String articleCategory_json*/
        String articleCategory = articleCategory_json;
        String articleTitle = articleTitle_json;

        Member loginSession = (Member)httpSession.getAttribute("member");

        String blogTitle = blogDao.getBlogTitle();

        List<Member> allMemberAccount = memberDao.findAll();

        List<Category> categories = categoryService.findAll();

        List<Article> articles = articleService.findAllArticle();
        List<Article> articlesResult_category = new ArrayList<>();
        List<Article> articlesResult_contents = new ArrayList<>();

        List<Comment> comments = articleDao.findAllComment();
        List<Comment> newComments = new ArrayList<>();

        int categorySize = categories.size();

        int articleSize = articles.size();
        int whileValue_articleCategory = 0;
        int whileValue_articleContents = 0;

        String fileName = null;

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

        for (Article article1 : articles) {
            if (article1.getArticleCategory().equals(articleCategory)) {
                articlesResult_category.add(article1);
            }
        }

        int articleSize_category = articlesResult_category.size();

        if(articleTitle.equals("0")) {
            try{
                articlesResult_contents.add(articlesResult_category.get(0));

                fileName = articlesResult_contents.get(0).getArticleFileName();
            } catch (IndexOutOfBoundsException exception) {
                Article article = new Article();
                article.setArticleTitle("아직 내용이 없어요");
                article.setArticleContents("내용을 입력해주세요");

                articlesResult_contents.add(article);

                fileName = null;
            }
        }
        else {
            for (Article article : articlesResult_category) {
                if (article.getArticleTitle().equals(articleTitle)) {
                    articlesResult_contents.add(article);

                    fileName = articlesResult_contents.get(0).getArticleFileName();
                    break;
                }
            }
        }

        while (whileValue_comment < commentSize) {
            if (articleTitle_json.equals("0")) {
                if(comments.get(whileValue_comment).getCommentTitle().equals(articlesResult_contents.get(0).getArticleTitle())) {
                    newComments.add(comments.get(whileValue_comment));
                }
            }
            else if (comments.get(whileValue_comment).getCommentTitle().equals(articleTitle)) {
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

        return "/index";
    }

    @RequestMapping(value = "/searchArticleTitle", method = RequestMethod.GET)
    public String searchArticleTitle(Article article, Model model, HttpSession httpSession) {
        Member loginSession = (Member)httpSession.getAttribute("member");

        String articleTitle = article.getArticleTitle();

        List<Member> memberList = memberDao.findAll();

        List<Category> categories = categoryService.findAll();
        List<Category> openCategory = new ArrayList<>();

        List<Article> articles = articleService.findAllArticle();
        List<Article> censoredArticles = new ArrayList<>();
        List<Article> articleSearchResult = new ArrayList<>();

        int categorySize = categories.size();
        int whileValue_compareCategoryTitle = 0;

        int articleSize = articles.size();
        int whileValue_compareAriticleTitle = 0;

        String adminID = memberList.get(0).getMemberID();

        if (loginSession == null) {
            while (whileValue_compareCategoryTitle < categorySize) {
                if (categories.get(whileValue_compareCategoryTitle).getCategoryClosed().equals("공개")) {
                    openCategory.add(categories.get(whileValue_compareCategoryTitle));
                }

                whileValue_compareCategoryTitle = whileValue_compareCategoryTitle + 1;
            }

            for (Category category : openCategory) {
                for (Article value : articles) {
                    if (category.getCategoryTitle().equals(value.getArticleCategory())) {
                        censoredArticles.add(value);
                    }
                }
            }

            int censoredArticleSize = censoredArticles.size();
            int whileValue_censoredArticle = 0;

            while (whileValue_censoredArticle < censoredArticleSize) {
                if(censoredArticles.get(whileValue_censoredArticle).getArticleTitle().contains(articleTitle)) {
                    articleSearchResult.add(censoredArticles.get(whileValue_censoredArticle));
                }

                whileValue_censoredArticle = whileValue_censoredArticle + 1;
            }
        } else if (!loginSession.getMemberID().equals(adminID)) {
            while (whileValue_compareCategoryTitle < categorySize) {
                if (categories.get(whileValue_compareCategoryTitle).getCategoryClosed().equals("공개")) {
                    openCategory.add(categories.get(whileValue_compareCategoryTitle));
                }

                whileValue_compareCategoryTitle = whileValue_compareCategoryTitle + 1;
            }

            for (Category category : openCategory) {
                for (Article value : articles) {
                    if (category.getCategoryTitle().equals(value.getArticleCategory())) {
                        censoredArticles.add(value);
                    }
                }
            }

            int censoredArticleSize = censoredArticles.size();
            int whileValue_censoredArticle = 0;

            while (whileValue_censoredArticle < censoredArticleSize) {
                if(censoredArticles.get(whileValue_censoredArticle).getArticleTitle().contains(articleTitle)) {
                    articleSearchResult.add(censoredArticles.get(whileValue_censoredArticle));
                }

                whileValue_censoredArticle = whileValue_censoredArticle + 1;
            }
        }
        else {
            while(whileValue_compareAriticleTitle < articleSize) {
                /*해당하는 값이 일체하는 동안 내용이 보임.*/
                if(articles.get(whileValue_compareAriticleTitle).getArticleTitle().contains(articleTitle)) {
                    articleSearchResult.add(articles.get(whileValue_compareAriticleTitle));

                }
                whileValue_compareAriticleTitle = whileValue_compareAriticleTitle + 1;
            }
        }


        model.addAttribute("articleTitle", articleTitle);
        model.addAttribute("articleSearchResult", articleSearchResult);
        return "/article/searchArticleTitle";
    }

    @RequestMapping(value = "/goToGetFilePage", method = RequestMethod.GET)
    public String goToGetFilePage(@RequestParam(value = "articleTitle", required=true) String articleTitle,
                                  @RequestParam(value = "fileName", required=true) String fileName, Model model)
            throws UnsupportedEncodingException {
        System.out.println(fileName);
        List<Article> articles = articleService.findAllArticle();
        byte[] articleFileContents = new byte[0];

        for (Article article : articles) {
            if (article.getArticleTitle().equals(articleTitle)) {
                if (article.getArticleFileName().equals(fileName)) {

                    articleFileContents = article.getArticleFileContents();
                    break;
                }
            }
        }

        byte[] encode = Base64.getEncoder().encode(articleFileContents);
        String fileEncoded = new String(encode, "UTF-8");

        model.addAttribute("articleFileName", fileName);
        model.addAttribute("articleFileContents", fileEncoded);

        return "/article/getArticleFile";
    }

    @RequestMapping(value = "/goToModifyArticlePage", method = RequestMethod.GET)
    public String goToModifyArticlePage(
            @RequestParam(value = "articleTitle", required=true)
                    String articleTitle, Model model) {
        List<Article> articleList = articleDao.findAllArticle();

        for (Article article1 : articleList) {
            if (article1.getArticleTitle().equals(articleTitle)) {
                model.addAttribute("articleTitle", articleTitle);
                model.addAttribute("currentArticle", article1);
            }
        }

        return "/article/modifyArticle";
    }

    @RequestMapping(value = "/modifyArticle", method = RequestMethod.POST)
    public String modifyArticle(@RequestParam("articleTitle") String articleTitle,
                                @RequestParam("modifyArticleTitle") String modifyArticleTitle,
                                @RequestParam("modifyArticleContents") String modifyArticleContents,
                                @RequestParam("modifyArticleFileName") String modifyArticleFileName,
                                @RequestParam("modifyArticleFileContents") MultipartFile modifyArticleFileContents,
                                Model model) throws IOException {
        Article modifyArticle = new Article();

        byte[] fileUploadBytes = modifyArticleFileContents.getBytes();

        List<Article> articleList = articleDao.findAllArticle();

        for (Article article1 : articleList) {
            if (article1.getArticleTitle().equals(articleTitle)) {
                if (modifyArticleTitle.isEmpty()) {
                    modifyArticle.setArticleTitle(article1.getArticleTitle());
                } else {
                    modifyArticle.setArticleTitle(modifyArticleTitle);
                }
                modifyArticle.setArticleAuthor(article1.getArticleAuthor());
                modifyArticle.setArticleDate(article1.getArticleDate());
                modifyArticle.setArticleCategory(article1.getArticleCategory());
                modifyArticle.setArticleContents(modifyArticleContents);
                if (modifyArticleFileName.isEmpty()) {
                    modifyArticle.setArticleFileName(article1.getArticleFileName());
                    modifyArticle.setArticleFileContents(article1.getArticleFileContents());
                } else {
                    modifyArticle.setArticleFileName(modifyArticleFileName);
                    modifyArticle.setArticleFileContents(fileUploadBytes);
                }
            }
        }

        int modifyArticleResult = articleDao.articleModify(articleTitle, modifyArticle);

        if (modifyArticleResult == 0){
            System.out.println("Article Modify Failed!");
        }
        else{
            System.out.println("Article Modify Success!");
        }

        model.addAttribute("articleTitle", articleTitle);
        model.addAttribute("currentArticle", modifyArticle);

        return "/article/modifyArticle";
    }

    @RequestMapping(value = "/deleteArticle", method = RequestMethod.GET)
    public String deleteArticle(@RequestParam(value = "currentTitle", required=true)
                                            String currentTitle, Model model) {
        int deleteResult = articleDao.articleDelete(currentTitle);

        if (deleteResult == 0){
            System.out.println("Article Delete Failed!");
        }
        else{
            System.out.println("Article Delete Success!");
        }

        return "redirect: /index";
    }

    @RequestMapping(value = "/deleteArticleFile", method = RequestMethod.GET)
    public String deleteArticleFile(@RequestParam(value = "articleTitle", required=true)
                                        String articleTitle, Model model) {
        int deleteResult = articleDao.articleFileDelete(articleTitle);

        if (deleteResult == 0){
            System.out.println("Article File Delete Failed!");
        }
        else{
            System.out.println("Article File Delete Success!");
        }

        return "redirect: /index";
    }

    @RequestMapping(value = "/writeComments", method = RequestMethod.POST)
    public String writeNewComments(
            @RequestParam(value = "commentTitle", required = true)
                                             String commentTitle,
            Comment comment, HttpSession httpSession) {
        int commentResult = articleDao.commentInsert(comment, httpSession, commentTitle);

        if (commentResult == 0){
            System.out.println("Comment Register Failed!");
        }
        else{
            System.out.println("Comment Register Success!");
        }

        return "redirect:/index";
    }

    @RequestMapping(value = "/goToModifyCommentPage", method = RequestMethod.GET)
    public String goToModifyCommentPage(@RequestParam(value = "currentComment", required = true)
                                                    String currentComment, Model model) {
        model.addAttribute("currentComment", currentComment);

        return "/article/modifyComment";
    }

    @RequestMapping(value = "/modifyComment", method = RequestMethod.POST)
    public String modifyComment(@RequestParam(value = "currentComment", required = true)
                                            String currentComment,
                                @RequestParam(value = "modifyComment", required = true)
                                        String modifyComment, Model model) {
        int commentResult = articleDao.commentModify(currentComment, modifyComment);

        if (commentResult == 0){
            System.out.println("Comment Modify Failed!");
        }
        else{
            System.out.println("Comment Modify Success!");
        }

        model.addAttribute("currentComment", currentComment);

        return "/article/modifyComment";
    }

    @RequestMapping(value = "/deleteComment", method = RequestMethod.GET)
    public String deleteComment(@RequestParam(value = "currentComment", required = true)
                                        String currentComment) {
        int deleteResult = articleDao.commentDelete(currentComment);

        if (deleteResult == 0){
            System.out.println("Comment Delete Failed!");
        }
        else{
            System.out.println("Comment Delete Success!");
        }

        return "redirect: /index";
    }
}