package com.increff.pos.dao;

import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.pojo.OrdersPojo;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao{
    private static String SELECT_BY_ID = "select ordersPojo from OrdersPojo ordersPojo where id=:id";
    private static String SELECT_ALL = "select ordersPojo from OrdersPojo ordersPojo order by ordersPojo.id desc";
    private static String SELECT_ORDER_COUNT = "select coalesce(count(op.id),0) from OrdersPojo op " +
            "WHERE op.time >= :dateTimeStartOfDay AND op.time < :dateTimeStartOfNextDay " +
            "and op.status = :orderStatus";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int insert(OrdersPojo ordersPojo) {
        entityManager.persist(ordersPojo);
        return ordersPojo.getId();
    }

    public OrdersPojo select(int orderId) {
        TypedQuery<OrdersPojo> query = getQuery(SELECT_BY_ID, OrdersPojo.class);
        query.setParameter("id", orderId);
        return getSingle(query);
    }

    public int selectOrderCount(ZonedDateTime dateTimeStartOfDay, ZonedDateTime dateTimeStartOfNextDay){
        TypedQuery<Long> query = getQuery(SELECT_ORDER_COUNT , Long.class);
        query.setParameter("dateTimeStartOfDay", dateTimeStartOfDay);
        query.setParameter("dateTimeStartOfNextDay", dateTimeStartOfNextDay);
        query.setParameter("orderStatus", OrderStatus.INVOICED);
        return getSingle(query).intValue();
    }

    public List<OrdersPojo> selectAll() {
        TypedQuery<OrdersPojo> query = getQuery(SELECT_ALL, OrdersPojo.class);
        return query.getResultList();
    }

}
