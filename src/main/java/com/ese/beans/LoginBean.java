package com.ese.beans;

import com.ese.model.db.StaffModel;
import com.ese.service.BarcodePrintingService;
import com.ese.service.LoginService;
import com.ese.service.security.SimpleAuthenticationManager;
import com.ese.service.security.UserDetail;
import com.ese.service.security.encryption.EncryptionService;
import com.ese.utils.AttributeName;
import com.ese.utils.FacesUtil;
import com.ese.utils.MessageDialog;
import com.ese.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ViewScoped
@ManagedBean(name = "loginBean")
public class LoginBean extends Bean{
    private static final long serialVersionUID = 4112578634029374840L;

    @ManagedProperty("#{loginService}") private LoginService loginService;
    @ManagedProperty("#{sessionRegistry}") private SessionRegistry sessionRegistry;
    @ManagedProperty("#{sas}") private CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy;
    @ManagedProperty("#{barcodePrintingService}") private BarcodePrintingService barcodePrintingService;

    private String userName = "";
    private String password = "";
    private UserDetail userDetail;
    private Set set;

    @PostConstruct
    private void init(){
        set = new HashSet<>();
        if(!Utils.isNull(SecurityContextHolder.getContext().getAuthentication())){
            userDetail = (UserDetail) SecurityContextHolder.getContext()
                                                           .getAuthentication()
                                                           .getPrincipal();
            set = (Set)FacesUtil.getSession()
                                .getAttribute(AttributeName.AUTHORIZE.getName());
        }
    }

    public String login(){
        log.info("-- SessionRegistry principle size: {}", sessionRegistry.getAllPrincipals()
                                                                         .size());
        if(!Utils.isZero(userName.length()) &&
           !Utils.isZero(password.length())) {

            setPassword(EncryptionService.encryption(password));
            if(loginService.isUserExist(getUserName(), getPassword())){
                StaffModel staffModel = loginService.getStaffModel();
                userDetail = new UserDetail(staffModel.getUsername(),
                                            staffModel.getPassword(),
                                            "USER",
                                            staffModel.getMsTitleModel()
                                                      .getName(),
                                            staffModel.getName());
                userDetail.setId(Utils.parseInt(staffModel.getId(), 0));
                HttpServletRequest httpServletRequest = FacesUtil.getRequest();
                HttpServletResponse httpServletResponse = FacesUtil.getResponse();
                UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(getUserDetail(), getPassword());
                request.setDetails(new WebAuthenticationDetails(httpServletRequest));
                SimpleAuthenticationManager simpleAuthenticationManager = new SimpleAuthenticationManager();
                Authentication result = simpleAuthenticationManager.authenticate(request);
                log.debug("-- authentication result: {}", result.toString());
                SecurityContextHolder.getContext().setAuthentication(result);
                compositeSessionAuthenticationStrategy.onAuthentication(request, httpServletRequest, httpServletResponse);
                HttpSession httpSession = FacesUtil.getSession();
                httpSession.setAttribute(AttributeName.USER_DETAIL.getName(), getUserDetail());
                httpSession.setAttribute(AttributeName.AUTHORIZE.getName(), loginService.getAuthorize());
                httpSession.setAttribute(AttributeName.STAFF.getName(), staffModel.getId());
                log.debug("-- userDetail[{}]", userDetail.toString());
                return "PASS";
            }
        }
        showDialog(MessageDialog.WARNING.getMessageHeader(), "Invalid username or password.");
        return "loggedOut";
    }

    public boolean isRendered(String key){
        return set.contains(key);
    }

    public void test(){
        System.out.println("test");
        barcodePrintingService.insert();
//        try {
////            loginService.getStaffModel()
//        } catch (InterruptedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }

}
