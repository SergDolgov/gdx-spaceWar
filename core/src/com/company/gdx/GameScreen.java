package com.company.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static com.company.gdx.ScreenManager.*;

public class GameScreen extends AbstractScreen {
    private SpriteBatch batch;
    private GameType gameType;
    private TextureAtlas atlas;
    private final static int MAX_BULLET_COUNT = 300;
    private Ship me;
    private final List<EnemyShip> enemies = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList(MAX_BULLET_COUNT);
    private final List<Item> items = new ArrayList(10);
    private KeyboardAdapter inputProcessor = new KeyboardAdapter();

    private Texture screenTexture;
    private BitmapFont font;
    private String text, text1, text2;
    private boolean paused;
    private float gameTimer;
    private float worldTimer;
    private TextureRegion[][] itemRegions;
    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        this.gameType = GameType.ONE_PLAYER;
    }
    @Override
    public void show () {
        screenTexture = new Texture("space.jpg");
        atlas = new TextureAtlas("game.pack");
        Gdx.input.setInputProcessor(inputProcessor);
        itemRegions = new TextureRegion(atlas.findRegion("powerUps")).split(30, 30);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        gameTimer = 100.0f;
        me = new Ship(100, 200, 4);
        me.setActive(true);

        for (int i = 0; i < MAX_BULLET_COUNT;i++) bullets.add(new Bullet());
        for (int i = 0; i < 10; i++) items.add(new Item());

        int x = MathUtils.random(WORLD_WIDTH);
        int y = MathUtils.random(WORLD_HEIGHT);
        //items.add(new Item());
        //items.get(0).setup(x, y, Item.Type.values()[0]);

        text = "Heloo";
        text1 = "Heloo1";
        text2 = "Heloo2";
    }

    @Override
    public void render (float delta) {
        
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) paused = !paused;

        if (paused) {
            font.draw(batch, "Pause" ,50, 580);
            return;
        }

        update(delta);

        me.moveTo(inputProcessor.getDirection());
        me.rotateTo(inputProcessor.getMousePos());
        me.update(delta);

        batch.begin();
        batch.draw(screenTexture,0,0);
        
        if (Gdx.input.isTouched()) fire(me);

        me.render(batch);

        for (EnemyShip enemy : enemies)
            if(enemy.isActive()){
                for (Bullet bullet : bullets)
                    if (bullet.isActive() && checkIntersection(bullet, enemy)) {
                        Vector2 bulletPos = bullet.getPosition();
                        Vector2 enemyPos = enemy.getPosition();
                        text = "bullet(" + (int)bulletPos.x + ";" +(int)bulletPos.y+"; "+ bullet.getCellSize()
                                +") Velocity("+ (int)enemyPos.x+";"+(int)enemyPos.y+"; "+ enemy.getCellSize()+")";
                        bullet.deactivate();
                        enemy.deactivate();
                        break;
                        }
                if (!enemy.destroyed) {
                    enemy.moveTo(me.getPosition());
                    if (enemy.getPosition().dst(me.getPosition()) < enemy.getPursuilRadius()) {
                        enemy.rotateTo(me.getPosition());
                        fire(enemy);
                        enemy.update(delta);
                    }
                }
                enemy.render(batch);
            }

        bullets.forEach(bullet -> {
            if (bullet.isActive()){
                bullet.moveTo(delta);
                bullet.render(batch);
            }
        });

//        items.get(0).update(delta);
//        items.get(0).render(batch);

//        items.forEach(item -> {
//            if (item.isActive()){
//                item.update(delta);
//                item.render(batch, itemRegions);
//                Vector2 pos = item.getPosition();
//                Vector2 moosePos = item.getPosition();
//                text2 = "pos(" + (int)pos.x + ";" +(int)pos.y+") Velocity("+ (int)moosePos.x+";"+(int)moosePos.y+")";
//            }
//        });

        font.draw(batch, text ,30, 620);
        font.draw(batch, text1 ,30, 600);
        font.draw(batch, text2 ,30, 580);

        batch.end();

    }
    private void fire(Ship ship){
        for (Bullet bullet : bullets)
            if (!bullet.isActive()){
                ship.fire(bullet);
                return;
            }
    }
    public static boolean checkIntersection(MovableObject obj1, MovableObject obj2) {

        if ((obj1 instanceof Bullet) &&( obj2 instanceof Ship)
                &&((Bullet) obj1).getOwner().getOwnerType()==((Ship) obj2).getOwnerType()) return false;

        double halfSize1 = obj1.getCellSize() / 2;
        double halfSize2 = obj2.getCellSize() / 2;

        double distanceX = Math.abs(obj2.getPosition().x - obj1.getPosition().x);
        double distanceY = Math.abs(obj2.getPosition().y - obj1.getPosition().y);

        return distanceX < halfSize1 + halfSize2 && distanceY < halfSize1 + halfSize2;
    }
    public void update(float dt) {

        worldTimer += dt;
        if (!paused) {
            gameTimer += dt;
            if (gameTimer > 15.0f) {
                gameTimer = 0;
                for (int i = 0; i < 5;i++) {
                    int x = MathUtils.random(WORLD_WIDTH);
                    int y = MathUtils.random(WORLD_HEIGHT);
                    int speed = MathUtils.random(1, 4);
                    enemies.add(new EnemyShip(x, y, "spaceship1.png", speed));
                }

                //generateRandomItem(3, 0.5f);
                //regions = new TextureRegion(atlas.findRegion("powerUps")).split(30, 30);
            }
        }
    }
    public void generateRandomItem(int count, float probability) {
        for (int q = 0; q < count; q++) {
            float n = MathUtils.random(0.0f, 1.0f);
            if (n <= probability) {
                int type = MathUtils.random(0, Item.Type.values().length - 1);
                for (Item item : items) {
                    if (!item.isActive()) {
                        int x = MathUtils.random(WORLD_WIDTH);
                        int y = MathUtils.random(WORLD_HEIGHT);
                        item.setup(x, y, Item.Type.values()[type]);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void dispose () {
        batch.dispose();
        me.dispose();
    }
}
