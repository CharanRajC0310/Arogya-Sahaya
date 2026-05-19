package com.arogya.sahaya

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arogya.sahaya.data.*
import com.arogya.sahaya.reminder.NotificationHelper
import com.arogya.sahaya.reminder.ReminderManager
import com.arogya.sahaya.remoteconfig.RemoteConfigManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// ----------------------------
// LOCALIZATION (KANNADA & ENGLISH)
// ----------------------------

enum class Language { EN, KN }

object Strings {
    private val strings = mapOf(
        Language.EN to mapOf(
            "app_name" to "Arogya-Sahaya",
            "tagline" to "Your Local Health Companion",
            "namaste" to "Namaste!",
            "welcome_msg" to "Please help us know you better",
            "name" to "Your Name",
            "age" to "Age",
            "conditions" to "Illnesses (e.g. Sugar, BP)",
            "caregiver_name" to "Caregiver Name",
            "caregiver_phone" to "Caregiver Phone Number",
            "save_continue" to "Save & Continue",
            "home" to "Home",
            "meds" to "Meds",
            "vitals" to "Vitals",
            "asha" to "ASHA",
            "profile" to "Profile",
            "health_today" to "Your Health Today",
            "emergency_sos" to "EMERGENCY SOS",
            "tap_for_help" to "Tap for Immediate Help",
            "health_camps" to "Upcoming Health Camps",
            "log_vitals" to "Log BP or Heart Rate",
            "health_trends" to "Health Trends",
            "recent_records" to "Recent Records",
            "medicine_schedule" to "Medicine Schedule",
            "add_med" to "Add New Medicine",
            "asha_calendar" to "ASHA Health Calendar",
            "logout" to "Change Language / Reset Profile"
        ),
        Language.KN to mapOf(
            "app_name" to "ಆರೋಗ್ಯ ಸಹಾಯ",
            "tagline" to "ನಿಮ್ಮ ಸ್ಥಳೀಯ ಆರೋಗ್ಯ ಸಂಗಾತಿ",
            "namaste" to "ನಮಸ್ಕಾರ!",
            "welcome_msg" to "ನಿಮ್ಮ ಬಗ್ಗೆ ನಮಗೆ ತಿಳಿಸಿ",
            "name" to "ನಿಮ್ಮ ಹೆಸರು",
            "age" to "ವಯಸ್ಸು",
            "conditions" to "ಆರೋಗ್ಯ ಸಮಸ್ಯೆಗಳು (ಉದಾ: ಸಕ್ಕರೆ ಕಾಯಿಲೆ, ಬಿಪಿ)",
            "caregiver_name" to "ಪಾಲನೆ ಮಾಡುವವರ ಹೆಸರು",
            "caregiver_phone" to "ಪಾಲನೆ ಮಾಡುವವರ ಫೋನ್ ಸಂಖ್ಯೆ",
            "save_continue" to "ಉಳಿಸಿ ಮತ್ತು ಮುಂದುವರಿಯಿರಿ",
            "home" to "ಮನೆ",
            "meds" to "ಔಷಧಿಗಳು",
            "vitals" to "ಆರೋಗ್ಯ",
            "asha" to "ಆಶಾ",
            "profile" to "ಪ್ರೊಫೈಲ್",
            "health_today" to "ಇಂದಿನ ನಿಮ್ಮ ಆರೋಗ್ಯ",
            "emergency_sos" to "ತುರ್ತು ಪರಿಸ್ಥಿತಿ (SOS)",
            "tap_for_help" to "ತಕ್ಷಣದ ಸಹಾಯಕ್ಕಾಗಿ ಇಲ್ಲಿ ಒತ್ತಿ",
            "health_camps" to "ಮುಂಬರುವ ಆರೋಗ್ಯ ಶಿಬಿರಗಳು",
            "log_vitals" to "ಬಿಪಿ ಅಥವಾ ಹೃದಯ ಬಡಿತ ದಾಖಲಿಸಿ",
            "health_trends" to "ಆರೋಗ್ಯದ ಏರಿಳಿತ",
            "recent_records" to "ಇತ್ತೀಚಿನ ದಾಖಲೆಗಳು",
            "medicine_schedule" to "ಔಷಧದ ವೇಳಾಪಟ್ಟಿ",
            "add_med" to "ಹೊಸ ಔಷಧಿ ಸೇರಿಸಿ",
            "asha_calendar" to "ಆಶಾ ಆರೋಗ್ಯ ಕ್ಯಾಲೆಂಡರ್",
            "logout" to "ಭಾಷೆ ಬದಲಿಸಿ / ಮರುಹೊಂದಿಸಿ"
        )
    )

