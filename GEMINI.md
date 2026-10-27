# Frequency App — AI Assistant Rules

## Project Identity
- App name: **Frequency**
- Package: `com.david.frequency`
- Owner: Dave (`daveans1` on GitHub)
- This is a fork of ViVi Music (which itself forked Metrolist).
- Language: **Kotlin only**. UI: **Jetpack Compose + Material 3**.
- Build system: Gradle (Kotlin DSL).

---

## Architecture — Know Before Editing
- **MVVM** pattern: Screens → ViewModels → Repository/Database
- **Hilt** is used for dependency injection everywhere. Never instantiate dependencies manually.
- **Room** is the local database. Never write raw SQL outside of DAOs.
- **MusicService.kt** is the core playback engine (Media3/ExoPlayer). It is extremely large and sensitive — be very conservative with edits here.
- The `vivimusic/` sub-package contains Dave's custom additions (updater, changelog, commit screen). Do not confuse this with the base ViVi codebase.
- All GitHub URLs and repo config live in a single source of truth: `app/src/main/kotlin/com/david/frequency/constants/GithubConfig.kt`. Never hardcode GitHub URLs elsewhere.

---

## Editing Rules

### 1. Read Before You Write
- Before editing any file, read the relevant file(s) fully to understand existing patterns.
- Check how similar things are already done elsewhere in the codebase before introducing new patterns.

### 2. Make Surgical Edits
- Only change the specific lines needed. Do NOT rewrite entire files or functions unless explicitly asked.
- Preserve all existing comments, annotations (`@Composable`, `@HiltViewModel`, `@Singleton`, etc.), and doc strings.
- Never remove code that wasn't directly part of the requested change.

### 3. Kotlin & Compose Standards
- Follow existing code style: use `val` over `var` wherever possible, use Kotlin idioms (`.let`, `.also`, `when`, etc.).
- All new UI must be written as `@Composable` functions, consistent with the existing Compose patterns.
- New ViewModels must use `@HiltViewModel` and `@Inject constructor`.
- New singletons/dependencies must be provided via a Hilt `@Module`, not manually instantiated.

### 4. Database Changes Require Migrations
- If you modify any Room `@Entity` (add/remove/rename a field), you MUST add a corresponding migration in `MusicDatabase.kt`.
- Never change a database entity without also bumping the database version and writing the migration. Failing to do this will crash the app on existing installs.

### 5. Never Touch These Without Being Explicitly Asked
- `MusicService.kt` — extremely complex, changes here can break all playback.
- `MusicDatabase.kt` migrations — wrong migrations corrupt user data.
- `GithubConfig.kt` — changing this breaks the OTA updater.
- `proguard-rules.pro` — changes here can break release builds.
- `AndroidManifest.xml` — only edit if strictly required for a feature.
- Any `*_v2.kt` files (e.g., `Player_v2.kt`, `Lyrics_v2.kt`) — these are newer rewrites; prefer editing these over the originals if both exist.

### 6. Build Flavors
- The app has two variant flavors: `foss` (default, no Google Play Services) and `gms` (with Cast).
- Features that require Google Play Services must be guarded with `BuildConfig.CAST_AVAILABLE`.
- Do not add Google-specific dependencies to the `foss` flavor.

### 7. String Resources
- All user-facing text must go in `app/src/main/res/values/strings.xml`, not hardcoded in Kotlin/Compose files.
- Reference strings via `stringResource(R.string.your_key)` in Compose.

### 8. Committing Changes
- After making edits, always confirm with Dave before staging and committing.
- Use clear, descriptive commit messages (e.g., `fix: correct updater API URL`, `feat: add sleep timer to player menu`).
- Never force-push to `main`.

### 9. RAM & Low-End Phone Optimization
- **Target Low-Spec Hardware**: The app must stay responsive and lightweight on low-end phones (2GB–4GB RAM, entry-level SoCs, Android Go).
- **Zero Allocations in Composition**: Never create heavy objects, formatters, brushes, or regexes inside Composable bodies without wrapping them in `remember`.
- **Bitmap & Memory Discipline**: Never hold unmanaged `Bitmap` instances or unmetered in-memory caches. Let Coil handle downsampling and disk caching within bounded memory budgets.
- **Avoid Heavy GPU Blurs/Shaders**: Multi-pass runtime `RenderEffect` blurs and complex live mesh shaders trigger severe GPU lag and RAM spikes on low-tier chipsets. Always favor lightweight translucent/tinted surfaces.
- **Coroutines & Scopes**: No `GlobalScope`. Collect StateFlow/SharedFlow with lifecycle awareness (`collectAsStateWithLifecycle` or `repeatOnLifecycle`) so processing stops when the UI is hidden.

### 10. Mandatory Documentation Synchronization (Doc-Sync Rule)
- Whenever making any major change (e.g., modifying architecture, adding a new screen/feature, altering Room entities/migrations, adding/removing dependencies, or changing build flavors/config):
  - You MUST update `ARCHITECTURE.md` to reflect the updated map, entities, or tech stack.
  - You MUST update `README.md` if user-facing features or repo links changed.
  - You MUST update `changelog.json` if bumping versions.
- Never complete a major task without ensuring docs match the code.

---

## Key File Reference
| File | Purpose |
|------|---------|
| `app/build.gradle.kts` | Version, SDK, build config, flavors |
| `constants/GithubConfig.kt` | All GitHub URLs (single source of truth) |
| `constants/PreferenceKeys.kt` | All DataStore preference keys |
| `playback/MusicService.kt` | Core ExoPlayer engine — handle with care |
| `db/MusicDatabase.kt` | Room DB + all migrations |
| `di/AppModule.kt` | Hilt providers |
| `ui/theme/FrequencyColors.kt` | Custom color tokens — use these, not hardcoded colors |
| `vivimusic/updater/frequencyupdater.kt` | OTA updater UI + logic |
