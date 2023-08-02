package com.increff.pos.dao;

import com.increff.pos.pojo.DailySalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Repository
public class ReportDao extends AbstractDao{

    @PersistenceContext
    private EntityManager entityManager;

    private static String SELECT_SALE_REPORT = "SELECT SUM(oi.quantity), SUM(oi.sellingPrice * oi.quantity), bp.brand, bp.category "+
            "FROM OrdersPojo op "+
            "INNER JOIN OrderItemPojo oi ON op.id = oi.orderId "+
            "INNER JOIN ProductPojo pp ON pp.id = oi.productId "+
            "INNER JOIN BrandPojo bp ON bp.id = pp.brandCategoryId "+
            "WHERE (:name is null or bp.brand=:name) "+
            "AND (:category is null or bp.category = :category) "+
            "AND DATE(op.time) between :startDate and :endDate "+
            "AND op.status = 'INVOICED' "+
            "GROUP BY bp.brand, bp.category";

    private static String SELECT_DAILY_SALE_REPORT = "select dailySalesPojo from DailySalesPojo dailySalesPojo order by " +
                                        "dailySalesPojo.id desc";

    private static String SELECT_SALE_BY_DATE = "select dailySalesPojo from DailySalesPojo dailySalesPojo where date=:date";

    private static String SELECT_INVENTORY_REPORT = "SELECT bp.brand, bp.category, COALESCE(SUM(ip.quantity),0)" +
            "FROM InventoryPojo ip " +
            "INNER JOIN ProductPojo pp ON ip.productId = pp.id " +
            "RIGHT JOIN BrandPojo bp ON pp.brandCategoryId = bp.id " +
            "GROUP BY bp.brand, bp.category";

    @Transactional
    public void insertDailySales(DailySalesPojo dailySalesPojo) {
        entityManager.persist(dailySalesPojo);
    }

    public List<DailySalesPojo> selectDailySalesReport() {
        TypedQuery<DailySalesPojo> query = getQuery(SELECT_DAILY_SALE_REPORT, DailySalesPojo.class);
        return query.getResultList();
    }

    public DailySalesPojo selectSaleByDate(ZonedDateTime date){
        TypedQuery<DailySalesPojo> query= getQuery(SELECT_SALE_BY_DATE, DailySalesPojo.class);
        query.setParameter("date", date);
        return query.getSingleResult();
    }

    public List<Object[]> selectSaleReport(String brand, String category, Date startDate , Date endDate) throws ParseException {
        TypedQuery<Object[]> query = getQuery(SELECT_SALE_REPORT, Object[].class);
        query.setParameter("name",brand);
        query.setParameter("category",category);
        query.setParameter("startDate",startDate);
        query.setParameter("endDate",endDate);
        return query.getResultList();
    }

    public List<Object[]> selectInventoryReport(){
        TypedQuery<Object[]> query = getQuery(SELECT_INVENTORY_REPORT, Object[].class);
        return query.getResultList();
    }

}
