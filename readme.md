# Arogya-Sahaya Local — Complete Build Package for Antigravity

I'll deliver this in two parts as requested:

**PART 1:** Detailed screen-by-screen UI prompts (ready to paste into Antigravity)
**PART 2:** Complete SOP & Workflow Document (formatted as Markdown — convert to PDF using any tool like Pandoc, MS Word "Save as PDF", or online MD-to-PDF converters; instructions at the end)

---

# 📱 PART 1 — DETAILED SCREEN-BY-SCREEN PROMPTS

> **How to use:** Each prompt below describes ONE screen completely. Paste them sequentially into Antigravity (or any code-gen agent) to generate each screen. They are written in implementation-ready language.

---

## 🟢 SCREEN 1 — Splash Screen

**Prompt:**
Create a `SplashActivity` that displays for 1.5 seconds when the app launches. Background color is deep teal `#00796B`. Center the app logo (a simple medical cross with a heart icon, 120dp x 120dp, white tint), with the app name **"Arogya-Sahaya"** below it in white, 32sp, bold. Below that, show the tagline **"Your Health Companion"** in 18sp white. Use a fade-in animation (800ms). After 1.5s, check SharedPreferences for `is_onboarded`. If false → navigate to `OnboardingActivity`. If true → navigate to `MainActivity` (Home Dashboard). The splash must be set as the launcher activity in `AndroidManifest.xml`.

---

## 🟢 SCREEN 2 — Onboarding / Medical Profile Setup

**Prompt:**
Create an `OnboardingActivity` using Jetpack Compose with a vertically scrollable `Column`. Background `#F5F5F5`. At the top show heading **"Welcome! Tell us about you"** (28sp, bold, dark teal `#00796B`). Below this:
- **Name field**: `OutlinedTextField`, label "Your Name", height 64dp, font 20sp, single-line, capitalized words.
- **Age field**: `OutlinedTextField`, label "Your Age", numeric keyboard, max 3 digits, height 64dp.
- **Caregiver Phone Number field**: `OutlinedTextField`, label "Caregiver / Family Phone Number", phone keyboard, mandatory.
- **Chronic Conditions section**: Heading "Select Your Conditions" (22sp). Display large `FilterChip`s (height 56dp, font 18sp) for: Diabetes, Hypertension, Heart Disease, Asthma, Arthritis, Thyroid, Other. Multiple selectable.
- **"Save & Continue" button**: Full-width, height 72dp, deep teal background, white text 22sp bold, rounded corners 12dp.

On click: validate all fields are non-empty. Save the profile to Room DB via `UserProfileRepository.insertProfile()`. Set SharedPreferences `is_onboarded = true`. Navigate to `MainActivity` and finish this activity. Show a `Toast` "Profile saved successfully" on success. Use `OnboardingViewModel` injected via Hilt.

---

## 🟢 SCREEN 3 — Home Dashboard

**Prompt:**
Create `HomeFragment` (or `HomeScreen` Composable) inside `MainActivity` — this is the central hub. Use a `LazyColumn` for vertical scrolling. Background `#FAFAFA`.

**Top App Bar:** Height 72dp, deep teal `#00796B`, displays "Namaste, {UserName}" (22sp, white, bold) on the left and a Settings gear icon on the right.

**Card 1 — Today's Pending Medicines** (elevation 4dp, rounded 16dp, padding 16dp, white background):
- Title "💊 Today's Medicines" (22sp bold, dark text)
- Show a horizontally-scrollable row OR vertical list of pending pills for today. Each item: medicine name (20sp), dosage (18sp), session time (Morning/Afternoon/Night) icon + time. Right side: large green "✓ Taken" button (48dp height) and red "✗ Missed" button.
- If empty, show "No medicines scheduled for today" (18sp gray) and a "+ Add Medicine" button.

**Card 2 — Latest Vital Reading**:
- Title "❤️ Last Vital Reading" (22sp bold)
- Display BP "120/80 mmHg", Heart Rate "72 bpm", Glucose "110 mg/dL" — each in 20sp, with the date below in 14sp gray.
- "Log New Reading" button (full width, 56dp, deep teal).

**Card 3 — Upcoming ASHA Camp**:
- Title "🏥 Next Health Camp" (22sp bold)
- Show date in large 24sp ("15 May 2026"), location below in 18sp ("Village Community Hall"), days remaining badge ("In 3 days").
- "View Calendar" button.

**Card 4 — SOS Emergency Button** (CRITICAL):
- Full width, height 100dp, background red `#D32F2F`, rounded 16dp.
- Centered text "🚨 EMERGENCY SOS" in 28sp bold white.
- On tap: trigger haptic feedback + open `SosConfirmationDialog`.

**Bottom Navigation Bar** (`BottomNavigationView`): 4 items with large 32dp icons + 14sp labels: Home, Medicines, Vitals, Calendar.

