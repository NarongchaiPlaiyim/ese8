package com.ese.model.dao;

import com.ese.model.view.report.UserAndRoleViewReport;

import java.io.Serializable;
import java.util.List;

public interface StaffDAOInf<T, ID extends Serializable> extends GenericDAOInf<T, ID>{
    T findByUserNameAndPassword(String userName, String password) throws Exception;
    boolean isUsernameExist(String userName) throws Exception;
    List<T> findUserByIsValid();
    List<T> findUserBySearch(int departmentId, int factionId, String keySearch);
    void deleteByUpdate(T entity) throws Exception;
    T findByUserName(String userName) throws Exception;
    List<UserAndRoleViewReport> genSQLReportUserAndRole();
}
