package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/** Source-generated presentation assets for the public portfolio edition. */
public final class ProceduralTextures implements com.badlogic.gdx.utils.Disposable {
    private final Array<Texture> owned = new Array<>();

    private Texture finish(Pixmap p) {
        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        owned.add(t);
        return t;
    }

    public Texture panel(boolean highlighted) {
        Pixmap p = new Pixmap(420, 110, Pixmap.Format.RGBA8888);
        p.setColor(0, 0, 0, 0); p.fill();
        p.setColor(highlighted ? new Color(0.10f, 0.20f, 0.25f, .97f) : new Color(.035f,.065f,.10f,.96f));
        p.fillRectangle(7, 7, 406, 96);
        p.setColor(highlighted ? Color.valueOf("f1d38aff") : Color.valueOf("99835cff"));
        p.drawRectangle(7, 7, 406, 96);
        p.drawLine(20, 12, 400, 12);
        p.drawLine(20, 97, 400, 97);
        p.fillTriangle(0,55,7,48,7,62);
        p.fillTriangle(419,55,412,48,412,62);
        return finish(p);
    }

    public Texture card() {
        Pixmap p = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
        p.setColor(.025f,.045f,.075f,.94f); p.fill();
        p.setColor(Color.valueOf("99835cff"));
        p.drawRectangle(2,2,252,252);
        p.drawRectangle(7,7,242,242);
        for (int x : new int[]{2, 233}) for (int y : new int[]{2, 233}) p.fillRectangle(x,y,21,3);
        return finish(p);
    }

    public Texture sound(boolean enabled) {
        Pixmap p = new Pixmap(64,64,Pixmap.Format.RGBA8888);
        p.setColor(0,0,0,0);p.fill();
        p.setColor(Color.valueOf("f1d38aff"));
        p.fillRectangle(10,25,12,16);p.fillTriangle(20,25,35,14,35,51);
        if (enabled) {
            p.drawLine(42,23,49,31);p.drawLine(49,31,42,42);
            p.drawLine(48,15,59,31);p.drawLine(59,31,48,49);
        } else {
            p.setColor(Color.valueOf("e77c7cff"));
            for(int i=0;i<3;i++) {p.drawLine(42+i,23,57+i,42);p.drawLine(42+i,42,57+i,23);}
        }
        return finish(p);
    }

    public Texture block(int index) {
        String[] colors={"63cdd7ff","e8c95dff","b38bd5ff","64bc87ff","db7485ff","739fe0ff","e5a266ff","798795ff"};
        Color base=Color.valueOf(colors[index % colors.length]);
        Pixmap p=new Pixmap(24,24,Pixmap.Format.RGBA8888);
        p.setColor(base.cpy().mul(.45f));p.fill();
        p.setColor(base);p.fillRectangle(2,2,20,20);
        p.setColor(base.cpy().lerp(Color.WHITE,.4f));p.fillRectangle(3,3,18,2);p.fillRectangle(3,3,2,18);
        p.setColor(base.cpy().mul(.65f));p.fillRectangle(3,19,18,2);p.fillRectangle(19,3,2,18);
        return finish(p);
    }

    public Texture encounter(boolean boss) {
        Pixmap p=new Pixmap(48,48,Pixmap.Format.RGBA8888);
        p.setColor(0,0,0,0);p.fill();
        p.setColor(boss ? Color.valueOf("e27979ff") : Color.valueOf("e5c475ff"));
        if(boss) {
            p.fillCircle(24,23,15);p.fillRectangle(15,28,18,14);
            p.setColor(Color.valueOf("12202aff"));p.fillCircle(18,22,4);p.fillCircle(30,22,4);
            p.fillTriangle(24,26,20,32,28,32);
            for(int x=18;x<=30;x+=6)p.drawLine(x,37,x,42);
        } else {
            p.fillTriangle(24,3,12,29,36,29);p.fillTriangle(24,45,12,19,36,19);
        }
        return finish(p);
    }

    public Texture solid() {
        Pixmap p=new Pixmap(1,1,Pixmap.Format.RGBA8888);p.setColor(Color.WHITE);p.fill();return finish(p);
    }

    @Override public void dispose() { for(Texture t:owned)t.dispose();owned.clear(); }
}
