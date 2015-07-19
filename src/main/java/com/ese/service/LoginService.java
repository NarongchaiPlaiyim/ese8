package com.ese.service;

import com.ese.model.dao.MenuObjectDAO;
import com.ese.model.dao.StaffDAO;
import com.ese.model.db.MenuObjectModel;
import com.ese.model.db.StaffModel;
import com.ese.utils.Utils;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
public class LoginService extends Service{
    private static final long serialVersionUID = 4112578634088874840L;
    @Resource private StaffDAO staffDAO;
    @Resource private MenuObjectDAO menuObjectDAO;

    @Getter StaffModel staffModel;

    public boolean isUserExist(final String userName, final String password){
        log.debug("-- isUserExist({}, {})", userName, password);
        boolean result = Boolean.FALSE;
        try {
            staffModel = staffDAO.findByUserNameAndPassword(userName, password);
            if(!Utils.isNull(staffModel)){
                result = !result;
            }
        } catch (Exception e) {
            log.error("Exception while calling isUserExist()", e);
        }
        return result;
    }

//    public Map<String, String> getAuthorize(){
//        List<String> stringList;
//        Map<String, String> map = new HashMap();
//        try {
//            stringList = menuObjectDAO.findByStaffId(staffModel.getId());
//            for (String s : stringList) map.put(s, s);
//        } catch (Exception e) {
//            log.error("Exception while calling getAuthorize()", e);
//        }
//        return map;
//    }

    public Set<String> getAuthorize(){
        Set set = new HashSet<>();
        try {
            set = menuObjectDAO.findByStaffId(staffModel.getId()).stream()
                                                                 .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("Exception while calling getAuthorize()", e);
        }
        return set;
    }


    public List<MenuObjectModel> getAllMenuObject(){
        try {
            return menuObjectDAO.findAllOrderByCode();
        } catch (Exception e) {
            System.err.println(e);
            return Collections.EMPTY_LIST;
        }
    }

    public void test(String startBarcode, String finishBarcode){
        try {

            System.out.println("test");
//            System.out.println(warehouseDAO.findByStatus2().toString()+"");
//            System.out.println(locationItemsDAO.findLocationByItemId(58));
//            System.out.println(locationDAO.getLocationModelList());
//            barcodeRegisterDAO.getDataTable();
//            List<BarcodeRegisterModel> barcodeRegisterModelList = barcodeRegisterDAO.findByIsValid();
//            System.out.println(barcodeRegisterModelList.toString());
//            System.out.println(barcodeRegisterModelList.size());
//            System.out.println("Price is "+barcodeRegisterDAO.getPrice("I-0000100"));

//            System.out.println(barcodeRegisterDAO.checkBarcode2(startBarcode, finishBarcode));
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
