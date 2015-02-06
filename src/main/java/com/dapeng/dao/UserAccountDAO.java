package com.dapeng.dao;

import com.dapeng.domain.UserAccount;

public interface UserAccountDAO extends HibernateDAO<UserAccount, Long> {

	UserAccount findByUsername(String username);
}
