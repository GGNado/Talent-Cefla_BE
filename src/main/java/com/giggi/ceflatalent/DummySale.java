package com.giggi.ceflatalent;

public record DummySale(
    String idCompany,
    String idOrderNum,
    String idsCustomer,
    String idsItem,
    String idOrderDate,
    String idInvoiceDate,
    String valRevenues,
    String valCost
) {
}