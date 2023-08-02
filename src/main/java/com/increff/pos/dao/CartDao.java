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
    private static String SELECT_BY_ID = "select cartPojo from CartPojo cartPojo where id=:id";
    private static String SELECT_ALL = "select cartPojo from CartPojo cartPojo where userEmail=:email order by cartPojo.id desc";
    private static String DELETE_BY_ID = "delete from CartPojo cartPojo where id=:id";
    private static String DELETE_ALL = "delete from CartPojo cartPojo where userEmail=:email";
    private static String SELECT_BY_PRODUCT_ID = "select cartPojo from CartPojo cartPojo where productId=:productId " +
                                            "and userEmail=:email";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(CartPojo cartPojo) {
        entityManager.persist(cartPojo);
    }

    public CartPojo selectId(int itemNo) {
        TypedQuery<CartPojo> query = getQuery(SELECT_BY_ID, CartPojo.class);
        query.setParameter("id", itemNo);
        return getSingle(query);
    }

    public CartPojo selectProduct(int productId, String userEmail) {
        TypedQuery<CartPojo> query = getQuery(SELECT_BY_PRODUCT_ID, CartPojo.class);
        query.setParameter("productId", productId);
        query.setParameter("email",userEmail);
        return getSingle(query);
    }

    public List<CartPojo> selectAll(String userEmail) {
        TypedQuery<CartPojo> query = getQuery(SELECT_ALL, CartPojo.class);
        query.setParameter("email",userEmail);
        return query.getResultList();
    }
    @Transactional
    public void deleteId(int itemNo) {
        Query query = entityManager.createQuery(DELETE_BY_ID);
        query.setParameter("id", itemNo);
        query.executeUpdate();
    }

    public void deleteAll(String userEmail){
        Query query = entityManager.createQuery(DELETE_ALL);
        query.setParameter("email",userEmail);
        query.executeUpdate();
    }

}


