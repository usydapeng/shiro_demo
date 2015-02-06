package com.dapeng.dao;

import com.dapeng.dao.orm.GenericsUtils;
import com.dapeng.dao.orm.PageRequest;
import com.dapeng.dao.orm.Pagination;
import com.dapeng.dao.orm.PropertyFilter;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbsHibernateGenericDAO<T, ID extends Serializable> implements HibernateDAO<T, ID> {

	private static final Logger logger = LoggerFactory.getLogger(AbsHibernateGenericDAO.class);
	
	private Class<T> entityClass;

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	public AbsHibernateGenericDAO() {
		this.entityClass = GenericsUtils.getSuperClassGenricType(getClass());
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	/**
	 * 取得sessionFactory.
	 */
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * 保存新增或修改的对象.
	 */
	@Override
	public void save(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().saveOrUpdate(entity);
		logger.info("save entity: {}", entity);
	}
	
	/**
	 * 删除对象.
	 * 
	 * @param entity 对象必须是session中的对象或含id属性的transient对象.
	 */
	@Override
	public void delete(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().delete(entity);
		logger.debug("delete entity: {}", entity);
	}

	/**
	 * 按id删除对象.
	 */
	@Override
	public void delete(final ID id) {
		Assert.notNull(id, "id不能为空");
		delete(findById(id));
		logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
	}

	/**
	 * 按id获取对象.
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public T findById(final ID id) {
		Assert.notNull(id, "id不能为空");
		return (T) getSession().get(entityClass, id);
	}

	/**
	 * 按id列表获取对象列表.
	 */
	@Override
	public List<T> getAll(final Collection<ID> ids) {
		return find(Restrictions.in(getIdName(), ids));
	}
	
	/**
	 *	获取全部对象.
	 */
	@Override
	public List<T> findAll() {
		return find();
	}
	
	/**
	 *	获取全部对象, 支持按属性行序.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(String orderByProperty, boolean isAsc) {
		Criteria c = createCriteria();
		if (isAsc) {
			c.addOrder(Order.asc(orderByProperty));
		} else {
			c.addOrder(Order.desc(orderByProperty));
		}
		return c.list();
	}

	/**
	 * 按属性查找对象列表, 匹配方式为相等.
	 */
	@Override
	public List<T> findBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return find(criterion);
	}

	/**
	 * 按属性查找唯一对象, 匹配方式为相等.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T findUniqueBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return (T) createCriteria(criterion).uniqueResult();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <X> List<X> find(final String hql, final Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <X> List<X> find(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <X> X findUnique(final String hql, final Object... values) {
		Query q = createQuery(hql, values);
		
		q.setFirstResult(0);
		q.setMaxResults(1);
		
		List<X> list = q.list();
		return list.size() > 0 ? list.get(0) : null;
//		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <X> X findUnique(final String hql, final Map<String, ?> values) {
		Query q = createQuery(hql, values);
		
		q.setFirstResult(0);
		q.setMaxResults(1);
		
		List<X> list = q.list();
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	@Override
	public int batchExecute(final String hql, final Object... values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values 命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	@Override
	public int batchExecute(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).executeUpdate();
	}
	
	
	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	@Override
	public long countHqlResult(final String hql, final Object... values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	@Override
	public long countHqlResult(final String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	private String prepareCountHql(String orgHql) {
		String countHql = "select count (*) " + removeSelect(removeOrders(orgHql));
		return countHql;
	}

	private static String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		return hql.substring(beginPos);
	}

	private static String removeOrders(String hql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * 按HQL分页查询.
	 * 
	 * @param pageRequest 分页参数.
	 * @param hql hql语句.
	 * @param values 数量可变的查询参数,按顺序绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pagination<T> pageByHql(final PageRequest pageRequest, String hql, final Object... values) {
		Assert.notNull(pageRequest, "pageRequest不能为空");

		Pagination<T> page = new Pagination<T>(pageRequest);

		if (pageRequest.isCountTotal()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}

		if (pageRequest.isOrderBySetted()) {
			hql = setOrderParameterToHql(hql, pageRequest);
		}
		Query q = createQuery(hql, values);

		setPageParameterToQuery(q, pageRequest);

		List result = q.list();
		page.setItems(result);
		return page;
	}
	
	/**
	 * 在HQL的后面添加分页参数定义的orderBy, 辅助函数.
	 */
	@Override
	public String setOrderParameterToHql(final String hql, final PageRequest pageRequest) {
		StringBuilder builder = new StringBuilder(hql);
		builder.append(" order by");

		for (PageRequest.Sort orderBy : pageRequest.getSort()) {
			builder.append(String.format(" %s %s,", orderBy.getProperty(), orderBy.getDir()));
		}

		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
	
	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
	@Override
	public Query setPageParameterToQuery(final Query q, final PageRequest pageRequest) {
		q.setFirstResult(pageRequest.getOffset());
		q.setMaxResults(pageRequest.getPageSize());
		return q;
	}
	
	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public Query createQuery(final String queryString, final Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	
	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public Query createQuery(final String queryString, final Map<String, ?> values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> find(final Criterion... criterions) {
		return createCriteria(criterions).list();
	}

	/**
	 * 按Criteria查询唯一对象.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	@SuppressWarnings("unchecked")
	public T findUnique(final Criterion... criterions) {
		return (T) createCriteria(criterions).uniqueResult();
	}

	/**
	 * 按属性过滤条件列表分页查找对象.
	 */
	public Pagination<T> findPage(final PageRequest pageRequest, final List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return findPage(pageRequest, criterions);
	}
	
	/**
	 * 按Criteria分页查询.
	 * 
	 * @param criterions 数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Pagination<T> findPage(final PageRequest pageRequest, final Criterion... criterions) {
		Assert.notNull(pageRequest, "page不能为空");

		Pagination<T> page = new Pagination<T>(pageRequest);

		Criteria c = createCriteria(criterions);

		if (pageRequest.isCountTotal()) {
			long totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}

		setPageRequestToCriteria(c, pageRequest);

		List result = c.list();
		page.setItems(result);
		return page;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Pagination<T> findPage(final PageRequest pageRequest, final Criteria criteria) {
		Assert.notNull(pageRequest, "page不能为空");

		Pagination<T> page = new Pagination<T>(pageRequest);

		if (pageRequest.isCountTotal()) {
			long totalCount = countCriteriaResult(criteria);
			page.setTotalCount(totalCount);
		}

		setPageRequestToCriteria(criteria, pageRequest);

		List result = criteria.list();
		page.setItems(result);
		return page;
	}
	
	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 */
	protected Criteria setPageRequestToCriteria(final Criteria c, final PageRequest pageRequest) {
		Assert.isTrue(pageRequest.getPageSize() > 0, "Page Size must larger than zero");

		c.setFirstResult(pageRequest.getOffset());
		c.setMaxResults(pageRequest.getPageSize());

		if (pageRequest.isOrderBySetted()) {
			for (PageRequest.Sort sort : pageRequest.getSort()) {
				if (PageRequest.Sort.ASC.equals(sort.getDir())) {
					c.addOrder(Order.asc(sort.getProperty()));
				} else {
					c.addOrder(Order.desc(sort.getProperty()));
				}
			}
		}
		return c;
	}
	
	/**
	 * 根据Criterion条件创建Criteria.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	public Criteria getCriteria(){
		Criteria criteria = getSession().createCriteria(entityClass);
		
		return criteria;
	}
	
	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected long countCriteriaResult(final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) GenericsUtils.getFieldValue(impl, "orderEntries");
			
			GenericsUtils.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		// 执行Count查询
		Long totalCountObject = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		long totalCount = (totalCountObject != null) ? totalCountObject : 0;

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			GenericsUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		return totalCount;
	}
	
	/**
	 * 按属性条件参数创建Criterion,辅助函数.
	 */
	protected Criterion buildCriterion(final String propertyName, final Object propertyValue, final PropertyFilter.MatchType matchType) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = null;
		//根据MatchType构造criterion
		switch (matchType) {
		case EQ:
			criterion = Restrictions.eq(propertyName, propertyValue);
			break;
		case LIKE:
			criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE);
			break;

		case LE:
			criterion = Restrictions.le(propertyName, propertyValue);
			break;
		case LT:
			criterion = Restrictions.lt(propertyName, propertyValue);
			break;
		case GE:
			criterion = Restrictions.ge(propertyName, propertyValue);
			break;
		case GT:
			criterion = Restrictions.gt(propertyName, propertyValue);
		}
		return criterion;
	}

	/**
	 * 按属性条件列表创建Criterion数组,辅助函数.
	 */
	protected Criterion[] buildCriterionByPropertyFilter(final List<PropertyFilter> filters) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		for (PropertyFilter filter : filters) {
			if (!filter.hasMultiProperties()) { //只有一个属性需要比较的情况.
				Criterion criterion = buildCriterion(filter.getPropertyName(), filter.getMatchValue(),
						filter.getMatchType());
				criterionList.add(criterion);
			} else {//包含多个属性需要比较的情况,进行or处理.
				Disjunction disjunction = Restrictions.disjunction();
				for (String param : filter.getPropertyNames()) {
					Criterion criterion = buildCriterion(param, filter.getMatchValue(), filter.getMatchType());
					disjunction.add(criterion);
				}
				criterionList.add(disjunction);
			}
		}
		return criterionList.toArray(new Criterion[criterionList.size()]);
	}
	/**
	 * 初始化对象.
	 * 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化.
	 * 如果传入entity, 则只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性.
	 * 如需初始化关联属性,需执行:
	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合.
	 * Hibernate.initialize(user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initProxyObject(Object proxy) {
		Hibernate.initialize(proxy);
	}

	/**
	 * Flush当前Session.
	 */
	public void flush() {
		getSession().flush();
	}

	/**
	 * 为Query添加distinct transformer.
	 * 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Query distinct(Query query) {
		query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return query;
	}

	/**
	 * 为Criteria添加distinct transformer.
	 * 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Criteria distinct(Criteria criteria) {
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

	/**
	 * 取得对象的主键名.
	 */
	protected String getIdName() {
		ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
		return meta.getIdentifierPropertyName();
	}

	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
	 */
	public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
		if (newValue == null || newValue.equals(oldValue)) {
			return true;
		}
		Object object = findUniqueBy(propertyName, newValue);
		return (object == null);
	}

	@Override
	public void update(T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().update(entity);
		logger.debug("update entity: {}", entity);
	}
}
