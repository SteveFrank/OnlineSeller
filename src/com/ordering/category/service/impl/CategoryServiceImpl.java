package com.ordering.category.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ordering.category.dao.CategoryDao;
import com.ordering.category.po.Category;
import com.ordering.category.service.CategoryService;
import com.ordering.utils.PageBean;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
	
	@Resource(name="categoryDao")
	private CategoryDao categoryDao;

	@Override
	public List<Category> queryAllCategory() {
		return categoryDao.queryAllCategory();
	}

	@Override
	public Category findCategoryByCid(String cid) {
		return categoryDao.findCategoryByCid(cid);
	}

	@Override
	public PageBean<Category> queryAll_PageCategory(int pageCurrent, int pageSize) {
		PageBean<Category> pageBean = categoryDao.findCategoryAll(pageCurrent, pageSize);
		List<Category> categoryList = pageBean.getBeanList();
		pageBean.setBeanList(categoryList);
		return pageBean;
	}
}
