package com.dottorrent.uso.client.service;

import com.dottorrent.uso.client.gui.pane.GamePlayingPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 游戏界面的键盘检测事件，检测键盘输入并判断是否击中、击中时间的偏移，击中后应当获得的分数
 *
 * @author .torrent
 * @version 1.0.0 2020/12/7
 */
public class LineHitObjKeyListener extends KeyAdapter {
    private int lockedHitObjIndex = -1;
    private ArrayList<HitObject> hitObjects;
    private long musicStartTime;
    private boolean judgingStarted;
    private int keyCode;
    private int progressIndex=0;
    private GamePlayingPane gamePlayingPane=null;

    public LineHitObjKeyListener(int keyCode) {
        this.keyCode=keyCode;
        hitObjects = new ArrayList<>();
    }

    public LineHitObjKeyListener(GamePlayingPane gamePlayingPane, int keyCode) {
        this.keyCode=keyCode;
        this.gamePlayingPane=gamePlayingPane;
        hitObjects = new ArrayList<>();
    }

    public void setMusicStartTime(long musicStartTime) {
        this.musicStartTime = musicStartTime;
        new ScheduledThreadPoolExecutor(1).schedule(
                () -> setJudgingStarted(true),
                 musicStartTime-System.currentTimeMillis(),
                TimeUnit.MILLISECONDS);
    }

    public void setJudgingStarted(boolean judgingStarted) {
        this.judgingStarted = judgingStarted;
    }

    public void addHitObjects(HitObject hitObject) {
        hitObjects.add(hitObject);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        long currentTime=System.currentTimeMillis();
        if(e.getKeyCode()!=keyCode){
            return;
        }
        // 如果还没有启动检测，就忽略事件
        if(!judgingStarted){
            return;
        }
        // 如果这一条轨道被阻塞了(也就是按键已经按下了，且按下的按键没放开)
        if(lockedHitObjIndex!=-1){
            return;
        }
        long diff=GameConfig.getJudgeOffset();
        for(int i=progressIndex;i<hitObjects.size();i++){
            HitObject hitObject= hitObjects.get(i);
            long tempDiff=currentTime-musicStartTime+GameConfig.getHitDelay()-hitObject.getStartTime();
            if(Math.abs(tempDiff)<Math.abs(diff)){
                diff=tempDiff;
                lockedHitObjIndex=i;
            }
        }
        if(lockedHitObjIndex!=-1){
            progressIndex=lockedHitObjIndex+1;
            if(hitObjects.get(lockedHitObjIndex).getEndTime()==0){
                //是普通滑块的情况
                if(Math.abs(diff)<=40) {
                    gamePlayingPane.getPlayingResult().setHitObjectsStatus(hitObjects.get(lockedHitObjIndex),
                            PlayingResult.GREAT);
                }else{
                    gamePlayingPane.getPlayingResult().setHitObjectsStatus(hitObjects.get(lockedHitObjIndex),
                            (diff>0?PlayingResult.LATE:PlayingResult.EARLY));
                }
                System.out.println("Hit-S! "+diff);
            }else{
                //是长条滑块的情况
                gamePlayingPane.getPlayingResult().setHitObjectsStatus(hitObjects.get(lockedHitObjIndex),
                        PlayingResult.GREAT);
                System.out.println("Hit-L! "+diff);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        long currentTime=System.currentTimeMillis();
        if(e.getKeyCode()!=keyCode){
            return;
        }
        // 如果还没有启动检测，就忽略事件
        if(!judgingStarted){
            return;
        }
        // 如果这一条轨道未被阻塞(也就是按键没有被按下)，就忽略事件
        if(lockedHitObjIndex==-1){
            return;
        }
        if(hitObjects.get(lockedHitObjIndex).getEndTime()!=0){
            long diff=currentTime-musicStartTime+GameConfig.getHitDelay()-hitObjects.get(lockedHitObjIndex).getEndTime();
            if(diff<0){
                gamePlayingPane.getPlayingResult().setHitObjectsStatus(hitObjects.get(lockedHitObjIndex),
                        PlayingResult.MISS);
                System.out.println("Hit-L-End-Miss! "+diff);
            }
        }
        lockedHitObjIndex=-1;
    }
}
