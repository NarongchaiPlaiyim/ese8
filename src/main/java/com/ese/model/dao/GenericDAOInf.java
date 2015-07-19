package com.ese.model.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import java.io.Serializable;
import java.util.List;

public interface GenericDAOInf<T, ID extends Serializable> {
    T findByID(ID id) throws Exception;
    List<T> findAll() throws Exception;
    List<T> findByCriteria(Criterion... criterion) throws Exception;
    T findOneByCriteria(Criterion... criterion) throws Exception;

    void persist(T entity) throws Exception;
    void persist(List<T> entityList) throws Exception;

    void delete(T entity) throws Exception;
    void delete(List<T> entityList) throws Exception;

    T update(T entity) throws Exception;
    List<T>  update(List<T> entityList) throws Exception;

    T saveOrUpdate(T entity) throws Exception;
    List<T> saveOrUpdate(List<T> entityList) throws Exception;

    Criteria getCriteria() throws Exception;


    boolean isRecordExist(Criterion... criterion) throws Exception;
}
