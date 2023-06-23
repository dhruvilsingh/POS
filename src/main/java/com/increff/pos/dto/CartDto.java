package com.increff.pos.dto;

import com.increff.pos.controller.LoginController;
import com.increff.pos.model.CartData;
import com.increff.pos.model.CartForm;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.*;
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
        CartPojo cartPojo = cartService.get(cartForm.getProductBarcode(), LoginDto.userEmail);
        if(cartPojo != null){
            throw new ApiException("Product already exist in the cart!!");
        }
        check(cartForm);
        cartPojo = convert(cartForm);
        cartService.add(cartPojo);
    }

    public CartData get(int id) throws ApiException {
        CartPojo cartPojo = cartService.get(id);
        return convert(cartPojo);
    }
    public List<CartData> getAll() {
        List<CartPojo> list = cartService.getAll(LoginDto.userEmail);
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
        check(cartForm);
        CartPojo cartPojo = convert(cartForm);
        cartService.update(id, cartPojo);
    }

    private void check(CartForm cartForm) throws ApiException {
        int productId = productService.getId(cartForm.getProductBarcode());
        if (productId == -1) {
            throw new ApiException("Invalid Barcode");
        }
        InventoryPojo inventoryPojo = inventoryService.get(productId);
        int productQuantity = inventoryPojo.getProductQuantity();
        int inputQuantity = cartForm.getProductQuantity();
        double productMrp = productService.get(productId).getProductMrp();
        if (productQuantity < inputQuantity) {
            throw new ApiException("Inventory only has " + productQuantity + " items");
        }
        if (productMrp < cartForm.getProductSP()) {
            throw new ApiException("Selling price cannot be more than MRP!!  (MRP = " + productMrp + " )");
        }
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
        cartPojo.setUserEmail(LoginDto.userEmail);
        cartPojo.setProductBarcode(cartForm.getProductBarcode());
        cartPojo.setProductQuantity(cartForm.getProductQuantity());
        cartPojo.setProductSP(cartForm.getProductSP());
        return cartPojo;
    }

}
