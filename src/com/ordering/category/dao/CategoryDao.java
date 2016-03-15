package com.ordering.category.dao;

import java.util.List;

import com.ordering.category.po.Category;
import com.ordering.utils.PageBean;

public interface CategoryDao {

	public List<Category> queryAllCategory();

	public Category findCategoryByCid(String cid);

	public PageBean<Category> findCategoryAll(int pageCurrent, int pageSize);
	
}
