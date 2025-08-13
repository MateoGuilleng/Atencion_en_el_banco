public class Main {
    public static void main(String[] args) {
        System.out.println("=== SIMULADOR DE ATENCIÓN BANCARIA ===");
        System.out.println("Implementando colas de prioridad (enqueue/dequeue)\n");
        
        BankSimulation simulation = new BankSimulation();
        
        System.out.println("Agregando clientes...");
        simulation.addCustomer(new Customer("c1", "caja", "normal"));
        simulation.addCustomer(new Customer("c2", "plataforma", "normal"));
        simulation.addCustomer(new Customer("c3", "caja", "normal"));
        simulation.addCustomer(new Customer("c4", "caja", "adulto mayor"));
        simulation.addCustomer(new Customer("c5", "plataforma", "gestante"));
        simulation.addCustomer(new Customer("c6", "caja", "normal"));
        simulation.addCustomer(new Customer("c7", "plataforma", "normal"));
        simulation.addCustomer(new Customer("c8", "caja", "capacidades diferentes"));
        simulation.addCustomer(new Customer("c9", "plataforma", "adulto mayor"));
        simulation.addCustomer(new Customer("c10", "caja", "normal"));
        simulation.addCustomer(new Customer("c11", "plataforma", "gestante"));
        simulation.addCustomer(new Customer("c12", "caja", "normal"));
        simulation.addCustomer(new Customer("c13", "plataforma", "normal"));
        simulation.addCustomer(new Customer("c14", "caja", "adulto mayor"));
        simulation.addCustomer(new Customer("c15", "plataforma", "capacidades diferentes"));
        simulation.addCustomer(new Customer("c16", "caja", "normal"));
        simulation.addCustomer(new Customer("c17", "plataforma", "normal"));
        simulation.addCustomer(new Customer("c18", "caja", "gestante"));
        simulation.addCustomer(new Customer("c19", "plataforma", "normal"));
        simulation.addCustomer(new Customer("c20", "caja", "normal"));
        
        System.out.println("Total de clientes agregados: 20\n");
        
        try {
            simulation.startSimulation();
            simulation.showStatistics();
        } catch (Exception e) {
            System.err.println("Error durante la simulación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            simulation.stopSimulation();
        }
    }
}