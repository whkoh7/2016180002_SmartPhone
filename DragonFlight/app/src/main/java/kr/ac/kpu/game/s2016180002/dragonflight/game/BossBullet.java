package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.Recyclable;

public class BossBullet implements GameObject, BoxCollidable, Recyclable {
    private int speed;
    private float x;
    private final GameBitmap bitmap;
    private float y;

    private BossBullet(float x, float y, int speed){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.bitmap = new GameBitmap(R.mipmap.boss_bullet_01);
    }


    public static BossBullet get(float x, float y, int speed) {
        BaseGame game = BaseGame.get();
        BossBullet bossbullet = (BossBullet) game.get(BossBullet.class);
        if(bossbullet == null){
            return new BossBullet(x,y,speed);
        }
        bossbullet.init(x, y, speed);
        return bossbullet;
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
