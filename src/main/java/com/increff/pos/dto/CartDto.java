package com.increff.pos.dto;

import com.increff.pos.model.data.CartData;
import com.increff.pos.model.form.CartForm;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class CartDto {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public void add(CartForm cartForm) throws ApiException {
        NormalizeUtil.normalize(cartForm);
        ValidationUtil.checkValid(cartForm);
        ProductPojo productPojo = productService.getCheckBarcode(cartForm.getBarcode());
        double productMrp = productPojo.getMrp();
        int productId = productPojo.getId();
        int availableInventory = inventoryService.getCheck(productId).getQuantity();
        CartPojo cartPojo = cartService.get(productId, SecurityUtil.getUserEmail());
        if(cartPojo != null){
            int updatedQuantity = cartPojo.getQuantity() + cartForm.getQuantity();
            checkInput(updatedQuantity, cartForm.getSellingPrice(), availableInventory, productMrp);
            cartService.update(cartPojo.getId(), updatedQuantity, cartForm.getSellingPrice());
            return;
        }
        checkInput(cartForm.getQuantity(), cartForm.getSellingPrice(), availableInventory, productMrp);
        cartPojo = ConversionUtil.convert(cartForm, CartPojo.class);
        cartPojo.setProductId(productId);
        cartPojo.setUserEmail(SecurityUtil.getUserEmail());
        cartService.add(cartPojo);
    }


    public CartData get(int id) throws ApiException {
        CartPojo cartPojo = cartService.getCheck(id);
        String productBarcode = productService.getCheck(cartPojo.getProductId()).getBarcode();
        CartData cartData = ConversionUtil.convert(cartPojo, CartData.class);
        cartData.setBarcode(productBarcode);
        return cartData;
    }

    public List<CartData> getAll() throws ApiException {
        List<CartPojo> list = cartService.getAll(SecurityUtil.getUserEmail());
        List<CartData> list2 = new ArrayList<CartData>();
        for (CartPojo cartPojo : list) {
            String productBarcode = productService.getCheck(cartPojo.getProductId()).getBarcode();
            CartData cartData = ConversionUtil.convert(cartPojo, CartData.class);
            cartData.setBarcode(productBarcode);
            list2.add(cartData);
        }
        return list2;
    }

    public void delete(int id) throws ApiException{
        cartService.delete(id);
    }

    public void update(int id, CartForm cartForm) throws ApiException {
        NormalizeUtil.normalize(cartForm);
        ValidationUtil.checkValid(cartForm);
        ProductPojo productPojo = productService.getCheckBarcode(cartForm.getBarcode());
        double productMrp = productPojo.getMrp();
        int productId = productPojo.getId();
        int availableInventory = inventoryService.getCheck(productId).getQuantity();
        checkInput(cartForm.getQuantity(), cartForm.getSellingPrice(), availableInventory, productMrp);
        cartService.update(id, cartForm.getQuantity(), cartForm.getSellingPrice());
    }


    private void checkInput(int quantity, double sellingPrice, int availableInventory, double productMrp) throws ApiException {
        int inputQuantity = quantity;
        if (availableInventory < inputQuantity) {
            throw new ApiException(availableInventory + " items left in inventory!");
        }
        if (productMrp < sellingPrice) {
            throw new ApiException("Selling price cannot be more than MRP!!  (MRP = " + productMrp + ")!");
        }
    }

}
