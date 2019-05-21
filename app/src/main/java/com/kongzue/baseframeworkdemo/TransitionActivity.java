package com.kongzue.baseframeworkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.SwipeBack;
import com.kongzue.baseframework.util.JumpParameter;

@Layout(R.layout.activity_transition)
@SwipeBack(true)
public class TransitionActivity extends BaseActivity {
    
    private Button btnTransition;
    
    @Override
    public void initViews() {
        btnTransition = findViewById(R.id.btn_transition);
    }
    
    @Override
    public void initDatas(JumpParameter paramer) {
    
    }
    
    @Override
    public void setEvents() {
        btnTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
