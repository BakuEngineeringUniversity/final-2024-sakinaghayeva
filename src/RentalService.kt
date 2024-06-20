import java.io.*

class RentalService {
    private val fleet: MutableList<Vehicle> = ArrayList()
    private val rentals: MutableMap<Customer, MutableMap<Vehicle, Int>> = HashMap()
    private val customers: MutableList<Customer> = ArrayList()
    private val rentalHistory: MutableList<Map.Entry<Customer, Vehicle>> = ArrayList()
    private val rentalFile = "rentals.txt"

    fun addVehicle(vehicle: Vehicle) {
        fleet.add(vehicle)
        println("Maşın əlavə edildi: ${vehicle.make} ${vehicle.model} (${vehicle.year})")
    }

    private fun getVehicle(make: String, model: String): Vehicle? {
        for (vehicle in fleet) {
            if (vehicle.make.equals(make, ignoreCase = true) && vehicle.model.equals(model, ignoreCase = true)) {
                return vehicle
            }
        }
        return null
    }

    fun searchVehicleByMake(make: String) {
        val foundVehicles = fleet.filter { vehicle -> vehicle.make.equals(make, ignoreCase = true) && !vehicle.isRented }
        if (foundVehicles.isNotEmpty()) {
            foundVehicles.forEach { vehicle -> vehicle.displayInfo() }
        } else {
            println("Belə bir maşın növü yoxdur.")
        }
    }

    fun searchVehicleByModel(model: String) {
        val foundVehicles = fleet.filter { vehicle -> vehicle.model.equals(model, ignoreCase = true) && !vehicle.isRented }
        if (foundVehicles.isNotEmpty()) {
            foundVehicles.forEach { vehicle -> vehicle.displayInfo() }
        } else {
            println("Belə bir maşın növü yoxdur.")
        }
    }

    fun searchVehicleByYear(year: Int) {
        val foundVehicles = fleet.filter { vehicle -> vehicle.year == year && !vehicle.isRented }
        if (foundVehicles.isNotEmpty()) {
            foundVehicles.forEach { vehicle -> vehicle.displayInfo() }
        } else {
            println("Belə bir maşın növü yoxdur.")
        }
    }

    fun searchVehicleByRate(hourlyRate: Double) {
        fleet.filter { vehicle -> vehicle.hourlyRate <= hourlyRate && !vehicle.isRented }
            .forEach { vehicle -> vehicle.displayInfo() }
    }

    fun rentVehicle(customer: Customer, make: String, model: String, hours: Int) {
        val vehicle = getVehicle(make, model)
        if (vehicle != null && !vehicle.isRented) {
            vehicle.isRented = true
            rentals.computeIfAbsent(customer) { HashMap() }[vehicle] = hours
            val totalCost = vehicle.hourlyRate * hours
            println("Maşın icarəyə götürüldü: ${vehicle.make} ${vehicle.model} (${vehicle.year})")
            println("Ümumi məbləğ: $totalCost AZN")
            val rentalHistory: MutableList<Pair<Customer, Vehicle>> = ArrayList()

            rentalHistory.add(Pair(customer, vehicle))

        } else {
            println("Bu maşın mövcud deyil.")
        }
    }

    fun returnVehicle(make: String, model: String) {
        for (vehicle in fleet) {
            if (vehicle.make.equals(make, ignoreCase = true) && vehicle.model.equals(
                    model,
                    ignoreCase = true
                ) && vehicle.isRented
            ) {
                vehicle.isRented = false
                rentals.forEach { (customer, customerRentals) ->
                    customerRentals.remove(vehicle)
                }
                println("Maşın geri qaytarıldı: $make $model")
                return
            }
        }
        println("Belə bir icarə tapılmadı.")
    }

    fun extendRental(customer: Customer, make: String, model: String, additionalHours: Int) {
        val vehicle = getVehicle(make, model)
        if (vehicle != null && vehicle.isRented) {
            val currentHours = rentals[customer]?.get(vehicle) ?: 0
            rentals[customer]?.set(vehicle, currentHours + additionalHours)
            val totalCost = vehicle.hourlyRate * additionalHours
            println("İcarə müddəti uzadıldı: $additionalHours saat")
            println("Əlavə məbləğ: $totalCost AZN")
        } else {
            println("Bu maşın mövcud deyil.")
        }
    }

    fun registerCustomer(customer: Customer) {
        customers.add(customer)
        println("İstifadəçi qeydiyyatdan keçdi: ${customer.firstName} ${customer.lastName}")
    }

    fun login(finCode: String): Customer? {
        return customers.find { it.finCode.equals(finCode, ignoreCase = true) }
    }

    fun displayRentedVehicles() {
        val rentedVehicles = rentals.values.flatMap { it.keys }
        if (rentedVehicles.isNotEmpty()) {
            println("Hazırda icarədə olan maşınlar:")
            rentedVehicles.forEach { vehicle -> vehicle.displayInfo() }
        } else {
            println("Hazırda icarədə maşın yoxdur.")
        }
    }

    fun saveRentals() {
        try {
            val fileWriter = FileWriter(rentalFile)
            BufferedWriter(fileWriter).use { writer ->
                for (rental in rentals) {
                    val customer = rental.key
                    for ((vehicle, hours) in rental.value) {
                        writer.write(
                            "${customer.firstName},${customer.lastName},${customer.phoneNumber},${customer.finCode},${vehicle.make},${vehicle.model},${vehicle.year},${vehicle.hourlyRate},$hours\n"
                        )
                    }
                }
            }
            println("Rentallar fayla yazıldı.")
        } catch (e: IOException) {
            println("Rentallar fayla yazılarkən xəta baş verdi: ${e.message}")
        }
    }

    fun loadRentals() {
        try {
            val fileReader = FileReader(rentalFile)
            BufferedReader(fileReader).use { reader ->
                reader.lines().forEach { line ->
                    val parts = line.split(",")
                    if (parts.size == 9) {
                        val firstName = parts[0]
                        val lastName = parts[1]
                        val phoneNumber = parts[2]
                        val finCode = parts[3]
                        val make = parts[4]
                        val model = parts[5]
                        val year = parts[6].toIntOrNull() ?: 0
                        val hourlyRate = parts[7].toDoubleOrNull() ?: 0.0
                        val hours = parts[8].toIntOrNull() ?: 0
                        val customer = Customer(firstName, lastName, phoneNumber, finCode)
                        if (!customers.contains(customer)) {
                            customers.add(customer)
                        }
                        val vehicle = Car(make, model, year, hourlyRate)
                        vehicle.isRented = true
                        fleet.add(vehicle)
                        rentals.computeIfAbsent(customer) { HashMap() }[vehicle] = hours
                    }
                }
            }
            println("Rentallar yükləndi.")
        } catch (e: IOException) {
            println("Rentallar yüklənərkən xəta baş verdi: ${e.message}")
        }
    }
}
