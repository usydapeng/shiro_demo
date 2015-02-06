package com.dapeng.dao;

import com.dapeng.domain.SlugInfo;
import org.springframework.stereotype.Repository;

@Repository("slugInfoDAO")
public class SlugInfoDAOImpl extends AbsHibernateGenericDAO<SlugInfo, Long> implements SlugInfoDAO {

}
