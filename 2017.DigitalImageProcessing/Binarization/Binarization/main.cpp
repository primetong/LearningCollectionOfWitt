//- ͼ��Ķ�ֵ�� -  
//- by ��θʹ -

//cvThreshold()�������ܣ�����Canny������ͼ����б�Ե���
//����ԭ�ͣ�
//void cvThreshold( //����˵����
//const CvArr* src,			//��һ��������ʾ����ͼ�񣬱���Ϊ��ͨ���Ҷ�ͼ��
//CvArr* dst,			//�ڶ���������ʾ����ı�Եͼ��Ϊ��ͨ���ڰ�ͼ��
//double threshold,			//������������ʾ��ֵ
//double max_value,			//���ĸ�������ʾ���ֵ��
//int threshold_type			//�����������ʾ���㷽����
//);
//��OpenCV��imgproc\types_c.h�п����ҵ����㷽���Ķ��塣
///* Threshold types ��*/
//enum
//{	CV_THRESH_BINARY = 0,  /* value = value > threshold ? max_value : 0       */
//	CV_THRESH_BINARY_INV = 1,  /* value = value > threshold ? 0 : max_value       */
//	CV_THRESH_TRUNC = 2,  /* value = value > threshold ? threshold : value   */
//	CV_THRESH_TOZERO = 3,  /* value = value > threshold ? value : 0           */
//	CV_THRESH_TOZERO_INV = 4,  /* value = value > threshold ? 0 : value           */
//	CV_THRESH_MASK = 7,
//	CV_THRESH_OTSU = 8  /* use Otsu algorithm to choose the optimal threshold value; combine the flag with one of the above CV_THRESH_* values */
//  ���һ��������Ӧ�㷨ȡ��ֵ�������䷽���������ǰ��ĵ��ĸ�����threshold����Ч
//};
#include <opencv.hpp>
using namespace std;

IplImage *g_pGrayImage = NULL;
IplImage *g_pBinaryImage = NULL;
const char *pstrWindowsBinaryTitle = "��ֵ��֮���ͼ��";

void on_trackbar(int val)
{
	// תΪ��ֵͼ  
	cvThreshold(g_pGrayImage, g_pBinaryImage, val, 255, CV_THRESH_BINARY);
	// ��ʾ��ֵͼ  
	cvShowImage(pstrWindowsBinaryTitle, g_pBinaryImage);
}

int main(int argc, char** argv)
{
	const char *pstrWindowsSrcTitle = "ԭͼ - by Lenna";
	const char *pstrWindowsToolBarName = "��ֵ����ֵ";

	// ���ļ��м���ԭͼ  
	IplImage *pSrcImage = cvLoadImage("Lenna.jpg", CV_LOAD_IMAGE_UNCHANGED);

	// תΪ�Ҷ�ͼ  Gray = R*0.299 + G*0.587 + B*0.114 ��ͨ��˳��B->G->R��
	g_pGrayImage = cvCreateImage(cvGetSize(pSrcImage), IPL_DEPTH_8U, 1);
	cvCvtColor(pSrcImage, g_pGrayImage, CV_BGR2GRAY);

	// ������ֵͼ  
	g_pBinaryImage = cvCreateImage(cvGetSize(g_pGrayImage), IPL_DEPTH_8U, 1);

	// ��ʾԭͼ  
	cvNamedWindow(pstrWindowsSrcTitle, CV_WINDOW_AUTOSIZE);
	cvShowImage(pstrWindowsSrcTitle, pSrcImage);
	// ������ֵͼ����  
	cvNamedWindow(pstrWindowsBinaryTitle, CV_WINDOW_AUTOSIZE);

	// ������    
	int nThreshold = 63;
	cvCreateTrackbar(pstrWindowsToolBarName, pstrWindowsBinaryTitle, &nThreshold, 254, on_trackbar);

	on_trackbar(63);		//��ʼ��ֵ�����ã�����������Ϊ63

	cvWaitKey(0);

	cvDestroyWindow(pstrWindowsSrcTitle);
	cvDestroyWindow(pstrWindowsBinaryTitle);
	cvReleaseImage(&pSrcImage);
	cvReleaseImage(&g_pGrayImage);
	cvReleaseImage(&g_pBinaryImage);
	return 0;
}