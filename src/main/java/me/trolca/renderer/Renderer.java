package me.trolca.renderer;

import me.trolca.jade.GameObject;
import me.trolca.jade.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private static final int MAX_BATCHES_AMOUNT = 1000;
    private List<RenderBatch> batchesList;

    public Renderer(){
        this.batchesList = new ArrayList<>();
        batchesList.add(new RenderBatch(MAX_BATCHES_AMOUNT));
    }

    public void addGameObject(GameObject go){
        SpriteRenderer spriteRenderer = go.getComponent(SpriteRenderer.class);

        if(spriteRenderer != null){
            for(RenderBatch renderBatch : batchesList){
                if(renderBatch.isFree()){
                    renderBatch.addSpriteRenderer(spriteRenderer);
                    return;
                }
            }
            RenderBatch renderBatch = new RenderBatch(MAX_BATCHES_AMOUNT);
            batchesList.add(renderBatch);
            renderBatch.addSpriteRenderer(spriteRenderer);
        }
    }

    public void render(){
        for(RenderBatch renderBatch : batchesList)
            renderBatch.render();
    }

}
