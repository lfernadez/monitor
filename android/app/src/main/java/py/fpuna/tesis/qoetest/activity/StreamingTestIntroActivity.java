package py.fpuna.tesis.qoetest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.fragment.StartStreamingTestFragment;
import py.fpuna.tesis.qoetest.fragment.StreamingIntroDosFragment;
import py.fpuna.tesis.qoetest.fragment.StreamingIntroFragment;

public class StreamingTestIntroActivity extends FragmentActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private Button siguienteBtn;
    private Button atrasBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_test_intro);

        mPager = (ViewPager) findViewById(R.id.pagerStreamingIntro);
        mPagerAdapter = new IntroPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        atrasBtn = (Button) findViewById(R.id.leftButton);
        atrasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() == 0) {
                    finish();
                }else{
                    if(mPager.getCurrentItem() == (NUM_PAGES -1)){
                        siguienteBtn.setText("SIGUIENTE");
                    }
                    mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                }
            }
        });

        siguienteBtn = (Button) findViewById(R.id.rightButton);
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() == (NUM_PAGES - 1)) {
                    Intent intent = new Intent(getBaseContext(),
                            StreamingTestActivity.class);
                    intent.putExtras(getIntent().getExtras());
                    startActivity(intent);
                } else {
                    if (mPager.getCurrentItem() + 1 == (NUM_PAGES - 1)) {
                        siguienteBtn.setText("COMENZAR TEST");
                    }
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                }
            }
        });
    }

    private class IntroPagerAdapter extends FragmentPagerAdapter {

        public IntroPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return StreamingIntroFragment.newInstance();
                case 1:
                    return StreamingIntroDosFragment.newInstance();
                case 2:
                    return StartStreamingTestFragment.newInstance();
                default:
                    return StreamingIntroFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
