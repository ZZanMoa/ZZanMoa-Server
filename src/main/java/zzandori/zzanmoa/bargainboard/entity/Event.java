package zzandori.zzanmoa.bargainboard.entity;

public enum Event {
    DISCOUNT_SALE(1, "할인행사"),
    DIRECT_TRADE(2, "직거래 마켓");

    private final int id;
    private final String description;

    Event(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static Event getById(int id) {
        for (Event event : values()) {
            if (event.getId() == id) {
                return event;
            }
        }
        throw new IllegalArgumentException("No matching event for id: " + id);
    }
}