Inject `HomeViewModel` via Hilt that exposes `LiveData<HomeDashboardState>` from `MedicineRepository`, `VitalRepository`, and `CampRepository`.

---

## 🟢 SCREEN 4 — Medicine List Screen

**Prompt:**
Create `MedicineListFragment` displaying all saved medications. Top app bar title "My Medicines". Use `RecyclerView` with custom `MedicineAdapter`. Each item card (elevation 2dp, rounded 12dp, margin 8dp):
- Medicine name (22sp bold), dosage (18sp), sessions as colored tags (Morning=yellow, Afternoon=orange, Night=indigo).
- Edit (pencil) and Delete (trash) icons on the right (48dp tap targets).

**Floating Action Button** (FAB): Bottom-right, 72dp, deep teal, white "+" icon. On click → `AddMedicineActivity`.

Empty state: Center illustration + text "No medicines added yet. Tap + to add one." (20sp).

ViewModel uses `MedicineRepository.getAllMedicines()` returning `Flow<List<Medicine>>`. On delete: show confirmation dialog "Delete {name}?", then call `repository.deleteMedicine(med)` and cancel its WorkManager reminder via `WorkManager.getInstance(ctx).cancelUniqueWork("pill_${med.id}")`.

---

## 🟢 SCREEN 5 — Add / Edit Medicine Screen

**Prompt:**
Create `AddMedicineActivity` with a vertically scrollable form. Top app bar with back arrow and title "Add Medicine".
- **Medicine Name field**: 64dp height, label "Medicine Name", 20sp.
- **Dosage field**: 64dp, label "Dosage (e.g., 500 mg)", 20sp.
- **Session selection**: Heading "When to take?" (22sp). Three large checkbox-cards arranged vertically (height 80dp each), each showing icon + session name + default time:
  - ☀️ Morning — 08:00 AM
  - 🌤️ Afternoon — 02:00 PM
  - 🌙 Night — 09:00 PM
- Each card is tappable (toggles selection, color changes to teal when selected). Tapping the time opens `TimePickerDialog` to customize.
- **Notes field** (optional): multiline, 100dp, label "Notes (optional)".
- **"Save Medicine" button**: full width, 72dp, deep teal, 22sp bold.

On save: validate name & dosage non-empty + at least one session selected. Save via `MedicineRepository.insertMedicine(medicine)` which returns the new ID. For each selected session, schedule a WorkManager `PeriodicWorkRequest` with appropriate initial delay (calculate `delayUntilNextOccurrence(hour, minute)`). Tag each work with `pill_${medicineId}_${session}`. Toast "Reminder set for {sessions}" and finish the activity.

---

## 🟢 SCREEN 6 — Full-Screen Medicine Reminder Notification

**Prompt:**
Create `ReminderActivity` launched as a **full-screen intent** when the WorkManager `MedicineReminderWorker` fires. Configure window flags: `FLAG_SHOW_WHEN_LOCKED`, `FLAG_TURN_SCREEN_ON`, `FLAG_KEEP_SCREEN_ON`. Background pulsing red-to-orange gradient. Layout (Compose):
- Top: Large pill icon (120dp).
- Heading: "Time for your Medicine!" (32sp bold white).
- Medicine name (40sp white bold).
- Dosage (28sp white).
- Current time (20sp white).
- Two large action buttons side by side at the bottom (height 100dp):
  - **"✓ TAKEN"** (green `#388E3C`, 26sp white bold)
  - **"✗ MISSED"** (red `#C62828`, 26sp white bold)
- Optional small "Snooze 10 min" text button below.

On Taken: insert `DoseHistory(medicineId, timestamp, status="TAKEN")` via repository, play short success sound, finish activity.
On Missed: insert with status="MISSED", finish activity.
On Snooze: schedule a new one-time `WorkRequest` with 10-min delay.

The notification shown by the worker should also have `setFullScreenIntent(pendingIntent, true)` and `setCategory(NotificationCompat.CATEGORY_ALARM)` with `IMPORTANCE_HIGH` channel. Request `USE_FULL_SCREEN_INTENT` permission in manifest (required for Android 14+).

---

## 🟢 SCREEN 7 — Vital Log Entry Screen

**Prompt:**
Create `AddVitalActivity` form with title "Log Today's Reading". Use a vertical Column.
- **Date display** at top: "Today — {dd MMM yyyy}" in 22sp.
- **Blood Pressure section**: Heading "🩸 Blood Pressure" (22sp bold). Two side-by-side numeric `OutlinedTextField`s: "Systolic" (e.g., 120) and "Diastolic" (e.g., 80), each height 72dp, large 24sp text, numeric keyboard.
- **Heart Rate section**: Heading "❤️ Heart Rate (bpm)". Single numeric field 72dp.
- **Glucose section**: Heading "🍬 Blood Glucose (mg/dL)". Optional decimal field 72dp.
- **"Save Reading" button**: full-width 72dp, deep teal.

