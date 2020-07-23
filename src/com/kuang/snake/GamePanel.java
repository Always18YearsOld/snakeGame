package com.kuang.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * @author ：ltb
 * @date ：2020/7/23
 */
public class GamePanel extends JPanel implements KeyListener, ActionListener {

    int length;
    int[] snakeX = new int[600];//蛇的X坐标
    int[] snakeY = new int[500];//蛇的Y坐标
    //方向
    String direct;
    //游戏是否开始
    boolean isStart = false;

    Timer timer = new Timer(100, this);//定时器

    //定义一个食物
    int foodX;
    int foodY;
    Random ran = new Random();

    //死亡判断
    boolean isFail = false;


    //积分系统
    int score;


    //构造器
    public GamePanel() {
        init();
        //获取键盘的监听事件
        this.setFocusable(true);
        this.addKeyListener(this);
        timer.start(); //让时间动起来
    }


    //初始化游戏数据
    public void init() {
        length = 3;
        //头部坐标
        snakeX[0] = 100;
        snakeY[0] = 100;
        //第一个身体坐标
        snakeX[1] = 75;
        snakeY[1] = 100;
        //第二个身体坐标
        snakeX[2] = 50;
        snakeY[2] = 100;
        direct = "R";

        //初始化食物
        foodX = 25 + 25 * ran.nextInt(34);
        foodY = 75 + 25 * ran.nextInt(24);

        score = 0;

    }


    //画板：画界面，画蛇
    //Graphics：画笔
    @Override
    protected void paintComponent(Graphics g) {
        //清屏
        super.paintComponent(g);
        this.setBackground(Color.WHITE);

        //绘制头部的广告栏
        Data.header.paintIcon(this, g, 25, 11);

        //绘制游戏区域
        g.fillRect(25, 75, 850, 600);

        if ("R".equalsIgnoreCase(direct)) {
            Data.right.paintIcon(this, g, snakeX[0], snakeY[0]);
        } else if ("L".equalsIgnoreCase(direct)) {
            Data.left.paintIcon(this, g, snakeX[0], snakeY[0]);
        } else if ("U".equalsIgnoreCase(direct)) {
            Data.up.paintIcon(this, g, snakeX[0], snakeY[0]);
        } else if ("D".equalsIgnoreCase(direct)) {
            Data.down.paintIcon(this, g, snakeX[0], snakeY[0]);
        }

        for (int i = 1; i < length; i++) {
            Data.body.paintIcon(this, g, snakeX[i], snakeY[i]);
        }


        //画食物
        Data.food.paintIcon(this, g, foodX, foodY);

        //画积分
        g.setColor(Color.WHITE);
        g.setFont(new Font("微软雅黑", Font.BOLD, 18));
        g.drawString("长度：" + length, 750, 35);
        g.drawString("分数：" + score, 750, 50);


        //游戏提示：是否开始
        if (isStart == false) {
            //画一个文字，String
            g.setColor(Color.WHITE);
            //设置字体
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("按下空格开始游戏", 300, 300);
        }

        //失败提醒
        if (isFail) {
            //画一个文字，String
            g.setColor(Color.RED);
            //设置字体
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("游戏失败，按下空格重新开始", 200, 300);
        }
    }


    //接收键盘的输入：监听
    @Override
    public void keyPressed(KeyEvent e) {
        //键盘按下，未释放
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {     //如果按下的是空格键
            if (isFail) {    //失败，游戏再来一遍
                isFail = false;
                init();     //重新初始化游戏
            } else {
                isStart = !isStart;
            }
            repaint();  //刷新界面
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            direct = "L";
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            direct = "R";
        } else if (keyCode == KeyEvent.VK_UP) {
            direct = "U";
        } else if (keyCode == KeyEvent.VK_DOWN) {
            direct = "D";
        }
    }

    //定时器，监听事件流动：帧：执行定时操作
    @Override
    public void actionPerformed(ActionEvent e) {
        //如果游戏处于开始状态，并且游戏没有结束
        if (isStart && !isFail) {
            for (int i = length - 1; i > 0; i--) {    //除了脑袋，身体都向前移动
                snakeX[i] = snakeX[i - 1];
                snakeY[i] = snakeY[i - 1];
            }

            //通过控制方向让头部移动
            if ("R".equalsIgnoreCase(direct)) {
                //右移
                snakeX[0] += 25;    //头部移动
                //边界判断
                if (snakeX[0] > 850) {
                    snakeX[0] = 25;
                }
            } else if ("L".equalsIgnoreCase(direct)) {
                //左移
                snakeX[0] -= 25;    //头部移动
                //边界判断
                if (snakeX[0] < 25) {
                    snakeX[0] = 850;
                }
            } else if ("U".equalsIgnoreCase(direct)) {
                //上移
                snakeY[0] -= 25;    //头部移动
                //边界判断
                if (snakeY[0] < 75) {
                    snakeY[0] = 650;
                }
            } else if ("D".equalsIgnoreCase(direct)) {
                //下移
                snakeY[0] += 25;    //头部移动
                //边界判断
                if (snakeY[0] > 650) {
                    snakeY[0] = 75;
                }
            }

            //如果小蛇的头和食物坐标重合
            if (snakeX[0] == foodX && snakeY[0] == foodY) {
                //长度+1
                length++;

                score +=10;

                //重新生成食物
                foodX = 25 + 25 * ran.nextInt(34);
                foodY = 75 + 25 * ran.nextInt(24);
            }

            //结束判断
            for (int i = 1; i < length; i++) {
                if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                    isFail = true;
                }
            }


            //刷新界面
            repaint();
        }
        timer.start(); //让时间动起来
    }


    @Override
    public void keyTyped(KeyEvent e) {
        //键盘按下，弹起：敲击
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //释放某个键
    }


}
