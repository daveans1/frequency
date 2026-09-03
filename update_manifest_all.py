path = 'app/src/main/AndroidManifest.xml'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

new_components = '''        <!-- Identify Widget -->
        <receiver
            android:name=".widget.IdentifyWidgetReceiver"
            android:exported="true"
            android:label="@string/widget_identify">
            <intent-filter>
                <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
            </intent-filter>
            <meta-data
                android:name="android.appwidget.provider"
                android:resource="@xml/identify_widget_info" />
        </receiver>
        
        <activity
            android:name=".widget.RecognitionWidgetActivity"
            android:theme="@style/Theme.Translucent.NoTitleBar"
            android:excludeFromRecents="true"
            android:exported="false" />

        <!-- Mood Grid Widget -->
        <receiver
            android:name=".widget.MoodGridWidgetReceiver"
            android:exported="true"
            android:label="@string/widget_mood">
            <intent-filter>
                <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
                <action android:name="com.david.frequency.widget.PLAY_MOOD" />
            </intent-filter>
            <meta-data
                android:name="android.appwidget.provider"
                android:resource="@xml/mood_grid_widget_info" />
        </receiver>

        <activity
            android:name=".widget.MoodGridConfigActivity"
            android:exported="true"
            android:theme="@style/Theme.Widget.frequency">
            <intent-filter>
                <action android:name="android.appwidget.action.APPWIDGET_CONFIGURE" />
            </intent-filter>
        </activity>

        <!-- Google Cast Options Provider -->'''

content = content.replace('        <!-- Google Cast Options Provider -->', new_components)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
