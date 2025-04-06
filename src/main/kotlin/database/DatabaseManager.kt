package ice.private.brynj.database

import database.exposedLib.tables.*
import ice.private.brynj.database.model.Amenity
import ice.private.brynj.database.model.Hotel
import ice.private.brynj.database.model.Room
import ice.private.brynj.database.tables.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.sql.Connection

object DatabaseManager {
    private val dbFile: File = File("database.sqlite").absoluteFile
    private val db: Database = Database.connect("jdbc:sqlite:file:${dbFile}")

    fun init() {
        TransactionManager.defaultDatabase = db
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        createTablesIfNotExists()
        populateTablesIfEmpty()
    }

    private fun createTablesIfNotExists() = transaction {
        SchemaUtils.create(HotelTable, RoomTable, AmenityTable, HotelAmenitiesTable, BookingTable)
    }

    private fun populateTablesIfEmpty() = transaction {
        val isDbEmpty = listOf(
            HotelTable,
            RoomTable,
            AmenityTable,
            HotelAmenitiesTable,
            BookingTable
        ).all { it.selectAll().empty() }

        if (isDbEmpty) {
            println("db is empty..")
            populateHotelsTable()
            populateRoomsTable()
            populateAmenitiesTable()
            populateHotelAmenitiesTable()
//            populateBookingsTable()
        }
    }


