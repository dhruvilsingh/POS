package com.increff.pos.dto;

import com.increff.pos.controller.LoginController;
import com.increff.pos.model.CartData;
import com.increff.pos.model.CartForm;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    @Autowired
    private UserService userService;

    public void add(CartForm cartForm) throws ApiException {
        CartPojo cartPojo = cartService.get(cartForm.getProductBarcode(), getUser());
        if(cartPojo != null){
            int updatedQuantity = cartPojo.getProductQuantity() + cartForm.getProductQuantity();
            cartPojo.setProductQuantity(updatedQuantity);
            cartPojo.setProductSP(cartForm.getProductSP());
            checkInput(cartPojo);
            cartService.update(cartPojo.getItemNo(),cartPojo);
            return;
        }
        cartPojo = convert(cartForm);
        checkInput(cartPojo);
        cartService.add(cartPojo);
    }

    public CartData get(int id) throws ApiException {
        CartPojo cartPojo = cartService.get(id);
        return convert(cartPojo);
    }

    public List<CartData> getAll() {
        List<CartPojo> list = cartService.getAll(getUser());
        List<CartData> list2 = new ArrayList<CartData>();
        for (CartPojo cartPojo : list) {
            list2.add(convert(cartPojo));
        }
        return list2;
    }


    public void delete(int id){
        cartService.delete(id);
    }

    public void update(int id, CartForm cartForm) throws ApiException {
        CartPojo cartPojo = convert(cartForm);
        checkInput(cartPojo);
        cartService.update(id, cartPojo);
    }

    private void checkInput(CartPojo cartPojo) throws ApiException {
        int productId = productService.getId(cartPojo.getProductBarcode());
        if (productId == -1) {
            throw new ApiException("Invalid Barcode");
        }
        InventoryPojo inventoryPojo = inventoryService.get(productId);
        int productQuantity = inventoryPojo.getProductQuantity();
        int inputQuantity = cartPojo.getProductQuantity();
        double productMrp = productService.get(productId).getProductMrp();
        if (productQuantity < inputQuantity) {
            throw new ApiException("Inventory has " + productQuantity + " items");
        }
        if (productMrp < cartPojo.getProductSP()) {
            throw new ApiException("Selling price cannot be more than MRP!!  (MRP = " + productMrp + " )");
        }
    }

    private String getUser(){
        UserPrincipal principal = SecurityUtil.getPrincipal();
        return principal.getEmail();
    }

    private static CartData convert(CartPojo cartPojo) {
        CartData cartData = new CartData();
        cartData.setItemNo(cartPojo.getItemNo());
        cartData.setProductBarcode(cartPojo.getProductBarcode());
        cartData.setProductQuantity(cartPojo.getProductQuantity());
        cartData.setProductSP(cartPojo.getProductSP());
        return cartData;
    }

    private CartPojo convert(CartForm cartForm) {
        CartPojo cartPojo = new CartPojo();
        cartPojo.setUserEmail(getUser());
        cartPojo.setProductBarcode(cartForm.getProductBarcode());
        cartPojo.setProductQuantity(cartForm.getProductQuantity());
        cartPojo.setProductSP(cartForm.getProductSP());
        return cartPojo;
    }

}
