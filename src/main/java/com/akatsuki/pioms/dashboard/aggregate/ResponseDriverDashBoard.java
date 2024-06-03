package com.akatsuki.pioms.dashboard.aggregate;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.order.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDriverDashBoard {
    private DeliveryDriver deliveryDriver;
    private FranchiseDTO franchise;
    private FranchiseOwnerDTO franchiseOwner;
    private List<OrderDTO> order;
    private List<InvoiceDTO> invoice;
}
