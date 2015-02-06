package com.dapeng.dao.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 与具体ORM实现无关的分页查询结果封装.
 * 
 * @param <T> Page中记录的类型.
 */
public class Pagination<T> extends PageRequest implements Iterable<T>, Serializable {

	private static final long serialVersionUID = 1239233431993044520L;
	
	protected List<T> items = null;
	protected long totalCount = -1;
	private int sliderCount;
	private int itemsSize = 0;
	
	private int pageNo;
	private int pageSize;
	private String orderBy;
	private String orderDir = Sort.DESC;
	
	public Pagination() {
	}

	public Pagination(PageRequest request) {
		this.pageNo = request.pageNo;
		this.pageSize = request.pageSize;
		this.countTotal = request.countTotal;
		this.orderBy = request.orderBy;
		this.orderDir = request.orderDir;
		this.sliderCount = request.sliderCount;
	}
	
	public Pagination(List<T> items, Pagination<?> pagination) {
		this.countTotal =pagination.isCountTotal();
		this.orderBy = pagination.getOrderBy();
		this.orderDir = pagination.getOrderDir();
		this.pageNo = pagination.getPageNo();
		this.pageSize = pagination.getPageSize();
		this.totalCount = pagination.getTotalCount();
		this.items = items;
		this.sliderCount = pagination.getSliderCount();
		this.itemsSize  = items != null ? items.size() : 0;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
	
	public int getItemsSize() {
		return items != null ? items.size() : 0;
	}

	public void setItemsSize(int itemsSize) {
		this.itemsSize = itemsSize;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/** 
	 * 实现Iterable接口, 可以for(Object item : page)遍历使用
	 */
	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	/**
	 * 根据pageSize与totalItems计算总页数.
	 */
	public int getTotalPages() {
		return (int) Math.ceil((double) totalCount / (double) getPageSize());

	}

	/**
	 * 是否还有下一页.
	 */
	public boolean hasNextPage() {
		return (getPageNo() + 1 <= getTotalPages());
	}

	/**
	 * 是否最后一页.
	 */
	public boolean isLastPage() {
		return !hasNextPage();
	}

	/**
	 * 取得下页的页号, 序号从1开始.
	 * 当前页为尾页时仍返回尾页序号.
	 */
	public int getNextPage() {
		if (hasNextPage()) {
			return getPageNo() + 1;
		} else {
			return getPageNo();
		}
	}
	
	/**
	 * 取得是否还有下一页
	 */
	public boolean getHasNextPage() {
		return hasNextPage();
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean hasPrePage() {
		return (getPageNo() > 1);
	}

	/**
	 * 是否第一页.
	 */
	public boolean isFirstPage() {
		return !hasPrePage();
	}

	/**
	 * 取得上页的页号, 序号从1开始.
	 * 当前页为首页时返回首页序号.
	 */
	public int getPrePage() {
		if (hasPrePage()) {
			return getPageNo() - 1;
		} else {
			return getPageNo();
		}
	}
	
	/**
	 * 取得是否还有上一页
	 * @return
	 */
	public boolean getHasPrePage() {
		return hasPrePage();
	}
	
	public int getSliderCount() {
		return sliderCount;
	}

	public void setSliderCount(int sliderCount) {
		this.sliderCount = sliderCount;
	}

	/**
	 * 计算以当前页为中心的页面列表,如"首页,23,24,25,26,27,末页"
	 * @return pageNo列表
	 */
	public List<Integer> getSlider() {
		int count = sliderCount;
		int halfSize = count / 2;
		int totalPage = getTotalPages();

		int startPageNo = Math.max(getPageNo() - halfSize, 1);
		int endPageNo = Math.min(startPageNo + count - 1, totalPage);

		if (endPageNo - startPageNo < count) {
			startPageNo = Math.max(endPageNo - count, 1);
		}

		List<Integer> result = new ArrayList<Integer>();
		for (int i = startPageNo; i <= endPageNo; i++) {
			result.add(i);
		}
		return result;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderDir() {
		return orderDir;
	}

	public void setOrderDir(String orderDir) {
		this.orderDir = orderDir;
	}
	
	
}
