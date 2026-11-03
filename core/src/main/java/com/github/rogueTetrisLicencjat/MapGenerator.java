package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class MapGenerator {

    private MapGenerator() {}

    public static Array<MapNode> generateMap() {
        Array<Array<MapNode>> layers = new Array<>();
        int totalLayers = 10;

        for (int i = 0; i < totalLayers; i++) {
            Array<MapNode> layerNodes = new Array<>();
            int nodesInLayer = (i == 0 || i == totalLayers - 1) ? 1 : MathUtils.random(2, 3);

            for (int j = 0; j < nodesInLayer; j++) {
                MapNode.Type type = MapNode.Type.ENEMY;

                if (i == 8) {
                    type = MapNode.Type.REST;
                } else if (i > 0) {
                    float chance = MathUtils.random();
                    if (chance < 0.2f) {
                        type = MapNode.Type.MYSTERY;
                    } else if (chance < 0.45f && i > 1) {
                        type = MapNode.Type.ELITE;
                    } else if (chance < 0.55f && i > 3) {
                        type = MapNode.Type.REST;
                    }
                }

                float spacingY = 400f / nodesInLayer;
                float posY = 100f + (j * spacingY) + (spacingY / 2f) + MathUtils.random(-20, 20);

                MapNode node = new MapNode(type, 150 + i * 200, posY);

                if (i == 0) {
                    node.setReachable(true);
                    node.setY(300);
                }
                if (i == totalLayers - 1) {
                    node.setType(MapNode.Type.BOSS);
                    node.setY(300);
                }

                layerNodes.add(node);
            }
            layers.add(layerNodes);
        }

        for (int i = 0; i < totalLayers - 1; i++) {
            Array<MapNode> currentLayer = layers.get(i);
            Array<MapNode> nextLayer = layers.get(i + 1);

            for (MapNode current : currentLayer) {
                MapNode target = nextLayer.get(MathUtils.random(nextLayer.size - 1));
                current.getConnections().add(target);
            }

            for (MapNode next : nextLayer) {
                boolean hasIncoming = false;
                for (MapNode current : currentLayer) {
                    if (current.getConnections().contains(next, true)) {
                        hasIncoming = true;
                        break;
                    }
                }
                if (!hasIncoming) {
                    MapNode source = currentLayer.get(MathUtils.random(currentLayer.size - 1));
                    if (!source.getConnections().contains(next, true)) {
                        source.getConnections().add(next);
                    }
                }
            }
        }

        Array<MapNode> worldMap = new Array<>();
        for (Array<MapNode> layer : layers) {
            worldMap.addAll(layer);
        }

        return worldMap;
    }
}
