package com.ordering.category.service;

import java.util.List;

import com.ordering.category.po.Category;

public interface CategoryService {
	public List<Category> queryAllCategory();
	public Category findCategoryByCid(String cid);
}
