package kr.ac.kpu.game.s2016180002.dragonflight.game;


import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.ui.view.GameView;

public class Player implements GameObject, BoxCollidable {
    private static final String TAG = Player.class.getSimpleName();
    private static final int BULLET_SPEED = 1500;
    private static final float FIRE_INTERVAL = 1.0f / 7.5f;
    private static final float LASER_DURATION = FIRE_INTERVAL / 3;
    private float fireTime;
    private int imageWidth;
    private int imageHeight;
    private float x, y;
    private float tx, ty;
    private float speed;
    private final float limitX;
    private GameBitmap planeBitmap;
    private GameBitmap fireBitmap;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.tx = x;
        this.ty = 0;
        this.speed = 800;
        this.planeBitmap = new GameBitmap(R.mipmap.player);
        this.fireBitmap = new GameBitmap(R.mipmap.bullet_01);
        this.fireTime = 0.0f;
        this.limitX = GameView.view.getWidth();
    }

    public void moveTo(float x, float y) {
        if(x < limitX && x > 0)
            this.x = x;
        //this.ty = this.y;
    }

    public void update() {
        BaseGame game = BaseGame.get();
//        float dx = speed * game.frameTime;
//        if (tx < x) { // move left
//            dx = -dx;
//        }
//        x += dx;
//        if ((dx > 0 && x > tx) || (dx < 0 && x < tx)) {
//            x = tx;
//        }

        fireTime += game.frameTime;
        if(fireTime >= FIRE_INTERVAL){
            fireBullet();
            fireTime -= FIRE_INTERVAL;
        }
    }

    private void fireBullet() {
        Bullet bullet = Bullet.get(this.x, this.y, BULLET_SPEED);
        BaseGame game = BaseGame.get();
        game.add(BaseGame.Layer.bullet, bullet);
    }

    public void draw(Canvas canvas) {
        planeBitmap.draw(canvas,x,y);
        if(fireTime < LASER_DURATION) {
            fireBitmap.draw(canvas, x, y - 50);
        }
    }

    @Override
    public void getBoundingRect(RectF rect) {
        float bound = 10;
        rect.set(x - bound, y + bound, x + bound,  y - bound);
//        planeBitmap.getBoundingRect(x, y, rect);
    }

    public float getX(){
        return x;
    }

}