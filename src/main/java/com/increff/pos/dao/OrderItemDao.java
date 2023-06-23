package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao{
//    private static String select_id = "select orderItemPojo from OrderItemPojo orderItemPojo where productId=:id";
//    private static String select_all = "select orderItemPojo from OrderItemPojo orderItemPojo";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        System.out.println("  HELLO  " + orderItemPojo.getOrderItemId() + "  " + orderItemPojo.getOrderId() + "   " + orderItemPojo.getProductQuantity()+"   "+orderItemPojo.getProductId()+ "   "+orderItemPojo.getSellingPrice());
        entityManager.persist(orderItemPojo);
    }

}
