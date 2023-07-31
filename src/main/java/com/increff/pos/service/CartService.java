package com.increff.pos.service;

import com.increff.pos.dao.CartDao;
import com.increff.pos.model.forms.CartForm;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CartService {
    @Autowired
    private CartDao cartDao;

    public void add(CartPojo cartPojo) throws ApiException {
        cartDao.insert(cartPojo);
    }

    public CartPojo get(int itemNo) throws ApiException {
        return getCheck(itemNo);
    }

    public CartPojo get(int productId, String userEmail) throws ApiException {
        return cartDao.selectProduct(productId, userEmail);
    }

    public List<CartPojo> getAll(String userEmail) throws ApiException {
        List<CartPojo> list = cartDao.selectAll(userEmail); //todo to rename the variable name.
        return list;
    }

    public void update(int itemNo, int quantity, double sellingPrice) throws ApiException {
        CartPojo exCartPojo = getCheck(itemNo);
        exCartPojo.setQuantity(quantity);
        exCartPojo.setSellingPrice(sellingPrice);
    }

    public void delete(int id) throws ApiException {
        getCheck(id);
        cartDao.deleteId(id);
    }

    public void deleteAll(String userEmail){
        cartDao.deleteAll(userEmail);
    }



    //check functions

    public CartPojo getCheck(int itemNo) throws ApiException { //todo make this private.
        CartPojo cartPojo = cartDao.selectId(itemNo);
        if (cartPojo == null) {
            throw new ApiException("Cart item with given ID does not exist!");
        }
        return cartPojo;
    }

}
