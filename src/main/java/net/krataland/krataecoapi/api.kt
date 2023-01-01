package net.krataland.krataecoapi

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.bson.UuidRepresentation
import org.bukkit.entity.Player
import org.litote.kmongo.KMongo
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.util.KMongoUtil

data class Dinero(val cuenta: String, val cantidad: Int)
class KrataDatabase {



    val settings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString("mongodb://admin:admin@20.106.208.178:27017/"))
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .codecRegistry(KMongoUtil.defaultCodecRegistry)
        .build()

    var client = KMongo.createClient(settings)

    fun connectDb(): MongoDatabase {
        return client.getDatabase("KrataEconomy")
    }

}


val collection = KrataDatabase().connectDb().getCollection<Dinero>()


class KrataEconomy {

    /**
     * Crea un banco con el nombre especificado
     */
    fun createEconomy(ecoName: Player): Any? {

        val dato = collection.findOne("{cuenta:'${ecoName.name}'}")

        return if (dato?.cuenta.toString() == ecoName.name) {
            ecoName.sendMessage("Ya existe ese valor")
        } else {
            collection.insertOne(Dinero(ecoName.name, 0))
            ecoName.sendMessage("Creando cuenta para ${ecoName.name} | Dinero: 0")
        }

    }

    /**
     * Actualiza el dinero directamente a una cantidad, sin importar el valor anterior.
     */
    fun setMoney(Name: String, Cantidad: Int): String {

        val dato = collection.findOne("{cuenta:'$Name'}")

        return if (dato?.cuenta == null) {
            "No se encuentra la cuenta especificada."
        } else {
            collection.updateOne(Filters.eq("cuenta", Name), Updates.set("cantidad", Cantidad))
            "Agregados $Cantidad$ a $Name"
        }
    }

    /**
     * Elimina X cantidad de dinero de una cuenta, el dinero eliminado resultante no puede ser inferior a 0.
     */
    fun substractMoney(Name: String, Cantidad: Int): Any? {
        val dato = collection.findOne("{cuenta:'$Name'}")

        return if (dato?.cuenta == Name) {
            if (dato.cantidad >= Cantidad) {
                "Substraidos a ${Name.toString()} la cantidad de ${Cantidad.toString()}"
                collection.findOneAndUpdate(
                    Filters.eq("cuenta", Name),
                    Updates.set("cantidad", dato.cantidad.minus(Cantidad))
                )
            } else {
                "La cantidad especificada dejara la cuenta en negativo."
            }
        } else {
            "Cuenta ${Name}, no encontrada"
        }
    }

    /**
     * Añade X cantidad de dinero a una cuenta, el dinero añadido puede ser cualquiera, siempre se sumará.
     */
    fun addMoney(Name: String, Cantidad: Int): String {
        val dato = collection.findOne("{cuenta:'$Name'}")

        return if (dato?.cuenta == null) {
            "No se encuentra la cuenta especificada."
        } else {
            collection.updateOne(Filters.eq("cuenta", Name), Updates.set("cantidad", dato?.cantidad?.plus(Cantidad)))
            "Agregados $Cantidad$ a $Name"
        }
    }


    /**
     * Comprueba si un usuario tiene X cantidad de Dinero en una cuenta
     */
    fun hasMoney(Name: String, Cantidad: Int): Boolean {
        val datos = collection.findOne("{cuenta:'$Name'}")
        val tieneDinero = datos?.cantidad?.equals(Cantidad) as Boolean

        return tieneDinero
    }
    /**
     * Retorna los datos de una cuenta.
     * @return getEconomy#nombre getEconomy#dinero
     */
    fun getEconomy(Name: String): Any {
        val datos = collection.findOne("{cuenta:'${Name}'}")

        return if(datos?.cuenta == null){
            "No existe tal cuenta"
        } else {
            "Economia de ${datos?.cuenta}\n- Dinero: ${datos?.cantidad}"
        }

    }


}