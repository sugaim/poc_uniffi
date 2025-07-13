package com.example;

import java.util.HashMap;

import uniffi.poc_ffi.BsModel;
import uniffi.poc_ffi.OptionType;
import uniffi.poc_ffi.Poc_ffiKt;

public class OptionsCalculator {
    
    static {
        // Ensure the UniFFI library is initialized
        Poc_ffiKt.uniffiEnsureInitialized();
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        try {
            System.out.println("=== Use map ===");
            HashMap<String, Integer> data = new HashMap<>();
            data.put("hoge", 42);
            data.put("fuga", 24);
            int sum = Poc_ffiKt.sumValues(data);
            System.out.println("Sum of values: " + sum);

            System.out.println("=== Black-Scholes Options Calculator ===");
            
            // Test parameters
            double spot = 100.0;      // Current stock price
            double strike = 105.0;    // Strike price
            double vol = 0.25;        // Volatility (25%)
            double rate = 0.05;       // Risk-free rate (5%)
            double time = 0.25;       // Time to expiration (3 months)
            
            // Calculate call option price using bs_call function
            System.out.println("\n--- Using bs_call function ---");
            double callPrice = Poc_ffiKt.bsCall(spot, strike, vol, rate, time);
            System.out.printf("Call option price: $%.4f%n", callPrice);
            
            // Calculate option premiums using bs_prem function with BsModel
            System.out.println("\n--- Using bs_prem function with BsModel ---");
            BsModel model = new BsModel(spot, vol);
            
            double callPremium = Poc_ffiKt.bsPrem(model, strike, rate, time, OptionType.CALL);
            double putPremium = Poc_ffiKt.bsPrem(model, strike, rate, time, OptionType.PUT);
            
            System.out.printf("Call premium: $%.4f%n", callPremium);
            System.out.printf("Put premium: $%.4f%n", putPremium);
            
            // Verify call-put parity: Call - Put = Spot - Strike * e^(-rate * time)
            double parity = callPremium - putPremium;
            double expectedParity = spot - strike * Math.exp(-rate * time);
            System.out.printf("%nCall-Put Parity Check:%n");
            System.out.printf("Call - Put = %.4f%n", parity);
            System.out.printf("Expected = %.4f%n", expectedParity);
            System.out.printf("Difference = %.6f%n", Math.abs(parity - expectedParity));
            
            // Test with different parameters
            System.out.println("\n--- Testing with different parameters ---");
            testDifferentScenarios();

            System.out.println("=== raise Exception ===");
            Poc_ffiKt.bsPrem(model, strike, rate, -1, OptionType.CALL);
            
        } catch (uniffi.poc_ffi.Exception e) {
            System.err.println("Error calculating options: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testDifferentScenarios() throws uniffi.poc_ffi.Exception {
        System.out.println("\nScenario 1: In-the-money call (spot > strike)");
        testScenario(110.0, 100.0, 0.20, 0.03, 0.5);
        
        System.out.println("\nScenario 2: Out-of-the-money call (spot < strike)");
        testScenario(90.0, 100.0, 0.30, 0.04, 0.25);
        
        System.out.println("\nScenario 3: At-the-money (spot = strike)");
        testScenario(100.0, 100.0, 0.15, 0.02, 1.0);
    }
    
    private static void testScenario(double spot, double strike, double vol, double rate, double time) throws uniffi.poc_ffi.Exception {
        BsModel model = new BsModel(spot, vol);
        
        double callPrice = Poc_ffiKt.bsPrem(model, strike, rate, time, OptionType.CALL);
        double putPrice = Poc_ffiKt.bsPrem(model, strike, rate, time, OptionType.PUT);
        
        System.out.printf("Spot: $%.2f, Strike: $%.2f, Vol: %.1f%%, Rate: %.1f%%, Time: %.2f years%n", 
                         spot, strike, vol * 100, rate * 100, time);
        System.out.printf("  Call: $%.4f, Put: $%.4f%n", callPrice, putPrice);
    }
}
