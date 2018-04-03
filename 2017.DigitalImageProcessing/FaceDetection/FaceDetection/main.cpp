//-*- 人脸检测 -*-  
//-*- author : witt -*-

#include <opencv.hpp> 
using namespace std;
using namespace cv;

CascadeClassifier faceDetection;		//实例化特征分类器对象

int main(){
	vector<Rect> faces;

	bool xmlfound = faceDetection.load("haarcascade_frontalface_alt2.xml");
	if (!xmlfound){
		printf("没有找到该级联特征分类器！请检查路径\n");
		return -1;
	}

	Mat img = imread("Lenna.jpg");
	Mat imgGray;
	if (img.empty())
		printf("工程目录下没有找到该图片!");

	cvtColor(img, imgGray, CV_RGB2GRAY);	//转为灰度图
	//equalizeHist(imgGray, imgGray);   //直方图均衡化

	faceDetection.detectMultiScale(imgGray,     //const Mat& image,待检测图片，一般为灰度图像加快检测速度；
		faces,    //CV_OUT vector<Rect>& objects,被检测物体的矩形框向量组；
		1.1,    //double scaleFactor = 1.1,表示在前后两次相继的扫描中，搜索窗口的比例系数。默认为1.1即每次搜索窗口依次扩大10%;
		3,    /*int minNeighbors = 3,表示构成检测目标的相邻矩形的最小个数(默认为3个)。
				如果组成检测目标的小矩形的个数和小于 min_neighbors - 1 都会被排除。
				如果min_neighbors 为 0, 则函数不做任何操作就返回所有的被检候选矩形框，
				这种设定值一般用在用户自定义对检测结果的组合程序上；*/
		0,    /*int flags = 0,要么使用默认值，要么使用CV_HAAR_DO_CANNY_PRUNING，如果设置为
				CV_HAAR_DO_CANNY_PRUNING，那么函数将会使用Canny边缘检测来排除边缘过多或过少的区域，
				因此这些区域通常不会是人脸所在区域；*/
		Size(0, 0)    //minSize和maxSize用来限制得到的目标区域的范围。
		);
	if (faces.size() > 0)
	{
		for (int i = 0; i < faces.size(); i++)
		{
			rectangle(img, Point(faces[i].x, faces[i].y), 
				Point(faces[i].x + faces[i].width, faces[i].y + faces[i].height),
				Scalar(0, 0, 255), 2, 8);	/*框出人脸位置,rectangle(Mat& img, Point pt1,Point pt2,
											const Scalar& color, int thickness=1, int lineType=8, int shift=0)
											img图像。pt1矩形的一个顶点。pt2矩形对角线上的另一个顶点。
											color线条颜色 (RGB) 或亮度（灰度图像 ）(grayscale image）。
											thickness组成矩形的线条的粗细程度。取负值时（如 CV_FILLED）函数绘制填充了色彩的矩形。
											line_type线条的类型，见cvLine的描述。
											shift坐标点的小数点位数。*/
		}
	}

	imshow("人脸检测结果", img);
	waitKey(0);

	return 0;
}

/*
人脸检测算法的可靠性很大程度上依赖于分类器的设计，在2001年，
Viola和Jones两位大牛发表了经典的《Rapid Object Detection using a Boosted Cascade of Simple Features》
和《Robust Real - Time Face Detection》【2】，在AdaBoost算法的基础上，使用Haar - like小波特征和积分图方法进行人脸检测，
他俩不是最早使用提出小波特征的，但是他们设计了针对人脸检测更有效的特征，并对AdaBoost训练出的强分类器进行级联。
这可以说是人脸检测史上里程碑式的一笔了，也因此当时提出的这个算法被称为Viola - Jones检测器。
又过了一段时间，Rainer Lienhart和Jochen Maydt两位大牛将这个检测器进行了扩展【3】，
最终形成了OpenCV现在的Haar分类器。在OpenCV2.0中又扩充了基于LBP特征的人脸检测器，某些情况下LBP特征比Haar来的更为快速
*/

/*
在进行识别时首先通过大量的具有比较明显的haar特征（矩形）的物体图像用模式识别的方法训练出分类器，
分类器是个级联的，每级都以大概相同的识别率保留进入下一级的具有物体特征的候选物体，
而每一级的子分类器则由许多haar特征构成（由积分图像计算得到，并保存下位置），有水平的、竖直的、倾斜的，
并且每个特征带一个阈值和两个分支值，每级子分类器带一个总的阈值。识别物体的时候，同样计算积分图像为后面计算haar特征做准备，
然后采用与训练的时候有物体的窗口同样大小的窗口遍历整幅图像，以后逐渐放大窗口，同样做遍历搜索物体；每当窗口移动到一个位置，
即计算该窗口内的haar特征，加权后与分类器中haar特征的阈值比较从而选择左或者右分支值，累加一个级的分支值与相应级的阈值比较，
大于该阈值才可以通过进入下一轮筛选。当通过分类器所有级的时候说明这个物体以大概率被识别。
*/
