# Instructions
There's no need to copy the demo code, instead, I recommend you to read the instructions below to get a better grasp.

1. Include jlayer library into your project.

  *jlayer directory in this demo: /app/libs/jl1.0.1.jar*

2. Create a jlayer `Decoder` instance.

        Decoder decoder = new Decoder();

3. Create a jlayer `BitStream` instance of a given mp3 source.

        InputStream mp3Source = new URL("your network mp3 source")
                .openConnection()
                .getInputStream();
        Bitstream bitStream = new Bitstream(mp3Source);

4. Create an `AudioTrack` instance.

        final int sampleRate = 44100;
        final int minBufferSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize,
                AudioTrack.MODE_STREAM);

5. Decode the mp3 `BitStream` data by `Decoder` and feed the outcoming PCM chunks to `AudioTrack`.

        final int READ_THRESHOLD = 2147483647;
        
        Header frame;
        int framesReaded = 0;
        while (framesReaded++ <= READ_THRESHOLD && (frame = bitStream.readFrame()) != null) {
            SampleBuffer sampleBuffer = (SampleBuffer) decoder.decodeFrame(frame, bitStream);
            short[] pcmChunk = sampleBuffer.getBuffer();
            audioTrack.write(pcmChunk, 0, pcmChunk.length);
            bitstream.closeFrame();
        }
