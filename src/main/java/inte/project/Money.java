package inte.project;
//author Karolina Klimek kakl1405

import java.util.Objects;

// ser till att all hantering av pengar hanteras konsekvent i hela systemet.
// Hanterar endast SEK
public class Money implements Comparable<Money> {

    private static final int ORE_PER_CROWN = 100;
    private int amountInOre;

    public Money(int amountInOre){
        this.amountInOre = amountInOre;
    }

    public Money(int crown, int ore){
        this.amountInOre = crown*ORE_PER_CROWN + ore;
    }
    public int getAmountOfCrown(){
        return amountInOre /ORE_PER_CROWN;
    }
    private int getAmountOfOre(){
        return amountInOre %ORE_PER_CROWN;
    }

    public Money add(int ore){
        return (new Money(amountInOre + ore));
    }
    //Tar ett Money object och anropar första add metoden med dess värde.
    public Money add(Money m){
        return add(m.amountInOre);
    }
    public Money subtract(int ore){
        return new Money (amountInOre - ore);
    }
    //tar ett money object och anropar första subtract metoden med dess värde.
    public Money subtract(Money m){
        return subtract(m.amountInOre);
    }

    public int getAmountInOre(){
        return amountInOre;
    }

    public int compareTo(Money other){
        return amountInOre - other.amountInOre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amountInOre == money.amountInOre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountInOre);
    }

    public String toString(){
        return String.format("%s:%02d kr", getAmountOfCrown(), getAmountOfOre());
    }
}