    private fun Transaction.populateHotelsTable() {
        val hotels = listOf(
            Hotel("Hotel Búðir", "Sæbraut 136", "Borgarnes", 4.5, "Experience Icelandic hospitality."),
            Hotel("Ion Adventure Hotel", "Hverfisgata 51", "Vestmannaeyjar", 0.5, "Close to popular attractions."),
            Hotel("Black Pearl Suites", "Laugavegur 142", "Reykjavík", 1.5, "Cozy retreat with beautiful views."),
            Hotel("Hotel Borg", "Frakkastígur 120", "Egilsstaðir", 0.5, "Modern design with Icelandic charm."),
            Hotel("Fosshotel Glacier", "Öldugata 50", "Húsavík", 2.5, "Enjoy the Northern Lights from our rooftop."),
            Hotel("Silica Hotel", "Miðtún 11", "Akranes", 3.5, "Enjoy the Northern Lights from our rooftop."),
            Hotel(
                "Brown-Hays Hotel",
                "28502 Patel Fords Suite 669",
                "South Kristin",
                1.6,
                "Summer six none figure writer political life sport far leader."
            ),
            Hotel(
                "Aguirre LLC Hotel",
                "21670 Larson Cove",
                "West Amandachester",
                0.2,
                "Phone bag suddenly also human bad blood."
            ),
            Hotel(
                "Lozano Ltd Hotel",
                "571 William Shores Suite 455",
                "New Josephville",
                2.9,
                "Detail option other throughout kitchen conference tax college notice."
            ),
            Hotel(
                "Adams-Chandler Hotel",
                "6462 Miller Fork",
                "South Maryland",
                2.3,
                "Billion meeting their that certainly big same produce trade."
            ),
            Hotel(
                "Murphy, Drake and Burns Hotel",
                "17213 Donna Estate Apt. 795",
                "West Kevin",
                4.7,
                "Admit others moment south picture under."
            ),
            Hotel("Browning Inc Hotel", "248 Laura Tunnel", "Claytonmouth", 3.1, "Edge by American poor front."),
            Hotel(
                "Moore-Welch Hotel",
                "670 Holmes Field",
                "Angelaberg",
                4.9,
                "Economic big best view positive kid leave."
            ),
            Hotel(
                "Thompson, Garrison and Alexander Hotel",
                "047 Green Cliffs",
                "South Brianborough",
                4.4,
                "Place message there upon adult hear physical."
            ),
            Hotel(
                "Baldwin Group Hotel",
                "10408 Jason Meadow Suite 734",
                "Lake Meganview",
                0.1,
                "Manager everyone recently difficult necessary yeah few investment."
            ),
            Hotel(
                "Miller, Waller and Smith Hotel",
                "737 Alvarez Spurs",
                "Port Tyronetown",
                3.4,
                "Fall often research interesting else later."
            ),
            Hotel("Johnson Inc Hotel", "929 Sullivan Gardens", "Heidimouth", 4.9, "Mean them rather some score."),
            Hotel("Pineda Inc Hotel", "653 Tina Locks", "East Wesleyborough", 4.4, "Too produce simple after cut."),
            Hotel(
                "Bolton and Sons Hotel",
                "422 Huffman Mews",
                "North Patricia",
                0.3,
                "Special age group leave quite table near."
            ),
            Hotel(
                "Newman, Payne and Parks Hotel",
                "0608 Davis Glen Apt. 623",
                "West Marytown",
                1.0,
                "Person federal science accept within finally."
            ),
            Hotel("Flores Inc Hotel", "571 Michelle Spur", "New David", 4.2, "Mouth sure light second edge."),
            Hotel(
                "Jackson, Reese and Barker Hotel",
                "63583 Joshua Village",
                "Smithview",
                1.3,
                "Social reflect moment keep knowledge of center rich."
            ),
            Hotel(
                "Townsend, Wilson and Grant Hotel",
                "1974 Brandon Brooks",
                "North Alice",
                1.9,
                "Country seek president base structure world leader unit class government."
            ),
            Hotel(
                "Garcia, Peterson and Ferguson Hotel",
                "7478 Lawson Isle Apt. 354",
                "East Cherylfurt",
                1.7,
                "Choice off morning arrive more weight."
            ),
            Hotel(
                "Lopez, Johnson and Jackson Hotel",
                "33868 Jacob Mountains",
                "Tiffanymouth",
                1.6,
                "Road president sign laugh operation child less."
            ),
            Hotel(
                "Carroll-Mcdonald Hotel",
                "8025 Ellis Overpass Suite 899",
                "Lake Alan",
                4.7,
                "Environment option best reason join candidate north visit oil crime."
            ),
            Hotel(
                "Wolfe, Ramos and Gates Hotel",
                "6133 Vazquez Plaza Suite 742",
                "Gainesberg",
                3.0,
                "Among human television cold language fly size two natural."
            ),
            Hotel(
                "Weber-Morris Hotel",
                "64061 Sarah Track",
                "Lake Katherine",
                1.1,
                "Media should buy statement lead hour month."
            ),
            Hotel(
                "Curtis Ltd Hotel",
                "734 Cook Groves Suite 399",
                "Robertberg",
                1.9,
                "At difficult teacher whose above authority hair road ground decide cultural."
            ),
            Hotel(
                "Gibbs Group Hotel",
                "26002 Cynthia Cliffs Suite 784",
                "South Staceyfort",
                0.8,
                "Voice protect wide soldier cover because spring whatever war individual specific."
            ),
            Hotel(
                "Payne-Stout Hotel",
                "4009 Sharon Locks",
                "Whiteton",
                2.2,
                "Traditional animal strong quite choice the agree."
            ),
            Hotel(
                "Hughes and Sons Hotel",
                "267 Barbara Heights Suite 406",
                "Hectorview",
                4.0,
                "None way fire per rate market."
            ),
            Hotel(
                "Moody-Ochoa Hotel",
                "9784 Petty Mission",
                "East Isaacfort",
                0.7,
                "Board daughter question after energy boy institution."
            ),
            Hotel("Mcconnell LLC Hotel", "2619 Carson Hills", "Garyland", 1.6, "Cell certain boy push able."),
            Hotel(
                "Morrow, Hernandez and Beck Hotel",
                "47175 Cheryl Terrace Suite 303",
                "Lake Nicole",
                3.7,
                "Prepare reflect factor bit speak my benefit."
            ),
            Hotel(
                "Pitts, Hall and Duran Hotel",
                "2619 Todd Trace",
                "East Georgeport",
                3.9,
                "Quickly offer society audience find."
            ),
            Hotel(
                "Austin PLC Hotel",
                "4250 Wanda Isle",
                "New Jasmine",
                1.7,
                "Son present firm citizen friend agreement medical couple paper the out."
            ),
            Hotel(
                "Alvarez-Cook Hotel",
                "364 Scott Bridge Apt. 783",
                "Matthewstad",
                2.0,
                "Someone along get save trial."
            ),
            Hotel(
                "Shelton PLC Hotel",
                "1645 Martin Plains",
                "East Michaelport",
                4.2,
                "Responsibility role least position imagine house quite bed mean machine."
            ),
            Hotel(
                "Jones and Sons Hotel",
                "529 John Glens",
                "Washingtonside",
                4.6,
                "Claim page everything fear task training."
            ),
            Hotel(
                "Herrera, Jones and Villarreal Hotel",
                "851 Dawn Crossroad Apt. 144",
                "Fullerberg",
                3.9,
                "Miss drug main condition dream prove."
            ),
            Hotel(
                "Garcia Ltd Hotel",
                "44246 Lee Skyway",
                "South Ericamouth",
                3.6,
                "Before serious rich suggest draw become consider similar create house."
            ),
            Hotel(
                "Lawrence PLC Hotel",
                "227 Elizabeth Fall",
                "Christopherburgh",
                1.8,
                "Name anything skill image nice others training."
            ),
            Hotel(
                "Cook, Hunter and Ortiz Hotel",
                "10292 Bailey River",
                "Port Michelle",
                2.8,
                "President third structure PM account tend."
            ),
            Hotel(
                "Underwood, Valdez and Johnson Hotel",
                "819 Jeff Ramp Suite 884",
                "Micheleborough",
                0.0,
                "Task field bit just then ok doctor hope five."
            ),
            Hotel(
                "Smith-Payne Hotel",
                "67941 Ewing Bypass",
                "East Ronald",
                1.7,
                "Popular data approach consider power bag believe treatment evidence."
            ),
            Hotel(
                "Rodriguez PLC Hotel",
                "25711 Norman Harbor",
                "Travisfort",
                3.6,
                "Worry throughout pressure until fire Mrs center."
            ),
            Hotel("Dennis-Price Hotel", "088 Aaron Skyway", "North Amberberg", 2.4, "Above Mrs general office talk."),
            Hotel(
                "Meyers-Booth Hotel",
                "759 Joseph Fork Apt. 137",
                "Port Jasonfort",
                0.5,
                "Paper beautiful cell institution level."
            ),
            Hotel(
                "Reid-Vazquez Hotel",
                "4112 Morris Meadow Suite 729",
                "Hoffmanmouth",
                0.9,
                "Since term fill open he with wall learn."
            ),
            Hotel(
                "Benjamin, Lindsey and Gonzales Hotel",
                "91945 Robert Harbor",
                "Aaronbury",
                0.6,
                "Argue commercial huge man human garden."
            ),
            Hotel(
                "Swanson PLC Hotel",
                "57557 Brendan Villages",
                "South Monique",
                2.6,
                "Focus type society suggest another also catch kid rise while face."
            ),
            Hotel("Baker-Dunn Hotel", "916 Mark Ridges Apt. 601", "Adkinsfurt", 0.8, "Popular though my society."),
            Hotel(
                "Jimenez-Lara Hotel",
                "2789 Foster Village Apt. 702",
                "Lake Krystalbury",
                0.9,
                "Action which leave society north new against town."
            ),
            Hotel(
                "Padilla-Hill Hotel",
                "594 Lori Overpass",
                "Port Frederick",
                1.0,
                "Finish technology everything a activity apply many her she."
            ),
            Hotel(
                "Campos Group Hotel",
                "468 Kevin Road Apt. 526",
                "Lake Jessicamouth",
                0.2,
                "Spend change add local land play paper."
            ),
            Hotel(
                "Norris PLC Hotel",
                "532 Amanda Roads Suite 115",
                "Port Theresa",
                0.9,
                "Power forward never see argue evidence hour field open."
            ),
            Hotel(
                "Smith and Sons Hotel",
                "202 Julie Plain Suite 979",
                "Port Nathaniel",
                1.0,
                "Rate day recently onto do account world."
            ),
            Hotel(
                "Rodriguez and Sons Hotel",
                "265 Whitaker Crest Suite 773",
                "South Jordanfort",
                2.9,
                "Help save energy space body away look for."
            ),
            Hotel(
                "Moore and Sons Hotel",
                "8121 Marshall Lakes Suite 497",
                "Aaronside",
                4.5,
                "Expert through dream many her itself great husband grow."
            ),
            Hotel(
                "Russell, Smith and Bush Hotel",
                "1901 Wagner Burgs Apt. 941",
                "Joseville",
                1.3,
                "List world these try water."
            ),
            Hotel(
                "Beasley Group Hotel",
                "2524 Nicole Square Suite 217",
                "Mcculloughhaven",
                0.9,
                "Campaign question away without herself my east for kitchen."
            ),
            Hotel(
                "Gilbert-Rodriguez Hotel",
                "60912 Kristina Plains Apt. 889",
                "Hicksland",
                3.5,
                "Blood red cell that under purpose health Congress."
            ),
            Hotel(
                "Davidson-Jimenez Hotel",
                "33681 Yvonne Fort Apt. 188",
                "West Sherrystad",
                0.9,
                "Little front move he here success threat to without politics."
            ),
            Hotel(
                "Watson-Moore Hotel",
                "749 Moreno Flats",
                "Johnsborough",
                0.9,
                "Person large development someone crime fight whatever continue fact."
            ),
            Hotel(
                "Ortega Inc Hotel",
                "45145 Moore Green",
                "Rodneystad",
                3.6,
                "Specific statement should election career value partner tax."
            ),
            Hotel(
                "Reid-Edwards Hotel",
                "05551 Denise Ferry Apt. 019",
                "Aprilmouth",
                0.4,
                "Education our one cup happy."
            ),
            Hotel(
                "Wright-Smith Hotel",
                "247 Anna Tunnel",
                "East Royport",
                2.8,
                "Four song read political argue floor but us lawyer."
            ),
            Hotel(
                "Holt-Weber Hotel",
                "8052 Crawford Drives",
                "New Rebecca",
                0.7,
                "Seem ready air science until fear war."
            ),
            Hotel(
                "Ramirez-Warren Hotel",
                "767 Oscar Islands",
                "West Sabrina",
                3.0,
                "Activity half I thus fine film operation experience rest trip."
            ),
            Hotel(
                "Garcia-Davidson Hotel",
                "97338 Wright Plains",
                "Meghanhaven",
                1.4,
                "Maintain approach listen subject."
            ),
            Hotel(
                "Wilson, Rodriguez and Banks Hotel",
                "139 Carr Port Suite 506",
                "New Emilyview",
                5.0,
                "Identify past arrive yeah score ten feeling spring success travel."
            ),
            Hotel(
                "Smith, Underwood and Morales Hotel",
                "51074 Christine Motorway",
                "Millerborough",
                1.6,
                "Employee section consider stop once trip."
            ),
            Hotel(
                "Williams-Mayer Hotel",
                "889 Hall Field Suite 147",
                "Stephenhaven",
                3.1,
                "Water role up century type camera note theory morning religious."
            ),
            Hotel(
                "Villanueva Group Hotel",
                "78710 Norma Freeway Suite 452",
                "North Jasonborough",
                4.2,
                "Instead car with else reflect move."
            ),
            Hotel(
                "Williams, Durham and Williams Hotel",
                "31807 Michael Shores",
                "South Robertland",
                2.7,
                "Second position art safe."
            ),
            Hotel(
                "Vargas Group Hotel",
                "57808 Tanner Way",
                "Christinaport",
                4.4,
                "Anything avoid capital we talk argue."
            ),
            Hotel(
                "Price-Williams Hotel",
                "554 Jennifer Unions Suite 568",
                "Port Markton",
                0.2,
                "Tax along box half senior record energy property."
            ),
            Hotel(
                "Gibson Group Hotel",
                "305 Martin Trail Apt. 561",
                "Lynchfurt",
                4.0,
                "Candidate avoid cold opportunity us."
            ),
            Hotel(
                "Grimes-Chavez Hotel",
                "7587 Douglas Vista",
                "South Jessica",
                4.0,
                "Market small third yet training citizen operation."
            ),
            Hotel(
                "Sexton PLC Hotel",
                "1835 Luis Bridge",
                "North Sharonton",
                3.9,
                "Worker term foreign seat memory road."
            ),
            Hotel(
                "Callahan Group Hotel",
                "47706 Steve Fords Suite 022",
                "Port Paige",
                1.1,
                "Participant far yard bill father drive court newspaper buy."
            ),
            Hotel(
                "Wong PLC Hotel",
                "595 Sarah Points Suite 591",
                "East Christopher",
                4.0,
                "Mean apply bring people expert federal performance rest."
            ),
            Hotel(
                "Watts-Johns Hotel",
                "7367 Miguel Club",
                "Karitown",
                3.7,
                "Building significant manager yeah boy nature involve suddenly."
            ),
            Hotel(
                "Brown Inc Hotel",
                "583 Tiffany Stream Suite 257",
                "Lake Molly",
                2.7,
                "Air far agent purpose interesting scientist method."
            ),
            Hotel("Wagner LLC Hotel", "497 Jeremy Spurs Apt. 946", "North Aaron", 2.9, "Real read over peace both."),
            Hotel(
                "Acosta, Taylor and Smith Hotel",
                "887 Edwin Land",
                "New Thomasside",
                4.5,
                "While final thought level politics box as however college themselves."
            ),
            Hotel(
                "Dean, Mccoy and Davenport Hotel",
                "90628 Robert Mountains Apt. 933",
                "Murphyland",
                3.3,
                "Prove now else right however."
            ),
            Hotel(
                "Murphy-Barnes Hotel",
                "1663 Brown Port Suite 502",
                "Leonton",
                3.7,
                "Certain expert industry lead surface."
            ),
            Hotel(
                "Scott, Tran and Peters Hotel",
                "39730 Murphy Plain",
                "Lake Kenneth",
                1.6,
                "Leave worry compare will bar break."
            ),
            Hotel(
                "Nunez, Murphy and Torres Hotel",
                "2507 Farley Stream Suite 508",
                "North Sarahchester",
                0.5,
                "Black rise source morning face war year year training."
            ),
            Hotel(
                "Ramos, Sanders and Santana Hotel",
                "64862 Jennifer Overpass Suite 463",
                "Port Ashley",
                2.9,
                "Claim two give building wind adult them land success your."
            ),
            Hotel(
                "Chavez-Mason Hotel",
                "104 Justin Loop",
                "Bartlettmouth",
                2.7,
                "Service along night eye land successful."
            ),
            Hotel(
                "Ashley-Mcgee Hotel",
                "225 Miles Village Suite 002",
                "Ballardmouth",
                4.4,
                "Term as position mission challenge wonder bar factor early also prove."
            ),
            Hotel(
                "Neal-Payne Hotel",
                "208 Francisco Dam",
                "Christopherfort",
                1.3,
                "Them daughter either help risk need show."
            ),
            Hotel(
                "Solis, Simpson and Hawkins Hotel",
                "3988 Patty Glens",
                "New Casey",
                3.7,
                "Word table information represent."
            ),
            Hotel(
                "Michael-Arnold Hotel",
                "474 West Lakes",
                "Sanfordshire",
                2.2,
                "Seek country economy add identify reflect seem week own person."
            ),
            Hotel(
                "Taylor LLC Hotel",
                "82229 Philip Ridge",
                "Patelfurt",
                1.7,
                "Act south actually standard draw laugh young."
            ),
            Hotel(
                "Wall-Matthews Hotel",
                "88117 Freeman Square",
                "Chavezview",
                1.7,
                "High letter until who later watch."
            ),
            Hotel(
                "Hicks Group Hotel",
                "5279 Moore Ridge Apt. 896",
                "West Kevinmouth",
                1.3,
                "Senior although design work set knowledge official example."
            )
        )

        HotelTable.batchInsert(hotels) { hotel ->
            this[HotelTable.name] = hotel.name
            this[HotelTable.address] = hotel.address
            this[HotelTable.city] = hotel.city
            this[HotelTable.starRating] = hotel.starRating
            this[HotelTable.description] = hotel.description
        }
    }

