package com.smilemakers.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.doctorFragment.Doctor
import com.smilemakers.ui.dashBoard.patientFragment.Patient
import com.smilemakers.data.db.entities.User

@Database(
    entities = [User::class, Patient::class, Doctor::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getPatientDao(): PatientDao
    abstract fun getDoctorDao(): DoctorDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance=it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                context.getString(R.string.mydb_name)
            ).build()

    }
}