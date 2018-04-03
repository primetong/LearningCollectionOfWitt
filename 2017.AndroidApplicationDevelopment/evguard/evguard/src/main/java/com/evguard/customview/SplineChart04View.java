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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGrid;

import com.audrey.mode.TableShowViewData;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @ClassName SplineChart01View
 * @Description 曲线图 的例子
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */
public class SplineChart04View extends DemoView {

	private String TAG = "SplineChart04View";
	private SplineChart chart = new SplineChart();
	
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	// 分类轴标签集合
	private LinkedList<String> labels = new LinkedList<String>();
	private LinkedList<SplineData> chartData = new LinkedList<SplineData>();
	Paint pToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);

	private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();

	public SplineChart04View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public SplineChart04View(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public SplineChart04View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		// chartLabels();
		// chartDataSet();
		// chartDesireLines();
		// chartRender();

		// 綁定手势滑动事件
		// this.bindTouch(this,chart);

		// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
//		int[] ltrb = getBarLnDefaultSpadding();
//		chart.setPadding(ltrb[0] + DensityUtil.dip2px(this.getContext(), 10),
//				ltrb[1], ltrb[2] + DensityUtil.dip2px(this.getContext(), 20),
//				ltrb[3]);
//		// 显示边框
//		chart.showRoundBorder();
//		// 坐标系
//		// 数据轴最大值
//		chart.getDataAxis().setAxisMax(mAxisMax);
//		// chart.getDataAxis().setAxisMin(0);
//		// 数据轴刻度间隔
//		chart.getDataAxis().setAxisSteps(mAxisSteps);
//
//		// 标签轴最大值
//		chart.setCategoryAxisMax(mCategoryAxisMax);
//		// 标签轴最小值
//		chart.setCategoryAxisMin(mCategoryAxisMin);
//
//		// 隐藏标尺
//		chart.getDataAxis().hideAxisLine();
//		chart.getCategoryAxis().hideAxisLine();
	}

	@Override
	public void refreshChart() {
		super.refreshChart();
	}

	public void setChartData(List<PointD> linePoint) {
		chartLabel(linePoint);
		SplineData dataSeries = new SplineData("", linePoint, Color.rgb(
				0, 192, 255));
		dataSeries.getLinePaint().setStrokeWidth(6);
		dataSeries.setLabelVisible(false);
		dataSeries.setDotStyle(XEnum.DotStyle.HIDE);
		chartData.add(dataSeries);
//		chartDesireLines();
		chartRender();
		refreshChart();
	}

	private void chartLabel(List<PointD> linePoint) {
//		calcrucial(linePoint);
//		Date dmin = new Date((long) mCategoryAxisMin);
//		String minVlaue = format.format(dmin);
//		Date dmax = new Date((long) mCategoryAxisMax);
//		String maxVlaue = format.format(dmax);
//		labels.clear();
//		labels.add(minVlaue);
//		labels.add(maxVlaue);
	}
	
	private double mAxisMax = 0;
	private double mAxisMin = 0;
	private int mAxisSteps = 0;
	private double mCategoryAxisMax = 0;
	private double mCategoryAxisMin = 0;
	//设置数值轴最大值
	public void setAxisMax(double axisMax) {
		this.mAxisMax = axisMax;
	}
	//设置数值轴最大值
	public void setAxisMin(double axisMin) {
		this.mAxisMin = axisMin;
	}
	//设置数值轴步长
	public void setAxisSteps(int axisSteps) {
		this.mAxisSteps = axisSteps;
	}
	//设置横轴最小值
	public void setCategoryAxisMin(double categoryAxisMin) {
		this.mCategoryAxisMin = categoryAxisMin;
	}
	//设置横轴最大值
	public void setCategoryAxisMax(double categoryAxisMax) {
		this.mCategoryAxisMax = categoryAxisMax;
	}
	
	//设置
	public void setLables(LinkedList<String> labels) {
		this.labels = labels;
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
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(ltrb[0] + DensityUtil.dip2px(this.getContext(), 10),
					ltrb[1], ltrb[2] + DensityUtil.dip2px(this.getContext(), 20),
					ltrb[3]);
			// 标题
			// chart.setTitle("New GitHub repositories");
			// chart.addSubtitle("(XCL-Charts Demo)");
			// chart.getAxisTitle().setLeftTitle("Percentage (annual)");
			// chart.getAxisTitle().getLeftTitlePaint().setColor(Color.BLACK);

			// 数据源
			chart.setCategories(labels);
			chart.setDataSource(chartData);
			chart.setCustomLines(mCustomLineDataset);
			
			System.out.println("spline--mAxisMax--" + mAxisMax + ",mAxisMin--" + mAxisMin + ",mCategoryAxisMax--" + mCategoryAxisMax + ",mCategoryAxisMin--"+mCategoryAxisMin);
			
			chart.getDataAxis().setAxisMax(mAxisMax);
			 chart.getDataAxis().setAxisMin(0);
			// 数据轴刻度间隔
			chart.getDataAxis().setAxisSteps(mAxisMax/2);
			chart.getDataAxis().hideFirstTick();
//			// 标签轴最大值
			chart.setCategoryAxisMax(mCategoryAxisMax);
//			// 标签轴最小值
			chart.setCategoryAxisMin(mCategoryAxisMin);

			// 背景网格
			// PlotGrid plot = chart.getPlotGrid();
			// plot.hideHorizontalLines();
			// plot.hideVerticalLines();
			chart.getDataAxis().getAxisPaint()
					.setColor(Color.rgb(205, 205, 205));
			chart.getCategoryAxis().getAxisPaint()
					.setColor(Color.rgb(205, 205, 205));

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
//			// 隐藏刻度线
			chart.getDataAxis().hideTickMarks();
			chart.getCategoryAxis().hideTickMarks();
			chart.getDataAxis().setTickLabelMargin(25);
			chart.getCategoryAxis().setTickLabelMargin(35);

			// chart.getCategoryAxis().setLabelLineFeed(XEnum.LabelLineFeed.ODD_EVEN);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataSet() {

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
		CustomLineData s = new CustomLineData("", 15d,
				Color.rgb(54, 141, 238), 3);

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

}
