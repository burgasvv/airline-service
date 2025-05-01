package org.burgas.ticketservice.message;

public enum OrderedTicketMessages {

    ORDERED_TICKET_CANCELLED("Заказ на пассажирский билет был отменен"),
    ORDERED_TICKET_NOT_FOUND("Заказ на пассажирский билет не найден");

    private final String message;

    OrderedTicketMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
