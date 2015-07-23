package com.ese.model.view.report;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
//@Setter
public class UserAndRoleViewReport {

    private final String department,
                   faction,
                   title,
                   name,
                   loginName,
                   position,
                   createDate,
                   role;
//    private String userPrint;
//    private Date printDate;


    private UserAndRoleViewReport(String department, String faction, String title, String name, String loginName, String position, String createDate, String role) {
        this.department = department;
        this.faction = faction;
        this.title = title;
        this.name = name;
        this.loginName = loginName;
        this.position = position;
        this.createDate = createDate;
        this.role = role;
    }

    public static DepartmentBuilder builder() {
        return department ->
               faction ->
               title ->
               name ->
               loginName ->
               position ->
               createDate ->
               role ->
               new UserAndRoleViewReport(department, faction, title, name, loginName, position, createDate, role);
    }

    public interface DepartmentBuilder {
        FactionBuilder department(String department);
    }

    public interface FactionBuilder {
        TitleBuilder faction(String faction);
    }

    public interface TitleBuilder {
        NameBuilder title(String title);
    }

    public interface NameBuilder {
        LoginNameBuilder name(String name);
    }

    public interface LoginNameBuilder {
        PositionBuilder loginName(String loginName);
    }

    public interface PositionBuilder {
        CreateDateBuilder position(String position);
    }

    public interface CreateDateBuilder {
        RoleBuilder createDate(String createDate);
    }

    public interface RoleBuilder {
        UserAndRoleViewReport role(String role);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("department", department)
                .append("faction", faction)
                .append("title", title)
                .append("name", name)
                .append("loginName", loginName)
                .append("position", position)
                .append("createDate", createDate)
                .append("role", role)
                .toString();
    }
}
