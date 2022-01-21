package models;


import java.util.List;

public class Purchase {
    private final Product product;
    private int count;

    public Purchase(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    /**
     * parses purchase summary information from a textLine with format: barcode, amount
     *
     * @param textLine
     * @param products a list of products ordered and searchable by barcode
     *                 (i.e. the comparator of the ordered list shall consider only the barcode when comparing products)
     * @return a new Purchase instance with the provided information
     * or null if the textLine is corrupt or incomplete
     */
    public static Purchase fromLine(String textLine, List<Product> products) throws RuntimeException {

        //eerst lengte barcode kijken
        // cast een string naar long
        int lengthBarcode = textLine.indexOf(",");
        String stringBarcode = textLine.substring(0, lengthBarcode);
        long barcode = Long.parseLong(stringBarcode);

        // eerst maak ik een variable met de lengte van textLine
        // ik doe vervolgens maak ik een String varibale met de aantalen
        //vervolgens parse ik deze naar een int
        int lengthString = textLine.length();

        String numberString = textLine.substring(lengthBarcode + 2, lengthString);
        int number = Integer.parseInt(numberString);

        //als eerst maak ik een product aan met alleen de barcode omdat een indexOf alleen de
        //de classe Product accepteert.
        //vervolgens gebruik ik de index om een product te zoeken op de locatie van foundindex
        Product productBarcode = new Product(barcode,null,0.00);
        int foundIndex = products.indexOf(productBarcode);
        if (foundIndex == -1){
            return null;
        }

        Product product = products.get(foundIndex);

        Purchase newPurchase = new Purchase(product,number);

        return newPurchase;
    }

    /**
     * add a delta amount to the count of the purchase summary instance
     *
     * @param delta
     */
    public void addCount(int delta) {
        this.count += delta;
    }

    public long getBarcode() {
        return this.product.getBarcode();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }



    @Override
    public String toString() {
        //de toString aangepast met een printf zodat de double 2 decimale mee geeft
        double price = getProduct().getPrice();

        return String.format("%d/%s/%d/%.2f",getBarcode(),getProduct().getTitle(),getCount(),price);
    }
}
