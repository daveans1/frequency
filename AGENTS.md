# Frequency App — AI Assistant Rules

## Project Identity
- App name: **Frequency**
- Package: `com.david.frequency`
- Owner: Dave (`daveans1` on GitHub)
- Fork chain: ViVi Music (forked from Metrolist)
- Tech Stack: **Kotlin**, **Jetpack Compose + Material 3**, **Hilt DI**, **Room Database**, **Media3 / ExoPlayer**

---

## 1. Golden Rules of Editing
1. **Always Inspect First**: Never invent or guess classes, functions, or imports. Read the relevant source files before proposing or writing code.
2. **Surgical Edits Only**: Make minimal, targeted diffs. Do NOT overwrite whole files, functions, or Composable components when only a few lines need changes.
3. **Preserve Everything Else**: Keep existing comments, license notices, annotations (`@Composable`, `@HiltViewModel`, `@Inject`, `@Singleton`), and imports intact.
4. **Never Refactor Unasked**: Only touch what Dave specifically asked you to fix or build. Do not "clean up" or restructure working code.

---

## 2. High-Risk Files (DO NOT Touch Without Explicit Instruction)
- `playback/MusicService.kt` — Core media playback engine. Edits here easily break audio playback, background service, or lockscreen controls.
- `db/MusicDatabase.kt` & Room `@Entity` files — Modifying entities requires an explicit Room Migration and schema bump; otherwise existing user data crashes on startup.
- `constants/GithubConfig.kt` — Master source of truth for the OTA updater, release tags, and repo endpoints.
- `app/build.gradle.kts` & `gradle/libs.versions.toml` — Build dependencies and flavors (`foss` vs `gms`).
- `proguard-rules.pro` & `AndroidManifest.xml`.

---

## 3. Architecture & Style Patterns
- **MVVM**: UI in `@Composable` screens → State/Events in `@HiltViewModel` → Data in Repository / Room DAO.
- **Hilt Dependency Injection**: Always use `@Inject constructor(...)` and `@Module` providers. Never manually instantiate dependencies.
- **Strings**: Put all user-facing strings into `res/values/strings.xml` and call `stringResource(R.string...)`.
- **Theming**: Use `MaterialTheme.colorScheme` or custom tokens from `FrequencyColors`. Do not hardcode raw hex values.
- **Kotlin Idioms**: Prefer `val` over `var`. Use null-safe scopes (`.let`, `?:`, `run`).
- **v2 Screens**: When a screen has both an original and a `_v2` version (e.g. `Player_v2.kt`), work on the `_v2` version.

---

## 4. RAM & Low-End Phone Optimization
- **Target Low-Spec Hardware**: The app must remain smooth and lightweight on low-end phones (2GB–4GB RAM, Android Go).
- **Zero Allocations in Composition**: Never instantiate heavy objects, formatters, brushes, or regexes inside Composable bodies without wrapping them in `remember`.
- **Bitmap & Cache Restraint**: Never store raw `Bitmap` instances or unmetered in-memory caches. Let Coil handle downsampling and disk caching within bounded memory budgets.
- **Avoid Heavy GPU Blurs/Shaders**: Multi-pass runtime `RenderEffect` blurs and complex live mesh shaders trigger severe GPU lag and RAM spikes on low-tier chipsets. Always favor lightweight translucent/tinted surfaces.
- **Coroutines & Scopes**: No `GlobalScope`. Collect StateFlow/SharedFlow with lifecycle awareness (`collectAsStateWithLifecycle` or `repeatOnLifecycle`) so processing stops when the UI is hidden.

---

## 5. Mandatory Documentation Synchronization (Doc-Sync Rule)
- Whenever a major change is made (architectural shifts, new screens/features, database schema/migration changes, new dependencies, or updated build flavors/endpoints):
  - You **MUST** update `ARCHITECTURE.md` to keep the module map and architectural details accurate.
  - You **MUST** update `README.md` if user-facing features or download/repo links change.
  - You **MUST** update `changelog.json` when version numbers change.
- Never finalize a task without syncing documentation.
