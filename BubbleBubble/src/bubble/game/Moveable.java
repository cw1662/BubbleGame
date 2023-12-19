package bubble.game;

import bubble.game.component.Enemy;

public interface Moveable {

	public abstract void left();

	public abstract void right();

	public abstract void up();

	default public void down() {
	}; // default 를 사용하면 인터페이스도 몸체가 있는 메소드를 만들 수 있다. (다중상속이 안되는 것이 많기 때문에)

	default public void attack() {
	};

	default public void attack(Enemy e) {
	};
}
