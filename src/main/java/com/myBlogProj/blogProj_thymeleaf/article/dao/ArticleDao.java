package com.myBlogProj.blogProj_thymeleaf.article.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.myBlogProj.blogProj_thymeleaf.article.Article;
import com.myBlogProj.blogProj_thymeleaf.article.Comment;
import com.myBlogProj.blogProj_thymeleaf.member.Member;
import com.myBlogProj.blogProj_thymeleaf.test.FileUpload;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ArticleDao implements IArticleDao {

    SimpleDateFormat timeFormat = new SimpleDateFormat( "yyyy년 MM월dd일 HH시mm분ss초");

    Date timeValue = new Date();

    String currentTime = timeFormat.format(timeValue);

    private JdbcTemplate jdbcTemplate;

    public ArticleDao(ComboPooledDataSource comboPooledDataSource) {
        this.jdbcTemplate = new JdbcTemplate(comboPooledDataSource);
    }

    @Override
    public List<Article> findAllArticle() {
        final String articleSelect_sql =
                "SELECT * FROM testarticle20";

        return jdbcTemplate.query(articleSelect_sql,
                new BeanPropertyRowMapper<>(Article.class));
    }

    @Override
    public int articleInsert(final Article newArticle) throws IOException {

        int resultInsert = 0;

        final String articleInsert_sql =
                "INSERT INTO testarticle20 (articleTitle, articleAuthor, articleDate, " +
                        "articleCategory, articleContents, articleFileName, articleFileContents) " +
                        "values (?, ?, ?, ?, ?, ?, ?)";

        resultInsert = jdbcTemplate.update(articleInsert_sql,
                newArticle.getArticleTitle(), newArticle.getArticleAuthor(), newArticle.getArticleDate(),
                newArticle.getArticleCategory(), newArticle.getArticleContents(),
                newArticle.getArticleFileName(), newArticle.getArticleFileContents());

        return resultInsert;
    }

    @Override
    public int articleModify(String currentTitle, Article modifyArticle) {
        int resultUpdate = 0;

        final String sql = "UPDATE testarticle20 SET articleTitle = ?, articleAuthor = ?, articleDate = ?, articleCategory = ?, articleContents = ?, articleFileName = ?, articleFileContents = ? WHERE articleTitle = ?";

        resultUpdate = jdbcTemplate.update(sql, pstmt -> {
            pstmt.setString(1, modifyArticle.getArticleTitle());
            pstmt.setString(2, modifyArticle.getArticleAuthor());
            pstmt.setString(3, modifyArticle.getArticleDate());
            pstmt.setString(4, modifyArticle.getArticleCategory());
            pstmt.setString(5, modifyArticle.getArticleContents());
            pstmt.setString(6, modifyArticle.getArticleFileName());
            pstmt.setBytes(7, modifyArticle.getArticleFileContents());
            pstmt.setString(8, currentTitle);
        });
        return resultUpdate;
    }

    @Override
    public int articleDelete(String currentTitle) {
        int resultDelete = 0;

        final String sql = "DELETE FROM testarticle20 WHERE articleTitle = ?";

        resultDelete = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, currentTitle);
            }
        });
        return resultDelete;
    }

    @Override
    public int articleFileDelete(String articleTitle) {
        int resultUpdate = 0;

        final String sql = "UPDATE testarticle20 SET articleFileName = ?, articleFileContents = ? WHERE articleTitle = ?";

        resultUpdate = jdbcTemplate.update(sql, pstmt -> {
            pstmt.setString(1, null);
            pstmt.setBytes(2, null);
            pstmt.setString(3, articleTitle);
        });
        return resultUpdate;
    }

    @Override
    public int commentInsert(Comment comment, HttpSession httpSession, String commentTitle) {
        int resultInsert = 0;

        Member memberValue = (Member) httpSession.getAttribute("member");

        String author = memberValue.getMemberNickname();

        final String articleInsert_sql =
                "INSERT INTO testcomment20 (commentAuthor, " +
                        "commentDate, commentTitle, commentContents) " +
                        "values (?, ?, ?, ?)";

        resultInsert = jdbcTemplate.update(articleInsert_sql,
                author, currentTime, commentTitle,
                comment.getCommentContents());

        return resultInsert;
    }

    @Override
    public List<Comment> findAllComment() {
        final String commentSelect_sql =
                "SELECT * FROM testcomment20 ORDER BY commentTitle";

        return jdbcTemplate.query(commentSelect_sql,
                new BeanPropertyRowMapper<>(Comment.class));
    }

    @Override
    public int commentModify(String currentComment, String newComment) {
        int resultUpdate = 0;

        final String sql = "UPDATE testcomment20 SET commentContents = ? WHERE commentContents = ?";

        resultUpdate = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, newComment);
                pstmt.setString(2, currentComment);
            }
        });
        return resultUpdate;
    }

    @Override
    public int commentDelete(String currentComment) {
        int resultDelete = 0;

        final String sql = "DELETE FROM testcomment20 WHERE commentContents = ?";

        resultDelete = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, currentComment);
            }
        });
        return resultDelete;
    }
}
