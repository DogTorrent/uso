package com.dottorrent.uso.gui.component;

import com.dottorrent.uso.service.GameConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/27
 */
public class QualityButton extends JButton {
    boolean isHighQuality;

    public QualityButton(){
        this(GameConfig.getHighQuality());
    }

    public QualityButton(boolean isHighQuality){
        this.isHighQuality = isHighQuality;
    }

    public void setHighQuality(boolean isHighQuality) {
        this.isHighQuality = isHighQuality;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(isHighQuality) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        super.paintComponent(g);
    }
}