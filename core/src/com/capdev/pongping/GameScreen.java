package com.capdev.pongping;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen implements Screen {
    final PongPing game;

    private Texture ballImage, paddle1Image, paddle2Image;
    private Sound paddle1Hit, paddle2Hit;
    private OrthographicCamera camera;
    private Rectangle ball, paddle1,paddle2;
    private boolean ballTowards1 = false;
    private static final int ballSpeed = 200;
    private static final int paddleSpeed = 500;
    private float ballHorizontalSpeed = 0;
    private boolean ballGoingUp;

    public GameScreen(final PongPing game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);

        ballImage = new Texture(Gdx.files.internal("Ball.png"));
        paddle1Image = new Texture(Gdx.files.internal("Paddle1.png"));
        paddle2Image = new Texture(Gdx.files.internal("Paddle2.png"));
        paddle1Hit = Gdx.audio.newSound(Gdx.files.internal("paddle1hit.wav"));
        paddle2Hit = Gdx.audio.newSound(Gdx.files.internal("paddle2hit.wav"));

        ball = new Rectangle();
        ball.height = ballImage.getHeight();
        ball.width = ballImage.getWidth();
        ball.x = camera.viewportWidth / 2 - ball.getWidth() / 2;
        ball.y = camera.viewportHeight / 2 - ball.getHeight() / 2;

        paddle1 = new Rectangle();
        paddle1.height = paddle1Image.getHeight();
        paddle1.width = paddle1Image.getWidth();
        paddle1.x = 0;
        paddle1.y = camera.viewportHeight / 2 - paddle1.getHeight() / 2;

        paddle2 = new Rectangle();
        paddle2.height = paddle2Image.getHeight();
        paddle2.width = paddle2Image.getWidth();
        paddle2.x = camera.viewportWidth - paddle2.getWidth();
        paddle2.y = camera.viewportHeight / 2 - paddle2.getHeight() / 2;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(paddle1Image, paddle1.getX(),paddle1.getY());
        game.batch.draw(ballImage,ball.getX(),ball.getY());
        game.batch.draw(paddle2Image, paddle2.getX(),paddle2.getY());
        game.batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.W)) paddle1.y += paddleSpeed * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.S)) paddle1.y -= paddleSpeed * Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) paddle2.y += paddleSpeed * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) paddle2.y -= paddleSpeed * Gdx.graphics.getDeltaTime();

        if(ballTowards1 == false) {
            ball.x -= ballSpeed * Gdx.graphics.getDeltaTime();
        } else {
            ball.x += ballSpeed * Gdx.graphics.getDeltaTime();
        }

        if(ballGoingUp){
            ball.y += ballHorizontalSpeed * Gdx.graphics.getDeltaTime();
        } else if (ballGoingUp == false) {
            ball.y -= ballHorizontalSpeed * Gdx.graphics.getDeltaTime();
        }


        if(ball.overlaps(paddle1)) {
            ballTowards1 = true;
            ballHorizontalSpeed = MathUtils.random(500);
            if(MathUtils.randomBoolean()){
                ballGoingUp = true;
            }
            paddle1Hit.play();
        } else if (ball.overlaps(paddle2)) {
            ballTowards1 = false;
            ballHorizontalSpeed = MathUtils.random(500);
            if(MathUtils.randomBoolean()){
                ballGoingUp = true;
            }
            paddle2Hit.play();
        }

        if(ball.y < 0 ){
            ball.y = 5;
            ballGoingUp = true;

        } else if(ball.y > camera.viewportHeight - ball.getHeight()) {
            ballGoingUp = false;
        }

        if(ball.x < 0 || ball.x > camera.viewportWidth - ball.getWidth()) {
            game.setScreen(new MainMenuScreen(game));
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
