package com.example.administrator.myhomework;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/5/22 0022.
 */

public class WuziqiPanel extends View {
    private MediaPlayer mediaPlayer;

    private int mPanelWidth;  //棋盘宽度
    private float mLineHeight;  //棋盘单行的间距
    private int MAX_LINE;  //棋盘的最大行列数

    private Paint mPaint = new Paint();
    private int mPanelLineColor; //棋盘线的颜色

    private Bitmap mWhitePiece; //白棋的图片
    private Bitmap mBlackPiece; //黑棋的图片

    private final float RATIO_PIECE_OF_LINE_HEIGHT = 3 * 1.0f / 4;//棋子占行距的比例

    private boolean mIsWhite = true; //判断下子的是否为白方

    private ArrayList<Point> mWhitePieceArray = new ArrayList<>(); //已下的白子的列表
    private ArrayList<Point> mBlackPieceArray = new ArrayList<>(); //已下的黑子的列表

    private boolean mIsGameOver;  //判断游戏是否结束

    final int INIT_WIN = -1; //游戏刚开始的状态
    public final static int WHITE_WIN = 0;  //白子赢
    public final static int BLACK_WIN = 1;  //黑子赢
    public static final int NO_WIN = 2; //和棋

    private int mGameWinResult = INIT_WIN; //初始化游戏结果

    private OnGameStatusChangListener listener;//游戏状态监听器

    private OnWhiteChangListener listener1;

    private int MAX_COUNT_IN_LINE;  //定义多少棋子相邻是获得胜利

    public void setOnGameStatusChangeListener(OnGameStatusChangListener listener) {
        this.listener = listener;
    }

    public void setOnWhiteChangListener(OnWhiteChangListener listener1){
        this.listener1 = listener1;
    }


    public WuziqiPanel(Context context) {
        this(context, null);
    }

