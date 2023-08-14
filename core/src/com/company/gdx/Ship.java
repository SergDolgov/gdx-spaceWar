package com.company.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Ship extends MovableObject{
    Weapon weapon;
    private final Texture textureExplosion;
    private float fireTimer, expTimer;
    private  ShipOwner ownerType;
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
        weapon = new Weapon();
        textureExplosion = new Texture("explosion.png");
    }

    @Override
    public void activate(float x, float y, int speed) {
        super.activate(x, y, speed);
        //this.weapon = new Weapon();
        expTimer = 0;
        destroyed = false;
        //fireTimer = weapon.getFirePeriod();
    }

    public void fire(Bullet bullet) {
        if (fireTimer >= weapon.getFirePeriod()) {
            fireTimer = 0.0f;
            bullet.activate(this);
        }
    }

    @Override
    public void render(Batch batch) {
        if (expTimer > 0) {
            expTimer--;
            batch.draw(textureExplosion, getPosition().x, getPosition().y);
            if (expTimer <= 0) super.deactivate();
        }
        else
            super.render(batch);
    }

    @Override
    public void deactivate() {
        if (expTimer <= 0) expTimer = 5; destroyed=true;
    }

    public void update(float dt) {
        fireTimer += dt;
        //двигаем каждый кадр окружность за танком
       // circle.setPosition(position);
    }

}
