package com.ordering.product.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ordering.product.dao.ProductDao;
import com.ordering.product.po.Product;
import com.ordering.utils.PageBean;

@Repository("productDao")
public class ProductDaoImpl implements ProductDao {
	
	@Autowired(required=true)
	@PersistenceContext(name="unitName")
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public PageBean<Product> findProductBySid(String sid, int pageCurrent, int pageSize) {
		PageBean<Product> pb = new PageBean<Product>();
		//设置当前页面页码
		pb.setPageCurrent(pageCurrent);
		//设置当前页面的页面容量
		pb.setPageSize(pageSize);
		String sql = "select * from product where s_pro_id=?";
		List<Product> productList = entityManager.createNativeQuery(sql,Product.class).setParameter(1, sid).getResultList();
		//设置查询到的中行数
		pb.setRowCount(productList.size());
		
		int _pageSize = ((pageCurrent-1)*pageSize+pageSize);
		if(((pageCurrent-1)*pageSize+pageSize)>productList.size()) {
			_pageSize = pageSize-(((pageCurrent-1)*pageSize+pageSize)-productList.size());
		}
		
		pb.setBeanList(productList.subList((pageCurrent-1)*pageSize, (pageCurrent-1)*pageSize+_pageSize));
		entityManager.close();
		return pb;
	}

	@Override
	public void addProduct(Product product, String sid) {
		String sql = "insert into product values(?,?,?,?,?,?,?)";
		entityManager.createNativeQuery(sql)
			.setParameter(1, product.getPid())
			.setParameter(2, product.getDescrible())
			.setParameter(3, product.getImage())
			.setParameter(4, product.getPname())
			.setParameter(5, product.getPrice())
			.setParameter(6, product.getCategory().getCid())
			.setParameter(7, sid).executeUpdate();
		entityManager.close();
	}

}
