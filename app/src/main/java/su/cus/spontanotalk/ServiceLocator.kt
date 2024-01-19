package su.cus.spontanotalk

interface Service {
    fun performAction()
}

class ConcreteService : Service {
    override fun performAction() {
        println("Service action performed.")
    }
}
object ServiceLocator {
    private val services = mutableMapOf<Class<*>, Service>()

    fun <T: Service> registerService(serviceClass: Class<T>, service: T) {
        services[serviceClass] = service
    }

    fun <T: Service> getService(serviceClass: Class<T>): T {
        return serviceClass.cast(services[serviceClass]) ?: throw IllegalArgumentException("No service found for: ${serviceClass.name}")
    }
}
fun main() {
    // Register the service
    ServiceLocator.registerService(Service::class.java, ConcreteService())

    // Retrieve and use the service
    val service = ServiceLocator.getService(Service::class.java)
    service.performAction()


}


