package com.increff.pos.util;

import com.increff.pos.model.form.*;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Component;

import java.awt.peer.PanelPeer;
import java.util.ArrayList;
import java.util.List;


@Component
public class TestUtil {

    public static BrandForm getBrandForm(String brand, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    public static List<BrandForm> getBrandFormList() {
        List<BrandForm> brandFormList = new ArrayList<>();
        for(int i=0; i<5; i++){
            BrandForm brandForm = getBrandForm("brand" + i, "category" + i);
            brandFormList.add(brandForm);
        }
        return brandFormList;
    }

    public static ProductForm getProductForm(String barcode, String brand, String category, String name, Double mrp) {
        ProductForm productForm = new ProductForm();
        productForm.setBarcode(barcode);
        productForm.setBrand(brand);
        productForm.setCategory(category);
        productForm.setName(name);
        productForm.setMrp(mrp);
        return productForm;
    }

    public static List<ProductForm> getProductFormList() {
        List<ProductForm> productFormList = new ArrayList<>();
        for(int i=0; i<5 ; i++){
            ProductForm productForm = getProductForm("barcode"+i,"brand1","category1",
                    "product"+i, (double) (100 + i*10));
            productFormList.add(productForm);
        }
        return productFormList;
    }

    public static InventoryForm getInventoryForm(String barcode, Integer quantity) {
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);
        return inventoryForm;
    }

    public static List<InventoryForm> getInventoryFormList() {
        List<InventoryForm> inventoryFormList = new ArrayList<>();
        for(int i=0; i<5; i++){
            InventoryForm inventoryForm = getInventoryForm("barcode"+i, 100 + i*10);
            inventoryFormList.add(inventoryForm);
        }
        return inventoryFormList;
    }

    public static CartForm getCartForm(String barcode, int quantity, Double sellingPrice) {
        CartForm cartForm = new CartForm();
        cartForm.setBarcode(barcode);
        cartForm.setQuantity(quantity);
        cartForm.setSellingPrice(sellingPrice);
        return cartForm;
    }

    public static List<CartForm> getCartFormList() {
        List<CartForm> cartFormList = new ArrayList<>();
        for(int i=0; i<5 ; i++){
            CartForm cartForm = getCartForm("barcode"+i, 50 , (double) (100 + i*10));
            cartFormList.add(cartForm);
        }
        return cartFormList;
    }

    public static OrderItemForm getOrderItemForm(String barcode, int quantity, Double sellingPrice) {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode(barcode);
        orderItemForm.setQuantity(quantity);
        orderItemForm.setSellingPrice(sellingPrice);
        return orderItemForm;
    }

    public static List<OrderItemPojo> getOrderItemPojoList(){
        List<OrderItemPojo> list = new ArrayList<>();
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(1);
        orderItemPojo.setProductId(1);
        orderItemPojo.setQuantity(50);
        orderItemPojo.setSellingPrice((double) (100));
        list.add(orderItemPojo);
        return list;
    }

    public static ProductPojo getProductPojo(){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBrandCategoryId(1);
        productPojo.setId(1);
        productPojo.setBarcode("barcode1");
        productPojo.setMrp(100.0);
        productPojo.setName("product1");
        return productPojo;
    }

    public static UserForm getUserForm(String email, String password) {
        UserForm userForm = new UserForm();
        userForm.setEmail(email);
        userForm.setPassword(password);
        return userForm;
    }

    public static List<UserForm> getUserFormList() {
        List<UserForm> userFormList = new ArrayList<>();
        for(int i=0; i<5; i++){
            UserForm userForm = new UserForm();
            userForm.setEmail("test"+i+"@test.com");
            userForm.setPassword("password"+i);
            userFormList.add(userForm);
        }
        return userFormList;
    }

    public static LoginForm getLoginForm(String email, String password) {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail(email);
        loginForm.setPassword(password);
        return loginForm;
    }




}