    fun get(key: String, lang: Language): String {
        // Dynamic fetch from Remote Config: e.g. "en_app_name"
        val remoteKey = "${lang.name.lowercase()}_$key"
        val remoteValue = RemoteConfigManager.getString(remoteKey)
        return if (remoteValue.isNotEmpty()) {
            remoteValue
        } else {
            strings[lang]?.get(key) ?: key
        }
    }
}

// ----------------------------
// COLORS & THEME (Elderly Friendly)
// ----------------------------

val TealPrimary = Color(0xFF00796B)
val RedEmergency = Color(0xFFD32F2F)
val GreenSuccess = Color(0xFF388E3C)
val BackgroundGray = Color(0xFFF5F5F5)

@Composable
fun ArogyaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = TealPrimary,
            onPrimary = Color.White,
            background = BackgroundGray,
            surface = Color.White,
            secondary = Color(0xFF004D40)
        ),
        typography = Typography(
            bodyLarge = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
            labelLarge = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
            headlineMedium = androidx.compose.ui.text.TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold)
        ),
        content = content
    )
}

// ----------------------------
// MAIN ACTIVITY
// ----------------------------

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        NotificationHelper.createChannels(this)
        setContent {
            ArogyaTheme {
                ArogyaApp()
            }
        }
    }
}

// ----------------------------
// VIEWMODEL
// ----------------------------

class MainViewModel(
    private val profileRepo: UserProfileRepository,
    private val medicineRepo: MedicineRepository,
    private val vitalRepo: VitalRepository,
    private val ashaRepo: AshaRepository,
    private val reminderManager: ReminderManager
) : ViewModel() {

    var currentLanguage by mutableStateOf(Language.EN)

    val profile = profileRepo.observeProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val medicines = medicineRepo.observeActive()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestVital = vitalRepo.observeLatest()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val last7DaysVitals = vitalRepo.observeLast7()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ashaEvents = ashaRepo.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveProfile(name: String, age: String, conditions: String, cName: String, cPhone: String) {
        viewModelScope.launch {
            profileRepo.save(
                UserProfile(
                    name = name,
                    age = age.toIntOrNull() ?: 0,
                    chronicConditions = conditions,
                    caregiverName = cName,
                    caregiverNumber = cPhone
                )
            )
        }
    }

    fun addMedicine(name: String, dosage: String, morning: Boolean, afternoon: Boolean, night: Boolean) {
        viewModelScope.launch {
            val med = Medicine(
                name = name,
                dosage = dosage,
                morning = morning,
                morningTime = if (morning) "08:00" else null,
                afternoon = afternoon,
                afternoonTime = if (afternoon) "14:00" else null,
                night = night,
                nightTime = if (night) "20:00" else null,
                notes = ""
            )
            val id = medicineRepo.insert(med)
            reminderManager.scheduleMedicineReminders(med.copy(id = id))
            Firebase.analytics.logEvent("medicine_added") {
                param("name", name)
                param("dosage", dosage)
            }
        }
    }

    fun addVital(systolic: String, diastolic: String, heartRate: String) {
        viewModelScope.launch {
            vitalRepo.insert(
                VitalLog(
                    date = LocalDate.now().toString(),
                    systolic = systolic.toIntOrNull() ?: 120,
                    diastolic = diastolic.toIntOrNull() ?: 80,
                    heartRate = heartRate.toIntOrNull() ?: 72
                )
            )
            Firebase.analytics.logEvent("vitals_logged") {
                param("bp", "$systolic/$diastolic")
            }
        }
    }

    fun logSosEvent() {
        Firebase.analytics.logEvent("emergency_sos_triggered") {
            param("language", currentLanguage.name)
        }
    }
}

