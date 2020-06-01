package com.example.texttospeechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.LocaleList;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText textet;
    Button speakbtn;
    String text;
    TextView localetv,pitchprog,speedprog;
    SeekBar pitchbar,speedbar;
    TextToSpeech tts;

    Button accentbtn[]=new Button[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textet=findViewById(R.id.textet);
        localetv=findViewById(R.id.localetv);

        pitchbar=findViewById(R.id.pitchbar);
        pitchprog=findViewById(R.id.pitchprog);
;

        speedbar=findViewById(R.id.speedbar);
        speedprog=findViewById(R.id.speedprog);

        accentbtn[0]=findViewById(R.id.uk);
        accentbtn[1]=findViewById(R.id.us);
        accentbtn[2]=findViewById(R.id.chinese);
        accentbtn[3]=findViewById(R.id.french);
        accentbtn[4]=findViewById(R.id.german);
        accentbtn[5]=findViewById(R.id.italian);
        accentbtn[6]=findViewById(R.id.korean);
        accentbtn[7]=findViewById(R.id.taiwanese);
        accentbtn[8]=findViewById(R.id.indian);
        for(int i=0;i<9;i++)
            accentbtn[i].setOnClickListener(this);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {
                    Toast.makeText(getApplicationContext(),"Initialisation Complete",Toast.LENGTH_SHORT).show();
                }
            }
        });

        pitchbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int prog=progress;
                prog-=50;
                double p=1+((double)prog/50);
                pitchprog.setText(String.format("%.1f",p)+"f");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int prog=seekBar.getProgress();
                prog-=50;
                double p=1+((double)prog/50);
                float pr=(float)p;
                tts.setPitch(pr);
            }
        });

        speedbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int prog=progress;
                prog-=50;
                double p=1+((double)prog/50);
                speedprog.setText(String.format("%.1f",p)+"f");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int prog=seekBar.getProgress();
                prog-=50;
                double p=1+((double)prog/50);
                float pr=(float)p;
                tts.setSpeechRate(pr);
            }
        });
    }

    private void startspeaking(String text, final Locale locale) {


        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(final String utteranceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        localetv.setText(locale.getDisplayName());
                        pitchbar.setEnabled(false);
                        speedbar.setEnabled(false);
//                        Toast.makeText(getApplicationContext(),"Started"+utteranceId,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDone(final String utteranceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        localetv.setText("ACCENT");
                        pitchbar.setEnabled(true);
                        speedbar.setEnabled(true);
//                        Toast.makeText(getApplicationContext(),"Completed"+utteranceId,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(final String utteranceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error"+utteranceId,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Bundle params=new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

        tts.setLanguage(locale);
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

    @Override
    public void onClick(View v) {
        String text=textet.getText().toString();
        switch (v.getId())
        {
            case R.id.uk:
                startspeaking(text,Locale.UK);
                break;

            case R.id.us:
                startspeaking(text,Locale.US);
                break;

            case R.id.chinese:
                startspeaking(text,Locale.CHINESE);
                break;

            case R.id.french:
                startspeaking(text,Locale.FRENCH);
                break;

            case R.id.german:
                startspeaking(text,Locale.GERMAN);
                break;

            case R.id.italian:
                startspeaking(text,Locale.ITALIAN);
                break;

            case R.id.korean:
                startspeaking(text,Locale.KOREAN);
                break;

            case R.id.taiwanese:
                startspeaking(text,Locale.TAIWAN);
                break;

            case R.id.indian:
                startspeaking(text,new Locale("en","IN"));
                break;
        }
    }
}
