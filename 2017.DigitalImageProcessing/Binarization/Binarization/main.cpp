//- 图像的二值化 -  
//- by 陈胃痛 -

//cvThreshold()函数功能：采用Canny方法对图像进行边缘检测
//函数原型：
//void cvThreshold( //函数说明：
//const CvArr* src,			//第一个参数表示输入图像，必须为单通道灰度图。
//CvArr* dst,			//第二个参数表示输出的边缘图像，为单通道黑白图。
//double threshold,			//第三个参数表示阈值
//double max_value,			//第四个参数表示最大值。
//int threshold_type			//第五个参数表示运算方法。
//);
//在OpenCV的imgproc\types_c.h中可以找到运算方法的定义。
///* Threshold types ↓*/
//enum
//{	CV_THRESH_BINARY = 0,  /* value = value > threshold ? max_value : 0       */
//	CV_THRESH_BINARY_INV = 1,  /* value = value > threshold ? 0 : max_value       */
//	CV_THRESH_TRUNC = 2,  /* value = value > threshold ? threshold : value   */
//	CV_THRESH_TOZERO = 3,  /* value = value > threshold ? value : 0           */
//	CV_THRESH_TOZERO_INV = 4,  /* value = value > threshold ? 0 : value           */
//	CV_THRESH_MASK = 7,
//	CV_THRESH_OTSU = 8  /* use Otsu algorithm to choose the optimal threshold value; combine the flag with one of the above CV_THRESH_* values */
//  最后一个是自适应算法取阈值（最大类间方差法），这样前面的第四个参数threshold会无效
//};
#include <opencv.hpp>
using namespace std;

IplImage *g_pGrayImage = NULL;
IplImage *g_pBinaryImage = NULL;
const char *pstrWindowsBinaryTitle = "二值化之后的图像";

void on_trackbar(int val)
{
	// 转为二值图  
	cvThreshold(g_pGrayImage, g_pBinaryImage, val, 255, CV_THRESH_BINARY);
	// 显示二值图  
	cvShowImage(pstrWindowsBinaryTitle, g_pBinaryImage);
}

int main(int argc, char** argv)
{
	const char *pstrWindowsSrcTitle = "原图 - by Lenna";
	const char *pstrWindowsToolBarName = "二值化阈值";

	// 从文件中加载原图  
	IplImage *pSrcImage = cvLoadImage("Lenna.jpg", CV_LOAD_IMAGE_UNCHANGED);

	// 转为灰度图  Gray = R*0.299 + G*0.587 + B*0.114 （通道顺序B->G->R）
	g_pGrayImage = cvCreateImage(cvGetSize(pSrcImage), IPL_DEPTH_8U, 1);
	cvCvtColor(pSrcImage, g_pGrayImage, CV_BGR2GRAY);

	// 创建二值图  
	g_pBinaryImage = cvCreateImage(cvGetSize(g_pGrayImage), IPL_DEPTH_8U, 1);

	// 显示原图  
	cvNamedWindow(pstrWindowsSrcTitle, CV_WINDOW_AUTOSIZE);
	cvShowImage(pstrWindowsSrcTitle, pSrcImage);
	// 创建二值图窗口  
	cvNamedWindow(pstrWindowsBinaryTitle, CV_WINDOW_AUTOSIZE);

	// 滑动条    
	int nThreshold = 63;
	cvCreateTrackbar(pstrWindowsToolBarName, pstrWindowsBinaryTitle, &nThreshold, 254, on_trackbar);

	on_trackbar(63);		//初始阈值的设置，初步调试设为63

	cvWaitKey(0);

	cvDestroyWindow(pstrWindowsSrcTitle);
	cvDestroyWindow(pstrWindowsBinaryTitle);
	cvReleaseImage(&pSrcImage);
	cvReleaseImage(&g_pGrayImage);
	cvReleaseImage(&g_pBinaryImage);
	return 0;
}