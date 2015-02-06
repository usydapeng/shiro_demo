package com.dapeng.dao;

import com.dapeng.domain.UserPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("userPermissionDAO")
public class UserPermissionDAOImpl extends AbsHibernateGenericDAO<UserPermission, Long> implements UserPermissionDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<UserPermission> findAllByUserId(Long userId) {
		String sql = "select * from user_permission where userId = ?";
		return jdbcTemplate.query(sql, new Object[]{userId}, new RowMapper<UserPermission>() {
			@Override
			public UserPermission mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserPermission userPermission = new UserPermission();
				userPermission.setUserId(rs.getLong("userId"));
				userPermission.setPermission(rs.getString("permission"));
				userPermission.setCreateTime(rs.getDate("createTime"));
				userPermission.setEnabled(rs.getBoolean("enabled"));
				userPermission.setId(rs.getLong("id"));
				return userPermission;
			}
		});
	}
}
