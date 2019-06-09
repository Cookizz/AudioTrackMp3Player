package com.bigjelly.temporun;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import java.io.InputStream;
import java.net.URL;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;


public class MainActivity extends Activity {

    private Decoder mDecoder;
    private AudioTrack mAudioTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int sampleRate = 44100;
        final int minBufferSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize,
                AudioTrack.MODE_STREAM);

        mDecoder = new Decoder();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = new URL("http://icecast.omroep.nl:80/radio1-sb-mp3")
                            .openConnection()
                            .getInputStream();
                    Bitstream bitstream = new Bitstream(in);

                    final int READ_THRESHOLD = 2147483647;
                    int framesReaded = 0;

                    Header header;
                    for(; framesReaded++ <= READ_THRESHOLD && (header = bitstream.readFrame()) != null;) {
                        SampleBuffer sampleBuffer = (SampleBuffer) mDecoder.decodeFrame(header, bitstream);
                        short[] buffer = sampleBuffer.getBuffer();
                        mAudioTrack.write(buffer, 0, buffer.length);
                        bitstream.closeFrame();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        mAudioTrack.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mAudioTrack.stop();
    }
}
