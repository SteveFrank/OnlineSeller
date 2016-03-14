package com.ordering.category.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ordering.category.dao.CategoryDao;
import com.ordering.category.po.Category;

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
}
