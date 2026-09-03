path = r'C:\Users\david\.gemini\antigravity\brain\b36611a1-19ca-44f8-8a11-5c32790dc209\implementation_plan.md'
with open(path, 'w', encoding='utf-8') as f:
    f.write('''# Comprehensive UI/UX Redesign: "Acoustic Glass & Electric Frequency"

Redesign the entire interface of Frequency to match the brand identity, modern vibe, and electric cyan/abyssal midnight palette of the logo, transforming the app from a generic Material utility into an audiophile music experience.

---

## User Review Required

> [!IMPORTANT]
> **Staged Execution Strategy**:
> This redesign touches the foundational design system, the primary navigation dock, all 4 core screens, and the Settings console.
> To ensure continuous build stability and immediate testability, execution is split into 4 clear phases:
> 1. **Phase 1: Design Tokens & Foundations** (Color system, acoustic glass modifier, typography & glow tokens)
> 2. **Phase 2: The Settings "Acoustic Console" Overhaul** (Bento grid modules, live telemetry banner, tactile controls)
> 3. **Phase 3: Floating Acoustic Navigation Dock & Mini-Player** (Frosted glass floating capsule, waveform progress)
> 4. **Phase 4: Core Music Screens** (Pulse/Home, Now Playing Deck, Radar/Search, Vault/Library)
> 
> We are ready to begin with **Phase 1 & Phase 2** immediately upon your approval.

---

## Proposed Changes

Grouped by layer and executed in strict dependency order:

### Foundation Layer (Phase 1)

#### [NEW] [FrequencyColors.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/theme/FrequencyColors.kt)
- Define the brand palette derived directly from the logo:
  - DeepAbyss (#0A0E1A) — primary window background
  - MidnightSurface (#121829) — elevated card surface
  - AcousticGlass (#161E38 @ 75% opacity) — frosted containers
  - ElectricAqua (#20E0B0) — primary accent, active indicators, playheads
  - SonicCyan (#3DF5FC) — frequency peaks, radar ripples, audio badges
  - SonicWhite (#F5F7FF) — high-contrast typography

#### [NEW] [AcousticModifiers.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/component/AcousticModifiers.kt)
- Reusable Compose modifiers for:
  - cousticGlass(blurRadius: Dp, alpha: Float, strokeWidth: Dp): hardware-accelerated frosted glass with subtle cyan border sheen
  - 
eonGlow(color: Color, radius: Dp): radial luminescence for active playheads and indicators
  - udioShimmer(): 120 BPM rhythmic wave shimmer for skeleton loading states

#### [MODIFY] [Theme.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/theme/Theme.kt)
- Integrate FrequencyColors into ivimusicTheme
- Ensure dynamic Material You continues to work if toggled, while setting "Electric Frequency" as the default signature brand theme

---

### Settings "Acoustic Console" Layer (Phase 2)

#### [MODIFY] [SettingsScreen.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/screens/settings/SettingsScreen.kt)
- Replace flat list with the **Audiophile Telemetry Strip**:
  - Displays live output audio codec (LDAC • 990kbps / USB DAC / Speaker), buffer health, and active DSP profile
- Implement the **2×2 Bento Console Grid**:
  - Module 1: Audio Engine & DSP Core
  - Module 2: Visual Stage & Theming
  - Module 3: Cloud Streams & Connected Accounts
  - Module 4: The Vault & Storage Partitions
- Retain the fast instant search bar with search registry integration

#### [NEW] [AcousticControls.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/component/AcousticControls.kt)
- **Illuminated Power Pod**: Hardware-style toggle with cyan LED indicator dot
- **Precision Audio Fader**: Tactile slider with step graduation and haptic ticks

---

### Navigation & Dock Layer (Phase 3)

#### [MODIFY] [FloatingNav.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/component/FloatingNav.kt) / [AppNavigation.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/component/AppNavigation.kt)
- Re-engineer the navigation bar into a true **Floating Acoustic Dock**:
  - Elevated 16dp above bottom edge with 32dp pill curvature
  - Real-time frosted glass backdrop blur (color-surface-glass)
  - Glowing indicator dot beneath active tab icon
- Redesign the Mini-Player into an integrated acoustic capsule above the dock

---

### Core Music Screens (Phase 4)

#### [MODIFY] [HomeScreen.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/screens/HomeScreen.kt) ("Pulse")
- Hero Soundwave Spotlight stage with dynamic vinyl artwork card
- Snapping horizontal mood pills carousel
- 2-column Quick Picks stack with mini artwork and swipe gestures

#### [MODIFY] [PlayerScreen.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/screens/PlayerScreen.kt) ("Frequency Deck")
- 300dp artwork canvas with ambient luminescent halo blur
- Interactive Waveform Seekbar replacing generic slider
- Expandable frosted glass karaoke lyric drawer

#### [MODIFY] [SearchScreen.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/screens/search/SearchScreen.kt) ("Sonic Radar")
- Integrated concentric listening radar button powered by ShazamKit
- Dual-tone angular gradient genre cards

#### [MODIFY] [LibraryScreen.kt](file:///c:/Users/david/frequency/app/src/main/kotlin/com/david/frequency/ui/screens/library/LibraryScreen.kt) ("The Vault")
- 2×2 Mood Launch Matrix linking to custom playlists
- Lossless/downloaded audio quality badge chips

---

## Verification Plan

### Automated Compilation
- Run ./gradlew :app:compileUniversalGmsDebugKotlin after each phase to guarantee 100% build integrity and zero compiler regressions.

### Manual & Visual Verification
- Verify high contrast and legibility across all screens in OLED dark mode.
- Verify smooth 60/120fps scrolling with frosted glass modifiers.
- Test Settings navigation flow and ensure all existing features (Audio quality, Lyrics, EQ, Accounts) remain fully functional under the new Bento layout.
''')
