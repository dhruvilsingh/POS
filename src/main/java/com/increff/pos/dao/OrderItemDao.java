package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.model.enums.OrderStatus;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao{

    private static String SELECT_ALL = "select orderItemPojo from OrderItemPojo orderItemPojo where orderId=:id order by orderItemPojo.id desc";

    private static String SELECT_BY_ID = "select orderItemPojo from OrderItemPojo orderItemPojo where id = :id";

    private static String SELECT_ITEM_BY_ORDER_ID = "select orderItemPojo from OrderItemPojo orderItemPojo where orderID = :orderId and" +
                                "productId = :productId";

    private static String DELETE_BY_ID = "delete from OrderItemPojo orderItemPojo where id=:id";


    private static String SELECT_ITEM_COUNT = "SELECT COUNT(ip.id) " +
                                                    "FROM OrdersPojo op " +
                                                    "INNER JOIN OrderItemPojo ip on " +
                                                    "op.id = ip.orderId "+
                                                    "WHERE op.time >= :dateTimeStartOfDay AND op.time < :dateTimeStartOfNextDay " +
                                                    "and op.status = :orderStatus";

    private static String SELECT_REVENUE = "SELECT SUM(ip.sellingPrice * ip.quantity) " +
                                                "FROM OrdersPojo op " +
                                                "INNER JOIN OrderItemPojo ip on " +
                                                "op.id = ip.orderId "+
                                                "WHERE op.time >= :dateTimeStartOfDay AND op.time < :dateTimeStartOfNextDay " +
                                                "and op.status = :orderStatus";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        entityManager.persist(orderItemPojo);
    }

    public OrderItemPojo selectId(int itemId){
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ID, OrderItemPojo.class);
        query.setParameter("id", itemId);
        return getSingle(query);
    }

    public OrderItemPojo selectOrderItem(int productId, int orderId){
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ITEM_BY_ORDER_ID, OrderItemPojo.class);
        query.setParameter("productId", productId);
        query.setParameter("orderId", orderId);
        return getSingle(query);
    }

    public List<OrderItemPojo> selectAll(int orderId){
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ALL, OrderItemPojo.class);
        query.setParameter("id", orderId);
        return query.getResultList();
    }

    @Transactional
    public int deleteId(int itemNo) {
        Query query = entityManager.createQuery(DELETE_BY_ID);
        query.setParameter("id", itemNo);
        return query.executeUpdate();
    }

    public int selectItemCount(ZonedDateTime dateTimeStartOfDay, ZonedDateTime dateTimeStartOfNextDay) {
        TypedQuery<Long> query = getQuery(SELECT_ITEM_COUNT, Long.class);
        query.setParameter("dateTimeStartOfDay", dateTimeStartOfDay);
        query.setParameter("dateTimeStartOfNextDay", dateTimeStartOfNextDay);
        query.setParameter("orderStatus", OrderStatus.INVOICED);
        return getSingle(query).intValue();
    }

    public double selectRevenue(ZonedDateTime dateTimeStartOfDay, ZonedDateTime dateTimeStartOfNextDay){
        TypedQuery<Double> query = getQuery(SELECT_REVENUE, Double.class);
        query.setParameter("dateTimeStartOfDay", dateTimeStartOfDay);
        query.setParameter("dateTimeStartOfNextDay", dateTimeStartOfNextDay);
        query.setParameter("orderStatus", OrderStatus.INVOICED);
        return getSingle(query).doubleValue();
    }

}
