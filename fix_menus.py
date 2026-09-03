import sys
content = open('app/src/main/kotlin/com/david/frequency/ui/menu/PlaylistScreenMenus.kt', encoding='utf-8').read()
old_menu = '''        Download.STATE_QUEUED, Download.STATE_DOWNLOADING -> Material3MenuItemData(
            title = { Text(stringResource(R.string.downloading)) },
            description = { Text(stringResource(R.string.download_in_progress_desc)) },
            icon = {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            },
            onClick = {
                onDownload()
                onDismiss()
            }
        )'''
new_menu = '''        Download.STATE_QUEUED, Download.STATE_DOWNLOADING -> Material3MenuItemData(
            title = { Text("Stop downloading") },
            description = { Text("Stop downloading pending songs") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.close),
                    contentDescription = null
                )
            },
            onClick = {
                onDownload()
                onDismiss()
            }
        )'''
content = content.replace(old_menu, new_menu)
open('app/src/main/kotlin/com/david/frequency/ui/menu/PlaylistScreenMenus.kt', 'w', encoding='utf-8').write(content)
