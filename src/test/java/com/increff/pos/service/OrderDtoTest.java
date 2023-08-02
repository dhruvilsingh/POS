//package com.increff.pos.service;
//
//import com.increff.pos.dto.*;
//import com.increff.pos.model.data.OrderData;
//import com.increff.pos.model.forms.*;
//import com.increff.pos.util.StringUtil;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockHttpServletRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class OrderDtoTest extends AbstractUnitTest{
//
//    @Autowired
//    private BrandDto brandDto;
//    @Autowired
//    private CartDto cartDto;
//    @Autowired
//    private ProductDto productDto;
//    @Autowired
//    private InventoryDto inventoryDto;
//    @Autowired
//    private UserDto userDto;
//    @Autowired
//    private LoginDto loginDto;
//    @Autowired
//    private OrderItemDto orderItemDto;
//    @Autowired
//    private OrderDto orderDto;
//    @Autowired
//    private OrderService orderService;
//
//    @Before
//    public void addTestData() throws ApiException {
//        BrandForm brandForm = new BrandForm();
//        brandForm.setBrand(" Brand1 ");
//        brandForm.setCategory("cAt1   ");
//        brandDto.add(brandForm);
//
//        ProductForm productForm = new ProductForm();
//        productForm.setBrand(brandDto.getAll().get(0).getBrand());
//        productForm.setCategory(brandDto.getAll().get(0).getCategory());
//        for(int i=0; i<5 ; i++){
//            productForm.setName("Product"+i);
//            productForm.setBarcode("Barcode"+i);
//            productForm.setMrp(Double.valueOf(100 + i*10));
//            productDto.add(productForm);
//        }
//
//        InventoryForm inventoryForm = new InventoryForm();
//        for(int i=0; i<5 ; i++){
//            inventoryForm.setBarcode("Barcode"+i);
//            inventoryForm.setQuantity(100);
//            inventoryDto.addInventory(inventoryForm);
//        }
//
//        UserForm userForm = new UserForm();
//        userForm.setEmail("test@test.com");
//        userForm.setPassword("1234");
//        userDto.addUser(userForm);
//
//        LoginForm loginForm = new LoginForm();
//        loginForm.setEmail("test@test.com");
//        loginForm.setPassword("1234");
//        HttpServletRequest request = new MockHttpServletRequest();
//        loginDto.login(request,loginForm);
//
//        CartForm cartForm = new CartForm();
//        for(int i=0; i<5 ; i++){
//            cartForm.setBarcode("barcode"+i);
//            cartForm.setSellingPrice(Double.valueOf(100 + i*10));
//            cartForm.setQuantity(50 + i*10);
//            cartDto.add(cartForm);
//        }
//    }
//
//    @Test
//    public void testGetAll() throws ApiException{
//        orderItemDto.add();
//        List<OrderData> orderDataList = orderDto.getAll();
//        for(int i=0; i<orderDataList.size(); i++){
//            assertEquals(StringUtil.OrderStatus.CREATED, orderDataList.get(i).getStatus());
//            assertEquals(orderDto.get(orderDataList.get(i).getId()).getTime(), orderDataList.get(i).getTime());
//        }
//    }
//
//    @Test
//    public void getByInvalidId(){
//        try{
//            orderDto.get(0);
//        }catch (ApiException exception){
//            assertEquals("Order with given ID does not exists!", exception.getMessage());
//        }
//    }
//}
