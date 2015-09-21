# AudioTrackMp3Player
Sample demo of Android network mp3 music player using AudioTrack and [jlayer](http://www.javazoom.net/javalayer/javalayer.html).

*Extremely simple demo is just for you! `MainActivity` involves all you want.*

## Explanation

1. Include jlayer library into your project.

  *jlayer directory in this demo: /app/libs/jl1.0.1.jar*

2. Create a `Decoder` from [jlayer](http://www.javazoom.net/javalayer/javalayer.html) java library.

        Decoder mDecoder = new Decoder();

3. Build an `InputStream` of your mp3 url source and feed them into `BitStream`.

        InputStream in = new URL("http://icecast.omroep.nl:80/radio1-sb-mp3")
                .openConnection()
                .getInputStream();
        Bitstream bitstream = new Bitstream(in);

4. Create an `AudioTrack` instance.

        final int sampleRate = 44100;
        final int minBufferSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);

        AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize,
                AudioTrack.MODE_STREAM);

5. Decode the mp3 stream by Decoder and feed the PCM product to `AudioTrack`.

        Header header;
        for(; framesReaded-- > 0 && (header = bitstream.readFrame()) != null;) {
            SampleBuffer sampleBuffer = (SampleBuffer) mDecoder.decodeFrame(header, bitstream);
            short[] buffer = sampleBuffer.getBuffer();
            mAudioTrack.write(buffer, 0, buffer.length);
            bitstream.closeFrame();
        }
