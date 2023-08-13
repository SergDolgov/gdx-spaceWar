package com.company.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    private final TextureRegion textureRegion;
    private final float firePeriod;
    private final float radius;
    private final int damage;
    private final float projectileSpeed;
    private final float projectileLifeTime;

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public float getProjectileLifeTime() {
        return projectileLifeTime;
    }


    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }


    public Weapon() {
        Texture texture = new Texture("Weapon.png");
        textureRegion = new TextureRegion(texture);
        this.firePeriod = 0.7f;
        this.damage = 1;
        this.radius = 500.0f;
        this.projectileSpeed = 3.0f;
        this.projectileLifeTime = this.radius / this.projectileSpeed;
    }
}
