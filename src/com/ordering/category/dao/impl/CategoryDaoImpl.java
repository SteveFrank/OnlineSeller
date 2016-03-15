package com.ordering.category.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ordering.category.dao.CategoryDao;
import com.ordering.category.po.Category;
import com.ordering.utils.PageBean;

@Repository("categoryDao")
public class CategoryDaoImpl implements CategoryDao {
	@Autowired(required=true)
	@PersistenceContext(name="unitName")
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> queryAllCategory() {
		String jpal = "select category from com.ordering.category.po.Category category";
		return entityManager.createQuery(jpal).getResultList();
	}

	@Override
	public Category findCategoryByCid(String cid) {
		String jpal = "select category from com.ordering.category.po.Category category where category.cid=:cid";
		return (Category) entityManager.createQuery(jpal).setParameter("cid", cid).getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageBean<Category> findCategoryAll(int pageCurrent, int pageSize) {
		PageBean<Category> pb = new PageBean<Category>();
		//设置当前页面页码
		pb.setPageCurrent(pageCurrent);
		//设置当前页面的页面容量
		pb.setPageSize(pageSize);
		String sql = "select * from category";
		List<Category> categoryList = entityManager.createNativeQuery(sql,Category.class).getResultList();
		//设置查询到的中行数
		pb.setRowCount(categoryList.size());
		int _pageSize = ((pageCurrent-1)*pageSize+pageSize);
		if(((pageCurrent-1)*pageSize+pageSize)>categoryList.size()) {
			_pageSize = pageSize-(((pageCurrent-1)*pageSize+pageSize)-categoryList.size());
		}
		
		pb.setBeanList(categoryList.subList((pageCurrent-1)*pageSize, (pageCurrent-1)*pageSize+_pageSize));
		
		entityManager.close();
		
		return pb;
	}
}
