#include "opencv2/imgproc/imgproc.hpp"  
#include "opencv2/highgui/highgui.hpp"  

using namespace cv;

int main()
{
	//ʹ��LoG��������Ե���
	Mat src, src_gray;
	int kernel_size = 3;
	const char* window_name = "Laplacian-of-Gaussian Edeg Detection";

	src = imread("Lenna.jpg");
	GaussianBlur(src, src, Size(3, 3), 0, 0, BORDER_DEFAULT);	//��ͨ����˹ģ��ȥ����
	cvtColor(src, src_gray, CV_RGB2GRAY);
	namedWindow(window_name, CV_WINDOW_AUTOSIZE);

	Mat dst, abs_dst;
	Laplacian(src_gray, dst, CV_16S, kernel_size);	//ͨ��������˹��������Ե���
	convertScaleAbs(dst, abs_dst);

	imshow(window_name, abs_dst);

	//ʹ���Զ����˲�����Ե���
	//�Զ����˲����� 1  1  1
	//               1 -8  1
	//               1  1  1
	Mat custom_src, custom_gray, Kernel;
	custom_src = imread("Lenna.jpg");
	GaussianBlur(custom_src, custom_src, Size(3, 3), 0, 0, BORDER_DEFAULT);	//��ͨ����˹ģ��ȥ����
	cvtColor(custom_src, custom_gray, CV_RGB2GRAY);
	namedWindow("Custom Filter", CV_WINDOW_AUTOSIZE);

	Kernel = (Mat_<double>(3, 3) << 1, 1, 1, 1, -8, 1, 1, 1, 1);	//�Զ����˲���������Ե���
	Mat custdst, abs_custdst;
	filter2D(custom_gray, custdst, CV_16S, Kernel, Point(-1, -1));
	convertScaleAbs(custdst, abs_custdst);

	imshow("Custom Filter", abs_custdst);
	waitKey(0);

	return 0;
}