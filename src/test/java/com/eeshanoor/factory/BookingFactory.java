package com.eeshanoor.factory;

import com.eeshanoor.models.Booking;
import com.github.javafaker.Faker;

/**
 * Factory for generating Booking test data with Builder pattern.
 */
public class BookingFactory {
    private static final Faker faker = new Faker();

    public static Booking validBooking() {
        return new Builder().build();
    }

    public static Booking bookingWithGuest(String firstName, String lastName) {
        return new Builder().firstName(firstName).lastName(lastName).build();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String  firstName     = faker.name().firstName();
        private String  lastName      = faker.name().lastName();
        private int     totalPrice    = faker.number().numberBetween(50, 500);
        private boolean depositPaid   = faker.bool().bool();
        private String  checkIn       = "2025-06-01";
        private String  checkOut      = "2025-06-07";
        private String  additionalNeeds = faker.options().option("Breakfast", "Lunch", "Dinner", "None");

        public Builder firstName(String v)     { this.firstName = v; return this; }
        public Builder lastName(String v)      { this.lastName = v; return this; }
        public Builder totalPrice(int v)       { this.totalPrice = v; return this; }
        public Builder depositPaid(boolean v)  { this.depositPaid = v; return this; }
        public Builder checkIn(String v)       { this.checkIn = v; return this; }
        public Builder checkOut(String v)      { this.checkOut = v; return this; }
        public Builder needs(String v)         { this.additionalNeeds = v; return this; }

        public Booking build() {
            return new Booking(firstName, lastName, totalPrice, depositPaid,
                new Booking.BookingDates(checkIn, checkOut), additionalNeeds);
        }
    }
}