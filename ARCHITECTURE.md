# Frequency App — Architecture Overview

## 🧬 Lineage
```
Metrolist (open-source Android music player)
    └─▶ ViVi Music (vivimusicGroup/vivimusic — renamed, heavily restyled)
            └─▶ Frequency (daveans1/frequency — Dave's fork, rebranded again)
```
> Dave cloned ViVi (which itself was a Metrolist fork), renamed the app to **Frequency**, changed the package name to `com.david.frequency`, and is actively developing it as his own project.

---

## 📦 Package & Identity
| Property | Value |
|----------|-------|
| Package name | `com.david.frequency` |
| App name | Frequency |
| Version | 6.0.7 (versionCode 76) |
| Min SDK | 26 (Android 8) |
| Target SDK | 37 |
| Language | Kotlin (100%) |
| UI framework | Jetpack Compose + Material 3 |
| Build system | Gradle (Kotlin DSL) |

---

## 🏗️ Architecture Pattern
**MVVM + Clean Architecture** with Hilt DI

```
UI Layer (Compose Screens)
    │
    ▼
ViewModels (state holders, coroutine scopes)
    │
    ▼
Repository / Database Layer (Room)
    │
    ▼
Data Sources:
    ├── Local: Room DB (MusicDatabase / InternalDatabase)
    ├── Remote: YouTube / YT Music via innertube module
    ├── Lyrics: Multiple providers (see below)
    └── External APIs: LastFM, JioSaavn, Spotify import, Discord RPC
```

---

## 📁 Source Module Map
```
app/src/main/kotlin/com/david/frequency/
│
├── api/                    — HTTP API helpers
├── constants/              — App-wide constants
│   └── GithubConfig.kt     — Centralized GitHub repo config (owner=daveans1, repo=frequency)
├── db/                     — Room database
│   ├── MusicDatabase.kt    — Main DB wrapper
│   ├── daos/               — DAO interfaces
│   └── entities/           — DB entities (Song, Album, Artist, Playlist, Lyrics, etc.)
├── di/                     — Hilt dependency injection modules
├── eq/                     — Equalizer (custom audio processing)
│   ├── audio/              — DSP audio processors (BiquadFilter, ParametricEQ, etc.)
│   └── data/               — EQ profiles
├── extensions/             — Kotlin extension functions
├── listentogether/         — P2P listen-together feature (synced playback with friends)
├── lyrics/                 — Lyrics system
│   └── providers: BetterLyrics, KuGou, LrcLib, Musixmatch, PaxSenix, SimpMusic, YouLyPlus, YouTube, Subtitle
├── models/                 — Domain models
├── playback/               — Core music engine
│   ├── MusicService.kt     — Main MediaLibraryService (ExoPlayer, 3630 lines!)
│   ├── audio/              — Audio processors (Resampler, SilenceDetector)
│   └── queues/             — Queue types (ListQueue, YouTubeQueue, PlaylistQueue, etc.)
├── recognition/            — Shazam-style music recognition
├── ui/
│   ├── component/          — Reusable composables (custom UI kit)
│   ├── menu/               — Context menus (Song, Album, Artist, Playlist, Queue menus)
│   ├── player/             — Player screens (Player.kt, Player_v2.kt, MiniPlayer, Canvas)
│   ├── screens/            — All app screens
│   │   ├── artist/         — Artist + artist albums/songs screens
│   │   ├── equalizer/      — EQ screen (Axion EQ)
│   │   ├── library/        — Local library (Songs, Albums, Artists, Playlists, Mix)
│   │   ├── playlist/       — Playlist screens
│   │   ├── recognition/    — Music recognition UI
│   │   ├── search/         — Search (online + local + suggestions)
│   │   ├── settings/       — Settings (Appearance, Player, Content, Lyrics, Privacy, etc.)
│   │   └── wrapped/        — "Spotify Wrapped"-style yearly recap feature
│   ├── theme/              — FrequencyColors.kt, Theme.kt, Type.kt
│   └── utils/              — UI utilities
├── utils/                  — General utilities (DataStore prefs, Network, Download, PoToken)
├── viewmodels/             — Shared ViewModels
├── vivimusic/              — Dave's custom additions (kept the "vivimusic" sub-namespace)
│   ├── changelog/          — In-app changelog screen
│   ├── commitscreen/       — Shows GitHub commits in-app
│   ├── component/          — Extra UI components
│   ├── shapes/             — Custom shapes (RoundedStarShape, etc.)
│   └── updater/            — OTA updater system
│       ├── frequencyupdater.kt       — Main updater UI + logic
│       ├── downloadmanager/          — Background download worker
│       └── FrequencyNotificationProvider.kt
└── widget/                 — Home screen widgets (Music, Mood Grid, Turntable, Identify)
```

---

## 🎵 Core Playback Stack
- **ExoPlayer** (Media3) — audio engine
- **MediaLibraryService** — Android media session integration
- **ResolvingDataSource** — Resolves YouTube stream URLs on-the-fly
- **CacheDataSource** — Caches streams locally (configurable size)
- **OkHttp** — HTTP for stream fetching + API calls
- **innertube** module — YouTube Music API (separate Gradle module)
- **PoToken** — Bot-detection mitigation for YouTube

