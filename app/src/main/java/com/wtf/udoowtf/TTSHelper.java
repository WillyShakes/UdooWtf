package com.wtf.udoowtf;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.List;
import java.util.Locale;

/**
 * Before you can use this class you need to install a Text-to-speech engine on your android device.
 */
public class TTSHelper {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextToSpeech tts;
    private Context mContext;

    public TTSHelper(Context context) {
        TextToSpeech.OnInitListener ttsListener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        };

        tts = new TextToSpeech(context, ttsListener );
        tts.setLanguage(Locale.ITALY);
        List<TextToSpeech.EngineInfo> engines = tts.getEngines();
        if (engines.size() > 0) {
            for (int i=0; i < engines.size(); i++) {
                if (engines.get(i).name.contains("tts")) {
                    Log.d(TAG, "engines: " + engines.get(0).name);
                    Log.d(TAG, "size engines: " + engines.size());
                    String engineName = engines.get(1).name;
                    tts = new TextToSpeech(context, ttsListener, engineName);
                }
            }
        }

    }

    public void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
