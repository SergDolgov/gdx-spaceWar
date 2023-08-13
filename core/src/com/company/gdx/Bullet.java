package com.company.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends MovableObject{
    private Ship owner;
    public Ship getOwner() {
        return owner;
    }
    private final Vector2 velocity;
    private int damage;
    private float currentTime;
    private float maxTime;
    private final TextureRegion textureRegion1 = new TextureRegion(new Texture("projectile1.png"));
    private final TextureRegion textureRegion = new TextureRegion(new Texture("projectile.png"));
    public Bullet () {
        super(0, 0, "projectile.png", 4, 10);
        this.velocity = new Vector2(1,1);
        setActive(false);
        this.damage = 0;
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
            this.velocity.scl(2);
            this.setTextureRegion(textureRegion);
        }

        setPosition(owner.getPosition());

        setActive(true);
        this.damage = weapon.getDamage();
        this.maxTime = weapon.getProjectileLifeTime();
        this.currentTime = 0.0f;
    }

    public void moveTo(float delta) {
        getPosition().add(velocity);
        currentTime += 1;
        if (!canMove() || currentTime >= maxTime) deactivate();
    }

}

