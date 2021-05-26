package kr.ac.kpu.game.s2016180002.dragonflight.game;

import android.graphics.Canvas;

import java.util.Random;

import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.ui.view.GameView;

public class EnemyGenerator implements GameObject {

    private static final float INITIAL_SPAWN_INTERVAL = 5.0f;
    private float time;
    private float spawnInterval;
    private int wave;
    public boolean boss_spawn;

    public EnemyGenerator(){
        time = INITIAL_SPAWN_INTERVAL;
        spawnInterval = INITIAL_SPAWN_INTERVAL;
        wave = 0;
    }

    @Override
    public void update() {
        BaseGame game = BaseGame.get();
        time += game.frameTime;
        if(time >= spawnInterval){
            generate();
            time -= spawnInterval;
        }
    }

    private void generate() {
        BaseGame game = BaseGame.get();
        wave++;
        int tenth = GameView.view.getWidth() / 10;
        Random r = new Random();
        for(int i = 1; i <= 9; i+= 2){
            if(boss_spawn) {
                wave--;
                break;
            }
            int x = tenth * i;
            int y = 0;
            int level = wave / 10 - r.nextInt(3);
            if (level < 1) level = 1;
            if (level > 20) level = 20;

            if(wave == 2){
                Boss boss = Boss.get(1,GameView.view.getWidth()/2,y,10 * wave);
                game.add(BaseGame.Layer.boss, boss);
                boss_spawn = true;
                break;
            }
            Enemy enemy = Enemy.get(level,x,y,700);
            game.add(BaseGame.Layer.enemy, enemy);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // does nothing
    }
}
