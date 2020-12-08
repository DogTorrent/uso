package com.dottorrent.uso.gui.pane;

import com.dottorrent.uso.gui.thread.LineThread;
import com.dottorrent.uso.gui.component.MusicList;
import com.dottorrent.uso.gui.component.QualityLabel;
import com.dottorrent.uso.service.GameConfig;
import com.dottorrent.uso.service.HitObject;
import com.dottorrent.uso.service.Music;
import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/28
 */
public class GamePlayingPane extends JLayeredPane {
    private ImageIcon bgImageIcon;
    private ImageIcon lineImageIcon;
    private ImageIcon keyImageIcon;
    private double scalingFactor;
    private QualityLabel bgImageLabel;
    private QualityLabel[] lineImageLabels;
    private QualityLabel keyListenerMaskLabel;
    private JLabel highLightHitResult;
    private ExecutorService lineExecutorService;
    private ExecutorService showHitResultExecutorService;
    private ShowHitResultThread showHitResultThread;
    private LineThread[] lineThreads;
    private Music music;
    private long startTime;
    private GamePlayingPane gamePlayingPane;
    private int hitAreaY;
    private int lineBoldWidth;
    /**
     * 音符滑块提前显示的毫秒数，也就是音符滑块从顶部下落到判定线所需要的时间，我们需要提前这么久开始让滑块显示在画面上
     */
    private long keyShowAdvancedMillis;


    public QualityLabel[] getLineImageLabels() {
        return lineImageLabels;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getLineBoldWidth() {
        return lineBoldWidth;
    }

    public long getKeyShowAdvancedMillis() {
        return keyShowAdvancedMillis;
    }

    public QualityLabel getKeyListenerMaskLabel() {
        return keyListenerMaskLabel;
    }


    public GamePlayingPane(Music music) {
        this(music, GameConfig.getScalingFactor());
    }

    public GamePlayingPane(Music music, double scalingFactor) {
        this.scalingFactor = scalingFactor;
        this.music = music;
        this.gamePlayingPane = this;

        bgImageIcon = new ImageIcon(getClass().getResource("/pictures/bg.png"));
        bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                (int) (bgImageIcon.getIconWidth() * scalingFactor + 96),
                (int) (bgImageIcon.getIconHeight() * scalingFactor + 54),
                Image.SCALE_SMOOTH));
        bgImageLabel = new QualityLabel();

