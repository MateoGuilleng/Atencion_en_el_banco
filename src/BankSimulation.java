import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class BankSimulation {
    private List<ServicePoint> servicePoints;
    private PriorityQueue cajaQueue;
    private PriorityQueue plataformaQueue;
    private List<Customer> allCustomers;
    private ExecutorService executorService;
    private volatile boolean simulationRunning;
    private final Object cajaLock = new Object();
    private final Object plataformaLock = new Object();
    
    public BankSimulation() {
        servicePoints = new ArrayList<>();
        servicePoints.add(new ServicePoint("Caja1", "caja"));
        servicePoints.add(new ServicePoint("Caja2", "caja"));
        servicePoints.add(new ServicePoint("Caja3", "caja"));
        servicePoints.add(new ServicePoint("Plataforma1", "plataforma"));
        servicePoints.add(new ServicePoint("Plataforma2", "plataforma"));
        servicePoints.add(new ServicePoint("Plataforma3", "plataforma"));
        
        cajaQueue = new PriorityQueue();
        plataformaQueue = new PriorityQueue();
        allCustomers = new ArrayList<>();
        
        executorService = Executors.newFixedThreadPool(servicePoints.size());
        simulationRunning = false;
    }
    
    public void addCustomer(Customer customer) {
        allCustomers.add(customer);
        if (customer.getServiceType().equals("caja")) {
            synchronized (cajaLock) {
                cajaQueue.enqueue(customer);
            }
        } else if (customer.getServiceType().equals("plataforma")) {
            synchronized (plataformaLock) {
                plataformaQueue.enqueue(customer);
            }
        }
    }
    
    public void startSimulation() {
        simulationRunning = true;
        System.out.println("=== INICIANDO SIMULACIÓN DE ATENCIÓN BANCARIA ===\n");
        
        showCurrentState();
        
        processAllCustomers();
        
        showCurrentState();
        
        System.out.println("=== SIMULACIÓN COMPLETADA ===");
        simulationRunning = false;
    }
    
    private void processAllCustomers() {
        while (hasCustomersWaiting()) {
            processNextBatch();
        }
    }
    
    private void processNextBatch() {
        List<Customer> cajaBatch = getNextBatch("caja", 3);
        List<Customer> plataformaBatch = getNextBatch("plataforma", 3);
        
        for (int i = 0; i < cajaBatch.size(); i++) {
            if (i < servicePoints.size() && servicePoints.get(i).getType().equals("caja")) {
                Customer customer = cajaBatch.get(i);
                if (customer != null) {
                    servicePoints.get(i).startService(customer);
                }
            }
        }
        
        for (int i = 0; i < plataformaBatch.size(); i++) {
            if (i + 3 < servicePoints.size() && servicePoints.get(i + 3).getType().equals("plataforma")) {
                Customer customer = plataformaBatch.get(i);
                if (customer != null) {
                    servicePoints.get(i + 3).startService(customer);
                }
            }
        }
        
        showCurrentState();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        for (ServicePoint servicePoint : servicePoints) {
            if (servicePoint.getCurrentCustomer() != null) {
                servicePoint.completeService();
            }
        }
        
        showCurrentState();
    }
    
    private List<Customer> getNextBatch(String serviceType, int batchSize) {
        List<Customer> batch = new ArrayList<>();
        if (serviceType.equals("caja")) {
            synchronized (cajaLock) {
                for (int i = 0; i < batchSize && !cajaQueue.isEmpty(); i++) {
                    batch.add(cajaQueue.dequeue());
                }
            }
        } else if (serviceType.equals("plataforma")) {
            synchronized (plataformaLock) {
                for (int i = 0; i < batchSize && !plataformaQueue.isEmpty(); i++) {
                    batch.add(plataformaQueue.dequeue());
                }
            }
        }
        return batch;
    }
    
    private boolean hasCustomersWaiting() {
        synchronized (cajaLock) {
            synchronized (plataformaLock) {
                return !cajaQueue.isEmpty() || !plataformaQueue.isEmpty();
            }
        }
    }
    
    private void showCurrentState() {
        System.out.println("Estado actual:");
        for (Customer customer : allCustomers) {
            System.out.println(customer);
        }
        System.out.println();
    }
    
    public void stopSimulation() {
        simulationRunning = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public void showStatistics() {
        System.out.println("=== ESTADÍSTICAS ===");
        System.out.println("Total de clientes: " + allCustomers.size());
        
        long cajaCustomers = allCustomers.stream()
            .filter(c -> c.getServiceType().equals("caja"))
            .count();
        long plataformaCustomers = allCustomers.stream()
            .count();
        
        System.out.println("Clientes de caja: " + cajaCustomers);
        System.out.println("Clientes de plataforma: " + plataformaCustomers);
        
        Map<String, Long> priorityCounts = allCustomers.stream()
            .collect(Collectors.groupingBy(Customer::getPriority, Collectors.counting()));
        
        System.out.println("\nDistribución por prioridad:");
        priorityCounts.forEach((priority, count) -> 
            System.out.println(priority + ": " + count + " clientes"));
    }
}