Validate ranges: Systolic 70–250, Diastolic 40–150, HR 30–200, Glucose 30–500. Show inline error if out of range. On save: build `VitalLog(date=today, systolic, diastolic, heartRate, glucose)` and insert via `VitalRepository.insert()`. Toast "Reading saved" and finish. Use `VitalViewModel` (Hilt).

---

## 🟢 SCREEN 8 — Vital Trend Chart Screen (7-Day)

**Prompt:**
Create `VitalTrendFragment` showing a 7-day trend using **MPAndroidChart** `LineChart`. Top app bar title "Vital Trends". Add three `Chip` filters at the top: "BP", "Heart Rate", "Glucose" (only one active at a time, default = BP).

Below the chips, show the `LineChart` (height 320dp, 16dp padding):
- X-axis: last 7 days as labels (formatted "Mon", "Tue"...).
- Y-axis: appropriate range per metric.
- For BP, show TWO line datasets in the same chart: Systolic (red `#D32F2F`) and Diastolic (orange `#F57C00`), both with circles + smooth curves + value labels (16sp).
- For Heart Rate: single green line.
- For Glucose: single purple line.
- Enable legend, disable description, enable touch zoom = false, animate Y axis 800ms.

Below the chart, show **statistics card**: "Average BP: 122/81", "Highest: 140/90 on Mon", "Lowest: 110/72 on Wed".

Below that, a vertical list of the last 7 readings (date + values, 18sp).

A FAB at the bottom-right "+" → `AddVitalActivity`.

A "📄 Share Report" button at the bottom (good-to-have) generates a PDF using `PdfDocument` API and shares via `Intent.ACTION_SEND`.

ViewModel uses `VitalRepository.getLast7Days(): Flow<List<VitalLog>>`.

---

## 🟢 SCREEN 9 — ASHA Connect Calendar Screen

**Prompt:**
Create `AshaCalendarFragment` with a simple, elderly-friendly month-view calendar. Top app bar: "ASHA Health Camps".

Use **Kizitonwose CalendarView** library (or a custom `RecyclerView` 7-column grid). Days of the week header in 18sp bold. Day cells 56dp x 56dp, font 20sp. Highlight today with a teal circle. Days with a scheduled camp/visit are highlighted with a small red dot below the date and a light teal background.

Below the calendar (50% screen height), show a `LazyColumn` of upcoming events for the selected date OR for the upcoming month if none selected. Each event card:
- Date (large 22sp bold)
- Event type icon (🏥 Camp / 👩‍⚕️ ASHA Visit)
- Title (20sp): e.g., "General Health Camp"
- Location (18sp gray): e.g., "Panchayat Bhavan"
- Time (16sp): "9:00 AM – 1:00 PM"

Pre-populate the Room `AshaEvent` table on first launch with at least 6 simulated upcoming events spread across the next 60 days (use a `RoomDatabase.Callback` with `onCreate`).

ViewModel exposes `Flow<List<AshaEvent>>` via `AshaRepository`.

(Good-to-have): Schedule a one-time WorkManager notification 24 hours before each event using its date.

---

## 🟢 SCREEN 10 — SOS Emergency Confirmation Dialog & Action

**Prompt:**
On the Home Dashboard SOS button tap, show a `Dialog` (full-screen overlay, semi-transparent black background):
- Title "🚨 Send Emergency Alert?" (28sp bold white on red card).
- Subtitle: "This will contact your caregiver: {caregiverName/number}" (20sp).
- Two large buttons (height 80dp):
  - **"📞 CALL NOW"** (green) — fires `Intent(Intent.ACTION_DIAL, Uri.parse("tel:$caregiverNumber"))` (using `ACTION_DIAL` for safety/simulation; `ACTION_CALL` requires permission).
  - **"💬 SEND WHATSAPP/SMS"** (blue) — fires WhatsApp intent first: `Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$number?text=${URLEncoder.encode(message)}"))`. Fallback to SMS: `Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$number")).putExtra("sms_body", message)`.
- "Cancel" button below.

Pre-composed message: `"EMERGENCY: {UserName} ({Age}) needs help. Conditions: {chronicConditions}. Sent from Arogya-Sahaya app at {timestamp}."`

The button must respond within **2 seconds** of tap. Add `Vibrator` haptic feedback (300ms) on the SOS press.

Read caregiver number from the `UserProfile` table via `UserProfileRepository`. If no number is saved, show "Please add a caregiver number in Settings" with a button to navigate to Settings.

---

## 🟢 SCREEN 11 — Settings / Profile Screen

**Prompt:**
Create `SettingsFragment`. Title "Settings". Vertical `LazyColumn` of clickable rows (each 72dp height, divider between, 20sp text):
1. **My Profile** — opens `EditProfileActivity` (same form as onboarding, pre-filled).
2. **Caregiver Number** — opens dialog to update the phone number.
3. **Notification Sound** — choose from 3 default tones.
4. **Dark Mode** — toggle switch.
5. **Language** — English / Hindi / Kannada (good-to-have).
6. **Medication History** — opens `DoseHistoryActivity` showing scrollable list of all past Taken/Missed doses grouped by date.
7. **Export Data (PDF)** — generates 7-day report PDF.
8. **About** — version info.

