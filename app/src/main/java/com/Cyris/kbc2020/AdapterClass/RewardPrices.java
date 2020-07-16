package com.Cyris.kbc2020.AdapterClass;

import java.util.ArrayList;

public class RewardPrices {
    public static ArrayList<String> price = new ArrayList<>();
    public static ArrayList<String> priceinfo = new ArrayList<>();

    public static ArrayList<String> getPrice()
    {
        price.clear();
        price.add("14.   ₹7 Crores");
        price.add("13.   ₹3 Crores");
        price.add("12.   ₹1 Crore");
        price.add("11.   ₹50,00,000");
        price.add("10.   ₹25,00,000");
        price.add("9.    ₹12,50,000");
        price.add("8.    ₹6,40,000");
        price.add("7.    ₹3,20,000");
        price.add("6.    ₹1,60,000");
        price.add("5.    ₹80,000");
        price.add("4.    ₹40,000");
        price.add("3.    ₹20,000");
        price.add("2.    ₹10,000");
        price.add("1.    ₹5000");


        return price;
    }

    public static ArrayList<String> getPriceInfo()
    {
        priceinfo.clear();
        priceinfo.add("₹7 Crores");
        priceinfo.add("₹3 Crores");
        priceinfo.add("₹1 Crore");
        priceinfo.add("₹50,00,000");
        priceinfo.add("₹25,00,000");
        priceinfo.add("₹12,50,000");
        priceinfo.add("₹6,40,000");
        priceinfo.add("₹3,20,000");
        priceinfo.add("₹1,60,000");
        priceinfo.add("₹80,000");
        priceinfo.add("₹40,000");
        priceinfo.add("₹20,000");
        priceinfo.add("₹10,000");
        priceinfo.add("₹5000");


        return priceinfo;
    }
}
