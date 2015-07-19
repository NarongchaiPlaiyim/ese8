package com.ese.model.dao;

import java.io.Serializable;

public interface StaffDAOInf<T, ID extends Serializable> extends GenericDAOInf<T, ID>{
    T findByUserNameAndPassword(String userName, String password) throws Exception;
    boolean isUsernameExist(String userName) throws Exception;
}
