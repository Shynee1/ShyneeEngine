package gameEngine.objects;

import gameEngine.Transform;
import gameEngine.abstracts.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    //Global
    private static int ID_COUNTER = 0;
    //Unique to each GameObject
    private int uid = -1;

    private String name;
    private List<Component> components;
    private int zIndex;
    public Transform transform;

    /**
     * Creates a simple GameObject with default values
     * @param name The name of the GameObject
     */
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.zIndex = 0;

        this.uid = GameObject.ID_COUNTER++;
    }

    /**
     * Creates a GameObject with the parameters
     * @param name The name of the GameObject
     * @param transform Custom class that includes position and scale
     * @param zIndex Integer that decides depth of GameObjects
     */
    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;

        this.uid = GameObject.ID_COUNTER++;
    }

    /**
     * Gets a component that is attached to the GameObject
     * @param componentClass Generic class of the component you want to get
     * @return Returns the component or null
     * @param <T> Generic value that extends Component
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }

        return null;
    }

    /**
     * Removes a component from the GameObject
     * @param componentClass Generic class of the component you want to get
     * @param <T> Generic value that extends Component
     */
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        this.components.add(c);
        c.generateId();
        c.gameObject = this;
    }

    public void update(float dt) {
        for (int i=0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void start() {
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    /**
     * Adds every component's ImGui widget to the main Scene window
     */
    public void imgui(){
        for (Component c : components){
            c.imgui();
        }
    }

    public int getZIndex(){
        return this.zIndex;
    }

    public int getUid(){
        return uid;
    }

    public static void init(int maxID){
        GameObject.ID_COUNTER = maxID;
    }

    public List<Component> getAllComponents(){
        return this.components;
    }
}
