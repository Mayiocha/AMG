package com.example.amg.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.amg.model.Animal
import com.example.amg.model.Lot

class AdminSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "joee", null, 4) {

    override fun onCreate(db: SQLiteDatabase?) {
        // 1. Crear Tabla de Lotes
        db?.execSQL("""
        CREATE TABLE lots (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            quantity INTEGER,
            stage INTEGER,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
    """.trimIndent())

        // 2. Crear Tabla de Animales
        db?.execSQL("""
        CREATE TABLE animals (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            tag_id TEXT,
            lot_id INTEGER,
            category TEXT, 
            race TEXT,
            weight REAL,
            month_old INTEGER,
            is_healthy INTEGER,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY(lot_id) REFERENCES lots(id)
        )
    """.trimIndent())

        db?.execSQL("""
    CREATE TABLE inventory (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        quantity REAL NOT NULL,
        unit TEXT NOT NULL DEFAULT 'kg',
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    )
""".trimIndent())

        // 3. Crear Tabla de Mezclas
        db?.execSQL("""
    CREATE TABLE mixtures (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        type TEXT NOT NULL,
        quantity REAL NOT NULL,
        unit TEXT NOT NULL DEFAULT 'kg',
        status TEXT NOT NULL DEFAULT 'Activa',
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    )
""".trimIndent())

        // 4. Crear Tabla de Ingredientes por Mezcla
        db?.execSQL("""
    CREATE TABLE mixture_ingredients (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        mixture_id INTEGER NOT NULL,
        inventory_id INTEGER NOT NULL,
        quantity REAL NOT NULL,
        FOREIGN KEY(mixture_id) REFERENCES mixtures(id) ON DELETE CASCADE,
        FOREIGN KEY(inventory_id) REFERENCES inventory(id)
    )
""".trimIndent())

        // 3. INSERTAR DATOS SEMILLA — 3 lotes para los animales de prueba
        db?.execSQL("INSERT INTO lots (quantity, stage) VALUES (10, 1)") // Lote 1
        db?.execSQL("INSERT INTO lots (quantity, stage) VALUES (8, 1)")  // Lote 2
        db?.execSQL("INSERT INTO lots (quantity, stage) VALUES (5, 2)")  // Lote 3

        // Animales semilla en lote 1
        db?.execSQL("""
        INSERT INTO animals (tag_id, lot_id, category, race, weight, month_old, is_healthy) 
        VALUES ('TAG-001', 1, 'Vaca', 'Holstein', 550.5, 24, 1)
    """.trimIndent())
        db?.execSQL("""
        INSERT INTO animals (tag_id, lot_id, category, race, weight, month_old, is_healthy) 
        VALUES ('TAG-002', 1, 'Toro', 'Angus', 820.0, 36, 1)
    """.trimIndent())
        db?.execSQL("""
        INSERT INTO animals (tag_id, lot_id, category, race, weight, month_old, is_healthy) 
        VALUES ('TAG-003', 1, 'Ternera', 'Jersey', 120.0, 6, 0)
    """.trimIndent())

        // Datos semilla para el inventario
        db?.execSQL("INSERT INTO inventory (name, quantity, unit) VALUES ('Maíz', 550.0, 'kg')")
        db?.execSQL("INSERT INTO inventory (name, quantity, unit) VALUES ('Silo de Alfalfa', 1200.0, 'kg')")
        db?.execSQL("INSERT INTO inventory (name, quantity, unit) VALUES ('Pasto Seco', 800.0, 'kg')")
        db?.execSQL("INSERT INTO inventory (name, quantity, unit) VALUES ('Melaza', 200.0, 'L')")

        // Datos semilla para mezclas
        db?.execSQL("INSERT INTO mixtures (name, type, quantity, unit, status) VALUES ('Mezcla Engorda 1', 'Engorda', 50.0, 'kg', 'Activa')")
        db?.execSQL("INSERT INTO mixtures (name, type, quantity, unit, status) VALUES ('Mezcla Lechera', 'Producción', 35.0, 'kg', 'Activa')")
        
        // Ingredientes para Mezcla Engorda 1 (id=1)
        db?.execSQL("INSERT INTO mixture_ingredients (mixture_id, inventory_id, quantity) VALUES (1, 1, 30.0)") // 30kg Maíz
        db?.execSQL("INSERT INTO mixture_ingredients (mixture_id, inventory_id, quantity) VALUES (1, 2, 20.0)") // 20kg Silo
        
        // Ingredientes para Mezcla Lechera (id=2)
        db?.execSQL("INSERT INTO mixture_ingredients (mixture_id, inventory_id, quantity) VALUES (2, 2, 25.0)") // 25kg Silo
        db?.execSQL("INSERT INTO mixture_ingredients (mixture_id, inventory_id, quantity) VALUES (2, 3, 10.0)") // 10kg Pasto
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS animals")
        db?.execSQL("DROP TABLE IF EXISTS lots")
        db?.execSQL("DROP TABLE IF EXISTS mixture_ingredients")
        db?.execSQL("DROP TABLE IF EXISTS mixtures")
        db?.execSQL("DROP TABLE IF EXISTS inventory")
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON;")
    }

