package models;

import java.awt.*;

public class Product {
    private final long barcode;
    private String title;
    private double price;

    public Product(long barcode) {
        this.barcode = barcode;
    }

    public Product(long barcode, String title, double price) {
        this(barcode);
        this.title = title;
        this.price = price;
    }

    /**
     * parses product information from a textLine with format: barcode, title, price
     * @param textLine
     * @return  a new Product instance with the provided information
     *          or null if the textLine is corrupt or incomplete
     */
    public static Product fromLine(String textLine) {
        //barcode is altijd 15 lang
        // cast een string naar long
        String stringBarcode = textLine.substring(0,15);
        long barcode = Long.parseLong(stringBarcode);

        // hier zoek ik naar een , na index 17
        // als deze gevonden is wordt deze opgeslagen in title
        int endCharTitle = textLine.indexOf(",",17);
        String title = textLine.substring(17,endCharTitle);

        //als eerst haal ik de comma weg. vervolgens kijk ik totale lengte.
        //ik check of er nog een comma dit in het laatste gedeelte.
        //ik parse sting to double
        int priceStartingPoint = endCharTitle+1;
        String priceString = textLine.substring(priceStartingPoint,textLine.length());

        if (priceString.contains(",")){
            int indexComma = priceString.indexOf(",");
            String pricesStringChecked = priceString.substring(0,indexComma);
            System.out.println(pricesStringChecked);
            priceString= pricesStringChecked;
        }
            double price = Double.parseDouble(priceString);
            Product newProduct  = new Product(barcode,title,price);
        return newProduct;
    }

    public long getBarcode() {
        return barcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Product)) return false;
        return this.getBarcode() == ((Product)other).getBarcode();
    }

    @Override
    public String toString() {
        return barcode +"/"+
                 title + "/" +
                price;
    }


    // TODO add public and private methods as per your requirements
}
