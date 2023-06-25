package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao{

    private static String select_all = "select orderItemPojo from OrderItemPojo orderItemPojo where orderId=:id";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        System.out.println("  HELLO  " + orderItemPojo.getOrderItemId() + "  " + orderItemPojo.getOrderId() + "   " + orderItemPojo.getProductQuantity()+"   "+orderItemPojo.getProductId()+ "   "+orderItemPojo.getSellingPrice());
        entityManager.persist(orderItemPojo);
    }

    public List<OrderItemPojo> selectAll(int orderId){
        TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
        query.setParameter("id", orderId);
        return query.getResultList();
    }

}
