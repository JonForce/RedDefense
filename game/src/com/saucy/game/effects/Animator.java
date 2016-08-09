package com.saucy.game.effects;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.rendering.Graphic;
import com.saucy.game.Callback;
import com.saucy.game.Direction;
import com.saucy.game.Game;
import com.saucy.game.gui.GraphicAccessor;

public class Animator {
	
	private final Game game;
	private final ArrayList<Tween> tweens;
	
	/**
	 * Create an animation utility.
	 */
	public Animator(Game game) {
		this.game = game;
		this.tweens = new ArrayList<Tween>();
	}
	
	/**
	 * @param time The duration of the sliding animation.
	 * @param direction The Direction of the animation.
	 * @param graphic The Graphic to animate.
	 */
	public Animator slideGraphicOffscreen(float time, Direction direction, Graphic graphic) {
		final Vector2 translation;
		if (direction == Direction.RIGHT)
			translation = new Vector2(game.screenWidth(), 0);
		else if (direction == Direction.UP)
			translation = new Vector2(0, game.screenHeight());
		else if (direction == Direction.LEFT)
			translation = new Vector2(-game.screenWidth(), 0);
		else
			translation = new Vector2(0, -game.screenHeight());
		
		tweens.add(Tween.to(graphic, GraphicAccessor.POSITION_TWEEN, time)
				.target(graphic.x() + translation.x, graphic.y() + translation.y)
				.ease(Quad.OUT)
				.start(game.tweenManager()));
		return this;
	}
	
	/**
	 * @param time The duration of the sliding animations.
	 * @param timeVariation The variation in animation duration between the Graphics.
	 * @param direction The direction to slide the Graphics.
	 * @param graphics The Graphics to be animated.
	 */
	public Animator slideGraphicsOffscreen(float time, int timeVariation, Direction direction, Graphic[] graphics) {
		for (Graphic graphic : graphics)
			slideGraphicOffscreen(time - (timeVariation/2) + game.random().nextInt(timeVariation), direction, graphic);
		return this;
	}
	
	/**
	 * @param time The duration of the sliding animation.
	 * @param direction The direction to slide the Graphics.
	 * @param graphics The Graphics to be animated.
	 */
	public Animator slideGraphicsOffscreen(float time, Direction direction, Graphic... graphics) {
		slideGraphicsOffscreen(time, 0, direction, graphics);
		return this;
	}
	
	/**
	 * Rotate the specified Graphic FOREVER.
	 * @param target The Graphic to rotate FOREVER.
	 * @param speed The speed at which to rotate.
	 */
	public Animator rotateGraphicIndefinitely(Graphic target, float speed) {
		tweens.add(
				Tween.to(target, GraphicAccessor.ROTATION_TWEEN, speed)
					.target(360f)
					.ease(Linear.INOUT)
					.repeat(-1, 0)
					.start(game.tweenManager())
				);
		return this;
	}
	
	/**
	 * @param tween The index of the Tween to retrieve.
	 * @return The n'th Tween generated by the Animator.
	 */
	public Tween getTween(int tween) {
		return this.tweens.get(tween);
	}
	
	/**
	 * @return The most recent Tween added to the Animator.
	 */
	public Tween get() {
		return this.tweens.get(tweens.size() - 1);
	}
	
	/** Set the callbacks of all the Tweens created by the Animator to the specified callback.
	 * The method automatically converts the Callback to a TweenCallback.
	 */
	public void setAllCallbacksTo(final Callback callback) {
		for (Tween tween : this.tweens)
			if (tween != null)
				tween.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						callback.call();
					}
				});
	}

	public Animator tweenGraphicTo(Graphic graphic, Vector2 position, float time) {
		tweens.add(Tween.to(graphic, GraphicAccessor.POSITION_TWEEN, time)
				.target(position.x,position.y)
				.ease(Quad.IN)
				.start(game.tweenManager()));
		return this;
	}
}