Each row: leading icon (32dp) + label + trailing chevron `>`.

---

## 🟢 SCREEN 12 — Medication History Screen

**Prompt:**
Create `DoseHistoryActivity`. Title "Medication History". Use `RecyclerView` grouped by date (use sticky headers). Each row: medicine name (20sp), session, scheduled time, status badge (green "TAKEN" / red "MISSED" / gray "PENDING"), 16sp.

Above the list: summary card showing "This week: 18 taken / 3 missed / 90% adherence" with a small progress bar.

Filter chips at top: All / Taken / Missed / This Week / This Month.

Data source: `DoseHistoryRepository.getAllGroupedByDate(): Flow<Map<String, List<DoseHistory>>>`.

---

# 📄 PART 2 — COMPLETE SOP & WORKFLOW DOCUMENT

> Save the content below as `Arogya_Sahaya_SOP.md`, then convert to PDF using:
> - **Pandoc**: `pandoc Arogya_Sahaya_SOP.md -o Arogya_Sahaya_SOP.pdf --pdf-engine=xelatex`
> - OR open in **VS Code** → install "Markdown PDF" extension → Right-click → "Markdown PDF: Export (pdf)"
> - OR paste into Google Docs / Word and "Save as PDF"
> - OR use online tool: **md-to-pdf.fly.dev**

---

```markdown
# AROGYA-SAHAYA LOCAL
## Standard Operating Procedure (SOP) & Complete Build Specification
### Digital Health Companion for Elderly Users in Rural India
**Version 1.0 | Platform: Android | Language: Kotlin | Timeline: 4 Weeks**

---

## TABLE OF CONTENTS
1. Project Overview
2. Goals & Success Criteria
3. System Architecture (MVVM + Repository + Hilt)
4. Tech Stack & Dependencies
5. Project Structure
6. Database Schema (Room)
7. Frontend Design System
8. Screen-by-Screen Specifications
9. Navigation Flow
10. Reminder Workflow (WorkManager + AlarmManager)
11. Chart Implementation (MPAndroidChart)
12. SOS Emergency Workflow
13. Permissions & Manifest
14. Testing Plan
15. Build & Deployment SOP
16. Acceptance Checklist

---

## 1. PROJECT OVERVIEW

**App Name:** Arogya-Sahaya Local  
**Purpose:** A localised, offline-first medication and wellness tracker for elderly users in rural India. Converts complex prescriptions into simple, time-based reminders, tracks vitals, displays ASHA camp dates, and provides a one-tap SOS to caregivers.

**Target Users:** Elderly (60+) patients in rural India, often with low literacy, on multi-drug chronic regimens.

**Single-User App:** One profile per device install.

---

## 2. GOALS & SUCCESS CRITERIA

| Goal | Acceptance Condition |
|---|---|
| Doze-Mode Alarms | Pill notifications fire even in Doze mode |
| 7-Day Vital Chart | MPAndroidChart renders BP, HR, Glucose for 7 days |
| Elderly UI | Contrast ≥ 4.5:1, font ≥ 18sp, touch ≥ 48dp |
| Repository Pattern | All DB access via Repository — no direct DAO calls in ViewModels |
| Offline | All core features work without internet |
| SOS | Triggers within 2 seconds of tap |
| Reboot Persistence | All alarms reschedule after reboot |
| Crash-Free | Zero crashes on standard flows |

---

## 3. SYSTEM ARCHITECTURE

```
┌──────────────────────────────────────────────────────────┐
│                    UI LAYER (Compose / XML)              │
│   Activities, Fragments, Composables                     │
└────────────────────────┬─────────────────────────────────┘
                         │ observes LiveData / StateFlow
┌────────────────────────▼─────────────────────────────────┐
│              VIEWMODEL LAYER (Hilt-injected)             │
│   HomeViewModel, MedicineViewModel, VitalViewModel...    │
└────────────────────────┬─────────────────────────────────┘
                         │ calls suspend functions
┌────────────────────────▼─────────────────────────────────┐
│         REPOSITORY LAYER (Single source of truth)        │
│   MedicineRepository, VitalRepository, AshaRepository    │
└────────────────────────┬─────────────────────────────────┘
                         │
       ┌─────────────────┼──────────────────┐
       ▼                 ▼                  ▼
