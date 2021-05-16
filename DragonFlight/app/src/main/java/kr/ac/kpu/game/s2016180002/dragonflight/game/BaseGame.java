package kr.ac.kpu.game.s2016180002.dragonflight.game;


import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s2016180002.dragonflight.ui.view.GameView;
import kr.ac.kpu.game.s2016180002.dragonflight.utils.CollisionHelper;

public class BaseGame {
    private static final String TAG = BaseGame.class.getSimpleName();
    // singleton
    private static BaseGame instance;
    private Player player;
    private Score score;

    public static BaseGame get() {
        if (instance == null) {
            instance = new BaseGame();
        }
        return instance;
    }
    public float frameTime;
    private boolean initialized;

    //    Player player;
    ArrayList<ArrayList<GameObject>> layers;
    private static HashMap<Class,ArrayList<GameObject>> recycleBin = new HashMap<>();

    public void recycle(GameObject object) {
        Class clazz = object.getClass();
        ArrayList<GameObject> array = recycleBin.get(clazz);
        if(array == null){
            array = new ArrayList<>();
            recycleBin.put(clazz,array);
        }
        array.add(object);
    }
    public GameObject get(Class clazz){
        ArrayList<GameObject> array = recycleBin.get(clazz);
        if(array==null||array.isEmpty())return null;
        return array.remove(0);
    }

    public  enum Layer{
        bg1, enemy, bullet, player, hp,  ui, controller, ENEMY_COUNT
    }

    public boolean initResources() {
        if (initialized) {
            return false;
        }
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();

        initLayers(Layer.ENEMY_COUNT.ordinal());

        player = new Player(w/2, h - 300);
        //layers.get(Layer.player.ordinal()).add(player);
        add(Layer.player, player);
        add(Layer.controller, new EnemyGenerator());

        int margin = (int)(20*GameView.MULTIPLIER);
        score = new Score(w - margin, margin);
        score.setScore(0);
        add(Layer.ui, score);
        for(int i = 1; i < 4; i ++) {
            ImageObject life = new ImageObject(R.mipmap.hp, 50, 100 * i);
            add(Layer.hp, life);
        }
        VerticalScrollBackground bg = new VerticalScrollBackground(R.mipmap.bg_dragon,10);
        add(Layer.bg1,bg);
//        HorizontalScrollBackground clouds = new HorizontalScrollBackground(R.mipmap.clouds, 20);
//        add(Layer.bg2,clouds);

        initialized = true;
        return true;
    }

    private void initLayers(int layerCount) {
        layers = new ArrayList<>();
        for(int i = 0; i < layerCount; i++){
            layers.add(new ArrayList<>());
        }
    }

    public void update() {
        //if (!initialized) return;
        for(ArrayList<GameObject> objects : layers) {
            for (GameObject o : objects) {
                o.update();
            }
        }

        ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
        ArrayList<GameObject> bullets = layers.get(Layer.bullet.ordinal());
        ArrayList<GameObject> players = layers.get(Layer.player.ordinal());
        for(GameObject o1: enemies){
            Enemy enemy = (Enemy) o1;
            boolean collided = false;
            for( GameObject o2 : bullets){
                Bullet bullet = (Bullet) o2;
                if(CollisionHelper.collides(enemy, bullet)) {
                    remove(bullet);
                    remove(enemy);
                    score.addScore(100);
                    collided = true;
                    break;
                }
            }
            for(GameObject o3 : players) {
                Player player = (Player) o3;
                if(CollisionHelper.collides(enemy,player)){
                    score.addScore(-500);
                }
            }
        }
//        for (GameObject o1 : objects) {
//            if (!(o1 instanceof Enemy)) {
//                continue;
//            }
//            Enemy enemy = (Enemy)o1;
//            boolean removed = false;
//            for(GameObject o2 : objects) {
//                if(!(o2 instanceof Bullet)) {
//                    continue;
//                }
//                Bullet bullet = (Bullet)o2;
//                if(CollisionHelper.collides(enemy, bullet)) {
//                    remove(enemy);
//                    remove(bullet);
////                    recycle(bullet);
////                    bullet.recycle();
//                    removed = true;
//                    break;
//                }
//            }
//            if (removed) {
//                continue;
//            }
//            if (CollisionHelper.collides((BoxCollidable) enemy, player)) {
//            }
//        }
    }

    public void draw(Canvas canvas) {
        //if (!initialized) return;
            for(ArrayList<GameObject> objects : layers) {
                for (GameObject o : objects) {
                    o.draw(canvas);
                }
            }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            player.moveTo(event.getX(), event.getY());
            return true;
        }
        return false;
    }

    public void add(Layer layer, GameObject gameObject) {
        GameView.view.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<GameObject> objects = layers.get(layer.ordinal());
                objects.add(gameObject);
            }
        });
//        Log.d(TAG, "<A> object count = " + objects.size());
    }

    public void remove(GameObject gameObject) {
        if(gameObject instanceof Recyclable){
            ((Recyclable) gameObject).recycle();
            recycle(gameObject);
        }
        GameView.view.post(new Runnable() {
            @Override
            public void run() {
                for(ArrayList<GameObject> objects : layers) {
                    boolean removed = objects.remove(gameObject);
                    if(removed) {
                        break;
                    }
                }
            }
//                Log.d(TAG, "<R> object count = " + objects.size());
        });
    }
}