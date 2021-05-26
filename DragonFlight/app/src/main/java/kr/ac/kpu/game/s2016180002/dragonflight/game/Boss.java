package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s2016180002.dragonflight.ui.view.GameView;

public class Boss implements GameObject, BoxCollidable, Recyclable {
    private static final int[] RESOURCE_IDS ={
            R.mipmap.boss_insect, R.mipmap.boss_slime
    };
    private float x;
    private float y;
    private float hp;
    private int level;
    private float speed;
    private GameBitmap bitmap;

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x,y,rect);
    }

    public static Boss get(int level, float x, float y, float hp){
        BaseGame game = BaseGame.get();
        Boss boss = (Boss) game.get(Boss.class);
        if(boss == null){
            boss = new Boss();
        }
        boss.init(level,x,y,hp);
        return boss;
    }

    private void init(int level, float x, float y, float hp) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.level = level;
        this.speed = 400;
        int resId = RESOURCE_IDS[level - 1];
        this.bitmap = new GameBitmap(resId);
    }

    @Override
    public void update() {
        BaseGame game = BaseGame.get();
        if( y < 300) {
            y += speed * game.frameTime;
            return;
        }
        if(x >= GameView.view.getWidth() - bitmap.getWidth()/2 * GameView.MULTIPLIER|| x <= bitmap.getWidth()/2 * GameView.MULTIPLIER)
            speed *= -1;
        x += speed * game.frameTime;
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas,x,y);
    }

    @Override
    public void recycle() {
    }
}
