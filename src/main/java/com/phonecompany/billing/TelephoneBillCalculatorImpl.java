package com.phonecompany.billing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;


public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {


    public BigDecimal calculate(String phoneLog) {
        var phoneNumberToRecords = new HashMap<String, List<Record>>();
        var totalBill = new BigDecimal(0);
        var records = new ArrayList<Record>();

        try (var scanner = new Scanner(phoneLog)) {
            while (scanner.hasNextLine()) {
                var record = new Record(scanner.nextLine());
                charge(record);
                records.add(record);

                if(phoneNumberToRecords.containsKey(record.getPhoneNumber())){  //putIfAbsent
                    phoneNumberToRecords.get(record.getPhoneNumber()).add(record);
                } else {
                    phoneNumberToRecords.put(record.getPhoneNumber(), List.of(record));
                }
            }
        }

        discountForMostFrequentlyCalledNumber(phoneNumberToRecords, records);
        records.stream().forEach(record -> totalBill.add(record.getChargedPrice())); //this can be done somehow with sum group function
        return totalBill;
    }

    private void discountForMostFrequentlyCalledNumber(HashMap<String, List<Record>> phoneNumberToRecords, ArrayList<Record> records) {
        Optional<Entry<String, Long>> max = records.stream().collect(Collectors.groupingBy(Record::getPhoneNumber, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Entry::getValue));

        if(max.isPresent()){
            List<Record> records1 = phoneNumberToRecords.get(max.get());
            for (Record record : records1){
                record.setChargedPrice(new BigDecimal(0));
            }
        }
    }

    private void discountForLongCall(Record record) {
        long minutes = MINUTES.between(record.getStart(), record.getEnd());
        if(minutes > 5){
            BigDecimal newPrice = record.getChargedPrice().subtract(new BigDecimal((minutes - 5) * 0.2));
            record.setChargedPrice(newPrice);
        }
    }

    private void charge(Record record) {
        BigDecimal bill = new BigDecimal(0);
        long minutes = MINUTES.between(record.getStart(), record.getEnd());
        bill.add(new BigDecimal(minutes * 1));

        LocalDateTime peekStart = record.getStart().withHour(8).withMinute(0);
        LocalDateTime peekEnd = record.getEnd().withHour(16).withMinute(0);

        if(record.getStart().isBefore(peekStart)){
            long lowChargedMinutesBefore = MINUTES.between(record.getStart(), peekStart);
            bill.subtract(new BigDecimal(lowChargedMinutesBefore * 0.5));
        }

        if(record.getEnd().isAfter(peekEnd)){
            long lowChargedMinutesAfter = MINUTES.between(record.getEnd(), peekEnd);
            bill.subtract(new BigDecimal(lowChargedMinutesAfter * 0.5));
        }
        record.setChargedPrice(bill);
        discountForLongCall(record);
    }
}
