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
    private static String select_id = "select productPojo from ProductPojo productPojo where productId=:id";
    private static String select_all = "select productPojo from ProductPojo productPojo";
    private static String select_barcode = "select productPojo from ProductPojo productPojo where productBarcode=:productBarcode";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(ProductPojo productPojo) {
        entityManager.persist(productPojo);
    }

    public ProductPojo select(int productId) {
        TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
        query.setParameter("id", productId);
        return getSingle(query);
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
        return query.getResultList();
    }

    public ProductPojo select(String productBarcode) {
        TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
        query.setParameter("productBarcode", productBarcode);
        return getSingle(query);
    }

    public void update(ProductPojo productPojo) {
    }
}
