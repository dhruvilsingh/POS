package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceData {
    private String number;
    private String date;
    private List<InvoiceItem> invoiceItems;
}
