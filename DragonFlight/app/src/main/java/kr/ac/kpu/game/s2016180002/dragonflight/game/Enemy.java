package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s2016180002.dragonflight.ui.view.GameView;

public class Enemy implements GameObject, BoxCollidable, Recyclable {
    private static final float FRAMES_PER_SECOND = 8.0f;
    private static final int[] RESOURCE_IDS = {
            R.mipmap.monster_1, R.mipmap.monster_2, R.mipmap.monster_3, R.mipmap.monster_4
    };
    private float x;
    private AnimationGameBitmap bitmap;
    private int level;
    private int hp;
    private float y;
    private int speed;

    private Enemy() {
    }

    public static Enemy get(int level, int x, int y, int speed) {
        BaseGame game = BaseGame.get();
        Enemy enemy = (Enemy) game.get(Enemy.class);
        if(enemy == null){
            enemy = new Enemy();
        }
        enemy.init(level,x,y,speed);
        return enemy;
    }

    private void init(int level, int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.level = level;
        this.hp = level;

        int resId = RESOURCE_IDS[level-1];

        this.bitmap = new AnimationGameBitmap(resId, FRAMES_PER_SECOND,0);
    }

    @Override
    public void update() {
        BaseGame game = BaseGame.get();
        y += speed * game.frameTime;

        if (y > GameView.view.getHeight()){
            game.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas,x,y);
    }

    public void generateItem(){
        Random r = new Random();
        Item item = Item.get(this.x,this.y,r.nextInt(4));
        BaseGame game = BaseGame.get();
        game.add(BaseGame.Layer.item, item);
    }

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x,y, rect);
    }

    @Override
    public void recycle() {
        // 재활용 통에 들어가는 시점에 불리는 함수
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
