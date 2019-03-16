package models;

import java.math.BigDecimal;

public class OrderDetails {

    // Members
    private int orderNumber;
    private String productCode;
    private int quantityOrdered;
    private BigDecimal priceEach;
    private Short orderLineNumber;

    // Constructor
    public OrderDetails(int orderNumber, String productCode, int quantityOrdered, BigDecimal priceEach, Short orderLineNumber) {
        this.orderNumber = orderNumber;
        this.productCode = productCode;
        this.quantityOrdered = quantityOrdered;
        this.priceEach = priceEach;
        this.orderLineNumber = orderLineNumber;
    }

    // Getters/Setters

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(int quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public BigDecimal getPriceEach() {
        return priceEach;
    }

    public void setPriceEach(BigDecimal priceEach) {
        this.priceEach = priceEach;
    }

    public Short getOrderLineNumber() {
        return orderLineNumber;
    }

    public void setOrderLineNumber(Short orderLineNumber) {
        this.orderLineNumber = orderLineNumber;
    }


    // Display

    @Override
    public String toString() {
        return
                " Product Code='" + productCode + System.lineSeparator() +
                " Quantity Ordered=" + quantityOrdered + System.lineSeparator() +
                " Price Each=" + priceEach + System.lineSeparator() +
                " Order Line Number=" + orderLineNumber + System.lineSeparator()
                ;
    }
}
