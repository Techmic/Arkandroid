package com.alex.arkanoidprototype.controler;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

class MainThread extends Thread{
    public static final int MAX_FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean running){
        this.running = running;
    }

    public MainThread(SurfaceHolder holder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = holder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run(){
        long startTime;
        long ExecTimeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while(running){
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }

            }catch(Exception e){e.printStackTrace();}
            finally {
                if (canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            ExecTimeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - ExecTimeMillis;
            try{
                if (waitTime > 0){
                    this.sleep(waitTime);
                }

            }catch (Exception e){e.printStackTrace();}

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if (frameCount == MAX_FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }
}
