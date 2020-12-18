package com.myBlogProj.blogProj_thymeleaf.member.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.myBlogProj.blogProj_thymeleaf.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*Spring Container에 해당 클래스에 대한 Bean 객체를 생성*/
@Repository
public class MemberDao implements IMemberDao {
    /*블로그 만들기 프로젝트
     * 기반 > Spring MVC + Thymeleaf + Oracle Database*/

    /*JdbcTemplate를 활용하여 db 연결할 때 필요한 설정을 미리 등록
     * 개발자는 sql문만 신경쓰면 됨.*/
    private JdbcTemplate jdbcTemplate;

    @Autowired // > dbConfig.xml > dataSource
    public MemberDao(ComboPooledDataSource comboPooledDataSource) {
        /*Dao 객체가 생성되는 생성자에 @Autowired 어노테이션을 달아줌
         * 스프링 컨테이너에 있는 dataSource 객체가 Dao 생성자의 파라미터를 통해서 들어옴
         * 템플릿을 초기화해서 생성을 할 때 매개변수로 사용할 수 있다.
         * 나머지 쿼리는 기존 jdbc, jdbctemplate과 동일하다*/
        this.jdbcTemplate = new JdbcTemplate(comboPooledDataSource);
    }

    @Override
    public int memberInsert(final Member member) {
        /*숫자를 반환
         * > 0이면 들어간 값이 없는 것
         * > 1이면 들어간 값이 있는 것*/
        int resultInsert = 0;

        final String memberInsert_sql =
                "INSERT INTO testmember20 (memberID, memberPW, memberNickname, memberBirth, memberIntroWord) " +
                        "values (?, ?, ?, ?, ?)";

        /*JdbcTemplate 활용 > 첫 번째 ?에 memberId, 두 번째 ?에 memberPw 정보가 들어감*/
        resultInsert = jdbcTemplate.update(memberInsert_sql,
                member.getMemberID(), member.getMemberPW(),
                member.getMemberNickname(), member.getMemberBirth(),
                member.getMemberIntroWord());

        return resultInsert;
    }

    @Override
    public Member memberSelect(final Member member) {
        /*로그인할 때 멤버 객체 담을 리스트를 생성*/
        List<Member> memberList = null;

        final String memberSelect_sql =
                "SELECT * FROM testmember20 WHERE memberID = ? AND memberPW = ?";

        /*JdbcTemplate + RowMapper 활용*/
        memberList = jdbcTemplate.query(memberSelect_sql, new RowMapper<Member>() {
            /*RowMapper<객체 타입>을 통해서 자동 생성된 코드*/
            public Member mapRow(ResultSet resultSet, int i) throws SQLException {
                Member member_dbData = new Member();

                member_dbData.setMemberID(resultSet.getString("memberID"));
                member_dbData.setMemberPW(resultSet.getString("memberPW"));

                return member_dbData;
            }
        }, member.getMemberID(), member.getMemberPW());

        if (memberList.isEmpty()) {
            /*들어간 값이 없으면 Null을 반환*/
            return null;
        }

        return memberList.get(0); // memberList에 있는 member의 첫 번째 객체를 반환
    }

    @Override
    public List<Member> findAll() {
        /*카테고리의 전체 내용을 가져옴*/
        final String memberSelect_sql =
                "SELECT * FROM testmember20";

        /*jdbctemplate + BeanProepertyRowMapper를 활용하여 Member의 내용을 전부 가져옴
         * 대신에 Category 클래스를 생성해 놓아야.*/
        return jdbcTemplate.query(memberSelect_sql,
                new BeanPropertyRowMapper<>(Member.class));
    }

    @Override
    public int changeAdminNickname(String adminID, String adminNickName) {
        int resultUpdate = 0;

        final String sql = "UPDATE testmember20 " +
                "SET memberNickname = ? " +
                "WHERE memberID = ?";

        resultUpdate = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, adminNickName);
                pstmt.setString(2, adminID);
            }
        });
        return resultUpdate;
    }

    @Override
    public int changeAdminIntroWord(String adminID, String adminIntroWord) {
        int resultUpdate = 0;

        final String sql = "UPDATE testmember20 " +
                "SET memberIntroWord = ? " +
                "WHERE memberID = ?";

        resultUpdate = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, adminIntroWord);
                pstmt.setString(2, adminID);
            }
        });
        return resultUpdate;
    }

    @Override
    public String findMemberID(Member member) {
        String memberNickname = member.getMemberNickname();
        String memberBirth = member.getMemberBirth();

        final String blogTitleSelect_sql =
                "SELECT memberID FROM testmember20 WHERE memberNickname = ? AND memberBirth = ?";

        return jdbcTemplate.queryForObject(
                blogTitleSelect_sql, new Object[]{memberNickname, memberBirth}, String.class);
    }

    @Override
    public int resetMemberPW(String memberID, Member member) {
        int resultUpdate = 0;

        final String sql = "UPDATE testmember20 " +
                "SET memberPW = ? " +
                "WHERE memberID = ?";

        resultUpdate = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, member.getMemberPW());
                pstmt.setString(2, memberID);
            }
        });
        return resultUpdate;
    }

    @Override
    public int modifyMemberInfo(String loginSessionID, Member member) {
        int resultUpdate = 0;

        final String sql_modifyMemberInfo = "UPDATE testmember20 " +
                "SET memberPW = ?, memberNickname = ?, memberBirth = ?, memberIntroWord = ?" +
                "WHERE memberID = ?";

        resultUpdate = jdbcTemplate.update(sql_modifyMemberInfo, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, member.getMemberPW());
                pstmt.setString(2, member.getMemberNickname());
                pstmt.setString(3, member.getMemberBirth());
                pstmt.setString(4, member.getMemberIntroWord());
                pstmt.setString(5, loginSessionID);
            }
        });

        return resultUpdate;
    }

    @Override
    public int removeMember(String loginMemberID) {
        int resultDelete = 0;

        final String sql = "DELETE FROM testmember20 WHERE memberID = ?";

        resultDelete = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, loginMemberID);
            }
        });
        return resultDelete;
    }
}
