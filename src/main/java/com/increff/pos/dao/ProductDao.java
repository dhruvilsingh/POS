package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao{
    private static String SELECT_BY_ID = "select productPojo from ProductPojo productPojo where id=:id";
    private static String SELECT_ALL = "select productPojo from ProductPojo productPojo order by productPojo.id desc";
    private static String SELECT_BY_BARCODE = "select productPojo from ProductPojo productPojo where barcode=:productBarcode";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(ProductPojo productPojo) {
        entityManager.persist(productPojo);
    }

    public ProductPojo select(int productId) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_BY_ID, ProductPojo.class);
        query.setParameter("id", productId);
        return getSingle(query);
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
        return query.getResultList();
    }

    public ProductPojo select(String productBarcode) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_BY_BARCODE, ProductPojo.class);
        query.setParameter("productBarcode", productBarcode);
        return getSingle(query);
    }

}
