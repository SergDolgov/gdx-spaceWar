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
    private final GameType gameType;
    private TextureAtlas atlas;
    private final static int MAX_ENEMYS = 20;
    private final static int MAX_BULLETS = 300;
    private final static int MAX_ITEMS = 10;
    private Ship me;
    private final List<EnemyShip> enemies = new ArrayList<>(MAX_ENEMYS);
    private final List<Bullet> bullets = new ArrayList(MAX_BULLETS);
    private final List<Item> items = new ArrayList(MAX_ITEMS);
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
        itemRegions = new TextureRegion(atlas.findRegion("powerUps")).split(30, 30);
        Gdx.input.setInputProcessor(inputProcessor);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        gameTimer = 100.0f;
        me = new Ship("spaceship2.png", 64, ShipOwner.PLAYER);
        me.activate(WORLD_WIDTH/2, 64, 6);

        for (int i = 0; i < MAX_ENEMYS;i++) enemies.add(new EnemyShip("spaceship1.png", 64, ShipOwner.AI));
        for (int i = 0; i < MAX_BULLETS;i++) bullets.add(new Bullet("projectile.png", 10));
        for (int i = 0; i < MAX_ITEMS; i++) items.add(new Item());

        text = "Heloo";

    }

    @Override
    public void render (float delta) {
        
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) paused = !paused;

        if (paused) return;

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
                        bullet.deactivate();
                        enemy.deactivate();
                        break;
                        }
                if (!enemy.isDestroyed()) {
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
                bullet.moveTo();
                bullet.render(batch);
            }
        });

        items.forEach(item -> {
            if (item.isActive()){
                item.update(delta);
                item.render(batch, itemRegions);
            }
        });

        font.draw(batch, text ,30, 620);

        batch.end();

    }
    private void fire(Ship ship){
        for (Bullet bullet : bullets)
            if (!bullet.isActive()){
                text = bullet.toString();
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
                for (int i = 0; i < MAX_ENEMYS;i++) {
                    //for (EnemyShip enemy : enemies)
                        if(!enemies.get(i).isActive()) {
                            int x = MathUtils.random(WORLD_WIDTH);
                            int y = MathUtils.random(WORLD_HEIGHT);
                            int speed = MathUtils.random(1, 4);
                            enemies.get(i).activate(x, y, speed);
                            break;
                        }
                }

                generateRandomItem(MathUtils.random(4), MathUtils.random(0.5f));

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
                        item.activate(x, y, Item.Type.values()[type]);
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