    public WuziqiPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WuziqiPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取xml中自定义的属性值并对相应的属性赋值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WuziqiPanel);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attrName = a.getIndex(i);
            switch (attrName) {
                //棋盘背景
                case R.styleable.WuziqiPanel_panel_background:
                    BitmapDrawable panelBackgroundBitmap = (BitmapDrawable) a.getDrawable(attrName);
                    setBackground(panelBackgroundBitmap);
                    break;
                //棋盘线的颜色
                case R.styleable.WuziqiPanel_panel_line_color:
                    mPanelLineColor = a.getColor(attrName, 0x88000000);
                    break;
                //白棋图片
                case R.styleable.WuziqiPanel_white_piece_img:
                    BitmapDrawable whitePieceBitmap = (BitmapDrawable) a.getDrawable(attrName);
                    mWhitePiece = whitePieceBitmap.getBitmap();
                    break;
                //黑棋图片
                case R.styleable.WuziqiPanel_black_piece_img:
                    BitmapDrawable blackPieceBitmap = (BitmapDrawable) a.getDrawable(attrName);
                    mBlackPiece = blackPieceBitmap.getBitmap();
                    break;
                case R.styleable.WuziqiPanel_max_count_line:
                    MAX_LINE = a.getInteger(attrName, 10);
                    break;
                case R.styleable.WuziqiPanel_max_win_count_piece:
                    MAX_COUNT_IN_LINE = a.getInteger(attrName, 5);
                    break;

            }
        }
        init();//调用初始化
    }

    //初始化函数
    private void init() {
        mPaint.setColor(mPanelLineColor);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        if (mWhitePiece == null) {
            mWhitePiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_w2);//白子图片
        }
        if (mBlackPiece == null) {
            mBlackPiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_b1);//黑子图片
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        //解决嵌套在ScrollView中时等情况出现的问题
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }

        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * RATIO_PIECE_OF_LINE_HEIGHT);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        checkGameOver();
    }

    //定义检查游戏是否结束的函数
    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhitePieceArray);
        boolean blackWin = checkFiveInLine(mBlackPieceArray);
        boolean noWin = checkNoWin(whiteWin, blackWin);
        //如果游戏结束,获取游戏结果mGameWinResult
        if (whiteWin) {
            mGameWinResult = WHITE_WIN;
        } else if (blackWin) {
            mGameWinResult = BLACK_WIN;
        } else if (noWin) {
            mGameWinResult = NO_WIN;
        }
        if (whiteWin || blackWin || noWin) {
            mIsGameOver = true;
            //回调游戏状态接口
            if (listener != null) {
                listener.onGameOver(mGameWinResult);
            }
        }
    }

    //检查是否有连珠
    private boolean checkFiveInLine(ArrayList<Point> points) {
        for (Point point : points) {
            int x = point.x;
            int y = point.y;

            boolean checkHorizontal = checkHorizontalFiveInLine(x, y, points);
            boolean checkVertical = checkVerticalFiveInLine(x, y, points);
            boolean checkLeftDiagonal = checkLeftDiagonalFiveInLine(x, y, points);
            boolean checkRightDiagonal = checkRightDiagonalFiveInLine(x, y, points);
            //当直线、竖线、斜上方、斜下方任意一条线满足则返回true
            if (checkHorizontal || checkVertical || checkLeftDiagonal || checkRightDiagonal) {
                return true;
            }
        }

        return false;
    }

    //检查横线上是否有五子连珠
    private boolean checkHorizontalFiveInLine(int x, int y, ArrayList<Point> points) {
        int count = 1;
        for (int i = 1;i < MAX_COUNT_IN_LINE;i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        for (int i = 1;i < MAX_COUNT_IN_LINE;i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }

        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    //检查竖线上有没有相同棋子的五子连珠
    private boolean checkVerticalFiveInLine(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1;i < MAX_COUNT_IN_LINE;i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        for (int i = 1;i < MAX_COUNT_IN_LINE;i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }

        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    //检查向左斜的线上有没有相同棋子的五子连珠
    private boolean checkLeftDiagonalFiveInLine(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1;i < MAX_COUNT_IN_LINE;i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        for (int i = 1;i < MAX_COUNT_IN_LINE;i++) {
            if (points.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }

        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    //检查向右斜的线上有没有相同棋子的五子连珠
    private boolean checkRightDiagonalFiveInLine(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1;i < MAX_COUNT_IN_LINE;i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        for (int i = 1;i < MAX_COUNT_IN_LINE;i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }

        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    //检查是否和棋
    private boolean checkNoWin(boolean whiteWin, boolean blackWin) {
        if (whiteWin || blackWin) {
            return false;
        }
        int maxPieces = MAX_LINE * MAX_LINE;
        //如果白棋和黑棋的总数等于棋盘格子数,说明和棋
        if (mWhitePieceArray.size() + mBlackPieceArray.size() == maxPieces) {
            return true;
        }
        return false;
    }

    public void restartGame(){
        mWhitePieceArray.clear();
        mBlackPieceArray.clear();
        mIsGameOver = false;
        mGameWinResult = INIT_WIN;
        invalidate();
    }

    //绘制棋子
    private void drawPiece(Canvas canvas) {
        for (int i = 0,n = mWhitePieceArray.size();i < n;i++) {
            Point whitePoint = mWhitePieceArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x + (1 -RATIO_PIECE_OF_LINE_HEIGHT) / 2) * mLineHeight,
                    (whitePoint.y + (1 -RATIO_PIECE_OF_LINE_HEIGHT) / 2) * mLineHeight,null);
        }
        for (int i = 0,n = mBlackPieceArray.size();i < n;i++) {
            Point blackPoint = mBlackPieceArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x + (1 -RATIO_PIECE_OF_LINE_HEIGHT) / 2) * mLineHeight,
                    (blackPoint.y + (1 -RATIO_PIECE_OF_LINE_HEIGHT) / 2) * mLineHeight,null);
        }
    }

    //绘制棋盘
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0;i < MAX_LINE; i ++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);

            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);//画横线
            canvas.drawLine(y, startX, y, endX, mPaint);//画竖线
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x, y);
            if (mWhitePieceArray.contains(p) || mBlackPieceArray.contains(p)) {
                return false;
            }

            if (mIsWhite) {
                mWhitePieceArray.add(p);
            } else {
                mBlackPieceArray.add(p);
            }
            invalidate();
            mIsWhite = !mIsWhite;
            return true;
        }
        if (listener1!=null){
            listener1.onWhite(mIsWhite);

        }
        return true;


    }

    //根据触摸点获取最近的格子位置
    private Point getValidPoint(int x, int y) {
        return new Point((int)(x / mLineHeight),(int)(y / mLineHeight));
    }


}
