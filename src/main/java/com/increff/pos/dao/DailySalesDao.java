package com.increff.pos.dao;

import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.DailySalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DailySalesDao extends AbstractDao{
    private static String select_all = "select dailySalesPojo from DailySalesPojo dailySalesPojo order by dailySalesPojo.id desc";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(DailySalesPojo dailySalesPojo) {
        entityManager.persist(dailySalesPojo);
    }

    public List<DailySalesPojo> selectAll() {
        TypedQuery<DailySalesPojo> query = getQuery(select_all, DailySalesPojo.class);
        return query.getResultList();
    }

}
