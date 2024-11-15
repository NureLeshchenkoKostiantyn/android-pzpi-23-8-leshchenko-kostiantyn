package com.example.labtask21;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
private View colorP;
private SeekBar seekBarR, seekBarG, seekBarB;
private LinearLayout layout1, linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        colorP = findViewById(R.id.colorP);
        seekBarR = findViewById(R.id.seekBarR);
        seekBarG = findViewById(R.id.seekBarG);
        seekBarB = findViewById(R.id.seekBarB);
        linearLayout = findViewById(R.id.linearLayout);
        layout1 = findViewById(R.id.layout1);
        configureLayoutForOrientation();

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekBarR.setOnSeekBarChangeListener(listener);
        seekBarG.setOnSeekBarChangeListener(listener);
        seekBarB.setOnSeekBarChangeListener(listener);
        updateColor();
    }
    private void updateColor(){
        int red = seekBarR.getProgress();
        int green = seekBarG.getProgress();
        int blue = seekBarB.getProgress();
        int color = Color.rgb(red, green, blue);
        colorP.setBackgroundColor(color);
    }

    private void configureLayoutForOrientation() {

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layout1.setOrientation(LinearLayout.HORIZONTAL);


            colorP.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 2));
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        } else {
            layout1.setOrientation(LinearLayout.VERTICAL);


            colorP.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 2));
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        }
    }


}
