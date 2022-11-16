package ru.zulvit.entity;

public record Invoice(int ID, int overheadId, int productId, float price, int amount) {

    @Override
    public String toString() {
        return "Invoice[" +
                "ID=" + ID + ", " +
                "overheadId=" + overheadId + ", " +
                "productId=" + productId + ", " +
                "price=" + price + ", " +
                "amount=" + amount + ']';
    }
}