    private fun Transaction.populateRoomsTable() {
        val priceRange = (4000..12000)

        val hotelIds = HotelTable.selectAll().map { it[HotelTable.id].value }

        val roomNumbers = listOf(
            "101", "102", "103", "104", "105",
            "201", "202", "203", "204", "205",
            "301", "302", "303", "304", "305",
            "401", "402", "403", "404", "405"
        )

        val rooms = hotelIds.flatMap { hotelId ->
            roomNumbers.map { roomNumber ->
                Room(
                    roomNumber = roomNumber,
                    pricePerNight = priceRange.random(),
                    hotelId = hotelId
                )
            }
        }

        RoomTable.batchInsert(rooms) { room ->
            this[RoomTable.roomNumber] = room.roomNumber
            this[RoomTable.pricePerNight] = room.pricePerNight
            this[RoomTable.hotel] = room.hotelId
        }
    }

    private fun Transaction.populateAmenitiesTable() {
        val amenities = listOf(
            Amenity("Free Wi-Fi"),
            Amenity("Parking"),
            Amenity("24-Hour Front Desk"),
            Amenity("Fitness Center"),
            Amenity("Swimming Pool"),
            Amenity("Spa & Wellness Center"),
            Amenity("Airport Shuttle"),
            Amenity("Room Service"),
            Amenity("Restaurant"),
            Amenity("Bar"),
            Amenity("Laundry Service"),
            Amenity("Non-Smoking Rooms"),
            Amenity("Pet-Friendly"),
            Amenity("Business Center"),
            Amenity("Air Conditioning"),
            Amenity("Heating"),
            Amenity("Flat-Screen TV"),
            Amenity("Mini-Bar"),
            Amenity("Safe Deposit Box"),
            Amenity("Concierge Service"),
        )

        AmenityTable.batchInsert(amenities) { amenity ->
            this[AmenityTable.name] = amenity.name
        }
    }


    private fun Transaction.populateHotelAmenitiesTable() {
        val hotelAmenityAmount = 5

        val hotelIds = HotelTable.selectAll().map { it[HotelTable.id].value }
        val amenityIds = AmenityTable.selectAll().map { it[AmenityTable.id].value }

        val hotelAmenityPairs = hotelIds.flatMap { hotelId ->
            amenityIds.shuffled().take(hotelAmenityAmount).map { amenityId ->
                hotelId to amenityId
            }
        }

        HotelAmenitiesTable.batchInsert(hotelAmenityPairs) { (hotelId, amenityId) ->
            this[HotelAmenitiesTable.hotel] = hotelId
            this[HotelAmenitiesTable.amenity] = amenityId
        }
    }

    private fun Transaction.populateBookingsTable() {
        TODO("Maybe not?")
    }

}