import java.util.*;

public class PriorityQueue {
    private List<Customer> customers;
    
    public PriorityQueue() {
        this.customers = new ArrayList<>();
    }
    
    public void enqueue(Customer customer) {
        customers.add(customer);
        customers.sort((c1, c2) -> {
            int priorityDiff = c2.getPriorityLevel() - c1.getPriorityLevel();
            if (priorityDiff != 0) {
                return priorityDiff;
            }
            return customers.indexOf(c1) - customers.indexOf(c2);
        });
    }
    
    public Customer dequeue() {
        if (customers.isEmpty()) {
            return null;
        }
        return customers.remove(0);
    }
    
    public Customer peek() {
        if (customers.isEmpty()) {
            return null;
        }
        return customers.get(0);
    }
    
    public boolean isEmpty() {
        return customers.isEmpty();
    }
    
    public int size() {
        return customers.size();
    }
    
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }
    
    public boolean remove(Customer customer) {
        return customers.remove(customer);
    }
}