class MainViewModelFactory(private val app: ArogyaApplication) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            app.userProfileRepository,
            app.medicineRepository,
            app.vitalRepository,
            app.ashaRepository,
            ReminderManager(app)
        ) as T
    }
}

// ----------------------------
// ROOT APP
// ----------------------------

@Composable
fun ArogyaApp() {
    val context = LocalContext.current
    val app = context.applicationContext as ArogyaApplication
    val vm: MainViewModel = viewModel(factory = MainViewModelFactory(app))

    val profile by vm.profile.collectAsStateWithLifecycle()
    var splashDone by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        splashDone = true
    }

    if (!splashDone) {
        SplashScreen(vm.currentLanguage)
    } else {
        if (profile == null) {
            OnboardingScreen(vm, onComplete = { n, a, c, cn, cp ->
                vm.saveProfile(n, a, c, cn, cp)
            })
        } else {
            MainDashboard(profile!!, vm)
        }
    }
}

@Composable
fun SplashScreen(lang: Language) {
    Box(modifier = Modifier.fillMaxSize().background(TealPrimary), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.LocalHospital, "Hospital", tint = Color.White, modifier = Modifier.size(140.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(Strings.get("app_name", lang), color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            Text(Strings.get("tagline", lang), color = Color.White.copy(0.9f), fontSize = 20.sp)
        }
    }
}

@Composable
fun OnboardingScreen(vm: MainViewModel, onComplete: (String, String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var conditions by remember { mutableStateOf("") }
    var caregiverName by remember { mutableStateOf("") }
    var caregiverPhone by remember { mutableStateOf("") }

    val lang = vm.currentLanguage

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { vm.currentLanguage = if (lang == Language.EN) Language.KN else Language.EN }) {
                Text(if (lang == Language.EN) "ಕನ್ನಡ" else "English", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Text(Strings.get("namaste", lang), fontSize = 36.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
        Text(Strings.get("welcome_msg", lang), fontSize = 22.sp)

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(Strings.get("name", lang)) }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp))
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text(Strings.get("age", lang)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp))
        OutlinedTextField(value = conditions, onValueChange = { conditions = it }, label = { Text(Strings.get("conditions", lang)) }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp))
        OutlinedTextField(value = caregiverName, onValueChange = { caregiverName = it }, label = { Text(Strings.get("caregiver_name", lang)) }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp))
        OutlinedTextField(value = caregiverPhone, onValueChange = { caregiverPhone = it }, label = { Text(Strings.get("caregiver_phone", lang)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp))

        Button(
            onClick = { if (name.isNotBlank() && age.isNotBlank()) onComplete(name, age, conditions, caregiverName, caregiverPhone) },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(Strings.get("save_continue", lang), fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboard(profile: UserProfile, vm: MainViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val lang = vm.currentLanguage

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Strings.get("namaste", lang) + " " + profile.name, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TealPrimary, titleContentColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                val iconSize = 32.dp
                NavigationBarItem(selected = selectedTab == 0, onClick = { selectedTab = 0 }, icon = { Icon(Icons.Default.Home, "Home", modifier = Modifier.size(iconSize)) }, label = { Text(Strings.get("home", lang), fontSize = 12.sp) })
                NavigationBarItem(selected = selectedTab == 1, onClick = { selectedTab = 1 }, icon = { Icon(Icons.Default.Medication, "Meds", modifier = Modifier.size(iconSize)) }, label = { Text(Strings.get("meds", lang), fontSize = 12.sp) })
                NavigationBarItem(selected = selectedTab == 2, onClick = { selectedTab = 2 }, icon = { Icon(Icons.Default.ShowChart, "Vitals", modifier = Modifier.size(iconSize)) }, label = { Text(Strings.get("vitals", lang), fontSize = 12.sp) })
                NavigationBarItem(selected = selectedTab == 3, onClick = { selectedTab = 3 }, icon = { Icon(Icons.Default.CalendarMonth, "ASHA", modifier = Modifier.size(iconSize)) }, label = { Text(Strings.get("asha", lang), fontSize = 12.sp) })
                NavigationBarItem(selected = selectedTab == 4, onClick = { selectedTab = 4 }, icon = { Icon(Icons.Default.Person, "Profile", modifier = Modifier.size(iconSize)) }, label = { Text(Strings.get("profile", lang), fontSize = 12.sp) })
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                0 -> HomeContent(vm)
                1 -> MedicineScreen(vm)
                2 -> VitalsScreen(vm)
                3 -> AshaScreen(vm)
                4 -> ProfileScreen(profile, vm)
            }
        }
    }
}

