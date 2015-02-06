package com.dapeng.dao;

import com.dapeng.dao.orm.PageRequest;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface HibernateDAO<T, ID extends Serializable> {

	void save(T entity);

	void delete(T entity);

	void delete(ID id);

	List<T> getAll(Collection<ID> ids);

	T findById(ID id);

	List<T> findAll();

	List<T> findBy(String propertyName, Object value);

	T findUniqueBy(String propertyName, Object value);

	List<T> findAll(String orderByProperty, boolean isAsc);

	<X> List<X> find(final String hql, final Object... values);
	
	<X> List<X> find(final String hql, final Map<String, ?> values);
	
	<X> X findUnique(final String hql, final Object... values);
	
	<X> X findUnique(final String hql, final Map<String, ?> values);

	int batchExecute(final String hql, final Object... values);
	
	int batchExecute(final String hql, final Map<String, ?> values);
	
	long countHqlResult(final String hql, final Object... values);
	
	long countHqlResult(final String hql, final Map<String, ?> values);

	SessionFactory getSessionFactory();

	Session getSession();

	String setOrderParameterToHql(String hql, PageRequest pageRequest);

	Query setPageParameterToQuery(Query q, PageRequest pageRequest);

	void update(T entity);

}