package com.phonecompany.billing;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class TelephoneBillCalculatorTest {

    TelephoneBillCalculator telephoneBillCalculator;
    String testLog = "420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57\n" +
            "420776562353,18-01-2020 08:59:20,18-01-2020 09:10:00";

    @BeforeEach
    void setUp() {
        telephoneBillCalculator = new TelephoneBillCalculatorImpl();
    }

    @org.junit.jupiter.api.Test
    void calculate() {
        telephoneBillCalculator.calculate(testLog);
    }
}