@Composable
fun HomeContent(vm: MainViewModel) {
    val vital by vm.latestVital.collectAsStateWithLifecycle()
    val lang = vm.currentLanguage
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(Strings.get("health_today", lang), color = TealPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                if (vital != null) {
                    Text("BP: ${vital!!.systolic}/${vital!!.diastolic} mmHg", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Heart Rate: ${vital!!.heartRate} bpm", fontSize = 22.sp)
                } else {
                    Text("No vitals logged today.", fontSize = 20.sp)
                }
            }
        }

        Button(
            onClick = { 
                vm.logSosEvent()
                triggerEmergency(context) 
            },
            modifier = Modifier.fillMaxWidth().height(140.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RedEmergency),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(50.dp))
                Text(Strings.get("emergency_sos", lang), fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Text(Strings.get("tap_for_help", lang), fontSize = 16.sp)
            }
        }
        
        Section(title = Strings.get("health_camps", lang), lang = lang) {
            val events by vm.ashaEvents.collectAsStateWithLifecycle()
            if (events.isEmpty()) {
                Text("No upcoming events.")
            } else {
                events.take(2).forEach { event ->
                    Text("• ${event.title}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("  On ${event.date} at ${event.location}", fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

fun triggerEmergency(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vm?.defaultVibrator
    } else {
        @Suppress("DEPRECATION") context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
    vibrator?.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
    Toast.makeText(context, "SOS ALERT SENT!", Toast.LENGTH_LONG).show()
}

@Composable
fun MedicineScreen(vm: MainViewModel) {
    val medicines by vm.medicines.collectAsStateWithLifecycle()
    val lang = vm.currentLanguage
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column {
            Text(Strings.get("medicine_schedule", lang), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(medicines) { med ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(med.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                Text("Take: ${med.dosage}", fontSize = 18.sp, color = TealPrimary)
                                Row(modifier = Modifier.padding(top = 8.dp)) {
                                    if (med.morning) TimeTag("M")
                                    if (med.afternoon) TimeTag("A")
                                    if (med.night) TimeTag("N")
                                }
                            }
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.CheckCircle, "Taken", tint = GreenSuccess, modifier = Modifier.size(48.dp))
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).size(72.dp),
            containerColor = TealPrimary,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, "Add Medicine", modifier = Modifier.size(36.dp))
        }
    }

    if (showAddDialog) {
        AddMedicineDialog(lang, onDismiss = { showAddDialog = false }, onSave = { n, d, m, a, ni -> vm.addMedicine(n, d, m, a, ni); showAddDialog = false })
    }
}

@Composable
fun TimeTag(text: String) {
    Surface(color = TealPrimary.copy(0.1f), shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(end = 8.dp)) {
        Text(text, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
    }
}

@Composable
fun AddMedicineDialog(lang: Language, onDismiss: () -> Unit, onSave: (String, String, Boolean, Boolean, Boolean) -> Unit) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var morning by remember { mutableStateOf(true) }
    var afternoon by remember { mutableStateOf(false) }
    var night by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(Strings.get("add_med", lang), fontSize = 24.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Medicine Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = dosage, onValueChange = { dosage = it }, label = { Text("Dosage") }, modifier = Modifier.fillMaxWidth())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = morning, onCheckedChange = { morning = it }); Text("Morning")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = afternoon, onCheckedChange = { afternoon = it }); Text("Afternoon")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = night, onCheckedChange = { night = it }); Text("Night")
                }
            }
        },
        confirmButton = { Button(onClick = { if(name.isNotBlank()) onSave(name, dosage, morning, afternoon, night) }) { Text("Save") } }
    )
}

