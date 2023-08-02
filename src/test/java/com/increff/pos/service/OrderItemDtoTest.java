//package com.increff.pos.service;
//
//import com.increff.pos.dto.*;
//import com.increff.pos.model.data.InventoryData;
//import com.increff.pos.model.data.OrderItemData;
//import com.increff.pos.model.forms.*;
//import com.increff.pos.model.enums.OrderStatus;
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
//public class OrderItemDtoTest extends AbstractUnitTest{
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
//
//    @Test
//    public void testAdd() throws ApiException {
//        orderItemDto.add();
//        int orderId = orderDto.getAll().get(0).getId();
//        List<OrderItemData> orderItemList = orderItemDto.getAll(orderId);
//        int size = orderItemList.size();
//        for(int i=0; i<size ; i++){
//            assertEquals(100 + i*10, orderItemList.get(i).getSellingPrice(), 0);
//            assertEquals("barcode"+i, orderItemList.get(i).getProductBarcode());
//            assertEquals(50 + i*10, orderItemList.get(i).getQuantity(), 0);
//            assertEquals("product"+i, orderItemList.get(i).getProductName());
//            assertEquals(100 + i*10, orderItemList.get(i).getProductMrp(),0);
//        }
//
//        List<InventoryData> inventoryData = inventoryDto.getAll();
//        int size1 = inventoryData.size();
//        for(int i=0; i<size1 ; i++){
//            assertEquals(50-i*10, inventoryData.get(size1-1-i).getQuantity(), 0);
//            assertEquals("barcode"+i, inventoryData.get(size1-1-i).getBarcode());
//        }
//    }
//
//    @Test
//    public void testInvalidQuantityAdd(){
//        try{
//            InventoryForm inventoryForm = new InventoryForm();
//            inventoryForm.setQuantity(50);
//            inventoryForm.setBarcode("barcode4");
//            int productId = inventoryDto.getAll().get(0).getProductId();
//            inventoryDto.update(productId, inventoryForm);
//
//            orderItemDto.add();
//        }catch(ApiException exception){
//            assertEquals("50 items are left for barcode4", exception.getMessage());
//        }
//    }
//
//    @Test
//    public void testInvalidSellingPriceAdd(){
//        try{
//            ProductForm productForm = new ProductForm();
//            int productId = productDto.getAll().get(0).getId();
//            productForm.setBarcode("barcode4");
//            productForm.setBrand("brand1");
//            productForm.setCategory("cat1");
//            productForm.setName("product4");
//            productForm.setMrp(Double.valueOf(100));
//            productDto.update(productId,productForm);
//
//            orderItemDto.add();
//        }catch(ApiException exception){
//            assertEquals("Selling price for barcode4 is greater than Mrp 100.0", exception.getMessage());
//        }
//    }
//
//    @Test
//    public void testGetById() throws ApiException {
//        orderItemDto.add();
//        int orderId = orderDto.getAll().get(0).getId();
//        for(int i=0; i<5 ; i++){
//            int itemId = orderItemDto.getAll(orderId).get(i).getId();
//            assertEquals(100 + i*10, orderItemDto.get(itemId).getSellingPrice(), 0);
//            assertEquals("barcode"+i, orderItemDto.get(itemId).getProductBarcode());
//            assertEquals(50 + i*10, orderItemDto.get(itemId).getQuantity(), 0);
//            assertEquals("product"+i, orderItemDto.get(itemId).getProductName());
//            assertEquals(100 + i*10, orderItemDto.get(itemId).getProductMrp(),0);
//        }
//    }
//
//    @Test
//    public void testGetByInvalidId(){
//        try{
//            orderItemDto.get(0);
//        }catch (ApiException exception){
//            assertEquals("Item with given ID does not exist!", exception.getMessage());
//        }
//    }
//
//    @Test
//    public void testDelete(){
//        try{
//            orderItemDto.add();
//            int orderId = orderDto.getAll().get(0).getId();
//            List<OrderItemData> orderItemData = orderItemDto.getAll(orderId);
//            int size = orderItemData.size();
//            for(int i=0; i<size ; i++){
//                int itemId = orderItemData.get(i).getId();
//                orderItemDto.delete(itemId);
//            }
//        }catch (ApiException exception){
//            assertEquals("Order cannot be empty!", exception.getMessage());
//        }
//    }
//
//    @Test
//    public void testDeleteInvalidId(){
//        try{
//            orderItemDto.delete(0);
//        }catch(ApiException exception){
//            assertEquals("Item with given ID does not exist!", exception.getMessage());
//        }
//    }
//
//    @Test
//    public void testDeleteInvoicedOrder(){
//        try{
//            orderItemDto.add();
//            int orderId = orderDto.getAll().get(0).getId();
//            orderService.update(orderId, OrderStatus.INVOICED);
//            List<OrderItemData> orderItemData = orderItemDto.getAll(orderId);
//            orderItemDto.delete(orderItemData.get(1).getId());
//        }catch (ApiException exception){
//            assertEquals("Order is invoiced!", exception.getMessage());
//        }
//    }
//
//    @Test
//    public void testUpdate() throws ApiException{
//        orderItemDto.add();
//        int orderId = orderDto.getAll().get(0).getId();
//        int size = orderItemDto.getAll(orderId).size();
//
//        for(int i=0; i<size; i++){
//            int orderItemId = orderItemDto.getAll(orderId).get(i).getId();
//            OrderItemForm orderItemForm = new OrderItemForm();
//            orderItemForm.setQuantity(70 - i*10);
//            orderItemForm.setSellingPrice(Double.valueOf(100));
//            orderItemDto.update(orderItemId, orderItemForm);
//        }
//
//        for(int i=0; i<size; i++){
//            int orderItemId = orderItemDto.getAll(orderId).get(i).getId();
//            assertEquals(100, orderItemDto.get(orderItemId).getSellingPrice(), 0);
//            assertEquals("barcode"+i, orderItemDto.get(orderItemId).getProductBarcode());
//            assertEquals(70 - i*10, orderItemDto.get(orderItemId).getQuantity(), 0);
//            assertEquals("product"+i, orderItemDto.get(orderItemId).getProductName());
//            assertEquals(100 + i*10, orderItemDto.get(orderItemId).getProductMrp(),0);
//        }
//    }
//
//    @Test
//    public void testInvoicedItemUpdate() throws ApiException{
//        try{
//            orderItemDto.add();
//            int orderId = orderDto.getAll().get(0).getId();
//            orderService.update(orderId, OrderStatus.INVOICED);
//            int itemId = orderItemDto.getAll(orderId).get(0).getId();
//
//            OrderItemForm orderItemForm = new OrderItemForm();
//            orderItemForm.setSellingPrice(Double.valueOf(100));
//            orderItemForm.setQuantity(50);
//            orderItemDto.update(itemId, orderItemForm);
//        }catch (ApiException exception){
//            assertEquals("Order is invoiced!", exception.getMessage());
//        }
//    }
//
//    @Test
//    public void testUpdateInvalidSellingPrice(){
//        try{
//            orderItemDto.add();
//            int orderId = orderDto.getAll().get(0).getId();
//            int itemId = orderItemDto.getAll(orderId).get(0).getId();
//
//            OrderItemForm orderItemForm = new OrderItemForm();
//            orderItemForm.setSellingPrice(Double.valueOf(110));
//            orderItemForm.setQuantity(50);
//            orderItemDto.update(itemId, orderItemForm);
//        }catch (ApiException exception){
//            assertEquals("Selling price cannot be more than MRP!!  (MRP = 100.0)", exception.getMessage());
//        }
//    }
//
//    @Test
//    public void testUpdateInvalidQuantity(){
//        try{
//            orderItemDto.add();
//            int orderId = orderDto.getAll().get(0).getId();
//            int itemId = orderItemDto.getAll(orderId).get(0).getId();
//
//            OrderItemForm orderItemForm = new OrderItemForm();
//            orderItemForm.setSellingPrice(Double.valueOf(100));
//            orderItemForm.setQuantity(110);
//            orderItemDto.update(itemId, orderItemForm);
//        }catch (ApiException exception){
//            assertEquals("Sorry! Available up to: 100", exception.getMessage());
//        }
//    }
//
//}
