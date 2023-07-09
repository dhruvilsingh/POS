package com.increff.pos.dao;

import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Repository
public class SaleReportDao extends AbstractDao{

    private static String select_report = "SELECT SUM(oi.productQuantity), SUM(oi.sellingPrice * oi.productQuantity), bp.brandName, bp.category "+
            "FROM OrderPojo op "+
            "INNER JOIN OrderItemPojo oi ON op.orderId = oi.orderId "+
            "INNER JOIN ProductPojo pp ON pp.productId = oi.productId "+
            "INNER JOIN BrandPojo bp ON bp.brandId = pp.productBrandCategory "+
            "WHERE (:name is null or bp.brandName=:name) "+
            "AND (:category is null or bp.category = :category) "+
            "AND DATE(op.orderTime) between :startDate and :endDate "+
            "GROUP BY bp.brandName, bp.category";


    public List<Object[]> selectReport(String brandName, String category, Date startDate , Date endDate) throws ParseException {
        TypedQuery<Object[]> query = getQuery(select_report, Object[].class);
        query.setParameter("name",brandName);
        query.setParameter("category",category);
        query.setParameter("startDate",startDate);
        query.setParameter("endDate",endDate);
        return query.getResultList();
    }
}
