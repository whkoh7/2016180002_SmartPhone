package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.ui.view.GameView;

public class HorizontalScrollBackground implements GameObject {
    private final Bitmap bitmap;
    private final float speed;
    private Rect srcRect = new Rect();
    private RectF dstRect = new RectF();
    private float scroll;

    public HorizontalScrollBackground(int resId, int speed){
        this.speed = speed * GameView.MULTIPLIER;
        bitmap = GameBitmap.load(resId);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        srcRect.set(0,0,w,h);
        float l = 0;// x - w / 2 * GameView.MULTIPLIER;
        float t = 0;// - h / 2 * GameView.MULTIPLIER;
        float b = GameView.view.getHeight();
        float r = b * w / h;
        dstRect.set(l,t,r,b);
    }

    @Override
    public void update() {
        BaseGame game = BaseGame.get();
        float amount = speed * game.frameTime;
        scroll += amount;
    }

    @Override
    public void draw(Canvas canvas) {
        int vw = GameView.view.getWidth();
        int vh = GameView.view.getHeight();
        int iw = bitmap.getWidth();
        int ih = bitmap.getHeight();
        int dw = vh * iw / ih;
        int curr = (int)(scroll % dw);
        if(curr > 0) curr -= dw;

        while(curr < vw){
            dstRect.set(curr,0,curr+dw,vh);
            canvas.drawBitmap(bitmap,srcRect,dstRect, null);
            curr += dw;
        }
    }
}