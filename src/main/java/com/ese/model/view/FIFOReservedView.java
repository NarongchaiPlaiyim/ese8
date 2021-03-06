package com.ese.model.view;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Setter
@Getter
public class FIFOReservedView {

    private int pickingOrderLineId;
    private String salesId;
    private String itemId;
    private int inventtransId;
    private int inventtransQty;
    private int pickingLineQty;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("pickingOrderLineId", pickingOrderLineId)
                .append("salesId", salesId)
                .append("itemId", itemId)
                .append("inventtransId", inventtransId)
                .append("inventtransQty", inventtransQty)
                .append("pickingLineQty", pickingLineQty)
                .toString();
    }
}
