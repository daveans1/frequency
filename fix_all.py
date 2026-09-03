import os
import re

for root, dirs, files in os.walk('app/src/main/kotlin'):
    for file in files:
        if file.endswith('.kt'):
            path = os.path.join(root, file)
            content = open(path, encoding='utf-8').read()
            original = content
            
            # Fix state logic
            content = re.sub(
                r'(\w+\??\.all\s*\{\s*[^\}]*?\.state\s*==\s*Download\.STATE_QUEUED\s*\|\|\s*[^\}]*?\.state\s*==\s*Download\.STATE_DOWNLOADING)\s*\|\|\s*[^\}]*?\.state\s*==\s*Download\.STATE_COMPLETED\s*\}',
                lambda m: m.group(1).replace('.all', '.any') + ' }',
                content,
                flags=re.DOTALL
            )
            
            # Fix menu text
            old_menu_1 = '''Download.STATE_QUEUED, Download.STATE_DOWNLOADING -> Material3MenuItemData(
            title = { Text(stringResource(R.string.downloading)) },
            description = { Text(stringResource(R.string.download_in_progress_desc)) },'''
            
            new_menu_1 = '''Download.STATE_QUEUED, Download.STATE_DOWNLOADING -> Material3MenuItemData(
            title = { Text("Stop downloading") },
            description = { Text("Stop downloading pending songs") },'''
            
            content = content.replace(old_menu_1, new_menu_1)
            
            # For YouTube menu items
            old_menu_2 = '''Download.STATE_QUEUED, Download.STATE_DOWNLOADING -> {
                                    Material3MenuItemData(
                                        title = { Text(text = stringResource(R.string.downloading)) },'''
            
            new_menu_2 = '''Download.STATE_QUEUED, Download.STATE_DOWNLOADING -> {
                                    Material3MenuItemData(
                                        title = { Text(text = "Stop downloading") },'''
                                        
            content = content.replace(old_menu_2, new_menu_2)

            if content != original:
                print(f'Updated {path}')
                open(path, 'w', encoding='utf-8').write(content)
