/**
 * Copyright 2014  XCL-Charts
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Project XCL-Charts
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.evguard.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGrid;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName SplineChart01View
 * @Description 曲线图 的例子
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */
public class SplineChartView extends DemoView {

    private String TAG = "SplineChart04View";
    private SplineChart chart = new SplineChart();
    // 分类轴标签集合
    private LinkedList<String> labels = new LinkedList<String>();
    private LinkedList<SplineData> chartData = new LinkedList<SplineData>();
    Paint pToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);
    private double mAxisMax = 0;
    private double mAxisMin = 0;
    private int mAxisSteps = 0;
    private double mCategoryAxisMax = 0;
    private double mCategoryAxisMin = 0;

    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();

    public SplineChartView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView();
    }

    public SplineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SplineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        // chartLabels();
        // chartDataSet();
        // chartDesireLines();
        // chartRender();
        //
        // //綁定手势滑动事件
        // this.bindTouch(this,chart);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 图所占范围大小
        chart.setChartRange(w, h);
    }

    private void chartRender() {
        try {

            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            // int [] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(20, 15, 15, 20);

            // //标题
            // chart.setTitle("New GitHub repositories");
            // chart.addSubtitle("(XCL-Charts Demo)");
            // chart.getAxisTitle().setLeftTitle("Percentage (annual)");
            // chart.getAxisTitle().getLeftTitlePaint().setColor(Color.BLACK);

            // 显示边框
            // chart.showRoundBorder();

            // 数据源
            chart.setCategories(labels);
            chart.setDataSource(chartData);
            // chart.setCustomLines(mCustomLineDataset);

            // 坐标系
            // 数据轴最大值
            chart.getDataAxis().setAxisMax(20);
            // chart.getDataAxis().setAxisMin(0);
            // 数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(5);

            // 标签轴最大值
            chart.setCategoryAxisMax(6);
            // 标签轴最小值
            chart.setCategoryAxisMin(0);

            // 背景网格
            PlotGrid plot = chart.getPlotGrid();
            plot.hideHorizontalLines();
            plot.hideVerticalLines();
            chart.getDataAxis().getAxisPaint()
                    .setColor(Color.rgb(127, 204, 204));
            chart.getCategoryAxis().getAxisPaint()
                    .setColor(Color.rgb(127, 204, 204));

            chart.getDataAxis().getTickMarksPaint()
                    .setColor(Color.rgb(127, 204, 204));
            chart.getCategoryAxis().getTickMarksPaint()
                    .setColor(Color.rgb(127, 204, 204));

            // 定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

                @Override
                public String textFormatter(String value) {
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (label);
                }
            });

            // 不使用精确计算，忽略Java计算误差,提高性能
            chart.disableHighPrecision();

            chart.disablePanMode();
            chart.hideBorder();
            chart.getPlotLegend().hide();

            // chart.getCategoryAxis().setLabelLineFeed(XEnum.LabelLineFeed.ODD_EVEN);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    private void chartDataSet() {
        // //线1的数据集
        // List<PointD> linePoint1 = new ArrayList<PointD>();
        // linePoint1.add(new PointD(0d, 3d));
        // linePoint1.add(new PointD(1d, 9d));
        // linePoint1.add(new PointD(2d, 8d));
        // linePoint1.add(new PointD(3d, 7d));
        // linePoint1.add(new PointD(6d, 15d));
        // SplineData dataSeries1 = new SplineData("Go",linePoint1,
        // Color.rgb(54, 141, 238) );
        // //把线弄细点
        // dataSeries1.getLinePaint().setStrokeWidth(3);
        // dataSeries1.setLineStyle(XEnum.LineStyle.DASH);
        // dataSeries1.setLabelVisible(false);
        // dataSeries1.setDotStyle(XEnum.DotStyle.HIDE);
        //
        // chartData.add(dataSeries1);

    }

    private void chartLabels() {
        labels.add("2018");
        labels.add("2019");
        labels.add("2020");
        labels.add("2021");
        labels.add("2022");
        labels.add("2023");
    }

    private void chartDesireLines() {
        CustomLineData s = new CustomLineData("", 15d, Color.rgb(54, 141, 238),
                3);

        s.hideLine();
        s.getLineLabelPaint().setColor(Color.rgb(54, 141, 238));
        s.getLineLabelPaint().setTextSize(27);
        s.setLineStyle(XEnum.LineStyle.DASH);
        s.setLabelOffset(5);
        mCustomLineDataset.add(s);

    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void setAxisMax(int max) {
        this.mAxisMax = max;
    }

    public void setAxisMin(int min) {
        this.mAxisMin = min;
    }

    public void setCategoryAxisMin(int min) {
        this.mCategoryAxisMin = min;
    }

    public void setCategoryAxisMax(int max) {
        this.mCategoryAxisMax = max;
    }

    public void setLables(LinkedList<String> label) {
        for (String string : label) {
            System.out.println("labels::  "  + string);
        }
        this.labels = label;
    }

    public void setChartData(List<PointD> points) {
        SplineData dataSeries = new SplineData("", points, Color.rgb(54, 141,
                238));
        for (PointD pointD : points) {
            System.out.println("pointD.x--" + pointD.x + "pointD.y--"
                    + pointD.y);
        }
        // 把线弄细点
        dataSeries.getLinePaint().setStrokeWidth(8);
        dataSeries.setLineStyle(XEnum.LineStyle.DASH);
        dataSeries.setLabelVisible(false);
        dataSeries.setDotStyle(XEnum.DotStyle.HIDE);
        chartData.clear();
        chartData.add(dataSeries);
        chartReRender();
        this.refreshChart();
    }

    private void chartReRender() {
        // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
        int[] ltrb = getBarLnDefaultSpadding();
        chart.setPadding(ltrb[0] + DensityUtil.dip2px(this.getContext(), 10),
                ltrb[1], ltrb[2] + DensityUtil.dip2px(this.getContext(), 20),
                ltrb[3]);
        chart.setCategories(labels);

        chart.getDataAxis().setAxisMax(mAxisMax);
        chart.getDataAxis().setAxisMin(0);
        // 数据轴刻度间隔
        chart.getDataAxis().setAxisSteps(mAxisMax / 2);
        chart.getDataAxis().hideFirstTick();
        // // 标签轴最大值
        chart.setCategoryAxisMax(mCategoryAxisMax);
        // // 标签轴最小值
        chart.setCategoryAxisMin(mCategoryAxisMin);
        chart.getDataAxis().getAxisPaint().setColor(Color.rgb(205, 205, 205));
        chart.getCategoryAxis().getAxisPaint()
                .setColor(Color.rgb(205, 205, 205));
        chart.getDataAxis().getTickMarksPaint().setColor(0Xfff3f3f3);
        chart.getDataAxis().getTickLabelPaint().setColor(0Xfff3f3f3);
        chart.getCategoryAxis().getTickLabelPaint().setColor(0Xfff3f3f3);
        chart.getCategoryAxis().getTickLabelPaint().setTextSize(25);

        chart.getDataAxis().setTickLabelMargin(30);
        chart.getDataAxis().setTickLabelMargin(35);

        // 定义数据轴标签显示格式
        chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

            @Override
            public String textFormatter(String value) {
                // TODO Auto-generated method stub
                Double tmp = Double.parseDouble(value);
                DecimalFormat df = new DecimalFormat("#0");
                String label = df.format(tmp).toString();
                return (label);
            }
        });

        // 不使用精确计算，忽略Java计算误差,提高性能
        chart.disableHighPrecision();

        chart.disablePanMode();
        chart.hideBorder();
        chart.getPlotLegend().hide();
        // // 隐藏刻度线
        chart.getCategoryAxis().hideTickMarks();
        chart.getDataAxis().setTickLabelMargin(25);
        chart.getCategoryAxis().setTickLabelMargin(35);
        chart.setDataSource(chartData);
    }

    public void chartRefresh() {
        // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
        int[] ltrb = getBarLnDefaultSpadding();
        chart.setPadding(ltrb[0] + DensityUtil.dip2px(this.getContext(), 10),
                ltrb[1], ltrb[2] + DensityUtil.dip2px(this.getContext(), 10),
                ltrb[3]);
        chart.setCategories(labels);
        // // 标签轴最大值
        chart.setCategoryAxisMax(mCategoryAxisMax);
        // // 标签轴最小值
        chart.setCategoryAxisMin(mCategoryAxisMin);
        chart.getDataAxis().getAxisPaint().setColor(Color.rgb(205, 205, 205));
        chart.getCategoryAxis().getAxisPaint()
                .setColor(Color.rgb(205, 205, 205));
        chart.getDataAxis().getTickMarksPaint().setColor(0Xfff3f3f3);
        chart.getDataAxis().getTickLabelPaint().setColor(0Xfff3f3f3);
        chart.getCategoryAxis().getTickLabelPaint().setColor(0Xfff3f3f3);
        chart.getCategoryAxis().getTickLabelPaint().setTextSize(25);

        chart.getDataAxis().setTickLabelMargin(30);
        chart.getDataAxis().setTickLabelMargin(35);

        // 定义数据轴标签显示格式
        chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

            @Override
            public String textFormatter(String value) {
                // TODO Auto-generated method stub
                Double tmp = Double.parseDouble(value);
                DecimalFormat df = new DecimalFormat("#0");
                String label = df.format(tmp).toString();
                return (label);
            }
        });

        // 不使用精确计算，忽略Java计算误差,提高性能
        chart.disableHighPrecision();

        chart.disablePanMode();
        chart.hideBorder();
        chart.getPlotLegend().hide();
        // // 隐藏刻度线
        chart.getCategoryAxis().hideTickMarks();

        chart.getDataAxis().setTickLabelMargin(25);
        chart.getCategoryAxis().setTickLabelMargin(35);
        this.refreshChart();
    }

}
