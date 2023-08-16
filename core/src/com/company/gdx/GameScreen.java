package com.company.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static com.company.gdx.ScreenManager.*;

public class GameScreen extends AbstractScreen {
    private SpriteBatch batch;
    private final GameType gameType;
    private final static int MAX_ENEMYS = 12;
    private final static int MAX_BULLETS = 300;
    private final static int MAX_ITEMS = 10;
    private Ship playerShip;
    private final List<EnemyShip> enemies = new ArrayList(MAX_ENEMYS);
    private final List<Bullet> bullets = new ArrayList(MAX_BULLETS);
    private final List<Item> items = new ArrayList(MAX_ITEMS);
    private final KeyboardAdapter inputProcessor = new KeyboardAdapter();
    private Texture screenTexture;
    private BitmapFont font;
    private String text;
    private boolean paused;
    private float gameTimer;
    private float worldTimer;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        this.gameType = GameType.ONE_PLAYER;
    }
    @Override
    public void show () {
        screenTexture = new Texture("space.jpg");

        Gdx.input.setInputProcessor(inputProcessor);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        gameTimer = 100.0f;

        initCollections();

        text = "Game state";

    }

    private void initCollections() {
        initPlayer();
        for (int i = 0; i < MAX_ENEMYS; i++) enemies.add(new EnemyShip("spaceship1.png", 64, ShipOwner.AI));
        for (int i = 0; i < MAX_BULLETS;i++) bullets.add(new Bullet());
        for (int i = 0; i < MAX_ITEMS; i ++) items.add(new Item());
    }

    private void initPlayer() {
        playerShip = new Ship("spaceship2.png", 64, ShipOwner.PLAYER);
        playerShip.activate(WORLD_WIDTH/2 - 32, 64, 5);
        playerShip.weapon.setType(Weapon.Type.MEDIUM);
        playerShip.setHp(20);
    }

    @Override
    public void render (float delta) {
        
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) paused = !paused;

        if (paused) return;

        update(delta);

        if (Gdx.input.isTouched()) fire(playerShip);

        batch.begin();
        batch.draw(screenTexture,0,0);
        renderAll(batch);

        text = "Hp - " + playerShip.hp;

        font.draw(batch, text ,30, 620);
        batch.end();
    }

    private void renderAll(Batch batch){
        renderPlayer(batch);
        renderEnemies(batch);
        renderBullets(batch);
        renderItems(batch);
    }

    private void renderItems(Batch batch) {
        items.forEach(item -> {
            if (item.isActive()){
                item.moveTo();
                item.render(batch);
            }
        });
    }

    private void renderBullets(Batch batch) {
        bullets.forEach(bullet -> {
            if (bullet.isActive()){
                bullet.moveTo();
                bullet.render(batch);
            }
        });
    }

    private void renderEnemies(Batch batch) {
        for (EnemyShip enemy : enemies)
            if(enemy.isActive()){
                for (Bullet bullet : bullets)
                    if (bullet.isActive() && checkIntersection(bullet, enemy)) {
                        bullet.deactivate();
                        enemy.takeDamage(bullet.getDamage());
                        break;
                    }
                if (!enemy.isDestroyed()) {
                    enemy.moveTo();
                    enemy.rotateTo(playerShip.getPosition());
                    if (enemy.getPosition().dst(playerShip.getPosition()) < enemy.getPursuilRadius()) {
                        fire(enemy);
                    }
                }
                enemy.render(batch);
            }
    }

    private void renderPlayer(Batch batch) {

        if (!playerShip.isActive()) initPlayer();

        playerShip.moveTo(inputProcessor.getDirection());
        playerShip.rotateTo(inputProcessor.getMousePos());
        playerShip.render(batch);

        for (Bullet bullet : bullets)
            if (bullet.isActive() && checkIntersection(bullet, playerShip)) {
                bullet.deactivate();
                playerShip.takeDamage(bullet.getDamage());
                break;
            }
        for (Item item : items)
            if (item.isActive() && checkIntersection(item, playerShip)) {
                item.deactivate();

//                text = "bulet(" + (int)item.getPosition().x + ","+(int)item.getPosition().y +
//                        ") player(" + (int)playerShip.getPosition().x + ","+(int)playerShip.getPosition().y+
//                        ") Damage() ("+item.getDamage() +","+playerShip.hp + ")";
                playerShip.setHp(item.getDamage());
 //               text = text + " - "+playerShip.hp ;
                break;
            }

    }

    private void fire(Ship ship){
        for (Bullet bullet : bullets)
            if (!bullet.isActive()){
                ship.fire(bullet);
                return;
            }
    }

    public static boolean checkIntersection(MovableObject obj1, MovableObject obj2) {

        if (((obj1 instanceof Bullet  && !(obj1 instanceof Item)) &&( obj2 instanceof Ship)
                &&((Bullet) obj1).getOwner().getOwnerType()==((Ship) obj2).getOwnerType())
            )
            return false;

        Vector2 posObj1 = obj1.getPosition();
        Vector2 posObj2 = obj2.getPosition();
        int sizeObj1 = obj1.getCellSize();
        int sizeObj2 = obj2.getCellSize();

        int xMaxObj1 = (int)posObj1.x + sizeObj1;
        int yMaxObj1 = (int)posObj1.y + sizeObj1;
        int xMaxObj2 = (int)posObj2.x + sizeObj2;
        int yMaxObj2 = (int)posObj2.y + sizeObj2;

        return !(posObj1.x > xMaxObj2 || posObj2.x > xMaxObj1 || posObj1.y > yMaxObj2 || posObj2.y > yMaxObj1);
    }

    public void update(float dt) {
        worldTimer += dt;
        if (!paused) {
            gameTimer += dt;
            if (gameTimer > 15.0f) {
                gameTimer = 0;
                generateRandomEnemies();
                generateRandomItem(MathUtils.random(4), MathUtils.random(0.5f));
            }
        }
    }

    private void generateRandomEnemies(){
        for (int i = 0; i < 5;i++)
            for (EnemyShip enemy : enemies)
                if(!enemy.isActive()) {
                    int x = MathUtils.random(WORLD_WIDTH);
                    int y = WORLD_HEIGHT;
                    int speed = MathUtils.random(1, 4);
                    enemy.activate(x, y, speed);
                    break;
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
        playerShip.dispose();
    }
}
