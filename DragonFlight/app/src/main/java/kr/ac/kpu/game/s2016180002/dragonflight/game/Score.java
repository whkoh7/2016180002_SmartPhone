package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.ui.view.GameView;

public class Score implements GameObject {
    private final Bitmap bitmap;
    private final int right;
    private final int top;
    private final float ADD_INTERVAL = 1.0f / 7.5f;
    private float flightTime;

    public void setScore(int score) {
        this.score = score;
        this.displayScore = score;
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    private int score, displayScore;
    Rect src = new Rect();
    RectF dst = new RectF();

    public Score(int right, int top){
        bitmap = GameBitmap.load(R.mipmap.number_24x32);
        this.right = right;
        this.top = top;
    }

    @Override
    public void update() {
        BaseGame game = BaseGame.get();
        flightTime += game.frameTime;
        if(flightTime > ADD_INTERVAL){
            score += 1;
            flightTime -= ADD_INTERVAL;
        }
        displayScore = score;
    }

    @Override
    public void draw(Canvas canvas) {
        int value = this.displayScore;
        int nw = bitmap.getWidth() / 10;
        int nh = bitmap.getHeight();
        int x = right;
        int dw = (int)(nw * GameView.MULTIPLIER);
        int dh = (int)(nh * GameView.MULTIPLIER);
        while(value > 0){
            int digit = value%10;
            src.set(digit*nw,0,(digit+1)*nw,nh);
            x -= dw;
            dst.set(x, top, x + dw, top + dh);
            canvas.drawBitmap(bitmap,src,dst,null);

            value /= 10;
        }
    }


}
