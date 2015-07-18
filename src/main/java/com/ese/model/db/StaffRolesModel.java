package com.ese.model.db;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "staff_roles")
@Proxy(lazy=false)
public class StaffRolesModel extends AbstractModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name="staff", nullable=false, columnDefinition="int default 0")
    private StaffModel staff;

    @OneToOne
    @JoinColumn(name="roles", nullable=false, columnDefinition="int default 0")
    private SystemRoleModel roles;

    @Column(name="isvalid")
    private int isValid;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("staff", staff)
                .append("roles", roles)
                .toString();
    }
}
