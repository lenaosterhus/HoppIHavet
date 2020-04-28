package com.example.badeapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.badeapp.models.BadestedForecast
import com.example.badeapp.models.getCurrentForecast
import com.example.badeapp.models.getCurrentWaterTempC
import com.example.badeapp.persistence.LocationForecastDB
import com.example.badeapp.persistence.OceanForecastDB
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.inTheFutureFromNow
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*
import com.example.badeapp.api.LocationForecast.RequestManager as LocationForecastAPI
import com.example.badeapp.api.OceanForecast.RequestManager as OceanForecastAPI

sealed class Badested(
    val lat: String,
    val lon: String,
    val name: String,
    val info: String
) {


    private val TAG = "BADESTED"

    var jobLocation: CompletableJob? = null
    var jobOcean: CompletableJob? = null


    //Databases
    private lateinit var LFDB: LocationForecastDB
    private lateinit var OFDB: OceanForecastDB

    fun initialiseDB(application: Context) {
        Log.d(TAG, "Initing DB's")
        LFDB = LocationForecastDB.getDatabase(application)
        OFDB = OceanForecastDB.getDatabase(application)
    }


    /**
     * We cant have multiple threads trying to update the weather data at once, we
     * therefor put the updating behind a mutex. Then only one thread can try to
     * update the weather data. This makes it so we don't request a update for the same
     * Badested on different threads.
     */
    private val locationMutex = Mutex()
    private val oceanMutex = Mutex()


    private var locationLockdown: Date? = null
    private var oceanLockdown: Date? = null


    private val _forecast: MutableLiveData<BadestedForecast?> = MutableLiveData()
    val forecast: LiveData<BadestedForecast?> = _forecast


    // Info fra Oslo Kommune eller https://www.oslofjorden.com/badesteder/

    object Hovedoya : Badested(
        "59.898397",
        "10.738595",
        "Hovedøya",
        "Hovedøya er Oslos nærmeste og største øy, rett utenfor Vippetangen. Her finner du store gressletter, ruiner etter et Cistercienserkloster, mange flotte stier og turveier og en sydvendt badeplass."
    )

    object Sorenga : Badested(
        "59.894894",
        "10.724028",
        "Sørenga Sjøbad",
        "Stor, delvis flytende badeplass med barnevennlig sandstrand, stupebrett, bryggekanter, benker, gresslette - alt man trenger for en flott sommerdag ved sjøen.\n" + "Sjøbadet er universelt utformet med egen baderampe for bevegelseshemmede.\n" + "Hvis man blir sulten er det flere restauranter og matbutikk på Sørenga, rett innenfor badeplassen."
    )

    object Tjuvholmen : Badested(
        "59.906102",
        "10.720453",
        "Tjuvholmen Sjøbad",
        "Tjuvholmen sjøbad ligger ytterst på Tjuvholmen, sør for Aker brygge - rett ved det nye Astrup Fearnley Museet.\n" + "Flott park med skulpturer og gressplen for solbading.\n" + "Selve stranda har små steiner og er mest for barn som vil vasse litt, svømmedyktige har mulighet for en dukkert fra bryggekanten på utsiden.\n" + "Enda lengre ut på Tjuvholmen er det en fin flytebrygge med et lite stupebrett og dusj. Her er det fin kveldssol helt til sola går ned.\n" + "Massevis av spisesteder og butikker på Tjuvholmen og Aker Brygge."
    )

    object Paradisbukta : Badested(
        "59.901614",
        "10.665422",
        "Paradisbukta",
        "Fin sandstrand med noen bergknauser.\n" + "Det er flotte turveier stort sett rundt hele Bygdøy. Følg kyststi skilting. Litt øst for Paradisbukta finner du minnested og minnesmerke for Tsunamikatastrofen 26.12.2004\n" + "Kort gangavstand østover til Huk (1.2km), Huk naturiststrand (1 km) og vestover til Bygdøy Sjøbad (1.4km)."
    )

    object Hukodden : Badested(
        "59.894349",
        "10.674263",
        "Hukodden",
        "Hukodden er en av Oslos mest populære og fineste badesteder. Her kryr det av mennesker på fine sommerdager. Mye fin sand gir deg sydenfølelse. Selve stranda er halvmåneformet, det er også flere mindre sandstrender.\n" + "Store gressletter egnet for soling og ballspill\n" + "Ytterst på odden mot øst er det kai med badebåtforbindelse til Oslo.\n" + "Hukodden strandrestaurant ligger flott til helt ytterst på odden mot sør.\n" + "Huk naturiststrand ligger på vestsiden av den lille bukta."
    )

    //    object Bekkelagsbadet:  Badested("Bekkelagsbadet er en park som ligger på Søndre Bekkelaget. Parken består av badebrygge, stupetårn og et større grøntområde med sandvolleyballbane, parkour og flere andre aktiviteter.")
    object Bekkensten : Badested(
        "59.791851",
        "10.731905",
        "Bekkensten",
        "Bekkensten er en populær badeplass i Svartskog. Stor gresslette ovenfor badeplassen. Liten sandstrand med noe småstein. Svaberg.\n" + "Nyoppussede toaletter våren 2010"
    )

    object Bestemorstranda : Badested(
        "59.827157",
        "10.759095",
        "Bestemorstanda",
        "Bestemorstranda er den første badeplassen du kommer til langs Ingierstrandveien. Her finner du en enkel badeplass med en liten sandstrand og svaberg.\n" + "Brygga på Bestemorstranda er en populær fiskeplass."
    )

    object BygdoySjobad : Badested(
        "59.908739",
        "10.663209",
        "Bygdøy Sjøbad",
        "Området ble rehabilitert av Statsbygg i 2008 og framstår i dag som et meget fint sted med en fin sandstrand.\n" + "Badestranda ligger ganske langt inn i Bestumkilen, på vestsiden av Bøgdøy."
    )

    object Fiskevollbukta : Badested(
        "59.842146",
        "10.773534",
        "Fiskevollbukta",
        "Fiskevollbukta har en liten vestvendt sandstrand og svaberg. Mye kveldssol. En del skjell og steiner på stranda og ute i vannet."
    )

    object Gressholmen : Badested(
        "59.883348",
        "10.724997",
        "Gressholmen",
        "Gressholmen tilbyr bademuligheter en kort båttur fra Rådhuskaia i Oslo sentrum.\n" + "Flere små bukter og områder med grov sand og ruglete svaberg.\n" + "Mat og drikke får du på Gressholmen kro."
    )

    object Rambergoya : Badested(
        "59.878858",
        "10.714658",
        "Rambergøya",
        "Flere bademuligheter på sørsiden av Rambergøya.\n" + "Rambergøya henger sammen med Gressholmen med en steinfylling."
    )

    object Hvervenbukta : Badested(
        "59.833008",
        "10.771047",
        "Hvervenbukta",
        "Det er to fine sandstrender, svaberg og gressbakker hvor man kan nyte sommersolen til langt på kvelden.\n" + "Fin baderampe for bevegelseshemmede.\n" + "Toalettene er nyoppusset våren 2010."
    )

    //    object Haaoybukta:      Badested("Håøya er den største av øyene i Indre Oslofjord, og ligger nordvest for Drøbak og Oscarsborg festning. Det er lov å telte inntil to døgn av gangen på øyas nordside. Søndre Håøya er naturreservat.\n" + "Et havørnpar hekker på sørøstre Håøya. Vi ber deg om ta ekstra hensyn i dette området. Havørn er svært sårbar for forstyrrelser i hekkeperioden. Dette er første gang på over 125 år at et havørnpar hekker ved Oslofjorden.\n" + "Søndre Håøya er naturreservat.\n" + "Alle planter er fredet i naturreservatet. Det er ikke tillatt å plukke blomster eller bryte greiner. Det er heller ikke lov å fjerne nedfall eller ved. Bær og matsopp kan du plukke på hele øyen. Også i naturreservatet.\n" + "På midtre og nordre Håøya har vi geiter som beitedyr fra mai til desember. Ta hensyn til beitedyra og hold hunden i bånd.")
    object Taajebukta : Badested(
        "59.697676",
        "10.558484",
        "Tåjebukta, Håøya",
        "Liten åpen uthavn på vestsiden av Håøya i Oslofjorden." + "\n" + "Håøya er den største av øyene i Indre Oslofjord, og ligger nordvest for Drøbak og Oscarsborg festning. Det er lov å telte inntil to døgn av gangen på øyas nordside. Søndre Håøya er naturreservat.\n" + "Et havørnpar hekker på sørøstre Håøya. Vi ber deg om ta ekstra hensyn i dette området. Havørn er svært sårbar for forstyrrelser i hekkeperioden. Dette er første gang på over 125 år at et havørnpar hekker ved Oslofjorden.\n" + "Søndre Håøya er naturreservat.\n" + "Alle planter er fredet i naturreservatet. Det er ikke tillatt å plukke blomster eller bryte greiner. Det er heller ikke lov å fjerne nedfall eller ved. Bær og matsopp kan du plukke på hele øyen. Også i naturreservatet.\n" + "På midtre og nordre Håøya har vi geiter som beitedyr fra mai til desember. Ta hensyn til beitedyra og hold hunden i bånd."
    )

    object Ingierstrand : Badested(
        "59.818710",
        "10.749657",
        "Ingierstrand",
        "Badeplass i Oppegård kommune. Svært kupert terreng med svaberg og strand. Funkisperle." + "\n" + "Fasiliteter\n" + "Stupetårn\n" + "10 mobile toaletter (vannklosetter) er åpne i sommersesongen, ca. 15. mai til 15. september.\n" + "Kiosk\n" + "Baderampe\n" + "Badebrygger og badeflåter\n" + "Benker \n" + "Fra 24. juni til 21. august er det livreddere til stede alle dager fra klokken 12:00 til 20:00"
    )

    //    object Katten:          Badested("59.855599", "10.782492")
    object Langoyene : Badested(
        "59.870161",
        "10.716933",
        "Langøyene",
        "Langøyene har en familievennlig og lang sandstrand. Nord på øya er det mulig å telte." + "\n" + "Fasiliteter\n" + "stor, offentlig badestrand\n" + "22 utedoer og en utedo tilpasset handicappede (åpent i sommersesongen)\n" + "svaberg og to sandstrender\n" + "stor gresslette\n" + "fotballbaner\n" + "to sandvolleyballbaner\n" + "benker og bord\n" + "badebrygger og badeflåte\n" + "muligheter for telting på nordsiden av øya (se adferdsreglene for telting)\n" + "naturistbadeplass på den sørlige delen av øya\n" + "delvis tilrettelagt for bevegelseshemmede\n" + "kiosk\n" + "Det er ikke tillatt å tenne bål, men det er tilrettelagte plasser for grilling. Telting er tillatt på Nordre Langøy. Borevannet på øya er renset for bakterier, men smaker vondt og anbefales ikke som drikkevann. Det trenger saltvann inn i borehullet. Mye av den store sletta mellom Søndre og Nordre Langøy er sperret av på grunn av forurensning. Området skal rehabiliteres."
    )

    //    object NordstrandBad:   Badested()
    object Sollerudstranda : Badested(
        "59.911014",
        "10.648008",
        "Sollerudstranda",
        "Sollerudstranda består av to sammenhengende sand og steinstrender.\n" + "Stor gresslette i bakkant.\n" + "Butikk og flere restauranter på Lysaker brygge."
    )

    object Solvikbukta : Badested(
        "59.864351",
        "10.749228",
        "Solvikbukta",
        "Badeplass på Malmøya. Grovkornet sand- og steinstrand med gress og svaberg. Oslos eneste kombinerte bade- og campingplass for funksjonshemmede. Badeplassen ligger i Malmøya naturresevat." + "\n" + "Fasiliteter\n" + "1 mobilt toalett (vannklosett) på parkeringsplassen ved Skinnerbukta i sommersesongen, ca. 15. mai 15. september, håndvask mulig.\n" + "HC-toalett på campingplass rett ved.\n" + "Badebrygge\n" + "Drikkevannsanlegg\n" + "Dusj\n" + "Bord og benker \n" + "Butikk og kafé som tilhører Solvik camping"
    )

    object Ulvoya : Badested(
        "59.866607",
        "10.770682",
        "Sydstranda, Ulvøya",
        "Sydstranda ligger helt sør på Ulvøya og drives av Ulvøy Vel og er den eneste badeplassen i Oslofjorden (?) hvor du må betale inngangspenger. Resultatet er en ren og ordentlig badeplass.\n" + "Sanden på stranda er ganske fin, en del mindre steiner fra oppsmuldrede bergknauser i vannkanten.\n" + "Fint stupetårn med 2 avsatser henholdsvis 3 og 5 meter høyt. NB det er litt grunnt ved stupetårnet, pass på å følge annvisningene på skiltet om å hoppe/stupe rett fram og vise ekstra forsiktighet ved lavvann.\n" + "Et gjennomregulert område med mange skilt. Ro på stranden etter kl 22. Stranden stenges kl 24.00. Hunder har ikke adgang til standa i badesesongen, 1. mai - 1. september"
    )


    /**
     * This function updates the weather data if there exists newer data.
     */
    private fun updateLocationForecast() {
        jobLocation = Job()

        Log.d(TAG, "$name: update locationForecast- starting")

        //If someone else is currently updating location forecast we exit
        //Or if the updating is on lockdown
        if (locationMutex.isLocked || locationLockdown?.after(currentTime()) == true)
            return


        Log.d(TAG, "$name: update locationForecast - not locked")

        jobLocation?.let { job ->
            CoroutineScope(IO + job).launch {

                locationMutex.withLock { //Only one person updating data at once

                    Log.d(TAG, "$name: update locationForecast - in coroutine and with lock")


                    //1) Get location forecast info from from database
                    val locationForecasts = LFDB.locationForecastDao().getForecasts(lat, lon)

                    //2) If data is present and not outdated we don't need to do anything
                    if (locationForecasts.isNotEmpty() && !locationForecasts.any { it -> it.isOutdated() }) {
                        return@launch
                    }

                    //4) Get new data from server
                    val newData = LocationForecastAPI.request(lat, lon)

                    ////5) If data is missing we lockdown any new requests for 20 min, and return
                    if (newData.isNullOrEmpty()) {
                        Log.d(TAG, "$name: update locationForecast - newData is null/empty")
                        locationLockdown = inTheFutureFromNow(20L)
                        return@launch
                    }


                    Log.d(TAG, "$name: update locationForecast - new data exists")
                    //Removes existing entries and puts the new ones inn
                    LFDB.locationForecastDao().replaceAll(newData)

                    withContext(Main) {
                        job.complete()
                    }
                }
            }
        }
    }

    private fun updateOceanForecast() {
        jobOcean = Job()

        Log.d(TAG, "$name: update oceanForecast- starting")

        //If someone else is currently updating location forecast we exit
        //Or if the updating is on lockdown
        if (oceanMutex.isLocked || oceanLockdown?.after(currentTime()) == true)
            return


        Log.d(TAG, "$name: update oceanForecast - not locked")

        jobOcean?.let { job ->
            CoroutineScope(IO + job).launch {

                oceanMutex.withLock { //Only one person updating data at once

                    Log.d(TAG, "$name: update oceanForecast - in coroutine and with lock")


                    //1) Get location forecast info from from database
                    val oceanForecasts = OFDB.oceanForecastDao().getForecasts(lat, lon)

                    //2) If data is present and not outdated we don't need to do anything
                    if (oceanForecasts.isNotEmpty() && !oceanForecasts.any { it -> it.isOutdated() }) {
                        return@launch
                    }

                    //4) Get new data from server
                    val newData = OceanForecastAPI.request(lat, lon)

                    ////5) If data is missing we lockdown any new requests for 20 min, and return
                    if (newData.isNullOrEmpty()) {
                        Log.d(TAG, "$name: update oceanForecast - newData is null/empty")
                        oceanLockdown = inTheFutureFromNow(20L)
                        return@launch
                    }


                    Log.d(TAG, "$name: update oceanForecast - new data exists")
                    //Removes existing entries and puts the new ones inn
                    OFDB.oceanForecastDao().replaceAll(newData)

                    withContext(Main) {
                        job.complete()
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        jobLocation?.cancel()
        jobOcean?.cancel()
    }

    fun updateAll() {
        Log.d(TAG, "updateAll: for $name")
        updateLocationForecast()
        updateOceanForecast()
        setNewForecast() // @TODO remove
    }

    private fun setNewForecast() {
        //@TODO make this propper

        var waterTempc: Double? = null
        var airTempc: Double? = null
        var symbol: Int? = null

        CoroutineScope(IO).launch {

            delay(1000)

            val oceanForecasts = OFDB.oceanForecastDao().getForecasts(lat, lon)
            val temp1 = oceanForecasts.getCurrentWaterTempC()
            val waterTempc = temp1

            val locationForecasts = LFDB.locationForecastDao().getForecasts(lat, lon)
            val temp = locationForecasts.getCurrentForecast()
            airTempc = temp?.airTempC
            symbol = temp?.getIcon()

            if (temp == null) return@launch

            withContext(Dispatchers.Main) {
                _forecast.value = BadestedForecast(
                    lat,
                    lon,
                    temp.from,
                    temp.to,
                    airTempc,
                    waterTempc,
                    symbol
                )
            }
        }
    }

    override fun toString(): String {
        return "Badested: $name"
    }

}



