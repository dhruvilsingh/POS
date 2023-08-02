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

    private static String SELECT_BY_ID = "select brandPojo from BrandPojo brandPojo where id=:brandId";
    private static String SELECT_ALL = "select brandPojo from BrandPojo brandPojo order by brandPojo.id desc";
    private static String SELECT_BY_BRAND_CATEGORY = "select brandPojo from BrandPojo brandPojo where brand=:brand and category=:category";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(BrandPojo brandPojo) {
        entityManager.persist(brandPojo);
    }

    public BrandPojo select(int brandId) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_ID, BrandPojo.class);
        query.setParameter("brandId", brandId);
        return getSingle(query);
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
        return query.getResultList();
    }

    public BrandPojo selectBrandCategory(String brand, String category){
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND_CATEGORY,BrandPojo.class);
        query.setParameter("brand",brand );
        query.setParameter("category",category);
        return getSingle(query);
    }
}