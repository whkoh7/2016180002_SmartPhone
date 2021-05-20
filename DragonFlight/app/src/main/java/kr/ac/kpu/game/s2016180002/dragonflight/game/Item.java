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
    private int speed = - 100;
    private float x;
    private float y;
    private int type;
    private final GameBitmap bitmap;

    private Item(float x, float y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
        if(type == 0){this.bitmap = new GameBitmap(R.mipmap.coin);}
        else if(type == 1){this.bitmap = new GameBitmap(R.mipmap.gem_ruby);}
        else if(type == 2){this.bitmap = new GameBitmap(R.mipmap.gem_emerald);}
        else{this.bitmap = new GameBitmap(R.mipmap.gem_diamond);}
    }

    public static Item get(float x, float y, int type) {
        BaseGame game = BaseGame.get();
        Item item = (Item)game.get(Item.class);
        if(item == null){
            return new Item(x,y,type);
        }
        item.init(x,y,type);
        return item;
    }

    private void init(float x, float y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.speed = -100;
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
