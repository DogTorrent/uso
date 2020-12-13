package com.dottorrent.uso.gui;

import com.dottorrent.uso.gui.pane.GamePlayingPane;
import com.dottorrent.uso.gui.pane.MusicSelectingPane;
import com.dottorrent.uso.service.Music;
import com.dottorrent.uso.service.User;

import javax.swing.*;
import java.awt.*;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/28
 */
public class LocalGameFrame extends JFrame {
    private Container oldPane;
    private Container defaultPane;
    private LauncherFrame launcherFrame;
    private User user;

    public LocalGameFrame(LauncherFrame launcherFrame){
        super();
        this.setUndecorated(true);
        this.defaultPane=this.getContentPane();
        this.launcherFrame=launcherFrame;
        this.user=new User(0,null,null);
    }
    public void enterMusicSelectingPane(){
        if(this.getContentPane()!=defaultPane&&this.getContentPane()!=null) {
            oldPane=this.getContentPane();
            oldPane.setVisible(false);
        }
        this.setContentPane(new MusicSelectingPane(user));
        if(!this.isVisible()) {
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
        this.getContentPane().setVisible(true);
        this.repaint();
    }

    public void enterGamePlayingPane(Music music, User user){
        if(this.getContentPane()!=defaultPane&&this.getContentPane()!=null) {
            oldPane=this.getContentPane();
            oldPane.setVisible(false);
        }
        this.setContentPane(new GamePlayingPane(music, user));
        if(!this.isVisible()) {
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
        this.getContentPane().setVisible(true);
        this.repaint();
    }

    public void exitFrame(){
        launcherFrame.pack();
        this.dispose();
    }

    public Container getOldPane() {
        return oldPane;
    }
}
