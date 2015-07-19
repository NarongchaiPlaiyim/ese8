package com.ese.beans;

import com.ese.service.IndexService;
import com.ese.service.security.UserDetail;
import com.ese.utils.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

import javax.faces.bean.ManagedProperty;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public abstract class Bean implements Serializable {
    @ManagedProperty("#{indexService}") protected IndexService indexService;
    @ManagedProperty("#{log}") protected Logger log;
    @ManagedProperty("#{moLogger}") protected Logger moLogger;
    @ManagedProperty("#{mtLogger}") protected Logger mtLogger;

    private String messageHeader;
    private String message;
    private UserDetail userDetail;

    protected void showDialogError(String message){
        showDialog(MessageDialog.ERROR.getMessageHeader(), message);
    }

    protected void showDialogWarning(String message){
        showDialog(MessageDialog.WARNING.getMessageHeader(), message);
    }

    protected void showDialogUpdated(){
        showDialog(MessageDialog.UPDATE.getMessageHeader(), MessageDialog.UPDATE.getMessage());
    }

    protected void showDialogSaved(){
        showDialog(MessageDialog.SAVE.getMessageHeader(), MessageDialog.SAVE.getMessage());
    }

    protected void showDialogEdited(){
        showDialog(MessageDialog.EDIT.getMessageHeader(), MessageDialog.EDIT.getMessage());
    }

    protected void showDialogCreated(){
        showDialog(MessageDialog.CREATE.getMessageHeader(), MessageDialog.CREATE.getMessage());
    }

    protected void showDialogDeleted(){
        showDialog(MessageDialog.DELETE.getMessageHeader(), MessageDialog.DELETE.getMessage());
    }

    protected void showDialog(String messageHeader, String message){
        setMessageHeader(messageHeader);
        setMessage(message);
        FacesUtil.showDialog(NamesUtil.DIALOG_NAME.getName());
    }

    protected void showDialog(String messageHeader, String message, String dialogName){
        setMessageHeader(messageHeader);
        setMessage(message);
        FacesUtil.showDialog(dialogName);
    }

    protected void showDialog(String dialogName){
        FacesUtil.showDialog(dialogName);
    }

    protected boolean preLoad(){
        boolean result = Boolean.FALSE;
        try{
            UserDetail userDetail = (UserDetail) FacesUtil.getSession()
                                                          .getAttribute(AttributeName.USER_DETAIL.getName());
            if(Utils.isNull(userDetail)){
                FacesUtil.redirect(NamesUtil.LOGIN_PAGE.getName());
            }
            setUserDetail(userDetail);
            result = !result;
        } catch (Exception e) {
            FacesUtil.redirect(NamesUtil.LOGIN_PAGE.getName());
        }
        return result;
    }

    protected boolean isAuthorize(String key){
        boolean result = Boolean.FALSE;
        try {
            Set set = (Set) FacesUtil.getSession()
                                     .getAttribute(AttributeName.AUTHORIZE.getName());
            if(!set.contains(key)){
                FacesUtil.redirect(NamesUtil.MAIN_PAGE.getName());
            }
            result = !result;
        } catch (Exception e) {
            FacesUtil.redirect(NamesUtil.LOGIN_PAGE.getName());
        }
        return result;
    }

    protected UserDetail getUser(){
        UserDetail userDetail = this.userDetail;
        if(Utils.isNull(userDetail)){
            userDetail = (UserDetail)FacesUtil.getSession()
                                              .getAttribute(AttributeName.USER_DETAIL.getName());
        }
        return userDetail;
    }
}
