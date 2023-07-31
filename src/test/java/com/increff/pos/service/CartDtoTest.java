package com.increff.pos.service;

import com.increff.pos.dto.*;
import com.increff.pos.model.data.CartData;
import com.increff.pos.model.forms.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CartDtoTest extends AbstractUnitTest{

    @Autowired
    private BrandDto brandDto;
    @Autowired
    private CartDto cartDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private UserDto userDto;
    @Autowired
    private LoginDto loginDto;

    @Before
    public void addTestData() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(" Brand1 ");
        brandForm.setCategory("cAt1   ");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand(brandDto.getAll().get(0).getBrand());
        productForm.setCategory(brandDto.getAll().get(0).getCategory());
        for(int i=0; i<5 ; i++){
            productForm.setName("Product"+i);
            productForm.setBarcode("Barcode"+i);
            productForm.setMrp(Double.valueOf(100 + i*10));
            productDto.add(productForm);
        }

        InventoryForm inventoryForm = new InventoryForm();
        for(int i=0; i<5 ; i++){
            inventoryForm.setBarcode("Barcode"+i);
            inventoryForm.setQuantity(100);
            inventoryDto.addInventory(inventoryForm);
        }

        UserForm userForm = new UserForm();
        userForm.setEmail("test@test.com");
        userForm.setPassword("1234");
        userDto.addUser(userForm);

        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("test@test.com");
        loginForm.setPassword("1234");
        HttpServletRequest request = new MockHttpServletRequest();
        loginDto.login(request,loginForm);
    }

    @Test
    public void testAddCart() throws ApiException {
        CartForm cartForm = new CartForm();
        cartForm.setBarcode("barcode4");
        cartForm.setSellingPrice(Double.valueOf(100));
        cartForm.setQuantity(50);
        cartDto.add(cartForm);

        int cartItemId = cartDto.getAll().get(0).getId();

        assertEquals("barcode4",cartDto.get(cartItemId).getBarcode());
        assertEquals(50,cartDto.get(cartItemId).getQuantity(),0);
        assertEquals(100,cartDto.get(cartItemId).getSellingPrice(), 0);
    }

    @Test
    public void testAddUpdateCart() throws ApiException{
        CartForm cartForm = new CartForm();
        cartForm.setBarcode("barcode4");
        cartForm.setSellingPrice(Double.valueOf(100));
        cartForm.setQuantity(50);
        cartDto.add(cartForm);
        cartDto.add(cartForm);

        int cartItemId = cartDto.getAll().get(0).getId();

        assertEquals("barcode4",cartDto.get(cartItemId).getBarcode());
        assertEquals(100,cartDto.get(cartItemId).getQuantity(),0);
        assertEquals(100,cartDto.get(cartItemId).getSellingPrice(), 0);
    }

    @Test
    public void testAddInvalidSellingPrice(){
        CartForm cartForm = new CartForm();
        cartForm.setBarcode("barcode4");
        cartForm.setSellingPrice(Double.valueOf(150));
        cartForm.setQuantity(50);
        try{
            cartDto.add(cartForm);
        }catch(ApiException exception){
            assertEquals("Selling price cannot be more than MRP!!  (MRP = 140.0)!", exception.getMessage());
        }
    }

    @Test public void testAddInvalidQuantity(){
        CartForm cartForm = new CartForm();
        cartForm.setBarcode("barcode4");
        cartForm.setSellingPrice(Double.valueOf(100));
        cartForm.setQuantity(120);
        try{
            cartDto.add(cartForm);
        }catch(ApiException exception){
            assertEquals("Inventory has only 100 items!", exception.getMessage());
        }
    }

    @Test
    public void testGetByInvalidId(){
        try{
            cartDto.get(0);
        }catch(ApiException exception){
            assertEquals("Cart item with given ID does not exist!", exception.getMessage());
        }
    }

    @Test
    public void testGetAll() throws ApiException{
        CartForm cartForm = new CartForm();
        for(int i=0; i<5 ; i++){
            cartForm.setBarcode("barcode"+i);
            cartForm.setSellingPrice(Double.valueOf(100 + i*10));
            cartForm.setQuantity(50);
            cartDto.add(cartForm);
        }

        List<CartData> cartData = cartDto.getAll();
        int size = cartData.size();
        for(int i=0; i<size ; i++){
            assertEquals(100 + i*10, cartData.get(size-1-i).getSellingPrice(), 0);
            assertEquals("barcode"+i, cartData.get(size-1-i).getBarcode());
            assertEquals(50, cartData.get(size-1-i).getQuantity(), 0);
        }
    }

    @Test
    public void testDeleteById() throws ApiException{
        CartForm cartForm = new CartForm();
        for(int i=0; i<5 ; i++){
            cartForm.setBarcode("barcode"+i);
            cartForm.setSellingPrice(Double.valueOf(100 + i*10));
            cartForm.setQuantity(50);
            cartDto.add(cartForm);
        }

        int cartItemId = cartDto.getAll().get(0).getId();
        cartDto.delete(cartItemId);

        List<CartData> cartData = cartDto.getAll();
        int size = cartData.size();
        for(int i=0; i<size ;i++){
            assertEquals(100 + i*10, cartData.get(size-1-i).getSellingPrice(), 0);
            assertEquals("barcode"+i, cartData.get(size-1-i).getBarcode());
            assertEquals(50, cartData.get(size-1-i).getQuantity(), 0);
        }
    }

    @Test
    public void testDeleteInvalidId(){
        try{
            cartDto.delete(0);
        }catch(ApiException exception){
            assertEquals("Cart item with given ID does not exist!", exception.getMessage());
        }
    }

    @Test
    public void testUpdate() throws ApiException{
        CartForm cartForm = new CartForm();
        cartForm.setBarcode("Barcode1");
        cartForm.setSellingPrice(Double.valueOf(100));
        cartForm.setQuantity(50);
        cartDto.add(cartForm);

        int cartItemId = cartDto.getAll().get(0).getId();

        cartForm.setBarcode("Barcode1");
        cartForm.setQuantity(40);
        cartForm.setSellingPrice(Double.valueOf(110));
        cartDto.update(cartItemId,cartForm);

        assertEquals("barcode1",cartDto.get(cartItemId).getBarcode());
        assertEquals(40,cartDto.get(cartItemId).getQuantity(), 0);
        assertEquals(110,cartDto.get(cartItemId).getSellingPrice(), 0);
    }

    @Test
    public void testUpdateInvalidBarcode(){
        CartForm cartForm = new CartForm();
        cartForm.setBarcode("Barcode1");
        cartForm.setSellingPrice(Double.valueOf(100));
        cartForm.setQuantity(50);
        try {
            cartDto.add(cartForm);

            int cartItemId = cartDto.getAll().get(0).getId();

            cartForm.setBarcode("InvalidBarcode");
            cartForm.setQuantity(40);
            cartForm.setSellingPrice(Double.valueOf(110));

            cartDto.update(cartItemId,cartForm);
        }catch (ApiException exception){
            assertEquals("Invalid Barcode!", exception.getMessage());
        }
    }
}
