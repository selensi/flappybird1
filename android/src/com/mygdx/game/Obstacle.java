package com.mygdx.game;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class Obstacle {
    private Texture texture;
    private float x, y;
    private float width, height;
    private float velocityX;
    public Obstacle(Texture texture, float x, float y, float width, float height, float velocityX) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = velocityX;
    }
    public void update(float delta) {
        x += velocityX * delta;
    }
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
    public void dispose() {
        texture.dispose();
    }
    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }
}