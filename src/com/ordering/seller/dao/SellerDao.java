package com.ordering.seller.dao;

import com.ordering.seller.po.Seller;

public interface SellerDao {
	public int saveSeller(Seller seller);
	public Seller findSellerByEmailCode(String code);
	public void updateEmailActive(Seller seller);
	public Seller findSellerByEmail(String email);
	public Seller findSellerBySid(String sid);
	public void updateAccpetState(String sid, String b);
}
