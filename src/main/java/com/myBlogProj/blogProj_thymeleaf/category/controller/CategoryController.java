package com.myBlogProj.blogProj_thymeleaf.category.controller;

import com.myBlogProj.blogProj_thymeleaf.category.Category;
import com.myBlogProj.blogProj_thymeleaf.category.dao.CategoryDao;
import com.myBlogProj.blogProj_thymeleaf.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*Spring Container에 해당 클래스에 대한 Bean 객체를 생성*/
@Controller
@RequestMapping("/category") // @RequestMapping에 공통된 부분
public class CategoryController {
    /*블로그 만들기 프로젝트
     * 기반 > Spring MVC + Thymeleaf + Oracle Database*/

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryService categoryService;

    /*@RequestMapping(value = "/사용자가 누른 URL", method = Requested.GET / POST)
     * 해당 링크를 눌렀을 때, 아래의 메서드에 있는 기능이 실행됨.
     * URL은 ""로 둘러쌓음*/
    @RequestMapping(value = "/categorySetting", method = RequestMethod.GET)
    public String goToCategorySettingPage() {
        return "/category/categorySetting";
    }

    @RequestMapping(value = "/categoryAdd", method = RequestMethod.GET)
    public String addCategory(Category category) {

        categoryService.categoryRegister(category);

        return "/category/categorySetting";
    }

    @RequestMapping(value = "/categoryChange", method = RequestMethod.GET)
    public String changeCategory(@RequestParam("categoryChangeBefore") String categoryChangeBefore,
                                 @RequestParam("categoryChangeAfter") String categoryChangeAfter,
                                 @RequestParam("categoryClosedChange") String categoryClosedChange)
    {
        int resultCategoryNameChange =
                categoryDao.categoryChange(categoryChangeBefore, categoryChangeAfter, categoryClosedChange);

        if(resultCategoryNameChange == 0) {
            System.out.println("CategoryName change failed!");
        }
        else {
            System.out.println("CategoryName change success!");
        }

        return "/category/categorySetting";
    }

    @RequestMapping(value = "/categoryDelete", method = RequestMethod.GET)
    public String deleteCategory(@RequestParam("categoryDelete") String categoryDelete) {

        int resultCategoryDelete = categoryDao.categoryDelete(categoryDelete);

        if(resultCategoryDelete == 0) {
            System.out.println("Category delete failed!");
        }
        else {
            System.out.println("Category delete success!");
        }

        return "/category/categorySetting";
    }

    @RequestMapping(value = "/categoryPreview", method = RequestMethod.GET)
    public ModelAndView goToPreviewPage() {
        /*DB 데이터 가져오기 > http://zetcode.com/springboot/thymeleaf/
         * CategoryService.java > findAll()*/
        List<Category> categories = categoryService.findAll();
        
        Map<String, Object> category_dbData_map = new HashMap<>();
        /*categories를 어디에서 사용하는지는 의문
        * > 아마 categoryPreview.html
        * > th:each="category : ${categories}"*/
        category_dbData_map.put("categories", categories);

        /*ModelAndView > View의 이름 + 넘길 값*/
        return new ModelAndView("/category/categoryPreview", category_dbData_map);
    }
}
