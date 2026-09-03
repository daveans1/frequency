path = 'app/src/main/kotlin/com/david/frequency/widget/RecognitionWidgetActivity.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('import android.app.Activity', 'import androidx.activity.ComponentActivity')
content = content.replace('class RecognitionWidgetActivity : Activity() {', 'class RecognitionWidgetActivity : ComponentActivity() {')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
