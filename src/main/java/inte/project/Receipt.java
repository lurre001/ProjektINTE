package inte.project;
//author Dan Jensen

//**STUB**//

public class Receipt {

    private final Order order;

    private final Store store;

    public Receipt(Order order, Store store){
        this.order = order;
        this.store = store;
    }

    public String print(){
        return "\n" + store.toString() + "\n" + order.getInfo().replaceAll("[\\[\\]]", "");
    }
}