┌─────────────┐  ┌──────────────┐  ┌──────────────────┐
│  ROOM DB    │  │ WorkManager  │  │ SharedPreferences│
│  (DAO)      │  │ AlarmManager │  │ (settings)       │
└─────────────┘  └──────────────┘  └──────────────────┘
```

**Pattern:** MVVM + Repository + Hilt DI  
**Rules:**
- ViewModels NEVER touch DAO directly.
- Repository is the only data gateway.
- All DB ops use Kotlin Coroutines (`suspend` + `Flow`).
- All DI via `@Inject` constructors and `@HiltAndroidApp`.

---

## 4. TECH STACK & DEPENDENCIES

```kotlin
// build.gradle (Module: app)
android {
    compileSdk 34
    defaultConfig {
        minSdk 26          // Android 8.0
        targetSdk 34
    }
    buildFeatures { compose true; viewBinding true }
}

dependencies {
    // Core
    implementation "androidx.core:core-ktx:1.12.0"
    implementation "androidx.appcompat:appcompat:1.6.1"
    implementation "com.google.android.material:material:1.11.0"

    // Compose
    implementation platform("androidx.compose:compose-bom:2024.02.00")
    implementation "androidx.compose.material3:material3"
    implementation "androidx.activity:activity-compose:1.8.2"

    // Navigation
    implementation "androidx.navigation:navigation-compose:2.7.7"
    implementation "androidx.navigation:navigation-fragment-ktx:2.7.7"

    // Lifecycle / ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.7.0"

    // Room
    implementation "androidx.room:room-runtime:2.6.1"
    implementation "androidx.room:room-ktx:2.6.1"
    kapt "androidx.room:room-compiler:2.6.1"

    // Hilt
    implementation "com.google.dagger:hilt-android:2.50"
    kapt "com.google.dagger:hilt-android-compiler:2.50"
    implementation "androidx.hilt:hilt-navigation-compose:1.1.0"
    implementation "androidx.hilt:hilt-work:1.1.0"
    kapt "androidx.hilt:hilt-compiler:1.1.0"

    // WorkManager
    implementation "androidx.work:work-runtime-ktx:2.9.0"

    // MPAndroidChart
    implementation "com.github.PhilJay:MPAndroidChart:v3.1.0"

    // Calendar
    implementation "com.kizitonwose.calendar:compose:2.5.0"

    // Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
}
```

---

## 5. PROJECT STRUCTURE

```
com.arogya.sahaya/
├── ArogyaApplication.kt                  // @HiltAndroidApp
├── di/
│   ├── DatabaseModule.kt
│   ├── RepositoryModule.kt
│   └── WorkerModule.kt
├── data/
│   ├── local/
│   │   ├── ArogyaDatabase.kt
│   │   ├── entity/
│   │   │   ├── UserProfile.kt
│   │   │   ├── Medicine.kt
│   │   │   ├── DoseHistory.kt
│   │   │   ├── VitalLog.kt
│   │   │   └── AshaEvent.kt
│   │   └── dao/
│   │       ├── UserProfileDao.kt
│   │       ├── MedicineDao.kt
│   │       ├── DoseHistoryDao.kt
│   │       ├── VitalLogDao.kt
│   │       └── AshaEventDao.kt
│   └── repository/
│       ├── UserProfileRepository.kt
│       ├── MedicineRepository.kt
│       ├── VitalRepository.kt
│       └── AshaRepository.kt
├── ui/
│   ├── splash/SplashActivity.kt
│   ├── onboarding/OnboardingActivity.kt + ViewModel
│   ├── main/MainActivity.kt
│   ├── home/HomeFragment.kt + ViewModel
│   ├── medicine/
│   │   ├── MedicineListFragment.kt
│   │   ├── AddMedicineActivity.kt
│   │   └── MedicineViewModel.kt
│   ├── reminder/ReminderActivity.kt
│   ├── vital/
│   │   ├── AddVitalActivity.kt
│   │   ├── VitalTrendFragment.kt
│   │   └── VitalViewModel.kt
│   ├── asha/AshaCalendarFragment.kt + ViewModel
│   ├── sos/SosDialogFragment.kt
│   ├── settings/SettingsFragment.kt
│   ├── history/DoseHistoryActivity.kt
│   └── theme/ (Color.kt, Type.kt, Theme.kt)
├── worker/
│   ├── MedicineReminderWorker.kt
│   └── BootReceiver.kt
├── notification/
│   └── NotificationHelper.kt
└── util/
    ├── Constants.kt
    ├── DateUtils.kt
    └── Extensions.kt
```

---

## 6. DATABASE SCHEMA (ROOM)

### 6.1 UserProfile
```kotlin
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,        // Always 1 (single user)
    val name: String,
    val age: Int,
    val chronicConditions: String,       // CSV: "Diabetes,Hypertension"
    val caregiverName: String?,
    val caregiverNumber: String,
    val createdAt: Long = System.currentTimeMillis()
)
```

### 6.2 Medicine
```kotlin
@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dosage: String,                  // "500 mg"
    val morning: Boolean,
    val morningTime: String?,            // "08:00"
    val afternoon: Boolean,
    val afternoonTime: String?,          // "14:00"
    val night: Boolean,
    val nightTime: String?,              // "21:00"
    val notes: String?,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
