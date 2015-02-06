package com.dapeng.dao;

import com.dapeng.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository("userAccountDAO")
public class UserAccountDAOImpl extends AbsHibernateGenericDAO<UserAccount, Long> implements UserAccountDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public UserAccount findByUsername(String username) {
		String sql = "select * from user_account where username = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{username}, new RowMapper<UserAccount>() {
			@Override
			public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserAccount userAccount = new UserAccount();
				userAccount.setId(rs.getLong("id"));
				userAccount.setPassword(rs.getString("passwd"));
				userAccount.setUsername(rs.getString("username"));
				userAccount.setConfirmed(rs.getBoolean("confirmed"));
				userAccount.setUserRole(rs.getInt("userRole"));
				userAccount.setSlug(rs.getLong("slug"));
				userAccount.setEnabled(rs.getBoolean("enabled"));
				userAccount.setCreateTime(rs.getDate("createTime"));
				return userAccount;
			}
		});
	}
}
