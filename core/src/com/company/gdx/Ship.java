package com.company.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Ship extends MovableObject{
    Weapon weapon;
    private final Texture textureExplosion;
    private float fireTimer, expTimer;
    private  ShipOwner ownerType;
    public ShipOwner getOwnerType() {
        return ownerType;
    }
    boolean destroyed;
    public Ship(float x, float y, int speed) {
        this(x, y, "spaceship2.png", speed);
        ownerType = ShipOwner.PLAYER;
    }

    public Ship(float x, float y, String textureName, int speed) {
        super(x, y, textureName, speed);
        ownerType = ShipOwner.AI;
        this.weapon = new Weapon();
        textureExplosion = new Texture("explosion.png");
        expTimer = 0;
        destroyed = false;
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
