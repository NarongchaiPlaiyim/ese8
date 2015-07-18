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
@Table(name = "mst_status")
@Proxy(lazy=false)
public class StatusModel {
    @Id
    private int id;

    @OneToOne
    @JoinColumn(name="table_id")
    private TableModel tableId;

    @Column(name="status_seq")
    private int statusSeq;

    @Column(name="status_description")
    private String statusDes;

    @Column(name="caption")
    private String caption;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("tableId", tableId)
                .append("statusSeq", statusSeq)
                .append("statusDes", statusDes)
                .append("caption", caption)
                .toString();
    }
}
