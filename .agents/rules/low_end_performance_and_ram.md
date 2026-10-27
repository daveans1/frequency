---
trigger: always_on
---
# Rule: Low-End Phone Support & RAM Preservation

## Core Directive
Frequency must run smoothly on budget and low-end Android devices (devices with 2GB–4GB RAM, entry-level SoCs, and Android Go editions). Never write code, animations, or memory structures that can cause high memory pressure, frequent Garbage Collection (GC) thrashing, Out-Of-Memory (OOM) crashes, or dropped frames.

## 1. Memory (RAM) Discipline
- **No Context/View Leaks**: Never leak `Activity`, `Context`, or Composable state in singletons, static objects, or background callbacks. Always use `ApplicationContext` where long-lived context is required.
- **Strict Coroutine Scoping**: Never use `GlobalScope`. Always bind coroutines to `viewModelScope`, `lifecycleScope`, or Compose `LaunchedEffect` / `rememberCoroutineScope`.
- **Lifecycle-Aware State Collection**: Always collect flows using lifecycle-safe scopes (`collectAsStateWithLifecycle` or `repeatOnLifecycle`) so collection terminates when the UI is backgrounded or disposed.
- **No Unbounded In-Memory Caches**:
  - Never maintain custom static or ViewModel lists/maps that grow without eviction limits.
  - Never hold unmanaged `Bitmap` objects in memory. Let Coil handle bitmap downsampling and caching bounded by hardware memory limits.
- **Database Queries**: Never load unbounded Room database lists into memory at once. Use pagination or bounded limits for tracks, history, and playlists.

## 2. Jetpack Compose Allocation & Recomposition Hygiene
- **Zero Allocations in Composition**: Never instantiate heavy objects, formatters (`SimpleDateFormat`, `DecimalFormat`), regex patterns, or gradient brushes inside Composable bodies without `remember`.
- **Derived State**: Use `derivedStateOf` when observing rapidly mutating states (e.g., audio slider position, scroll offsets) to prevent recomposition on every frame.
- **Lazy List Keys**: Always provide a stable, unique `key` parameter to `items()` in `LazyColumn` and `LazyRow` to prevent continuous item reconstruction while scrolling.

## 3. GPU & Rendering Load
- **Avoid Heavy Blur & Shader Stacking**: Runtime `RenderEffect` blurs and complex dynamic mesh shaders severely tax lower-end GPUs and spike RAM. Always favor clean, lightweight frosted acrylic/solid surfaces with subtle alpha over multi-pass hardware blurs.
- **Flat Layout Hierarchy**: Keep Composable trees shallow. Avoid redundant nested `Box` and `Surface` wrappers that induce heavy overdraw.
- **Animation Metering**: Continuous animations (e.g. rotating thumbnails, waveform pulses, shimmer effects) must pause or stop when off-screen or when playback is paused.
