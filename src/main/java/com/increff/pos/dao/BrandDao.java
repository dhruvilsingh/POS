package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao{

    private static String select_id = "select brandPojo from BrandPojo brandPojo where brandId=:id";
    private static String select_all = "select brandPojo from BrandPojo brandPojo order by brandPojo.brandId desc";
    private static String check_category = "select brandPojo from BrandPojo brandPojo where brandName=:brandName and category=:category";
    private static String select_brands = "select distinct brandPojo.brandName from BrandPojo brandPojo";
    private static String select_cat = "select brandPojo.category from BrandPojo brandPojo where brandName=:brandName";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(BrandPojo brandPojo) {
        entityManager.persist(brandPojo);
    }

    public BrandPojo select(int brandId) {
        TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
        query.setParameter("id", brandId);
        return getSingle(query);
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
        return query.getResultList();
    }

    public void update(BrandPojo brandPojo) {
    }

    public int selectBrandCategory(String brandName, String category){
        TypedQuery<BrandPojo> query = getQuery(check_category,BrandPojo.class);
        query.setParameter("brandName",brandName );
        query.setParameter("category",category);
        BrandPojo brandPojo = getSingle(query);
        if(brandPojo == null){
            return -1;
        }
        return brandPojo.getBrandId();
    }
}