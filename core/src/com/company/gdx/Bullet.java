package com.company.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends MovableObject{
    private Ship owner;
    public Ship getOwner() {
        return owner;
    }
    private Vector2 velocity = new Vector2();
    private int damage;
    private float currentTime;
    private float maxTime;
    private final TextureRegion textureRegion1 = new TextureRegion(new Texture("projectile1.png"));
    private final TextureRegion textureRegion = new TextureRegion(new Texture("projectile.png"));

    public Bullet () {
        super("projectile.png", 10);
        velocity.set(1, 1);
        damage=0;
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
        currentTime += 1;
        if (!canMove() || currentTime >= maxTime) deactivate();
    }

}

