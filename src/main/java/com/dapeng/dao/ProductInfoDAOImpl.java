package com.dapeng.dao;

import com.dapeng.domain.ProductInfo;
import org.springframework.stereotype.Repository;

@Repository("productInfoDAO")
public class ProductInfoDAOImpl extends AbsHibernateGenericDAO<ProductInfo, Long> implements ProductInfoDAO {

}
