package inte.project;
// Klass som innehåller datastrukturen med alla customers och kan hantera dessa.
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class CustomerHandler {
    private HashSet<Customer> customerHashSet;
    private HashMap<String, Customer> customerHashMapName;
    private HashMap<Integer, Customer> customerHashMapMembershipID;
    private HashMap<String, Customer> customerHashMapAdress;
    private HashMap<String, Customer> customerHashMapPhoneNumber;


    // Initierar klass
    CustomerHandler(){
        customerHashSet = new HashSet<>();
        customerHashMapMembershipID = new HashMap<>();
        customerHashMapName = new HashMap<>();
        customerHashMapAdress = new HashMap<>();
        customerHashMapPhoneNumber = new HashMap<>();
    }
    // Adderar privateperson so that they may be found by name, adress, memberID and phone if the person doesn't already exist in the database
    // if not member not added to membershipID hashmap
    public void addPrivatePerson(String name, String address, String email, String phoneNumber, int birthYear) throws IllegalArgumentException{
        Customer customer = new PrivatePerson(name, address, email, phoneNumber, birthYear);
        if(!customerHashSet.contains(customer)){
            customerHashSet.add(customer);
            customerHashMapName.put(customer.getName(), customer);
            customerHashMapAdress.put(customer.getAddress(), customer);
            if(customer.isMember()){
                customerHashMapMembershipID.put(customer.getMembership().getMemberID(), customer);
            }
            customerHashMapPhoneNumber.put(customer.getPhoneNumber(), customer);
        }else{
            throw new IllegalArgumentException("Customer already exists");
        }

    }
    public Collection<Customer> getAllCustomers(){

        return Collections.unmodifiableCollection(customerHashSet);
    }
    public Customer getCustomerByName(String name) throws IllegalArgumentException{
        if(customerHashMapName.containsKey(name)){
            return customerHashMapName.get(name);
        }else{
            throw new IllegalArgumentException("No costumer with that name exists");
        }

    }
    public Customer getCustomerByAdress(String adress){
        if(customerHashMapAdress.containsKey(adress)){
            return customerHashMapAdress.get(adress);
        }else{
            throw new IllegalArgumentException("No costumer with that address exists");
        }

    }
    public Customer getCustomerByMembershipID(int mID){
        if(customerHashMapAdress.containsKey(mID)){
            return customerHashMapMembershipID.get(mID);
        }else{
            throw new IllegalArgumentException("No member with that ID exists");
        }

    }
    public Customer getCustomerByPhoneNumber(String phone){
        if(customerHashMapPhoneNumber.containsKey(phone)){
            return customerHashMapPhoneNumber.get(phone);
        }else{
            throw new IllegalArgumentException("No costumer with that phonenumber exists");
        }
    }

}