```

### 6.3 DoseHistory
```kotlin
@Entity(tableName = "dose_history")
data class DoseHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medicineId: Int,
    val medicineName: String,
    val session: String,                 // "MORNING" / "AFTERNOON" / "NIGHT"
    val scheduledTime: Long,
    val actualTime: Long?,
    val status: String,                  // "TAKEN" / "MISSED" / "PENDING"
    val date: String                     // "2026-05-07"
)
```

### 6.4 VitalLog
```kotlin
@Entity(tableName = "vital_log")
data class VitalLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,                    // "2026-05-07"
    val timestamp: Long = System.currentTimeMillis(),
    val systolic: Int,
    val diastolic: Int,
    val heartRate: Int,
    val glucose: Float? = null,
    val notes: String? = null
)
```

### 6.5 AshaEvent
```kotlin
@Entity(tableName = "asha_events")
data class AshaEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,                   // "General Health Camp"
    val type: String,                    // "CAMP" or "VISIT"
    val date: String,                    // "2026-05-15"
    val startTime: String,
    val endTime: String,
    val location: String,
    val description: String?
)
```

### 6.6 Database Class
```kotlin
@Database(
    entities = [UserProfile::class, Medicine::class, DoseHistory::class,
                VitalLog::class, AshaEvent::class],
    version = 1, exportSchema = false
)
abstract class ArogyaDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun medicineDao(): MedicineDao
    abstract fun doseHistoryDao(): DoseHistoryDao
    abstract fun vitalLogDao(): VitalLogDao
    abstract fun ashaEventDao(): AshaEventDao
}
```

---

## 7. FRONTEND DESIGN SYSTEM

### 7.1 Color Palette
| Token | Hex | Usage |
|---|---|---|
| Primary Teal | `#00796B` | App bars, primary buttons |
| Primary Light | `#4DB6AC` | Accents |
| Background | `#FAFAFA` | Screen background |
| Surface | `#FFFFFF` | Cards |
| Text Primary | `#212121` | Body text |
| Text Secondary | `#757575` | Labels |
| Success Green | `#388E3C` | Taken / OK |
| Error Red | `#D32F2F` | SOS / Missed |
| Warning Amber | `#F57C00` | Diastolic / Warnings |
| Info Blue | `#1976D2` | Heart Rate |

### 7.2 Typography (all ≥ 18sp)
| Style | Size | Weight |
|---|---|---|
| Display | 32sp | Bold |
| H1 | 28sp | Bold |
| H2 | 22sp | Bold |
| Body Large | 20sp | Regular |
| Body | 18sp | Regular |
| Button | 22sp | Bold |
| Caption | 16sp | Regular |

### 7.3 Spacing & Sizing
- Padding: 16dp default, 24dp for sections
- Card corner radius: 16dp
- Button corner radius: 12dp
- Min touch target: **48dp** (most buttons 64–80dp for elderly)
- Card elevation: 2–4dp

### 7.4 Accessibility
- All `contentDescription` on icons
- TalkBack-compatible
- Support `fontScale` system setting up to 1.3x
- Min contrast 4.5:1 (WCAG AA)
- Optional Dark Mode

---

## 8. SCREEN-BY-SCREEN SPECIFICATIONS

> *(See Part 1 above — all 12 screens fully specified)*

| # | Screen | Activity/Fragment | Purpose |
|---|---|---|---|
| 1 | Splash | `SplashActivity` | Branding + onboarding routing |
| 2 | Onboarding | `OnboardingActivity` | Capture profile + caregiver |
| 3 | Home Dashboard | `HomeFragment` | Today's pills, vitals, camp, SOS |
| 4 | Medicine List | `MedicineListFragment` | Manage all meds |
| 5 | Add/Edit Medicine | `AddMedicineActivity` | Schedule reminders |
| 6 | Reminder Full-Screen | `ReminderActivity` | Doze-safe alarm UI |
| 7 | Add Vital | `AddVitalActivity` | Log BP/HR/Glucose |
| 8 | Vital Trend Chart | `VitalTrendFragment` | 7-day chart |
| 9 | ASHA Calendar | `AshaCalendarFragment` | Camp dates |
| 10 | SOS Dialog | `SosDialogFragment` | Emergency contact |
| 11 | Settings | `SettingsFragment` | Profile + caregiver config |
| 12 | Dose History | `DoseHistoryActivity` | Past records |

---

## 9. NAVIGATION FLOW

```
SplashActivity
    │
    ├─[first launch]─► OnboardingActivity ──► MainActivity
    └─[returning]──────────────────────────► MainActivity
                                                │
                            ┌───────────────────┼─────────────────┐
                            │                   │                 │
                       BottomNav           Home actions       Settings menu
                            │                   │                 │
        ┌───────┬───────┬───┴───┐               │                 │
        ▼       ▼       ▼       ▼               ▼                 ▼
     Home  Medicines Vitals  Calendar      SosDialog      DoseHistory
                │       │                                          
                ▼       ▼                                          
         AddMedicine  AddVital                                     
                              \                                    
                               ► VitalTrendFragment                
                                                                   
WorkManager Trigger ────► ReminderActivity (Full-screen)
```

