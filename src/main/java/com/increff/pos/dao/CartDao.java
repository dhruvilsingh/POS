package com.increff.pos.dao;

import com.increff.pos.pojo.CartPojo;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CartDao extends  AbstractDao{
    private static String select_id = "select cartPojo from CartPojo cartPojo where itemNo=:id";
    private static String select_all = "select cartPojo from CartPojo cartPojo where userEmail=:email";
    private static String delete_id = "delete from CartPojo cartPojo where itemNo=:id";
    private static String select_barcode = "select cartPojo from CartPojo cartPojo where productBarcode=:barcode and userEmail=:email";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(CartPojo cartPojo) {
        entityManager.persist(cartPojo);
    }

    public CartPojo select(int itemNo) {
        TypedQuery<CartPojo> query = getQuery(select_id, CartPojo.class);
        query.setParameter("id", itemNo);
        return getSingle(query);
    }

    public CartPojo select(String barcode, String userEmail) {
        TypedQuery<CartPojo> query = getQuery(select_barcode, CartPojo.class);
        query.setParameter("barcode", barcode);
        query.setParameter("email",userEmail);
        return getSingle(query);
    }

    public List<CartPojo> selectAll(String userEmail) {
        TypedQuery<CartPojo> query = getQuery(select_all, CartPojo.class);
        query.setParameter("email",userEmail);
        return query.getResultList();
    }

    public int deleteId(int itemNo) {
        Query query = entityManager.createQuery(delete_id);
        query.setParameter("id", itemNo);
        return query.executeUpdate();
    }

    public void update(CartPojo cartPojo) {
    }
}
