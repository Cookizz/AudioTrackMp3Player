# AudioTrackMp3Player
An instruction of creating a simple network mp3 music player for Android using AudioTrack and [jlayer](http://www.javazoom.net/javalayer/javalayer.html).

## Steps

1. Include jlayer library into your project.

  *jlayer directory in this demo: /app/libs/jl1.0.1.jar*

2. Create a jlayer `Decoder` instance.

        Decoder mDecoder = new Decoder();

3. Create a jlayer `BitStream` instance of a given mp3 source.

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

        final int READ_THRESHOLD = 2147483647;
        
        Header header;
        int framesReaded = 0;
        while (framesReaded++ <= READ_THRESHOLD && (header = bitStream.readFrame()) != null) {
            SampleBuffer sampleBuffer = (SampleBuffer) mDecoder.decodeFrame(header, bitStream);
            short[] pcmChunk = sampleBuffer.getBuffer();
            mAudioTrack.write(pcmChunk, 0, pcmChunk.length);
            bitstream.closeFrame();
        }
