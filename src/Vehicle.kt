abstract class Vehicle(val make: String, val model: String, val year: Int, val hourlyRate: Double) {
    var isRented: Boolean = false

    abstract fun displayInfo()
}

class Car(make: String, model: String, year: Int, hourlyRate: Double) : Vehicle(make, model, year, hourlyRate) {
    override fun displayInfo() {
        println("$make $model ($year), saatlıq icarə haqqı: $hourlyRate AZN")
    }
}
