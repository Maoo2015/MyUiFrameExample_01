package com.example.myui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myui.Item.IconItem;
import com.example.myui.Item.MenuItem;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BorderMenuActivity extends FragmentActivity {

    final static long ANIMATION_DURATION  = 500;

    LinearLayout topContainer;
    LinearLayout leftContainer;

    Drawable menuIconTop;
    Drawable menuIconCenter;
    Drawable menuIconBottom;

    ImageView imageTop;
    ImageView imageCenter;
    ImageView imageBottom;

    private boolean menuShowed;

    private ArrayList<MenuItem> topItems = new ArrayList<MenuItem>();
    private ArrayList<IconItem> leftItems = new ArrayList<IconItem>();
    private HashMap<Integer, MenuItem> items = new HashMap<Integer, MenuItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_border_menu);

        initMenuIcon();
        initMenuButton();
        initMenus();

    }

    private void initMenuIcon() {
        menuIconTop = getResources().getDrawable(R.drawable.icn_menu_top);
        menuIconCenter = getResources().getDrawable(R.drawable.icn_menu_center);
        menuIconBottom = getResources().getDrawable(R.drawable.icn_menu_bottom);

    }

    private void initMenus() {
        topContainer = (LinearLayout) findViewById(R.id.ly_container_top);
        topContainer.post(new Runnable() {

            @Override
            public void run() {
                float origin = ViewHelper.getX(topContainer);
                ViewHelper.setX(topContainer, origin - topContainer.getWidth());
            }
        });
        topContainer = (LinearLayout) findViewById(R.id.ly_container_left);
        topContainer.post(new Runnable() {

            @Override
            public void run() {
                float origin = ViewHelper.getY(topContainer);
                ViewHelper.setY(topContainer, origin - topContainer.getHeight());
            }
        });
    }

    private void initMenuButton(){
        findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(menuShowed)
                    hideMenu();
                else
                    showMenu();

            }
        });
        imageTop = (ImageView) findViewById(R.id.icn_menu_top);
        imageCenter = (ImageView) findViewById(R.id.icn_menu_center);
        imageBottom = (ImageView) findViewById(R.id.icn_menu_bottom);
    }

    private final float centerRotateAnimation = Utils.dpToPx(56, getResources()) / 2; //?

    private void animateIcon() {
        RotateAnimation rotateAnimationCenter = setAnimation(0, 225);
        rotateAnimationCenter.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                if (!topItems.isEmpty())
                    topItems.get(0).show();
                if (!leftItems.isEmpty())
                    leftItems.get(0).show();
            }
        });
        imageCenter.startAnimation(rotateAnimationCenter);

        RotateAnimation rotateAnimationTop = setAnimation(0, 120);
        rotateAnimationTop.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                RotateAnimation rotateAnimationTop = setAnimation(120, 245);
                imageTop.setImageDrawable(menuIconTop);
                imageTop.startAnimation(rotateAnimationTop);
            }
        });
        imageTop.startAnimation(rotateAnimationTop);

        RotateAnimation rotateAnimationBottom = setAnimation(0, 100);
        rotateAnimationBottom.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {}

            @Override
            public void onAnimationRepeat(Animation arg0) {}

            @Override
            public void onAnimationEnd(Animation arg0) {
                RotateAnimation rotateAnimationBottom = setAnimation(100, 195);
                imageBottom.setImageDrawable(menuIconBottom);
                imageBottom.startAnimation(rotateAnimationBottom);
            }
        });
        imageBottom.startAnimation(rotateAnimationBottom);
    }

    private RotateAnimation setAnimation(int x, int y) {
        RotateAnimation rotateAnimation = new RotateAnimation(x, y,
                centerRotateAnimation, centerRotateAnimation);
        rotateAnimation.setInterpolator(new AccelerateInterpolator());
        rotateAnimation.setDuration(ANIMATION_DURATION);
        rotateAnimation.setFillAfter(true);
        return rotateAnimation;
    }

    public void showMenu(){
        // Animate icon button
        animateIcon();

        // show top menu
        ViewHelper.setX(topContainer, -topContainer.getWidth()+Utils.dpToPx(56, getResources()));
        float origin = ViewHelper.getX(topContainer);
        ObjectAnimator.ofFloat(topContainer, "x", 0)
                .setDuration(ANIMATION_DURATION).start();
        // show left menu
        ViewHelper.setY(leftContainer, -leftContainer.getHeight()+Utils.dpToPx(56, getResources()));
        origin = ViewHelper.getY(leftContainer);
        ObjectAnimator.ofFloat(leftContainer, "y", 0)
                .setDuration(ANIMATION_DURATION).start();
        //Show fade View
        viewFade.setClickable(true);
        ObjectAnimator.ofFloat(viewFade, "alpha", 0.6f)
                .setDuration(ANIMATION_DURATION).start();
        menuShowed = true;
        float originalWidth = contentView.getWidth();
        float width = contentView.getWidth() - Utils.dpToPx(56, getResources());
        float scaleX = width / originalWidth;
        float originalHeight = contentView.getHeight();
        float height = contentView.getHeight() - Utils.dpToPx(56, getResources());
        float scaleY = height / originalHeight;
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, scaleX, 1, scaleY,contentView.getWidth(),contentView.getHeight());
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        scaleAnimation.setDuration(ANIMATION_DURATION/2);
        scaleAnimation.setFillAfter(true);
        contentView.startAnimation(scaleAnimation);

    }

    public void hideMenu(){
        // Animate icon button
        final float centerRotateAnimation = Utils.dpToPx(56, getResources()) / 2;
        RotateAnimation rotateAnimationCenter = new RotateAnimation(225,0,centerRotateAnimation,centerRotateAnimation);
        rotateAnimationCenter.setInterpolator(new AccelerateInterpolator());
        rotateAnimationCenter.setDuration(ANIMATION_DURATION);
        rotateAnimationCenter.setFillAfter(true);
        imageCenter.startAnimation(rotateAnimationCenter);

        RotateAnimation rotateAnimationTop = new RotateAnimation(245,120,centerRotateAnimation,centerRotateAnimation);
        rotateAnimationTop.setInterpolator(new AccelerateInterpolator());
        rotateAnimationTop.setDuration(ANIMATION_DURATION/2);
        rotateAnimationTop.setFillAfter(true);
        rotateAnimationTop.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {}

            @Override
            public void onAnimationRepeat(Animation arg0) {}

            @Override
            public void onAnimationEnd(Animation arg0) {
                RotateAnimation rotateAnimationTop = new RotateAnimation(120,0,centerRotateAnimation,centerRotateAnimation);
                rotateAnimationTop.setInterpolator(new AccelerateInterpolator());
                rotateAnimationTop.setDuration(ANIMATION_DURATION/2);
                rotateAnimationTop.setFillAfter(true);
                imageTop.setImageDrawable(menuIconTop);
                imageTop.startAnimation(rotateAnimationTop);
            }
        });
        imageTop.startAnimation(rotateAnimationTop);

        RotateAnimation rotateAnimationBottom = new RotateAnimation(195,100,centerRotateAnimation,centerRotateAnimation);
        rotateAnimationBottom.setInterpolator(new AccelerateInterpolator());
        rotateAnimationBottom.setDuration(ANIMATION_DURATION/2);
        rotateAnimationBottom.setFillAfter(true);
        rotateAnimationBottom.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {}

            @Override
            public void onAnimationRepeat(Animation arg0) {}

            @Override
            public void onAnimationEnd(Animation arg0) {
                RotateAnimation rotateAnimationBottom = new RotateAnimation(100,0,centerRotateAnimation,centerRotateAnimation);
                rotateAnimationBottom.setInterpolator(new AccelerateInterpolator());
                rotateAnimationBottom.setDuration(ANIMATION_DURATION/2);
                rotateAnimationBottom.setFillAfter(true);
                imageBottom.setImageDrawable(icon_menu_bottom);
                imageBottom.startAnimation(rotateAnimationBottom);
            }
        });
        imageBottom.startAnimation(rotateAnimationBottom);

        // hide top menu
        float origin = ViewHelper.getX(topContainer);
        ObjectAnimator.ofFloat(topContainer, "x", -topContainer.getWidth())
                .setDuration(ANIMATION_DURATION).start();
        // hide left menu
        origin = ViewHelper.getY(leftContainer);
        ObjectAnimator.ofFloat(leftContainer, "y", -leftContainer.getHeight())
                .setDuration(ANIMATION_DURATION).start();
        //hide fade View
        viewFade.setClickable(false);
        ObjectAnimator.ofFloat(viewFade, "alpha", 0f)
                .setDuration(ANIMATION_DURATION).start();

        float originalWidth = contentView.getWidth();
        float width = contentView.getWidth() - Utils.dpToPx(56, getResources());
        float scaleX = width / originalWidth;
        float originalHeight = contentView.getHeight();
        float height = contentView.getHeight() - Utils.dpToPx(56, getResources());
        float scaleY = height / originalHeight;
        ScaleAnimation scaleAnimation = new ScaleAnimation(scaleX,1,scaleY,1,contentView.getWidth(),contentView.getHeight());
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        scaleAnimation.setDuration(ANIMATION_DURATION/2);
        scaleAnimation.setFillAfter(true);
        contentView.startAnimation(scaleAnimation);

        for(MenuItem menuItem : topItems)
            menuItem.hide();
        for(MenuItem menuItem : leftItems)
            menuItem.hide();


        menuShowed = false;

    }
}