---

## 🗄️ Database (Room)
Key entities:
| Entity | Purpose |
|--------|---------|
| `SongEntity` | Cached songs (metadata) |
| `AlbumEntity` / `ArtistEntity` | Albums and artists |
| `PlaylistEntity` + `PlaylistSongMap` | User playlists |
| `LyricsEntity` | Cached lyrics |
| `FormatEntity` | Stream format/quality cache |
| `Event` | Play history events |
| `PlayCountEntity` | Per-song play counts (for Wrapped) |
| `RecognitionHistory` | Shazam history |
| `SearchHistory` | Search history |
| `SetVideoIdEntity` | YouTube video ID mappings |
| `SpeedDialItem` | Speed dial shortcuts |

---

## 🔌 External Integrations
| Service | Purpose |
|---------|---------|
| YouTube / YT Music | Primary streaming source (via innertube) |
| LastFM | Scrobbling |
| Discord | Rich Presence (RPC) |
| JioSaavn | Alternative streaming |
| Spotify | Playlist import only |
| Apple Music Scraper | Canvas artwork |
| DeepL / Mistral / OpenRouter | Lyrics translation / AI features |
| Shazam | Music recognition (VibraSignature) |
| Kizzy | Discord integration helper |
| Crowdin | Translations (crowdin.yml) |

---

## 🔄 OTA Updater System
- Checks **`daveans1/frequency`** GitHub releases via API
- Supports both stable releases and nightly builds (via GitHub Actions)
- Centralized config: `GithubConfig.kt`
- Downloads APK in background via `UpdateDownloadWorker` (WorkManager)

---

## 🏭 Build Flavors
| Dimension | Flavors |
|-----------|---------|
| `variant` | `foss` (default, F-Droid compatible, no GMS) · `gms` (Google Cast support) |
| `abi` | `universal` · `arm64` · `armeabi` · `x86` |

---

---

## 🎨 Theme Engine & Design System
- **Dual-Mode Architecture**:
  - `FrequencyDarkColorScheme`: Deep Obsidian and Charcoal surfaces with high-contrast typography and subtle glass borders.
  - `FrequencyLightColorScheme`: Luminous Porcelain and Crisp White surfaces with obsidian typography, slate sub-labels, and subtle frost borders.
  - `pureBlack`: AMOLED true black override for ultra-high battery efficiency on OLED displays.
  - `dynamicColor`: Android 12+ wallpaper Monet extraction via `dynamicLightColorScheme` / `dynamicDarkColorScheme`.
  - Custom Seed Colors: Integrated with `materialKolor` for generating coherent custom light and dark palettes from arbitrary hue seeds.
- **Acoustic Glassmorphism**:
  - `adaptiveAcousticGlass`: Dynamically switches between frosted porcelain and deep acoustic glass based on theme mode.
  - Uses hairline borders (`GlassBorder` / `GlassBorderLight`) and single-pass translucent surface fills, avoiding heavy multi-pass GPU blurs.

---

## 🧭 Navigation Shell — "Sonic Dock"
- **Unified Acoustic Capsule**:
  - Single floating pill container (`FloatingNav.kt`) replacing legacy split-pill and rigid docked layouts.
  - Houses Search, Home, Library, and Explore in an integrated capsule with fluid animated indicator pills.
  - Adaptive styling: seamlessly transitions across Light Mode, Dark Mode, and Pure Black AMOLED.
  - Enabled by default via `FloatingNavBarKey` with backward-compatible toggle to compact navigation bar in Appearance Settings.

---

## ⚡ RAM & Low-Spec Performance Architecture
Designed specifically to safeguard budget devices (2GB–4GB RAM, Android Go editions):
- **Zero Allocations in Composition**: Brushes, formatters, and heavy objects are strictly wrapped in `remember`.
- **Bitmap & Memory Discipline**: No raw unmetered Bitmaps; Coil manages downsampling and bounded disk/memory caches.
- **Lightweight GPU Footprint**: Eliminates multi-pass `RenderEffect` runtime blurs and GPU-heavy shaders in favor of shallow Composable trees and alpha-tinted surfaces.
- **Lifecycle-Safe Scopes**: All flows collected with lifecycle awareness (`collectAsStateWithLifecycle` / `repeatOnLifecycle`) so processing suspends when UI is hidden.

---

## 📝 Key Files to Know
| File | Role |
|------|------|
| `constants/GithubConfig.kt` | All GitHub links / updater config |
| `playback/MusicService.kt` | Core playback engine (3600+ lines) |
| `db/MusicDatabase.kt` | Room database with migrations |
| `vivimusic/updater/frequencyupdater.kt` | In-app OTA updater |
| `ui/theme/FrequencyColors.kt` | Dual-mode color tokens (Dark + Luminous Light) |
| `ui/theme/Theme.kt` | Dynamic dual-mode theme engine |
| `ui/component/FloatingNav.kt` | Unified Sonic Dock navigation capsule |
| `ui/screens/settings/AppearanceSettings.kt` | Appearance Hub (Theme Mode, Navigation, Themes) |
| `di/AppModule.kt` | Hilt DI providers |
| `app/build.gradle.kts` | Build config, version, flavors, API keys |
