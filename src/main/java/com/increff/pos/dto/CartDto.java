package com.increff.pos.dto;

import com.increff.pos.model.data.CartData;
import com.increff.pos.model.forms.CartForm;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidationUtil;
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
        normalize(cartForm);
        ValidationUtil.checkValid(cartForm);
        ProductPojo productPojo = productService.getCheckBarcode(cartForm.getBarcode());
        double productMrp = productPojo.getMrp();
        int productId = productPojo.getId();
        int availableInventory = inventoryService.get(productId).getQuantity();
        CartPojo cartPojo = cartService.get(productId, SecurityUtil.getUser());
        if(cartPojo != null){
            int updatedQuantity = cartPojo.getQuantity() + cartForm.getQuantity();
            checkInput(updatedQuantity, cartForm.getSellingPrice(), availableInventory, productMrp);
            cartService.update(cartPojo.getId(), updatedQuantity, cartForm.getSellingPrice());
            return;
        }
        checkInput(cartForm.getQuantity(), cartForm.getSellingPrice(), availableInventory, productMrp);
        cartService.add(convert(cartForm, productId));
    }


    public CartData get(int id) throws ApiException {
        CartPojo cartPojo = cartService.get(id);
        String productBarcode = productService.get(cartPojo.getProductId()).getBarcode();
        return convert(cartPojo, productBarcode);
    }

    public List<CartData> getAll() throws ApiException {
        List<CartPojo> list = cartService.getAll(SecurityUtil.getUser());
        List<CartData> list2 = new ArrayList<CartData>();
        for (CartPojo cartPojo : list) {
            String productBarcode = productService.get(cartPojo.getProductId()).getBarcode();
            list2.add(convert(cartPojo, productBarcode));
        }
        return list2;
    }

    public void delete(int id) throws ApiException{
        cartService.delete(id);
    }

    public void update(int id, CartForm cartForm) throws ApiException {
        normalize(cartForm);
        ValidationUtil.checkValid(cartForm);
        ProductPojo productPojo = productService.getCheckBarcode(cartForm.getBarcode());
        double productMrp = productPojo.getMrp();
        int productId = productPojo.getId();
        int availableInventory = inventoryService.get(productId).getQuantity();
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

    //convert functions

    private static CartData convert(CartPojo cartPojo, String productBarcode) {
        CartData cartData = new CartData();
        cartData.setId(cartPojo.getId());
        cartData.setBarcode(productBarcode);
        cartData.setQuantity(cartPojo.getQuantity());
        cartData.setSellingPrice(cartPojo.getSellingPrice());
        return cartData;
    }

    private static CartPojo convert(CartForm cartForm, int productId) {
        CartPojo cartPojo = new CartPojo();
        cartPojo.setUserEmail(SecurityUtil.getUser());
        cartPojo.setProductId(productId);
        cartPojo.setQuantity((int)cartForm.getQuantity());
        cartPojo.setSellingPrice(cartForm.getSellingPrice());
        return cartPojo;
    }

    private static void normalize(CartForm cartForm){
        cartForm.setBarcode(StringUtil.toLowerCase(cartForm.getBarcode()));
        cartForm.setSellingPrice(StringUtil.roundOff(cartForm.getSellingPrice()));
    }

}
