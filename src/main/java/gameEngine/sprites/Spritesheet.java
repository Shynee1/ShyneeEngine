package gameEngine.sprites;

import gameEngine.renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {

    private Texture sheet;
    private List<Sprite> sprites;

    public Spritesheet(Texture spritesheet, int spriteWidth, int spriteHeight, int numSprites, int spacing){
        this.sprites = new ArrayList<>();
        this.sheet = spritesheet;
        int currentX = 0;
        int currentY = sheet.getHeight() - spriteHeight;

        for (int i = 0; i < numSprites; i++){
            float topY = (currentY+spriteHeight)/(float)sheet.getHeight();
            float rightX = (currentX+spriteWidth)/(float)sheet.getWidth();
            float leftX = currentX/(float)sheet.getWidth();
            float bottomY = currentY/(float)sheet.getHeight();

            Vector2f[] texCoords = new Vector2f[]{
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite(this.sheet, texCoords);
            this.sprites.add(sprite);

            currentX += spriteWidth+spacing;
            if (currentX >= sheet.getWidth()){
                currentX = 0;
                currentY -= spriteHeight+spacing;
            }
        }
    }

    public Sprite getSprite(int index){
        return this.sprites.get(index);
    }
}
