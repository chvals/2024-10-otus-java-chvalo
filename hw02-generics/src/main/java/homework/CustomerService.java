package homework;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    TreeMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(o -> o.getScores()));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> customerStringEntry = map.firstEntry();
        if (customerStringEntry == null)
            return null;
        Customer firstCustomer = customerStringEntry.getKey();
        String value = customerStringEntry.getValue();
        return Map.entry(new Customer(firstCustomer.getId(), firstCustomer.getName(), firstCustomer.getScores()), value);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> customerStringEntry = map.higherEntry(customer);
        if (customerStringEntry == null)
            return null;
        Customer nextCustomer = customerStringEntry.getKey();
        String value = customerStringEntry.getValue();
        return nextCustomer != null ? Map.entry(new Customer(nextCustomer.getId(), nextCustomer.getName(), nextCustomer.getScores()), value) : null;
    }

    public void add(Customer customer, String data) {
        map.put(new Customer(customer.getId(), customer.getName(), customer.getScores()), data);
    }
}
