package inte.project;
//author Dan Jensen

import java.util.*;

public class Register {


    private final Store store;

    private Report dailyReport = new Report();

    private final Scanner scanner = new Scanner(System.in);

    //saves purchases based on ID
    private HashMap<String, Purchase> dailyPurchases = new HashMap<>();

    //saves purchase collections based on Date
    private HashMap<String, HashMap<String, Purchase>> dailyReports = new HashMap<>();

    private HashMap<Integer, Cash> cashBalance = new HashMap<>();

    private HashMap<String, Product> inventory = new HashMap<>();

    private Money totalBalance = new Money(0);

    //slots for Cash
    private static final List<Cash> ACCEPTED_DENOMINATIONS = Arrays.asList(
            new Cash(new Money(1, 0), 0),
            new Cash(new Money(5, 0), 0),
            new Cash(new Money(10, 0), 0),
            new Cash(new Money(20, 0), 0),
            new Cash(new Money(50, 0), 0),
            new Cash(new Money(100, 0), 0),
            new Cash(new Money(200, 0), 0),
            new Cash(new Money(500, 0), 0),
            new Cash(new Money(1000, 0), 0));

    public Register(Store store, Product... products) {
        this.store = store;
        for (Cash cash: ACCEPTED_DENOMINATIONS)
            this.cashBalance.putIfAbsent(cash.getDenomination().getAmountOfCrown(), cash);
        for (Product p:products) {
            p.addStore(store);
            inventory.put(p.getId(), p);
        }
        }

        public Purchase scanProductsForPurchase(Product... products) {
        Purchase purchase = new Purchase();
        for (Product product: products) {;
                purchase.addProduct(inventory.get(product.getId()));
                inventory.remove(product.getId());
        }
        return purchase;
        }

        public void cancelPurchaseAfterScan(Purchase purchase) {
            for (Product product : purchase.getProductsToPurchase()) {
                inventory.put(product.getId(), product);
            }
        }

    public void makePurchase(Purchase purchase, Payment... payments) {
        purchase.paySeparatelyForProducts(payments);
        if (purchase.getCashFromPayment() != null) addCashPaymentToRegister(purchase);
        totalBalance = totalBalance.add(purchase.getCurrentPayment());
        addToDailyReports(purchase);
        printReceipt(purchase);
        }

    //gets the cash
    public void addCashPaymentToRegister(Purchase purchase) {
        for (Cash cash: purchase.getCashFromPayment()) {
            cashBalance.get(cash.getDenomination().getAmountOfCrown()).add(cash.getQuantity());
        }
    }

    //literally just a system out print of purchase.getInfo
    public void printReceipt(Purchase purchase){
        Receipt receipt = new Receipt(purchase, store);
        receipt.print();
    }

    //logs purchases
    public void addToDailyReports(Purchase purchase){
        dailyPurchases.put(purchase.getPurchaseId(), purchase);
        dailyReports.put(purchase.getDateOfPurchase(), dailyPurchases);
    }

     //add points kollar membership, returnerar void
    public double getPointsForPurchase(int costOfPurchase){
        if(costOfPurchase < 100){
            return costOfPurchase * 0.1;
        }
        else if(costOfPurchase < 1000){
            return costOfPurchase * 0.2;
        }
        else if(costOfPurchase < 10000){
            return costOfPurchase * 0.3;
        }else{
            return costOfPurchase * 0.4;
        }
    }


    public Collection<Cash> getCashBalance() {
        return Collections.unmodifiableCollection(cashBalance.values());
    }

    public Money getTotalBalance() {
        return totalBalance;
    }
}