---

## 10. REMINDER WORKFLOW (WorkManager + AlarmManager)

### 10.1 Strategy
- **WorkManager** is primary (Doze-safe, battery-friendly).
- **AlarmManager** with `setExactAndAllowWhileIdle()` is fallback for exact timing on Android 12+.
- Use `setForegroundAsync()` in worker to ensure execution.

### 10.2 Worker Implementation
```kotlin
@HiltWorker
class MedicineReminderWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
    private val medRepo: MedicineRepository,
    private val notifier: NotificationHelper
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        val medId = inputData.getInt("medicineId", -1)
        val session = inputData.getString("session") ?: return Result.failure()
        val med = medRepo.getById(medId) ?: return Result.failure()

        notifier.showReminderNotification(med, session)   // Full-screen intent
        return Result.success()
    }
}
```

### 10.3 Scheduling Logic
```kotlin
fun scheduleReminder(medId: Int, session: String, hour: Int, minute: Int) {
    val delay = calculateDelayUntilNext(hour, minute)
    val data = workDataOf("medicineId" to medId, "session" to session)
    
    val request = PeriodicWorkRequestBuilder<MedicineReminderWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(data)
        .addTag("pill_${medId}_$session")
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "pill_${medId}_$session",
        ExistingPeriodicWorkPolicy.UPDATE,
        request
    )
}
```

### 10.4 Boot Persistence
```kotlin
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Trigger one-time worker that re-schedules all active medicines
            WorkManager.getInstance(ctx).enqueue(
                OneTimeWorkRequestBuilder<RescheduleAllWorker>().build()
            )
        }
    }
}
```
Manifest: `<receiver android:name=".worker.BootReceiver" android:exported="true">` with `RECEIVE_BOOT_COMPLETED` permission.

### 10.5 Notification Channel
```kotlin
val channel = NotificationChannel(
    "medicine_reminders", "Medicine Reminders",
    NotificationManager.IMPORTANCE_HIGH
).apply {
    description = "Alerts for medicine doses"
    enableVibration(true)
    enableLights(true)
    setSound(soundUri, audioAttributes)
}
```
Notification uses `setFullScreenIntent()` + `setCategory(CATEGORY_ALARM)`.

---

## 11. CHART IMPLEMENTATION (MPAndroidChart)

```kotlin
fun setupBpChart(chart: LineChart, data: List<VitalLog>) {
    val systolicEntries = data.mapIndexed { i, v -> Entry(i.toFloat(), v.systolic.toFloat()) }
    val diastolicEntries = data.mapIndexed { i, v -> Entry(i.toFloat(), v.diastolic.toFloat()) }

    val sysSet = LineDataSet(systolicEntries, "Systolic").apply {
        color = Color.parseColor("#D32F2F")
        circleRadius = 6f; lineWidth = 3f
        valueTextSize = 14f; mode = LineDataSet.Mode.CUBIC_BEZIER
    }
    val diaSet = LineDataSet(diastolicEntries, "Diastolic").apply {
        color = Color.parseColor("#F57C00")
        circleRadius = 6f; lineWidth = 3f
        valueTextSize = 14f; mode = LineDataSet.Mode.CUBIC_BEZIER
    }

    chart.data = LineData(sysSet, diaSet)
    chart.xAxis.valueFormatter = IndexAxisValueFormatter(data.map { formatDay(it.date) })
    chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    chart.xAxis.textSize = 14f
    chart.axisLeft.textSize = 14f
    chart.axisRight.isEnabled = false
    chart.description.isEnabled = false
    chart.legend.textSize = 16f
    chart.animateY(800)
    chart.invalidate()
}
```

---

## 12. SOS EMERGENCY WORKFLOW

```
[User taps SOS] → [Vibrate 300ms] → [Show Confirm Dialog]
       │
       ├─[CALL]──► Intent(ACTION_DIAL, "tel:$caregiver")
       │
       └─[MESSAGE]──► WhatsApp deep-link → fallback SMS Intent
                       Body: "EMERGENCY: {name}({age}) needs help. 
                              Conditions: {conditions}. Time: {now}"
```
- Response time: ≤ 2 seconds (measure with `System.currentTimeMillis()`)
- If caregiver number not set → redirect to Settings.

---

## 13. PERMISSIONS & MANIFEST

```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
<uses-permission android:name="android.permission.USE_FULL_SCREEN_INTENT"/>
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
<uses-permission android:name="android.permission.VIBRATE"/>
<uses-permission android:name="android.permission.SEND_SMS"/>
<uses-permission android:name="android.permission.WAKE_LOCK"/>
<!-- ACTION_DIAL needs no permission; ACTION_CALL would need CALL_PHONE -->
```
Request `POST_NOTIFICATIONS` at runtime on Android 13+. Request `SCHEDULE_EXACT_ALARM` via Settings intent on Android 12+.

