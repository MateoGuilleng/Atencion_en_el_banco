# Simulador de Atención Bancaria

Simulación de atención de clientes en una sucursal bancaria usando colas de prioridad en Java.

## Características
- 3 cajas y 3 plataformas
- Sistema de prioridades: gestante/capacidades diferentes > adulto mayor > normal
- Procesamiento por lotes (batches) según prioridad
- Simulación secuencial simplificada

## Explicación Técnica del Código

### Customer.java - Representación de Clientes
Esta clase modela cada cliente del banco con sus características esenciales:

- **id**: Identificador único del cliente (c1, c2, etc.)
- **serviceType**: Tipo de servicio que necesita ("caja" o "plataforma")
- **priority**: Nivel de prioridad ("normal", "adulto mayor", "gestante", "capacidades diferentes")
- **status**: Estado actual ("en espera", "en atención", "servido")
- **assignedService**: Punto de servicio asignado cuando está siendo atendido

El método `getPriorityLevel()` asigna valores numéricos a las prioridades:
- Gestante: 4 (máxima prioridad)
- Capacidades diferentes: 3
- Adulto mayor: 2
- Normal: 1 (mínima prioridad)

### ServicePoint.java - Puntos de Servicio
Representa cada caja o plataforma disponible:

- **id**: Nombre del punto de servicio (Caja1, Caja2, Caja3, Plataforma1, Plataforma2, Plataforma3)
- **type**: Tipo de servicio ("caja" o "plataforma")
- **isAvailable**: Indica si está libre para atender
- **currentCustomer**: Cliente que está siendo atendido actualmente

Los métodos principales son:
- `startService()`: Inicia la atención de un cliente
- `completeService()`: Finaliza la atención y libera el punto de servicio

### PriorityQueue.java - Implementación de Cola de Prioridad
Esta es la implementación central del sistema de prioridades:

**Estructura Interna:**
- Usa un `ArrayList<Customer>` para almacenar los clientes
- Cada vez que se agrega un cliente, se reordena automáticamente

**Método enqueue():**
```java
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
```

**Cómo Funciona el Ordenamiento:**
1. **Primera prioridad**: Compara niveles de prioridad numérica
   - `c2.getPriorityLevel() - c1.getPriorityLevel()` ordena de mayor a menor prioridad
2. **Segunda prioridad**: Si tienen la misma prioridad, mantiene el orden de llegada
   - `customers.indexOf(c1) - customers.indexOf(c2)` preserva el orden FIFO

**Método dequeue():**
- Siempre retorna el primer cliente de la lista (el de mayor prioridad)
- Lo remueve de la cola usando `customers.remove(0)`

### BankSimulation.java - Motor de la Simulación
Esta clase coordina toda la simulación usando procesamiento por lotes:

**Componentes Principales:**
- **servicePoints**: Lista de puntos de servicio (3 cajas + 3 plataformas)
- **cajaQueue/plataformaQueue**: Colas separadas por tipo de servicio
- **cajaLock/plataformaLock**: Objetos de sincronización para evitar condiciones de carrera

**Sistema de Procesamiento por Lotes:**
```java
private void processNextBatch() {
    List<Customer> cajaBatch = getNextBatch("caja", 3);
    List<Customer> plataformaBatch = getNextBatch("plataforma", 3);
    
    // Asignar clientes a puntos de servicio
    // Esperar 1 segundo
    // Marcar todos como servidos
}
```

**Flujo de Ejecución:**
1. **Inicio**: Se crean las colas y puntos de servicio
2. **Procesamiento por Lotes**: Se toman hasta 6 clientes (3 caja + 3 plataforma) por prioridad
3. **Asignación Simultánea**: Todos los clientes del lote pasan a "en atención" al mismo tiempo
4. **Espera**: Se espera 1 segundo (tiempo fijo para todos)
5. **Finalización**: Todos los clientes del lote pasan a "servido" simultáneamente
6. **Repetición**: Se repite hasta que no queden clientes en espera

**Método getNextBatch():**
```java
private List<Customer> getNextBatch(String serviceType, int batchSize) {
    List<Customer> batch = new ArrayList<>();
    if (serviceType.equals("caja")) {
        synchronized (cajaLock) {
            for (int i = 0; i < batchSize && !cajaQueue.isEmpty(); i++) {
                batch.add(cajaQueue.dequeue());
            }
        }
    }
    return batch;
}
```

**Gestión de Colas Separadas:**
- **cajaQueue**: Solo clientes que necesitan servicio de caja
- **plataformaQueue**: Solo clientes que necesitan servicio de plataforma
- Esto evita que un cliente de caja sea atendido en plataforma y viceversa

### Main.java - Punto de Entrada
Configura y ejecuta la simulación:

1. **Crear Simulación**: Instancia `BankSimulation`
2. **Agregar Clientes**: Crea 20 clientes con diferentes características
3. **Ejecutar**: Llama `startSimulation()` para iniciar el proceso
4. **Estadísticas**: Muestra resultados finales con `showStatistics()`

## Cómo Funciona el Sistema de Prioridades

**Ejemplo Práctico:**
Si tenemos estos clientes en la cola de caja:
- c1: normal, llegó primero
- c2: adulto mayor, llegó segundo  
- c3: gestante, llegó tercero

**Orden de Atención:**
1. **c3 (gestante)** - Prioridad 4, se atiende primero
2. **c2 (adulto mayor)** - Prioridad 2, se atiende segundo
3. **c1 (normal)** - Prioridad 1, se atiende último

**Implementación Técnica:**
```java
customers.sort((c1, c2) -> {
    int priorityDiff = c2.getPriorityLevel() - c1.getPriorityLevel();
    if (priorityDiff != 0) {
        return priorityDiff;  // Ordenar por prioridad
    }
    return customers.indexOf(c1) - customers.indexOf(c2);  // Mantener orden de llegada
});
```

## Ventajas del Nuevo Sistema Simplificado

1. **Simplicidad**: Código más directo y fácil de entender
2. **Predecibilidad**: Todos los clientes se procesan en el mismo tiempo
3. **Eficiencia**: Procesamiento por lotes maximiza el uso de recursos
4. **Claridad**: El flujo de prioridades es más evidente
5. **Mantenibilidad**: Menos complejidad en la gestión de hilos

## Flujo Completo de la Simulación

1. **Inicialización**: Se crean las colas y puntos de servicio
2. **Carga de Clientes**: Se agregan 20 clientes a las colas correspondientes
3. **Procesamiento por Lotes**: Se procesan hasta 6 clientes simultáneamente por prioridad
4. **Ciclo de Atención**: 
   - Tomar siguiente lote por prioridad
   - Asignar a puntos de servicio disponibles
   - Esperar 1 segundo
   - Marcar como servidos
5. **Finalización**: Todos los clientes son atendidos y se muestran estadísticas

## Diferencias con el Sistema Anterior

**Sistema Anterior (Complejo):**
- Múltiples hilos concurrentes
- Tiempos de servicio individuales por cliente
- Gestión compleja de sincronización
- Posibles condiciones de carrera

**Sistema Actual (Simplificado):**
- Procesamiento secuencial por lotes
- Tiempo fijo de 1 segundo para todos
- Sincronización simple con locks
- Flujo predecible y claro

Este nuevo diseño mantiene la funcionalidad esencial del sistema de prioridades mientras simplifica significativamente la implementación, haciendo el código más comprensible y mantenible.
