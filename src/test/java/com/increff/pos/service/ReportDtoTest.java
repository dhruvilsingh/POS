//package com.increff.pos.service;
//
//import com.increff.pos.dto.*;
//import com.increff.pos.model.forms.*;
//import com.increff.pos.util.StringUtil.OrderStatus;
//import org.junit.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockHttpServletRequest;
//
//import javax.servlet.http.HttpServletRequest;
//
//import static junit.framework.TestCase.assertEquals;
//
//public class ReportDtoTest extends AbstractUnitTest{
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
//    @Autowired
//    private ReportDto reportDto;
//    @Autowired
//    private ReportService reportService;
//
//    @Before
//    public void addTestData() throws ApiException {
//        BrandForm brandForm = new BrandForm();
//        brandForm.setBrandName(" Brand1 ");
//        brandForm.setCategory("cAt1   ");
//        brandDto.add(brandForm);
//
//        ProductForm productForm = new ProductForm();
//        productForm.setProductBrand(brandDto.getAll().get(0).getBrandName());
//        productForm.setProductCategory(brandDto.getAll().get(0).getCategory());
//        for(int i=0; i<5 ; i++){
//            productForm.setProductName("Product"+i);
//            productForm.setProductBarcode("Barcode"+i);
//            productForm.setProductMrp(Double.valueOf(100 + i*10));
//            productDto.add(productForm);
//        }
//
//        InventoryForm inventoryForm = new InventoryForm();
//        for(int i=0; i<5 ; i++){
//            inventoryForm.setProductBarcode("Barcode"+i);
//            inventoryForm.setProductQuantity(100);
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
//            cartForm.setProductBarcode("barcode"+i);
//            cartForm.setProductSP(Double.valueOf(100 + i*10));
//            cartForm.setProductQuantity(50 + i*10);
//            cartDto.add(cartForm);
//        }
//        orderItemDto.add();
//        orderService.update(orderDto.getAll().get(0).getOrderId(), OrderStatus.INVOICED);
//    }
////
////    @Test
////    public void testGetDailySalesReport() throws ApiException {
////        reportService.addDailySalesReport();
////        List<DailySalesData> dailySalesDataList = reportDto.getDailySalesReport();
////        for(int i=0; i< dailySalesDataList.size(); i++){
////            assertEquals("111", dailySalesDataList.get(i).getDate());
////            assertEquals("", dailySalesDataList.get(i).getRevenue());
////            assertEquals("", dailySalesDataList.get(i).getOrderCount());
////            assertEquals("", dailySalesDataList.get(i).getOrderItemCount());
////        }
////    }
//}
