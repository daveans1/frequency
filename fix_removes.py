import os
import re

for root, dirs, files in os.walk('app/src/main/kotlin'):
    for file in files:
        if file.endswith('.kt'):
            path = os.path.join(root, file)
            content = open(path, encoding='utf-8').read()
            original = content
            
            # The pattern to find Download.STATE_DOWNLOADING -> { ... sendRemoveDownload ... }
            pattern = re.compile(r'(Download\.STATE_DOWNLOADING\s*->\s*\{\s*)(?:val\s+downloads\s*=\s*downloadUtil\.downloads\.value\s*)?((?:songs|songs\?|songsList)\.forEach\s*\{\s*song\s*->\s*)DownloadService\.sendRemoveDownload\(\s*context,\s*ExoDownloadService::class\.java,\s*(song\.song\.id|song\.id|it\.id),\s*false\s*\)(\s*\})', re.DOTALL)
            
            # Replaces with a check for state != COMPLETED
            def replacement(m):
                prefix = m.group(1)
                loop = m.group(2)
                song_id = m.group(3)
                suffix = m.group(4)
                # Ensure we have a downloads reference
                return prefix + "val downloads = downloadUtil.downloads.value\n                                        " + loop + f"if (downloads[{song_id}]?.state != Download.STATE_COMPLETED) {{\n                                                DownloadService.sendRemoveDownload(\n                                                    context,\n                                                    ExoDownloadService::class.java,\n                                                    {song_id},\n                                                    false\n                                                )\n                                            }}" + suffix
                
            content = pattern.sub(replacement, content)
            
            if content != original:
                print(f'Updated removes in {path}')
                open(path, 'w', encoding='utf-8').write(content)
