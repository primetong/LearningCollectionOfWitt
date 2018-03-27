#include <opencv2/opencv.hpp>  
#include "highgui/highgui.hpp"    
#include <opencv2/xfeatures2d.hpp>

using namespace cv;
using namespace std;

//计算原始图像点位在经过矩阵变换后在目标图像上对应位置  
Point2f getTransformPoint(const Point2f originalPoint, const Mat &transformMaxtri);

int main(int argc, char *argv[])
{
	Mat image01 = imread("left2.jpg");
	Mat image02 = imread("right2.jpg");

	if (image01.data == NULL || image02.data == NULL)
		return 0;
	imshow("待拼接图像左图", image01);
	imshow("待拼接图像右图", image02);

	//灰度图转换  
	Mat image1, image2;
	cvtColor(image01, image1, CV_RGB2GRAY);
	cvtColor(image02, image2, CV_RGB2GRAY);

	//提取特征点    
	Ptr<Feature2D> f2d = xfeatures2d::SIFT::create();	//修改SIFT参数即可修改算法（比如SURF）
	vector<KeyPoint> keyPoint1, keyPoint2;
	f2d->detect(image1, keyPoint1);
	f2d->detect(image2, keyPoint2);

	//特征点描述，为下边的特征点匹配做准备    
	Mat imageDesc1, imageDesc2;
	f2d->compute(image1, keyPoint1, imageDesc1);
	f2d->compute(image2, keyPoint2, imageDesc2);

	//获得匹配特征点，并提取最优配对     
	FlannBasedMatcher matcher;
	vector<DMatch> matchePoints;
	matcher.match(imageDesc1, imageDesc2, matchePoints, Mat());
	sort(matchePoints.begin(), matchePoints.end()); //特征点排序    
	//获取排在前N个的最优匹配特征点  
	vector<Point2f> imagePoints1, imagePoints2;
	for (int i = 0; i<10; i++)
	{
		imagePoints1.push_back(keyPoint1[matchePoints[i].queryIdx].pt);
		imagePoints2.push_back(keyPoint2[matchePoints[i].trainIdx].pt);
	}

	//获取图像1到图像2的投影映射矩阵，尺寸为3*3  
	Mat homo = findHomography(imagePoints1, imagePoints2, CV_RANSAC);
	Mat adjustMat = (Mat_<double>(3, 3) << 1.0, 0, image01.cols, 0, 1.0, 0, 0, 0, 1.0);
	Mat adjustHomo = adjustMat*homo;

	//获取最强配对点在原始图像和矩阵变换后图像上的对应位置，用于图像拼接点的定位  
	Point2f originalLinkPoint, targetLinkPoint, basedImagePoint;
	originalLinkPoint = keyPoint1[matchePoints[0].queryIdx].pt;
	targetLinkPoint = getTransformPoint(originalLinkPoint, adjustHomo);
	basedImagePoint = keyPoint2[matchePoints[0].trainIdx].pt;

	//图像配准  
	Mat imageTransform1;
	warpPerspective(image01, imageTransform1, adjustMat*homo, Size(image02.cols + image01.cols + 110, image02.rows));

	//在最强匹配点左侧的重叠区域进行累加，是衔接稳定过渡，消除突变  
	Mat image1Overlap, image2Overlap; //图1和图2的重叠部分     
	image1Overlap = imageTransform1(Rect(Point(targetLinkPoint.x - basedImagePoint.x, 0), Point(targetLinkPoint.x, image02.rows)));
	image2Overlap = image02(Rect(0, 0, image1Overlap.cols, image1Overlap.rows));
	Mat image1ROICopy = image1Overlap.clone();  //复制一份图1的重叠部分  
	for (int i = 0; i<image1Overlap.rows; i++)
	{
		for (int j = 0; j<image1Overlap.cols; j++)
		{
			double weight;
			weight = (double)j / image1Overlap.cols;  //随距离改变而改变的叠加系数  
			image1Overlap.at<Vec3b>(i, j)[0] = (1 - weight)*image1ROICopy.at<Vec3b>(i, j)[0] + weight*image2Overlap.at<Vec3b>(i, j)[0];
			image1Overlap.at<Vec3b>(i, j)[1] = (1 - weight)*image1ROICopy.at<Vec3b>(i, j)[1] + weight*image2Overlap.at<Vec3b>(i, j)[1];
			image1Overlap.at<Vec3b>(i, j)[2] = (1 - weight)*image1ROICopy.at<Vec3b>(i, j)[2] + weight*image2Overlap.at<Vec3b>(i, j)[2];
		}
	}
	Mat ROIMat = image02(Rect(Point(image1Overlap.cols, 0), Point(image02.cols, image02.rows)));  //图2中不重合的部分  
	ROIMat.copyTo(Mat(imageTransform1, Rect(targetLinkPoint.x, 0, ROIMat.cols, image02.rows))); //不重合的部分直接衔接上去  
	namedWindow("拼接结果-SIFT", 0);
	imshow("拼接结果-SIFT", imageTransform1);
	imwrite("拼接结果-SIFT.jpg", imageTransform1);
	waitKey();
	return 0;
}

//计算原始图像点位在经过矩阵变换后在目标图像上对应位置  
Point2f getTransformPoint(const Point2f originalPoint, const Mat &transformMaxtri)
{
	Mat originelP, targetP;
	originelP = (Mat_<double>(3, 1) << originalPoint.x, originalPoint.y, 1.0);
	targetP = transformMaxtri*originelP;
	float x = targetP.at<double>(0, 0) / targetP.at<double>(2, 0);
	float y = targetP.at<double>(1, 0) / targetP.at<double>(2, 0);
	return Point2f(x, y);
}