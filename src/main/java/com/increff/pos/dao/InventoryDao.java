package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao{
    private static String select_id = "select inventoryPojo from InventoryPojo inventoryPojo where productId=:id";
    private static String select_all = "select inventoryPojo from InventoryPojo inventoryPojo";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(InventoryPojo inventoryPojo) {
        entityManager.persist(inventoryPojo);
    }

    public InventoryPojo select(int productId) {
        TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
        query.setParameter("id", productId);
        return getSingle(query);
    }

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
        return query.getResultList();
    }

    public void update(InventoryPojo inventoryPojo) {
    }
}
