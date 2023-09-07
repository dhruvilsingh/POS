package com.increff.pos.dto;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.CartDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.data.BrandReportData;
import com.increff.pos.model.data.DailySalesData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.CartForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.schedulers.DailySalesReportScheduler;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
public class ReportDtoTest extends AbstractUnitTest {
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
    private List<Integer> productIdList = new ArrayList<>();
    private final String userEmail = "test@test.com";
    @Autowired
    private ReportDto reportDto;
    @Autowired
    private DailySalesReportScheduler dailySalesReportScheduler;

    @Before
    public void addTestData() throws ApiException {
        List<BrandForm> brandFormList = TestUtil.getBrandFormList();
        for(int i =0; i<brandFormList.size(); i++){
            brandDao.insert(ConversionUtil.convert(brandFormList.get(i), BrandPojo.class));
        }

        List<ProductForm> productFormList = TestUtil.getProductFormList();
        int brandCategoryId = brandDao.selectBrandCategory("brand1","category1").getId();
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

        orderDto.createOrder();
        int orderId = orderDto.getAll().get(0).getId();
        orderService.update(orderId, OrderStatus.INVOICED);
    }

    @Test
    public void testGetBrandReport() throws ApiException {
        List<BrandReportData> brandReportDataList = reportDto.getBrandReport();
        int size = brandReportDataList.size();
        for(int i=0; i<size; i++){
            assertEquals("brand"+i, brandReportDataList.get(size-i-1).getBrand());
            assertEquals("category"+i, brandReportDataList.get(size-i-1).getCategory());
        }
    }

    @Test
    public void testGetInventoryReport(){
        List<InventoryReportData> inventoryReportDataList = reportDto.getInventoryReport();
        int size = inventoryReportDataList.size();
        for(int i=0; i<size; i++){
            if(i==1){
                assertEquals("brand1", inventoryReportDataList.get(i).getBrand());
                assertEquals("category1", inventoryReportDataList.get(i).getCategory());
                assertEquals(350, inventoryReportDataList.get(i).getQuantity(),0);
            }
            else{
                assertEquals("brand"+i, inventoryReportDataList.get(i).getBrand());
                assertEquals("category"+i, inventoryReportDataList.get(i).getCategory());
                assertEquals(0, inventoryReportDataList.get(i).getQuantity(),0);
            }
        }
    }

    @Test
    public void testDailySaleReport() throws Exception {
        dailySalesReportScheduler.scheduleDailySale();
        Thread.sleep(11000);
        List<DailySalesData> dailySalesDataList = reportDto.getDailySalesReport();
        assertEquals(1,dailySalesDataList.get(0).getOrderCount(),0);
        assertEquals(5.0,dailySalesDataList.get(0).getOrderItemCount(),0);
        assertEquals(30000.0,dailySalesDataList.get(0).getRevenue());
    }
}
