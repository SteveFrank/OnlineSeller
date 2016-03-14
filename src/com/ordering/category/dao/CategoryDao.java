package com.ordering.category.dao;

import java.util.List;

import com.ordering.category.po.Category;

public interface CategoryDao {

	public List<Category> queryAllCategory();

	public Category findCategoryByCid(String cid);
	
}
