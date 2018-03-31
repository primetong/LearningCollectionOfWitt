//preprocessing helper  
//负责返回文件夹内所有图片（read_img），检测人脸（detectAndDraw并可以在原图中画出），截图（CutImg），提取(DetectandExtract)
//@ Author : witt

#include "opencv2/core/core.hpp"  
#include "opencv2/highgui/highgui.hpp"  
#include "opencv2/contrib/contrib.hpp"  
#include <cv.h>  
#include <vector>  
#include <utility>  
using namespace cv;
using namespace std;

void detectFaceAndCut(char* dir, int K);

void loadResizeAndTogray(char* dir, int K, vector<Mat> &images, vector<int> &labels,
	vector<Mat> &testimages, vector<int> &testlabels, String train_or_predict);

void trainresizeandtogray(char* dir, int K, vector<Mat> &images, vector<int> &labels);
void predictresizeandtogray(char* dir, int K, vector<Mat> &images, vector<int> &labels);

Ptr<FaceRecognizer> Recognition(vector<Mat> images, vector<int> labels,
	vector<Mat> testimages, vector<int> testlabels);

void normalizeone(const char* dir, IplImage* standard);

void CutImg(IplImage* src, CvRect rect, IplImage* res);

vector<Rect> detectAndDraw(Mat& img, CascadeClassifier& cascade,
	CascadeClassifier& nestedCascade,
	double scale, bool tryflip, bool draw);

IplImage* DetectandExtract(Mat& img, CascadeClassifier& cascade,
	CascadeClassifier& nestedCascade,
	double scale, bool tryflip);

int read_img(const string& dir, vector<Mat> &images);

vector<pair<char*, Mat>>  read_img(const string& dir);