package com.increff.pos.dto;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.CartDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.dto.*;
import com.increff.pos.model.data.CartData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.CartForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CartDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CartDto cartDto;
    @Autowired
    private CartDao cartDao;
    @Autowired
    ProductService productService;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private UserDto userDto;
    @Autowired
    private LoginDto loginDto;

    private List<Integer> productIdList = new ArrayList<>();

    private final String userEmail = "test@test.com";

    @Before
    public void addTestData() throws ApiException {
        brandDao.insert(ConversionUtil.convert(TestUtil.getBrandForm("brand1","category1"), BrandPojo.class));

        List<ProductForm> productFormList = TestUtil.getProductFormList();
        int brandCategoryId = brandDao.selectAll().get(0).getId();
        List<ProductPojo> productPojoList = ConversionUtil.convert(productFormList, ProductPojo.class);;
        for(int i=0; i<5 ; i++){
            productPojoList.get(i).setBrandCategoryId(brandCategoryId);
        }

        productIdList = productService.addList(productPojoList);
        for(int i=0; i<5; i++){
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setProductId(productIdList.get(i));
            inventoryPojo.setQuantity(100 + i*10);
            inventoryDao.insert(inventoryPojo);
        }

        userDto.addUser(TestUtil.getUserForm(userEmail,"1234"));

        HttpServletRequest request = new MockHttpServletRequest();
        loginDto.login(request,TestUtil.getLoginForm(userEmail,"1234"));
    }

    //Test add product to cart
    @Test
    public void testAddCart() throws ApiException {
        CartForm cartForm = TestUtil.getCartForm("barcode0", 40, 100.0);
        cartDto.add(cartForm);

        CartPojo cartPojo = cartDao.selectAll(userEmail).get(0);

        assertEquals(productIdList.get(0),cartPojo.getId());
        assertEquals(40,cartPojo.getQuantity(),0);
        assertEquals(100,cartPojo.getSellingPrice(), 0);
    }

    //Test invalid selling price validation
    @Test
    public void testAddInvalidSellingPrice(){
        CartForm cartForm = TestUtil.getCartForm("barcode0", 40, 200.0);
        try{
            cartDto.add(cartForm);
            fail();
        }catch(ApiException exception){
            assertEquals("Selling price cannot be more than MRP!!  (MRP = 100.0)!", exception.getMessage());
        }
    }

    //Test invalid quantity validation
    @Test public void testAddInvalidQuantity(){
        CartForm cartForm = TestUtil.getCartForm("barcode0", 200, 100.0);
        try{
            cartDto.add(cartForm);
            fail();
        }catch(ApiException exception){
            assertEquals("100 items left in inventory!", exception.getMessage());
        }
    }

    //Test get cart item by ID
    @Test
    public void testGetById() throws ApiException {
        CartForm cartForm = TestUtil.getCartForm("barcode0", 40, 100.0);
        CartPojo cartPojo = ConversionUtil.convert(cartForm, CartPojo.class);
        cartPojo.setUserEmail(userEmail);
        cartPojo.setProductId(productIdList.get(0));
        cartDao.insert(cartPojo);

        int cartItemId = cartDao.selectAll("test@test.com").get(0).getId();
        CartData cartData = cartDto.get(cartItemId);
        assertEquals("barcode0",cartData.getBarcode());
        assertEquals(40,cartData.getQuantity(),0);
        assertEquals(100,cartData.getSellingPrice(), 0);
    }


    //Test get all cart items
    @Test
    public void testGetAll() throws ApiException{
        List<CartForm> cartFormList = TestUtil.getCartFormList();
        for(int i=0; i<5 ; i++){
            CartPojo cartPojo = ConversionUtil.convert(cartFormList.get(i), CartPojo.class);
            cartPojo.setProductId(productIdList.get(i));
            cartPojo.setUserEmail(userEmail);
            cartDao.insert(cartPojo);
        }

        List<CartData> cartData = cartDto.getAll();
        int size = cartData.size();
        for(int i=0; i<size ; i++){
            assertEquals(100 + i*10, cartData.get(size-1-i).getSellingPrice(), 0);
            assertEquals("barcode"+i, cartData.get(size-1-i).getBarcode());
            assertEquals(50, cartData.get(size-1-i).getQuantity(), 0);
        }
    }


    //Test delete cart item by ID
    @Test
    public void testDeleteById() throws ApiException{
        List<CartForm> cartFormList = TestUtil.getCartFormList();
        for(int i=0; i<5 ; i++){
            CartPojo cartPojo = ConversionUtil.convert(cartFormList.get(i), CartPojo.class);
            cartPojo.setProductId(productIdList.get(i));
            cartPojo.setUserEmail(userEmail);
            cartDao.insert(cartPojo);
        }

        int cartItemId = cartDao.selectAll(userEmail).get(0).getId();
        cartDto.delete(cartItemId);

        List<CartPojo> cartPojoList = cartDao.selectAll(userEmail);
        int size = cartPojoList.size();
        assertEquals(size, 4);
        for(int i=0; i<size ;i++){
            assertEquals(100 + i*10, cartPojoList.get(size-1-i).getSellingPrice(), 0);
            assertEquals(productIdList.get(i), cartPojoList.get(size-1-i).getProductId());
            assertEquals(50, cartPojoList.get(size-1-i).getQuantity(), 0);
        }
    }

    @Test
    public void testUpdate() throws ApiException{
        CartForm cartForm = TestUtil.getCartForm("barcode0", 40, 100.0);
        CartPojo cartPojo = ConversionUtil.convert(cartForm, CartPojo.class);
        cartPojo.setUserEmail(userEmail);
        cartPojo.setProductId(productIdList.get(0));
        cartDao.insert(cartPojo);

        int cartItemId = cartDao.selectAll(userEmail).get(0).getId();

        cartDto.update(cartItemId,TestUtil.getCartForm("barcode0", 60, 95.0));

        CartPojo updatedCartPojo = cartDao.selectAll(userEmail).get(0);
        assertEquals(productIdList.get(0), cartPojo.getProductId());
        assertEquals(60,cartPojo.getQuantity(), 0);
        assertEquals(95.0,cartPojo.getSellingPrice(), 0);
    }

}
