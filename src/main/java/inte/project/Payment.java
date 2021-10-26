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
    }


    //cash payment collects bills to give to Register later
    public Payment(Money paymentAmount, Cash... cash) {
        this.paymentAmount = paymentAmount;
        for (Cash money : cash) {
            amountPaid = amountPaid.add(this.collectCashPayments(money));
        }
        this.paymentType = "Cash";
    }

    //Card is the type so that card information is saved later, gets overriden by cardType anyways in Purchase later
    public Payment(Money paymentAmount, Card card) {
        this.paymentAmount = paymentAmount;
        this.cardPayment = card;
        card.pay(paymentAmount);
        customer = card.getCardOwner();
        this.paymentType = "Card";
    }
    //points payment
    public Payment(Money paymentAmount, Customer customer) {
        this.paymentAmount = paymentAmount;
        this.customer = customer;
        if (!customer.isMember()) throw new IllegalArgumentException("Customer is not member and can't pay with points.");
        this.pointPayment = customer.getMembership().getMembershipPoints();
        amountPaid = amountPaid.add(new Money(pointPayment.getCertainAmountOfPoints(paymentAmount.getAmountInOre())));
        this.paymentType = "Points";
    }
    //gets the bills together
    public Money collectCashPayments(Cash money) {
        if (!this.cashPayment.containsKey(money.getDenomination().getAmountOfCrown()))
        this.cashPayment.put(money.getDenomination().getAmountOfCrown(), money);
        else this.cashPayment.get(money.getDenomination().getAmountOfCrown()).add(money.getQuantity());
        return money.getTotal();
    }

    //Cah is different because it needs to be able to give change in Register
    public Money getPayment() {
        if (this.paymentType.equals("Cash")) return amountPaid;
        return paymentAmount;
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

    @Override
    public String toString() {
        if (this.paymentType.equals("Card") && cardPayment.getCardType().equals("Debitcard"))
            return getCardPayment().toString() + "\nAmount paid: " + paymentAmount;
        if (this.paymentType.equals("Card") && cardPayment.getCardType().equals("Giftcard"))
            return getCardPayment().toString() + "\nAmount paid: " + paymentAmount + "\nRemaining balance: " + cardPayment.getBalanceAmount();
        if (this.paymentType.equals("Points"))
            return pointPayment.toString() + "\nAmount paid: " + amountPaid;
        return "Cash:" + "\nAmount paid: " + amountPaid;
    }
}
