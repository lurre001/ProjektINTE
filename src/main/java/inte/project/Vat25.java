package inte.project;
//author Marah Zeibak
public interface Vat25 extends Vat{

    default double getVat() {
        return 0.25;
    }


}
