package com.smilemakers

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.smilemakers.dashBoard.appointmentFragment.AppointMentViemodelFactory
import com.smilemakers.dashBoard.appointmentFragment.AppointmentRepository
import com.smilemakers.dashBoard.doctorFragment.DoctorRepository
import com.smilemakers.dashBoard.doctorFragment.DoctorVieModelFactory
import com.smilemakers.dashBoard.patientFragment.PatientRepository
import com.smilemakers.dashBoard.patientFragment.PatientViewModelFactory
import com.smilemakers.dashBoard.profile.ProfileRepository
import com.smilemakers.dashBoard.profile.ProfileViewModelFactory
import com.smilemakers.db.AppDatabase
import com.smilemakers.forgotPassword.ForgorPasswordRepository
import com.smilemakers.forgotPassword.ForgotPasswordViewModelFactory
import com.smilemakers.login.AuthViewModelFactory
import com.smilemakers.login.UserRepository
import com.smilemakers.network.MyApi
import com.smilemakers.network.NetworkConnectionInterceptor
import com.smilemakers.utils.LruBitmapCache
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

public class AppController : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppController))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }

        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from singleton { ProfileRepository(instance(), instance()) }
        bind() from singleton { ForgorPasswordRepository(instance(), instance()) }
        bind() from singleton { PatientRepository(instance(), instance()) }
        bind() from singleton { DoctorRepository(instance(), instance()) }
        bind() from singleton { AppointmentRepository(instance(), instance()) }

        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }
        bind() from provider { ForgotPasswordViewModelFactory(instance()) }
        bind() from provider { PatientViewModelFactory(instance()) }
        bind() from provider { DoctorVieModelFactory(instance()) }
        bind() from provider { AppointMentViemodelFactory(instance()) }

    }
    val TAG = AppController::class.java.simpleName

    private var mRequestQueue: RequestQueue? = null
    private var mImageLoader: ImageLoader? = null
    var mLruBitmapCache: LruBitmapCache? = null


    override fun onCreate() {
        super.onCreate()
        val context: Context = AppController.applicationContext()
    }

    init {
        mInstance = this
    }

    companion object {
        private var mInstance: AppController? = null

        fun getInstance() = mInstance

        fun applicationContext(): Context {
            return mInstance!!.applicationContext
        }
    }

    @Synchronized
    fun getInstance(): AppController? {
        return mInstance
    }

    fun getRequestQueue(): RequestQueue? {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(applicationContext)
        }
        return mRequestQueue
    }

    fun getImageLoader(): ImageLoader? {
        getRequestQueue()
        if (mImageLoader == null) {
            getLruBitmapCache()
            mImageLoader = ImageLoader(mRequestQueue, mLruBitmapCache)
        }
        return mImageLoader
    }

    fun getLruBitmapCache(): LruBitmapCache? {
        if (mLruBitmapCache == null) mLruBitmapCache =
            LruBitmapCache()
        return mLruBitmapCache
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String?) {
        req.setTag(if (TextUtils.isEmpty(tag)) TAG else tag)
        //  getRequestQueue()!!.add<Any>(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.setTag(TAG)
        //   getRequestQueue()!!.add<Any>(req)
    }

    fun cancelPendingRequests(tag: Any?) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }
}