package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
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
public class OrderItemDao extends AbstractDao{

    private static String select_all = "select orderItemPojo from OrderItemPojo orderItemPojo where orderId=:id";

    private static String select_item_count = "SELECT COUNT(orderItemPojo.orderItemId) " +
                                                    "FROM OrderPojo orderPojo " +
                                                    "INNER JOIN OrderItemPojo orderItemPojo on " +
                                                    "orderPojo.orderId = orderItemPojo.orderId "+
                                                    "WHERE DATE(orderPojo.orderTime) = :date";

    private static String select_revenue = "SELECT SUM(orderItemPojo.sellingPrice * orderItemPojo.productQuantity) " +
                                                "FROM OrderPojo orderPojo " +
                                                "INNER JOIN OrderItemPojo orderItemPojo on " +
                                                "orderPojo.orderId = orderItemPojo.orderId "+
                                                "WHERE DATE(orderPojo.orderTime) = :date";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        System.out.println("  HELLO  " + orderItemPojo.getOrderItemId() + "  " + orderItemPojo.getOrderId() + "   " + orderItemPojo.getProductQuantity()+"   "+orderItemPojo.getProductId()+ "   "+orderItemPojo.getSellingPrice());
        entityManager.persist(orderItemPojo);
    }

    public int selectItemCount(LocalDate date) {
        Date convertedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        TypedQuery<Long> query = getQuery(select_item_count, Long.class);
        query.setParameter("date", convertedDate);
        return query.getSingleResult().intValue();
    }

    public double selectRevenue(LocalDate date){
        Date convertedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        TypedQuery<Double> query = getQuery(select_revenue, Double.class);
        query.setParameter("date", convertedDate);
        return query.getSingleResult().doubleValue();
    }

    public List<OrderItemPojo> selectAll(int orderId){
        TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
        query.setParameter("id", orderId);
        return query.getResultList();
    }

}
