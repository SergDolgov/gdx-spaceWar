package com.company.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Ship extends MovableObject{
    Weapon weapon = new Weapon();
    private final static TextureAtlas atlas = new TextureAtlas("game.pack");
    private final static TextureRegion textureHp = new TextureRegion(atlas.findRegion("bar"));
    private final Texture textureExplosion = new Texture("explosion.png");
    int hp;
    int hpMax;
    private float fireTimer, expTimer;
    private final ShipOwner ownerType;
    private boolean destroyed;

    public boolean isDestroyed() {
        return destroyed;
    }

    public ShipOwner getOwnerType() {
        return ownerType;
    }

    public Ship(String textureName, int cellSize, ShipOwner ownerType) {
        super(textureName, cellSize);
        this.ownerType = ownerType;
    }

    public void setHp(int hp) {
        this.hp = hp;
        this.hpMax = hp;
    }

    @Override
    public void activate(float x, float y, int speed) {
        super.activate(x, y, speed);
        //this.weapon = new Weapon();
        expTimer = 0;
        destroyed = false;
        fireTimer = weapon.getFirePeriod();
        this.hpMax = 4;
        this.hp = this.hpMax;
    }

    public void fire(Bullet bullet) {
        if (fireTimer >= weapon.getFirePeriod()) {
            fireTimer = 0.0f;
            bullet.activate(this);
        }
    }

    @Override
    public void render(Batch batch) {
        Vector2 position = getPosition();
        int cellSize = getCellSize();
        fireTimer += 1;
        if (expTimer > 0) {
            expTimer--;
            batch.draw(textureExplosion, position.x, position.y);
            if (expTimer <= 0) super.deactivate();
        }
        else{
            super.render(batch);
            if (hp < hpMax) {
                batch.setColor(0, 0, 0, 0.8f);
                batch.draw(textureHp, position.x + 8 - 2, position.y + cellSize - 8 - 2, 44, 12);
                batch.setColor(0, 1, 0, 0.8f);
                batch.draw(textureHp, position.x + 8, position.y + cellSize  - 8, ((float) hp / hpMax) * 40, 8);
                batch.setColor(1, 1, 1, 1);
            }
        }
    }

    @Override
    public void deactivate() {
        if (expTimer <= 0) expTimer = 5; destroyed=true;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            deactivate();
        }
    }
}
