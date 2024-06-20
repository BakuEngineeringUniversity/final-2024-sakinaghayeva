import java.util.Scanner

fun main() {
    val rentalService = RentalService()
    val scanner = Scanner(System.`in`)

    rentalService.loadRentals()

    rentalService.addVehicle(Car("Toyota", "Camry", 2020, 20.0))
    rentalService.addVehicle(Car("Honda", "Civic", 2019, 15.0))
    rentalService.addVehicle(Car("Ford", "Mustang", 2021, 25.0))
    rentalService.addVehicle(Car("BMW", "X5", 2018, 30.0))

    println("Xoş gəlmisiniz! Zəhmət olmasa login edin və ya yeni istifadəçi yaradın.")
    print("FIN Kodunuzu daxil edin: ")
    val finCode = scanner.nextLine()
    var customer = rentalService.login(finCode)

    if (customer == null) {
        println("Yeni istifadəçi qeydiyyatı:")
        print("Ad: ")
        val firstName = scanner.nextLine()
        print("Soyad: ")
        val lastName = scanner.nextLine()
        var phoneNumber: String
        while (true) {
            print("Telefon Nömrəsi (yalnız rəqəmlər): ")
            phoneNumber = scanner.nextLine()
            if (phoneNumber.matches("\\d+".toRegex())) {
                break
            } else {
                println("Düzgün telefon nömrəsi daxil edin.")
            }
        }

        customer = Customer(firstName, lastName, phoneNumber, finCode)
        rentalService.registerCustomer(customer)
    } else {
        println("Xoş gəldiniz, ${customer.firstName} ${customer.lastName}")
    }

    while (true) {
        println("\nMenyu:")
        println("1. Maşın axtar")
        println("2. Maşın icarəyə götür")
        println("3. Maşını qaytar")
        println("4. İcarəni uzat")
        println("5. Hazırda icarədə olan maşınlar")
        println("6. Çıxış")
        print("Seçim: ")

        val choice = scanner.nextLine()
        when {
            choice == "1" -> {
                println("Maşın axtarış:")
                println("1. Marka üzrə")
                println("2. Model üzrə")
                println("3. İl üzrə")
                println("4. Qiymət üzrə")
                print("Seçim: ")
                val searchOption = scanner.nextLine()
                when (searchOption) {
                    "1" -> {
                        print("Marka daxil edin: ")
                        val make = scanner.nextLine()
                        rentalService.searchVehicleByMake(make)
                    }
                    "2" -> {
                        print("Model daxil edin: ")
                        val model = scanner.nextLine()
                        rentalService.searchVehicleByModel(model)
                    }
                    "3" -> {
                        print("İl daxil edin: ")
                        val year = scanner.nextLine().toIntOrNull()
                        if (year != null) {
                            rentalService.searchVehicleByYear(year)
                        } else {
                            println("Düzgün il daxil edin.")
                        }
                    }
                    "4" -> {
                        print("Maksimal qiymət daxil edin: ")
                        val rate = scanner.nextLine().toDoubleOrNull()
                        if (rate != null) {
                            rentalService.searchVehicleByRate(rate)
                        } else {
                            println("Düzgün qiymət daxil edin.")
                        }
                    }
                    else -> println("Yanlış seçim, zəhmət olmasa verilen integerlerden birini daxil edin.")
                }
            }
            choice == "2" -> {
                print("Marka daxil edin: ")
                val make = scanner.nextLine()
                print("Model daxil edin: ")
                val model = scanner.nextLine()
                print("Saat sayı daxil edin: ")
                val hours = scanner.nextLine().toIntOrNull()
                if (hours != null) {
                    rentalService.rentVehicle(customer, make, model, hours)
                } else {
                    println("Düzgün saat sayı daxil edin.")
                }
            }
            choice == "3" -> {
                print("Marka daxil edin: ")
                val make = scanner.nextLine()
                print("Model daxil edin: ")
                val model = scanner.nextLine()
                rentalService.returnVehicle(make, model)
            }
            choice == "4" -> {
                print("Marka daxil edin: ")
                val make = scanner.nextLine()
                print("Model daxil edin: ")
                val model = scanner.nextLine()
                print("Əlavə saat sayı daxil edin: ")
                val additionalHours = scanner.nextLine().toIntOrNull()
                if (additionalHours != null) {
                    rentalService.extendRental(customer, make, model, additionalHours)
                } else {
                    println("Düzgün saat sayı daxil edin.")
                }
            }
            choice == "5" -> {
                rentalService.displayRentedVehicles()
            }
            choice == "6" -> {
                rentalService.saveRentals()
                println("Proqramdan çıxılır.")
                break
            }
            else -> println("Yanlış seçim, zəhmət olmasa integer daxil edin.")
        }
    }
}




