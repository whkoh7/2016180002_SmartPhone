package kr.ac.kpu.game.s2016180002.dragonflight.game;


import android.app.AlertDialog;
import android.graphics.Canvas;
import android.media.Image;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kpu.game.s2016180002.dragonflight.R;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2016180002.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s2016180002.dragonflight.ui.activity.MainActivity;
import kr.ac.kpu.game.s2016180002.dragonflight.ui.view.GameView;
import kr.ac.kpu.game.s2016180002.dragonflight.utils.CollisionHelper;

public class BaseGame {
    private static final String TAG = BaseGame.class.getSimpleName();
    private static final int MAX_POWER = 3;
    private static final int BOSS_SCORE = 100;
    // singleton
    private static BaseGame instance;
    private Player player;
    private Score score;
    private float previousX, previousY;
    public boolean bossdie = false;
    private MediaPlayer mediaPlayer;
    private int hp = 3;

    public static BaseGame get() {
        if (instance == null) {
            instance = new BaseGame();
        }
        return instance;
    }
    public float frameTime;
    private boolean initialized;
    private static float COLLIDE_INTERVAL = 1.0f/3.0f;
    private float collideTime = 0.0f;

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
        bg1, boss, enemy, bullet, player, hp,  ui, controller, item,  bossbullet, bg2, ENEMY_COUNT,;
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
        mediaPlayer = MediaPlayer.create(GameView.view.getContext(),R.raw.dragon_flight);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
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

        ArrayList<GameObject> enemies       = layers.get(Layer.enemy.ordinal());
        ArrayList<GameObject> bullets       = layers.get(Layer.bullet.ordinal());
        ArrayList<GameObject> players       = layers.get(Layer.player.ordinal());
        ArrayList<GameObject> hps           = layers.get(Layer.hp.ordinal());
        ArrayList<GameObject> items         = layers.get(Layer.item.ordinal());
        ArrayList<GameObject> bosses        = layers.get(Layer.boss.ordinal());
        ArrayList<GameObject> bossbullets   = layers.get(Layer.bossbullet.ordinal());
        collideTime += frameTime;
        for(GameObject o1: enemies){
            Enemy enemy = (Enemy) o1;
            boolean collided = false;
            for( GameObject o2 : bullets){
                Bullet bullet = (Bullet) o2;
                if(CollisionHelper.collides(enemy, bullet)) {
                    remove(bullet, false);
                    if(enemy.getHp() > 0) {
                        enemy.setHp(enemy.getHp() - bullet.power);
                    }
                    else if(enemy.getHp() <= 0) {
                        enemy.generateItem();
                        remove(enemy, false);
                        score.addScore(BOSS_SCORE);
                    }
                    collided = true;
                    break;
                }
            }
            if(collided){
                break;
            }
            for(GameObject o3 : players){
                Player player = (Player)o3;
                if(CollisionHelper.collides(enemy,player)) {
                    if (collideTime >= COLLIDE_INTERVAL) {
                        for (GameObject o4 : hps) {
                            ImageObject hp = (ImageObject) o4;
                            remove(hp, false);
                            this.hp--;
                            if(this.hp == 0)
                            {
                                remove(player,false);
                                VerticalScrollBackground gameover = new VerticalScrollBackground(R.mipmap.bg_gameover,0);
                                add(Layer.bg2, gameover);
                            }
                            collideTime = 0.0f;
                            break;
                        }
                        collided = true;
                        break;
                    }
                }
            }
            if(collided){
                break;
            }
        }
        for(GameObject o1 : players){
            Player player = (Player) o1;
            boolean collided = false;
            for(GameObject o2 : items){
                Item item = (Item) o2;
                if(CollisionHelper.collides(item,player)){
                    Log.d(TAG, "itemtype : " + item.getType());
                    if(item.getType() < 4) {
                        score.addScore(item.getType() * 100);
                    }
                    else if(item.getType() == 4){
                        if(player.power < MAX_POWER)
                            player.power += 1;
                    }
                    remove(item, false);
                    collided = true;
                    break;
                }
            }
            if(collided){
                break;
            }
            for(GameObject o3 : bossbullets){
                BossBullet bossBullet = (BossBullet) o3;
                if(CollisionHelper.collides(bossBullet,player)){
                    if (collideTime >= COLLIDE_INTERVAL) {
                        for (GameObject o4 : hps) {
                            ImageObject hp = (ImageObject) o4;
                            remove(hp, false);
                            this.hp--;
                            if(this.hp == 0)
                            {
                                remove(player,false);
                                VerticalScrollBackground gameover = new VerticalScrollBackground(R.mipmap.bg_gameover,0);
                                add(Layer.bg2, gameover);
                            }
                            collideTime = 0.0f;
                            break;
                        }
                        collided = true;
                        break;
                    }
                }
            }
            if(collided){
                break;
            }
        }
        for(GameObject o1: bosses) {
            Boss boss = (Boss) o1;
            boolean collided = false;
            for (GameObject o2 : bullets) {
                Bullet bullet = (Bullet) o2;
                if (CollisionHelper.collides(boss, bullet)) {
                    if (boss.getHp() > 0) {
                        boss.setHp(boss.getHp() - bullet.power);
                    } else if (boss.getHp() == 0) {
                        bossdie = true;
                        remove(boss, false);
                        score.addScore(1000);
                    }
                    remove(bullet, false);
                    collided = true;
                    break;
                }
            }
            if(collided) {
                break;
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
        if(action == MotionEvent.ACTION_DOWN && action != MotionEvent.ACTION_MOVE){
            previousX = event.getX();
            previousY = event.getY();
            return true;
            }
        else if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
            player.moveTo(player.getX() + (event.getX() - previousX), event.getY());
            previousX = event.getX();
            return true;
        }
        return false;
//        int action = event.getAction();
////        if (action == MotionEvent.ACTION_DOWN) {
//        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
//            player.moveTo(event.getX(), event.getY());
//            return true;
//        }
//        return false;
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

    public void remove(GameObject gameObject){
        remove(gameObject,true);
    }
    public void remove(GameObject gameObject, boolean delayed) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (ArrayList<GameObject> objects: layers) {
                    boolean removed = objects.remove(gameObject);
                    if (removed) {
                        if (gameObject instanceof Recyclable) {
                            ((Recyclable) gameObject).recycle();
                            recycle(gameObject);
                        }
                        //Log.d(TAG, "Removed: " + gameObject);
                        break;
                    }
                }
            }
        };
        if (delayed) {
            GameView.view.post(runnable);
        } else{
            runnable.run();
        }
    }
}