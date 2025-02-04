package com.aop.demo.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public int add(int a, int b) {
        System.out.println("Executing add() method...");
        return a + b;
    }

    public int subtract(int a, int b) {
        System.out.println("Executing subtract() method...");
        return a - b;
    }

    public int multiply(int a, int b) {
        System.out.println("Executing multiply() method...");
        return a * b;
    }

    public int divide(int a, int b) {
        System.out.println("Executing divide() method...");
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    }
}
