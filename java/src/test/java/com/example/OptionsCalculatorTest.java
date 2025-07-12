package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import uniffi.poc_ffi.BsModel;
import uniffi.poc_ffi.OptionType;
import uniffi.poc_ffi.Poc_ffiKt;

public class OptionsCalculatorTest {
    
    private static final double EPSILON = 1e-6;
    
    static {
        // Ensure the UniFFI library is initialized
        Poc_ffiKt.uniffiEnsureInitialized();
    }
    
    @Test
    public void testBsCall() throws uniffi.poc_ffi.Exception {
        double spot = 100.0;
        double strike = 100.0;
        double vol = 0.20;
        double rate = 0.05;
        double time = 1.0;
        
        double result = Poc_ffiKt.bsCall(spot, strike, vol, rate, time);
        
        // At-the-money call with these parameters should be around 10.45
        assertTrue("Call option price should be positive", result > 0);
        assertTrue("Call option price should be reasonable", result > 5.0 && result < 20.0);
    }
    
    @Test
    public void testBsPrem() throws uniffi.poc_ffi.Exception {
        BsModel model = new BsModel(100.0, 0.20);
        double strike = 100.0;
        double rate = 0.05;
        double time = 1.0;
        
        double callPrice = Poc_ffiKt.bsPrem(model, strike, rate, time, OptionType.CALL);
        double putPrice = Poc_ffiKt.bsPrem(model, strike, rate, time, OptionType.PUT);
        
        assertTrue("Call price should be positive", callPrice > 0);
        assertTrue("Put price should be positive", putPrice > 0);
        
        // Call should be more expensive than put for these parameters (spot = strike, positive rate)
        assertTrue("Call should be more expensive than put", callPrice > putPrice);
    }
    
    @Test
    public void testCallPutParity() throws uniffi.poc_ffi.Exception {
        double spot = 100.0;
        double strike = 100.0;
        double vol = 0.20;
        double rate = 0.05;
        double time = 1.0;
        
        BsModel model = new BsModel(spot, vol);
        
        double callPrice = Poc_ffiKt.bsPrem(model, strike, rate, time, OptionType.CALL);
        double putPrice = Poc_ffiKt.bsPrem(model, strike, rate, time, OptionType.PUT);
        
        // Call-Put Parity: C - P = S - K * e^(-r*T)
        double leftSide = callPrice - putPrice;
        double rightSide = spot - strike * Math.exp(-rate * time);
        
        assertEquals("Call-Put parity should hold", rightSide, leftSide, EPSILON);
    }
    
    @Test
    public void testBsModelCreation() {
        BsModel model = new BsModel(100.0, 0.25);
        
        assertEquals("Spot should be set correctly", 100.0, model.getSpot(), EPSILON);
        assertEquals("Vol should be set correctly", 0.25, model.getVol(), EPSILON);
        
        // Test modification
        model.setSpot(110.0);
        model.setVol(0.30);
        
        assertEquals("Modified spot should be correct", 110.0, model.getSpot(), EPSILON);
        assertEquals("Modified vol should be correct", 0.30, model.getVol(), EPSILON);
    }
    
    @Test
    public void testOptionTypeEnum() {
        assertEquals("CALL enum should have correct ordinal", 0, OptionType.CALL.ordinal());
        assertEquals("PUT enum should have correct ordinal", 1, OptionType.PUT.ordinal());
        
        OptionType[] values = OptionType.values();
        assertEquals("Should have exactly 2 option types", 2, values.length);
        assertEquals("First value should be CALL", OptionType.CALL, values[0]);
        assertEquals("Second value should be PUT", OptionType.PUT, values[1]);
    }
}
