package pt.ulusofona.deisi.cm2223.g22002160_22006182

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

@SuppressLint("MissingPermission")
class FusedLocation private constructor(context: Context) : LocationCallback() {
    private val TAG = FusedLocation::class.java.simpleName

    private var lastLocation: Location? = null
    private var locationListeners: MutableList<((LatLng?) -> Unit)> = mutableListOf()

    // Intervalos de tempo em que a localização é verificada, 20 segundos
    private val TIME_BETWEEN_UPDATES = 20 * 1000L

    // Este atributo será utilizado para acedermos à API da Fused Location
    private var client = FusedLocationProviderClient(context)

    // Configurar a precisão e os tempos entre atualizações da localização
    private var locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = TIME_BETWEEN_UPDATES
    }

    init {

        // Instanciar o objeto que permite definir as configurações
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        // Aplicar as configurações ao serviço de localização
        LocationServices.getSettingsClient(context)
            .checkLocationSettings(locationSettingsRequest)

        client.requestLocationUpdates(locationRequest,
            this, Looper.getMainLooper()
        )

    }

    override fun onLocationResult(locationResult: LocationResult) {
        //Log.i(TAG, locationResult?.lastLocation.toString())
        lastLocation = locationResult.lastLocation
        notifyListeners(locationResult)
    }

    fun getCurrentLocation(onSuccess: (LatLng?) -> Unit) {
        if (lastLocation != null){
            onSuccess(LatLng(lastLocation!!.latitude, lastLocation!!.longitude))
        } else {
            locationListeners.add(onSuccess)
        }
    }

    private fun notifyListeners(locationResult: LocationResult) {
        val currentLatLng = lastLocation?.let { LatLng(it.latitude, it.longitude) }
        locationListeners.forEach { listener ->
            listener(currentLatLng)
        }
        locationListeners.clear()
    }


    companion object {
        // Se quisermos ter vários listeners isto tem de ser uma lista
        private var listener: OnLocationChangedListener? = null
        private var instance: FusedLocation? = null

        fun getInstance(context: Context): FusedLocation {
            return instance ?: synchronized(this) {
                instance ?: FusedLocation(context.applicationContext).also { instance = it }
            }
        }
        fun registerListener(listener: OnLocationChangedListener) {
            this.listener = listener
        }

        fun unregisterListener() {
            listener = null
        }

        // Se tivermos vários listeners, temos de os notificar com um forEach
        fun notifyListeners(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            listener?.onLocationChanged(location.latitude, location.longitude)
        }



        // Só teremos uma instância em execução
        fun start(context: Context) {
            instance =
                if(instance == null) FusedLocation(context)
                else instance
        }

    }
}