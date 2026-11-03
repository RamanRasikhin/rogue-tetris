"""Build the Windows ZIP using the checksum-pinned official Temurin JRE."""
from pathlib import Path
import hashlib
import json
import shutil
import urllib.request
import zipfile

ROOT = Path(__file__).resolve().parents[1]


def main():
    config = json.loads((ROOT / 'tools/windows-runtime.json').read_text(encoding='utf8'))
    dist = ROOT / 'dist'
    dist.mkdir(exist_ok=True)
    downloaded = dist / 'temurin-jre.zip'
    urllib.request.urlretrieve(config['url'], downloaded)
    actual = hashlib.sha256(downloaded.read_bytes()).hexdigest()
    if actual != config['sha256']:
        raise RuntimeError('Temurin runtime SHA-256 mismatch')
    unpacked = dist / 'runtime-extracted'
    unpacked.mkdir(exist_ok=True)
    with zipfile.ZipFile(downloaded) as archive:
        for name in archive.namelist():
            target = (unpacked / name).resolve()
            if not target.is_relative_to(unpacked.resolve()):
                raise RuntimeError('Invalid runtime ZIP path')
        archive.extractall(unpacked)
    runtime = next(p for p in unpacked.iterdir() if (p / 'bin/java.exe').exists())
    package = dist / 'RogueTetris-Windows-x64'
    package.mkdir(exist_ok=True)
    shutil.copytree(runtime, package / 'runtime', dirs_exist_ok=True)
    shutil.copy2(ROOT / 'lwjgl3/build/libs/rogue-tetris-1.0.1.jar', package)
    shutil.copy2(ROOT / 'start.bat', package)
    shutil.copy2(ROOT / 'CREDITS.md', package)
    shutil.copy2(ROOT / 'tools/windows-runtime.json', package / 'RUNTIME-SOURCE.json')
    (package / 'README.txt').write_text(
        'RogueTetris — Windows x64\n\n'
        'Extract the entire ZIP, then double-click start.bat.\n'
        'Java is included under runtime/; no separate installation is required.\n'
        'Do not start the game from inside the ZIP viewer.\n'
        'If startup fails, see logs/launch.log.\n\n'
        'Sources and credits: https://github.com/RamanRasikhin/rogue-tetris\n'
        'Temurin runtime legal notices and licenses are preserved under runtime/.\n',
        encoding='utf8')
    output = dist / 'RogueTetris-Windows-x64.zip'
    with zipfile.ZipFile(output, 'w', zipfile.ZIP_DEFLATED) as archive:
        for path in package.rglob('*'):
            if path.is_file() and 'logs' not in path.relative_to(package).parts:
                archive.write(path, Path(package.name) / path.relative_to(package))
    print(output)


if __name__ == '__main__':
    main()
