"""Regenerate the public edition's three original, synthesized UI/game sounds."""
from pathlib import Path
import math
import struct
import wave

RATE = 22050
DEST = Path(__file__).resolve().parents[1] / 'assets'


def write(name, duration, frequency, overtones=0.15):
    samples = []
    count = int(duration * RATE)
    for i in range(count):
        t = i / RATE
        envelope = min(1.0, t / 0.006) * (1 - i / count) ** 2
        hz = frequency(t / duration)
        value = math.sin(2 * math.pi * hz * t)
        value += overtones * math.sin(4 * math.pi * hz * t)
        samples.append(struct.pack('<h', int(14000 * envelope * value)))
    with wave.open(str(DEST / name), 'wb') as output:
        output.setnchannels(1)
        output.setsampwidth(2)
        output.setframerate(RATE)
        output.writeframes(b''.join(samples))


if __name__ == '__main__':
    DEST.mkdir(exist_ok=True)
    write('click.wav', 0.075, lambda t: 720 - 180 * t)
    write('clear.wav', 0.28, lambda t: 440 + 440 * t)
    write('hit.wav', 0.20, lambda t: 150 - 85 * t, overtones=0.4)
