package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao{
    private static String select_id = "select orderPojo from OrderPojo orderPojo where orderId=:id";
    private static String select_all = "select orderPojo from OrderPojo orderPojo";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int insert(OrderPojo orderPojo) {
        System.out.println("order pojo id =  " + orderPojo.getOrderId());
        entityManager.persist(orderPojo);
        System.out.println("   persisted  ");
        return orderPojo.getOrderId();
    }

    public OrderPojo select(int orderId) {
        TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
        query.setParameter("id", orderId);
        return getSingle(query);
    }

    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
        return query.getResultList();
    }

    public void update(OrderPojo orderPojo) {
    }
}
