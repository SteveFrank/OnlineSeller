package com.ordering.product.service;

import com.ordering.product.po.Product;
import com.ordering.utils.PageBean;

public interface ProductService {
	public PageBean<Product> queryAll_PageProduct(String sid,int pageCurrent, int pageSize);

	public void addProduct(Product product, String sid);
}
