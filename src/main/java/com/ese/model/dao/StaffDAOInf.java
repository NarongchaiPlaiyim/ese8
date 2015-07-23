package com.ese.model.dao;

import com.ese.model.db.StaffModel;
import com.ese.model.view.report.UserAndRoleViewReport;

import java.util.List;

public interface StaffDAOInf extends GenericDAOInf<StaffModel, Integer>{
    StaffModel findByUserNameAndPassword(String userName, String password) throws Exception;
    boolean isUsernameExist(String userName) throws Exception;
    List<StaffModel> findUserByIsValid();
    List<StaffModel> findUserBySearch(int departmentId, int factionId, String keySearch);
    void deleteByUpdate(StaffModel entity) throws Exception;
    StaffModel findByUserName(String userName) throws Exception;
    List<UserAndRoleViewReport> genSQLReportUserAndRole();
}
