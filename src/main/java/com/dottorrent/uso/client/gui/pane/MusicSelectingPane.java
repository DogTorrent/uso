/*
 * Created by JFormDesigner on Thu Nov 26 15:25:41 CST 2020
 */

package com.dottorrent.uso.client.gui.pane;

import com.dottorrent.uso.client.gui.GameFrame;
import com.dottorrent.uso.client.gui.component.MusicList;
import com.dottorrent.uso.client.gui.component.QualityButton;
import com.dottorrent.uso.client.gui.component.QualityLabel;
import com.dottorrent.uso.client.service.GameConfig;
import com.dottorrent.uso.client.service.Music;
import com.dottorrent.uso.client.service.User;
import com.dottorrent.uso.client.service.manager.ScoreManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ScrollBarUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

/**
 * 选歌界面，不是独立窗口，继承自 {@link JLayeredPane}，已经包含了对用户是否为线上模式用户的检测，如果是本地用户则只提供保存至本地数据库的
 * 功能，线上用户则可以同时保存至本地和云端。
 * <p>
 * {@link User#getUserID()} 为 0 则认为是本地用户，否则认为是在线用户
 *
 * @author .torrent
 * @see JLayeredPane
 */
public class MusicSelectingPane extends JLayeredPane {
    private final User user;
    private final ImageIcon bgImageIcon;
    private ImageIcon topBarImageIcon;
    private ImageIcon bottomBarImageIcon;
    private final ImageIcon musicInfoImageIcon;
    private ImageIcon playButtonImageIcon;
    private ImageIcon playButtonOnMovedIcon;
    private final ImageIcon exitButtonImageIcon;
    private final ImageIcon exitButtonOnMovedImageIcon;
    private final ImageIcon exitButtonPressedImageIcon;
    private final double scalingFactor;
    private final JScrollPane musicListScrollPane;
    private final MusicList musicList;
    private final QualityLabel bgImageLabel;
    private final QualityLabel topBarImageLabel;
    private final QualityLabel bottomBarImageLabel;
    private final QualityLabel musicInfoImageLabel;
    private final QualityButton playButton;
    private final QualityButton exitButton;
    private final GameFrame gameFrame;

    public MusicSelectingPane(User user, GameFrame gameFrame) {
        this(GameConfig.getScalingFactor(), user, gameFrame);
    }

