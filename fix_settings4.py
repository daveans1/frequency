path = r'app/src/main/kotlin/com/david/frequency/ui/screens/settings/AppearanceSettings.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

import re

# Move import android.app.Activity to the top
content = content.replace('import android.app.Activity\nfun AppearanceSettings(', '@Composable\nfun AppearanceSettings(')
if 'import android.app.Activity' not in content:
    content = content.replace('import androidx.compose.foundation.layout.*', 'import android.app.Activity\nimport androidx.compose.foundation.layout.*')

# Remove the broken GridItemSize.Normal
content = content.replace('GridItemSize.Normal', 'GridItemSize.SMALL')

# Remove the bad font icon
content = content.replace('icon = painterResource(R.drawable.text_font),', 'icon = null,')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
