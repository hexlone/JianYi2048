package com.jianyiclub.jianyi2048.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;


import com.jianyiclub.jianyi2048.R;
import com.jianyiclub.jianyi2048.bean.Add;
import com.jianyiclub.jianyi2048.bean.MyCard;
import com.jianyiclub.jianyi2048.bean.Tran;
import com.jianyiclub.jianyi2048.uitls.Config;
import com.jianyiclub.jianyi2048.uitls.DpToPx;
import com.jianyiclub.jianyi2048.uitls.MyColor;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wl624 on 2018/2/21.
 */

public class MyGameView extends View {
    private Context context;
    private MediaPlayer player;
    private int cardSize,backSize,textSize,dd,textHeight,
            num;
    //private RectF[][] rects=new RectF[4][4];
    private MyCard[][] myCards=new MyCard[4][4];
    private Paint backPaint,cardPaint,cardPaint1,textPaint;
    private List<Point> emptyPoints = new ArrayList<Point>();
    private List<Add> listAddPoint = new ArrayList<>();
    private Point fromPoint=null,toPoint=null;
    private List<Tran> listTran = new ArrayList<>();
    private List<Integer> listAnim=new ArrayList<>();
    private AllAnimEnd allAnimEnd=null;
    private ScaleAnimEndListener scaleAnimEndListener=null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    private boolean touchFlag=false;
    private boolean drawCompleteCardFlag=true;
    private MyCard[][] drawCompleteCard=new MyCard[4][4];

    private float scaleProgress=1,tranProgress=1,turnOverProgress=1;
    public MyGameView(Context context) {
        super(context);
        this.context=context;
        player = MediaPlayer.create(context, R.raw.click);
        setPaint();
        setOnTouch();
    }

