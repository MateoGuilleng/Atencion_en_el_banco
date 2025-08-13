public class PriorityQueueTest {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE COLA DE PRIORIDAD ===\n");
        
        PriorityQueue queue = new PriorityQueue();
        
        Customer c1 = new Customer("c1", "caja", "normal");
        Customer c2 = new Customer("c2", "caja", "adulto mayor");
        Customer c3 = new Customer("c3", "caja", "gestante");
        Customer c4 = new Customer("c4", "caja", "capacidades diferentes");
        Customer c5 = new Customer("c5", "caja", "normal");
        
        System.out.println("Agregando clientes a la cola...");
        System.out.println("c1 (normal) - enqueue");
        queue.enqueue(c1);
        System.out.println("c2 (adulto mayor) - enqueue");
        queue.enqueue(c2);
        System.out.println("c3 (gestante) - enqueue");
        queue.enqueue(c3);
        System.out.println("c4 (capacidades diferentes) - enqueue");
        queue.enqueue(c4);
        System.out.println("c5 (normal) - enqueue");
        queue.enqueue(c5);
        
        System.out.println("\nEstado de la cola después de enqueue:");
        System.out.println("Tamaño: " + queue.size());
        
        System.out.println("\nProcesando clientes por prioridad (dequeue):");
        System.out.println("1. " + queue.dequeue());
        System.out.println("2. " + queue.dequeue());
        System.out.println("3. " + queue.dequeue());
        System.out.println("4. " + queue.dequeue());
        System.out.println("5. " + queue.dequeue());
        
        System.out.println("\nCola vacía: " + queue.isEmpty());
        System.out.println("Tamaño final: " + queue.size());
        
        System.out.println("\n=== PRUEBA COMPLETADA ===");
    }
}
