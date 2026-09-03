import os

path = r'app/src/main/baseline-prof.txt'
lines = [
    'HSPLandroidx/compose/runtime/**',
    'HSPLandroidx/compose/ui/**',
    'HSPLandroidx/compose/foundation/**',
    'HSPLandroidx/compose/material3/**',
    'HSPLandroidx/compose/animation/**',
    'HSPLcom/david/frequency/ui/theme/FrequencyColors*',
    'HSPLcom/david/frequency/ui/component/AcousticModifiersKt*',
    'HSPLcom/david/frequency/ui/component/AcousticControlsKt*',
    'HSPLcom/david/frequency/ui/component/FloatingNavKt*',
    'HSPLcom/david/frequency/ui/player/MiniPlayerKt*',
    'HSPLcom/david/frequency/ui/screens/settings/SettingsScreenKt*',
    'HSPLcom/david/frequency/ui/screens/HomeScreenKt*',
    'HSPLcom/david/frequency/ui/screens/LibraryScreenKt*',
    'HSPLcom/david/frequency/ui/screens/recognition/RecognitionScreenKt*'
]

with open(path, 'w', encoding='utf-8', newline='\n') as f:
    f.write('\n'.join(lines) + '\n')
