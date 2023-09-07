package com.increff.pos.dto;

import com.increff.pos.config.AbstractMockitoUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.CartDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.CartForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.InvoiceService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class InvoiceDtoTest extends AbstractMockitoUnitTest {
    @InjectMocks
    private InvoiceDto invoiceDto;
    @Mock
    private OrderService orderService;
    @Mock
    private OrderItemService orderItemService;
    @Mock
    private ProductService productService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private InvoiceService invoiceService;

    @Before
    public void setup() throws ApiException {
        initMocks(this);
    }

    //Test get invoice
    @Test
    public void testGetInvoice() throws ApiException, IOException {
        doNothing().when(orderService).getCheckCancelledStatus(1001);

        List<OrderItemPojo> list = TestUtil.getOrderItemPojoList();
        when(orderItemService.getAll(1001)).thenReturn(list);

        OrdersPojo orderPojo = mock(OrdersPojo.class);
        when(orderService.getCheck(1001)).thenReturn(orderPojo);
        when(orderService.getCheck(1001).getTime()).thenReturn(ZonedDateTime.now());

        ProductPojo productPojo = TestUtil.getProductPojo();
        when(productService.getCheck(1)).thenReturn(productPojo);

        doNothing().when(orderService).update(1001, OrderStatus.INVOICED);

        invoiceDto.getInvoice(1001);
        verify(orderService).getCheckCancelledStatus(1001);
        verify(orderService).update(1001,OrderStatus.INVOICED);
    }

}
