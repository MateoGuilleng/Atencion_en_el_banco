public class ServicePoint {
    private String id;
    private String type;
    private boolean isAvailable;
    private Customer currentCustomer;
    
    public ServicePoint(String id, String type) {
        this.id = id;
        this.type = type;
        this.isAvailable = true;
        this.currentCustomer = null;
    }
    
    public String getId() { return id; }
    public String getType() { return type; }
    public boolean isAvailable() { return isAvailable; }
    public Customer getCurrentCustomer() { return currentCustomer; }
    
    public void startService(Customer customer) {
        this.currentCustomer = customer;
        this.isAvailable = false;
        customer.setStatus("en atención");
        customer.setAssignedService(this.id);
    }
    
    public Customer completeService() {
        if (currentCustomer != null) {
            currentCustomer.setStatus("servido");
            Customer completed = currentCustomer;
            this.currentCustomer = null;
            this.isAvailable = true;
            return completed;
        }
        return null;
    }
    
    public boolean canServe(String serviceType) {
        return this.type.equals(serviceType);
    }
}