    public MusicSelectingPane(double scalingFactor, User user, GameFrame gameFrame) {
        this.scalingFactor = scalingFactor;
        this.user = user;
        this.gameFrame = gameFrame;

        //---- bgImageIcon ----
        bgImageIcon = new ImageIcon(getClass().getResource("/pictures/bg.png"));
        bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                (int) (bgImageIcon.getIconWidth() * scalingFactor + 96),
                (int) (bgImageIcon.getIconHeight() * scalingFactor + 54),
                Image.SCALE_SMOOTH));

        //---- musicInfoImageIcon ----
        musicInfoImageIcon = new ImageIcon(getClass().getResource("/pictures/popup_label_bg.png"));
        musicInfoImageIcon.setImage(musicInfoImageIcon.getImage().getScaledInstance(
                (int) (musicInfoImageIcon.getIconWidth() * scalingFactor),
                (int) (musicInfoImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));
        musicInfoImageLabel = new QualityLabel();

        //---- playButtonImageIcon && playButtonOnMovedIcon ----
        try {
            BufferedImage playButtonImage = ImageIO.read(getClass().getResource("/pictures/play_round.png"));
            int width = playButtonImage.getWidth() / 2;
            int height = playButtonImage.getHeight();
            playButtonImageIcon = new ImageIcon(playButtonImage.getSubimage(0, 0, width, height));
            playButtonImageIcon.setImage(playButtonImageIcon.getImage().getScaledInstance(
                    (int) (playButtonImageIcon.getIconWidth() * scalingFactor),
                    (int) (playButtonImageIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
            playButtonOnMovedIcon = new ImageIcon(playButtonImage.getSubimage(width, 0, width, height));
            playButtonOnMovedIcon.setImage(playButtonOnMovedIcon.getImage().getScaledInstance(
                    (int) (playButtonOnMovedIcon.getIconWidth() * scalingFactor),
                    (int) (playButtonOnMovedIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        playButton = new QualityButton();

        //---- topBarImageIcon && bottomBarImageIcon ----
        try {
            BufferedImage barImage = ImageIO.read(getClass().getResource("/pictures/bar.png"));
            int width = barImage.getWidth();
            int height = barImage.getHeight() / 2;
            topBarImageIcon = new ImageIcon(barImage.getSubimage(0, 0, width, height));
            topBarImageIcon.setImage(topBarImageIcon.getImage().getScaledInstance(
                    (int) (topBarImageIcon.getIconWidth() * scalingFactor),
                    (int) (topBarImageIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
            bottomBarImageIcon = new ImageIcon(barImage.getSubimage(0, height, width, height));
            bottomBarImageIcon.setImage(bottomBarImageIcon.getImage().getScaledInstance(
                    (int) (bottomBarImageIcon.getIconWidth() * scalingFactor),
                    (int) (bottomBarImageIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        //---- exitButtonImageIcon... ----
        exitButtonImageIcon = gameFrame.exitButtonImageIcon;
        exitButtonOnMovedImageIcon = MusicSelectingPane.this.gameFrame.exitButtonOnMovedImageIcon;
        exitButtonPressedImageIcon = MusicSelectingPane.this.gameFrame.exitButtonPressedImageIcon;

        exitButton = new QualityButton();
        topBarImageLabel = new QualityLabel();
        bottomBarImageLabel = new QualityLabel();
        musicListScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        musicList = new MusicList(scalingFactor);
        bgImageLabel = new QualityLabel();
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents

        //======== this ========
        this.setPreferredSize(new Dimension((int) (1920 * scalingFactor), (int) (1080 * scalingFactor)));

        //---- musicInfoImageLabel ----
        musicInfoImageLabel.setVisible(false);
        musicInfoImageLabel.setIcon(musicInfoImageIcon);
        musicInfoImageLabel.setSize(musicInfoImageIcon.getIconWidth(), musicInfoImageIcon.getIconHeight());
        musicInfoImageLabel.setPreferredSize(new Dimension(musicInfoImageIcon.getIconWidth(),
                musicInfoImageIcon.getIconHeight()));
        musicInfoImageLabel.setLocation(getPreferredSize().width / 4,
                (getPreferredSize().height - musicInfoImageIcon.getIconHeight()) / 2);
        musicInfoImageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        musicInfoImageLabel.setVerticalTextPosition(SwingConstants.TOP);
        musicInfoImageLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
        musicInfoImageLabel.setForeground(new Color(200, 200, 200));

        //---- playButton ----
        playButton.setIcon(playButtonImageIcon);
        playButton.setRolloverIcon(playButtonOnMovedIcon);
        playButton.setBounds((int) (musicInfoImageLabel.getX() + musicInfoImageIcon.getIconWidth() - playButtonImageIcon.getIconWidth() / 2 - 10 * scalingFactor),
                (int) (musicInfoImageLabel.getY() + musicInfoImageIcon.getIconHeight() - playButtonImageIcon.getIconHeight() / 2 - 10 * scalingFactor),
                playButtonImageIcon.getIconWidth(),
                playButtonImageIcon.getIconHeight());
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        playButton.setVisible(false);
        playButton.addActionListener(e -> {
            gameFrame.enterGamePlayingPane((Music) (musicList.getSelectedValue()));
        });

        //---- exitButton ----
        exitButton.setIcon(exitButtonImageIcon);
        exitButton.setRolloverIcon(exitButtonOnMovedImageIcon);
        exitButton.setPressedIcon(exitButtonPressedImageIcon);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                bgImageMovedWithMouse(e);
            }
        });
        exitButton.setPreferredSize(new Dimension(exitButtonImageIcon.getIconWidth(), exitButtonImageIcon.getIconHeight()));
        exitButton.setSize(exitButton.getPreferredSize());
        exitButton.setLocation((int) (10 * scalingFactor), (int) (10 * scalingFactor));
        exitButton.addActionListener(e -> {
            gameFrame.backToTitle();
        });
        this.add(exitButton, JLayeredPane.DEFAULT_LAYER, 0);

        //---- barImageLabel ----
        //初始化
        topBarImageLabel.setIcon(topBarImageIcon);
        bottomBarImageLabel.setIcon(bottomBarImageIcon);
        topBarImageLabel.setBounds(0, 0, topBarImageIcon.getIconWidth(), topBarImageIcon.getIconHeight());
        bottomBarImageLabel.setBounds(0, getPreferredSize().height - bottomBarImageIcon.getIconHeight(),
                bottomBarImageIcon.getIconWidth(), bottomBarImageIcon.getIconHeight());
        //样式设定
        topBarImageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        topBarImageLabel.setVerticalTextPosition(SwingConstants.CENTER);
        topBarImageLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
        topBarImageLabel.setForeground(new Color(200, 200, 200));
        showPlayerInfo();
        bottomBarImageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        bottomBarImageLabel.setVerticalTextPosition(SwingConstants.CENTER);
        bottomBarImageLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
        bottomBarImageLabel.setForeground(new Color(200, 200, 200));
        //添加进主面板
        this.add(topBarImageLabel, JLayeredPane.DEFAULT_LAYER, -1);
        this.add(bottomBarImageLabel, JLayeredPane.DEFAULT_LAYER, -1);

        //======== musicListScrollPane ========
        musicListScrollPane.setViewportView(musicList);
        musicListScrollPane.setBounds(getPreferredSize().width / 3 * 2, topBarImageLabel.getHeight(),
                getPreferredSize().width / 3, (int) (getPreferredSize().height - topBarImageLabel.getHeight() - 95 * scalingFactor));
        musicListScrollPane.setOpaque(false);
        musicListScrollPane.getViewport().setOpaque(false);
        musicListScrollPane.setBorder(BorderFactory.createEmptyBorder());
        musicListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        musicListScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI() {
            @Override
            public Dimension getPreferredSize(JComponent c) {
                c.setPreferredSize(new Dimension(1, 0));
                return c.getPreferredSize();
            }
        });
        {
            musicList.setCellSize(musicListScrollPane.getPreferredSize().width, 0);
            musicList.addListSelectionListener(e -> {
                showMusicInfo((Music) musicList.getSelectedValue());
                showHistory((Music) musicList.getSelectedValue());
                changeBgImage((Music) musicList.getSelectedValue());
            });
        }
        musicList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                bgImageMovedWithMouse(e);
            }
        });
        this.add(musicListScrollPane, JLayeredPane.DEFAULT_LAYER);

        //---- bgImageLabel ----
        bgImageLabel.setIcon(bgImageIcon);
        bgImageLabel.setSize(bgImageIcon.getIconWidth(), bgImageIcon.getIconHeight());
        bgImageLabel.setLocation(0, 0);
        bgImageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                bgImageMovedWithMouse(e);
            }
        });
        this.add(bgImageLabel, JLayeredPane.FRAME_CONTENT_LAYER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    /**
     * 背景随鼠标移动而移动
     *
     * @param e 鼠标移动事件
     */
    private void bgImageMovedWithMouse(MouseEvent e) {
        int moveX = e.getXOnScreen() - this.getLocationOnScreen().x;
        int moveY = e.getYOnScreen() - this.getLocationOnScreen().y;
        int redundantX = bgImageIcon.getIconWidth() - this.getWidth();
        int redundantY = bgImageIcon.getIconHeight() - this.getHeight();
        float proportionX = (float) redundantX / this.getWidth();
        float proportionY = (float) redundantY / this.getHeight();
        int destX = (int) (proportionX * moveX) - redundantX;
        int destY = (int) (proportionY * moveY) - redundantY;
        if (destX > 0) {
            destX = 0;
        } else if (destX < -redundantX) {
            destX = -redundantX;
        }
        if (destY > 0) {
            destY = 0;
        } else if (destY < -redundantY) {
            destY = -redundantY;
        }
        bgImageLabel.setLocation(-redundantX - destX, -redundantY - destY);
    }

    /**
     * 在右上方显示玩家信息
     */
    private void showPlayerInfo() {
        String userID = String.valueOf(user.getUserID());
        String userName = user.getUserName();
        if (user.getUserID() == 0) {
            userName = "本地玩家";
        }
        topBarImageLabel.setText("<html>" +
                "<head>" +
                "<meta charset=\"utf-8\"/>" +
                "<style type=\"text/css\">" +
                ".body { " +
                "width:" + (topBarImageLabel.getWidth() / 3) + ";" +
                "padding-left: " + (topBarImageLabel.getWidth() / 3 * 2) + ";" +
                "padding-right: " + (int) (30 * scalingFactor) + "px;" +
                "padding-top: " + (int) (20 * scalingFactor) + "px;" +
                "padding-bottom: " + (int) (20 * scalingFactor) + "px;" +
                "}" +
                ".textLine { " +
                "display:block;" +
                "overflow:hidden;" +
                "text-overflow:ellipsis;" +
                "white-space:nowrap; " +
                "font-size: " + (int) (28 * scalingFactor) + "px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body class=\"body\">" +
                "<p class=\"textLine\">" + "<b>ID : </b>" + userID + "</p>" +
                "<p class=\"textLine\">" + "<b>用户名 : </b>" + userName + "</p>" +
                "</body>" +
                "</html>");
    }

    /**
     * 在左下角显示特定谱面的信息（本地/同步）和特定用户的游玩历史（最高分）。
     * <p>
     * 当用户为本地用户时（也就是 {@link User#getUserID()} == 0 时），谱面一律显示为本地，不会联网进行检测
     *
     * @param music 鼠标在谱面列表选中的谱面
     */
    private void showHistory(Music music) {
        String type = "本地";
        String highScore = "0";

        if (user.getUserID() == 0) {
            type = "本地";
            highScore = String.valueOf(ScoreManager.getHighScore(String.valueOf(user.getUserID()),
                    music.getIdentifier(), Duration.ofMillis(500)));
        } else {
            if (ScoreManager.checkMusicExist(music.getIdentifier(), Duration.ofMillis(500))) {
                type = "同步";
                highScore = String.valueOf(ScoreManager.getHighScore(String.valueOf(user.getUserID()),
                        music.getIdentifier(), Duration.ofMillis(500)));
            }
        }
        bottomBarImageLabel.setText("<html>" +
                "<head>" +
                "<meta charset=\"utf-8\"/>" +
                "<style type=\"text/css\">" +
                ".body { " +
                "width:" + (bottomBarImageLabel.getWidth() / 3) + ";" +
                "padding-left: " + (int) (30 * scalingFactor) + "px;" +
                "padding-right: " + (int) (30 * scalingFactor) + "px;" +
                "padding-top: " + (int) (20 * scalingFactor) + "px;" +
                "padding-bottom: " + (int) (20 * scalingFactor) + "px;" +
                "}" +
                ".textLine { " +
                "display:block;" +
                "overflow:hidden;" +
                "text-overflow:ellipsis;" +
                "white-space:nowrap; " +
                "font-size: " + (int) (28 * scalingFactor) + "px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body class=\"body\">" +
                "<p class=\"textLine\">" + "<b>最高分 : </b>" + highScore + "</p>" +
                "<p class=\"textLine\">" + "<b>谱面类型 : </b>" + type + "</p>" +
                "</body>" +
                "</html>");
    }

    /**
     * 选中谱面时，在画面中央弹出谱面信息框。
     *
     * @param music 鼠标在谱面列表选中的谱面
     */
    private void showMusicInfo(Music music) {
        musicInfoImageLabel.setText("<html>" +
                "<head>" +
                "<meta charset=\"utf-8\"/>" +
                "<style type=\"text/css\">" +
                ".body { " +
                "width:" + (int) (musicInfoImageLabel.getWidth() - 20 * scalingFactor) + "px;" +
                "padding-left: " + (int) (30 * scalingFactor) + "px;" +
                "padding-right: " + (int) (30 * scalingFactor) + "px;" +
                "padding-top: " + (int) (20 * scalingFactor) + "px;" +
                "padding-bottom: " + (int) (20 * scalingFactor) + "px;" +
                "}" +
                ".textLine { " +
                "display:block;" +
                "overflow:hidden;" +
                "text-overflow:ellipsis;" +
                "white-space:nowrap; " +
                "font-size: " + (int) (28 * scalingFactor) + "px;" +
                "}" +
                ".headLine { " +
                "display:block;" +
                "overflow:hidden;" +
                "text-overflow:ellipsis;" +
                "white-space:nowrap; " +
                "font-size: " + (int) (38 * scalingFactor) + "px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body class=\"body\">" +
                "<h1 class=\"headLine\">" + music.getTitle() + "</h1>" +
                "<p class=\"textLine\">" + "<b>Version : </b>" + music.getVersion() + "</p>" +
                "<p class=\"textLine\">" + "<b>Artist : </b>" + music.getArtist() + "</p>" +
                "<p class=\"textLine\">" + "<b>Creator : </b>" + music.getCreator() + "</p>" +
                "<p class=\"textLine\">" + "<b>Difficulty : </b>" + music.getDifficulty() + "</p>" +
                "</body>" +
                "</html>");
        this.add(musicInfoImageLabel, JLayeredPane.MODAL_LAYER);
        musicInfoImageLabel.setVisible(true);
        this.add(playButton, JLayeredPane.POPUP_LAYER);
        playButton.setVisible(true);
    }


    /**
     * 根据选中的谱面指定的曲绘，修改选歌界面的背景。
     *
     * @param music 鼠标在谱面列表选中的谱面
     */
    private void changeBgImage(Music music) {
        if (music != null) {
            Path imgPath = music.getBgImagePath();
            if (imgPath.toFile().isFile()) {
                ImageIcon imgIcon = new ImageIcon(imgPath.toString());
                imgIcon.setImage(imgIcon.getImage().getScaledInstance(
                        (int) (1920 * scalingFactor + 96),
                        (int) (1080 * scalingFactor + 54),
                        Image.SCALE_SMOOTH));
                bgImageLabel.setIcon(imgIcon);
                bgImageLabel.repaint();
                return;
            }
        }
        if (!bgImageLabel.getIcon().equals(bgImageIcon)) {
            bgImageLabel.setIcon(bgImageIcon);
            bgImageLabel.repaint();
        }
    }

}
