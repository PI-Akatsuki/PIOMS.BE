package com.akatsuki.pioms.invoice.aggregate;

import com.akatsuki.pioms.exchange.aggregate.ResponseExchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ResponseInvoiceDetail {

    private InvoiceDTO invoice;
    private List<ResponseProduct> products;


    // 본사에서 가맹으로 돌려보낼 반품들입니다. 가맹에서 본사로 돌려보낼 반품상품정보는 invoiceDTO -> order -> invoice에 있습니다.
    private List<ResponseExchange> exchanges;

    public ResponseInvoiceDetail(Invoice invoice, Order order, Franchise franchise, List<Product> product, List<ExchangeDTO> exchangeDTOS) {
        this.invoice = new InvoiceDTO(invoice);
//        this.order = order;
//        this.franchise = franchise;

        this.products = product.stream()
                .map(ResponseProduct::new)
                .collect(Collectors.toList());
        this.exchanges = exchangeDTOS.stream().map(ResponseExchange::new).collect(Collectors.toList());
    }
}
