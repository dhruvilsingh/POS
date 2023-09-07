package com.increff.pos.dto;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.CartDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dto.*;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.model.form.CartForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CartDao cartDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private UserDto userDto;
    @Autowired
    private LoginDto loginDto;
    @Autowired
    private OrderDto orderDto;
    @Autowired
    private OrderDao orderDao;
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

        List<CartForm> cartFormList = TestUtil.getCartFormList();
        for(int i=0; i<5 ; i++){
            CartPojo cartPojo = ConversionUtil.convert(cartFormList.get(i), CartPojo.class);
            cartPojo.setProductId(productIdList.get(i));
            cartPojo.setUserEmail(userEmail);
            cartDao.insert(cartPojo);
        }
    }

    //Test create order
    @Test
    public void testCreateOrder() throws ApiException {
        orderDto.createOrder();
        OrderStatus orderStatus = orderDao.selectAll().get(0).getStatus();
        assertEquals(OrderStatus.CREATED,orderStatus);

        //check if inventory is reduced
        List<InventoryPojo> inventoryPojoList = inventoryDao.selectAll();
        int size = inventoryPojoList.size();
        for(int i=0; i<size ; i++){
            assertEquals(productIdList.get(i), inventoryPojoList.get(size-1-i).getProductId());
            assertEquals(50 + i*10, (int)inventoryPojoList.get(size-1-i).getQuantity());
        }
    }

    //Test get all orders
    @Test
    public void testGetAll() throws ApiException{
        orderDto.createOrder();

        List<OrderData> orderDataList = orderDto.getAll();
        for(int i=0; i<orderDataList.size(); i++){
            assertEquals("CREATED", orderDataList.get(i).getStatus());
            assertEquals(orderDao.select(orderDataList.get(i).getId()).getTime(), orderDataList.get(i).getTime());
        }
    }

    //Test get order by ID
    @Test
    public void testGetById() throws ApiException {
        orderDto.createOrder();
        int orderId = orderDao.selectAll().get(0).getId();
        OrderData orderData = orderDto.get(orderId);
        assertEquals("CREATED", orderData.getStatus());
    }

    //Test cancel order
    @Test
    public void testCancelOrder() throws ApiException {
        orderDto.createOrder();
        int orderId = orderDao.selectAll().get(0).getId();
        orderDto.cancelOrder(orderId);
        assertEquals(OrderStatus.CANCELLED, orderDao.select(orderId).getStatus());
    }

}
