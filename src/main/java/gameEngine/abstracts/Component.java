package gameEngine.abstracts;

import gameEngine.objects.GameObject;

public abstract class Component {
    public GameObject gameObject = null;

    public void start(){

    }

    public abstract void update(float dt);
}
