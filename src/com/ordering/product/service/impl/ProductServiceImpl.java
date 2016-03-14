package com.ordering.product.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ordering.product.dao.ProductDao;
import com.ordering.product.po.Product;
import com.ordering.product.service.ProductService;
import com.ordering.utils.PageBean;

@Service("productService")
public class ProductServiceImpl implements ProductService {
	
	@Resource(name="productDao")
	private ProductDao productDao;
	
	@Transactional
	@Override
	public PageBean<Product> queryAll_PageProduct(String sid,int pageCurrent, int pageSize){
		PageBean<Product> pageBean = productDao.findProductBySid(sid, pageCurrent, pageSize);
		List<Product> productList = pageBean.getBeanList();
		pageBean.setBeanList(productList);
		return pageBean;
	}
	
	@Transactional
	@Override
	public void addProduct(Product product, String sid) {
		productDao.addProduct(product,sid);
	}
}
