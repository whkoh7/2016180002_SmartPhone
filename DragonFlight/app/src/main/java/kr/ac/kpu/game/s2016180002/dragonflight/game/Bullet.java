package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.Recyclable;

public class Bullet implements GameObject, BoxCollidable, Recyclable {
    private static final String TAG = Bullet.class.getSimpleName();
    private int speed;
    private float x;
    private static final int[] RESOURCE_IDS = {
            R.mipmap.bullet_01, R.mipmap.bullet_02, R.mipmap.bullet_03
    };
    private GameBitmap bitmap;
    private float y;
    public  int power;

    private Bullet(float x, float y, int speed, int power) {
        this.x = x;
        this.y = y;
        this.speed = -speed;
        this.power = power;
        this.bitmap = new GameBitmap(R.mipmap.bullet_01);
    }

//    private static ArrayList<Bullet> recycleBin = new ArrayList();

    public static Bullet get(float x, float y, int speed, int power) {
        BaseGame game = BaseGame.get();
        Bullet bullet = (Bullet) game.get(Bullet.class);
        if(bullet == null){
            return new Bullet(x,y,speed,power);
        }
        Log.d(TAG, "get: " + power);
        bullet.init(x, y, speed, power);
        return bullet;
    }

    private void init(float x, float y, int speed, int power) {
        this.x = x;
        this.y = y;
        this.speed = -speed;
        this.power = power;
        int resId = RESOURCE_IDS[power - 1];
        this.bitmap = new GameBitmap(resId);
    }

    @Override
    public void update() {
        BaseGame game = BaseGame.get();
        y += speed * game.frameTime;
        if ( y < 0) {
            game.remove(this);
            recycle();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas, x, y);
    }

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x, y, rect);
    }

    @Override
    public void recycle() {
        // 재활용 통에 들어가는 시점에 불리는 함수
    }
}