package com.increff.pos.dto;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.CartDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.model.form.CartForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.OrderService;
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

public class OrderItemDtoTest extends AbstractUnitTest {

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
    private OrderService orderService;
    @Autowired
    private OrderItemDto orderItemDto;
    @Autowired
    private OrderItemDao orderItemDao;
    private List<Integer> productIdList = new ArrayList<>();
    private final String userEmail = "test@test.com";
    private int orderId;

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
        for(int i=0; i<4 ; i++){
            CartPojo cartPojo = ConversionUtil.convert(cartFormList.get(i), CartPojo.class);
            cartPojo.setProductId(productIdList.get(i));
            cartPojo.setUserEmail(userEmail);
            cartDao.insert(cartPojo);
        }

        orderDto.createOrder();
        orderId = orderDto.getAll().get(0).getId();
    }

    //Test adding existing item to order
    @Test
    public void testAddExistingOrderItem() throws ApiException {
        OrderItemForm orderItemForm = TestUtil.getOrderItemForm("barcode0",20,99.0);
        orderItemDto.add(orderItemForm,orderId);

        OrderItemPojo orderItemPojo = orderItemDao.selectOrderItem(productIdList.get(0),orderId);
        assertEquals(orderId, orderItemPojo.getOrderId(), 0);
        assertEquals(99,orderItemPojo.getSellingPrice(),0);
        assertEquals(70, orderItemPojo.getQuantity(),0);
        assertEquals(productIdList.get(0), orderItemPojo.getProductId());
    }

    //Test adding new item to order
    @Test
    public void testAddNewItem() throws ApiException {
        OrderItemForm orderItemForm = TestUtil.getOrderItemForm("barcode4",50,140.0);
        orderItemDto.add(orderItemForm,orderId);

        OrderItemPojo orderItemPojo = orderItemDao.selectOrderItem(productIdList.get(4),orderId);
        assertEquals(orderId, orderItemPojo.getOrderId(), 0);
        assertEquals(140,orderItemPojo.getSellingPrice(),0);
        assertEquals(50, orderItemPojo.getQuantity(),0);
        assertEquals(productIdList.get(4), orderItemPojo.getProductId());
    }

    //Test negative quantity validation
    @Test
    public void testInvalidQuantityAdd(){
        try{
            OrderItemForm orderItemForm = TestUtil.getOrderItemForm("barcode0",-50,100.0);
            orderItemDto.add(orderItemForm,orderId);
            fail();
        }catch(ApiException exception){
            assertEquals("Quantity should be more than 0", exception.getMessage());
        }
    }

    //Test invalid selling price validation
    @Test
    public void testInvalidSellingPriceAdd(){
        try{
            OrderItemForm orderItemForm = TestUtil.getOrderItemForm("barcode0",50,1000.0);
            orderItemDto.add(orderItemForm,orderId);
            fail();
        }catch(ApiException exception){
            assertEquals("Selling price for barcode0 cannot be more than MRP!!  (MRP = 100.0)", exception.getMessage());
        }
    }

    //Test get order item by ID
    @Test
    public void testGetById() throws ApiException {
        for(int i=0; i<4 ; i++){
            int itemId = orderItemDao.selectAll(orderId).get(i).getId();
            assertEquals(100 + i*10, orderItemDto.get(itemId).getSellingPrice(), 0);
            assertEquals("barcode"+i, orderItemDto.get(itemId).getBarcode());
            assertEquals(50, orderItemDto.get(itemId).getQuantity(), 0);
            assertEquals("product"+i, orderItemDto.get(itemId).getProductName());
            assertEquals(100 + i*10, orderItemDto.get(itemId).getProductMrp(),0);
        }
    }

    @Test
    public void testGetAll() throws ApiException {
        List<OrderItemData> orderItemData = orderItemDto.getAll(orderId);
        int size = orderItemData.size();
        for(int i=0; i<4; i++){
            assertEquals(100 + i*10, orderItemData.get(i).getSellingPrice(), 0);
            assertEquals("barcode"+i, orderItemData.get(i).getBarcode());
            assertEquals(50, orderItemData.get(i).getQuantity(), 0);
            assertEquals("product"+i, orderItemData.get(i).getProductName());
            assertEquals(100 + i*10, orderItemData.get(i).getProductMrp(),0);
        }
    }

    //Test delete an item by ID
    @Test
    public void testDelete(){
        try{
            List<OrderItemPojo> orderItemPojoList = orderItemDao.selectAll(orderId);
            int size = orderItemPojoList.size();
            for(int i=0; i<size ; i++){
                int itemId = orderItemPojoList.get(i).getId();
                orderItemDto.delete(itemId);
            }
            fail();
        }catch (ApiException exception){
            assertEquals("Order cannot be empty!", exception.getMessage());
        }
    }

    //Test delete order item from invoiced order
    @Test
    public void testDeleteInvoicedOrder(){
        try{
            orderService.update(orderId, OrderStatus.INVOICED);
            orderItemDto.delete(orderItemDao.selectAll(orderId).get(0).getId());
            fail();
        }catch (ApiException exception){
            assertEquals("Order is invoiced!", exception.getMessage());
        }
    }

    //Test order item update
    @Test
    public void testUpdate() throws ApiException{
        int orderItemId = orderItemDao.selectAll(orderId).get(0).getId();
        OrderItemForm orderItemForm = TestUtil.getOrderItemForm("barcode0", 10,50.0);
        orderItemDto.update(orderItemId,orderItemForm);
        OrderItemPojo orderItemPojo = orderItemDao.selectId(orderItemId);
        assertEquals(orderId,orderItemPojo.getOrderId(),0);
        assertEquals(productIdList.get(0),orderItemPojo.getProductId());
        assertEquals(10,orderItemPojo.getQuantity(),0);
        assertEquals(50.0,orderItemPojo.getSellingPrice(),0);
    }

    //Test update order item in invoiced order
    @Test
    public void testInvoicedItemUpdate() throws ApiException{
        try{
            orderService.update(orderId, OrderStatus.INVOICED);
            int orderItemId = orderItemDao.selectAll(orderId).get(0).getId();
            OrderItemForm orderItemForm = TestUtil.getOrderItemForm("barcode0", 10,50.0);
            orderItemDto.update(orderItemId,orderItemForm);
            fail();
        }catch (ApiException exception){
            assertEquals("Order is invoiced!", exception.getMessage());
        }
    }

    //Test invalid selling price validation on order item update
    @Test
    public void testUpdateInvalidSellingPrice(){
        try{
            int orderItemId = orderItemDao.selectAll(orderId).get(0).getId();
            OrderItemForm orderItemForm = TestUtil.getOrderItemForm("barcode0", 50,500.0);
            orderItemDto.update(orderItemId,orderItemForm);
            fail();
        }catch (ApiException exception){
            assertEquals("Selling price for barcode0 cannot be more than MRP!!  (MRP = 100.0)", exception.getMessage());
        }
    }

    //Test invalid quantity validation on order item update
    @Test
    public void testUpdateInvalidQuantity(){
        try{
            int orderItemId = orderItemDao.selectAll(orderId).get(0).getId();
            OrderItemForm orderItemForm = TestUtil.getOrderItemForm("barcode0", 500,100.0);
            orderItemDto.update(orderItemId,orderItemForm);
            fail();
        }catch (ApiException exception){
            assertEquals("100 items left in inventory!", exception.getMessage());
        }
    }

}