---

## 14. TESTING PLAN

| Test | Method |
|---|---|
| Doze Mode | `adb shell dumpsys deviceidle force-idle` then wait for reminder |
| Reboot Persistence | `adb reboot` → verify alarms via `adb shell dumpsys jobscheduler` |
| Offline | Toggle airplane mode → all features must function |
| Accessibility | Enable TalkBack + 130% font scale → navigate full app |
| Crash | Manual run-through of all 12 screens twice |
| SOS Latency | Stopwatch from tap to dialog visible (< 2s) |
| 7-Day Chart | Insert 7 sample readings → verify chart renders correctly |
| Repository | Code review: grep for `dao.` calls outside repository — must be zero |

---

## 15. BUILD & DEPLOYMENT SOP

### Phase 1 — Setup (Day 1–2)
1. Create Android Studio project (Empty Activity, Compose).
2. Add all dependencies in `build.gradle`.
3. Set up Hilt: `ArogyaApplication`, `@HiltAndroidApp`, `DatabaseModule`, `RepositoryModule`.
4. Create folder structure as specified.

### Phase 2 — Database (Day 3–4)
1. Implement all 5 entities + DAOs.
2. Implement `ArogyaDatabase` with `RoomDatabase.Callback` to seed `AshaEvent` data.
3. Implement all Repository classes.
4. Unit test DAOs.

### Phase 3 — UI Foundation (Day 5–7)
1. Create theme, colors, typography.
2. Build Splash + Onboarding.
3. Build MainActivity + BottomNavigation.
4. Build Home Dashboard skeleton.

### Phase 4 — Core Features (Day 8–14)
1. Medicine List + Add/Edit Medicine.
2. WorkManager scheduler + Worker + Notification.
3. Full-screen ReminderActivity.
4. Vital Log + Trend Chart (MPAndroidChart).
5. ASHA Calendar.

### Phase 5 — Emergency & Settings (Day 15–18)
1. SOS Dialog + intents.
2. Settings + Dose History.
3. Boot receiver + reschedule worker.

### Phase 6 — Polish & QA (Day 19–28)
1. Accessibility audit.
2. Doze mode test.
3. Reboot test.
4. Crash testing.
5. Generate signed APK.

---

## 16. ACCEPTANCE CHECKLIST

- [ ] App launches in < 3s
- [ ] Onboarding captures name, age, conditions, caregiver number
- [ ] Home shows pending pills, last vital, next camp, SOS
- [ ] Medicine reminder fires in Doze mode
- [ ] Full-screen notification shows name + dosage
- [ ] Taken/Missed buttons update DoseHistory
- [ ] Vital Log saves to Room DB
- [ ] 7-day chart renders BP, HR, Glucose
- [ ] ASHA calendar shows ≥ 4 upcoming events
- [ ] SOS triggers within 2 seconds
- [ ] SOS calls / sends WhatsApp / SMS to caregiver
- [ ] WorkManager reschedules after reboot
- [ ] All data stored locally (no internet)
- [ ] All fonts ≥ 18sp, touch targets ≥ 48dp
- [ ] Contrast ≥ 4.5:1
- [ ] Repository Pattern enforced
- [ ] Hilt DI throughout
- [ ] Zero crashes on standard flows

---

**"Simple technology, serious impact — because every life deserves a reliable guardian."**

*Document prepared for Antigravity AI build pipeline | Arogya-Sahaya Local v1.0 | 2026*
```

---

## 🎯 HOW TO GIVE THIS TO ANTIGRAVITY

1. **Save the SOP** above as `Arogya_Sahaya_SOP.md`.
2. **Convert to PDF** using one of these:
   - **Easiest:** Open [https://md-to-pdf.fly.dev](https://md-to-pdf.fly.dev) → paste content → download.
   - **VS Code:** Install "Markdown PDF" extension → right-click file → Export PDF.
   - **Pandoc CLI:** `pandoc Arogya_Sahaya_SOP.md -o SOP.pdf`
3. **Upload to Antigravity** along with the PDF you provided originally.
4. **Use this prompt to Antigravity:**
   > "Build the complete Arogya-Sahaya Local Android app using the attached SOP document. Generate the entire project including all 12 screens, Room database, repositories, ViewModels with Hilt, WorkManager-based reminders, MPAndroidChart vital trends, ASHA calendar, and SOS emergency feature. Follow MVVM + Repository Pattern strictly. The app must be fully offline-capable and elderly-accessible. Begin with project setup, then database, then UI screens in the order specified."
5. **Then paste each screen prompt from Part 1 individually** if Antigravity needs more granular generation.

This package is ready for a complete one-shot generation. Good luck with your build! 🚀