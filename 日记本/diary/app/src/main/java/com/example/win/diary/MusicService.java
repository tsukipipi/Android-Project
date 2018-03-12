package com.example.win.diary;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {

    //定义一个MediaPlayer类的对象
    public MediaPlayer mediaPlayer;

    public MusicService() {
    }

    //让客户端通过Binder实现访问服务中的方法
    class MyBinder extends Binder {

        //播放音乐
        public void play(String path) {
            try {
                if (mediaPlayer == null) {
                    //创建一个MediaPlayer播放器
                    mediaPlayer = new MediaPlayer();
                    //指定参数为音频文件
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    //指定播放的路径
                    mediaPlayer.setDataSource(path);
                    //准备播放
                    mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            //开始播放
                            mediaPlayer.start();
                        }
                    });
                }//end if
                else {
                    int position = getCurrentProgress();
                    mediaPlayer.seekTo(position);
                    try {
                        //开始播放
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//end play

        //暂停播放
        public void pause(){
            if(mediaPlayer != null && mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
            /*else {
                mediaPlayer.start();
            }*/
        }//end pause
    }

    //获取当前播放位置
    public int getCurrentProgress(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            return  mediaPlayer.getCurrentPosition();
        }
        else if(mediaPlayer != null && (!mediaPlayer.isPlaying())){
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        //mediaPlayer = MediaPlayer.create(this,R.raw.bilibili);
    }

    @Override
    public void onDestroy(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

}
