package homework;

import java.util.*;

public class CustomerReverseOrder {

    Stack<Customer> stack = new Stack<>();

    public void add(Customer customer) {
        stack.push(new Customer(customer.getId(), customer.getName(), customer.getScores()));
    }

    public Customer take() {
        return stack.pop();
    }
}
