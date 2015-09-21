package com.bigjelly.temporun;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;


public class MainActivity extends Activity {

    private Decoder mDecoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int sampleRate = 44100;
        final int minBufferSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);

        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
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
                    InputStream in = new URL("http://218.5.238.156/ws.cdn.baidupcs.com/file/a4faafe8eb86caf35b56d636a9d42c50?bkt=p2-qd-314&xcode=8defc358365529893b7d0db111a577918ad33029379d3321a271ffda32bcb45b&fid=4283643989-250528-583541549892735&time=1441517713&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-PthAkjXBTlf38k5%2BbSMD1ca7z8w%3D&to=lc&fm=Nan,B,T,t&sta_dx=3&sta_cs=1689&sta_ft=mp3&sta_ct=5&fm2=Nanjing,B,T,t&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=1400a4faafe8eb86caf35b56d636a9d42c5012b1193d0000003789a4&sl=69599310&expires=8h&rt=sh&r=719768603&mlogid=2636057181&vuk=-&vbdid=622005031&fin=Nothing%27s%20Gonna%20Change%20My%20Love%20For%20You.mp3&fn=Nothing%27s%20Gonna%20Change%20My%20Love%20For%20You.mp3&slt=pm&uta=0&rtype=1&iv=0&isw=0&wshc_tag=0&wsts_tag=55ebd093&wsid_tag=3b2466f7&wsiphost=ipdbm")
                            .openConnection()
                            .getInputStream();
                    Bitstream bitstream = new Bitstream(in);

                    final int READ_THRESHOLD = 2147483647;
                    int framesReaded = READ_THRESHOLD;

                    Header header;
                    for(; framesReaded-- > 0 && (header = bitstream.readFrame()) != null;) {
                        SampleBuffer sampleBuffer = (SampleBuffer) mDecoder.decodeFrame(header, bitstream);
                        short[] buffer = sampleBuffer.getBuffer();
                        audioTrack.write(buffer, 0, buffer.length);
                        bitstream.closeFrame();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BitstreamException e) {
                    e.printStackTrace();
                } catch (DecoderException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        audioTrack.play();
    }
}
