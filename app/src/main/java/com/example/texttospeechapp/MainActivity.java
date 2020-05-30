package com.example.texttospeechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.LocaleList;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText textet;
    Button speakbtn;
    String text;
    TextView localetv;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textet=findViewById(R.id.textet);
        speakbtn=findViewById(R.id.speakbtn);
        localetv=findViewById(R.id.localetv);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {
//                    tts.setPitch(0.5f);
//                    tts.setSpeechRate(0.8f);
                }
            }
        });

        speakbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=textet.getText().toString();
                Set<Locale> availableLanguages = tts.getAvailableLanguages();
                /*for(Locale locale:availableLanguages)
                {
                    if(locale.getDisplayLanguage().equals("English"))
                    Log.e("Locale:",locale.getDisplayLanguage()+" "+locale.getDisplayCountry()+" "+locale.toString());

                }*/
                startspeaking(text,Locale.CHINESE);

            }
        });
    }

    private void startspeaking(String text,Locale locale) {


        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(final String utteranceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Started"+utteranceId,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDone(String utteranceId) {
                Toast.makeText(getApplicationContext(),"Completed"+utteranceId,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String utteranceId) {
                Toast.makeText(getApplicationContext(),"Error"+utteranceId,Toast.LENGTH_SHORT).show();
            }
        });

        Bundle params=new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

        tts.setLanguage(locale);
        localetv.setText(locale.getDisplayName());
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,params,"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts!=null)
        {
            tts.stop();
            tts.shutdown();
        }
    }
}
