package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.CartDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.util.StringUtil;
import com.sun.javafx.binding.StringFormatter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartDao cartDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(CartPojo cartPojo) throws ApiException {
        normalize(cartPojo);
        if(StringUtil.isEmpty(cartPojo.getProductBarcode())) {
            throw new ApiException("Fields cannot be empty");
        }
        cartDao.insert(cartPojo);
    }

    @Transactional
    public CartPojo get(int itemNo) throws ApiException {
        return getCheck(itemNo);
    }

    @Transactional
    public CartPojo get(String barcode, String userEmail) throws ApiException {
        return cartDao.select(barcode, userEmail);
    }

    @Transactional
    public List<CartPojo> getAll(String userEmail) {
        return cartDao.selectAll(userEmail);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int itemNo, CartPojo cartPojo) throws ApiException {
        normalize(cartPojo);
        CartPojo exCartPojo = getCheck(itemNo);
        exCartPojo.setProductBarcode(cartPojo.getProductBarcode());
        exCartPojo.setProductQuantity(cartPojo.getProductQuantity());
        exCartPojo.setProductSP(cartPojo.getProductSP());
        cartDao.update(exCartPojo);
    }

    @Transactional
    public void delete(int id) {
        cartDao.deleteId(id);
    }

    @Transactional
    public CartPojo getCheck(int itemNo) throws ApiException {
        CartPojo cartPojo = cartDao.select(itemNo);
        if (cartPojo == null) {
            throw new ApiException("item with given ID does not exit, id: " + itemNo);
        }
        return cartPojo;
    }

    protected static void normalize(CartPojo cartPojo) {
        cartPojo.setProductBarcode(cartPojo.getProductBarcode().trim());
    }
}