    // ──────────────────────────────────────────────
    // ANIMALS
    // ──────────────────────────────────────────────

    fun insertAnimal(animal: Animal): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("tag_id", animal.tagId)
            put("lot_id", animal.lotId)
            put("category", animal.category)
            put("race", animal.race)
            put("weight", animal.weight)
            put("month_old", animal.monthOld)
            put("is_healthy", if (animal.isHealthy) 1 else 0)
        }
        val id = db.insert("animals", null, values)
        db.close()
        return id
    }

    fun getAllAnimals(): List<Animal> = getAnimalsByLot(null)

    fun getAnimalsByLot(lotId: Int?): List<Animal> {
        val lista = mutableListOf<Animal>()
        val db = this.readableDatabase
        val query = if (lotId == null)
            "SELECT * FROM animals"
        else
            "SELECT * FROM animals WHERE lot_id = $lotId"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Animal(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        tagId = cursor.getString(cursor.getColumnIndexOrThrow("tag_id")),
                        lotId = cursor.getInt(cursor.getColumnIndexOrThrow("lot_id")),
                        category = cursor.getString(cursor.getColumnIndexOrThrow("category")),
                        race = cursor.getString(cursor.getColumnIndexOrThrow("race")),
                        weight = cursor.getFloat(cursor.getColumnIndexOrThrow("weight")),
                        monthOld = cursor.getInt(cursor.getColumnIndexOrThrow("month_old")),
                        isHealthy = cursor.getInt(cursor.getColumnIndexOrThrow("is_healthy")) == 1
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun updateAnimal(animal: Animal): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("tag_id", animal.tagId)
            put("lot_id", animal.lotId)
            put("category", animal.category)
            put("race", animal.race)
            put("weight", animal.weight)
            put("month_old", animal.monthOld)
            put("is_healthy", if (animal.isHealthy) 1 else 0)
        }
        val rows = db.update("animals", values, "id = ?", arrayOf(animal.id.toString()))
        db.close()
        return rows
    }

    // ──────────────────────────────────────────────
    // LOTS
    // ──────────────────────────────────────────────

    /** Devuelve todos los lotes con conteo de animales por categoría. */
    fun getAllLots(): List<Lot> {
        val lista = mutableListOf<Lot>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM lots ORDER BY id ASC", null)
        if (cursor.moveToFirst()) {
            do {
                val lotId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val counts = getAnimalCountsForLot(db, lotId)
                lista.add(
                    Lot(
                        id = lotId,
                        quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        stage = cursor.getInt(cursor.getColumnIndexOrThrow("stage")),
                        createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at")) ?: "",
                        totalAnimals = counts[0],
                        vacas = counts[1],
                        toros = counts[2],
                        terneras = counts[3]
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    private fun getAnimalCountsForLot(db: SQLiteDatabase, lotId: Int): IntArray {
        // [total, vacas, toros, terneras]
        val counts = IntArray(4)
        val c = db.rawQuery(
            "SELECT category, COUNT(*) as cnt FROM animals WHERE lot_id = ? GROUP BY category",
            arrayOf(lotId.toString())
        )
        if (c.moveToFirst()) {
            do {
                val cat = c.getString(0).lowercase()
                val cnt = c.getInt(1)
                counts[0] += cnt
                when {
                    cat.contains("vaca")    -> counts[1] += cnt
                    cat.contains("toro")    -> counts[2] += cnt
                    cat.contains("ternera") -> counts[3] += cnt
                }
            } while (c.moveToNext())
        }
        c.close()
        return counts
    }

    /** Devuelve lista de IDs de lotes como Strings para spinners. */
    fun getAllLotIds(): List<String> {
        val ids = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM lots ORDER BY id ASC", null)
        if (cursor.moveToFirst()) {
            do { ids.add(cursor.getInt(0).toString()) } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return ids
    }

    fun insertLot(quantity: Int, stage: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("quantity", quantity)
            put("stage", stage)
        }
        val id = db.insert("lots", null, values)
        db.close()
        return id
    }

    fun updateLot(id: Int, quantity: Int, stage: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("quantity", quantity)
            put("stage", stage)
        }
        val rows = db.update("lots", values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    // ──────────────────────────────────────────────
    // INVENTORY
    // ──────────────────────────────────────────────

    fun getAllInventory(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT name, quantity, unit FROM inventory", null)
        if (cursor.moveToFirst()) {
            do {
                val info = "${cursor.getString(0)} - ${cursor.getFloat(1)} ${cursor.getString(2)}"
                list.add(cursor.getString(0) to info)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    /** Devuelve lista con id, name, quantity, unit para mostrar en la tabla. */
    fun getInventoryDetails(): List<Triple<String, Float, String>> {
        val list = mutableListOf<Triple<String, Float, String>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT name, quantity, unit FROM inventory", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(Triple(cursor.getString(0), cursor.getFloat(1), cursor.getString(2)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    /** Devuelve todos los items del inventario incluyendo su id de base de datos. */
    fun getInventoryWithIds(): List<InventoryItem> {
        val list = mutableListOf<InventoryItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, name, quantity, unit FROM inventory ORDER BY id ASC", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    InventoryItem(
                        id       = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name     = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        quantity = cursor.getFloat(cursor.getColumnIndexOrThrow("quantity")),
                        unit     = cursor.getString(cursor.getColumnIndexOrThrow("unit"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    /** Obtiene un item de inventario por su id. */
    fun getInventoryById(id: Int): InventoryItem? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, name, quantity, unit FROM inventory WHERE id = ?",
            arrayOf(id.toString())
        )
        var item: InventoryItem? = null
        if (cursor.moveToFirst()) {
            item = InventoryItem(
                id       = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name     = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                quantity = cursor.getFloat(cursor.getColumnIndexOrThrow("quantity")),
                unit     = cursor.getString(cursor.getColumnIndexOrThrow("unit"))
            )
        }
        cursor.close()
        db.close()
        return item
    }

    /** Actualiza nombre, cantidad y unidad de un item de inventario. */
    fun updateInventoryItem(id: Int, name: String, quantity: Float, unit: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name",     name)
            put("quantity", quantity)
            put("unit",     unit)
        }
        val rows = db.update("inventory", values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    /** Inserta un nuevo item en el inventario. Devuelve el id generado o -1 si falla. */
    fun insertInventoryItem(name: String, quantity: Float, unit: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name",     name)
            put("quantity", quantity)
            put("unit",     unit)
        }
        val id = db.insert("inventory", null, values)
        db.close()
        return id
    }

    data class InventoryItem(val id: Int, val name: String, val quantity: Float, val unit: String)

    // ──────────────────────────────────────────────
    // MIXTURES
    // ──────────────────────────────────────────────

    fun getAllMixtures(): List<Mixture> {
        val list = mutableListOf<Mixture>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, name, type, quantity, unit, status FROM mixtures", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Mixture(
                        id = cursor.getInt(0),
                        name = cursor.getString(1),
                        type = cursor.getString(2),
                        quantity = cursor.getFloat(3),
                        unit = cursor.getString(4),
                        status = cursor.getString(5)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getMixtureById(id: Int): Mixture? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, name, type, quantity, unit, status FROM mixtures WHERE id = ?", arrayOf(id.toString()))
        var mixture: Mixture? = null
        if (cursor.moveToFirst()) {
            mixture = Mixture(
                id = cursor.getInt(0),
                name = cursor.getString(1),
                type = cursor.getString(2),
                quantity = cursor.getFloat(3),
                unit = cursor.getString(4),
                status = cursor.getString(5)
            )
        }
        cursor.close()
        return mixture
    }

    fun insertMixtureWithIngredients(
        name: String, 
        type: String, 
        status: String, 
        ingredients: List<Pair<Int, Float>> // Pair<InventoryId, QuantityUsed>
    ): Long {
        val db = this.writableDatabase
        var mixtureId = -1L
        
        db.beginTransaction()
        try {
            // Calcular cantidad total asumiendo kg
            val totalQuantity = ingredients.sumOf { it.second.toDouble() }.toFloat()

            // Insertar Mezcla
            val mixValues = ContentValues().apply {
                put("name", name)
                put("type", type)
                put("quantity", totalQuantity)
                put("unit", "kg")
                put("status", status)
            }
            mixtureId = db.insert("mixtures", null, mixValues)
            if (mixtureId == -1L) throw Exception("Error insertando mezcla")

            // Procesar Ingredientes
            for (ingredient in ingredients) {
                val invId = ingredient.first
                val qtyUsed = ingredient.second

                // Insertar relacion
                val ingValues = ContentValues().apply {
                    put("mixture_id", mixtureId)
                    put("inventory_id", invId)
                    put("quantity", qtyUsed)
                }
                db.insert("mixture_ingredients", null, ingValues)

                // Restar del inventario
                db.execSQL("UPDATE inventory SET quantity = quantity - ? WHERE id = ?", arrayOf(qtyUsed, invId))
            }
            
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            mixtureId = -1L
        } finally {
            db.endTransaction()
            db.close()
        }
        return mixtureId
    }

    /**
     * Devuelve una lista de Triple<IdInventario, NombreAlimento, CantidadUsada>
     */
    fun getIngredientsForMixture(mixtureId: Int): List<Triple<Int, String, Float>> {
        val list = mutableListOf<Triple<Int, String, Float>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("""
            SELECT i.id, i.name, mi.quantity 
            FROM mixture_ingredients mi 
            JOIN inventory i ON mi.inventory_id = i.id 
            WHERE mi.mixture_id = ?
        """.trimIndent(), arrayOf(mixtureId.toString()))
        
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Triple(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getFloat(2)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun updateMixture(id: Int, name: String, type: String, status: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("type", type)
            // No editamos quantity ni unit porque vienen dados por los ingredientes
            put("status", status)
        }
        val rows = db.update("mixtures", values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    fun deleteAnimal(id: Int): Int {
        val db = this.writableDatabase
        val rows = db.delete("animals", "id = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    fun deleteLot(id: Int): Int {
        val db = this.writableDatabase
        val rows = db.delete("lots", "id = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    fun deleteInventoryItem(id: Int): Int {
        val db = this.writableDatabase
        val rows = db.delete("inventory", "id = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    fun deleteMixture(id: Int): Int {
        val db = this.writableDatabase
        val rows = db.delete("mixtures", "id = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    data class Mixture(val id: Int, val name: String, val type: String, val quantity: Float, val unit: String, val status: String)
}
