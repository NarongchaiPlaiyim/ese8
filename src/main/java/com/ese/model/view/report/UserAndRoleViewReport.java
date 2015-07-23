package com.ese.model.view.report;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
//@Setter
public class UserAndRoleViewReport {

    private  String department;
    private  String faction;
    private  String title;
    private  String name;
    private  String loginName;
    private  String position;
    private  String createDate;
    private  String role;
//    private String userPrint;
//    private Date printDate;

    public static class Builder {
        private String department;
        private String faction;
        private String title;
        private String name;
        private String loginName;
        private String position;
        private String createDate;
        private String role;

        public Builder department(String department){
            this.department = department;
            return this;
        }

        public Builder faction(String faction){
            this.faction = faction;
            return this;
        }

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder loginName(String loginName){
            this.loginName = loginName;
            return this;
        }

        public Builder position(String position){
            this.position = position;
            return this;
        }

        public Builder createDate(String createDate){
            this.createDate = createDate;
            return this;
        }

        public Builder role(String role){
            this.role = role;
            return this;
        }

        public UserAndRoleViewReport build() {
            return new UserAndRoleViewReport(this);
        }

    }

    private UserAndRoleViewReport(Builder builder) {
        this.department = builder.department;
        this.faction = builder.faction;
        this.title = builder.title;
        this.name = builder.name;
        this.loginName = builder.loginName;
        this.position = builder.position;
        this.createDate = builder.createDate;
        this.role = builder.role;
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
//                .append("userPrint", userPrint)
//                .append("printDate", printDate)
                .toString();
    }
}
