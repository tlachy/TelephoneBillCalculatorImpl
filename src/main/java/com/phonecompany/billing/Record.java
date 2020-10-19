package com.phonecompany.billing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Record {

    private static final String TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    private final String phoneNumber;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private BigDecimal chargedPrice;

    public Record(String csvLine) {
        var values = csvLine.split(",");
        phoneNumber = values[0].trim();
        start = LocalDateTime.parse(values[1], ofPattern(TIME_FORMAT));
        end = LocalDateTime.parse(values[2], ofPattern(TIME_FORMAT));
        validate();
        chargedPrice = new BigDecimal(0);
    }

    private void validate() {
        //TODO just example of some validation, JAVAX validation could be used
        if(phoneNumber.length() != 12){
            throw new IllegalArgumentException("Phone number has to have 12 digits");
        }
        if(start.isBefore(end)){
            throw new IllegalArgumentException("Start date cannot be before end date");
        }
    }

    public BigDecimal getChargedPrice() {
        return chargedPrice;
    }

    public Record setChargedPrice(BigDecimal chargedPrice) {
        this.chargedPrice = chargedPrice;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(phoneNumber, record.phoneNumber) &&
                Objects.equals(start, record.start) &&
                Objects.equals(end, record.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, start, end);
    }

    @Override
    public String toString() {
        return "Record{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
