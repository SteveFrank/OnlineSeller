package com.ordering.category.service;

import java.util.List;

import com.ordering.category.po.Category;
import com.ordering.utils.PageBean;

public interface CategoryService {
	public List<Category> queryAllCategory();
	public Category findCategoryByCid(String cid);
	public PageBean<Category> queryAll_PageCategory(int pageCurrent, int pageSize);
}
