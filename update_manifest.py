path = 'app/src/main/AndroidManifest.xml'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

new_components = '''        <!-- Lens Widget -->
        <receiver
            android:name=".widget.LensWidgetReceiver"
            android:exported="true"
            android:label="@string/widget_lens">
            <intent-filter>
                <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
                <action android:name="com.david.frequency.widget.lens.PLAY_PAUSE" />
                <action android:name="com.david.frequency.widget.lens.LIKE" />
                <action android:name="com.david.frequency.widget.lens.NEXT" />
                <action android:name="com.david.frequency.widget.lens.PREVIOUS" />
                <action android:name="com.david.frequency.widget.lens.UPDATE_WIDGET" />
                <action android:name="com.david.frequency.widget.lens.TOGGLE_CONTROLS" />
            </intent-filter>

            <meta-data
                android:name="android.appwidget.provider"
                android:resource="@xml/lens_widget_info" />
        </receiver>

        <!-- Google Cast Options Provider -->'''

content = content.replace('        <!-- Google Cast Options Provider -->', new_components)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
