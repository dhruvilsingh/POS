package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao{
    private static String select_id = "select orderPojo from OrderPojo orderPojo where orderId=:id";
    private static String select_all = "select orderPojo from OrderPojo orderPojo order by orderPojo.orderId desc";
    private static String select_order_count = "select count(op.orderId) from OrderPojo op where date(orderTime) = :date";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int insert(OrderPojo orderPojo) {
        entityManager.persist(orderPojo);
        return orderPojo.getOrderId();
    }

    public OrderPojo select(int orderId) {
        TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
        query.setParameter("id", orderId);
        return getSingle(query);
    }

    public int selectcount(LocalDate date){
        Date convertedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        TypedQuery<Long> query = getQuery(select_order_count , Long.class);
        query.setParameter("date", convertedDate);
        return query.getSingleResult().intValue();
    }

    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
        return query.getResultList();
    }

    public void update(OrderPojo orderPojo) {
    }
}
