package com.company;

public class Main {
    public static void main(String[] args) {
        RunnableImpl1 run=new RunnableImpl1();
        new Thread(run).start();
        new Thread(run).start();
        new Thread(run).start();
    }
}
