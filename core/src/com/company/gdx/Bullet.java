package com.company.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import static com.company.gdx.ScreenManager.WORLD_HEIGHT;
import static com.company.gdx.ScreenManager.WORLD_WIDTH;

public class Bullet extends MovableObject{
    private Ship owner;
    public Ship getOwner() {
        return owner;
    }
    private final Vector2 velocity = new Vector2();
    private int damage;
    private float currentTime;
    private float maxTime;
    private final TextureRegion textureRegion1 = new TextureRegion(new Texture("projectile1.png"));
    private final TextureRegion textureRegion = new TextureRegion(new Texture("projectile.png"));

    public Bullet () {
        super("projectile.png", 10);

    }

    public void activate(Ship owner) {
        this.owner = owner;
        Weapon weapon = owner.weapon;

        double angleRadians = Math.toRadians(owner.getAngle().angleDeg());
        float vx = weapon.getProjectileSpeed() * (float) Math.cos(angleRadians);
        float vy = weapon.getProjectileSpeed() * (float) Math.sin(angleRadians);

        this.velocity.set(vx, vy);
        if(owner.getOwnerType() == ShipOwner.AI)
            this.setTextureRegion(textureRegion1);
        else {
            this.velocity.scl(4);
            this.setTextureRegion(textureRegion);
        }

        setPosition(owner.getPosition());
        this.getPosition().add(32,32);

        setActive(true);
        this.damage = weapon.getDamage();
        this.maxTime = weapon.getProjectileLifeTime();
        this.currentTime = 0.0f;
    }

    public void moveTo() {
        getPosition().add(velocity);
        Vector2 position = getPosition();
        currentTime += 1;
        if (!(position.x > 0 && position.x < WORLD_WIDTH + owner.getCellSize()
                && position.y > 0 && position.y < WORLD_HEIGHT + owner.getCellSize())
                || currentTime >= maxTime) deactivate();
    }

    public int getDamage() {
        return damage;
    }

}

