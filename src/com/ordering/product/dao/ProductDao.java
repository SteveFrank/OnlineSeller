package com.ordering.product.dao;

import com.ordering.product.po.Product;
import com.ordering.utils.PageBean;

public interface ProductDao {

	public PageBean<Product> findProductBySid(String sid, int pageCurrent, int pageSize);

	public void addProduct(Product product, String sid);

}
