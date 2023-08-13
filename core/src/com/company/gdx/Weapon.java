package com.company.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    private TextureRegion textureRegion;
    private float firePeriod;
    private float radius;
    private int damage;
    private float projectileSpeed;
    private float projectileLifeTime;

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public float getProjectileLifeTime() {
        return projectileLifeTime;
    }

    public TextureRegion getTexture() {
        return textureRegion;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }

    public float getRadius() {
        return radius;
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