@Composable
fun VitalsScreen(vm: MainViewModel) {
    val vitals by vm.last7DaysVitals.collectAsStateWithLifecycle()
    val lang = vm.currentLanguage
    var showAdd by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(Strings.get("health_trends", lang), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
        
        Card(modifier = Modifier.fillMaxWidth().height(280.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(4.dp)) {
            if (vitals.size >= 2) {
                VitalChart(vitals)
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Add 2 or more logs", fontSize = 18.sp)
                }
            }
        }

        Button(onClick = { showAdd = true }, modifier = Modifier.fillMaxWidth().height(64.dp)) {
            Text(Strings.get("log_vitals", lang), fontSize = 20.sp)
        }

        Text(Strings.get("recent_records", lang), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        vitals.forEach { log ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(log.date)
                    Text("${log.systolic}/${log.diastolic} BP", fontWeight = FontWeight.Bold, color = RedEmergency)
                }
            }
        }
    }

    if (showAdd) {
        AddVitalDialog(onDismiss = { showAdd = false }, onSave = { s, d, h -> vm.addVital(s, d, h); showAdd = false })
    }
}

@Composable
fun VitalChart(vitals: List<VitalLog>) {
    val sortedVitals = vitals.sortedBy { it.timestamp }
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return if (index >= 0 && index < sortedVitals.size) sortedVitals[index].date.split("-").last() else ""
                    }
                }
                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val e1 = sortedVitals.mapIndexed { i, l -> Entry(i.toFloat(), l.systolic.toFloat()) }
            val e2 = sortedVitals.mapIndexed { i, l -> Entry(i.toFloat(), l.diastolic.toFloat()) }
            val s1 = LineDataSet(e1, "SYS").apply { color = RedEmergency.toArgb(); lineWidth = 3f }
            val s2 = LineDataSet(e2, "DIA").apply { color = TealPrimary.toArgb(); lineWidth = 3f }
            chart.data = LineData(s1, s2)
            chart.invalidate()
        },
        modifier = Modifier.fillMaxSize().padding(12.dp)
    )
}

@Composable
fun AddVitalDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var sys by remember { mutableStateOf("120") }
    var dia by remember { mutableStateOf("80") }
    var hr by remember { mutableStateOf("72") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Vitals") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = sys, onValueChange = { sys = it }, label = { Text("Systolic BP") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = dia, onValueChange = { dia = it }, label = { Text("Diastolic BP") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = hr, onValueChange = { hr = it }, label = { Text("Heart Rate") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = { Button(onClick = { onSave(sys, dia, hr) }) { Text("Log") } }
    )
}

@Composable
fun AshaScreen(vm: MainViewModel) {
    val events by vm.ashaEvents.collectAsStateWithLifecycle()
    val lang = vm.currentLanguage
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(Strings.get("asha_calendar", lang), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(events) { event ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(event.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("When: ${event.date}", fontSize = 18.sp)
                        Text("Where: ${event.location}", color = TealPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(profile: UserProfile, vm: MainViewModel) {
    val lang = vm.currentLanguage

    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(Strings.get("profile", lang), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
        
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileItem(label = Strings.get("name", lang), value = profile.name)
                ProfileItem(label = Strings.get("age", lang), value = profile.age.toString())
                ProfileItem(label = Strings.get("conditions", lang), value = profile.chronicConditions)
                HorizontalDivider()
                ProfileItem(label = Strings.get("caregiver_name", lang), value = profile.caregiverName ?: "N/A")
                ProfileItem(label = Strings.get("caregiver_phone", lang), value = profile.caregiverNumber)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = { vm.currentLanguage = if (lang == Language.EN) Language.KN else Language.EN },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(if (lang == Language.EN) "ಕನ್ನಡಕ್ಕೆ ಬದಲಿಸಿ" else "Switch to English", fontSize = 18.sp)
        }

        Button(
            onClick = { /* Logic to reset or logout */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(Strings.get("logout", lang), color = Color.White)
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column {
        Text(label, fontSize = 16.sp, color = Color.Gray)
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Section(title: String, lang: Language, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = TealPrimary, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
