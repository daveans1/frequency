path = 'app/src/main/kotlin/com/david/frequency/ui/theme/Theme.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

find_str = '''val DefaultThemeColor = Color(0xFFED5564)'''
replace_str = '''val DefaultThemeColor = Color(0xFFED5564)
val FrequencySignatureColor = Color(0xFF20E0B0)'''

content = content.replace(find_str, replace_str)

find_seed = '''        // Use materialKolor only when a specific seed color is provided
        rememberDynamicColorScheme(
            seedColor = themeColor, // themeColor is guaranteed non-default here
            isDark = darkTheme,
            specVersion = ColorSpec.SpecVersion.SPEC_2025,
            style = if (themeColor.toArgb() == 0xFF000000.toInt()) PaletteStyle.Monochrome else PaletteStyle.TonalSpot
        )'''

replace_seed = '''        // Use materialKolor with the chosen color or fallback to the brand's FrequencySignatureColor
        val effectiveSeed = if (themeColor == DefaultThemeColor) FrequencySignatureColor else themeColor
        rememberDynamicColorScheme(
            seedColor = effectiveSeed,
            isDark = darkTheme,
            specVersion = ColorSpec.SpecVersion.SPEC_2025,
            style = if (effectiveSeed.toArgb() == 0xFF000000.toInt()) PaletteStyle.Monochrome else PaletteStyle.TonalSpot
        )'''

content = content.replace(find_seed, replace_seed)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
