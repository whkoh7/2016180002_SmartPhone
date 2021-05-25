package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.Recyclable;

public class Boss implements GameObject, BoxCollidable, Recyclable {
    private static final int[] RESOURCE_IDS ={
            R.mipmap.boss_insect, R.mipmap.boss_slime
    };
    private float x;
    private float y;
    private float hp;
    private int level;
    private GameBitmap bitmap;

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x,y,rect);
    }

    public static Boss get(int level, int x, int y, int hp){
        BaseGame game = BaseGame.get();
        Boss boss = (Boss) game.get(Boss.class);
        if(boss == null){
            boss = new Boss();
        }
        boss.init(level,x,y,hp);
        return boss;
    }

    private void init(int level, int x, int y, int hp) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.level = level;

        this.bitmap = new GameBitmap()
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas,x,y);
    }

    @Override
    public void recycle() {
    }
}
