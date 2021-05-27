package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

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
    private static final int BULLET_SPEED = 800;
    private static float FIRE_INTERVAL = 1.0f / 3.5f;

    private float x;
    private float y;
    private float hp;
    private int level;
    private float speed;
    private GameBitmap bitmap;
    private float fireTime;

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
        fireTime = 0;
        int resId = RESOURCE_IDS[level - 1];
        this.bitmap = new GameBitmap(resId);
    }

    private void fireBullet() {
        BaseGame game = BaseGame.get();
        Random r = new Random();
        int pattern = r.nextInt(3);
        switch (pattern) {
            case 0:
                BossBullet bossbullet1 = BossBullet.get(this.x, this.y, BULLET_SPEED, -1, y + 1);
                BossBullet bossbullet2 = BossBullet.get(this.x, this.y, BULLET_SPEED, 0, y + 1);
                BossBullet bossbullet3 = BossBullet.get(this.x, this.y, BULLET_SPEED, 1, y + 1);
                game.add(BaseGame.Layer.bossbullet, bossbullet1);
                game.add(BaseGame.Layer.bossbullet, bossbullet2);
                game.add(BaseGame.Layer.bossbullet, bossbullet3);
                break;
            case 1:
                BossBullet bossbullet = BossBullet.get(this.x, this.y, BULLET_SPEED, 0, y + 1);
                game.add(BaseGame.Layer.bossbullet, bossbullet);
                break;
            case 2:
                BossBullet bossbullet4 = BossBullet.get(this.x, this.y, BULLET_SPEED, -1.5f, y + 1);
                BossBullet bossbullet5 = BossBullet.get(this.x, this.y, BULLET_SPEED, -1, y + 1);
                BossBullet bossbullet6 = BossBullet.get(this.x, this.y, BULLET_SPEED, 0, y + 1);
                BossBullet bossbullet7 = BossBullet.get(this.x, this.y, BULLET_SPEED, 1, y + 1);
                BossBullet bossbullet8 = BossBullet.get(this.x, this.y, BULLET_SPEED, 1.5f, y + 1);
                game.add(BaseGame.Layer.bossbullet, bossbullet4);
                game.add(BaseGame.Layer.bossbullet, bossbullet5);
                game.add(BaseGame.Layer.bossbullet, bossbullet6);
                game.add(BaseGame.Layer.bossbullet, bossbullet7);
                game.add(BaseGame.Layer.bossbullet, bossbullet8);
                break;
        }
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
        fireTime += game.frameTime;
        if(fireTime >= FIRE_INTERVAL){
            fireBullet();
            fireTime -= FIRE_INTERVAL;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas,x,y);
    }

    @Override
    public void recycle() {
    }


    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

}
