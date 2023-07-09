package com.increff.pos.dao;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao{
    private static String select_id = "select inventoryPojo from InventoryPojo inventoryPojo where productId=:id";
    private static String select_all = "select inventoryPojo from InventoryPojo inventoryPojo order by inventoryPojo.productId desc";
    private static String select_report = "SELECT bp.brandName, bp.category, SUM(ip.productQuantity)" +
                                            "FROM InventoryPojo ip " +
                                            "INNER JOIN ProductPojo pp ON ip.productId = pp.productId " +
                                            "INNER JOIN BrandPojo bp ON pp.productBrandCategory = bp.brandId " +
                                            "GROUP BY bp.brandName, bp.category";

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

    public List<Object[]> selectReport(){
        TypedQuery<Object[]> query = getQuery(select_report, Object[].class);
        return query.getResultList();
    }

    public void update(InventoryPojo inventoryPojo) {
    }
}