        lineImageIcon = new ImageIcon(getClass().getResource("/pictures/line.png"));
        lineImageIcon.setImage(lineImageIcon.getImage().getScaledInstance(
                (int) (lineImageIcon.getIconWidth() * scalingFactor),
                (int) (lineImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));
        lineImageLabels = new QualityLabel[4];
        Arrays.setAll(lineImageLabels, i -> new QualityLabel());
        hitAreaY = (int) (1000 * scalingFactor);
        lineBoldWidth = (int) (10 * scalingFactor);
        keyShowAdvancedMillis = (long) (hitAreaY) / GameConfig.getPixelsPerTick() * GameConfig.getMillisPerTick();

        keyImageIcon = new ImageIcon(getClass().getResource("/pictures/key.png"));
        keyImageIcon.setImage(keyImageIcon.getImage().getScaledInstance(
                (int) (keyImageIcon.getIconWidth() * scalingFactor),
                (int) (keyImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));

        keyListenerMaskLabel=new QualityLabel();
        highLightHitResult=new JLabel();

        try {
            music.initHitObjects();
            music.initAudio();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
        initComponents();
        initThreadPool();
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setUndecorated(true);
        GamePlayingPane gamePlayingPane = new GamePlayingPane(new MusicList().getSpecifiedMusic(16));
        testFrame.setContentPane(gamePlayingPane);
        testFrame.pack();
        testFrame.setVisible(true);
        testFrame.getContentPane().setVisible(true);
        testFrame.setLocationRelativeTo(null);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        //======== this ========
        this.setPreferredSize(new Dimension((int) (1920 * scalingFactor), (int) (1080 * scalingFactor)));
        if (screenWidth <= getPreferredSize().width || screenHeight <= getPreferredSize().height) {
            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                    (int) (getPreferredSize().width + 96),
                    (int) (getPreferredSize().height + 54),
                    Image.SCALE_SMOOTH)
            );
        }

        //---- keyListenerMaskLabel ----
        this.add(keyListenerMaskLabel);
        keyListenerMaskLabel.setFocusable(true);
        keyListenerMaskLabel.requestFocus();

        //---- highLightHitResult ----
        highLightHitResult.setPreferredSize(new Dimension(lineImageIcon.getIconWidth()*2,(int) (64*scalingFactor)));
        highLightHitResult.setLocation((getPreferredSize().width-highLightHitResult.getPreferredSize().width)/2,
                (getPreferredSize().height-highLightHitResult.getPreferredSize().height)/2);
        highLightHitResult.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (64*scalingFactor)));
        highLightHitResult.setHorizontalTextPosition(JLabel.CENTER);
        highLightHitResult.setVerticalTextPosition(JLabel.CENTER);
        this.add(highLightHitResult,JLayeredPane.POPUP_LAYER);
        highLightHitResult.setSize(new Dimension(lineImageIcon.getIconWidth()*2,(int) (64*scalingFactor)));
        highLightHitResult.setForeground(new Color(255,255,255));
        //highLightHitResult.setVisible(false);


        //---- lineImageLabels ----
        for (int i = 0; i < lineImageLabels.length; i++) {
            lineImageLabels[i].setIcon(lineImageIcon);
            lineImageLabels[i].setSize(lineImageIcon.getIconWidth(), lineImageIcon.getIconHeight());
            lineImageLabels[i].setLocation((getPreferredSize().width-lineImageIcon.getIconWidth()*4)/2+lineImageIcon.getIconWidth()*i, 0);
            if (i == 0) {
                this.add(lineImageLabels[i], JLayeredPane.DEFAULT_LAYER);
            } else {
                this.add(lineImageLabels[i], JLayeredPane.getLayer(lineImageLabels[0]));
            }
        }

        //---- bgImageLabel ----
        bgImageLabel.setIcon(bgImageIcon);
        bgImageLabel.setSize(bgImageIcon.getIconWidth(), bgImageIcon.getIconHeight());
        bgImageLabel.setLocation(0, 0);
        this.add(bgImageLabel, JLayeredPane.DEFAULT_LAYER);
    }

    private void initThreadPool() {
        lineExecutorService = Executors.newFixedThreadPool(4);
        lineThreads = new LineThread[4];
        showHitResultExecutorService=Executors.newSingleThreadExecutor();
        showHitResultThread=new ShowHitResultThread();
        for (int i = 0; i < lineThreads.length; i++) {
            lineThreads[i] = new LineThread(gamePlayingPane,keyImageIcon,i);
        }

        List<HitObject> hitObjects = music.getHitObjects();
        for (HitObject hitObject : hitObjects) {
            lineThreads[hitObject.getIndexX()].addHitObject(hitObject);
        }
    }

    private class ShowHitResultThread extends Thread{
        private long delay;

        private void setDelay(long delay) {
            this.delay = delay;
        }

        @Override
        public void run() {
            try {
                highLightHitResult.setText(String.valueOf(delay));
                highLightHitResult.setVisible(true);
                gamePlayingPane.repaint();
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            highLightHitResult.setVisible(false);
            gamePlayingPane.repaint();
        }
    }
    public void showHitResult(long delay){
        showHitResultThread.interrupt();
        showHitResultThread.setDelay(delay);
        showHitResultExecutorService.execute(showHitResultThread);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            new ScheduledThreadPoolExecutor(1).execute(() -> {
                startTime = GameConfig.getStartDelay() + System.currentTimeMillis();
                for (int i = 0; i < lineThreads.length; i++) {
                    LineThread lineThread = lineThreads[i];
                    lineExecutorService.execute(lineThread);
                }
                new ScheduledThreadPoolExecutor(1).schedule(() -> music.play(), startTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            });
        }
        System.out.println(music.getTitle() + " " + music.getVersion());
    }

}
