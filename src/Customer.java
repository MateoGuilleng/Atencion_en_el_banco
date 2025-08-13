public class Customer {
    private String id;
    private String serviceType;
    private String priority;
    private String status;
    private String assignedService;
    
    public Customer(String id, String serviceType, String priority) {
        this.id = id;
        this.serviceType = serviceType;
        this.priority = priority;
        this.status = "en espera";
        this.assignedService = "";
    }
    
    public String getId() { return id; }
    public String getServiceType() { return serviceType; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public String getAssignedService() { return assignedService; }
    
    public void setStatus(String status) { this.status = status; }
    public void setAssignedService(String assignedService) { this.assignedService = assignedService; }
    
    public int getPriorityLevel() {
        switch (priority) {
            case "gestante":
                return 4;
            case "capacidades diferentes":
                return 3;
            case "adulto mayor":
                return 2;
            default:
                return 1;
        }
    }
    
    @Override
    public String toString() {
        if (status.equals("en atención") && !assignedService.isEmpty()) {
            return id + ", en atención " + assignedService;
        } else {
            return id + ", " + status;
        }
    }
}
