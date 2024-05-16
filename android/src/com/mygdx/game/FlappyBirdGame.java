package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import java.util.Random;

public class FlappyBirdGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture birdTexture;
    private Texture background;
    private OrthographicCamera camera;
    private Rectangle bird;
    private float birdVelocity;
    private static final float GRAVITY = -155;
    private Array<Obstacle> obstacles;
    private float obstacleTimer;
    private Random random;
    private boolean gameOver;
    private Texture gameOverTexture;
    private Texture restartTexture;
    private Rectangle restartButton;

    @Override
    public void create() {
        batch = new SpriteBatch();
        birdTexture = new Texture("bird.png");
        background = new Texture("background.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        bird = new Rectangle(50, 240 / 2 - 5 / 2, 100, 50);
        birdVelocity = 0;

        obstacles = new Array<>();
        obstacleTimer = 0;
        random = new Random();
        gameOver = false;
        gameOverTexture = new Texture("gameover.png");
        restartTexture = new Texture("restart.png");
        restartButton = new Rectangle(200, 30, 400, 100);
    }

    @Override
    public void render() {
        handleInput();
        updateGame();
        drawGame();
    }

    private void handleInput() {
        if (!gameOver && Gdx.input.justTouched()) {
            birdVelocity = 100;
        }
        if (gameOver && Gdx.input.justTouched()) {
            restartGame();
        }
    }


    private void updateGame() {
        if (!gameOver) {
            birdVelocity += GRAVITY * Gdx.graphics.getDeltaTime();
            bird.y += birdVelocity * Gdx.graphics.getDeltaTime();
            if (bird.y < 0) {
                bird.y = 0;
            }
            updateObstacles();
            checkCollision();
        }
    }

    private void updateObstacles() {
        if (!gameOver) {
            obstacleTimer += Gdx.graphics.getDeltaTime();
            if (obstacleTimer >= 3) {
                float obstacleX = 800;
                float obstacleY = random.nextFloat() * (480 - 200);
                Obstacle obstacle = new Obstacle(new Texture("obstacle.png"), obstacleX, obstacleY, 50, 200, -100);
                obstacles.add(obstacle);
                obstacleTimer = 0;
            }
            for (Obstacle obstacle : obstacles) {
                obstacle.update(Gdx.graphics.getDeltaTime());
            }

            for (int i = obstacles.size - 1; i >= 0; i--) {
                Obstacle obstacle = obstacles.get(i);
                if (obstacle.getX() + obstacle.getWidth() < 0) {
                    obstacles.removeIndex(i);
                }
            }
        }
    }

    private void checkCollision() {
        if (!gameOver) {
            for (Obstacle obstacle : obstacles) {
                if (bird.overlaps(obstacle.getRectangle())) {
                    gameOver = true;
                    break;
                }
            }
        }
    }

    private void drawGame() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, 800, 480);
        batch.draw(birdTexture, bird.x, bird.y, 100, 50);
        for (Obstacle obstacle : obstacles) {
            obstacle.render(batch);
        }
        if (gameOver) {
            batch.draw(gameOverTexture, 200, 190);
            batch.draw(restartTexture, restartButton.x, restartButton.y, restartButton.width, restartButton.height);
        }
        batch.end();
    }

    private void restartGame() {
        bird.y = 240 / 2 - 5 / 2;
        birdVelocity = 0;
        obstacles.clear();
        obstacleTimer = 0;
        gameOver = false;
    }

    @Override
    public void dispose() {
        batch.dispose();
        birdTexture.dispose();
        background.dispose();
        gameOverTexture.dispose();
        restartTexture.dispose();
        for (Obstacle obstacle : obstacles) {
            obstacle.dispose();
        }
    }
}