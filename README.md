# AudioTrackMp3Player
Sample demo of Android network mp3 music player using AudioTrack and jlayer.

*Extremely simple demo is just for you! `MainActivity` involves all you want.*

## Explanation

1. Create a `Decoder` from jlayer library. (jlayer is an mp3 library for java platform)

        Decoder mDecoder = new Decoder();

2. Build an `InputStream` of your mp3 source and feed them into `BitStream`.

        InputStream in = new URL("http://icecast.omroep.nl:80/radio1-sb-mp3")
                .openConnection()
                .getInputStream();
        Bitstream bitstream = new Bitstream(in);

3. Create an `AudioTrack` instance.

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

4. Decode the mp3 stream by Decoder and feed the PCM product to `AudioTrack`.

        Header header;
        for(; framesReaded-- > 0 && (header = bitstream.readFrame()) != null;) {
            SampleBuffer sampleBuffer = (SampleBuffer) mDecoder.decodeFrame(header, bitstream);
            short[] buffer = sampleBuffer.getBuffer();
            mAudioTrack.write(buffer, 0, buffer.length);
            bitstream.closeFrame();
        }
