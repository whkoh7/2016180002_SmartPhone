package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s2016180002.dragonflight.ui.view.GameView;

public class Item implements GameObject, BoxCollidable, Recyclable {
    private static final int[] RESOURCE_IDS ={
            R.mipmap.coin, R.mipmap.gem_ruby, R.mipmap.gem_emerald, R.mipmap.gem_diamond, R.mipmap.power_up
    };
    private int speed = - 100;
    private float x;
    private float y;
    private int type;
    private GameBitmap bitmap;

    private Item(float x, float y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public static Item get(float x, float y, int type) {
        BaseGame game = BaseGame.get();
        Item item = (Item)game.get(Item.class);
        if(item == null){
            item = new Item(x,y,type);
        }
        item.init(x,y,type);
        return item;
    }

    private void init(float x, float y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.speed = -100;
        int resId = RESOURCE_IDS[type];
        this.bitmap = new GameBitmap(resId);
    }

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x,y,rect);
    }

    @Override
    public void update() {
        BaseGame game = BaseGame.get();
        y += speed * game.frameTime;
        speed += 5;
        if ( y > GameView.view.getHeight()) {
            game.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {bitmap.draw(canvas,x,y); }

    @Override
    public void recycle() {
    }

    public int getType(){
        return type;
    }
}