    public MyGameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        player = MediaPlayer.create(context, R.raw.click);
        setPaint();
        setOnTouch();

    }

    public MyGameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        player = MediaPlayer.create(context, R.raw.click);
        setPaint();
        setOnTouch();

    }
    private void setPaint() {
        cardPaint=new Paint();
        cardPaint1=new Paint();
        textPaint=new Paint();
        backPaint=new Paint();

        textSize=DpToPx.dpToPx(context,28);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        backPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        backPaint.setAntiAlias(true);
        backPaint.setColor(0x00ffffff);
        cardPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        cardPaint.setAntiAlias(true);
        cardPaint.setColor(MyColor.normalCardBack);
        cardPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
        cardPaint1.setAntiAlias(true);
        cardPaint1.setColor(MyColor.normalCardBack);


        Rect rect1 = new Rect();
        String str=123+"";
        textPaint.getTextBounds(str, 0, str.length(), rect1);
        textHeight = rect1.height();
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
    }
    private void setOnTouch() {
        setScaleAnimEndListener(new ScaleAnimEndListener() {
            @Override
            public void onEnd(boolean tt,MyCard[][] my) {
                Log.e("my length",my.length+"");
                touchFlag=tt;
                for (int y=0;y<my.length;y++){
                    for (int x=0;x<my.length;x++){
                        editor.putInt("-"+x+"-"+y,my[x][y].getNum());

                    }

                }
                editor.commit();


            }
        });
        setOnTouchListener(new OnTouchListener() {

            private float startX
                    ,
                    startY
                    ,
                    offsetX
                    ,
                    offsetY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if(touchFlag){

                            if (Math.abs(offsetX) > Math.abs(offsetY)) {
                                if (offsetX < -5) {
                                    //player.start();
                                    Log.e("eee0","ok");
                                    touchFlag=false;
                                    swipeLeft();


                                } else if (offsetX > 5) {
                                    //player.start();
                                    Log.e("eee01","ok");
                                    touchFlag=false;
                                    swipeRight();

                                }
                            } else {
                                if (offsetY < -5) {
                                    //player.start();
                                    Log.e("eee03","ok");
                                    touchFlag=false;
                                    swipeUp();

                                } else if (offsetY > 5) {
                                    //player.start();
                                    Log.e("eee04","ok");
                                    touchFlag=false;
                                    swipeDown();

                                }
                            }

                        }


                        break;
                }
                return true;
            }
        });
    }

    private void swipeLeft() {
        //listTran.clear();
        boolean merge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {

                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (myCards[x1][y].getFlag() > 0) {

                        if (myCards[x][y].getFlag() <=0) {

                            //Log.e("fff",x1+" "+y+" ;;;"+x+" "+y);
                            //startTranAnim(new Point(x1,y),new Point(x,y));
                            //x--;
                            //merge = true;
                            Tran tr=new Tran();
                            tr.setMyCard(myCards[x1][y]);
                            tr.setFromNum(myCards[x1][y].getNum());
                            tr.setFinalNum(myCards[x1][y].getNum());
                            tr.setFromPoint(new Point(x1,y));
                            tr.setToPoint(new Point(x,y));
                            tr.setFlag(false);
                            myCards[x][y].setFlag(myCards[x1][y].getNum());
                            myCards[x1][y].setNum(0);
                            drawCompleteCard[x1][y].setNum(0);
                            myCards[x1][y].setFlag(0);

                            listTran.add(tr);

                            //startTranAnim(false);
                            x--;
                            merge = true;
                        }else  if(myCards[x][y].getFlag()==myCards[x1][y].getFlag()){
                            Tran tr=new Tran();
                            tr.setMyCard(myCards[x1][y]);
                            tr.setFromNum(myCards[x1][y].getNum());
                            tr.setFinalNum(myCards[x1][y].getNum()+myCards[x1][y].getNum());
                            tr.setFromPoint(new Point(x1,y));
                            tr.setToPoint(new Point(x,y));
                            tr.setFlag(true);
                            myCards[x][y].setFlag(myCards[x1][y].getNum()+myCards[x1][y].getNum());
                            myCards[x1][y].setNum(0);
                            drawCompleteCard[x1][y].setNum(0);
                            myCards[x1][y].setFlag(0);
                            listTran.add(tr);
                            //startTranAnim(true);
                            merge = true;
                        }
                        break;
                    }

                }
            }
        }

        if (merge) {
            //addRandomNum(1);
            //checkComplete();
            player.start();
            startTranAnim(true);
        }else {
            Log.e("noTran","onEnd");
            scaleAnimEndListener.onEnd(true,myCards);
        }

    }
    private void swipeRight() {

        boolean merge = false;
        //listTran.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 4 - 1; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (myCards[x1][y].getFlag() > 0) {

                        if (myCards[x][y].getFlag() <= 0) {
                            Tran tr=new Tran();
                            tr.setMyCard(myCards[x1][y]);
                            tr.setFromNum(myCards[x1][y].getNum());
                            tr.setFinalNum(myCards[x1][y].getNum());
                            tr.setFromPoint(new Point(x1,y));
                            tr.setToPoint(new Point(x,y));
                            tr.setFlag(false);
                            myCards[x][y].setFlag(myCards[x1][y].getNum());
                            myCards[x1][y].setNum(0);
                            drawCompleteCard[x1][y].setNum(0);
                            myCards[x1][y].setFlag(0);

                            listTran.add(tr);

                            //startTranAnim(false);
                            x++;
                            merge = true;

                        } else if (myCards[x][y].getFlag()==myCards[x1][y].getFlag()) {
                            Tran tr=new Tran();
                            tr.setMyCard(myCards[x1][y]);
                            tr.setFromNum(myCards[x1][y].getNum());
                            tr.setFinalNum(myCards[x1][y].getNum()+myCards[x1][y].getNum());
                            tr.setFromPoint(new Point(x1,y));
                            tr.setToPoint(new Point(x,y));
                            tr.setFlag(true);
                            myCards[x][y].setFlag(myCards[x1][y].getNum()+myCards[x1][y].getNum());
                            myCards[x1][y].setNum(0);
                            drawCompleteCard[x1][y].setNum(0);
                            myCards[x1][y].setFlag(0);

                            listTran.add(tr);
                            //startTranAnim(true);
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }
        if (merge) {
            //addRandomNum(1);
            //checkComplete();
            player.start();
            startTranAnim(true);
        }else {
            Log.e("noTran","onEnd");
            scaleAnimEndListener.onEnd(true,myCards);
        }

    }

    //向上移动
    private void swipeUp() {


        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {
            for (int y = 0; y < Config.LINES; y++) {

                for (int y1 = y + 1; y1 < Config.LINES; y1++) {
                    if (myCards[x][y1].getFlag() > 0) {

                        if (myCards[x][y].getFlag() <= 0) {

                            Tran tr=new Tran();
                            tr.setMyCard(myCards[x][y1]);
                            tr.setFromNum(myCards[x][y1].getNum());
                            tr.setFinalNum(myCards[x][y1].getNum());
                            tr.setFromPoint(new Point(x,y1));
                            tr.setToPoint(new Point(x,y));
                            tr.setFlag(false);
                            myCards[x][y].setFlag(myCards[x][y1].getNum());
                            myCards[x][y1].setNum(0);
                            drawCompleteCard[x][y1].setNum(0);
                            myCards[x][y1].setFlag(0);

                            listTran.add(tr);

                            //startTranAnim(false);

                            y--;
                            merge = true;


                        } else if (myCards[x][y].getFlag()==myCards[x][y1].getFlag()) {
                            Tran tr=new Tran();
                            tr.setMyCard(myCards[x][y1]);
                            tr.setFromNum(myCards[x][y1].getNum());
                            tr.setFinalNum(myCards[x][y1].getNum()+myCards[x][y1].getNum());
                            tr.setFromPoint(new Point(x,y1));
                            tr.setToPoint(new Point(x,y));
                            tr.setFlag(true);
                            myCards[x][y].setFlag(myCards[x][y1].getNum()+myCards[x][y1].getNum());
                            myCards[x][y1].setNum(0);
                            drawCompleteCard[x][y1].setNum(0);
                            myCards[x][y1].setFlag(0);

                            listTran.add(tr);
                            //startTranAnim(true);
                            merge = true;
                        }

                        break;

                    }
                }
            }
        }
        if (merge) {
            //addRandomNum(1);
            //checkComplete();
            player.start();
            startTranAnim(true);
        }else {
            Log.e("noTran","onEnd");
            scaleAnimEndListener.onEnd(true,myCards);
        }

    }

    //向下移动
    private void swipeDown() {


        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {
            for (int y = Config.LINES - 1; y >= 0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (myCards[x][y1].getFlag() > 0) {

                        if (myCards[x][y].getFlag() <= 0) {
                            Tran tr=new Tran();
                            tr.setMyCard(myCards[x][y1]);
                            tr.setFromNum(myCards[x][y1].getNum());
                            tr.setFinalNum(myCards[x][y1].getNum());
                            tr.setFromPoint(new Point(x,y1));
                            tr.setToPoint(new Point(x,y));
                            tr.setFlag(false);
                            myCards[x][y].setFlag(myCards[x][y1].getNum());
                            myCards[x][y1].setNum(0);
                            drawCompleteCard[x][y1].setNum(0);
                            myCards[x][y1].setFlag(0);

                            listTran.add(tr);

                            //startTranAnim(false);

                            y++;
                            merge = true;

                        } else if (myCards[x][y].getFlag()==myCards[x][y1].getFlag()) {
                            Tran tr=new Tran();
                            tr.setMyCard(myCards[x][y1]);
                            tr.setFromNum(myCards[x][y1].getNum());
                            tr.setFinalNum(myCards[x][y1].getNum()+myCards[x][y1].getNum());
                            tr.setFromPoint(new Point(x,y1));
                            tr.setToPoint(new Point(x,y));
                            tr.setFlag(true);
                            myCards[x][y].setFlag(myCards[x][y1].getNum()+myCards[x][y1].getNum());
                            myCards[x][y1].setNum(0);
                            drawCompleteCard[x][y1].setNum(0);
                            myCards[x][y1].setFlag(0);

                            listTran.add(tr);
                            //startTranAnim(true);
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

//        DialogUtils.getAddChartDialog(context, ggggg.getMainFragment().getScore());

        if (merge) {
            //addRandomNum(1);
            //checkComplete();
            player.start();
            startTranAnim(true);
        }else {
            Log.e("noTran","onEnd");
            scaleAnimEndListener.onEnd(true,myCards);
        }

    }




    public int  setCardColor(int num){
        //this.num=num;

        int aa=0;
        switch (num) {
            case -2:
                break;
            case 0:
                aa=MyColor.normalCardBack;
                break;
            case 2:
                aa=MyColor._2Back;
                break;
            case 4:
                aa=MyColor._4Back;
                break;
            case 8:
                aa=MyColor._8Back;
                break;
            case 16:
                aa=MyColor._16Back;
                break;
            case 32:
                aa=MyColor._32Back;
                break;
            case 64:
                aa=MyColor._64Back;
                break;
            case 128:
                aa=MyColor._128Back;
                break;
            case 256:
                aa=MyColor._256Back;
                break;
            case 512:
                aa=MyColor._512Back;
                break;
            case 1024:
                aa=MyColor._1024Back;
                break;
            case 2048:
                aa=MyColor._2048Back;
                break;
            default:
                aa=MyColor.normalCardBack;
                break;
        }
        if(num!=0){
            //this.backColor=cardPaint.getColor();
        }

        return aa;
        
        //setBack(cardPaint.getColor());
        //invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        backSize=w/4;
        this.cardSize= (int) (backSize*0.95f);
        Log.e("cardSize",cardSize+"");
        dd= (int) (backSize*0.05f);

        setRect();

    }

    private void setRect() {
        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                RectF rect=new RectF(x*backSize+dd,y*backSize+dd,(x+1)*backSize-dd,(y+1)*backSize-dd);
                //rects[x][y]=rect;
                Point point=new Point(backSize/2+x*backSize,backSize/2+y*backSize);
                MyCard mc=new MyCard();
                mc.setRectF(rect);
                mc.setNum(0);
                mc.setFlag(0);
                mc.setPoint(point);
                MyCard mc1=new MyCard();
                mc1.setRectF(rect);
                mc1.setNum(0);
                mc1.setFlag(0);
                mc1.setPoint(point);
                myCards[x][y]=mc;
                drawCompleteCard[x][y]=mc1;

            }
        }
        //Log.e("gg",myCards[3][3].getNum()+"");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.drawRect(0,0,getWidth(),getHeight(),backPaint);
        canvas.restore();





        drawCompleteCard(canvas);


        drawAddCard(canvas);
        drawTranCard(canvas);






    }

    private void drawCompleteCard(Canvas canvas) {

        canvas.save();
        for(int y=0;y<4;y++){
            for(int x=0;x<4;x++){

                cardPaint1.setColor(setCardColor(drawCompleteCard[x][y].getNum()));
                textPaint.setTextSize(textSize);
                //setNum(myCards[x][y].getNum());
                canvas.drawRoundRect(drawCompleteCard[x][y].getRectF(),DpToPx.dpToPx(context,8),DpToPx.dpToPx(context,8),cardPaint1);
                canvas.drawText(drawCompleteCard[x][y].getNum()>0?(drawCompleteCard[x][y].getNum()+""):"",
                        drawCompleteCard[x][y].getRectF().left+cardSize/2,drawCompleteCard[x][y].getRectF().top+cardSize/2+textHeight/2,textPaint);
            }
        }
        canvas.restore();
    }

    private void drawTranCard(Canvas canvas) {
        canvas.save();
        for(int i=0;i<listTran.size();i++){
            float tranX=(listTran.get(i).getFromPoint().x-listTran.get(i).getToPoint().x)*backSize;
            float tranY=(listTran.get(i).getFromPoint().y-listTran.get(i).getToPoint().y)*backSize;
            //如果tranX>0 向左
            //如果tranY>0 向上

            float left=listTran.get(i).getMyCard().getRectF().left;
            float top=listTran.get(i).getMyCard().getRectF().top;
            float right=listTran.get(i).getMyCard().getRectF().right;
            float bottom=listTran.get(i).getMyCard().getRectF().bottom;
            //Log.e("num     ddd",listTran.get(i).getNum()+"");
            cardPaint.setColor(setCardColor(listTran.get(i).getFromNum()));
            textPaint.setTextSize(textSize);
            canvas.drawRoundRect(left-tranX*tranProgress,top-tranY*tranProgress,right-tranX*tranProgress,
                    bottom-tranY*tranProgress,DpToPx.dpToPx(context,8),DpToPx.dpToPx(context,8),cardPaint);


            canvas.drawText(listTran.get(i).getFromNum()>0?(listTran.get(i).getFromNum()+""):"",
                    left-tranX*tranProgress+cardSize/2,top-tranY*tranProgress+cardSize/2+textHeight/2,textPaint);
        }
        canvas.restore();
    }

    private void drawAddCard(Canvas canvas) {
        canvas.save();
        for (int i=0;i<listAddPoint.size();i++){
            cardPaint.setColor(setCardColor(listAddPoint.get(i).getNum()));
            float left=myCards[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].getRectF().left;
            float top=myCards[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].getRectF().top;
            float right=myCards[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].getRectF().right;
            float bottom=myCards[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].getRectF().bottom;
            canvas.drawRoundRect(left+(cardSize/2)*(1-scaleProgress),top+(cardSize/2)*(1-scaleProgress),
                    right-(cardSize/2)*(1-scaleProgress),bottom-(cardSize/2)*(1-scaleProgress),DpToPx.dpToPx(context,8),DpToPx.dpToPx(context,8),cardPaint);
            textPaint.setTextSize(textSize*scaleProgress);
            canvas.drawText(listAddPoint.get(i).getNum()>0?(listAddPoint.get(i).getNum()+""):"",
                    myCards[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].getRectF().left+cardSize/2,
                    myCards[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].getRectF().top+cardSize/2+textHeight/2,textPaint);
        }
        canvas.restore();
    }

    public void startGame() {

        //ggggg aty = ggggg.getMainFragment();
        //aty.clearScore();
        //aty.showBestScore(aty.getBestScore());


        listAddPoint.clear();
        addRandomNum(2);
    }

    private void addRandomNum(int a) {
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                    if (myCards[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        if (emptyPoints.size() > 0) {

            if(a==1){
                Point p = emptyPoints.remove((int) (Math.random() * emptyPoints
                        .size()));
                Add add = new Add();
                add.setPoint(p);
                add.setNum(Math.random() > 0.1 ? 2 : 4);
                listAddPoint.add(add);
            }else {
                for(int i=0;i<2;i++){
                    Point p = emptyPoints.remove((int) (Math.random() * emptyPoints
                            .size()));
                    Add add = new Add();
                    add.setPoint(p);
                    add.setNum(Math.random() > 0.1 ? 2 : 4);
                    listAddPoint.add(add);
                }
            }


        }
        startScaleAnim();

    }

    public void startScaleAnim(){
        ObjectAnimator animator=ObjectAnimator.ofFloat(this,"scaleProgress",0,1);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(120);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

                drawCompleteCardFlag=true;
                for (int i=0;i<listAddPoint.size();i++){
                    //myCards[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].setNum(listAddPoint.get(i).getNum());

                    myCards[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].setFlag(listAddPoint.get(i).getNum());
                    myCards[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].setNum(listAddPoint.get(i).getNum());


                }

                scaleAnimEndListener.onEnd(true,myCards);
            }

            @Override
            public void onAnimationEnd(Animator animator) {



                for (int i=0;i<listAddPoint.size();i++){
                    //myCards[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].setNum(listAddPoint.get(i).getNum());


                    drawCompleteCard[listAddPoint.get(i).getPoint().x][listAddPoint.get(i).getPoint().y].setNum(listAddPoint.get(i).getNum());


                }

                drawCompleteCardFlag=true;
                //drawCompleteCard=myCards;
                listAddPoint.clear();
                //scaleAnimEndListener.onEnd(true,myCards);
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }
    public void setScaleProgress(float scaleProgress) {
        this.scaleProgress = scaleProgress;
        invalidate();
    }
    private void startTranAnim(boolean ww) {
        final int listSize=listTran.size();
        listAnim.clear();
        this.setAllAnimEndListener(new AllAnimEnd() {
            @Override
            public void onEnd(int a) {
                Log.e("all",a+"  "+listSize);
               if(a==listSize){
                   addRandomNum(1);
               }
            }
        });

        for(int j=0;j<listTran.size();j++){

            if(listTran.get(j).isFlag()){
                ObjectAnimator animator=ObjectAnimator.ofFloat(this,"tranProgress",0,1);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(150);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        for(int i=0;i<listTran.size();i++){
                            myCards[listTran.get(i).getFromPoint().x][listTran.get(i).getFromPoint().y].setNum(0);
                            drawCompleteCard[listTran.get(i).getFromPoint().x][listTran.get(i).getFromPoint().y].setNum(0);
                        }

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {


                        for(int i=0;i<listTran.size();i++){
                            //Log.e("num",listTran.get(i).getNum()+"");
                            listTran.get(i).setFromNum(listTran.get(i).getFinalNum());
                            myCards[listTran.get(i).getToPoint().x][listTran.get(i).getToPoint().y].setNum(listTran.get(i).getFinalNum());
                            drawCompleteCard[listTran.get(i).getToPoint().x][listTran.get(i).getToPoint().y].setNum(listTran.get(i).getFinalNum());
                        }
                        listTran.clear();
                        listAddPoint.clear();
                        //addRandomNum(1);
                        int aa=0;
                        listAnim.add(aa);
                        allAnimEnd.onEnd(listAnim.size());
                        invalidate();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                animator.start();


                Log.e("turnOver",11+"");
            }else {
                ObjectAnimator animator=ObjectAnimator.ofFloat(this,"tranProgress",0,1);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(150);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        for(int i=0;i<listTran.size();i++){
                            myCards[listTran.get(i).getFromPoint().x][listTran.get(i).getFromPoint().y].setNum(0);
                            drawCompleteCard[listTran.get(i).getFromPoint().x][listTran.get(i).getFromPoint().y].setNum(0);
                        }

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        for(int i=0;i<listTran.size();i++){
                            //Log.e("num",listTran.get(i).getNum()+"");
                            myCards[listTran.get(i).getToPoint().x][listTran.get(i).getToPoint().y].setNum(listTran.get(i).getFinalNum());
                            drawCompleteCard[listTran.get(i).getToPoint().x][listTran.get(i).getToPoint().y].setNum(listTran.get(i).getFinalNum());
                        }
                        listTran.clear();
                        listAddPoint.clear();
                        int aa=0;
                        listAnim.add(aa);
                        allAnimEnd.onEnd(listAnim.size());
                        //addRandomNum(1);
                        invalidate();

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animator.start();
            }
        }



    }
    public void setTranProgress(float tranProgress) {
        this.tranProgress = tranProgress;
        invalidate();
    }
    public void setTurnOverProgress(float turnOverProgress){
        this.turnOverProgress=turnOverProgress;
        invalidate();
    }
    interface AllAnimEnd{
        public void onEnd(int a);
    }
    public void setAllAnimEndListener(AllAnimEnd all){
        allAnimEnd=all;
    }
    interface ScaleAnimEndListener{
        public void onEnd(boolean tt,MyCard[][] my);
    }

    public void setScaleAnimEndListener(ScaleAnimEndListener scaleAnim){
        scaleAnimEndListener=scaleAnim;
    }
}
