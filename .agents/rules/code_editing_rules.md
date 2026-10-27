# Frequency App — Code Editing Standards & Safety Rules

## 1. Golden Rule: Read & Investigate First
- **Never guess or assume code structure.** Always read the existing file and its imports before editing.
- Look for how similar features are implemented across the repo before writing new logic.

## 2. Surgical, Minimal Edits
- Modify **only** the lines required to satisfy the request.
- **Never** rewrite an entire file, class, or function when only a few lines need tweaking.
- Preserve existing comments, code formatting, imports, annotations (`@Composable`, `@HiltViewModel`, `@Inject`, `@Singleton`), and license headers.
- Never delete or "clean up" unrelated code unless explicitly instructed.

## 3. High-Risk Files — DO NOT Touch Without Explicit Confirmation
- `playback/MusicService.kt` — Core media playback engine. Sensitive to timing, lifecycle, and thread issues.
- `db/MusicDatabase.kt` & DAO/Entity files — Changing Room entities without a version bump and corresponding migration will crash the app.
- `constants/GithubConfig.kt` — Single source of truth for URLs and updates.
- `app/build.gradle.kts` — Dependency versions and build flavors (`foss` vs `gms`).
- `proguard-rules.pro` & `AndroidManifest.xml`.

## 4. Architecture & Coding Patterns
- **Architecture**: MVVM with Jetpack Compose (Material 3).
- **Dependency Injection**: Use **Hilt** (`@HiltViewModel`, `@Inject constructor(...)`, `@Module`, `@Provides`). Never instantiate dependencies or viewmodels manually.
- **Strings & Resources**: Never hardcode user-facing strings in UI code. Add them to `res/values/strings.xml` and call `stringResource(R.string...)`.
- **Colors & Theming**: Never hardcode hex colors in Composable screens. Use `MaterialTheme.colorScheme` or `FrequencyColors`.
- **Null Safety**: Leverage Kotlin's `?.let`, `?:`, `when`, and exhaustive pattern matching. Prefer immutable `val` over `var`.
- **v2 Files**: When modifying screens that have both an original and a `_v2` version (e.g., `Player_v2.kt`), focus on the `_v2` version unless told otherwise.

## 5. RAM & Low-End Phone Optimization
- **Target Low-Spec Hardware**: The app must remain responsive on devices with 2GB–4GB RAM and Android Go.
- **Zero Allocations in Composition**: Never instantiate heavy objects, formatters, brushes, or regex patterns inside Composable bodies without wrapping them in `remember`.
- **Bitmap & Memory Discipline**: Never keep raw `Bitmap` instances or unbounded in-memory caches. Let Coil handle bitmap downsampling and memory cache eviction within hardware limits.
- **Avoid Heavy GPU Blurs/Shaders**: Multi-pass runtime `RenderEffect` blurs and complex live mesh shaders trigger severe GPU bottlenecks and RAM spikes on low-tier chipsets. Always favor lightweight translucent/tinted surfaces.
- **Coroutines & Scopes**: No `GlobalScope`. Collect StateFlow/SharedFlow with lifecycle awareness (`collectAsStateWithLifecycle` or `repeatOnLifecycle`) so work stops when the UI is hidden.

## 6. Verification Before Submitting
- Check for unimported classes, missing dependencies, or syntax errors.
- Confirm changes comply with both `foss` (no Google Play Services) and `gms` build variants.

## 7. Mandatory Documentation Synchronization (Doc-Sync Rule)
- Whenever making any major change (architecture changes, new screens, database migrations/entities, dependency additions, or build flavor changes):
  - You **MUST** update `ARCHITECTURE.md` to keep the architecture map and technical specs current.
  - You **MUST** update `README.md` if user-facing features or download/repo links are changed.
  - You **MUST** update `changelog.json` if a version is bumped.
- Always confirm documentation is in sync before concluding a task.
