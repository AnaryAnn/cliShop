package application.model;

public enum Status {
    /**
     * Заказ создан
     */
    CREATED,

    /**
     * Заказ оплачен
     */
    PAID,

    /**
     * Заказ отклонен
     */
    REJECTED,

    /**
     * Оформлен возврат
     */
    REFUNDED
}
