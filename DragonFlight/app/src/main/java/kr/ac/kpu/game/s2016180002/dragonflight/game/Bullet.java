package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.Recyclable;

public class Bullet implements GameObject, BoxCollidable, Recyclable {
    private static final String TAG = Bullet.class.getSimpleName();
    private int speed;
    private float x;
    private final GameBitmap bitmap;
    private float y;

    private Bullet(float x, float y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = -speed;

        this.bitmap = new GameBitmap(R.mipmap.bullet_01);
    }

//    private static ArrayList<Bullet> recycleBin = new ArrayList();

    public static Bullet get(float x, float y, int speed) {
        BaseGame game = BaseGame.get();
        Bullet bullet = (Bullet) game.get(Bullet.class);
        if(bullet == null){
            return new Bullet(x,y,speed);
        }
        bullet.init(x, y, speed);
        return bullet;
    }

    private void init(float x, float y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = -speed;
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