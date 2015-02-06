package com.dapeng.dao;

import com.dapeng.domain.UserInfo;
import org.springframework.stereotype.Repository;

@Repository("userInfoDAO")
public class UserInfoDAOImpl extends AbsHibernateGenericDAO<UserInfo, Long> implements UserInfoDAO {

	@Override
	public UserInfo findByUserId(Long userId) {
		String hql = "from UserInfo where userId = ?";
		return findUnique(hql, userId);
	}
}
