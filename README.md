# AudioTrackMp3Player
An instruction of creating a simple network mp3 music player for Android using AudioTrack and [jlayer](http://www.javazoom.net/javalayer/javalayer.html).

## Steps

1. Include jlayer library into your project.

  *jlayer directory in this demo: /app/libs/jl1.0.1.jar*

2. Create a jlayer `Decoder` instance.

        Decoder mDecoder = new Decoder();

3. Create an InputStream of your mp3 url source and feed it to a `BitStream`.

        InputStream in = new URL("http://icecast.omroep.nl:80/radio1-sb-mp3")
                .openConnection()
                .getInputStream();
        Bitstream bitStream = new Bitstream(in);

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

5. Decode the mp3 `BitStream` data by `Decoder` and feed the outcoming PCM chunks to `AudioTrack`.

        Header header;
        while (framesReaded-- > 0 && (header = bitStream.readFrame()) != null) {
            SampleBuffer sampleBuffer = (SampleBuffer) mDecoder.decodeFrame(header, bitStream);
            short[] buffer = sampleBuffer.getBuffer();
            mAudioTrack.write(buffer, 0, buffer.length);
            bitstream.closeFrame();
        }
