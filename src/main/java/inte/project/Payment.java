package inte.project;
//author Dan Jensen

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Payment {

    private Money amountPaid = new Money(0);
    private Money paymentAmount;
    private String paymentType;
    private Customer customer;

    private Card cardPayment;
    private HashMap<Integer, Cash> cashPayment = new HashMap<>();
    private MembershipPoints pointPayment;


    public Payment(Money paymentAmount) {
        this.paymentAmount = paymentAmount;
        this.paymentType = "Insufficient amount";
    }


    //cash payment collects bills to give to Register later
    public Payment(Money paymentAmount, Cash... cash) {
        this.paymentAmount = paymentAmount;
        for (Cash money : cash) {
            this.collectCashPayments(money);
            amountPaid = amountPaid.add(money.getTotal());
        }
        this.paymentType = "Cash";
    }

    //Card is the type so that card information is saved later, gets overriden by cardType anyways in Purchase later
    public Payment(Money paymentAmount, Card card) {
        this.paymentAmount = paymentAmount;
        validateCard(card);
    }

    public void validateCard(Card card){
        this.cardPayment = card;
        if (card.getBalance().getAmountInOre() < paymentAmount.getAmountInOre()) this.paymentType = "Insufficient amount";
        if (card.getBalance().getAmountInOre() >= paymentAmount.getAmountInOre()){
            amountPaid = amountPaid.add(card.pay(paymentAmount));
            customer = card.getCardOwner();
            this.paymentType = "Card";
        }
    }

    //points payment
    public Payment(Money paymentAmount, Customer customer) {
        this.paymentAmount = paymentAmount;
        this.customer = customer;
        if (!customer.isMember()) throw new IllegalArgumentException("Customer is not member and can't pay with points.");
        validatePoints();
    }

    public void validatePoints() {
        this.pointPayment = customer.getMembership().getMembershipPoints();
        if (pointPayment.getAllPoints() < paymentAmount.getAmountInOre()) this.paymentType = "Insufficient amount";
        if (pointPayment.getAllPoints() >= paymentAmount.getAmountInOre()) {
            amountPaid = amountPaid.add(new Money(pointPayment.getCertainAmountOfPoints(paymentAmount.getAmountInOre())));
            this.paymentType = "Points";
        }
    }


    //gets the bills together
    public void collectCashPayments(Cash money) {
        if (!this.cashPayment.containsKey(money.getDenomination().getAmountOfCrown()))
        this.cashPayment.put(money.getDenomination().getAmountOfCrown(), money);
        else this.cashPayment.get(money.getDenomination().getAmountOfCrown()).add(money.getQuantity());
    }

    public void addCashToEmptyPayment(Cash... cash) {
        for (Cash money : cash) {
            this.collectCashPayments(money);
            amountPaid = amountPaid.add(money.getTotal());
        }
        this.paymentType = "Cash";
    }

    //Cash is different because it needs to be able to give change in Register
    public Money getPayment() {
        return amountPaid;
    }

    public Collection<Cash> getCashPaymentValues() {
        return Collections.unmodifiableCollection(this.cashPayment.values());
    }

    public String getPaymentType() {
        return paymentType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Card getCardPayment() {
        return cardPayment;
    }

    public MembershipPoints getPointPayment() {
        return pointPayment;
    }

    @Override
    public String toString() {
        if (this.paymentType.equals("Card") && cardPayment.getCardType().equals("Debitcard"))
            return getCardPayment().toString() + "\nAmount paid: " + amountPaid;
        if (this.paymentType.equals("Card") && cardPayment.getCardType().equals("Giftcard"))
            return getCardPayment().toString() + "\nAmount paid: " + amountPaid + "\nRemaining balance: " + cardPayment.getBalance();
        if (this.paymentType.equals("Points"))
            return pointPayment.toString() + "\nAmount paid: " + amountPaid;
        if (this.paymentType.equals("Cash"))
            return "Cash:" + "\nAmount paid: " + amountPaid;
        else return "Insufficient amount:" + "\nAmount paid: " + amountPaid;
    }
}
