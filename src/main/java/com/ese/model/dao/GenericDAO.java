package com.ese.model.dao;

import com.ese.utils.Utils;
import lombok.Getter;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAO<T, ID extends Serializable> implements GenericDAOInf<T, ID>, Serializable{
    @Resource private SessionFactory sessionFactory;
    @Resource protected Logger log;
    @Resource protected Logger moLogger;
    @Resource protected Logger mtLogger;
    @Value("#{config['schema']}")private String schema;
    @Value("#{config['catalog']}")private String catalog;
    @Getter protected String prefix;
    
    private Class<T> entityClass;

    @PostConstruct
    private void init() {
        prefix = catalog+"."+schema;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private Class<T> getEntityClass() {
        return entityClass;
    }

    protected Session getSession() throws Exception {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public T findByID(ID id) throws Exception {
        return (T) getSession().load(getEntityClass(), id);
    }

    @Override
    public List<T> findAll() throws Exception {
        return findByCriteria();
    }

    @Override
    public List<T> findByCriteria(Criterion... criterion) throws Exception {
        Criteria criteria = getCriteria();
        for (Criterion c : criterion) {
            criteria.add(c);
        }
        return Utils.safetyList(criteria.list());
    }

    @Override
    public T findOneByCriteria(Criterion... criterion) throws Exception {
        Criteria criteria = getCriteria();
        for (Criterion c : criterion) {
            criteria.add(c);
        }
        return (T) criteria.uniqueResult();
    }

    @Override
    public void persist(T entity) throws Exception {
        getSession().persist(entity);
    }

    @Override
    public void persist(List<T> entityList) throws Exception {
        entityList.forEach(entity -> {
                    sessionFactory
                            .getCurrentSession()
                            .persist(entity);
                });
    }

    @Override
    public T update(T entity) throws Exception {
        getSession().update(entity);
        return entity;
    }

    @Override
    public List<T>  update(List<T> entityList) throws Exception {
        entityList.forEach(entity -> {
                    sessionFactory
                            .getCurrentSession()
                            .update(entity);
                });
        return entityList;
    }

    @Override
    public T saveOrUpdate(T entity) throws Exception {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public List<T> saveOrUpdate(List<T> entityList) throws Exception {
        entityList.forEach(entity -> {
                    sessionFactory
                            .getCurrentSession()
                            .saveOrUpdate(entity);
                });
        return entityList;
    }

    @Override
    public void delete(T entity) throws Exception {
        getSession().delete(entity);
    }

    @Override
    public void delete(List<T> entityList) throws Exception {
        entityList.forEach(entity -> {
                    sessionFactory
                            .getCurrentSession()
                            .delete(entity);
                });
    }

    @Override
    public Criteria getCriteria() throws Exception {
        return getSession().createCriteria(getEntityClass());
    }

    @Override
    public boolean isRecordExist(Criterion... criterion) throws Exception {
        return findByCriteria(criterion).stream().count() > 0;
    }

    protected void flush() throws Exception {
        getSession().flush();
    }

    protected List<T> findBySQL(String sql, Object... params) throws Exception {
        Query query = getSession().createSQLQuery(sql);
        for(int i=0; i<params.length; i++) {
            query.setParameter(i, params[i]);
        }
        return Utils.safetyList(query.list());
    }
    protected List<T> findBySQL(String sql) throws Exception {
        return Utils.safetyList(getSession().createSQLQuery(sql)
                                            .list());
    }
}
