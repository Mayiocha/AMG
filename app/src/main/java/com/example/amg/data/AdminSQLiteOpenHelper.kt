package com.example.amg.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.amg.model.Animal
import com.example.amg.model.Lot

class AdminSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "joee", null, 2) {

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
        db?.execSQL("""
    INSERT INTO inventory (name, quantity, unit) 
    VALUES ('Maíz', 550.0, 'kg')""".trimIndent())
        db?.execSQL("INSERT INTO inventory (name, quantity, unit) VALUES ('Silo de Alfalfa', 1200.0, 'kg')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS animals")
        db?.execSQL("DROP TABLE IF EXISTS lots")
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
}
